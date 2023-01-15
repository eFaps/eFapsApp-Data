/*
 * Copyright 2003 - 2023 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.efaps.esjp.data.jaxb;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.esjp.data.IIdentifier;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EFapsUUID("543dbab9-768d-4ccf-bac5-e95517891124")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "identifier", namespace = "http://www.efaps.org/xsd")
@XmlType(name = "identifier", namespace = "http://www.efaps.org/xsd")
public class IdentifierDef
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(IdentifierDef.class);

    /**
     * Class used for validation.
     */
    @XmlAttribute(name = "class")
    private String className;

    /**
     * List of Properties.
     */
    @XmlElementRef
    private List<PropertyDef> properties;

    /**
     * Getter method for the instance variable {@link #className}.
     *
     * @return value of instance variable {@link #className}
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Setter method for instance variable {@link #className}.
     *
     * @param _className value for instance variable {@link #className}
     */
    public void setClassName(final String _className)
    {
        className = _className;
    }

    public Boolean hasInstance(final Parameter _parameter,
                               final Definition _definition,
                               final Map<String, Integer> _headers,
                               final String[] _value,
                               final Integer _idx)
    {
        Boolean ret = false;
        try {
            final Class<?> clazz = Class.forName(getClassName());
            final IIdentifier identifier = (IIdentifier) clazz.getConstructor().newInstance();
            ret = identifier.hasInstance(_parameter, _definition, _headers, _value, _idx);
        } catch (final EFapsException | ClassNotFoundException | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                        | SecurityException e) {
            IdentifierDef.LOG.error("Catched error on value evaluation", e);
        }
        return ret;
    }

    public Instance getInstance(final Parameter _parameter,
                                final Definition _definition,
                                final Map<String, Integer> _headers,
                                final String[] _value,
                                final Integer _idx)
    {
        Instance ret = null;
        try {
            final Class<?> clazz = Class.forName(getClassName());
            final IIdentifier identifier = (IIdentifier) clazz.getConstructor().newInstance();
            ret = identifier.getInstance(_parameter, _definition, _headers, _value, _idx);
        } catch (final EFapsException | ClassNotFoundException | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                        | SecurityException e) {
            IdentifierDef.LOG.error("Catched error on value evaluation", e);
        }
        return ret;
    }

    /**
     * Getter method for the instance variable {@link #properties}.
     *
     * @return value of instance variable {@link #properties}
     */
    public List<PropertyDef> getProperties()
    {
        return properties;
    }

    /**
     * @param _name name of the property
     * @return value for the property
     */
    public String getProperty(final String _name)
    {
        String ret = null;
        for (final PropertyDef def : properties) {
            if (_name.equals(def.getName())) {
                ret = def.getValue();
                break;
            }
        }
        return ret;
    }

    /**
     * @param _name name of the property
     * @return array of values for the property
     */
    public String[] analyseProperty(final String _name)
    {
        final List<String> ret = new ArrayList<>();
        // search for base name
        String valTmp = getProperty(_name);
        if (valTmp != null) {
            ret.add(valTmp);
        }
        for (int i = 1; i < 100; i++) {
            final String nameTmp = _name + String.format("%02d", i);
            valTmp = getProperty(nameTmp);
            if (valTmp == null) {
                break;
            } else {
                ret.add(valTmp);
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

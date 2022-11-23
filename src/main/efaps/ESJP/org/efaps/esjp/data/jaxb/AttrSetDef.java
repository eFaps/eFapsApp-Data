/*
 * Copyright 2003 - 2022 The eFaps Team
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
import org.efaps.esjp.data.IOnInstance;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EFapsUUID("574a5ebd-cb7f-4763-8949-4625e5e90b9c")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "attribute-set", namespace = "http://www.efaps.org/xsd")
@XmlType(name = "attribute-set", namespace = "http://www.efaps.org/xsd")
public class AttrSetDef
    extends AbstractDef
{

    private static final Logger LOG = LoggerFactory.getLogger(AttrSetDef.class);

    @XmlAttribute(name = "class")
    private String className;

    /**
     * List of Properties.
     */
    @XmlElementRef
    private List<PropertyDef> properties;

    public String getClassName()
    {
        return className;
    }

    public void setClassName(final String _className)
    {
        className = _className;
    }

    public List<PropertyDef> getProperties()
    {
        return properties;
    }

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

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    public void execute(final Parameter _parameter, final Instance instance, final Map<String, Integer> headers,
                        final String[] values, final Integer idx)
        throws EFapsException
    {
        try {
            final var clazz = Class.forName(getClassName());
            final IOnInstance onInstance = (IOnInstance) clazz.getConstructor().newInstance();
            onInstance.run(_parameter, this, instance, headers, values, idx);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            LOG.error("Catched", e);
        }

    }
}

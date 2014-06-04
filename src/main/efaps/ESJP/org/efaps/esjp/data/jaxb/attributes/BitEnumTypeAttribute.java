/*
 * Copyright 2003 - 2014 The eFaps Team
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
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.esjp.data.jaxb.attributes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.program.esjp.EFapsClassLoader;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("fcc1b9fa-6b09-4e80-9874-811190c1dea2")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "bitenum", namespace = "http://www.efaps.org/xsd")
public class BitEnumTypeAttribute
    extends AbstractEFapsAttribute<BitEnumTypeAttribute>
{

    /**
     * UUID of the Dimension this UoM belongs to.
     */

    @XmlElementWrapper(name = "values", namespace = "http://www.efaps.org/xsd")
    @XmlElement(name = "value")
    private final List<String> values = new ArrayList<String>();

    @Override
    public BitEnumTypeAttribute evalAttrValue(final Attribute _attribute,
                                              final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue != null && _dbValue instanceof List) {
            for (final Object obj : (List<?>) _dbValue) {
                this.values.add(obj.toString());
            }
        }
        return this;
    }

    @Override
    protected BitEnumTypeAttribute getThis()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        if (!getValues().isEmpty()) {
            final List<Object> objValues = new ArrayList<Object>();
            try {
                final Type type = _insert.getInstance().getType();
                final Attribute attribute = type.getAttribute(getAttrName());
                final Class<?> clazz = Class.forName(attribute.getClassName(), false, EFapsClassLoader.getInstance());
                if (clazz.isEnum()) {
                    final Object[] consts = clazz.getEnumConstants();
                    for (final Object cons : consts) {
                        for (final String value : getValues()) {
                            if (value.equals(cons.toString())) {
                                objValues.add(cons);
                            }
                        }
                    }
                }
            } catch (final ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!objValues.isEmpty()) {
                _insert.add(getAttrName(), objValues.toArray());
            }
        }
    }

    /**
     * Getter method for the instance variable {@link #values}.
     *
     * @return value of instance variable {@link #values}
     */
    public List<String> getValues()
    {
        return this.values;
    }
}

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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
@XmlRootElement(name = "enum", namespace = "http://www.efaps.org/xsd")
public class EnumTypeAttribute
    extends AbstractEFapsAttribute<EnumTypeAttribute>
{

    /**
     * Value of this enum.
     */
    @XmlAttribute(required = true)
    private String value;

    @Override
    public EnumTypeAttribute evalAttrValue(final Attribute _attribute,
                                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue != null) {
           this.value = _dbValue.toString();
        }
        return this;
    }

    @Override
    protected EnumTypeAttribute getThis()
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
        if (this.value != null) {
            try {
                final Type type = _insert.getInstance().getType();
                final Attribute attribute = type.getAttribute(getAttrName());
                final Class<?> clazz = Class.forName(attribute.getClassName(), false, EFapsClassLoader.getInstance());
                if (clazz.isEnum()) {
                    final Object[] consts = clazz.getEnumConstants();
                    for (final Object cons : consts) {
                        if (this.value.equals(cons.toString())) {
                            _insert.add(getAttrName(), cons);
                            break;
                        }
                    }
                }
            } catch (final ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

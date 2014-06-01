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
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.util.EFapsException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("d1f3cc0a-85ed-4833-8801-02fe37aa3ec4")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "boolean", namespace = "http://www.efaps.org/xsd")
public class BooleanTypeAttribute
    extends AbstractEFapsAttribute<BooleanTypeAttribute>
{
    /**
     * Name of the Attribute.
     */
    @XmlAttribute(required = true)
    public Boolean value;

    /**
     * Setter method for instance variable {@link #value}.
     *
     * @param _value value for instance variable {@link #value}
     */
    public BooleanTypeAttribute setValue(final Boolean _value)
    {
        this.value = _value;
        return getThis();
    }


    @Override
    protected BooleanTypeAttribute getThis()
    {
        return this;
    }

    /**
     * Getter method for the instance variable {@link #value}.
     *
     * @return value of instance variable {@link #value}
     */
    public Boolean getValue()
    {
        return this.value;
    }

    @Override
    public BooleanTypeAttribute evalAttrValue(final Attribute _attribute,
                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        setValue((Boolean) _dbValue);
        return getThis();
    }
}

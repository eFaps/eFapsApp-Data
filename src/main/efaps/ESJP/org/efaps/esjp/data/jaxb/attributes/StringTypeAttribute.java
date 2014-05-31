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
import javax.xml.bind.annotation.XmlElement;
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
@EFapsUUID("3d1a855c-c166-4507-bfbc-6ffb9623c591")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "string", namespace = "http://www.efaps.org/xsd")
public class StringTypeAttribute
    extends AbstractEFapsAttribute<StringTypeAttribute>
{

    @XmlElement
    private String value;

    @Override
    protected StringTypeAttribute getThis()
    {
        return this;
    }

    /**
     * Getter method for the instance variable {@link #value}.
     *
     * @return value of instance variable {@link #value}
     */
    public String getValue()
    {
        return this.value;
    }

    /**
     * Setter method for instance variable {@link #value}.
     *
     * @param _value value for instance variable {@link #value}
     */
    public StringTypeAttribute setValue(final String _value)
    {
        this.value = _value;
        return this;
    }

    @Override
    public StringTypeAttribute evalAttrValue(final Attribute _attribute,
                                             final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        setValue(String.valueOf(_dbValue));
        return this;
    }
}

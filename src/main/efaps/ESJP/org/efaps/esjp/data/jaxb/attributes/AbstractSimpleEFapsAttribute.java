/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.esjp.data.jaxb.attributes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("b34f1961-3e5f-4199-9ea6-9cc6d5e10d93")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractSimpleEFapsAttribute<T extends AbstractSimpleEFapsAttribute<T, S>, S>
    extends AbstractEFapsAttribute<T>
{
    /**
     * Name of the Attribute.
     */
    @XmlAttribute(required = true)
    public String value;

    public abstract S getValue();

    /**
     * Setter method for instance variable {@link #value}.
     *
     * @param _value value for instance variable {@link #value}
     */
    public T setValue(final S _value)
    {
        this.value = _value.toString();
        return getThis();
    }

    /**
     * Getter method for the instance variable {@link #value}.
     *
     * @return value of instance variable {@link #value}
     */
    protected String getValueInternal()
    {
        return this.value;
    }
}

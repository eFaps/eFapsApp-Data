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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.esjp.data.jaxb.AbstractEFapsObject;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("e7a3acbb-66c0-49de-914a-4d599c662c59")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractEFapsAttribute<T extends AbstractEFapsAttribute<T>>
{

    /**
     * Name of the Attribute.
     */
    @XmlAttribute(name = "aname", required = true)
    private String attrName;

    protected abstract T getThis();

    /**
     * Getter method for the instance variable {@link #name}.
     *
     * @return value of instance variable {@link #name}
     */
    public String getAttrName()
    {
        return this.attrName;
    }

    /**
     * Setter method for instance variable {@link #name}.
     *
     * @param _name value for instance variable {@link #name}
     */
    public T setAttrName(final String _attrName)
    {
        this.attrName = _attrName;
        return getThis();
    }

    public T evalAttrValue(final Attribute _attribute,
                           final Object _dbValue)
        throws EFapsException
    {
        setAttrName(_attribute.getName());
        return getThis();
    }

    /**
     * @return
     */
    public boolean isLink()
    {
        return false;
    }

    public AbstractEFapsObject<?> getLink()
    {
        return null;
    }

    /**
     * @param _insert Insert 2 add to
     * @throws EFapsException on error
     */
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        // do not add as a default behavior
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

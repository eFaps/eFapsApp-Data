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

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.admin.user.AbstractUserObject;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("0d746733-c72b-4145-a0fc-94c34f24fedb")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractPersonEFapsAttribute<T extends AbstractPersonEFapsAttribute<T>>
    extends AbstractEFapsAttribute<T>
{

    @XmlAttribute()
    private UUID uuid;

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute(required = true)
    private Long id;

    @Override
    public T evalAttrValue(final Attribute _attribute,
                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);

        if (_dbValue instanceof AbstractUserObject) {
            final AbstractUserObject user = (AbstractUserObject) _dbValue;
            setUUID(user.getUUID()).setID(user.getId()).setName(user.getName());
        }
        return getThis();
    }

    /**
     * Getter method for the instance variable {@link #uuid}.
     *
     * @return value of instance variable {@link #uuid}
     */
    public UUID getUuid()
    {
        return this.uuid;
    }

    /**
     * Setter method for instance variable {@link #uuid}.
     *
     * @param _uuid value for instance variable {@link #uuid}
     */
    public T setUUID(final UUID _uuid)
    {
        this.uuid = _uuid;
        return getThis();
    }

    /**
     * Getter method for the instance variable {@link #personName}.
     *
     * @return value of instance variable {@link #personName}
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter method for instance variable {@link #personName}.
     *
     * @param _personName value for instance variable {@link #personName}
     */
    public T setName(final String _personName)
    {
        this.name = _personName;
        return getThis();
    }

    /**
     * Getter method for the instance variable {@link #personID}.
     *
     * @return value of instance variable {@link #personID}
     */
    public Long getID()
    {
        return this.id;
    }

    /**
     * Setter method for instance variable {@link #personID}.
     *
     * @param _personID value for instance variable {@link #personID}
     */
    public T setID(final Long _personID)
    {
        this.id = _personID;
        return getThis();
    }
}

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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.Status;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("10778d5a-3953-4316-be5f-82dc0a03884b")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "status", namespace = "http://www.efaps.org/xsd")
public class StatusTypeAttribute
    extends AbstractEFapsAttribute<StatusTypeAttribute>
{

    @XmlAttribute(required = true)
    private UUID uuid;

    @XmlAttribute(required = true)
    private String key;

    @Override
    protected StatusTypeAttribute getThis()
    {
        return this;
    }

    /**
     * Getter method for the instance variable {@link #value}.
     *
     * @return value of instance variable {@link #value}
     */
    public UUID getUUID()
    {
        return this.uuid;
    }

    /**
     * Setter method for instance variable {@link #value}.
     *
     * @param _value value for instance variable {@link #value}
     */
    public StatusTypeAttribute setUUID(final UUID _uuid)
    {
        this.uuid = _uuid;
        return this;
    }

    @Override
    public StatusTypeAttribute evalAttrValue(final Attribute _attribute,
                                             final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue instanceof Long) {
            final Status status = Status.get((long) _dbValue);
            setUUID(status.getStatusGroup().getUUID()).setKey(status.getKey());
        }
        return this;
    }


    /**
     * Getter method for the instance variable {@link #key}.
     *
     * @return value of instance variable {@link #key}
     */
    public String getKey()
    {
        return this.key;
    }


    /**
     * Setter method for instance variable {@link #key}.
     *
     * @param _key value for instance variable {@link #key}
     */
    public StatusTypeAttribute setKey(final String _key)
    {
        this.key = _key;
        return this;
    }

    /**
     * @param _insert
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        _insert.add(getAttrName(), Status.find(getUUID(), getKey()));
    }
}

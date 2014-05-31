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

package org.efaps.esjp.data.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.esjp.data.jaxb.attributes.AbstractEFapsAttribute;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("ef002a77-4143-4f1f-b508-c4d4c09cb885")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractEFapsObject<T extends AbstractEFapsObject<T>>
{

    /**
     * UUID of the instance type.
     */
    @XmlAttribute(required = true)
    private UUID typeUUID;

    /**
     * ID of the instance.
     */
    @XmlAttribute(required = true)
    private long id;

    /**
     * External id of the instance.
     */
    @XmlAttribute(required = true)
    private long exId;

    /**
     * External System id of the instance.
     */
    @XmlAttribute(required = true)
    private long exSysId;

    /**
     * The list of attributes belonging to this object.
     */
    @XmlElementWrapper(name="attributes")
    @XmlElementRef
    private final List<AbstractEFapsAttribute<?>> attributes = new ArrayList<AbstractEFapsAttribute<?>>();

    /**
     * @param _instance
     */
    public AbstractEFapsObject(final Instance _instance)
        throws EFapsException
    {
        this.typeUUID = _instance.getTypeUUID();
        this.id = _instance.getId();
        this.exId = _instance.getExchangeId();
        this.exSysId = _instance.getExchangeSystemId();
    }

    /**
     *
     */
    public AbstractEFapsObject()
    {
    }

    /**
     * Getter method for the instance variable {@link #typeUUID}.
     *
     * @return value of instance variable {@link #typeUUID}
     */
    public UUID getTypeUUID()
    {
        return this.typeUUID;
    }

    /**
     * Setter method for instance variable {@link #typeUUID}.
     *
     * @param _typeUUID value for instance variable {@link #typeUUID}
     */
    public T setTypeUUID(final UUID _typeUUID)
    {
        this.typeUUID = _typeUUID;
        return getThis();
    }


    /**
     * Getter method for the instance variable {@link #id}.
     *
     * @return value of instance variable {@link #id}
     */
    public long getId()
    {
        return this.id;
    }


    /**
     * Setter method for instance variable {@link #id}.
     *
     * @param _id value for instance variable {@link #id}
     */
    public T setId(final long _id)
    {
        this.id = _id;
        return getThis();
    }


    /**
     * Getter method for the instance variable {@link #exId}.
     *
     * @return value of instance variable {@link #exId}
     */
    public long getExId()
    {
        return this.exId;
    }


    /**
     * Setter method for instance variable {@link #exId}.
     *
     * @param _exId value for instance variable {@link #exId}
     */
    public T setExId(final long _exId)
    {
        this.exId = _exId;
        return getThis();
    }


    /**
     * Getter method for the instance variable {@link #exSysId}.
     *
     * @return value of instance variable {@link #exSysId}
     */
    public long getExSysId()
    {
        return this.exSysId;
    }


    /**
     * Setter method for instance variable {@link #exSysId}.
     *
     * @param _exSysId value for instance variable {@link #exSysId}
     */
    public T setExSysId(final long _exSysId)
    {
        this.exSysId = _exSysId;
        return getThis();
    }


    /**
     * Getter method for the instance variable {@link #attributes}.
     *
     * @return value of instance variable {@link #attributes}
     */
    public List<AbstractEFapsAttribute<?>> getAttributes()
    {
        return this.attributes;
    }

    public void addAttribute(final AbstractEFapsAttribute<?> _attribute)
    {
        if (_attribute != null) {
            this.attributes.add(_attribute);
        }
    }

    protected abstract T getThis();
}

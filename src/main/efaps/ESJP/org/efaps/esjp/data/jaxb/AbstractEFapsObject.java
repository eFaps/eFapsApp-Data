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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.ci.CIAdminCommon;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.db.InstanceQuery;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.PrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.Update;
import org.efaps.esjp.data.jaxb.attributes.AbstractEFapsAttribute;
import org.efaps.esjp.data.jaxb.attributes.EFapsAttributes;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 * @param <T> Object that extends this class to be able to use chaining
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
     * External System id of the instance.
     */
    @XmlAttribute(required = true)
    private long generalId;

    /**
     * The list of attributes belonging to this object.
     */
    @XmlElementWrapper(name = "attributes", namespace = "http://www.efaps.org/xsd")
    @XmlElementRef
    private final List<AbstractEFapsAttribute<?>> attributes = new ArrayList<AbstractEFapsAttribute<?>>();

    /**
     * @param _instance instance this EfapsObject is based on
     * @throws EFapsException on error
     */
    public AbstractEFapsObject(final Instance _instance)
        throws EFapsException
    {
        this.typeUUID = _instance.getTypeUUID();
        this.id = _instance.getId();
        this.exId = _instance.getExchangeId();
        this.exSysId = _instance.getExchangeSystemId();
        this.generalId = _instance.getGeneralId();
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
     * @return this for chaining
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
     * @return this for chaining
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
     * @return this for chaining
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
     * @return this for chaining
     */
    public T setExSysId(final long _exSysId)
    {
        this.exSysId = _exSysId;
        return getThis();
    }

    /**
     * Getter method for the instance variable {@link #generalId}.
     *
     * @return value of instance variable {@link #generalId}
     */
    public long getGeneralId()
    {
        return this.generalId;
    }

    /**
     * Setter method for instance variable {@link #generalId}.
     *
     * @param _generalId value for instance variable {@link #generalId}
     */
    public void setGeneralId(final long _generalId)
    {
        this.generalId = _generalId;
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

    /**
     * @param _attribute attribute to add
     */
    public void addAttribute(final AbstractEFapsAttribute<?> _attribute)
    {
        if (_attribute != null) {
            this.attributes.add(_attribute);
        }
    }

    /**
     * Method used to be able to use type safe chaining methods.
     * @return this object
     */
    protected abstract T getThis();

    /**
     * @return the instance for this object.
     */
    public Instance getInstance()
    {
        return Instance.get(getTypeUUID(), getId());
    }

    /**
     * @param _maxDepth     maximum depth the objects will be followed through
     * @param _instances    instances that are already nested in this object
     * @throws EFapsException on error
     */
    public void load(final int _maxDepth,
                     final Set<Instance> _instances)
        throws EFapsException
    {
        final Type type = Type.get(getTypeUUID());
        final PrintQuery print = new PrintQuery(getInstance());
        for (final Attribute attribute : type.getAttributes().values()) {
            EFapsAttributes.add2Print(print, attribute);
        }
        print.execute();
        for (final Attribute attribute : type.getAttributes().values()) {
            final Object value = EFapsAttributes.get4Print(print, attribute);
            addAttribute(EFapsAttributes.getAttribute(attribute, value));
        }

        for (final AbstractEFapsAttribute<?> attr : getAttributes()) {
            if (attr.isLink()) {
                final AbstractEFapsObject<?> object = attr.getLink();
                if (object != null) {
                    if (!_instances.contains(object.getInstance())) {
                        final Set<Instance> inst = new HashSet<Instance>();
                        inst.addAll(_instances);
                        inst.add(getInstance());
                        if (inst.size() < _maxDepth) {
                            object.load(_maxDepth, inst);
                        }
                    }
                }
            }
        }
    }

    /**
     * @return the instance created or found in the system
     * @throws EFapsException on error
     */
    public Instance create()
        throws EFapsException
    {
        Instance ret = null;
        // Search a general instance that is the searched object
        // 1. Search for an object that was imported from
        //    the same system by searching by ExchangeSystemID and ExchangeID
        final QueryBuilder queryBldr = new QueryBuilder(CIAdminCommon.GeneralInstance);
        queryBldr.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.ExchangeSystemID, getExSysId());
        queryBldr.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.ExchangeID, getGeneralId());
        final MultiPrintQuery multi = queryBldr.getPrint();
        multi.addAttribute(CIAdminCommon.GeneralInstance.InstanceID, CIAdminCommon.GeneralInstance.InstanceTypeID);
        multi.executeWithoutAccessCheck();
        if (multi.next()) {
            ret = Instance.get(Type.get(multi.<Long>getAttribute(CIAdminCommon.GeneralInstance.InstanceTypeID)),
                            multi.<Long>getAttribute(CIAdminCommon.GeneralInstance.InstanceID));
        } else {
            // 2. check if the systemid is the same (e.g. meaning that it is an manual import)
            //    and that it was not an imported object
            final QueryBuilder queryBldr3 = new QueryBuilder(CIAdminCommon.GeneralInstance);
            queryBldr3.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.ExchangeSystemID, getExSysId());
            queryBldr3.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.ID, getGeneralId());
            queryBldr3.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.InstanceTypeID,
                            Type.get(getTypeUUID()).getId());
            queryBldr3.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.InstanceID, getId());
            queryBldr3.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.ExchangeID, 0);
            final MultiPrintQuery multi3 = queryBldr3.getPrint();
            multi3.addAttribute(CIAdminCommon.GeneralInstance.InstanceID, CIAdminCommon.GeneralInstance.InstanceTypeID);
            multi3.executeWithoutAccessCheck();
            if (multi3.next()) {
                ret = Instance.get(Type.get(multi3.<Long>getAttribute(CIAdminCommon.GeneralInstance.InstanceTypeID)),
                                multi3.<Long>getAttribute(CIAdminCommon.GeneralInstance.InstanceID));
            } else {
                final Insert insert = new Insert(getTypeUUID());
                // check if all links exists etc.
                for (final AbstractEFapsAttribute<?> attribute : getAttributes()) {
                    attribute.add2Insert(insert);
                }
                insert.execute();
                ret = insert.getInstance();

                final QueryBuilder queryBldr2 = new QueryBuilder(CIAdminCommon.GeneralInstance);
                queryBldr2.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.InstanceID, ret);
                queryBldr2.addWhereAttrEqValue(CIAdminCommon.GeneralInstance.InstanceTypeID, ret.getType().getId());
                final InstanceQuery query = queryBldr2.getQuery();
                query.executeWithoutAccessCheck();
                if (query.next()) {
                    final Update update = new Update(query.getCurrentValue());
                    update.add(CIAdminCommon.GeneralInstance.ExchangeSystemID, getExSysId());
                    update.add(CIAdminCommon.GeneralInstance.ExchangeID, getGeneralId());
                    update.executeWithoutTrigger();
                }
            }
        }
        return ret;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("typeUUID", this.typeUUID)
            .append("id", this.id)
            .append("exId", this.exId)
            .append("exSysId", this.exSysId)
            .append("generalId", this.generalId)
            .build();
    }
}

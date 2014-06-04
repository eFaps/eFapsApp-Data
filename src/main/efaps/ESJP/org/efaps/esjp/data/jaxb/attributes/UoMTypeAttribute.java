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
import javax.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.Dimension;
import org.efaps.admin.datamodel.Dimension.UoM;
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
@XmlRootElement(name = "uom", namespace = "http://www.efaps.org/xsd")
public class UoMTypeAttribute
    extends AbstractEFapsAttribute<UoMTypeAttribute>
{

    /**
     * UUID of the Dimension this UoM belongs to.
     */
    @XmlAttribute(required = true, name = "dimuuid")
    private UUID dimUUID;

    /**
     * Id of this UoM.
     */
    @XmlAttribute(required = true)
    private Long id;

    /**
     * Name of this UoM.
     */
    @XmlAttribute(required = true)
    private String name;

    @Override
    public UoMTypeAttribute evalAttrValue(final Attribute _attribute,
                                          final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue != null) {
            final UoM uom = Dimension.getUoM((Long) _dbValue);
            setDimUUID(uom.getDimension().getUUID());
            setId(uom.getId());
            setName(uom.getName());
        }
        return this;
    }

    @Override
    protected UoMTypeAttribute getThis()
    {
        return this;
    }

    /**
     * Getter method for the instance variable {@link #dimUUID}.
     *
     * @return value of instance variable {@link #dimUUID}
     */
    public UUID getDimUUID()
    {
        return this.dimUUID;
    }

    /**
     * Setter method for instance variable {@link #dimUUID}.
     *
     * @param _dimUUID value for instance variable {@link #dimUUID}
     * @return this. for chaining
     */
    public UoMTypeAttribute setDimUUID(final UUID _dimUUID)
    {
        this.dimUUID = _dimUUID;
        return this;
    }


    /**
     * Getter method for the instance variable {@link #id}.
     *
     * @return value of instance variable {@link #id}
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * Setter method for instance variable {@link #id}.
     *
     * @param _id value for instance variable {@link #id}
     * @return this. for chaining
     */
    public UoMTypeAttribute setId(final Long _id)
    {
        this.id = _id;
        return this;
    }

    /**
     * Getter method for the instance variable {@link #name}.
     *
     * @return value of instance variable {@link #name}
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter method for instance variable {@link #name}.
     *
     * @param _name value for instance variable {@link #name}
     * @return this. for chaining
     */
    public UoMTypeAttribute setName(final String _name)
    {
        this.name = _name;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        final UoM uom = Dimension.getUoM(getId());
        if (uom.getName().equals(getName())) {
            _insert.add(getAttrName(), getId());
        }
    }
}

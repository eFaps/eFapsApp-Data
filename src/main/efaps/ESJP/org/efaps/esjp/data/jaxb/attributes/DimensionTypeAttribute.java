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

import java.util.UUID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.Dimension;
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
@EFapsUUID("5c4f7a64-2623-4d1e-bb61-08ae3a81f8b7")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "dimension", namespace = "http://www.efaps.org/xsd")
public class DimensionTypeAttribute
    extends AbstractEFapsAttribute<DimensionTypeAttribute>
{

    /**
     * UUID of the Dimension this UoM belongs to.
     */
    @XmlAttribute(required = true)
    private UUID uuid;

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
    public DimensionTypeAttribute evalAttrValue(final Attribute _attribute,
                                                final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue != null) {
            final Dimension dim = Dimension.get((Long) _dbValue);
            setName(dim.getName());
            setId(dim.getId());
            setUUID(dim.getUUID());
        }
        return this;
    }

    @Override
    protected DimensionTypeAttribute getThis()
    {
        return this;
    }

    /**
     * Getter method for the instance variable {@link #dimUUID}.
     *
     * @return value of instance variable {@link #dimUUID}
     */
    public UUID getUUID()
    {
        return this.uuid;
    }

    /**
     * Setter method for instance variable {@link #dimUUID}.
     *
     * @param _dimUUID value for instance variable {@link #dimUUID}
     * @return this. for chaining
     */
    public DimensionTypeAttribute setUUID(final UUID _dimUUID)
    {
        this.uuid = _dimUUID;
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
    public DimensionTypeAttribute setId(final Long _id)
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
    public DimensionTypeAttribute setName(final String _name)
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
        final Dimension dim = Dimension.get(getUUID());
        if (dim != null) {
            _insert.add(getAttrName(), dim.getId());
        }
    }
}

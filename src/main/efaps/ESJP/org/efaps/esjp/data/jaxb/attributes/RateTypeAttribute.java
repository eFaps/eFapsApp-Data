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

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.datamodel.Attribute;
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
@EFapsUUID("4c62d445-2e64-4c65-a17f-c365e8e92dd1")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "rate", namespace = "http://www.efaps.org/xsd")
public class RateTypeAttribute
    extends AbstractEFapsAttribute<RateTypeAttribute>
{

    /**
     * Numerator.
     */
    @XmlAttribute(required = true)
    private BigDecimal numerator;

    /**
     * Denominator.
     */
    @XmlAttribute(required = true)
    private BigDecimal denominator;

    @Override
    protected RateTypeAttribute getThis()
    {
        return this;
    }

    @Override
    public RateTypeAttribute evalAttrValue(final Attribute _attribute,
                                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue instanceof Object[]) {
            setNumerator((BigDecimal) ((Object[]) _dbValue)[0]).setDenominator((BigDecimal) ((Object[]) _dbValue)[0]);
        }
        return this;
    }

    /**
     * Getter method for the instance variable {@link #numerator}.
     *
     * @return value of instance variable {@link #numerator}
     */
    public BigDecimal getNumerator()
    {
        return this.numerator;
    }

    /**
     * Setter method for instance variable {@link #numerator}.
     *
     * @param _numerator value for instance variable {@link #numerator}
     */
    public RateTypeAttribute setNumerator(final BigDecimal _numerator)
    {
        this.numerator = _numerator;
        return this;
    }

    /**
     * Getter method for the instance variable {@link #denominator}.
     *
     * @return value of instance variable {@link #denominator}
     */
    public BigDecimal getDenominator()
    {
        return this.denominator;
    }

    /**
     * Setter method for instance variable {@link #denominator}.
     *
     * @param _denominator value for instance variable {@link #denominator}
     */
    public RateTypeAttribute setDenominator(final BigDecimal _denominator)
    {
        this.denominator = _denominator;
        return this;
    }

    /**
     * @param _insert
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        _insert.add(getAttrName(), new Object[] { getNumerator(), getDenominator() });
    }

}

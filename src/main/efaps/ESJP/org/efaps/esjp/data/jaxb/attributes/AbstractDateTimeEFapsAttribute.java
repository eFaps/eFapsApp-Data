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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.esjp.data.jaxb.adapter.DateTimeAdapter;
import org.efaps.util.EFapsException;
import org.joda.time.DateTime;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 * @param <T> subclass of this class to allow chaining
 */
@EFapsUUID("be3296f6-32a9-4523-97ba-d644fa87d5d4")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractDateTimeEFapsAttribute<T extends AbstractDateTimeEFapsAttribute<T>>
    extends AbstractEFapsAttribute<T>
{
    /**
     * Name of the Attribute.
     */
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlAttribute
    private DateTime value;

    /**
     * Getter method for the instance variable {@link #value}.
     *
     * @return value of instance variable {@link #value}
     */
    public DateTime getValue()
    {
        return this.value;
    }

    /**
     * Setter method for instance variable {@link #value}.
     *
     * @param _value value for instance variable {@link #value}
     * @return this for chaining
     */
    public T setValue(final DateTime _value)
    {
        this.value = _value;
        return getThis();
    }

    @Override
    public T evalAttrValue(final Attribute _attribute,
                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        setValue((DateTime) _dbValue);
        return getThis();
    }

    /**
     * @param _insert insert to add 2
     * @throws EFapsException on error
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        _insert.add(getAttrName(), getValue());
    }
}

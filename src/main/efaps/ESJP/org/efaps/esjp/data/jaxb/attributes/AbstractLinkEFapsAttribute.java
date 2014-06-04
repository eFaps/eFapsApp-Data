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
import javax.xml.bind.annotation.XmlElement;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.esjp.data.jaxb.AbstractEFapsObject;
import org.efaps.esjp.data.jaxb.EFapsObject;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 * @param <T> childobject used for chaining
 */
@EFapsUUID("a77abd26-af45-4266-973f-ed64c11ca9ab")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractLinkEFapsAttribute<T extends AbstractLinkEFapsAttribute<T>>
    extends AbstractEFapsAttribute<T>
{

    /**
     * Name of the Attribute.
     */
    @XmlElement
    private EFapsObject object;

    @Override
    public T evalAttrValue(final Attribute _attribute,
                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue instanceof Instance && ((Instance) _dbValue).isValid()) {
            setObject(new EFapsObject((Instance) _dbValue));
        }
        return getThis();
    }

    /**
     * Getter method for the instance variable {@link #object}.
     *
     * @return value of instance variable {@link #object}
     */
    public EFapsObject getObject()
    {
        return this.object;
    }

    /**
     * Setter method for instance variable {@link #object}.
     *
     * @param _object value for instance variable {@link #object}
     */
    public void setObject(final EFapsObject _object)
    {
        this.object = _object;
    }

    @Override
    public boolean isLink()
    {
        return true;
    }

    @Override
    public AbstractEFapsObject<?> getLink()
    {
        return getObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        if (getLink() != null) {
            _insert.add(getAttrName(), getLink().create());
        }
    }
}

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
import jakarta.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.Type;
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
@EFapsUUID("247aa35d-da2e-454d-aa52-477ec361c4e8")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "long", namespace = "http://www.efaps.org/xsd")
public class LongTypeAttribute
    extends AbstractSimpleEFapsAttribute<LongTypeAttribute, Long>
{

    @Override
    protected LongTypeAttribute getThis()
    {
        return this;
    }

    @Override
    public Long getValue()
    {
        return Long.valueOf(getValueInternal());
    }

    @Override
    public LongTypeAttribute evalAttrValue(final Attribute _attribute,
                                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        setValue((Long) _dbValue);
        return this;
    }

    /**
     * @param _insert
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        final Type type = _insert.getInstance().getType();
        // check that it is not the id column
        if (!type.getAttribute(getAttrName()).getSqlColNames()
                        .contains(_insert.getInstance().getType().getMainTable().getSqlColId())) {
            _insert.add(getAttrName(), getValue());
        }
    }
}

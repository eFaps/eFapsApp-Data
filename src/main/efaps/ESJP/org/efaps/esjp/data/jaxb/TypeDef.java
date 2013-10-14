/*
 * Copyright 2003 - 2013 The eFaps Team
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

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.util.cache.CacheReloadException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("4eef5d83-6cdd-4597-b68c-7a0811afe75c")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "type")
@XmlType(name = "type")
public class TypeDef
    extends AbstractDef
{

    @XmlElementRef(required = true)
    private List<AttrDef> attributes;

    @XmlElementRef
    public List<ClassificationDef> classifications;

    /**
     * Getter method for the instance variable {@link #attributes}.
     *
     * @return value of instance variable {@link #attributes}
     */
    public List<AttrDef> getAttributes()
    {
        return this.attributes;
    }

    public Type getType(final Map<String, Integer> _headers,
                        final String[] _value)
        throws CacheReloadException
    {
        Type ret = null;
        if (getName() != null) {
            ret = Type.get(getName());
        } else if (getColumn() != null && _headers.containsKey(getColumn())) {
            ret = Type.get(_value[_headers.get(getColumn())]);
        }
        return ret;
    }

    /**
     * Getter method for the instance variable {@link #classifications}.
     *
     * @return value of instance variable {@link #classifications}
     */
    public List<ClassificationDef> getClassifications()
    {
        return this.classifications;
    }

    public ClassificationDef getClassificationDefByName(final String _name)
    {
        ClassificationDef ret = null;
        for (final ClassificationDef clazzDef : this.classifications) {
            if (_name.equals(clazzDef.getName())) {
                ret = clazzDef;
            }
        }
        return ret;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
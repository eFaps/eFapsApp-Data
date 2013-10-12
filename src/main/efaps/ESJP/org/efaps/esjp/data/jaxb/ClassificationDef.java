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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.util.cache.CacheReloadException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("9e9ab2a0-55f3-444a-8482-988f5d77905a")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "classification")
@XmlType(name = "classification")
public class ClassificationDef
    extends AbstractDef
{

    @XmlElementRef(required = true)
    private List<AttrDef> attributes;

    /**
     * Getter method for the instance variable {@link #attributes}.
     *
     * @return value of instance variable {@link #attributes}
     */
    public List<AttrDef> getAttributes()
    {
        return this.attributes;
    }

    public List<Classification> getClassifications(final Parameter _parameter,
                                                   final Map<String, Integer> _headers,
                                                   final String[] _value)
        throws CacheReloadException
    {
        final List<Classification> ret = new ArrayList<Classification>();
        Classification clazz = null;
        if (getName() != null) {
            clazz = Classification.get(getName());
        } else if (getColumn() != null && _headers.containsKey(getColumn())) {
            clazz = Classification.get(_value[_headers.get(getColumn())]);
        }
        if (clazz != null) {
            Classification parent = clazz.getParentClassification();
            while (parent != null) {
                ret.add(parent);
                parent = parent.getParentClassification();
            }
            Collections.reverse(ret);
            ret.add(clazz);
        }
        return ret;
    }
}

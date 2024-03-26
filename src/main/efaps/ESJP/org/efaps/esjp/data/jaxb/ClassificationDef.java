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
package org.efaps.esjp.data.jaxb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.columns.ClassificationColumn;
import org.efaps.util.cache.CacheReloadException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("9e9ab2a0-55f3-444a-8482-988f5d77905a")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "classification", namespace = "http://www.efaps.org/xsd")
@XmlType(name = "classification", namespace = "http://www.efaps.org/xsd")
public class ClassificationDef
    extends AbstractDef
{

    /**
     * List of attributes belonging to this Classification Definition.
     */
    @XmlElementRef
    private List<AttrDef> attributes;

    /**
     * Getter method for the instance variable {@link #attributes}.
     *
     * @return value of instance variable {@link #attributes}
     */
    public List<AttrDef> getAttributes()
    {
        return this.attributes == null ? new ArrayList<>() : this.attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValidateClass()
    {
        String ret = super.getValidateClass();
        if (ret == null || ret != null && ret.isEmpty()) {
            ret = ClassificationColumn.class.getName();
        }
        return ret;
    }

    /**
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _headers      headr mapping
     * @param _value        value of the current row
     * @return list of Classification
     * @throws CacheReloadException on error
     */
    public List<Classification> getClassifications(final Parameter _parameter,
                                                   final Map<String, Integer> _headers,
                                                   final String[] _value)
        throws CacheReloadException
    {
        final List<Classification> ret = new ArrayList<>();
        Classification clazz = null;
        if (getName() != null) {
            clazz = Classification.get(getName());
        } else if (getColumn() != null && _headers.containsKey(getColumn())) {
            clazz = Classification.get(_value[_headers.get(getColumn())].trim());
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

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.IColumnValue;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("79a3b4d8-be0b-49df-a7ce-81d5bc345699")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "attribute", namespace = "http://www.efaps.org/xsd")
@XmlType(name = "attribute", namespace = "http://www.efaps.org/xsd")
public class AttrDef
    extends AbstractDef
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDef.class);

    /**
     * Classname of an esjp.
     */
    @XmlAttribute(name = "class")
    private String className;

    /**
     * Fixed value.
     */
    @XmlAttribute(name = "fixedValue")
    private String fixedValue;

    /**
     * List of Properties.
     */
    @XmlElementRef
    private List<PropertyDef> properties;

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _headers  mapping of header to column position
     * @param _value    values
     * @param _idx      row index
     * @return value
     */
    public String getValue(final Parameter _parameter,
                           final Map<String, Integer> _headers,
                           final String[] _value,
                           final Integer _idx)
    {
        String ret = null;
        if (getColumn() != null &&  _headers.containsKey(getColumn())) {
            ret = _value[_headers.get(getColumn())];
        } else if (this.fixedValue != null && !this.fixedValue.isEmpty()) {
            ret = this.fixedValue;
        } else {
            try {
                final Class<?> clazz = Class.forName(this.className);
                final IColumnValue columnValue = (IColumnValue) clazz.newInstance();
                ret = columnValue.getValue(_parameter, this, _headers, _value, _idx);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                AttrDef.LOG.error("Catched error on value evaluation", e);
            } catch (final EFapsException e) {
                AttrDef.LOG.error("Catched error on value evaluation", e);
            }
        }
        return ret;
    }

    /**
     * Getter method for the instance variable {@link #properties}.
     *
     * @return value of instance variable {@link #properties}
     */
    public List<PropertyDef> getProperties()
    {
        return this.properties;
    }

    public String getProperty(final String _name)
    {
        String ret = null;
        for (final PropertyDef def : this.properties) {
            if (_name.equals(def.getName())) {
                ret = def.getValue();
                break;
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

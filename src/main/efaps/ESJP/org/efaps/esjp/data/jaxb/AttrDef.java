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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.IColumn;
import org.efaps.esjp.data.IColumnValue;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("79a3b4d8-be0b-49df-a7ce-81d5bc345699")
@EFapsApplication("eFapsApp-Data")
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
     * Regex to figure out if the attribute must be applied to the current
     * type of a row.
     */
    @XmlAttribute(name = "typeRegex")
    private String typeRegex;

    /**
     * Is this attribute a link to a parent object.
     */
    @XmlAttribute(name = "parentLink")
    private boolean parentLink = false;

    /**
     * In case of Update will this attribute overwrite the existing value
     * or not.
     */
    @XmlAttribute(name = "overwrite")
    private boolean overwrite = false;

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
            ret = _value[_headers.get(getColumn())].trim();
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

    /**
     * Getter method for the instance variable {@link #validate}.
     *
     * @return value of instance variable {@link #validate}
     */
    @Override
    public Boolean isValidate()
    {
        return this.fixedValue != null && !this.fixedValue.isEmpty() ? false : super.isValidate();
    }

    /**
     * @param _name name of the property
     * @return value for the property
     */
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

    /**
     * @param _name name of the property
     * @return array of values for the property
     */
    public String[] analyseProperty(final String _name)
    {
        final List<String> ret = new ArrayList<>();
        //search for base name
        String valTmp = getProperty(_name);
        if (valTmp != null) {
            ret.add(valTmp);
        }
        for (int i = 1; i < 100; i++) {
            final String nameTmp = _name + String.format("%02d", i);
            valTmp = getProperty(nameTmp);
            if (valTmp == null) {
                break;
            } else {
                ret.add(valTmp);
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

    /**
     * Getter method for the instance variable {@link #className}.
     *
     * @return value of instance variable {@link #className}
     */
    public String getClassName()
    {
        return this.className;
    }

    /**
     * Setter method for instance variable {@link #className}.
     *
     * @param _className value for instance variable {@link #className}
     */
    public void setClassName(final String _className)
    {
        this.className = _className;
    }

    /**
     * Getter method for the instance variable {@link #fixedValue}.
     *
     * @return value of instance variable {@link #fixedValue}
     */
    public String getFixedValue()
    {
        return this.fixedValue;
    }

    /**
     * Setter method for instance variable {@link #fixedValue}.
     *
     * @param _fixedValue value for instance variable {@link #fixedValue}
     */
    public void setFixedValue(final String _fixedValue)
    {
        this.fixedValue = _fixedValue;
    }

    /**
     * Getter method for the instance variable {@link #typeRegex}.
     *
     * @return value of instance variable {@link #typeRegex}
     */
    public String getTypeRegex()
    {
        return this.typeRegex;
    }

    /**
     * Setter method for instance variable {@link #typeRegex}.
     *
     * @param _typeRegex value for instance variable {@link #typeRegex}
     */
    public void setTypeRegex(final String _typeRegex)
    {
        this.typeRegex = _typeRegex;
    }

    /**
     * Getter method for the instance variable {@link #parentLink}.
     *
     * @return value of instance variable {@link #parentLink}
     */
    public boolean isParentLink()
    {
        return this.parentLink;
    }

    /**
     * Setter method for instance variable {@link #parentLink}.
     *
     * @param _parentLink value for instance variable {@link #parentLink}
     */
    public void setParentLink(final boolean _parentLink)
    {
        this.parentLink = _parentLink;
    }

    /**
     * @param _typeName Name of a type to be checked against the regex
     * @return true if must be applied on the given type, else false
     */
    public boolean applies(final String _typeName)
    {
        boolean ret;
        if (getTypeRegex() == null || getTypeRegex() != null && getTypeRegex().isEmpty()) {
            ret = true;
        } else {
            ret = _typeName.matches(getTypeRegex());
        }
        return ret;
    }

    /**
     * Getter method for the instance variable {@link #overwrite}.
     *
     * @return value of instance variable {@link #overwrite}
     */
    public boolean isOverwrite()
    {
        return this.overwrite;
    }

    /**
     * Setter method for instance variable {@link #overwrite}.
     *
     * @param _overwrite value for instance variable {@link #overwrite}
     */
    public void setOverwrite(final boolean _overwrite)
    {
        this.overwrite = _overwrite;
    }

    /**
     * Get the name of the columns used.
     *
     * @return collection of the used columns
     */
    public Collection<String> getColumnNames(final Parameter _parameter)
    {
        final List<String> ret = new ArrayList<>();
        if (getFixedValue() == null) {
            if (getColumn() != null) {
                ret.add(getColumn());
            } else {
                try {
                    final Class<?> clazz = Class.forName(this.className);
                    final IColumn column = (IColumn) clazz.newInstance();
                    ret.addAll(column.getColumnNames(_parameter, this));
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    AttrDef.LOG.error("Catched error on value evaluation", e);
                }
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

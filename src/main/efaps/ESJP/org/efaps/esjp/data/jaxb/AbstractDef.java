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

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.IColumnValidate;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("fb8cec2f-6d3f-431c-8679-1faa5a1428c9")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "abstractDef", namespace = "http://www.efaps.org/xsd")
@XmlType(name = "abstractDef", namespace = "http://www.efaps.org/xsd")
public abstract class AbstractDef
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDef.class);

    /**
     * Name of the Definition.
     */
    @XmlAttribute(name = "name")
    private String name;

    /**
     * Column of the Definition.
     */
    @XmlAttribute(name = "column")
    private String column;

    /**
     * Column of the Definition.
     */
    @XmlAttribute(name = "validate")
    private Boolean validate = true;

    /**
     * Class used for validation.
     */
    @XmlAttribute(name = "validateClass")
    private String validateClass;

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
     */
    public void setName(final String _name)
    {
        this.name = _name;
    }

    /**
     * Getter method for the instance variable {@link #column}.
     *
     * @return value of instance variable {@link #column}
     */
    public String getColumn()
    {
        return this.column;
    }

    /**
     * Setter method for instance variable {@link #column}.
     *
     * @param _column value for instance variable {@link #column}
     */
    public void setColumn(final String _column)
    {
        this.column = _column;
    }

    /**
     * Getter method for the instance variable {@link #validate}.
     *
     * @return value of instance variable {@link #validate}
     */
    public Boolean getValidate()
    {
        return this.validate;
    }

    /**
     * Setter method for instance variable {@link #validate}.
     *
     * @param _validate value for instance variable {@link #validate}
     */
    public void setValidate(final Boolean _validate)
    {
        this.validate = _validate;
    }

    /**
     * Getter method for the instance variable {@link #validateClass}.
     *
     * @return value of instance variable {@link #validateClass}
     */
    public String getValidateClass()
    {
        return this.validateClass;
    }

    /**
     * Setter method for instance variable {@link #validateClass}.
     *
     * @param _validateClass value for instance variable {@link #validateClass}
     */
    public void setValidateClass(final String _validateClass)
    {
        this.validateClass = _validateClass;
    }


    public Boolean validate(final Parameter _parameter,
                            final Map<String, Integer> _headers,
                            final String[] _value,
                            final Integer _idx)
    {
        Boolean ret = false;
        if (getValidate()) {
            try {
                final Class<?> clazz = Class.forName(getValidateClass());
                final IColumnValidate columnValue = (IColumnValidate) clazz.newInstance();
                ret = columnValue.validate(_parameter, this, _headers, _value, _idx);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                AbstractDef.LOG.error("Catched error on value evaluation", e);
            } catch (final EFapsException e) {
                AbstractDef.LOG.error("Catched error on value evaluation", e);
            }
        } else {
            ret = true;
        }
        return ret;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

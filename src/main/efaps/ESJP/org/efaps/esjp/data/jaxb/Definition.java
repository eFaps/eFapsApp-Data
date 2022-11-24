/*
 * Copyright 2003 - 2022 The eFaps Team
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
 */


package org.efaps.esjp.data.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;

@EFapsUUID("af9e72a7-9789-4e53-8d86-12d72fb6bd5a")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "definition", namespace = "http://www.efaps.org/xsd")
@XmlType(name = "definition", namespace = "http://www.efaps.org/xsd")
@XmlSeeAlso(TypeDef.class)
public class Definition
{
    /**
     * Name of the definition.
     */
    @XmlAttribute(name = "name")
    private String name;

    /**
     * csv file belonging to this definition.
     */
    @XmlAttribute(name = "file")
    private String file;

    /**
     * The line number to skip for start reading
     */
    @XmlAttribute(name = "skipLine")
    private Integer skipLine = 0;

    /**
     * Will the insert be executed.
     */
    @XmlAttribute(name = "execute")
    private boolean execute = true;

    /**
     * Will the insert be executed.
     */
    @XmlAttribute(name = "force")
    private boolean force = false;

    /**
     * Execute with accesscheck.
     */
    @XmlAttribute(name = "accessCheck")
    private boolean accessCheck = true;

    /**
     * Execute with trigger.
     */
    @XmlAttribute(name = "trigger")
    private boolean trigger = true;

    /**
     * Update instances if existing.
     */
    @XmlAttribute(name = "update")
    private boolean update = false;

    /**
     * key column.
     */
    @XmlAttribute(name = "keyColumn")
    private String keyColumn;

    /**
     * Type definition belonging to this definition.
     */
    @XmlElementRef(required=true)
    private TypeDef type;

    /**
     * Identifier definition belong to this definition.
     */
    @XmlElementRef
    private IdentifierDef identifier;


    @XmlElementRef
    private List<RowListener> rowListeners;

    /**
     * Getter method for the instance variable {@link #name}.
     *
     * @return value of instance variable {@link #name}
     */
    public String getName()
    {
        return name;
    }

    /**
     * Setter method for instance variable {@link #name}.
     *
     * @param _name value for instance variable {@link #name}
     */
    public void setName(final String _name)
    {
        name = _name;
    }

    /**
     * Getter method for the instance variable {@link #file}.
     *
     * @return value of instance variable {@link #file}
     */
    public String getFile()
    {
        return file;
    }

    /**
     * Getter method for the instance variable {@link #skipLine}.
     *
     * @return value of instance variable {@link #skipLine}
     */
    public Integer getSkipLine()
    {
        return skipLine;
    }

    /**
     * Setter method for instance variable {@link #skipLine}.
     *
     * @param _headerrow value for instance variable {@link #skipLine}
     */
    public void setSkipLine(final Integer _skipLine)
    {
        skipLine = _skipLine;
    }

    /**
     * Getter method for the instance variable {@link #type}.
     *
     * @return value of instance variable {@link #type}
     */
    public TypeDef getTypeDef()
    {
        return type;
    }

    /**
     * Getter method for the instance variable {@link #execute}.
     *
     * @return value of instance variable {@link #execute}
     */
    public boolean isExecute()
    {
        return execute;
    }

    /**
     * Setter method for instance variable {@link #execute}.
     *
     * @param _execute value for instance variable {@link #execute}
     */
    public void setExecute(final boolean _execute)
    {
        execute = _execute;
    }

    /**
     * Getter method for the instance variable {@link #keyColumn}.
     *
     * @return value of instance variable {@link #keyColumn}
     */
    public String getKeyColumn()
    {
        return keyColumn;
    }

    /**
     * Setter method for instance variable {@link #keyColumn}.
     *
     * @param _keyColumn value for instance variable {@link #keyColumn}
     */
    public void setKeyColumn(final String _keyColumn)
    {
        keyColumn = _keyColumn;
    }

    /**
     *
     */
    public boolean hasKey()
    {
        return keyColumn != null && !keyColumn.isEmpty();
    }

    /**
     * Getter method for the instance variable {@link #accessCheck}.
     *
     * @return value of instance variable {@link #accessCheck}
     */
    public boolean isAccessCheck()
    {
        return accessCheck;
    }

    /**
     * Setter method for instance variable {@link #accessCheck}.
     *
     * @param _accessCheck value for instance variable {@link #accessCheck}
     */
    public void setAccessCheck(final boolean _accessCheck)
    {
        accessCheck = _accessCheck;
    }

    /**
     * Getter method for the instance variable {@link #trigger}.
     *
     * @return value of instance variable {@link #trigger}
     */
    public boolean isTrigger()
    {
        return trigger;
    }

    /**
     * Setter method for instance variable {@link #trigger}.
     *
     * @param _trigger value for instance variable {@link #trigger}
     */
    public void setTrigger(final boolean _trigger)
    {
        trigger = _trigger;
    }

    /**
     * Getter method for the instance variable {@link #force}.
     *
     * @return value of instance variable {@link #force}
     */
    public boolean isForce()
    {
        return force;
    }

    /**
     * Setter method for instance variable {@link #force}.
     *
     * @param _force value for instance variable {@link #force}
     */
    public void setForce(final boolean _force)
    {
        force = _force;
    }

    /**
     * Getter method for the instance variable {@link #update}.
     *
     * @return value of instance variable {@link #update}
     */
    public boolean isUpdate()
    {
        return update;
    }

    /**
     * Setter method for instance variable {@link #update}.
     *
     * @param _update value for instance variable {@link #update}
     */
    public void setUpdate(final boolean _update)
    {
        update = _update;
    }

    /**
     * Getter method for the instance variable {@link #identifier}.
     *
     * @return value of instance variable {@link #identifier}
     */
    public IdentifierDef getIdentifier()
    {
        return identifier;
    }

    public List<RowListener> getRowListeners()
    {
        return rowListeners;
    }

    public void setRowListeners(final List<RowListener> rowListeners)
    {
        this.rowListeners = rowListeners;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("af9e72a7-9789-4e53-8d86-12d72fb6bd5a")
@EFapsRevision("$Rev$")
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
     * Index of the row containing the header.
     */
    @XmlAttribute(name = "headerrow")
    private  Integer headerrow = 1;

    /**
     * Will the insert be executed.
     */
    @XmlAttribute(name = "execute")
    private boolean execute = true;

    /**
     * key column.
     */
    @XmlAttribute(name = "keyColumn")
    private String keyColumn;

    /**
     * Type definition belonging to this defintion.
     */
    @XmlElementRef(required=true)
    private TypeDef type;

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
     * Getter method for the instance variable {@link #file}.
     *
     * @return value of instance variable {@link #file}
     */
    public String getFile()
    {
        return this.file;
    }

    /**
     * Getter method for the instance variable {@link #headerrow}.
     *
     * @return value of instance variable {@link #headerrow}
     */
    public Integer getHeaderrow()
    {
        return this.headerrow;
    }

    /**
     * Setter method for instance variable {@link #headerrow}.
     *
     * @param _headerrow value for instance variable {@link #headerrow}
     */
    public void setHeaderrow(final Integer _headerrow)
    {
        this.headerrow = _headerrow;
    }

    /**
     * Getter method for the instance variable {@link #type}.
     *
     * @return value of instance variable {@link #type}
     */
    public TypeDef getTypeDef()
    {
        return this.type;
    }

    /**
     * Getter method for the instance variable {@link #execute}.
     *
     * @return value of instance variable {@link #execute}
     */
    public boolean isExecute()
    {
        return this.execute;
    }

    /**
     * Setter method for instance variable {@link #execute}.
     *
     * @param _execute value for instance variable {@link #execute}
     */
    public void setExecute(final boolean _execute)
    {
        this.execute = _execute;
    }

    /**
     * Getter method for the instance variable {@link #keyColumn}.
     *
     * @return value of instance variable {@link #keyColumn}
     */
    public String getKeyColumn()
    {
        return this.keyColumn;
    }

    /**
     * Setter method for instance variable {@link #keyColumn}.
     *
     * @param _keyColumn value for instance variable {@link #keyColumn}
     */
    public void setKeyColumn(final String _keyColumn)
    {
        this.keyColumn = _keyColumn;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     *
     */
    public boolean hasKey()
    {
        return this.keyColumn != null && !this.keyColumn.isEmpty();
    }
}

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

import java.net.URL;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("b4d9c588-b8fe-4059-88d0-e911905d0a93")
@EFapsApplication("eFapsApp-Data")
@XmlRootElement(name = "data-import", namespace = "http://www.efaps.org/xsd")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(Definition.class)
public class DataImport
{

    /**
     * List of Definitions contained in this import.
     */
    @XmlElementRef(required = true)
    private List<Definition> definitions;

    /**
     * URL of the file.
     */
    private URL url;

    /**
     * Getter method for the instance variable {@link #defintion}.
     *
     * @return value of instance variable {@link #defintion}
     */
    public List<Definition> getDefinition()
    {
        return this.definitions;
    }

    /**
     * Getter method for the instance variable {@link #url}.
     *
     * @return value of instance variable {@link #url}
     */
    public URL getUrl()
    {
        return this.url;
    }

    /**
     * Setter method for instance variable {@link #url}.
     *
     * @param _url value for instance variable {@link #url}
     */
    public void setUrl(final URL _url)
    {
        this.url = _url;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

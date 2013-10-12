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

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("b4d9c588-b8fe-4059-88d0-e911905d0a93")
@EFapsRevision("$Rev$")
@XmlRootElement(name = "data-import")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(Definition.class)
public class DataImport
{

    @XmlElementRef(required = true)
    private List<Definition> definitions;

    private File file;

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
     * @param _file
     */
    public void setFile(final File _file)
    {
        this.file = _file;
    }

    /**
     * Getter method for the instance variable {@link #file}.
     *
     * @return value of instance variable {@link #file}
     */
    public File getFile()
    {
        return this.file;
    }
}

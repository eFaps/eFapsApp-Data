/*
 * Copyright 2003 - 2014 The eFaps Team
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
import javax.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("0bb639e3-7c40-4063-bf53-20564d32d6e0")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "classification", namespace = "http://www.efaps.org/xsd")
public class ClassificationObject
    extends AbstractEFapsObject<ClassificationObject>
{

    public ClassificationObject()
    {
        super();
    }

    /**
     * @param _instance
     */
    public ClassificationObject(final Instance _instance)
        throws EFapsException
    {
        super(_instance);
    }

    @Override
    protected ClassificationObject getThis()
    {
        return this;
    }
}
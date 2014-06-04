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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
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
@EFapsUUID("e3cff1d6-2eac-4891-844a-e1ea8c5be4cd")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "efapslist", namespace = "http://www.efaps.org/xsd")
public class ObjectList
{
    /**
     * The list of attributes belonging to this object.
     */
    @XmlElementWrapper(name = "objects", namespace = "http://www.efaps.org/xsd")
    @XmlElementRef
    private final List<EFapsObject> objects = new ArrayList<EFapsObject>();

    /**
     * Getter method for the instance variable {@link #objects}.
     *
     * @return value of instance variable {@link #objects}
     */
    public List<EFapsObject> getObjects()
    {
        return this.objects;
    }

    /**
     * Load the object from the eFaps DataBase.
     * @throws EFapsException on error
     */
    public void load()
        throws EFapsException
    {
        load(10);
    }

    /**
     * Load the object from the eFaps DataBase.
     * @param _maxDepth maximum depth of objects to follow
     * @throws EFapsException on error
     */
    public void load(final int _maxDepth)
        throws EFapsException
    {
        final Set<Instance> instances = new HashSet<Instance>();
        for (final EFapsObject object : getObjects()) {
            object.load(_maxDepth, instances);
            instances.add(object.getInstance());
        }
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

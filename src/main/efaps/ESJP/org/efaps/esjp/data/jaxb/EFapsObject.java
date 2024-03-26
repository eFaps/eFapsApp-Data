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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.PrintQuery;
import org.efaps.db.SelectBuilder;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("a5f67769-3f89-4df7-8018-5b46ed0a7841")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "efapsobject", namespace = "http://www.efaps.org/xsd")
public class EFapsObject
    extends AbstractEFapsObject<EFapsObject>
{

    /**
     * The list of attributes belonging to this object.
     */
    @XmlElementWrapper(name = "classifications", namespace = "http://www.efaps.org/xsd")
    @XmlElementRef
    private final List<ClassificationObject> classifications = new ArrayList<>();

    /**
     * Default Constructor.
     */
    public EFapsObject()
    {
        super();
    }

    /**
     * @param _instance instance of the object
     * @throws EFapsException on error
     */
    public EFapsObject(final Instance _instance)
        throws EFapsException
    {
        super(_instance);
    }

    /**
     * Getter method for the instance variable {@link #attributes}.
     *
     * @return value of instance variable {@link #attributes}
     */
    public List<ClassificationObject> getClassifications()
    {
        return this.classifications;
    }

    /**
     * @param _classification classification to be added
     * @return this for chaining
     */
    public EFapsObject addClassification(final ClassificationObject _classification)
    {
        if (_classification != null) {
            this.classifications.add(_classification);
        }
        return this;
    }

    @Override
    protected EFapsObject getThis()
    {
        return this;
    }

    /**
     * Load the object from the eFaps DataBase.
     * @throws EFapsException on error
     */
    public void load()
        throws EFapsException
    {
        load(10, Collections.<Instance>emptySet());
    }

    /**
     * Load the object from the eFaps DataBase.
     * @param _maxDepth maximum depth of objects to follow
     * @throws EFapsException on error
     */
    public void load(final int _maxDepth)
        throws EFapsException
    {
        load(_maxDepth, Collections.<Instance>emptySet());
    }

    @Override
    public void load(final int _maxDepth,
                     final Set<Instance> _instances)
        throws EFapsException
    {
        super.load(_maxDepth, _instances);
        final Type type = Type.get(getTypeUUID());
        if (!type.getClassifiedByTypes().isEmpty()) {
            final PrintQuery print = new PrintQuery(getInstance());
            final SelectBuilder clazzSel = new SelectBuilder().clazz().type();
            print.addSelect(clazzSel);
            print.execute();
            final Set<Classification> classes = new HashSet<>();
            final Object object = print.getSelect(clazzSel);
            if (object instanceof List) {
                for (final Object clazz : (List<?>) object) {
                    Classification classification = (Classification) clazz;
                    while (classification != null) {
                        classes.add(classification);
                        classification = classification.getParentClassification();
                    }
                }
            }
            final List<SelectBuilder> selects = new ArrayList<>();
            final PrintQuery clazzPrint = new PrintQuery(getInstance());
            for (final Classification clazz : classes) {
                final SelectBuilder selInst = SelectBuilder.get().clazz(clazz.getUUID()).instance();
                selects.add(selInst);
                clazzPrint.addSelect(selInst);
            }
            clazzPrint.execute();
            for (final SelectBuilder selInst : selects) {
                final Instance classInst = clazzPrint.getSelect(selInst);
                final ClassificationObject classObject = new ClassificationObject(classInst);
                final Set<Instance> inst = new HashSet<>();
                inst.addAll(_instances);
                inst.add(getInstance());
                if (inst.size() < _maxDepth) {
                    classObject.load(_maxDepth, inst);
                }
                addClassification(classObject);
            }
        }
    }

    @Override
    public Instance create()
        throws EFapsException
    {
        final Instance ret = super.create();
        for (final ClassificationObject clazz : getClassifications()) {
            clazz.create(ret);
        }
        return ret;
    }
}

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

package org.efaps.esjp.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.esjp.data.jaxb.AttrDef;
import org.efaps.esjp.data.jaxb.ClassificationDef;
import org.efaps.esjp.data.jaxb.DataImport;
import org.efaps.esjp.data.jaxb.Definition;
import org.efaps.esjp.data.jaxb.TypeDef;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id: AbstractImport_Base.java 10427 2013-10-12 15:18:26Z
 *          jan@moxter.net $
 */
@EFapsUUID("b3f81d2e-2352-43ab-acb2-2c98d2c32c05")
@EFapsRevision("$Rev$")
public abstract class AbstractImport_Base
{

    public static final Logger LOG = LoggerFactory.getLogger(AbstractImport.class);

    public Return execute(final Parameter _parameter)
        throws EFapsException
    {
        final String xmlFile = _parameter.getParameterValue("valueField");
        final File file = new File(xmlFile);
        try {
            final JAXBContext jc = getJAXBContext();
            final Unmarshaller unmarschaller = jc.createUnmarshaller();
            final Source source = new StreamSource(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            final Object object = unmarschaller.unmarshal(source);
            if (object instanceof DataImport) {
                final DataImport dataImp = (DataImport) object;
                dataImp.setFile(file);
                for (final Definition definition : dataImp.getDefinition()) {
                    final List<String[]> values = readCSV(_parameter, dataImp, definition);
                    final Map<String, Integer> headers = readHeader(_parameter, definition, values);
                    for (int i = 0; i < definition.getHeaderrow(); i++) {
                        values.remove(0);
                    }
                    for (final String[] value : values) {
                        final Insert insert = new Insert(definition.getTypeDef().getType(headers, value));
                        for (final AttrDef attr : definition.getTypeDef().getAttributes()) {
                            insert.add(attr.getName(), attr.getValue(_parameter, headers, value));
                        }
                        insert.execute();

                        if (definition.getTypeDef().getClassifications() != null) {
                            insertClassification(_parameter, definition.getTypeDef(), headers, value,
                                            insert.getInstance());
                        }
                    }
                }
            }
        } catch (final UnsupportedEncodingException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        } catch (final FileNotFoundException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        } catch (final JAXBException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        }
        return new Return();
    }

    protected void insertClassification(final Parameter _parameter,
                                        final TypeDef _typeDef,
                                        final Map<String, Integer> _headers,
                                        final String[] _value,
                                        final Instance _instance)
        throws EFapsException
    {
        AbstractImport_Base.LOG.trace("preparing inserts for classifications");
        final List<ClassificationDef> classifications = _typeDef.getClassifications();

        for (final ClassificationDef classification : classifications) {
            AbstractImport_Base.LOG.trace("preparing inserts for: {}", classification);
            for (final Classification clazz : classification.getClassifications(_parameter, _headers, _value)) {
                final Insert relInsert = new Insert(clazz.getClassifyRelationType());
                relInsert.add(clazz.getRelLinkAttributeName(), _instance);
                relInsert.add(clazz.getRelTypeAttributeName(), clazz.getId());
                relInsert.executeWithoutAccessCheck();

                final Insert classInsert = new Insert(clazz);
                classInsert.add(clazz.getLinkAttributeName(), _instance);
                final ClassificationDef clazzDef = _typeDef.getClassificationDefByName(clazz.getName());
                if (clazzDef != null) {
                    for (final AttrDef attr : clazzDef.getAttributes()) {
                        classInsert.add(attr.getName(), attr.getValue(_parameter, _headers, _value));
                    }
                }
                classInsert.executeWithoutAccessCheck();
            }
        }
    }

    protected Map<String, Integer> readHeader(final Parameter _parameter,
                                              final Definition _definition,
                                              final List<String[]> _values)
    {
        AbstractImport_Base.LOG.trace("Reading the Header from the CSVFile.");
        final Map<String, Integer> ret = new HashMap<String, Integer>();
        final String[] vals = _values.get(_definition.getHeaderrow() - 1);
        int i = 0;
        for (final String val : vals) {
            if (!val.isEmpty()) {
                ret.put(val, i);
            }
            i++;
        }
        AbstractImport_Base.LOG.debug("Header found: {}", ret);
        return ret;
    }

    protected List<String[]> readCSV(final Parameter _parameter,
                                     final DataImport _dataImport,
                                     final Definition _definition)
    {
        final List<String[]> ret = new ArrayList<String[]>();
        try {
            AbstractImport_Base.LOG.trace("Reading the CSV File.");
            final CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(new File(_dataImport
                            .getFile().getParent(), _definition.getFile())), "UTF-8"));
            ret.addAll(reader.readAll());
            reader.close();
        } catch (final IOException e) {
            AbstractImport_Base.LOG.error("Catched error on reading CSV from '{}'", e , _dataImport);
        }
        return ret;
    }

    /**
     * Get the JAXBContext used in this importer.
     *
     * @return an JAXBContext
     * @throws JAXBException on error
     */
    protected JAXBContext getJAXBContext()
        throws JAXBException
    {
        AbstractImport_Base.LOG.trace("Getting JAXBContext.");
        final JAXBContext context = JAXBContext.newInstance(getClasses());
        return context;
    }

    /**
     * @return an Array of classes used for the JAXBContext
     */
    protected Class<?>[] getClasses()
    {
        AbstractImport_Base.LOG.trace("Getting the Classes for the JAXBContext.");
        return new Class[] { TypeDef.class, Definition.class, DataImport.class, AttrDef.class, ClassificationDef.class };
    }
}

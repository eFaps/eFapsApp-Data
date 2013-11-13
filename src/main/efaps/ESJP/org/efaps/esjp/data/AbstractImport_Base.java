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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Parameter.ParameterValues;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.db.Update;
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

    /**
     * Logger for this class.
     */
    public static final Logger LOG = LoggerFactory.getLogger(AbstractImport.class);

    /**
     * Called by scripts inside of version.xml during install and update.
     *
     * @param _url  url of the file to be imported
     * @throws EFapsException on error
     */
    public void importFromFile(final URL _url)
        throws EFapsException
    {
        final Parameter parameter = new Parameter();
        parameter.put(ParameterValues.PARAMETERS, new HashMap<String, String[]>());
        parameter.getParameters().put("valueField", new String[] { _url.toString() });
        execute(parameter);
    }

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @return empty Return
     * @throws EFapsException on error
     */
    public Return execute(final Parameter _parameter)
        throws EFapsException
    {
        try {
            final String xmlFile = _parameter.getParameterValue("valueField");
            final URL url = new URL(xmlFile);
            final JAXBContext jc = getJAXBContext();
            final Unmarshaller unmarschaller = jc.createUnmarshaller();
            final URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            final Source source = new StreamSource(new InputStreamReader(connection.getInputStream()));
            final Object object = unmarschaller.unmarshal(source);
            if (object instanceof DataImport) {
                final DataImport dataImp = (DataImport) object;
                dataImp.setUrl(url);
                final Map<String, Instance> keys = new HashMap<String, Instance>();
                for (final Definition definition : dataImp.getDefinition()) {
                    AbstractImport_Base.LOG.info("Starting with definition: '{}'", definition.getName());
                    final List<String[]> values = readCSV(_parameter, dataImp, definition);
                    final Map<String, Integer> headers = readHeader(_parameter, definition, values);
                    for (int i = 0; i < definition.getHeaderrow(); i++) {
                        values.remove(0);
                    }
                    int j = definition.getHeaderrow() + 1;
                    boolean execute = true;
                    final Set<Integer> skip = new HashSet<Integer>();
                    AbstractImport_Base.LOG.info("Validating definition: '{}'", definition.getName());
                    //validate
                    for (final String[] value : values) {
                        if (definition.hasKey()) {
                            keys.put(value[headers.get(definition.getKeyColumn())], null);
                        }

                        final Boolean valid = definition.getTypeDef().validate(_parameter, headers, value, j);
                        if (!valid) {
                            execute = false;
                        }
                        final Type type = definition.getTypeDef().getType(headers, value);
                        String typeName ="no Type Name given";
                        if (type != null) {
                            typeName = type.getName();
                        }
                        for (final AttrDef attr : definition.getTypeDef().getAttributes()) {
                            if (attr.applies(typeName)) {
                                final Boolean check = attr.validate(_parameter, headers, value, j);
                                if (definition.hasKey() && attr.isParentLink()) {
                                    final String parentKey = attr.getValue(_parameter, headers, value, j);
                                    if (!keys.containsKey(parentKey)) {
                                        AbstractImport_Base.LOG.error("ParentKey '{}' in row {} not found.",
                                                        parentKey, j);
                                    }
                                }
                                if (!check) {
                                    execute = false;
                                    skip.add(j);
                                }
                            }
                        }
                        for (final ClassificationDef classDef : definition.getTypeDef().getClassifications()) {
                            final Boolean check = classDef.validate(_parameter, headers, value, j);
                            if (!check) {
                                execute = false;
                                skip.add(j);
                            }
                        }
                        j++;
                    }
                    if ((execute && definition.isExecute()) || (definition.isExecute() && definition.isForce())) {
                        AbstractImport_Base.LOG.info("Importing definition: '{}'", definition.getName());
                        //create
                        j = definition.getHeaderrow() + 1;
                        for (final String[] value : values) {
                            if (!execute && skip.contains(j)) {
                                AbstractImport_Base.LOG.info("Skipped Line: '{}': {} ", j, Arrays.toString(value));
                            } else {
                                final Type type = definition.getTypeDef().getType(headers, value);
                                final Insert insert = new Insert(type);
                                for (final AttrDef attr : definition.getTypeDef().getAttributes()) {
                                    if (attr.applies(type.getName())) {
                                        if (definition.hasKey() && attr.isParentLink()) {
                                            final String parentKey = attr.getValue(_parameter, headers, value, j);
                                            insert.add(attr.getName(), keys.get(parentKey));
                                        } else {
                                            insert.add(attr.getName(), attr.getValue(_parameter, headers, value, j));
                                        }
                                    }
                                }
                                add2TypeInsert(_parameter, definition, headers, values, value, insert, j);

                                execute(_parameter, definition, insert);

                                if (definition.hasKey()) {
                                    keys.put(value[headers.get(definition.getKeyColumn())], insert.getInstance());
                                }
                                if (definition.getTypeDef().getClassifications() != null) {
                                    insertClassification(_parameter, definition, headers, value,
                                                    insert.getInstance(), j);
                                }
                                add2Row(_parameter, definition, headers, values, value, insert.getInstance(), j);
                            }
                            j++;
                        }
                    }
                    AbstractImport_Base.LOG.info("Finished definition: '{}'", definition.getName());
                }
            }
        } catch (final UnsupportedEncodingException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        } catch (final FileNotFoundException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        } catch (final JAXBException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        } catch (final MalformedURLException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        } catch (final IOException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        }
        return new Return();
    }

    protected void execute(final Parameter _parameter,
                           final Definition _def,
                           final Update _update)
        throws EFapsException
    {
        if (_def.isAccessCheck() && _def.isTrigger()) {
            _update.execute();
        } else if (!_def.isAccessCheck() && _def.isTrigger()) {
            _update.executeWithoutAccessCheck();
        } else if (!_def.isAccessCheck() && !_def.isTrigger()) {
            _update.executeWithoutTrigger();
        }   else {
            _update.execute();
        }
    }


    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _def      definition
     * @param _headers  header mapping
     * @param _value    values of the current row
     * @param _instance instance of the created object
     * @param _idx      index of the current row
     * @throws EFapsException on error
     */
    protected void insertClassification(final Parameter _parameter,
                                        final Definition _def,
                                        final Map<String, Integer> _headers,
                                        final String[] _value,
                                        final Instance _instance,
                                        final Integer _idx)
        throws EFapsException
    {
        AbstractImport_Base.LOG.trace("preparing inserts for classifications");
        final List<ClassificationDef> classifications = _def.getTypeDef().getClassifications();
        final Set<Classification> inserted = new HashSet<Classification>();
        for (final ClassificationDef classification : classifications) {
            AbstractImport_Base.LOG.trace("preparing inserts for: {}", classification);
            for (final Classification clazz : classification.getClassifications(_parameter, _headers, _value)) {
                if (!inserted.contains(clazz)) {
                    inserted.add(clazz);
                    final Insert relInsert = new Insert(clazz.getClassifyRelationType());
                    relInsert.add(clazz.getRelLinkAttributeName(), _instance);
                    relInsert.add(clazz.getRelTypeAttributeName(), clazz.getId());
                    execute (_parameter, _def, relInsert);

                    final Insert classInsert = new Insert(clazz);
                    classInsert.add(clazz.getLinkAttributeName(), _instance);
                    final ClassificationDef clazzDef = _def.getTypeDef().getClassificationDefByName(clazz.getName());
                    if (clazzDef != null) {
                        for (final AttrDef attr : clazzDef.getAttributes()) {
                            classInsert.add(attr.getName(), attr.getValue(_parameter, _headers, _value, _idx));
                        }
                    }
                    execute (_parameter, _def, classInsert);
                }
            }
        }
    }


    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _definition   defintion the header belongs to
     * @param _values     list of row values
     * @return header mapping
     */
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

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _dataImport   the import defintion
     * @param _definition   defintion the values belong to
     * @return list of row values
     */
    protected List<String[]> readCSV(final Parameter _parameter,
                                     final DataImport _dataImport,
                                     final Definition _definition)
    {
        final List<String[]> ret = new ArrayList<String[]>();
        try {
            AbstractImport_Base.LOG.trace("Reading the CSV File.");
            final URL relative = new URL(_dataImport.getUrl(), _definition.getFile());
            final URLConnection connection = relative.openConnection();
            final CSVReader reader = new CSVReader(new InputStreamReader(connection.getInputStream()));
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


    /**
     * To be used by implementations.
     *
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _definition   current Defintion
     * @param _headers      Headers
     * @param _values       List of all values
     * @param _value        value for the current row
     * @param _insert       insert yo add 2
     * @param _j            row idex
     * @throws EFapsException on error
     */
    protected void add2TypeInsert(final Parameter _parameter,
                                  final Definition _definition,
                                  final Map<String, Integer> _headers,
                                  final List<String[]> _values,
                                  final String[] _value,
                                  final Insert _insert,
                                  final int _j)
        throws EFapsException
    {
      // To be used by implementations
    }

    /**
     * To be used by implementations.
     *
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _definition   current Defintion
     * @param _headers      Headers
     * @param _values       List of all values
     * @param _value        value for the current row
     * @param _instance     Instance of the created object
     * @param _j            row idex
     * @throws EFapsException on error
     */
    protected void add2Row(final Parameter _parameter,
                           final Definition _definition,
                           final Map<String, Integer> _headers,
                           final List<String[]> _values,
                           final String[] _value,
                           final Instance _instance,
                           final int _j)
       throws EFapsException
    {
        // To be used by implementations
    }
}

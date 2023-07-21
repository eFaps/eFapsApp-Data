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

package org.efaps.esjp.data;

import java.io.IOException;
import java.io.InputStreamReader;
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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Parameter.ParameterValues;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.db.PrintQuery;
import org.efaps.db.SelectBuilder;
import org.efaps.db.Update;
import org.efaps.esjp.data.jaxb.AttrDef;
import org.efaps.esjp.data.jaxb.AttrSetDef;
import org.efaps.esjp.data.jaxb.ClassificationDef;
import org.efaps.esjp.data.jaxb.DataImport;
import org.efaps.esjp.data.jaxb.Definition;
import org.efaps.esjp.data.jaxb.EFapsObject;
import org.efaps.esjp.data.jaxb.IdentifierDef;
import org.efaps.esjp.data.jaxb.ObjectList;
import org.efaps.esjp.data.jaxb.TypeDef;
import org.efaps.esjp.data.jaxb.attributes.DimensionTypeAttribute;
import org.efaps.esjp.data.jaxb.attributes.EFapsAttributes;
import org.efaps.esjp.data.jaxb.attributes.UoMTypeAttribute;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

@EFapsUUID("b3f81d2e-2352-43ab-acb2-2c98d2c32c05")
@EFapsApplication("eFapsApp-Data")
public abstract class AbstractImport_Base
{

    /**
     * Logger for this class.
     */
    public static final Logger LOG = LoggerFactory.getLogger(AbstractImport.class);

    /**
     * Called by scripts inside of version.xml during install and update.
     *
     * @param _url url of the file to be imported
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
            final JAXBContext jc = getJAXBContext();
            final Unmarshaller unmarschaller = jc.createUnmarshaller();
            unmarschaller.setEventHandler(new jakarta.xml.bind.helpers.DefaultValidationEventHandler());
            final Object object = unmarschaller.unmarshal(getSource4DataImport(_parameter));
            if (object instanceof DataImport) {
                importFromDefinition(_parameter, object);
            } else if (object instanceof EFapsObject) {
                importObjectFromXML(_parameter, (EFapsObject) object);
            } else if (object instanceof ObjectList) {
                importListFromXML(_parameter, (ObjectList) object);
            }
        } catch (final JAXBException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        }
        return new Return();
    }

    /**
     * @param _parameter
     * @param _object
     */
    protected void importObjectFromXML(final Parameter _parameter,
                                       final EFapsObject _object)
        throws EFapsException
    {
        System.out.println(_object);
        _object.create();
    }

    /**
     * @param _parameter
     * @param _object
     */
    protected void importListFromXML(final Parameter _parameter,
                                     final ObjectList _list)
        throws EFapsException
    {
        System.out.println(_list);
        for (final EFapsObject obj : _list.getObjects()) {
            obj.create();
        }
    }

    protected void importFromDefinition(final Parameter _parameter,
                                        final Object _object)
        throws EFapsException
    {
        final DataImport dataImp = (DataImport) _object;
        dataImp.setUrl(getUrl(_parameter));
        final Map<String, Instance> keys = new HashMap<>();
        for (final Definition definition : dataImp.getDefinition()) {
            AbstractImport_Base.LOG.info("Starting with definition: '{}'", definition.getName());
            final List<String[]> values = readCSV(_parameter, dataImp, definition);
            final Map<String, Integer> headers = readHeader(_parameter, definition, values);
            values.remove(0);
            int j = 1;
            boolean execute = true;
            final Set<Integer> skip = new HashSet<>();
            AbstractImport_Base.LOG.info("Validating definition: '{}'", definition.getName());
            // validate
            for (final String[] value : values) {
                if (definition.isUpdate()) {
                    final IdentifierDef idDef = definition.getIdentifier();
                    if (idDef == null) {
                        AbstractImport_Base.LOG.error("Definition '{}' has update defined but no identifier.",
                                        definition.getName());
                    } else {
                        final Boolean hasInstance = idDef.hasInstance(_parameter, definition, headers, value, j);
                        if (hasInstance) {
                            AbstractImport_Base.LOG.debug("Instance found={} in line {}", hasInstance, j);
                        } else {
                            AbstractImport_Base.LOG.warn("no Instance found in line {}", j);
                        }
                    }
                }
                final Boolean valid = definition.getTypeDef().validate(_parameter, headers, value, j);
                if (!valid) {
                    execute = false;
                }
                final Type type = definition.getTypeDef().getType(headers, value);
                String typeName = "no Type Name given";
                if (type != null) {
                    typeName = type.getName();
                }
                for (final AttrDef attr : definition.getTypeDef().getAttributes()) {
                    if (attr.applies(typeName)) {
                        Boolean check = attr.validate(_parameter, headers, value, j);
                        if (attr.isParentLink()) {
                            final String parentKey = attr.getValue(_parameter, headers, value, j);
                            if (!parentKey.isEmpty() && !keys.containsKey(parentKey)) {
                                AbstractImport_Base.LOG.error("ParentKey '{}' in row {} not found.",
                                                parentKey, j);
                                check = false;
                            }
                        }
                        if (!check) {
                            execute = false;
                            skip.add(j);
                        }
                    }
                }
                for (final AttrSetDef attrSetDef : definition.getTypeDef().getAttributeSets()) {
                    final Boolean check = attrSetDef.validate(_parameter, headers, value, j);
                    if (!check) {
                        execute = false;
                        skip.add(j);
                    }
                }

                for (final ClassificationDef classDef : definition.getTypeDef().getClassifications()) {
                    Boolean check = classDef.validate(_parameter, headers, value, j);
                    if (classDef != null) {
                        for (final AttrDef attr : classDef.getAttributes()) {
                            check = attr.validate(_parameter, headers, value, j);
                        }
                    }
                    if (!check) {
                        execute = false;
                        skip.add(j);
                    }
                }

                if (definition.hasKey() && execute) {
                    keys.put(value[headers.get(definition.getKeyColumn())], null);
                }
                j++;
            }
            if (execute && definition.isExecute() || definition.isExecute() && definition.isForce()) {
                AbstractImport_Base.LOG.info("Importing definition: '{}'", definition.getName());
                // create
                j = 1;
                for (final String[] value : values) {
                    if (!execute && skip.contains(j)) {
                        AbstractImport_Base.LOG.info("Skipped Line: '{}': {} ", j, Arrays.toString(value));
                    } else {
                        boolean isUpdate;
                        final Type type = definition.getTypeDef().getType(headers, value);
                        Update update;
                        if (definition.isUpdate()
                                        && definition.getIdentifier().hasInstance(_parameter, definition,
                                                        headers, value, j)) {
                            update = new Update(definition.getIdentifier().getInstance(_parameter, definition,
                                            headers, value, j));
                            isUpdate = true;
                        } else {
                            update = new Insert(type);
                            isUpdate = false;
                        }
                        for (final AttrDef attr : definition.getTypeDef().getAttributes()) {
                            if ((!isUpdate || isUpdate && attr.isOverwrite())
                                            && attr.applies(type.getName())) {
                                if (attr.isParentLink()) {
                                    final String parentKey = attr.getValue(_parameter, headers, value, j);
                                    update.add(attr.getName(), keys.get(parentKey));
                                } else {
                                    update.add(attr.getName(), attr.getValue(_parameter, headers, value, j));
                                }
                            }
                        }
                        add2TypeUpdate(_parameter, definition, headers, values, value, update, j);

                        execute(_parameter, definition, update);

                        if (definition.hasKey()) {
                            keys.put(value[headers.get(definition.getKeyColumn())], update.getInstance());
                        }
                        if (definition.getTypeDef().getAttributeSets() != null) {
                            updateAttributeSet(_parameter, definition, headers, value,
                                            update.getInstance(), j);
                        }

                        if (definition.getTypeDef().getClassifications() != null) {
                            updateClassification(_parameter, definition, headers, value,
                                            update.getInstance(), j);
                        }
                        if (definition.getRowListeners() != null) {
                            for (final var listener : definition.getRowListeners()) {
                                listener.execute(_parameter, update.getInstance(), headers, value, j);
                            }
                        }
                        add2Row(_parameter, definition, headers, values, value, update.getInstance(), j);
                    }
                    j++;
                }
            }
            AbstractImport_Base.LOG.info("Finished definition: '{}'", definition.getName());
        }
    }


    protected Source getSource4DataImport(final Parameter _parameter)
        throws EFapsException
    {
        Source source = null;
        try {
            final URL url = getUrl(_parameter);
            final URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            source = new StreamSource(new InputStreamReader(connection.getInputStream()));
        } catch (final MalformedURLException e) {
            AbstractImport_Base.LOG.error("MalformedURLException", e);
        } catch (final IOException e) {
            AbstractImport_Base.LOG.error("IOException", e);
        }
        return source;
    }

    /**
     * @param _parameter
     * @return
     */
    protected URL getUrl(final Parameter _parameter)
    {
        final String xmlFile = _parameter.getParameterValue("valueField");
        URL ret = null;
        try {
            ret = new URL(xmlFile);
        } catch (final MalformedURLException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        }
        return ret;
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
        } else {
            _update.execute();
        }
    }

    protected void updateAttributeSet(final Parameter _parameter,
                                      final Definition definition,
                                      final Map<String, Integer> _headers,
                                      final String[] _value,
                                      final Instance _instance,
                                      final Integer _idx)
        throws EFapsException
    {

        AbstractImport_Base.LOG.trace("preparing updates for AttributeSets");
        for (final AttrSetDef attrSetDef : definition.getTypeDef().getAttributeSets()) {
            attrSetDef.execute(_parameter, _instance, _headers, _value, _idx);
        }
    }

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _def definition
     * @param _headers header mapping
     * @param _value values of the current row
     * @param _instance instance of the created object
     * @param _idx index of the current row
     * @throws EFapsException on error
     */
    protected void updateClassification(final Parameter _parameter,
                                        final Definition _def,
                                        final Map<String, Integer> _headers,
                                        final String[] _value,
                                        final Instance _instance,
                                        final Integer _idx)
        throws EFapsException
    {
        AbstractImport_Base.LOG.trace("preparing updates for classifications");
        final List<ClassificationDef> classifications = _def.getTypeDef().getClassifications();
        final Set<Classification> inserted = new HashSet<>();
        for (final ClassificationDef classification : classifications) {
            AbstractImport_Base.LOG.trace("preparing inserts for: {}", classification);
            for (final Classification clazz : classification.getClassifications(_parameter, _headers, _value)) {
                if (!inserted.contains(clazz)) {
                    inserted.add(clazz);

                    final PrintQuery print = new PrintQuery(_instance);
                    final SelectBuilder selClazzInst = SelectBuilder.get().clazz(clazz.getUUID()).instance();
                    print.addSelect(selClazzInst);
                    print.executeWithoutAccessCheck();

                    final Instance classInst = print.<Instance>getSelect(selClazzInst);
                    Update classUpdate;
                    boolean isUpdate;
                    // if no clazzInst yet it must be created
                    if (classInst != null && classInst.isValid()) {
                        classUpdate = new Update(classInst);
                        isUpdate = true;
                    } else {
                        final Insert relInsert = new Insert(clazz.getClassifyRelationType());
                        relInsert.add(clazz.getRelLinkAttributeName(), _instance);
                        relInsert.add(clazz.getRelTypeAttributeName(), clazz.getId());
                        execute(_parameter, _def, relInsert);
                        classUpdate = new Insert(clazz);
                        isUpdate = false;
                    }
                    classUpdate.add(clazz.getLinkAttributeName(), _instance);
                    final ClassificationDef clazzDef = _def.getTypeDef().getClassificationDefByName(clazz.getName());
                    if (clazzDef != null) {
                        for (final AttrDef attr : clazzDef.getAttributes()) {
                            if (!isUpdate || isUpdate && attr.isOverwrite()) {
                                classUpdate.add(attr.getName(), attr.getValue(_parameter, _headers, _value, _idx));
                            }
                        }
                    }
                    execute(_parameter, _def, classUpdate);
                }
            }
        }
    }

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _definition defintion the header belongs to
     * @param _values list of row values
     * @return header mapping
     */
    protected Map<String, Integer> readHeader(final Parameter _parameter,
                                              final Definition _definition,
                                              final List<String[]> _values)
    {
        AbstractImport_Base.LOG.trace("Reading the Header from the CSVFile.");
        final Map<String, Integer> ret = new HashMap<>();
        final String[] vals = _values.get(0);
        int i = 0;
        for (final String val : vals) {
            if (!val.isEmpty()) {
                ret.put(val.trim(), i);
            }
            i++;
        }
        AbstractImport_Base.LOG.debug("Header found: {}", ret);
        return ret;
    }

    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @param _dataImport the import defintion
     * @param _definition defintion the values belong to
     * @return list of row values
     */
    protected List<String[]> readCSV(final Parameter _parameter,
                                     final DataImport _dataImport,
                                     final Definition _definition)
    {
        final List<String[]> ret = new ArrayList<>();
        try {
            AbstractImport_Base.LOG.trace("Reading the CSV File.");
            final URL relative = new URL(_dataImport.getUrl(), _definition.getFile());
            final URLConnection connection = relative.openConnection();
            final InputStreamReader inReader = new InputStreamReader(connection.getInputStream());
            final CSVReader reader = new CSVReader(inReader, CSVParser.DEFAULT_SEPARATOR,
                            CSVParser.DEFAULT_QUOTE_CHARACTER, _definition.getSkipLine());
            ret.addAll(reader.readAll());
            reader.close();
        } catch (final IOException e) {
            AbstractImport_Base.LOG.error("Catched error on reading CSV from '{}'", e, _dataImport);
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
        final List<Class<?>> clazzes = new ArrayList<>();
        clazzes.addAll(EFapsAttributes.CLASSMAPPING.values());
        clazzes.add(EFapsObject.class);
        clazzes.add(ObjectList.class);
        clazzes.add(UoMTypeAttribute.class);
        clazzes.add(DimensionTypeAttribute.class);
        clazzes.add(TypeDef.class);
        clazzes.add(Definition.class);
        clazzes.add(DataImport.class);
        clazzes.add(AttrDef.class);
        clazzes.add(ClassificationDef.class);
        clazzes.add(IdentifierDef.class);
        return clazzes.toArray(new Class<?>[clazzes.size()]);
    }

    /**
     * To be used by implementations.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _definition current Defintion
     * @param _headers Headers
     * @param _values List of all values
     * @param _value value for the current row
     * @param _insert insert yo add 2
     * @param _j row idex
     * @throws EFapsException on error
     */
    protected void add2TypeUpdate(final Parameter _parameter,
                                  final Definition _definition,
                                  final Map<String, Integer> _headers,
                                  final List<String[]> _values,
                                  final String[] _value,
                                  final Update _update,
                                  final int _j)
        throws EFapsException
    {
        // To be used by implementations
    }

    /**
     * To be used by implementations.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _definition current Defintion
     * @param _headers Headers
     * @param _values List of all values
     * @param _value value for the current row
     * @param _instance Instance of the created object
     * @param _j row idex
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

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

package org.efaps.esjp.data.columns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.InstanceQuery;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.esjp.data.IColumnValidate;
import org.efaps.esjp.data.IColumnValue;
import org.efaps.esjp.data.jaxb.AbstractDef;
import org.efaps.esjp.data.jaxb.AttrDef;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("ba04bec1-c2a5-4030-b986-2ae2b455355b")
@EFapsRevision("$Rev$")
public class QueryColumn
    implements IColumnValue, IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(QueryColumn.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean validate(final Parameter _parameter,
                            final AbstractDef _def,
                            final Map<String, Integer> _headers,
                            final String[] _value,
                            final Integer _idx)
        throws EFapsException
    {
        Boolean ret;
        if (_def instanceof AttrDef) {
            final String typeColumn = ((AttrDef) _def).getProperty("TypeColumn");
            final String selectColumn = ((AttrDef) _def).getProperty("SelectColumn");
            final String[] queryAttributes = ((AttrDef) _def).analyseProperty("QueryAttribute");
            final String[] queryValues = ((AttrDef) _def).analyseProperty("QueryValue");

            // validate
            if (typeColumn == null || selectColumn == null || queryAttributes.length == 0 || queryValues.length == 0) {
                QueryColumn.LOG.error("Must have all properties 'TypeColumn', "
                                + "'SelectColumn', 'QueryAttribute', 'QueryValue'.");
                ret = false;
            } else {
                if (queryAttributes.length != queryValues.length) {
                    QueryColumn.LOG.error(" 'QueryAttribute' and 'QueryValue' must have the same size.");
                    ret = false;
                } else {
                    final String type = _value[_headers.get(typeColumn)].trim();
                    final List<String>queryAttr = new ArrayList<String>();
                    for (final String queryAttribute : queryAttributes) {
                        queryAttr.add(_value[_headers.get(queryAttribute)].trim());
                    }
                    final List<String>queryVals = new ArrayList<String>();
                    for (final String queryValue : queryValues) {
                        queryVals.add(_value[_headers.get(queryValue)].trim());
                    }

                    final QueryBuilder queryBldr = new QueryBuilder(Type.get(type));
                    final Iterator<String> attrIter = queryAttr.iterator();
                    final Iterator<String> valIter = queryVals.iterator();

                    while (attrIter.hasNext() && valIter.hasNext()) {
                        queryBldr.addWhereAttrEqValue(attrIter.next(), valIter.next());
                    }
                    final InstanceQuery query = queryBldr.getQuery();
                    final List<Instance> instances = query.executeWithoutAccessCheck();

                    if (instances.size() == 1) {
                        QueryColumn.LOG.debug("Row: {} - {}", _idx, instances.get(0));
                        ret = true;
                    } else {
                        QueryColumn.LOG.warn("wrong number of Objects found in Row: {} - {}", _idx, _def);
                        ret = false;
                    }
                }
            }
        } else {
            QueryColumn.LOG.error("Validation only works for AttrDef.");
            ret = false;
        }
        return ret;
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue(final Parameter _parameter,
                           final AttrDef _attrDef,
                           final Map<String, Integer> _headers,
                           final String[] _value,
                           final Integer _idx)
        throws EFapsException
    {
        Object ret = null;
        final String typeColumn = _attrDef.getProperty("TypeColumn");
        final String selectColumn = _attrDef.getProperty("SelectColumn");
        final String[] queryAttributes = _attrDef.analyseProperty("QueryAttribute");
        final String[] queryValues = _attrDef.analyseProperty("QueryValue");
        final String type = _value[_headers.get(typeColumn)].trim();
        final List<String>queryAttr = new ArrayList<String>();
        for (final String queryAttribute : queryAttributes) {
            queryAttr.add(_value[_headers.get(queryAttribute)].trim());
        }
        final List<String>queryVals = new ArrayList<String>();
        for (final String queryValue : queryValues) {
            queryVals.add(_value[_headers.get(queryValue)].trim());
        }

        final QueryBuilder queryBldr = new QueryBuilder(Type.get(type));
        final Iterator<String> attrIter = queryAttr.iterator();
        final Iterator<String> valIter = queryVals.iterator();

        while (attrIter.hasNext() && valIter.hasNext()) {
            queryBldr.addWhereAttrEqValue(attrIter.next(), valIter.next());
        }
        final MultiPrintQuery multi = queryBldr.getPrint();
        final String select = _value[_headers.get(selectColumn)];
        multi.addSelect(select);
        multi.executeWithoutAccessCheck();

        if (multi.next()) {
            ret = multi.getSelect(select);
            QueryColumn.LOG.debug("Row: {} - {}", _idx, ret);
        }
        return ret == null  ? null : String.valueOf(ret);
    }

    @Override
    public Collection<String> getColumnNames(final Parameter _parameter,
                                             final AbstractDef _def)
    {
        final List<String> ret = new ArrayList<>();
        ret.add(((AttrDef) _def).getProperty("TypeColumn"));
        ret.add(((AttrDef) _def).getProperty("SelectColumn"));
        CollectionUtils.addAll(ret, ((AttrDef) _def).analyseProperty("QueryAttribute"));
        CollectionUtils.addAll(ret, ((AttrDef) _def).analyseProperty("QueryValue"));
        return ret;
    }
}

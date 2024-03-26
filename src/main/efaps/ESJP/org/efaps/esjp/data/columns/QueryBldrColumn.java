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
package org.efaps.esjp.data.columns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
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
 *
 */
@EFapsUUID("2fbe8e81-088f-4852-a985-252a1fe29099")
@EFapsApplication("eFapsApp-Data")
public class QueryBldrColumn
    implements IColumnValue, IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(QueryBldrColumn.class);

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
            final String type = ((AttrDef) _def).getProperty("Type");
            final String select = ((AttrDef) _def).getProperty("Select");
            final String[] queryAttributes = ((AttrDef) _def).analyseProperty("QueryAttribute");
            final String[] queryColumns = ((AttrDef) _def).analyseProperty("QueryColumn");

            // validate
            if (type == null || select == null || queryAttributes.length == 0 || queryColumns.length == 0) {
                QueryBldrColumn.LOG.error("Must have all properties 'Type', "
                                + "'Select', 'QueryAttribute', 'QueryColumn'.");
                ret = false;
            } else {
                if (queryAttributes.length != queryColumns.length) {
                    QueryBldrColumn.LOG.error(" 'QueryAttribute' and 'QueryValue' must have the same size.");
                    ret = false;
                } else {

                    final List<String>queryAttr = new ArrayList<>();
                    for (final String queryAttribute : queryAttributes) {
                        queryAttr.add(queryAttribute);
                    }
                    final List<String>queryVals = new ArrayList<>();
                    for (final String queryValue : queryColumns) {
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
                        QueryBldrColumn.LOG.debug("Row: {} - {}", _idx, instances.get(0));
                        ret = true;
                    } else {
                        QueryBldrColumn.LOG.warn("wrong number: {} Objects found in Row: {} - {}",
                                        instances.size(), _idx, _def);
                        ret = false;
                    }
                }
            }
        } else {
            QueryBldrColumn.LOG.error("Validation only works for AttrDef.");
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
        final String type = _attrDef.getProperty("Type");
        final String select = _attrDef.getProperty("Select");
        final String[] queryAttributes = _attrDef.analyseProperty("QueryAttribute");
        final String[] queryColumns = _attrDef.analyseProperty("QueryColumn");

        final List<String> queryAttr = new ArrayList<>();
        for (final String queryAttribute : queryAttributes) {
            queryAttr.add(queryAttribute);
        }
        final List<String> queryVals = new ArrayList<>();
        for (final String queryValue : queryColumns) {
            queryVals.add(_value[_headers.get(queryValue)].trim());
        }

        final QueryBuilder queryBldr = new QueryBuilder(Type.get(type));
        final Iterator<String> attrIter = queryAttr.iterator();
        final Iterator<String> valIter = queryVals.iterator();

        while (attrIter.hasNext() && valIter.hasNext()) {
            queryBldr.addWhereAttrEqValue(attrIter.next(), valIter.next());
        }

        final MultiPrintQuery multi = queryBldr.getPrint();
        multi.addSelect(select);
        multi.executeWithoutAccessCheck();

        if (multi.next()) {
            ret = multi.getSelect(select);
            QueryBldrColumn.LOG.debug("Row: {} - {}", _idx, ret);
        }
        return ret == null ? null : String.valueOf(ret);
    }

    @Override
    public Collection<String> getColumnNames(final Parameter _parameter,
                                             final AbstractDef _def)
    {
        final String[] queryColumns = ((AttrDef) _def).analyseProperty("QueryColumn");
        final List<String> ret = new ArrayList<>();
        CollectionUtils.addAll(ret, queryColumns);
        return ret;
    }
}

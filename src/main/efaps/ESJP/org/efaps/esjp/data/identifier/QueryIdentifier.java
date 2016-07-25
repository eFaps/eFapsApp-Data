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

package org.efaps.esjp.data.identifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.InstanceQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.esjp.data.IIdentifier;
import org.efaps.esjp.data.jaxb.Definition;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("75763a03-2a7d-4836-9680-77d04dc9d73e")
@EFapsApplication("eFapsApp-Data")
public class QueryIdentifier
    implements IIdentifier
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(QueryIdentifier.class);

    @Override
    public Instance getInstance(final Parameter _parameter,
                                final Definition _definition,
                                final Map<String, Integer> _headers,
                                final String[] _value,
                                final Integer _idx)
        throws EFapsException
    {
        final String[] queryAttributes = _definition.getIdentifier().analyseProperty("QueryAttribute");
        final String[] queryColumns = _definition.getIdentifier().analyseProperty("QueryColumn");

        final List<String> queryAttr = new ArrayList<>();
        for (final String queryAttribute : queryAttributes) {
            queryAttr.add(queryAttribute);
        }
        final List<String> queryVals = new ArrayList<>();
        for (final String queryValue : queryColumns) {
            queryVals.add(_value[_headers.get(queryValue)].trim());
        }
        final Type type = _definition.getTypeDef().getType(_headers, _value);
        final QueryBuilder queryBldr = new QueryBuilder(type);
        final Iterator<String> attrIter = queryAttr.iterator();
        final Iterator<String> valIter = queryVals.iterator();

        while (attrIter.hasNext() && valIter.hasNext()) {
            queryBldr.addWhereAttrEqValue(attrIter.next(), valIter.next());
        }
        final InstanceQuery query = queryBldr.getQuery();
        final List<Instance> instances = query.executeWithoutAccessCheck();

        Instance ret = null;
        if (instances.size() > 0) {
            QueryIdentifier.LOG.debug("Row: {} - {}", _idx, instances.get(0));
            ret = instances.get(0);
        }
        return ret;
    }

    @Override
    public Boolean hasInstance(final Parameter _parameter,
                               final Definition _definition,
                               final Map<String, Integer> _headers,
                               final String[] _value,
                               final Integer _idx)
        throws EFapsException
    {
        final String[] queryAttributes = _definition.getIdentifier().analyseProperty("QueryAttribute");
        final String[] queryColumns = _definition.getIdentifier().analyseProperty("QueryColumn");

        boolean ret;
        // validate
        if (queryAttributes.length == 0 || queryColumns.length == 0) {
            QueryIdentifier.LOG.error("Must have properties 'QueryAttribute', 'QueryColumn'.");
            ret = false;
        } else {
            if (queryAttributes.length != queryColumns.length) {
                QueryIdentifier.LOG.error(" 'QueryAttribute' and 'QueryValue' must have the same size.");
                ret = false;
            } else {

                final List<String> queryAttr = new ArrayList<>();
                for (final String queryAttribute : queryAttributes) {
                    queryAttr.add(queryAttribute);
                }
                final List<String> queryVals = new ArrayList<>();
                for (final String queryValue : queryColumns) {
                    queryVals.add(_value[_headers.get(queryValue)].trim());
                }
                final Type type = _definition.getTypeDef().getType(_headers, _value);
                final QueryBuilder queryBldr = new QueryBuilder(type);
                final Iterator<String> attrIter = queryAttr.iterator();
                final Iterator<String> valIter = queryVals.iterator();

                while (attrIter.hasNext() && valIter.hasNext()) {
                    queryBldr.addWhereAttrEqValue(attrIter.next(), valIter.next());
                }
                final InstanceQuery query = queryBldr.getQuery();
                final List<Instance> instances = query.executeWithoutAccessCheck();

                if (instances.size() < 1) {
                    QueryIdentifier.LOG.debug("Row: {} - no Instance found", _idx);
                    ret = false;
                } else if (instances.size() == 1) {
                    QueryIdentifier.LOG.debug("Row: {} - {}", _idx, instances.get(0));
                    ret = true;
                } else {
                    QueryIdentifier.LOG.warn("wrong number: {} of Instances found in Row: {}", instances.size(), _idx);
                    ret = false;
                }
            }
        }
        return ret;
    }

}

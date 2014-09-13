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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.InstanceQuery;
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
@EFapsUUID("abb661ae-49b0-4267-8378-59aae55b5175")
@EFapsRevision("$Rev$")
public class JAASSystemColumn
implements IColumnValue, IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(JAASSystemColumn.class);

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
            final String column = ((AttrDef) _def).getProperty("Column");
            if (column != null) {
                final String jaasName = _value[_headers.get(column)].trim();
                // Admin_User_JAASSystem
                final QueryBuilder queryBldr = new QueryBuilder(UUID.fromString("28e45c59-946d-4502-94b9-58a1bf23ab88"));
                queryBldr.addWhereAttrEqValue("Name", jaasName);
                final InstanceQuery query = queryBldr.getQuery();
                final List<Instance> result = query.executeWithoutAccessCheck();
                if (query.next()) {
                    if (result.size() == 1) {
                        JAASSystemColumn.LOG.debug("Row: {} - {}", _idx, query.getCurrentValue());
                        ret = true;
                    } else {
                        JAASSystemColumn.LOG.warn("JAASSytem no unique for Row: {} - {}", _idx, result);
                        ret = false;
                    }
                } else {
                    JAASSystemColumn.LOG.warn("no JAASSytem found in Row: {} - {}", _idx, _def);
                    ret = false;
                }
            } else {
                JAASSystemColumn.LOG.error("Missing property 'Column'.");
                ret = false;
            }
        } else {
            JAASSystemColumn.LOG.error("Validation only works for AttrDef.");
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
        String ret = null;
        final String column = _attrDef.getProperty("Column");
        if (column != null) {
            final String jaasName = _value[_headers.get(column)].trim();
            // Admin_User_JAASSystem
            final QueryBuilder queryBldr = new QueryBuilder(UUID.fromString("28e45c59-946d-4502-94b9-58a1bf23ab88"));
            queryBldr.addWhereAttrEqValue("Name", jaasName);
            final InstanceQuery query = queryBldr.getQuery();
            query.executeWithoutAccessCheck();
            if (query.next()) {
                JAASSystemColumn.LOG.debug("Row: {} - JAASSystem: '{}'", _idx, query.getCurrentValue());
                ret = Long.valueOf(query.getCurrentValue().getId()).toString();
            }
        } else {
            JAASSystemColumn.LOG.error("Missing property 'Column'.");
        }
        return ret;
    }

    @Override
    public Collection<String> getColumnNames(final Parameter _parameter,
                                             final AbstractDef _def)
    {
        final List<String> ret = new ArrayList<>();
        final String column = ((AttrDef) _def).getProperty("Column");
        ret.add(column);
        return ret;
    }
}

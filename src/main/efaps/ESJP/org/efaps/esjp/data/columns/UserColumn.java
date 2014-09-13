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

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.admin.user.AbstractUserObject;
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
@EFapsUUID("cd46066d-a810-4eaf-8f61-33cebbf80285")
@EFapsRevision("$Rev$")
public class UserColumn
    implements IColumnValue, IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserColumn.class);

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
            final String column = ((AttrDef)_def).getProperty("Column");
            if (column != null) {
                final String userName = _value[_headers.get(column)].trim();
                final AbstractUserObject user = AbstractUserObject.getUserObject(userName);
                if (userName != null) {
                    UserColumn.LOG.debug("Row: {} - {}", _idx, user);
                    ret = true;
                } else {
                    UserColumn.LOG.warn("no User found in Row: {} - {}", _idx, _def);
                    ret = false;
                }
            } else {
                UserColumn.LOG.error("Missing property 'Column'.");
                ret = false;
            }
        } else {
            UserColumn.LOG.error("Validation only works for AttrDef.");
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
            final String userName = _value[_headers.get(column)].trim();
            final AbstractUserObject user = AbstractUserObject.getUserObject(userName);
            if (user != null) {
                UserColumn.LOG.debug("Row: {} - User: '{}'", _idx, user);
                ret = Long.valueOf(user.getId()).toString();
            }
        } else {
            UserColumn.LOG.error("Missing property 'Column'.");
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

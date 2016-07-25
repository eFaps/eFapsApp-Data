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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
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
@EFapsUUID("5e33f385-d825-45cd-981f-62a88b9afba8")
@EFapsApplication("eFapsApp-Data")
public class BooleanColumn
    implements IColumnValue, IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(BooleanColumn.class);

    private static Map<String,Boolean> DEFAULTMAPPING = new HashMap<>();
    static {
        BooleanColumn.DEFAULTMAPPING.put("true", true);
        BooleanColumn.DEFAULTMAPPING.put("True", true);
        BooleanColumn.DEFAULTMAPPING.put("TRUE", true);
        BooleanColumn.DEFAULTMAPPING.put("yes", true);
        BooleanColumn.DEFAULTMAPPING.put("Yes", true);
        BooleanColumn.DEFAULTMAPPING.put("YES", true);
        BooleanColumn.DEFAULTMAPPING.put("si", true);
        BooleanColumn.DEFAULTMAPPING.put("Si", true);
        BooleanColumn.DEFAULTMAPPING.put("SI", true);
        BooleanColumn.DEFAULTMAPPING.put("false", false);
        BooleanColumn.DEFAULTMAPPING.put("False", false);
        BooleanColumn.DEFAULTMAPPING.put("FALSE", false);
        BooleanColumn.DEFAULTMAPPING.put("no", false);
        BooleanColumn.DEFAULTMAPPING.put("No", false);
        BooleanColumn.DEFAULTMAPPING.put("NO", false);
        BooleanColumn.DEFAULTMAPPING.put("", false);
    }

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
            if (column == null) {
                BooleanColumn.LOG.error("Properties wrong! 'Column' must be given.");
                ret = false;
            } else {
                final String booleanStr = _value[_headers.get(column)].trim();
                try {
                    Boolean test = null;
                    if (BooleanColumn.DEFAULTMAPPING.containsKey(booleanStr)) {
                        test = BooleanColumn.DEFAULTMAPPING.get(booleanStr);
                    } else {
                        test = Boolean.valueOf(booleanStr);
                    }
                    if (test == null) {
                        BooleanColumn.LOG.error("Colud not Parse/Analyse boolean for line: {}", _idx);
                        ret = false;
                    } else {
                        BooleanColumn.LOG.debug("Parsed/Analysed boolean: {} for line {} to {}", booleanStr,
                                        _idx, test);
                        ret = true;
                    }
                } catch (final Exception e) {
                    BooleanColumn.LOG.error("Catched parsing error on line: {}", _idx);
                    ret = false;
                }
            }
        } else {
            BooleanColumn.LOG.error("Validation only works for AttrDef.");
            ret = false;
        }
        return ret;
    }

    @Override
    public String getValue(final Parameter _parameter,
                           final AttrDef _attrDef,
                           final Map<String, Integer> _headers,
                           final String[] _value,
                           final Integer _idx)
        throws EFapsException
    {
        String ret;
        final String column = _attrDef.getProperty("Column");
        final String booleanStr = _value[_headers.get(column)].trim();
        Boolean test = null;
        if (BooleanColumn.DEFAULTMAPPING.containsKey(booleanStr)) {
            test = BooleanColumn.DEFAULTMAPPING.get(booleanStr);
        } else {
            test = Boolean.valueOf(booleanStr);
        }
        ret = test.toString();
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

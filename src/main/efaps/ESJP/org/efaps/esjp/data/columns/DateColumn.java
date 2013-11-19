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

import java.util.Map;

import org.efaps.admin.event.Parameter;
import org.efaps.db.Context;
import org.efaps.esjp.data.IColumnValidate;
import org.efaps.esjp.data.IColumnValue;
import org.efaps.esjp.data.jaxb.AbstractDef;
import org.efaps.esjp.data.jaxb.AttrDef;
import org.efaps.util.EFapsException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class DateColumn
    implements IColumnValue, IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(DateColumn.class);

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
            final String pattern = ((AttrDef) _def).getProperty("Pattern");
            if (column == null || pattern == null) {
                DateColumn.LOG.error("Properties wrong! 'Column' and 'Pattern' must be given.");
                ret = false;
            } else {
                final String dateStr = _value[_headers.get(column)].trim();
                try {
                    final DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
                    final DateTime dateTime = formatter.parseDateTime(dateStr);
                    final DateTimeFormatter isoFormatter = ISODateTimeFormat.dateTime()
                                    .withLocale(Context.getThreadContext().getLocale());
                    final String str = dateTime.toString(isoFormatter);
                    DateColumn.LOG.debug("Parsed date: {} for line {}", str, _idx);
                    ret = true;
                } catch (final Exception e) {
                    DateColumn.LOG.error("Catched parsing error on line: {}", _idx);
                    ret = false;
                }
            }
        } else {
            DateColumn.LOG.error("Validation only works for AttrDef.");
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
        final String column = _attrDef.getProperty("Column");
        final String pattern = _attrDef.getProperty("Pattern");
        final String dateStr = _value[_headers.get(column)].trim();
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        final DateTime dateTime = formatter.parseDateTime(dateStr);
        final DateTimeFormatter isoFormatter = ISODateTimeFormat.dateTime()
                        .withLocale(Context.getThreadContext().getLocale());
        return dateTime.toString(isoFormatter);
    }
}

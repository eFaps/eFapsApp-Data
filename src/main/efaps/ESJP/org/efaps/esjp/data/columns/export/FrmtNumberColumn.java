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

package org.efaps.esjp.data.columns.export;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brsanthu.dataexporter.model.NumberColumn;
import com.brsanthu.dataexporter.util.Util;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class FrmtNumberColumn
    extends NumberColumn
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(FrmtNumberColumn.class);

    private DecimalFormat formatter = null;

    private Locale locale;

    /**
     * @param _name
     * @param _title
     * @param _width
     * @param _formatter
     */
    public FrmtNumberColumn(final String _name,
                            final int _width,
                            final int _precision)
    {
        super(_name, _width, _precision);
        setGrouping(false);
    }

    public FrmtNumberColumn setLocale(final Locale _locale)
    {
        this.locale = _locale;
        return this;
    }

    public Locale getLocale()
    {
        Locale ret;
        if (this.locale == null) {
            ret = Locale.US;
        } else {
            ret = this.locale;
        }
        return ret;
    }

    @Override
    public String format(final Object _value)
    {

        if (this.formatter == null) {
            String formatString = "";
            if (isGrouping()) {
                formatString = "#,###,###";
            } else {
                formatString = "0";
            }

            if (isBracketNegitive()) {
                formatString = formatString + ";(#)";
            }

            this.formatter = (DecimalFormat) NumberFormat.getNumberInstance(getLocale());

            if (getPrecision() > 0) {
                this.formatter.applyPattern(formatString + "." + Util.createString("0", getPrecision()));
            } else {
                this.formatter.applyPattern(formatString);
            }
        }

        String formattedString = null;

        try {
            if (_value instanceof Number) {
                formattedString = this.formatter.format(_value);
            } else {
                formattedString = this.formatter.format(BigDecimal.ZERO);;
            }
        } catch (final Exception e) {
            FrmtNumberColumn.LOG.error("Cateched error", e);
        }
        return formattedString;
    }

}

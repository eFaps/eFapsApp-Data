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

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.dataexporter.model.NumberColumn;
import org.efaps.dataexporter.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("33938224-0525-4bd1-a4e3-c687000a569b")
@EFapsApplication("eFapsApp-Data")
public class FrmtNumberColumn
    extends NumberColumn
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(FrmtNumberColumn.class);

    /** The formatter. */
    private DecimalFormat formatter = null;

    /** The locale. */
    private Locale locale;

    /** The leading zeros. */
    private int leadingZeros = -1;

    /**
     * Instantiates a new frmt number column.
     *
     * @param _name the _name
     * @param _width the _width
     * @param _precision the _precision
     */
    public FrmtNumberColumn(final String _name,
                            final int _width,
                            final int _precision)
    {
        this(_name, _width, _precision, -1);
    }

    /**
     * Instantiates a new frmt number column.
     *
     * @param _name the _name
     * @param _width the _width
     * @param _precision the _precision
     * @param _leadingZeros the _leading zeros
     */
    public FrmtNumberColumn(final String _name,
                            final int _width,
                            final int _precision,
                            final int _leadingZeros)
    {
        super(_name, _width, _precision);
        setGrouping(false);
        setLeadingZeros(_leadingZeros);
    }

    /**
     * Sets the locale.
     *
     * @param _locale the _locale
     * @return the frmt number column
     */
    public FrmtNumberColumn setLocale(final Locale _locale)
    {
        this.locale = _locale;
        return this;
    }

    public FrmtNumberColumn withTitle(final String title)
    {
        setTitle(title);
        return this;
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
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

    /**
     * Getter method for the instance variable {@link #leadingZeros}.
     *
     * @return value of instance variable {@link #leadingZeros}
     */
    public int getLeadingZeros()
    {
        return this.leadingZeros;
    }

    /**
     * Setter method for instance variable {@link #leadingZeros}.
     *
     * @param _leadingZeros value for instance variable {@link #leadingZeros}
     * @return the frmt number column
     */
    public FrmtNumberColumn setLeadingZeros(final int _leadingZeros)
    {
        this.leadingZeros = _leadingZeros;
        return this;
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
                formatString = formatString + "." + Util.createString("0", getPrecision());
            }

            if (getLeadingZeros() > 0 ) {
                formatString = Util.createString("0", getLeadingZeros() - 1) + formatString;
            }
            this.formatter.applyPattern(formatString);
        }

        String formattedString = null;

        try {
            if (_value instanceof Number) {
                formattedString = this.formatter.format(_value);
            } else {
                formattedString = this.formatter.format(BigDecimal.ZERO);
            }
        } catch (final Exception e) {
            FrmtNumberColumn.LOG.error("Cateched error", e);
        }
        return formattedString;
    }
}

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
package org.efaps.esjp.data.columns.export;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.dataexporter.model.CellDetails;
import org.efaps.dataexporter.model.StringColumn;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FrmtDateTimeColumn.
 */
@EFapsUUID("2f33c7d2-bd57-41db-8501-763ef9c2ce94")
@EFapsApplication("eFapsApp-Data")
public class FrmtDateTimeColumn
    extends StringColumn
{

    /** Logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(FrmtDateTimeColumn.class);

    /** The date format. */
    private String datePattern = null;

    /** The null value. */
    private String nullValue;

    /** The empty value. */
    private String emptyValue;

    /**
     * Instantiates a new frmt date time column.
     *
     * @param _name the name
     * @param _datePattern the date pattern
     */
    public FrmtDateTimeColumn(final String _name,
                              final String _datePattern)
    {
        super(_name);
    }

    public FrmtDateTimeColumn(final String _name,
                              final String title,
                              final int _maxWidth)
    {
        super(_name, title, _maxWidth);
    }

    public FrmtDateTimeColumn(final String _name,
                              final String title,
                              final int _maxWidth,
                              final String _datePattern)
    {
        super(_name, title, _maxWidth);
        this.datePattern = _datePattern;
    }

    /**
     * Instantiates a new frmt date time column.
     *
     * @param _name the name
     * @param _maxWidth the max width
     * @param _datePattern the date pattern
     */
    public FrmtDateTimeColumn(final String _name,
                              final int _maxWidth,
                              final String _datePattern)
    {
        super(_name, _maxWidth);
        this.datePattern = _datePattern;
    }

    /**
     * Sets the max width.
     *
     * @param _maxWidth the max width
     * @return the frmt date time column
     */
    public FrmtDateTimeColumn setMaxWidth(final int _maxWidth)
    {
        super.setWidth(_maxWidth);
        return this;
    }

    public FrmtDateTimeColumn setPattern(final String _datePattern)
    {
        this.datePattern = _datePattern;
        return this;
    }

    /* (non-Javadoc)
     * @see org.efaps.dataexporter.model.StringColumn#format(org.efaps.dataexporter.model.CellDetails)
     */
    @Override
    public String format(final CellDetails _cellDetails)
    {
        final String ret;
        if (_cellDetails.getCellValue() != null) {
            if (_cellDetails.getCellValue() instanceof DateTime) {
                final DateTime dtTmp = (DateTime) _cellDetails.getCellValue();
                String tmp =  dtTmp.toString(datePattern);
                if (tmp.length() > getWidth()) {
                    FrmtDateTimeColumn.LOG.warn("DateTime formated is longer than the especified width");
                    tmp = tmp.substring(0, getWidth());
                }
                ret = tmp;
            } else if (_cellDetails.getCellValue() instanceof TemporalAccessor) {
                final TemporalAccessor dtTmp = (TemporalAccessor) _cellDetails.getCellValue();
                String tmp = DateTimeFormatter.ofPattern(datePattern).format(dtTmp);
                if (tmp.length() > getWidth()) {
                    FrmtDateTimeColumn.LOG.warn("DateTime formated is longer than the especified width");
                    tmp = tmp.substring(0, getWidth());
                }
                ret = tmp;
            } else if (_cellDetails.getCellValue() instanceof String) {
                final String tmp = (String) _cellDetails.getCellValue();
                if (tmp.isEmpty() && this.emptyValue != null) {
                    ret = this.emptyValue;
                } else {
                    ret = tmp;
                }
            } else {
                FrmtDateTimeColumn.LOG.error("Expected DateTime instance but it is "
                            + _cellDetails.getCellValue().getClass().getName()
                            + " instance with value " + _cellDetails.getCellValue());
                ret = "";
            }
        } else if (this.nullValue != null) {
            ret = this.nullValue;
        } else {
            ret = "";
        }
        return ret;
    }

    /**
     * Sets the null value.
     *
     * @param _nullValue the null value
     * @return the frmt date time column
     */
    public FrmtDateTimeColumn setNullValue(final String _nullValue)
    {
        this.nullValue = _nullValue;
        return this;
    }

    /**
     * Sets the empty value.
     *
     * @param _emptyValue the empty value
     * @return the frmt date time column
     */
    public FrmtDateTimeColumn setEmptyValue(final String _emptyValue)
    {
        this.emptyValue = _emptyValue;
        return this;
    }
}

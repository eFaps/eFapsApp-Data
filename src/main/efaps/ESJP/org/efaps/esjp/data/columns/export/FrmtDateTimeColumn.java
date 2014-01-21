package org.efaps.esjp.data.columns.export;

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brsanthu.dataexporter.model.CellDetails;
import com.brsanthu.dataexporter.model.StringColumn;

public class FrmtDateTimeColumn
    extends StringColumn
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(FrmtDateTimeColumn.class);

    private SimpleDateFormat dateFormat = null;

    private String nullValue;

    private String emptyValue;
    /**
     * @param _name
     * @param _width
     * @param _align
     */
    public FrmtDateTimeColumn(final String _name,
                              final String _datePattern)
    {
        super(_name);
    }

    public FrmtDateTimeColumn(final String _name,
                              final int _maxWidth,
                              final String _datePattern)
    {
        super(_name, _maxWidth);
        this.dateFormat = new SimpleDateFormat(_datePattern);
    }

    public FrmtDateTimeColumn setMaxWidth(final int _maxWidth)
    {
        super.setWidth(_maxWidth);
        return this;
    }

    @Override
    public String format(final CellDetails _cellDetails)
    {
        final String ret;
        if (_cellDetails.getCellValue() != null) {
            if (_cellDetails.getCellValue() instanceof DateTime) {
                final DateTime dtTmp = (DateTime) _cellDetails.getCellValue();
                String tmp = this.dateFormat.format(dtTmp.toDate());
                if (tmp.length() > getWidth()) {
                    FrmtDateTimeColumn.LOG.warn("DateTime formated is longer than the especified width");
                    tmp = tmp.substring(0, getWidth());
                }
                ret = tmp;
            } else {
                if (_cellDetails.getCellValue() instanceof String) {
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
            }
        } else {
            if (this.nullValue != null) {
                ret = this.nullValue;
            } else {
                ret = "";
            }
        }
        return ret;
    }

    public FrmtDateTimeColumn setNullValue(final String _nullValue)
    {
        this.nullValue = _nullValue;
        return this;
    }

    public FrmtDateTimeColumn setEmptyValue(final String _emptyValue)
    {
        this.emptyValue = _emptyValue;
        return this;
    }
}

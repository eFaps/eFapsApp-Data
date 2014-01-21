package org.efaps.esjp.data.columns.export;

import com.brsanthu.dataexporter.model.CellDetails;
import com.brsanthu.dataexporter.model.StringColumn;

public class FrmtColumn
    extends StringColumn
{
    private String nullValue;
    private String emptyValue;

    /**
     * @param _name
     * @param _width
     * @param _align
     */
    public FrmtColumn(final String _name)
    {
        super(_name);
    }

    public FrmtColumn(final String _name,
                      final int _maxWidth)
    {
        super(_name, _maxWidth);
    }

    public FrmtColumn setMaxWidth(final int _maxWidth)
    {
        super.setWidth(_maxWidth);
        return this;
    }

    @Override
    public String format(final CellDetails _cellDetails)
    {
        String ret;
        if (_cellDetails.getCellValue() == null) {
            if (this.nullValue != null) {
                ret = this.nullValue;
            } else {
                ret = "";
            }
        } else {
            if (_cellDetails.getCellValue() instanceof String) {
                String tmp = (String) _cellDetails.getCellValue();
                if (tmp.isEmpty() && this.emptyValue != null) {
                    ret = this.emptyValue;
                } else {
                    if (tmp.length() > getWidth()) {
                        // LOG
                        tmp = tmp.substring(0, getWidth());
                    }
                    ret = tmp;
                }
            } else {
                String tmp = _cellDetails.getCellValue().toString();
                if (tmp.length() > getWidth()) {
                    // LOG
                    tmp = tmp.substring(0, getWidth());
                }
                ret = tmp;
            }
        }
        return ret;
    }

    public FrmtColumn setNullValue(final String _nullValue)
    {
        this.nullValue = _nullValue;
        return this;
    }

    public FrmtColumn setEmptyValue(final String _emptyValue)
    {
        this.emptyValue = _emptyValue;
        return this;
    }
}

package org.efaps.esjp.data.columns.export;

import com.brsanthu.dataexporter.model.CellDetails;
import com.brsanthu.dataexporter.model.StringColumn;

public class FrmtColumn
    extends StringColumn
{
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
            ret = "";
        } else {
            String tmp = _cellDetails.getCellValue().toString();
            if (tmp.length() > getWidth()) {
                // LOG
                tmp = tmp.substring(0, getWidth());
            }
            ret = tmp;
        }
        return ret;
    }
}
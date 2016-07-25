/*
 * Copyright 2003 - 2016 The eFaps Team
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
 */

package org.efaps.esjp.data.columns.export;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.dataexporter.model.CellDetails;
import org.efaps.dataexporter.model.StringColumn;

/**
 * The Class FrmtColumn.
 */
@EFapsUUID("559ce76e-4a98-45c3-9237-d159095fd7b1")
@EFapsApplication("eFapsApp-Data")
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

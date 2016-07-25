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

package org.efaps.esjp.data;

import java.util.Map;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.jaxb.AttrDef;
import org.efaps.util.EFapsException;

/**
 * Interface for Column value classes.
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("a047b4ba-4a90-4346-a481-dce784838fc0")
@EFapsApplication("eFapsApp-Data")
public interface IColumnValue
    extends IColumn
{
    /**
     * Get the value for a column.
     *
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _attrDef      the attribute definition to be validated
     * @param _headers      headers of the csv file
     * @param _value        value for the current row
     * @param _idx          index of the row inside the csv
     * @return the value for the column as a string
     * @throws EFapsException on error
     */
    String getValue(final Parameter _parameter,
                    final AttrDef _attrDef,
                    final Map<String, Integer> _headers,
                    final String[] _value,
                    final Integer _idx)
        throws EFapsException;
}

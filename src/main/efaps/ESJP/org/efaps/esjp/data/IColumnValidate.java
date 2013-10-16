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
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.jaxb.AbstractDef;
import org.efaps.util.EFapsException;


/**
 * Interface for column validation classes.
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("e15916a2-8d7b-45fe-afcf-f790b84c4a5d")
@EFapsRevision("$Rev$")
public interface IColumnValidate
{
    /**
     * Validate a value for a column.
     *
     * @param _parameter    Parameter as passed by the eFaps API
     * @param _def          the definition to be validated
     * @param _headers      headers of the csv file
     * @param _value        value for the current row
     * @param _idx          index of the row inside the csv
     * @return <code>true</code> if valid, else <code>false</code>
     * @throws EFapsException on error
     */
    Boolean validate(final Parameter _parameter,
                     final AbstractDef _def,
                     final Map<String, Integer> _headers,
                     final String[] _value,
                     final Integer _idx)
        throws EFapsException;
}

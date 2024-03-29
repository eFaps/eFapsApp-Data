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
package org.efaps.esjp.data;

import java.util.Map;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.esjp.data.jaxb.Definition;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("ba21a89a-bdb6-40c6-92bc-ef73dc789cf9")
@EFapsApplication("eFapsApp-Data")
public interface IIdentifier
{

    /**
     * Get the instance of the row.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _definition the definition this Identifier belongs to
     * @param _headers headers of the csv file
     * @param _value value for the current row
     * @param _idx index of the row inside the csv
     * @return Instance if exist
     * @throws EFapsException on error
     */
    Instance getInstance(final Parameter _parameter,
                         final Definition _definition,
                         final Map<String, Integer> _headers,
                         final String[] _value,
                         final Integer _idx)
        throws EFapsException;

    /**
     * Get the value for a column.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _definition the definition this Identifier belongs to
     * @param _headers headers of the csv file
     * @param _value value for the current row
     * @param _idx index of the row inside the csv
     * @return true if instance exists else false
     * @throws EFapsException on error
     */
    Boolean hasInstance(final Parameter _parameter,
                        final Definition _definition,
                        final Map<String, Integer> _headers,
                        final String[] _value,
                        final Integer _idx)
        throws EFapsException;

}

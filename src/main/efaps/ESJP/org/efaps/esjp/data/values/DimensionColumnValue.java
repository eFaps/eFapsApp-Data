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

package org.efaps.esjp.data.values;

import java.util.Map;

import org.efaps.admin.datamodel.Dimension;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.IColumnValue;
import org.efaps.esjp.data.jaxb.AbstractDef;
import org.efaps.esjp.data.jaxb.AttrDef;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("7641c1ce-0e4f-45e2-8fba-d5b0620eceb3")
@EFapsRevision("$Rev$")
public class DimensionColumnValue
    implements IColumnValue
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDef.class);

    @Override
    public String getValue(final Parameter _paramter,
                           final AttrDef _attrDef,
                           final Map<String, Integer> _headers,
                           final String[] _value,
                           final Integer _idx)
        throws EFapsException
    {
        String ret = null;
        final String column = _attrDef.getProperty("Column");
        if (column != null) {
            final String dimName = _value[_headers.get(column)];
            final Dimension dim = Dimension.get(dimName);
            DimensionColumnValue.LOG.info("Found Dimension: {}", dim);
            if (dim != null) {
                ret = Long.valueOf(dim.getId()).toString();
            }
        }
        return ret;
    }

}

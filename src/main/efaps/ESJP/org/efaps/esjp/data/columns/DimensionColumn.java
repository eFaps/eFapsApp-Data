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

package org.efaps.esjp.data.columns;

import java.util.Map;
import java.util.UUID;

import org.efaps.admin.datamodel.Dimension;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.common.AbstractCommon;
import org.efaps.esjp.data.IColumnValidate;
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
public class DimensionColumn
    extends AbstractCommon
    implements IColumnValue, IColumnValidate
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(DimensionColumn.class);

    /**
     * {@inheritDoc}
     */
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
            final String dimStr = _value[_headers.get(column)];
            final Dimension dim;
            if (isUUID(dimStr)) {
                dim = Dimension.get(UUID.fromString(dimStr));
            } else {
                dim = Dimension.get(dimStr);
            }
            if (dim != null) {
                ret = Long.valueOf(dim.getId()).toString();
            }
        } else {
            DimensionColumn.LOG.error("Missing property 'Column'.");
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean validate(final Parameter _parameter,
                            final AbstractDef _def,
                            final Map<String, Integer> _headers,
                            final String[] _value,
                            final Integer _idx)
        throws EFapsException
    {
        Boolean ret;
        if (_def instanceof AttrDef) {
            final String column = ((AttrDef)_def).getProperty("Column");
            if (column != null) {
                final String dimStr = _value[_headers.get(column)];
                final Dimension dim;
                if (isUUID(dimStr)) {
                    dim = Dimension.get(UUID.fromString(dimStr));
                } else {
                    dim = Dimension.get(dimStr);
                }
                if (dim != null) {
                    DimensionColumn.LOG.debug("Row: {} - {}", _idx, dim);
                    ret = true;
                } else {
                    DimensionColumn.LOG.warn("no dimension found in Row: {} - {}", _idx, _def);
                    ret = false;
                }
            } else {
                DimensionColumn.LOG.error("Missing property 'Column'.");
                ret = false;
            }
        } else {
            DimensionColumn.LOG.error("Validation only works for AttrDef.");
            ret = false;
        }
        return ret;
    }
}

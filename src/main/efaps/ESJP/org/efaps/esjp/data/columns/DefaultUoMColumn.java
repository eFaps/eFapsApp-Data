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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.efaps.admin.datamodel.Dimension;
import org.efaps.admin.datamodel.Dimension.UoM;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
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
 *
 */
@EFapsUUID("80a1244f-919b-4757-afb0-fb089032ec92")
@EFapsApplication("eFapsApp-Data")
public class DefaultUoMColumn
    extends AbstractCommon
    implements IColumnValue, IColumnValidate
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUoMColumn.class);

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
        final String dimColumn = _attrDef.getProperty("DimensionColumn");
        final String valColumn = _attrDef.getProperty("ValueColumn");
        if (dimColumn != null && valColumn != null) {
            final String dimStr = _value[_headers.get(dimColumn)].trim();
            final String valStr = _value[_headers.get(valColumn)].trim();
            final Dimension dim;
            if (isUUID(dimStr)) {
                dim = Dimension.get(UUID.fromString(dimStr));
            } else {
                dim = Dimension.get(dimStr);
            }
            if (dim != null) {
                for (final UoM uom : dim.getUoMs()) {
                    if (uom.getSymbol().equals(valStr) || uom.getName().equals(valStr)) {
                        ret = Long.valueOf(uom.getId()).toString();
                        break;
                    }
                }
            }
        } else {
            DefaultUoMColumn.LOG.error("Missing property 'Column'.");
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
            final String dimColumn = ((AttrDef)_def).getProperty("DimensionColumn");
            final String valColumn = ((AttrDef)_def).getProperty("ValueColumn");
            if (dimColumn != null && valColumn != null) {
                final String dimStr = _value[_headers.get(dimColumn)].trim();
                final String valStr = _value[_headers.get(valColumn)].trim();
                final Dimension dim;
                if (isUUID(dimStr)) {
                    dim = Dimension.get(UUID.fromString(dimStr));
                } else {
                    dim = Dimension.get(dimStr);
                }
                if (dim != null) {
                    DefaultUoMColumn.LOG.debug("Row: {} - {}", _idx, dim);
                    ret = false;
                    for (final UoM uom : dim.getUoMs()) {
                        if (uom.getSymbol().equals(valStr) || uom.getName().equals(valStr)) {
                            ret = true;
                            break;
                        }
                    }
                    if (!ret) {
                        DefaultUoMColumn.LOG.warn("UoM not found for given Dimension found in Row: {} - {}", _idx, _def);
                    }
                } else {
                    DefaultUoMColumn.LOG.warn("no dimension found in Row: {} - {}", _idx, _def);
                    ret = false;
                }
            } else {
                DefaultUoMColumn.LOG.error("Missing property 'DimensionColumn' and/or 'ValueColumn'.");
                ret = false;
            }
        } else {
            DefaultUoMColumn.LOG.error("Validation only works for AttrDef.");
            ret = false;
        }
        return ret;
    }

    @Override
    public Collection<String> getColumnNames(final Parameter _parameter,
                                             final AbstractDef _def)
    {
        final List<String> ret = new ArrayList<>();
        final String dimColumn = ((AttrDef)_def).getProperty("DimensionColumn");
        final String valColumn = ((AttrDef)_def).getProperty("ValueColumn");
        ret.add(dimColumn);
        ret.add(valColumn);
        return ret;
    }
}

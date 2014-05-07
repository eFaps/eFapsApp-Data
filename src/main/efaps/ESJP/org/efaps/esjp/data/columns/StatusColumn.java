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

import org.efaps.admin.datamodel.Status;
import org.efaps.admin.datamodel.Status.StatusGroup;
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
@EFapsUUID("9a4553df-941c-4356-aa41-fb4e5fd04581")
@EFapsRevision("$Rev$")
public class StatusColumn
    extends AbstractCommon
    implements IColumnValue, IColumnValidate
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(StatusColumn.class);

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
            final String statusGrp = ((AttrDef)_def).getProperty("StatusGroup");
            final String status = ((AttrDef)_def).getProperty("Status");
            final String statusGrpColumn = ((AttrDef)_def).getProperty("StatusGrpColumn");
            final String statusColumn = ((AttrDef)_def).getProperty("StatusColumn");

            if ((statusGrp == null && statusGrpColumn == null) || (status == null && statusColumn == null)) {
                StatusColumn.LOG.error("Property combinations wrong."
                                + " Only one of each StatusGrp / StatusGrpColumn and Status / StatusColumn");
                ret = false;
            } else {
                final String statusGrpStr = statusGrp == null
                                ? _value[_headers.get(statusGrpColumn)].trim() : statusGrp;

                final StatusGroup stGrp;
                if (isUUID(statusGrpStr)) {
                    stGrp = Status.get(UUID.fromString(statusGrpStr));
                } else {
                    stGrp = Status.get(statusGrpStr);
                }
                if (stGrp == null) {
                    StatusColumn.LOG.error("no StatusGroup found in Row: {} - {}", _idx, _def);
                    ret = false;
                } else {
                    StatusColumn.LOG.debug("Row: {} - {}", _idx, stGrp);
                    final String statusStr = status == null ? _value[_headers.get(statusColumn)].trim() : status;
                    final Status st = stGrp.get(statusStr);
                    if (st == null) {
                        StatusColumn.LOG.error("no Status found in Row: {} - {}", _idx, _def);
                        ret = false;
                    } else {
                        StatusColumn.LOG.debug("Row: {} - {}", _idx, st);
                        ret = true;
                    }
                }
            }
        } else {
            StatusColumn.LOG.error("Validation only works for AttrDef.");
            ret = false;
        }
        return ret;
    }

    @Override
    public String getValue(final Parameter _parameter,
                           final AttrDef _attrDef,
                           final Map<String, Integer> _headers,
                           final String[] _value,
                           final Integer _idx)
        throws EFapsException
    {
        final String statusGrp = _attrDef.getProperty("StatusGroup");
        final String status = _attrDef.getProperty("Status");
        final String statusGrpColumn = _attrDef.getProperty("StatusGrpColumn");
        final String statusColumn = _attrDef.getProperty("StatusColumn");

        final String statusGrpStr = statusGrp == null ? _value[_headers.get(statusGrpColumn)].trim() : statusGrp;

        final StatusGroup stGrp;
        if (isUUID(statusGrpStr)) {
            stGrp = Status.get(UUID.fromString(statusGrpStr));
        } else {
            stGrp = Status.get(statusGrpStr);
        }

        StatusColumn.LOG.debug("Row: {} - {}", _idx, stGrp);
        final String statusStr = status == null ? _value[_headers.get(statusColumn)].trim() : status;
        return String.valueOf(stGrp.get(statusStr).getId());

    }

}

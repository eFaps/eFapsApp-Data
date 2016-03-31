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
package org.efaps.esjp.data.columns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.efaps.admin.common.NumberGenerator;
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
 * The Class NumberSequenceColumn.
 *
 * @author The eFaps Team
 */
@EFapsUUID("b918fbd7-ed76-4179-bbbe-9e4e99370363")
@EFapsApplication("eFapsApp-Data")
public class NumberGeneratorColumn
    extends AbstractCommon
    implements IColumnValue, IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(NumberGeneratorColumn.class);

    @Override
    public Collection<String> getColumnNames(final Parameter _parameter,
                                             final AbstractDef _def)
    {
        return new ArrayList<>();
    }

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
            final String numGenStr = ((AttrDef) _def).getProperty("NumberGenerator");
            final NumberGenerator numGen = isUUID(numGenStr) ? NumberGenerator.get(UUID.fromString(numGenStr))
                            : NumberGenerator.get(numGenStr);
            ret = numGen != null;
        } else {
            LOG.error("Validation only works for AttrDef.");
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
        final String numGenStr = _attrDef.getProperty("NumberGenerator");
        final NumberGenerator numGen = isUUID(numGenStr) ? NumberGenerator.get(UUID.fromString(numGenStr))
                        : NumberGenerator.get(numGenStr);
        return numGen.getNextVal();
    }
}

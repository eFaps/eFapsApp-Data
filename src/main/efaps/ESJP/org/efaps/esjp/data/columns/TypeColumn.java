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
package org.efaps.esjp.data.columns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.IColumnValidate;
import org.efaps.esjp.data.jaxb.AbstractDef;
import org.efaps.esjp.data.jaxb.TypeDef;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * 
 */
@EFapsUUID("865921ab-14b6-4028-8081-37ce4b39ff30")
@EFapsApplication("eFapsApp-Data")
public class TypeColumn
    implements IColumnValidate
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(TypeColumn.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean validate(final Parameter _parameter,
                            final AbstractDef _attrDef,
                            final Map<String, Integer> _headers,
                            final String[] _value,
                            final Integer _idx)
        throws EFapsException
    {
        Boolean ret;
        if (_attrDef instanceof TypeDef) {
            Type type = null;
            try {
                type = ((TypeDef) _attrDef).getType(_headers, _value);
            } finally {
                if (type == null) {
                    ret = false;
                    TypeColumn.LOG.warn("No type found in row {} for {}", _idx, _attrDef);
                } else {
                    ret = true;
                    TypeColumn.LOG.debug("Row: {} - {}", _idx, type);
                }
            }
        } else {
            TypeColumn.LOG.error("Validation only woroks for TypeDef.");
            ret = false;
        }
        return ret;
    }


    @Override
    public Collection<String> getColumnNames(final Parameter _parameter,
                    final AbstractDef _def)
    {
        return new ArrayList<>();
    }
}

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
package org.efaps.esjp.data.identifier;

import java.util.Map;

import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.esjp.data.IIdentifier;
import org.efaps.esjp.data.jaxb.Definition;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("1cd770fd-c8fd-475e-bd14-00861cd96ccd")
@EFapsApplication("eFapsApp-Data")
public class IDIdentifier
    implements IIdentifier
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(IDIdentifier.class);


    @Override
    public Boolean hasInstance(final Parameter _parameter,
                               final Definition _definition,
                               final Map<String, Integer> _headers,
                               final String[] _value,
                               final Integer _idx)
        throws EFapsException
    {
        boolean ret = false;
        final String columnName = _definition.getIdentifier().getProperty("IDColumn");
        if (columnName == null) {
            IDIdentifier.LOG.error("IDIdentifier needs the Propert IDColumn");
        } else {
            final String id = _value[_headers.get(columnName)];
            final Type type = _definition.getTypeDef().getType(_headers, _value);
            final Instance inst = Instance.get(type, id);
            ret = inst.isValid();
        }
        return ret;
    }

    @Override
    public Instance getInstance(final Parameter _parameter,
                                final Definition _definition,
                                final Map<String, Integer> _headers,
                                final String[] _value,
                                final Integer _idx)
        throws EFapsException
    {
        final String columnName = _definition.getIdentifier().getProperty("IDColumn");
        final String id = _value[_headers.get(columnName)];
        final Type type = _definition.getTypeDef().getType(_headers, _value);
        return Instance.get(type, id);
    }

}

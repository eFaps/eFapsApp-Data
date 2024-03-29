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
import java.util.List;
import java.util.Map;

import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.IColumnValidate;
import org.efaps.esjp.data.jaxb.AbstractDef;
import org.efaps.esjp.data.jaxb.ClassificationDef;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * 
 */
@EFapsUUID("f9443379-0c93-4f5e-8b85-567afe593748")
@EFapsApplication("eFapsApp-Data")
public class ClassificationColumn
    implements IColumnValidate
{

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(ClassificationColumn.class);

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
        if (_attrDef instanceof ClassificationDef) {
            List<Classification> classifications = null;
            try {
                classifications = ((ClassificationDef) _attrDef).getClassifications(_parameter, _headers, _value);
            } finally {
                if (classifications == null || classifications != null && classifications.isEmpty()) {
                    ret = false;
                    ClassificationColumn.LOG.warn("No classifcation found in row {} for {}", _idx, _attrDef);
                } else {
                    ret = true;
                    ClassificationColumn.LOG.debug("Row: {} - {}", _idx, classifications);
                }
            }
        } else {
            ClassificationColumn.LOG.error("Validation only woroks for TypeDef.");
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

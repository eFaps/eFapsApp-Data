/*
 * Copyright 2003 - 2014 The eFaps Team
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.jaxb.AttrDef;
import org.efaps.esjp.data.jaxb.DataImport;
import org.efaps.esjp.data.jaxb.Definition;
import org.efaps.esjp.data.jaxb.TypeDef;
import org.efaps.util.EFapsException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("b8bca7a1-a94d-4f47-98f3-0e17903c5624")
@EFapsApplication("eFapsApp-Data")
public abstract class CSVTemplate_Base
{
    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @return empty Return
     * @throws EFapsException on error
     */
    public Return ceateCSV(final Parameter _parameter)
        throws EFapsException
    {
        try {
            final TmpImport tmpImport = new TmpImport();
            final JAXBContext jc = tmpImport.getJAXBContext();
            final Unmarshaller unmarschaller = jc.createUnmarshaller();
            unmarschaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            final Object object = unmarschaller.unmarshal(tmpImport.getSource4DataImport(_parameter));
            if (object instanceof DataImport) {
                createFromDataImport(_parameter, (DataImport) object);
            }
        } catch (final JAXBException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        }
        return new Return();
    }

    public void createFromDataImport(final Parameter _parameter,
                                     final DataImport _dataImport)
    {
        final List<String> columns = new ArrayList<>();
        for (final Definition definition : _dataImport.getDefinition()) {
            final TypeDef typeDef = definition.getTypeDef();
            for (final AttrDef attr : typeDef.getAttributes()) {
                columns.addAll(attr.getColumnNames(_parameter));
            }
        }
    }


    public static class TmpImport
        extends AbstractImport
    {

    }
}

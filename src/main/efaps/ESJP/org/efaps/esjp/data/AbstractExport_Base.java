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


package org.efaps.esjp.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.jaxb.EFapsObject;
import org.efaps.esjp.data.jaxb.ObjectList;
import org.efaps.esjp.data.jaxb.adapter.CDataXMLStreamWriter;
import org.efaps.esjp.data.jaxb.attributes.EFapsAttributes;
import org.efaps.util.EFapsException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("a2237db1-0497-4723-9e64-61f379270e83")
@EFapsRevision("$Rev$")
public abstract class AbstractExport_Base
{
    /**
     * @param _parameter Parameter as passed by the eFaps API
     * @return empty Return
     * @throws EFapsException on error
     */
    public Return execute(final Parameter _parameter)
        throws EFapsException
    {
        return new Return();
    }

    protected void marschall(final Parameter _parameter,
                             final Object _object)
    {
        try {
            final JAXBContext jaxb = getJAXBContext();
            final Marshaller marshaller = jaxb.createMarshaller();

            final XMLOutputFactory xof = XMLOutputFactory.newInstance();

            final XMLStreamWriter streamWriter = xof.createXMLStreamWriter(System.out);
            final CDataXMLStreamWriter cdataStreamWriter = new CDataXMLStreamWriter(streamWriter);
            marshaller.marshal(_object, cdataStreamWriter);
            cdataStreamWriter.flush();
            cdataStreamWriter.close();
        } catch (final JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get the JAXBContext used in this importer.
     *
     * @return an JAXBContext
     * @throws JAXBException on error
     */
    protected JAXBContext getJAXBContext()
        throws JAXBException
    {
        AbstractImport_Base.LOG.trace("Getting JAXBContext.");
        final JAXBContext context = JAXBContext.newInstance(getClasses());
        return context;
    }

    /**
     * @return an Array of classes used for the JAXBContext
     */
    protected Class<?>[] getClasses()
    {
        AbstractImport_Base.LOG.trace("Getting the Classes for the JAXBContext.");

        final List<Class<?>> clazzes = new ArrayList<Class<?>>();
        clazzes.addAll(EFapsAttributes.CLASSMAPPING.values());
        clazzes.add(EFapsObject.class);
        clazzes.add(ObjectList.class);
        return clazzes.toArray(new Class<?>[clazzes.size()]);
    }

}

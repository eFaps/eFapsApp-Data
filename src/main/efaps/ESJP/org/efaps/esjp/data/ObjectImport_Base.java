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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.jaxb.EFapsObject;
import org.efaps.util.EFapsException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("7aaf10fb-6687-4399-a45a-81ae1915abb3")
@EFapsRevision("$Rev$")
public class ObjectImport_Base
    extends AbstractImport
{
    public static void main(final String[] _para) {
        new ObjectImport().test();
        //new ObjectExport().test();
    }


    public void test()
    {

        try {
            final JAXBContext jc = getJAXBContext();
            final Unmarshaller unmarschaller = jc.createUnmarshaller();
            final Object object = unmarschaller.unmarshal(getSource4DataImport(null));
            if (object instanceof EFapsObject) {
                System.out.println(object);
            }
        } catch (JAXBException | EFapsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected URL getUrl(final Parameter _parameter)
    {
        final File file =  new File("/Users/moxter/Workspaces/eFaps/data/object.xml");
        URL ret = null;
        try {
            ret = file.toURI().toURL();
        } catch (final MalformedURLException e) {
            AbstractImport_Base.LOG.error("Catched error:", e);
        }
        return ret;
    }

    /**
     * @return an Array of classes used for the JAXBContext
     */
    @Override
    protected Class<?>[] getClasses()
    {
        AbstractImport_Base.LOG.trace("Getting the Classes for the JAXBContext.");
        return new ObjectExport().getClasses();
    }
}

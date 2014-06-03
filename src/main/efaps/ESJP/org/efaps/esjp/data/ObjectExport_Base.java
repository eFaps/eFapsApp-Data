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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.efaps.admin.datamodel.Type;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.InstanceQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.esjp.data.jaxb.EFapsObject;
import org.efaps.esjp.data.jaxb.ObjectList;
import org.efaps.esjp.data.jaxb.attributes.CreatedTypeAttribute;
import org.efaps.esjp.data.jaxb.attributes.DateTypeAttribute;
import org.efaps.esjp.data.jaxb.attributes.DecimalTypeAttribute;
import org.efaps.esjp.data.jaxb.attributes.IntegerTypeAttribute;
import org.efaps.esjp.data.jaxb.attributes.LongTypeAttribute;
import org.efaps.esjp.data.jaxb.attributes.StringTypeAttribute;
import org.efaps.util.EFapsException;
import org.joda.time.DateTime;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("a40568b2-19da-49ff-b480-e3197782851")
@EFapsRevision("$Rev$")
public abstract class ObjectExport_Base
    extends AbstractExport
{

    public static void main(final String[] _para) {
        new ObjectExport().generateShema();
        //new ObjectExport().test();
    }


    @Override
    public Return execute(final Parameter _parameter)
        throws EFapsException
    {
        final Instance instance = _parameter.getInstance();
        if (instance != null && instance.isValid()) {
            //Sales_DocumentAbstract
            if (instance.getType().isKindOf(Type.get(UUID.fromString("c2615e57-c990-4572-8dfd-eda7f8f76e4d")))) {
                final ObjectList list = new ObjectList();
                list.getObjects().add(new EFapsObject(instance));
                //Sales_PositionAbstract
                final QueryBuilder queryBldr = new QueryBuilder(UUID.fromString("7531661c-2203-4f9a-82f7-4e0d214700dd"));
                queryBldr.addWhereAttrEqValue("DocumentAbstractLink", instance);
                final InstanceQuery query = queryBldr.getQuery();
                query.executeWithoutAccessCheck();
                while (query.next()) {
                    list.getObjects().add(new EFapsObject(query.getCurrentValue()));
                }
                list.load();
                marschall(_parameter, list);
            } else {
                final EFapsObject object = new EFapsObject(instance);
                object.load();
                marschall(_parameter, object);
            }
        }
        return new Return();
    }

    public void generateShema()
    {
        try {
            final JAXBContext jaxb = getJAXBContext();
            jaxb.generateSchema(new SchemaOutputResolver()
            {

                @Override
                public Result createOutput(final String _namespaceUri,
                                           final String _suggestedFileName)
                    throws IOException
                {
                    final File file = new File(_suggestedFileName);
                    final StreamResult result = new StreamResult(file);
                    result.setSystemId(file.toURI().toURL().toString());
                    return result;
                }
            });
        } catch (final JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void test()
    {
        try {
            final EFapsObject object = new EFapsObject()
                            .setTypeUUID(UUID.randomUUID())
                            .setId(5)
                            .setExId(0)
                            .setExSysId(0);

            final LongTypeAttribute attr = new LongTypeAttribute().setAttrName("ID").setValue(Long.valueOf(5));
            object.addAttribute(attr);

            final IntegerTypeAttribute attr2 = new IntegerTypeAttribute().setAttrName("Position").setValue(10);
            object.addAttribute(attr2);

            final DecimalTypeAttribute attr5 = new DecimalTypeAttribute().setAttrName("Amount").setValue(
                            new BigDecimal("10.5678"));
            object.addAttribute(attr5);

            final DateTypeAttribute attr6 = new DateTypeAttribute().setAttrName("Date").setValue(new DateTime());
            object.addAttribute(attr6);

            final CreatedTypeAttribute attr7 = new CreatedTypeAttribute().setAttrName("Created").setValue(
                            new DateTime());
            object.addAttribute(attr7);

            final StringTypeAttribute attr3 = new StringTypeAttribute().setAttrName("Name").setValue("001-0000568");
            object.addAttribute(attr3);
            final StringTypeAttribute attr4 = new StringTypeAttribute().setAttrName("Description").setValue(
                            "Her can be a long text\nwith linebreak and other stuff<br/>");
            object.addAttribute(attr4);

            marschall(null, object);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

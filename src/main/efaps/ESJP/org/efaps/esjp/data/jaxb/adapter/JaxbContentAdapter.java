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

package org.efaps.esjp.data.jaxb.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("772a3d80-f42d-49e0-8a77-f7267ed1c594")
@EFapsRevision("$Rev$")
public class JaxbContentAdapter
    extends XmlAdapter<String, String>
{

    public final static String PREFIX = JaxbContentAdapter.class.getName() +  ".Prefix";

    @Override
    public String unmarshal(final String _v)
        throws Exception
    {
        return _v;
    }

    @Override
    public String marshal(final String _v)
        throws Exception
    {
        return JaxbContentAdapter.PREFIX + _v;
    }
}

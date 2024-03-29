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
package org.efaps.esjp.data.jaxb.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.joda.time.DateTime;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("dbb86f99-be43-463b-97fa-3faab4daae91")
@EFapsApplication("eFapsApp-Data")
public class DateTimeAdapter
    extends XmlAdapter<String, DateTime>
{

    @Override
    public DateTime unmarshal(final String _value)
        throws Exception
    {
        return _value == null ? new DateTime() : new DateTime(_value);
    }

    @Override
    public String marshal(final DateTime _value)
        throws Exception
    {
        return _value == null ? null : _value.toString();
    }
}

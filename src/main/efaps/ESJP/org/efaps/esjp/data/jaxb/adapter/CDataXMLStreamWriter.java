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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("4cee272e-6ffa-47b0-9558-c4b1fbd1f37f")
@EFapsApplication("eFapsApp-Data")
public class CDataXMLStreamWriter
    extends XMLStreamWriterWrapper
{

    /**
     * @param _writer
     */
    public CDataXMLStreamWriter(final XMLStreamWriter _writer)
    {
        super(_writer);
    }

    @Override
    public void writeCharacters(final String _text)
        throws XMLStreamException
    {
        if (_text.startsWith(JaxbContentAdapter.PREFIX))
        {
            super.writeCData(_text.substring(JaxbContentAdapter.PREFIX.length()));
        }
        else
        {
            super.writeCharacters(_text);
        }
    }
}

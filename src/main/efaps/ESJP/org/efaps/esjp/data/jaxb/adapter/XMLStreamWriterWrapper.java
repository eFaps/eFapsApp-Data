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

import java.util.Stack;

import javax.xml.namespace.NamespaceContext;
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
@EFapsUUID("649a63aa-673f-4256-a025-46f12b0c69a8")
@EFapsApplication("eFapsApp-Data")
public class XMLStreamWriterWrapper
    implements XMLStreamWriter
{

    private enum State
    {
        SEEN_NOTHING, SEEN_ELEMENT, SEEN_DATA;
    }

    private State state = State.SEEN_NOTHING;

    private final Stack<State> stateStack = new Stack<>();

    private final String indentStep = "    ";
    private int depth = 0;
    private final XMLStreamWriter writer;

    private void onStartElement()
        throws XMLStreamException
    {
        this.stateStack.push(State.SEEN_ELEMENT);
        this.state = State.SEEN_NOTHING;
        if (this.depth > 0) {
            this.writer.writeCharacters("\n");
        }
        doIndent();
        this.depth++;
    }

    private void onEndElement()
        throws XMLStreamException
    {
        this.depth--;
        if (this.state == State.SEEN_ELEMENT) {
            this.writer.writeCharacters("\n");
            doIndent();
        }
        this.state = this.stateStack.pop();
    }

    private void onEmptyElement()
        throws XMLStreamException
    {
        this.state = State.SEEN_ELEMENT;
        if (this.depth > 0) {
            this.writer.writeCharacters("\n");
        }
        doIndent();
    }

    /**
     * Print indentation for the current level.
     *
     * @exception org.xml.sax.SAXException If there is an error writing the
     *                indentation characters, or if a filter further down the
     *                chain raises an exception.
     */
    private void doIndent()
        throws XMLStreamException
    {
        if (this.depth > 0) {
            for (int i = 0; i < this.depth; i++) {
                this.writer.writeCharacters(this.indentStep);
            }
        }
    }

    public XMLStreamWriterWrapper(final XMLStreamWriter _writer)
    {
        this.writer = _writer;
    }

    @Override
    public void writeStartElement(final String _localName)
        throws XMLStreamException
    {
        onStartElement();
        this.writer.writeStartElement(_localName);
    }

    @Override
    public void writeStartElement(final String _namespaceURI,
                                  final String _localName)
        throws XMLStreamException
    {
        onStartElement();
        this.writer.writeStartElement(_namespaceURI, _localName);
    }

    @Override
    public void writeStartElement(final String _prefix,
                                  final String _localName,
                                  final String _namespaceURI)
        throws XMLStreamException
    {
        onStartElement();
        this.writer.writeStartElement(_prefix, _localName, _namespaceURI);
    }

    @Override
    public void writeEmptyElement(final String _namespaceURI,
                                  final String _localName)
        throws XMLStreamException
    {
        onEmptyElement();
        this.writer.writeEmptyElement(_namespaceURI, _localName);
    }

    @Override
    public void writeEmptyElement(final String _prefix,
                                  final String _localName,
                                  final String _namespaceURI)
        throws XMLStreamException
    {
        onEmptyElement();
        this.writer.writeEmptyElement(_prefix, _localName, _namespaceURI);
    }

    @Override
    public void writeEmptyElement(final String _localName)
        throws XMLStreamException
    {
        onEmptyElement();
        this.writer.writeEmptyElement(_localName);
    }

    @Override
    public void writeEndElement()
        throws XMLStreamException
    {
        onEndElement();
        this.writer.writeEndElement();
    }

    @Override
    public void writeEndDocument()
        throws XMLStreamException
    {
        this.writer.writeEndDocument();
    }

    @Override
    public void close()
        throws XMLStreamException
    {
        this.writer.close();
    }

    @Override
    public void flush()
        throws XMLStreamException
    {
        this.writer.flush();
    }

    @Override
    public void writeAttribute(final String _localName,
                               final String _value)
        throws XMLStreamException
    {
        this.writer.writeAttribute(_localName, _value);
    }

    @Override
    public void writeAttribute(final String _prefix,
                               final String _namespaceURI,
                               final String _localName,
                               final String _value)
        throws XMLStreamException
    {
        this.writer.writeAttribute(_prefix, _namespaceURI, _localName, _value);
    }

    @Override
    public void writeAttribute(final String _namespaceURI,
                               final String _localName,
                               final String _value)
        throws XMLStreamException
    {
        this.writer.writeAttribute(_namespaceURI, _localName, _value);
    }

    @Override
    public void writeNamespace(final String _prefix,
                               final String _namespaceURI)
        throws XMLStreamException
    {
        this.writer.writeNamespace(_prefix, _namespaceURI);
    }

    @Override
    public void writeDefaultNamespace(final String _namespaceURI)
        throws XMLStreamException
    {
        this.writer.writeDefaultNamespace(_namespaceURI);
    }

    @Override
    public void writeComment(final String _data)
        throws XMLStreamException
    {
        this.writer.writeComment(_data);
    }

    @Override
    public void writeProcessingInstruction(final String _target)
        throws XMLStreamException
    {
        this.writer.writeProcessingInstruction(_target);
    }

    @Override
    public void writeProcessingInstruction(final String _target,
                                           final String _data)
        throws XMLStreamException
    {
        this.writer.writeProcessingInstruction(_target, _data);
    }

    @Override
    public void writeCData(final String _data)
        throws XMLStreamException
    {
        this.state = State.SEEN_DATA;
        this.writer.writeCData(_data);
    }

    @Override
    public void writeDTD(final String _dtd)
        throws XMLStreamException
    {
        this.writer.writeDTD(_dtd);
    }

    @Override
    public void writeEntityRef(final String _name)
        throws XMLStreamException
    {
        this.writer.writeEntityRef(_name);
    }

    @Override
    public void writeStartDocument()
        throws XMLStreamException
    {
        this.writer.writeStartDocument();
        this.writer.writeCharacters("\n");
    }

    @Override
    public void writeStartDocument(final String _version)
        throws XMLStreamException
    {
        this.writer.writeStartDocument(_version);
        this.writer.writeCharacters("\n");
    }

    @Override
    public void writeStartDocument(final String _encoding,
                                   final String _version)
        throws XMLStreamException
    {
        this.writer.writeStartDocument(_encoding, _version);
        this.writer.writeCharacters("\n");
    }

    @Override
    public void writeCharacters(final String _text)
        throws XMLStreamException
    {
        this.state = State.SEEN_DATA;
        this.writer.writeCharacters(_text);
    }

    @Override
    public void writeCharacters(final char[] text,
                                final int start,
                                final int len)
        throws XMLStreamException
    {
        this.state = State.SEEN_DATA;
        this.writer.writeCharacters(text, start, len);
    }

    @Override
    public String getPrefix(final String _uri)
        throws XMLStreamException
    {
        return this.writer.getPrefix(_uri);
    }

    @Override
    public void setPrefix(final String _prefix,
                          final String _uri)
        throws XMLStreamException
    {
        this.writer.setPrefix(_prefix, _uri);
    }

    @Override
    public void setDefaultNamespace(final String _uri)
        throws XMLStreamException
    {
        this.writer.setDefaultNamespace(_uri);
    }

    @Override
    public void setNamespaceContext(final NamespaceContext context)
        throws XMLStreamException
    {
        this.writer.setNamespaceContext(context);
    }

    @Override
    public NamespaceContext getNamespaceContext()
    {
        return this.writer.getNamespaceContext();
    }

    @Override
    public Object getProperty(final String _name)
        throws IllegalArgumentException
    {
        return this.writer.getProperty(_name);
    }
}

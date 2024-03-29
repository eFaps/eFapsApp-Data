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
package org.efaps.esjp.data.jaxb.attributes;

import java.io.StringWriter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.IJaxb;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsClassLoader;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Insert;
import org.efaps.esjp.data.jaxb.adapter.JaxbContentAdapter;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("3f185eb0-de9d-4091-a9a8-fedbd1f6d3c5")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "jaxb", namespace = "http://www.efaps.org/xsd")
public class JaxbTypeAttribute
    extends AbstractEFapsAttribute<JaxbTypeAttribute>
{
    /**
     * Content of this Attribute.
     */
    @XmlJavaTypeAdapter(JaxbContentAdapter.class)
    @XmlElement(required = true)
    private String content;

    @Override
    protected JaxbTypeAttribute getThis()
    {
        return this;
    }

    @Override
    public JaxbTypeAttribute evalAttrValue(final Attribute _attribute,
                                           final Object _dbValue)
        throws EFapsException
    {
        super.evalAttrValue(_attribute, _dbValue);
        if (_dbValue != null) {
            try {
                final Class<?> clazz = Class.forName(_attribute.getClassName(), false,
                                EFapsClassLoader.getInstance());
                final IJaxb jaxb = (IJaxb) clazz.newInstance();
                final JAXBContext jc = JAXBContext.newInstance(jaxb.getClasses());
                final Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                final StringWriter writer = new StringWriter();
                marshaller.marshal(_dbValue, writer);
                setContent(writer.toString());
            } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | JAXBException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * Getter method for the instance variable {@link #content}.
     *
     * @return value of instance variable {@link #content}
     */
    public String getContent()
    {
        return this.content;
    }

    /**
     * Setter method for instance variable {@link #content}.
     *
     * @param _content value for instance variable {@link #content}
     * @return this for chaining
     */
    public JaxbTypeAttribute setContent(final String _content)
    {
        this.content = _content;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add2Insert(final Insert _insert)
        throws EFapsException
    {
        _insert.add(getAttrName(), getContent());
    }
}

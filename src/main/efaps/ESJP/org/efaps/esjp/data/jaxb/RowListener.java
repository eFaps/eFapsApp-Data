/*
 * Copyright 2003 - 2022 The eFaps Team
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
 */
package org.efaps.esjp.data.jaxb;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.esjp.data.IOnRow;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EFapsUUID("bfcd0672-8df2-4bf2-9e9e-56f094cdd648")
@EFapsApplication("eFapsApp-Data")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "row-listener", namespace = "http://www.efaps.org/xsd")
@XmlType(name = "row-listener", namespace = "http://www.efaps.org/xsd")
public class RowListener
    extends AbstractDef
{

    private static final Logger LOG = LoggerFactory.getLogger(AttrSetDef.class);

    @XmlAttribute(name = "class")
    private String className;

    @XmlElementRef
    private List<PropertyDef> properties;


    public List<PropertyDef> getProperties()
    {
        return properties;
    }

    public String getProperty(final String _name)
    {
        String ret = null;
        for (final PropertyDef def : properties) {
            if (_name.equals(def.getName())) {
                ret = def.getValue();
                break;
            }
        }
        return ret;
    }

    public void execute(final Parameter _parameter, final Instance instance, final Map<String, Integer> headers,
                        final String[] values, final Integer idx)
        throws EFapsException
    {
        try {
            final var clazz = Class.forName(getClassName());
            final IOnRow onRow = (IOnRow) clazz.getConstructor().newInstance();
            onRow.run(_parameter, this, instance, headers, values, idx);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            LOG.error("Catched", e);
        }
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(final String className)
    {
        this.className = className;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

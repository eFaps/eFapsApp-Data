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

package org.efaps.esjp.data.jaxb;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.IColumnValue;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("79a3b4d8-be0b-49df-a7ce-81d5bc345699")
@EFapsRevision("$Rev$")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "attribute")
@XmlType(name = "attribute")
public class AttrDef
    extends AbstractDef
{
    @XmlAttribute(name = "class")
    private String className;

    @XmlAttribute(name = "method")
    private String methodName;

    @XmlAttribute(name = "fixedValue")
    private String fixedValue;


    /**
     * @param _headers
     * @param _value
     * @return
     */
    public String getValue(final Parameter _parameter,
                           final Map<String, Integer> _headers,
                           final String[] _value)
    {
        String ret = null;
        if (getColumn() != null &&  _headers.containsKey(getColumn())) {
            ret = _value[_headers.get(getColumn())];
        } else if (this.fixedValue != null && !this.fixedValue.isEmpty()) {
            ret = this.fixedValue;
        } else {
            try {
                final Class<?> clazz = Class.forName(this.className);
                final IColumnValue columnValue = (IColumnValue) clazz.newInstance();
                ret = columnValue.getValue(_parameter, _headers, _value);
            } catch (final ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return ret;
    }

}

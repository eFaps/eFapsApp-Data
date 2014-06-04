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

package org.efaps.esjp.data.jaxb.attributes;

import java.util.HashMap;
import java.util.Map;

import org.efaps.admin.datamodel.Attribute;
import org.efaps.admin.datamodel.IAttributeType;
import org.efaps.admin.datamodel.Type;
import org.efaps.admin.datamodel.attributetype.BitEnumType;
import org.efaps.admin.datamodel.attributetype.BooleanType;
import org.efaps.admin.datamodel.attributetype.CompanyLinkType;
import org.efaps.admin.datamodel.attributetype.ConsortiumLinkType;
import org.efaps.admin.datamodel.attributetype.CreatedType;
import org.efaps.admin.datamodel.attributetype.CreatorLinkType;
import org.efaps.admin.datamodel.attributetype.DateTimeType;
import org.efaps.admin.datamodel.attributetype.DateType;
import org.efaps.admin.datamodel.attributetype.DecimalType;
import org.efaps.admin.datamodel.attributetype.EnumType;
import org.efaps.admin.datamodel.attributetype.IntegerType;
import org.efaps.admin.datamodel.attributetype.JaxbType;
import org.efaps.admin.datamodel.attributetype.LinkType;
import org.efaps.admin.datamodel.attributetype.LinkWithRanges;
import org.efaps.admin.datamodel.attributetype.LongType;
import org.efaps.admin.datamodel.attributetype.ModifiedType;
import org.efaps.admin.datamodel.attributetype.ModifierLinkType;
import org.efaps.admin.datamodel.attributetype.OIDType;
import org.efaps.admin.datamodel.attributetype.RateType;
import org.efaps.admin.datamodel.attributetype.StatusType;
import org.efaps.admin.datamodel.attributetype.StringType;
import org.efaps.admin.datamodel.attributetype.TimeType;
import org.efaps.admin.datamodel.attributetype.TypeType;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.ci.CIAdminDataModel;
import org.efaps.db.PrintQuery;
import org.efaps.db.SelectBuilder;
import org.efaps.util.EFapsException;
import org.efaps.util.cache.CacheReloadException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("0b8af194-9a3c-4d9f-a0b9-c2c2cbfd57c3")
@EFapsRevision("$Rev$")
public final class EFapsAttributes
{

    /**
     * Basic mapping.
     */
    public static final Map<Class<?>, Class<?>> CLASSMAPPING = new HashMap<Class<?>, Class<?>>();
    static {
        EFapsAttributes.CLASSMAPPING.put(BooleanType.class, BooleanTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(CompanyLinkType.class, CompanyTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(ConsortiumLinkType.class, ConsortiumTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(CreatedType.class, CreatedTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(CreatorLinkType.class, CreatorTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(DateTimeType.class, DateTimeTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(DateType.class, DateTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(DecimalType.class, DecimalTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(IntegerType.class, IntegerTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(JaxbType.class, JaxbTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(LinkType.class, LinkTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(LinkWithRanges.class, LinkWithRangesTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(LongType.class, LongTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(ModifiedType.class, ModifiedTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(ModifierLinkType.class, ModifierTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(OIDType.class, OIDTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(RateType.class, RateTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(StatusType.class, StatusTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(StringType.class, StringTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(TimeType.class, TimeTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(TypeType.class, TypeTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(BitEnumType.class, BitEnumTypeAttribute.class);
        EFapsAttributes.CLASSMAPPING.put(EnumType.class, EnumTypeAttribute.class);
    }

    /**
     * Helper class.
     */
    private EFapsAttributes()
    {
    }


    /**
     * @param _attribute
     * @param _value
     * @return
     */
    public static AbstractEFapsAttribute<?> getAttribute(final Attribute _attribute,
                                                         final Object _dbValue)
        throws EFapsException
    {
        final AbstractEFapsAttribute<?> ret = EFapsAttributes.getAttribute(_attribute);
        if (ret != null) {
            ret.evalAttrValue(_attribute, _dbValue);
        }
        return ret;
    }

    public static AbstractEFapsAttribute<?> getAttribute(final Attribute _attribute)
                    throws CacheReloadException
    {
        AbstractEFapsAttribute<?> ret = null;
        final IAttributeType attrType = _attribute.getAttributeType().getDbAttrType();
        // fisrt evaluate the special types like UoM, Dimension
        if (_attribute.hasLink()) {
            final Type linkType = _attribute.getLink();
            if (linkType.getUUID().equals(CIAdminDataModel.UoM.uuid)) {
                ret = new UoMTypeAttribute();
            } else if (linkType.getUUID().equals(CIAdminDataModel.Dimension.uuid)) {
                ret = new DimensionTypeAttribute();
            }
        }

        if (ret == null && EFapsAttributes.CLASSMAPPING.containsKey(attrType.getClass())) {
            final Class<?> attrClazz = EFapsAttributes.CLASSMAPPING.get(attrType.getClass());
            try {
                ret = (AbstractEFapsAttribute<?>) attrClazz.newInstance();
            } catch (final InstantiationException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * @param _print
     * @param _attribute
     */
    public static void add2Print(final PrintQuery _print,
                                 final Attribute _attribute)
        throws EFapsException
    {
        final AbstractEFapsAttribute<?> attr = EFapsAttributes.getAttribute(_attribute);
        if (attr != null) {
            if (attr.isLink()) {
                final SelectBuilder sel = new SelectBuilder().linkto(_attribute.getName()).instance();
                _print.addSelect(sel);
            } else {
                _print.addAttribute(_attribute);
            }
        }
    }

    /**
     * @param _print
     * @param _attribute
     */
    public static Object get4Print(final PrintQuery _print,
                                   final Attribute _attribute)
        throws EFapsException
    {
        Object ret = null;
        final AbstractEFapsAttribute<?> attr = EFapsAttributes.getAttribute(_attribute);
        if (attr != null) {
            if (attr.isLink()) {
                final SelectBuilder sel = new SelectBuilder().linkto(_attribute.getName()).instance();
                ret = _print.getSelect(sel);
            } else {
                ret = _print.getAttribute(_attribute);
            }
        }
        return ret;
    }
}

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
package org.efaps.esjp.data;

import java.util.Collection;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.jaxb.AbstractDef;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 *
 */
@EFapsUUID("aa14bd4a-c9f0-40e8-a4bf-865777f22931")
@EFapsApplication("eFapsApp-Data")
public interface IColumn
{

    /**
     * Get the name of the columns used.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _def the definition the columns belong to
     * @return collection of the used columns
     */
    public Collection<String> getColumnNames(final Parameter _parameter,
                                             final AbstractDef _def);
}

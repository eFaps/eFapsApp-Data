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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Context;
import org.efaps.db.Context.FileParameter;
import org.efaps.esjp.common.file.FileUtil;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Import_Base.
 *
 * @author The eFaps Team
 */
@EFapsUUID("d3c66657-2cf1-4f24-8dfc-4afabc0ccec2")
@EFapsApplication("eFapsApp-Data")
public abstract class ImportData_Base
    extends AbstractImport
{
    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImportData.class);

    /** The file. */
    private URL xmlURL;

    /**
     * Data import from files.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @return the return
     * @throws EFapsException
     */
    public Return dataImportFromFiles(final Parameter _parameter)
        throws EFapsException
    {
        try {
            final Map<String, FileParameter> fileParameters = Context.getThreadContext().getFileParameters();
            final FileUtil fileUtil = new FileUtil();
            this.xmlURL = null;
            if (fileParameters.containsKey("upload")) {
                final FileParameter filePara = fileParameters.get("upload");
                final File file = fileUtil.getFile(filePara.getName());
                if (file.getName().endsWith(".xml")) {
                    this.xmlURL = file.toURI().toURL();
                }
                FileUtils.copyInputStreamToFile(filePara.getInputStream(), file);
            }
            int i = 0;
            while (fileParameters.containsKey("upload_" + i)) {
                final FileParameter filePara = fileParameters.get("upload_" + i);
                final File file = fileUtil.getFile(filePara.getName());
                if (file.getName().endsWith(".xml")) {
                    this.xmlURL = file.toURI().toURL();
                }
                FileUtils.copyInputStreamToFile(filePara.getInputStream(), file);
                i++;
            }
            if (this.xmlURL != null) {
                execute(_parameter);
            }
        } catch (final IOException e) {
            ImportData_Base.LOG.error("Catched", e);
        }
        return new Return();
    }

    /**
     * @param _parameter
     * @return
     */
    @Override
    protected URL getUrl(final Parameter _parameter)
    {
        return this.xmlURL;
    }
}

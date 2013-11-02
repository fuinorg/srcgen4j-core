/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.srcgen4j.core.velocity;

import java.io.File;

import org.fuin.utils4j.fileprocessor.FileHandler;
import org.fuin.utils4j.fileprocessor.FileHandlerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects all model files in a directory and it's sub directory.
 */
public final class FullFileHandler implements FileHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FullFileHandler.class);

    private final ParameterizedTemplateParser parser;

    private final ParameterizedTemplateModels templates;

    /**
     * Constructor with parent parser.
     * 
     * @param parser
     *            Parent parser - Cannot be NULL.
     */
    public FullFileHandler(final ParameterizedTemplateParser parser) {
        super();
        this.parser = parser;
        this.templates = new ParameterizedTemplateModels();
    }

    @Override
    public final FileHandlerResult handleFile(final File file) {
        if (parser.getModelFilter().accept(file)) {
            LOG.info("Adding template file: " + file.getName());
            final ParameterizedTemplateModel pt = ParameterizedTemplateModel.create(file);
            pt.init(parser.getContext(), parser.getVarMap());
            templates.addParamTemplate(pt);
        }
        return FileHandlerResult.CONTINUE;
    }

    /**
     * Returns the list of templates found in the given directory.
     * 
     * @return Collected templates.
     */
    public final ParameterizedTemplateModels getTemplates() {
        return templates;
    }

}

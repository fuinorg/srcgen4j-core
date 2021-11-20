/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.srcgen4j.core.velocity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuin.srcgen4j.commons.ParseException;
import org.fuin.utils4j.fileprocessor.FileHandler;
import org.fuin.utils4j.fileprocessor.FileHandlerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects all model files based on a list of changed files. Changed files may be model or template files. For a changed template file all
 * referencing model files will be added.
 */
public final class IncrementalFileHandler implements FileHandler {

    private static final String MSG_ADDING_MODEL_FILE = "Adding model file: {}";

    private static final Logger LOG = LoggerFactory.getLogger(IncrementalFileHandler.class);

    private final Map<File, List<ParameterizedTemplateModel>> templatesToModelMap;

    private final ParameterizedTemplateParser parser;

    private ParameterizedTemplateModels templates;

    /**
     * Constructor with parent parser.
     * 
     * @param parser
     *            Parent parser - Cannot be NULL.
     */
    public IncrementalFileHandler(final ParameterizedTemplateParser parser) {
        super();
        this.parser = parser;
        this.templates = new ParameterizedTemplateModels();
        this.templatesToModelMap = new HashMap<File, List<ParameterizedTemplateModel>>();
    }

    @Override
    public final FileHandlerResult handleFile(final File file) {
        if (parser.getTemplateFilter().accept(file)) {
            // Changed template file
            final List<ParameterizedTemplateModel> list = findReferencesTo(file);
            logList(file, list);
            templates.addParamTemplates(list);
        } else if (parser.getModelFilter().accept(file)) {
            // Changed model file
            LOG.info(MSG_ADDING_MODEL_FILE, file.getName());
            final ParameterizedTemplateModel pt = ParameterizedTemplateModel.create(file);
            pt.init(parser.getContext(), parser.getVarMap());
            templates.addParamTemplate(pt);
        }
        return FileHandlerResult.CONTINUE;
    }

    private void logList(final File file, final List<ParameterizedTemplateModel> list) {
        if (list == null || list.isEmpty()) {
            LOG.info("No references found to template: {}", file.getName());
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("Found {} reference(s) to template {}", list.size(), file.getName());
                for (final ParameterizedTemplateModel model : list) {
                    if (model.getFile() == null) {
                        // Should never happen...
                        LOG.info(MSG_ADDING_MODEL_FILE, model.getFile());
                    } else {
                        LOG.info(MSG_ADDING_MODEL_FILE, model.getFile().getName());
                    }
                }
            }
        }
    }

    /**
     * Returns the list of templates found in the given directory.
     * 
     * @return Collected templates.
     */
    public final ParameterizedTemplateModels getTemplates() {
        return templates;
    }

    /**
     * Clears the collected results.
     */
    public final void clear() {
        LOG.info("Clear incremental handler");
        this.templates = new ParameterizedTemplateModels();
    }

    private List<ParameterizedTemplateModel> findReferencesTo(final File templateFile) {
        List<ParameterizedTemplateModel> result = templatesToModelMap.get(templateFile);
        if (result == null) {
            try {
                final ParameterizedTemplateModels pts = parser.parse();
                result = pts.findReferencesTo(parser.getTemplateDir(), templateFile);
            } catch (final ParseException ex) {
                LOG.error("Error parsing model for template: " + templateFile, ex);
            }
        }
        return result;
    }

}

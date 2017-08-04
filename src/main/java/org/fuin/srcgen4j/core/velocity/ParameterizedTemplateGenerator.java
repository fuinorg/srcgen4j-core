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

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.fuin.srcgen4j.commons.GenerateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates files for a given {@link ParameterizedTemplateModels} model.
 */
public final class ParameterizedTemplateGenerator extends
        VelocityGenerator<ParameterizedTemplateModels> {

    private static final Logger LOG = LoggerFactory
            .getLogger(ParameterizedTemplateGenerator.class);

    /** Unique name of the only artifact type the generator produces. */
    public static final String ARTIFACT_NAME = "file";

    @Override
    protected final void generateIntern() throws GenerateException {

        final List<ParameterizedTemplateModel> modelList = getModel()
                .getModelList();
        if (modelList == null || modelList.size() == 0) {
            // Invalid model
            LOG.warn("No template models found");
            return;
        }

        for (final ParameterizedTemplateModel model : modelList) {
            generate(model);
        }

    }

    private void generate(final ParameterizedTemplateModel model)
            throws GenerateException {
        final List<TargetFile> targetFiles = model.createTargetFileList();
        if (targetFiles == null || targetFiles.size() == 0) {
            LOG.warn("No target files found: " + model.getTemplate()
                    + " [templates=" + model.getFile() + "]");
        } else {
            for (final TargetFile targetFile : targetFiles) {

                // Populate default values
                final VelocityContext context = new VelocityContext();
                if (model.getArguments() == null) {
                    LOG.debug("No default arguments");
                } else {
                    for (final Argument arg : model.getArguments()) {
                        context.put(arg.getKey(), arg.getValue());
                        LOG.debug("Default argument: " + arg);
                    }
                }

                // Set specific values for the target file
                if (targetFile.getArguments() == null) {
                    LOG.debug("No specific arguments");
                } else {
                    for (final Argument arg : targetFile.getArguments()) {
                        context.put(arg.getKey(), arg.getValue());
                        LOG.debug("Specific argument: " + arg);
                    }
                }

                merge(context, ARTIFACT_NAME, model.getTemplate(),
                        targetFile.getPathAndName());

            }
        }
    }

}

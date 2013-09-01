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

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.fuin.srcgen4j.commons.GenerateException;

/**
 * Generates files for the given {@link VelocityProducersConfig} model.
 */
public final class VelocityProducerGenerator extends VelocityGenerator<VelocityProducersConfig> {

    /** Unique name of the only artifact type the generator produces. */
    public static final String ARTIFACT_NAME = "file";

    @Override
    protected final void generateIntern() throws GenerateException {

        final List<VelocityProducerConfig> producerConfigList = getModel().getProducerConfigs();
        for (final VelocityProducerConfig producerConfig : producerConfigList) {

            final List<TargetFile> targetFiles = producerConfig.createTargetFileList();
            for (final TargetFile targetFile : targetFiles) {
                final VelocityContext context = new VelocityContext();
                if (targetFile.getArguments() != null) {
                    for (final Argument arg : targetFile.getArguments()) {
                        context.put(arg.getKey(), arg.getValue());
                    }
                }
                merge(context, ARTIFACT_NAME, producerConfig.getTemplate(),
                        targetFile.getPathAndName());
            }

        }

    }

}

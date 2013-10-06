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

import org.fuin.srcgen4j.commons.Config;
import org.fuin.srcgen4j.commons.ParseException;
import org.fuin.srcgen4j.commons.Parser;
import org.fuin.srcgen4j.commons.ParserConfig;

/**
 * Parses a given directory for XML files of type {@link ParameterizedTemplate}
 * or {@link ParameterizedTemplates} and combines all files into one model.
 */
public final class ParameterizedTemplateParser implements Parser<ParameterizedTemplates> {

    private ParameterizedTemplateParserConfig parserConfig;

    @Override
    public void initialize(final ParserConfig config) {
        final Config<ParserConfig> cfg = config.getConfig();
        if (!(cfg.getConfig() instanceof ParameterizedTemplateParserConfig)) {
            throw new IllegalStateException("The configuration is expected to be of type '"
                    + ParameterizedTemplateParserConfig.class.getName() + "', but was: "
                    + cfg.getConfig().getClass().getName());
        }
        parserConfig = (ParameterizedTemplateParserConfig) cfg.getConfig();
    }

    @Override
    public final ParameterizedTemplates parse() throws ParseException {
        final ParameterizedTemplates templates = new ParameterizedTemplates();
        populate(templates, parserConfig.getModelDir());
        return templates;
    }

    private void populate(final ParameterizedTemplates templates, final File dir) {

        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    populate(templates, file);
                } else {
                    if (ParameterizedTemplates.isParameterizedTemplatesFile(file)) {
                        templates.addParamTemplates(ParameterizedTemplates.create(file));
                    } else if (ParameterizedTemplate.isParameterizedTemplateFile(file)) {
                        templates.addParamTemplate(ParameterizedTemplate.create(file));
                    }
                }
            }
        }

    }

}

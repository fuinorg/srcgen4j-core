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
import java.util.Set;

import org.fuin.srcgen4j.commons.Config;
import org.fuin.srcgen4j.commons.IncrementalParser;
import org.fuin.srcgen4j.commons.ParseException;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a given directory for XML files of type {@link ParameterizedTemplate}
 * or {@link ParameterizedTemplates} and combines all files into one model.
 */
public final class ParameterizedTemplateParser implements IncrementalParser<ParameterizedTemplates> {

    private static final Logger LOG = LoggerFactory.getLogger(ParameterizedTemplateParser.class);

    private ParameterizedTemplateParserConfig parserConfig;

    private String name;

    @Override
    public void initialize(final ParserConfig config) {

        name = config.getName();

        LOG.debug("Initialize parser: " + name);

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
        LOG.info("Full parse: " + name);
        final ParameterizedTemplates templates = new ParameterizedTemplates();
        populateDir(templates, parserConfig.getModelDir());
        return templates;
    }

    @Override
    public final ParameterizedTemplates parse(final Set<File> files) throws ParseException {
        LOG.info("Incremental parse");
        final ParameterizedTemplates templates = new ParameterizedTemplates();
        for (final File file : files) {
            populateFile(templates, file);
        }
        return templates;
    }

    private void populateDir(final ParameterizedTemplates templates, final File dir) {

        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    populateDir(templates, file);
                } else {
                    populateFile(templates, file);
                }
            }
        }

    }

    private void populateFile(final ParameterizedTemplates templates, final File file) {
        if (ParameterizedTemplates.isParameterizedTemplatesFile(file)) {
            LOG.info("Adding templates file: " + file.getName());
            templates.addParamTemplates(ParameterizedTemplates.create(file));
        } else if (ParameterizedTemplate.isParameterizedTemplateFile(file)) {
            LOG.info("Adding template file: " + file.getName());
            templates.addParamTemplate(ParameterizedTemplate.create(file));
        }
    }

}

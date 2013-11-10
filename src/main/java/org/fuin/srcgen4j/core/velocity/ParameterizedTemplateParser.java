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
import java.io.FileFilter;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.fuin.srcgen4j.commons.IncrementalParser;
import org.fuin.srcgen4j.commons.ParseException;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.srcgen4j.core.base.AbstractParser;
import org.fuin.utils4j.fileprocessor.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a given directory for XML files of type
 * {@link ParameterizedTemplateModel} or {@link ParameterizedTemplateModels} and
 * combines all files into one model.
 */
public final class ParameterizedTemplateParser extends
        AbstractParser<ParameterizedTemplateParserConfig> implements
        IncrementalParser<ParameterizedTemplateModels> {

    private static final Logger LOG = LoggerFactory.getLogger(ParameterizedTemplateParser.class);

    private ParameterizedTemplateParserConfig parserConfig;

    private String name;

    private Map<String, String> varMap;

    private IOFileFilter fileFilter;

    private IncrementalFileHandler incrementalHandler;

    private FullFileHandler fullHandler;

    private IOFileFilter modelFilter;

    private IOFileFilter templateFilter;

    private SrcGen4JContext context;

    /**
     * Default constructor.
     */
    public ParameterizedTemplateParser() {
        super(ParameterizedTemplateParserConfig.class);
    }

    @Override
    public void initialize(final SrcGen4JContext context, final ParserConfig config) {

        name = config.getName();
        varMap = config.getParent().getVarMap();

        LOG.debug("Initialize parser: " + name);

        parserConfig = getConcreteConfig(config);
        modelFilter = new RegexFileFilter(parserConfig.getModelFilter());
        templateFilter = new RegexFileFilter(parserConfig.getTemplateFilter());
        fileFilter = new OrFileFilter(modelFilter, templateFilter);

        this.context = context;

    }

    @Override
    public final ParameterizedTemplateModels parse() throws ParseException {
        LOG.info("Full parse: " + name);
        if (fullHandler == null) {
            fullHandler = new FullFileHandler(this);
            final FileProcessor processor = new FileProcessor(fullHandler);
            processor.process(parserConfig.getModelDir());
            final ParameterizedTemplateModels models = fullHandler.getTemplates();
            models.init(context, varMap);
        }
        return fullHandler.getTemplates();
    }

    @Override
    public final ParameterizedTemplateModels parse(final Set<File> files) throws ParseException {
        LOG.info("Incremental parse");
        if (incrementalHandler == null) {
            incrementalHandler = new IncrementalFileHandler(this);
        }
        incrementalHandler.clear();
        for (final File file : files) {
            incrementalHandler.handleFile(file);
        }
        final ParameterizedTemplateModels models = incrementalHandler.getTemplates();
        models.init(context, varMap);
        return models;
    }

    @Override
    public final IOFileFilter getFileFilter() {
        return fileFilter;
    }

    /**
     * Returns the variable map used by the parser.
     * 
     * @return Key-Value map.
     */
    public final Map<String, String> getVarMap() {
        return varMap;
    }

    /**
     * Returns the filter for model files.
     * 
     * @return File filter.
     */
    public final FileFilter getModelFilter() {
        return modelFilter;
    }

    /**
     * Returns the filter for template files.
     * 
     * @return File filter.
     */
    public final FileFilter getTemplateFilter() {
        return templateFilter;
    }

    /**
     * Returns the template directory.
     * 
     * @return Canonical template directory.
     */
    public final File getTemplateDir() {
        return parserConfig.getTemplateDir();
    }

    /**
     * Returns the current context.
     * 
     * @return Context.
     */
    public final SrcGen4JContext getContext() {
        return context;
    }

}

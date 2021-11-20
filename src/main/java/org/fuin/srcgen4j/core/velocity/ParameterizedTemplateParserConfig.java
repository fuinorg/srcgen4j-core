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

import static org.fuin.utils4j.Utils4J.replaceVars;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.srcgen4j.commons.Config;
import org.fuin.srcgen4j.commons.GeneratorConfig;
import org.fuin.srcgen4j.commons.InitializableElement;
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.utils4j.Utils4J;
import org.fuin.srcgen4j.commons.AbstractElement;

/**
 * Configuration for a {@link ParameterizedTemplateParser}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parameterized-template-parser")
public class ParameterizedTemplateParserConfig extends AbstractElement
        implements InitializableElement<ParameterizedTemplateParserConfig, Config<GeneratorConfig>> {

    @XmlAttribute(name = "modelPath")
    private String modelPath;

    @XmlAttribute(name = "modelFilter")
    private String modelFilter;

    @XmlAttribute(name = "templatePath")
    private String templatePath;

    @XmlAttribute(name = "templateFilter")
    private String templateFilter;

    private transient File modelDir;

    private transient File templateDir;

    /**
     * Default constructor.
     */
    public ParameterizedTemplateParserConfig() {
        super();
    }

    /**
     * Constructor with model path.
     * 
     * @param modelPath
     *            Model path.
     * @param modelFilter
     *            Regular expression for selecting model files.
     */
    public ParameterizedTemplateParserConfig(final String modelPath, final String modelFilter) {
        super();
        this.modelPath = modelPath;
        this.modelFilter = modelFilter;
    }

    /**
     * Returns the model path.
     * 
     * @return Model path.
     */
    public final String getModelPath() {
        return modelPath;
    }

    /**
     * Returns the model directory.
     * 
     * @return Model directory or NULL.
     */
    public final File getModelDir() {
        if ((modelDir == null) && (modelPath != null)) {
            modelDir = Utils4J.getCanonicalFile(new File(modelPath));
        }
        return modelDir;
    }

    /**
     * Sets the model path to a new value.
     * 
     * @param modelPath
     *            Model path to set.
     */
    public final void setModelPath(final String modelPath) {
        this.modelPath = modelPath;
    }

    /**
     * Returns the regular expression for selecting model files.
     * 
     * @return Model file filter expression.
     */
    public final String getModelFilter() {
        return modelFilter;
    }

    /**
     * Sets the regular expression for selecting model files.
     * 
     * @param modelFilter
     *            Model file filter expression.
     */
    public final void setModelFilter(final String modelFilter) {
        this.modelFilter = modelFilter;
    }

    /**
     * Returns the template path.
     * 
     * @return Template path.
     */
    public final String getTemplatePath() {
        return templatePath;
    }

    /**
     * Returns the template directory.
     * 
     * @return Template directory or NULL.
     */
    public final File getTemplateDir() {
        if ((templateDir == null) && (templatePath != null)) {
            try {
                templateDir = new File(templatePath).getCanonicalFile();
            } catch (final IOException ex) {
                throw new RuntimeException("Couldn't determine canonical template file: " + templatePath, ex);
            }
        }
        return templateDir;
    }

    /**
     * Sets the template path to a new value.
     * 
     * @param templatePath
     *            Template path to set.
     */
    public final void setTemplatePath(final String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * Returns the regular expression for selecting template files.
     * 
     * @return Template file filter expression.
     */
    public final String getTemplateFilter() {
        return templateFilter;
    }

    /**
     * Sets the regular expression for selecting template files.
     * 
     * @param templateFilter
     *            Template file filter expression.
     */
    public final void setTemplateFilter(final String templateFilter) {
        this.templateFilter = templateFilter;
    }

    @Override
    public final ParameterizedTemplateParserConfig init(final SrcGen4JContext context, final Config<GeneratorConfig> parent,
            final Map<String, String> vars) {
        inheritVariables(vars);
        setModelPath(replaceVars(getModelPath(), getVarMap()));
        setTemplatePath(replaceVars(getTemplatePath(), getVarMap()));
        return this;
    }

}

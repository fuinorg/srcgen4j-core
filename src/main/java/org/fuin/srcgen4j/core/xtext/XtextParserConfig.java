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
package org.fuin.srcgen4j.core.xtext;

import java.io.File;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.srcgen4j.commons.AbstractElement;
import org.fuin.srcgen4j.commons.Config;
import org.fuin.srcgen4j.commons.InitializableElement;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.utils4j.Utils4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration for a {@link XtextParser}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xtext-parser-config")
public class XtextParserConfig extends AbstractElement implements
        InitializableElement<XtextParserConfig, Config<ParserConfig>> {

    private static final Logger LOG = LoggerFactory
            .getLogger(XtextParserConfig.class);

    @XmlAttribute(name = "modelPath")
    private String modelPath;

    @XmlAttribute(name = "modelExt")
    private String modelExt;

    @XmlAttribute(name = "setupClass")
    private String setupClassName;

    private transient SrcGen4JContext context;

    private transient File modelDir;

    private transient Class<?> setupClass;

    /**
     * Default constructor.
     */
    public XtextParserConfig() {
        super();
    }

    /**
     * Constructor with model path.
     * 
     * @param modelPath
     *            Model path.
     * @param modelExt
     *            Model file extension.
     */
    public XtextParserConfig(final String modelPath, final String modelExt) {
        super();
        this.modelPath = modelPath;
        this.modelExt = modelExt;
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
     * Returns the Model file extension.
     * 
     * @return Model file extension.
     */
    public final String getModelExt() {
        return modelExt;
    }

    /**
     * Sets the Model file extension.
     * 
     * @param modelExt
     *            Model file extension.
     */
    public final void setModelExt(final String modelExt) {
        this.modelExt = modelExt;
    }

    /**
     * Returns the setup class name.
     * 
     * @return Full qualified name of the class that does the DSL setup.
     */
    public final String getSetupClassName() {
        return setupClassName;
    }

    /**
     * Sets the setup class name to a new value.
     * 
     * @param setupClassName
     *            Full qualified name of the class that does the DSL setup.
     */
    public final void setSetupClassName(final String setupClassName) {
        this.setupClassName = setupClassName;
    }

    @Override
    public final XtextParserConfig init(final SrcGen4JContext context,
            final Config<ParserConfig> parent, final Map<String, String> vars) {
        this.context = context;
        inheritVariables(vars);
        setModelPath(replaceVars(getModelPath(), getVarMap()));
        return this;
    }

    /**
     * Returns the setup class.
     * 
     * @return Setup class of type {@link #setupClassName}.
     */
    public final Class<?> getSetupClass() {
        if (setupClass != null) {
            return setupClass;
        }
        LOG.info("Creating setup class: " + setupClassName);
        try {
            setupClass = Class.forName(setupClassName, true,
                    context.getClassLoader());
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException("Couldn't load setup class: "
                    + setupClassName, ex);
        }
        return setupClass;
    }

}

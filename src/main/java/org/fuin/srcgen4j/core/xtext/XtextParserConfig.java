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

import static org.fuin.utils4j.Utils4J.replaceVars;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.emf.common.util.URI;
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
public class XtextParserConfig extends AbstractElement implements InitializableElement<XtextParserConfig, Config<ParserConfig>> {

    private static final Logger LOG = LoggerFactory.getLogger(XtextParserConfig.class);

    @XmlAttribute(name = "modelPath")
    private String modelPath;

    @XmlAttribute(name = "modelExt")
    private String modelExt;

    @XmlAttribute(name = "setupClass")
    private String setupClassName;

    @XmlTransient
    private SrcGen4JContext context;

    @XmlTransient
    private List<File> modelDirs;

    @XmlTransient
    private List<URI> modelResources;

    @XmlTransient
    private Class<?> setupClass;

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
    public final XtextParserConfig init(final SrcGen4JContext context, final Config<ParserConfig> parent, final Map<String, String> vars) {
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
        LOG.info("Creating setup class: {}", setupClassName);
        try {
            setupClass = Class.forName(setupClassName, true, context.getClassLoader());
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException("Couldn't load setup class: " + setupClassName, ex);
        }
        return setupClass;
    }

    private List<String> paths() {
        final List<String> list = new ArrayList<>();
        final StringTokenizer tok = new StringTokenizer(modelPath, ";");
        while (tok.hasMoreTokens()) {
            list.add(tok.nextToken());
        }
        return list;
    }

    /**
     * Returns the information if the given string is a file reference. It's considered a file if there has no ':' character in the text.
     * 
     * @param str
     *            Text to test for a file.
     * 
     * @return {@code true} if it references a file.
     */
    public static boolean isFile(final String str) {
        return str.contains(":");
    }

    /**
     * Returns a file for the string.
     * 
     * @param str
     *            File reference.
     * 
     * @return File.
     */
    public static File asFile(final String str) {
        return Utils4J.getCanonicalFile(new File(str));
    }

    /**
     * Returns the information if the given string is a resource reference. It's considered a file if there is a ':' character in the text.
     * 
     * @param str
     *            Text to test for a resource.
     * 
     * @return {@code true} if it references a resource.
     */
    public static boolean isResource(final String str) {
        return !isFile(str);
    }

    /**
     * Returns a file for the string.
     * 
     * @param str
     *            File reference.
     * 
     * @return File.
     */
    public static URI asResource(final String str) {
        return URI.createURI(str);
    }

    /**
     * Returns a list of model directories to parse.
     * 
     * @return Directory list or NULL.
     */
    public final List<File> getModelDirs() {
        if ((modelDirs == null) && (modelPath != null)) {
            modelDirs = paths().stream().filter(XtextParserConfig::isFile).map(XtextParserConfig::asFile).collect(Collectors.toList());
        }
        return modelDirs;
    }

    /**
     * Returns a list of model resources to parse.
     * 
     * @return List of resources or NULL.
     */
    public final List<URI> getModelResources() {
        if ((modelResources == null) && (modelPath != null)) {
            modelResources = new ArrayList<>();
            modelResources = paths().stream().filter(XtextParserConfig::isResource).map(XtextParserConfig::asResource)
                    .collect(Collectors.toList());
        }
        return modelResources;
    }

}

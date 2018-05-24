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
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.TrimmedNotEmpty;
import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.MarshalObjectException;
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.srcgen4j.commons.UnmarshalObjectException;
import org.fuin.srcgen4j.core.base.Producer;
import org.fuin.utils4j.Utils4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Velocity template that has a number of parameters. Every target file defines concrete values for those parameters. The list of target
 * files is statically defined or dynamically created by another class that generates the list.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parameterized-template")
@XmlType(propOrder = { "template", "arguments", "targetFiles", "tflProducerConfig" })
@TargetArgsMatchTemplateArgs
@TargetFilesMustBeAvailable
public class ParameterizedTemplateModel implements Serializable, Comparable<ParameterizedTemplateModel>, Producer {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ParameterizedTemplateModel.class);

    @XmlAttribute
    @TrimmedNotEmpty
    private String template;

    @Valid
    @XmlElementWrapper(name = "arguments")
    @XmlElement(name = "argument")
    private List<Argument> arguments;

    @Valid
    @XmlElement(name = "target-file")
    private List<TargetFile> targetFiles;

    @Valid
    @XmlElement(name = "target-file-list-producer")
    private TargetFileListProducerConfig tflProducerConfig;

    private transient File file;

    /**
     * Default constructor.
     */
    public ParameterizedTemplateModel() {
        super();
    }

    /**
     * Constructor with file list.
     * 
     * @param template
     *            Relative path and name of the template - Cannot be NULL.
     * @param targetFiles
     *            Array of target files - Cannot be NULL and should have at least one entry.
     */
    public ParameterizedTemplateModel(final String template, final TargetFile... targetFiles) {
        super();

        this.template = template;
        this.targetFiles = new ArrayList<TargetFile>();
        this.tflProducerConfig = null;

        if (targetFiles != null) {
            for (final TargetFile targetFile : targetFiles) {
                this.targetFiles.add(targetFile);
            }
        }

    }

    /**
     * Constructor with target file list producer.
     * 
     * @param template
     *            Relative path and name of the template - Cannot be NULL.
     * @param tflProducerConfig
     *            Configuration - Cannot be NULL.
     */
    public ParameterizedTemplateModel(final String template, final TargetFileListProducerConfig tflProducerConfig) {
        super();

        this.template = template;
        this.targetFiles = null;
        this.tflProducerConfig = tflProducerConfig;

    }

    /**
     * Returns the path and name of the template.
     * 
     * @return Relative path and name.
     */
    public final String getTemplate() {
        return template;
    }

    /**
     * Sets the path and name of the template.
     * 
     * @param template
     *            Relative path and name.
     */
    public final void setTemplate(final String template) {
        this.template = template;
    }

    /**
     * Returns the list of arguments defined by the template. All values serve as default for the parameters defined in a target file.
     * 
     * @return Arguments and their default values defined for the template.
     */
    public final List<Argument> getArguments() {
        return arguments;
    }

    /**
     * Sets the list of arguments defined by the template. All values serve as default for the parameters defined in a target file.
     * 
     * @param arguments
     *            Arguments and their default values defined for the template or NULL.
     */
    public final void setArguments(final List<Argument> arguments) {
        this.arguments = arguments;
    }

    /**
     * Adds an argument to the template. If the list of arguments does not exist, it will be created.
     * 
     * @param argument
     *            Arguments to add.
     */
    public final void addArgument(@NotNull final Argument argument) {
        if (arguments == null) {
            arguments = new ArrayList<Argument>();
        }
        arguments.add(argument);
    }

    /**
     * Returns the list of target files to produce.
     * 
     * @return Target files.
     */
    public final List<TargetFile> getTargetFiles() {
        return targetFiles;
    }

    /**
     * Sets the list of target files to produce.
     * 
     * @param targetFiles
     *            Target file list to set or NULL.
     */
    public final void setTargetFiles(final List<TargetFile> targetFiles) {
        this.targetFiles = targetFiles;
    }

    /**
     * Returns the list of target files. Either by using the producer or by simply returning the internal list.
     * 
     * @return Target files - Never NULL and has at least one entry.
     */
    public final List<TargetFile> createTargetFileList() {
        if (tflProducerConfig == null) {
            LOG.info("Using target file list: {} elements", targetFiles.size());
            return targetFiles;
        }
        final TargetFileListProducer producer = tflProducerConfig.getTargetFileListProducer();
        LOG.info("Using target file list producer: {}", producer.getClass().getName());
        return producer.createTargetFiles();
    }

    /**
     * Returns the target file producer configuration.
     * 
     * @return Configuration.
     */
    public final TargetFileListProducerConfig getTargetFileListProducerConfig() {
        return tflProducerConfig;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((template == null) ? 0 : template.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParameterizedTemplateModel other = (ParameterizedTemplateModel) obj;
        if (template == null) {
            if (other.template != null) {
                return false;
            }
        } else if (!template.equals(other.template)) {
            return false;
        }
        return true;
    }

    // CHECKSTYLE:ON

    @Override
    public final int compareTo(final ParameterizedTemplateModel other) {
        return template.compareTo(other.template);
    }

    /**
     * Marshals the object to XML.
     * 
     * @param file
     *            File to write the instance to.
     */
    public final void writeToXml(final File file) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            helper.write(this, file, createJaxbContext());
        } catch (final MarshalObjectException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Marshals the object to an XML String.
     * 
     * @return XML String.
     */
    public final String toXml() {
        try {
            final JaxbHelper helper = new JaxbHelper();
            return helper.write(this, createJaxbContext());
        } catch (final MarshalObjectException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Marshals the object to XML.
     * 
     * @param writer
     *            Writer to write the instance to.
     */
    public final void writeToXml(final Writer writer) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            helper.write(this, writer, createJaxbContext());
        } catch (final MarshalObjectException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the file the template was parsed from.
     * 
     * @return Template file or NULL.
     */
    public final File getFile() {
        return file;
    }

    /**
     * Sets the file the template was parsed from.
     * 
     * @param file
     *            Template file or NULL.
     */
    public final void setFile(final File file) {
        this.file = file;
    }

    /**
     * Initializes the model.
     * 
     * @param context
     *            Current context.
     * @param vars
     *            Variables to use.
     */
    public final void init(final SrcGen4JContext context, final Map<String, String> vars) {

        if (template != null) {
            template = Utils4J.replaceVars(template, vars);
        }
        if (arguments != null) {
            for (final Argument argument : arguments) {
                argument.init(vars);
            }
        }
        if (targetFiles != null) {
            for (final TargetFile targetFile : targetFiles) {
                targetFile.init(vars);
            }
        }
        if (tflProducerConfig != null) {
            tflProducerConfig.init(context, this, vars);
        }
    }

    /**
     * Checks if this model has a reference to the given template file.
     * 
     * @param templateDir
     *            Directory where all template files are located - Cannot be NULL.
     * @param templateFile
     *            Template file to check - Cannot be NULL.
     * 
     * @return If the template is referenced TRUE else FALSE.
     */
    public final boolean hasReferenceTo(final File templateDir, final File templateFile) {
        final String p1 = Utils4J.getCanonicalPath(new File(templateDir, template));
        final String p2 = Utils4J.getCanonicalPath(templateFile);
        return p1.equals(p2);
    }

    private static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(ParameterizedTemplateModel.class, TargetFile.class, Argument.class);
        } catch (final JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates an instance by reading the XML from a reader.
     * 
     * @param reader
     *            Reader to use.
     * 
     * @return New instance.
     */
    public static ParameterizedTemplateModel create(final Reader reader) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final ParameterizedTemplateModel pc = helper.create(reader, createJaxbContext());
            Contract.requireValid(pc);
            return pc;
        } catch (final UnmarshalObjectException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates an instance by reading the XML from a file.
     * 
     * @param file
     *            File to read.
     * 
     * @return New instance.
     */
    public static ParameterizedTemplateModel create(final File file) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final ParameterizedTemplateModel pc = helper.create(file, createJaxbContext());
            pc.setFile(file);
            Contract.requireValid(pc);
            return pc;
        } catch (final UnmarshalObjectException ex) {
            throw new RuntimeException(ex);
        }
    }

}

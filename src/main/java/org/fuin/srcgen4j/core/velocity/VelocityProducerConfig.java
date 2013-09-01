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
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.TrimmedNotEmpty;
import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.MarshalObjectException;
import org.fuin.srcgen4j.commons.UnmarshalObjectException;
import org.fuin.srcgen4j.core.base.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates files using velocity.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "velocity-producer")
@XmlType(propOrder = { "targetFileListProducer", "targetFiles", "template" })
@TargetFilesMustBeAvailable
public class VelocityProducerConfig implements Serializable, Comparable<VelocityProducerConfig>,
        Producer {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(VelocityProducerConfig.class);

    @XmlAttribute
    @TrimmedNotEmpty
    private String template;

    @XmlElement(name = "target-file")
    private List<TargetFile> targetFiles;

    @XmlElement(name = "target-file-list-producer")
    private TargetFileListProducerConfig tflProducerConfig;

    /**
     * Default constructor.
     */
    public VelocityProducerConfig() {
        super();
    }

    /**
     * Constructor with file list.
     * 
     * @param template
     *            Relative path and name of the template - Cannot be NULL.
     * @param targetFiles
     *            Array of target files - Cannot be NULL and should have at
     *            least one entry.
     */
    public VelocityProducerConfig(final String template, final TargetFile... targetFiles) {
        super();

        this.template = template;
        this.targetFiles = new ArrayList<TargetFile>();
        this.tflProducerConfig = null;

        if (targetFiles != null) {
            for (final TargetFile targetFile : targetFiles) {
                this.targetFiles.add(targetFile);
            }
        }

        Contract.requireValid(this);
    }

    /**
     * Constructor with producer.
     * 
     * @param template
     *            Relative path and name of the template - Cannot be NULL.
     * @param tflProducerConfig
     *            Configuration - Cannot be NULL.
     */
    public VelocityProducerConfig(final String template,
            final TargetFileListProducerConfig tflProducerConfig) {
        super();

        this.template = template;
        this.targetFiles = null;
        this.tflProducerConfig = tflProducerConfig;

        Contract.requireValid(this);
    }

    /**
     * Returns the path and name of the template.
     * 
     * @return Relative path and name - Never NULL.
     */
    public final String getTemplate() {
        return template;
    }

    /**
     * Returns the list of target files to produce.
     * 
     * @return Target files - Never NULL and has at least one entry.
     */
    public final List<TargetFile> getTargetFiles() {
        return targetFiles;
    }

    /**
     * Returns the list of target files to produce. Either by using the producer
     * or by simply returning the internal list.
     * 
     * @return Target files - Never NULL and has at least one entry.
     */
    public final List<TargetFile> createTargetFileList() {
        if (tflProducerConfig == null) {
            LOG.info("Using target file list: " + targetFiles.size() + " elements");
            return targetFiles;
        }
        final TargetFileListProducer producer = tflProducerConfig.getTargetFileListProducer();
        LOG.info("Using target file list producer: " + producer.getClass().getName());
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
        final VelocityProducerConfig other = (VelocityProducerConfig) obj;
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
    public final int compareTo(final VelocityProducerConfig other) {
        return template.compareTo(other.template);
    }

    private static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(VelocityProducerConfig.class, TargetFile.class,
                    Argument.class);
        } catch (final JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Checks if the given file contains a serialized velocity producer.
     * 
     * @param file
     *            File to check.
     * 
     * @return If the file seems to contain velocity producer XML content TRUE
     *         else FALSE.
     */
    public static boolean isVelocityProducerFile(final File file) {
        if (!file.getName().endsWith(".xml")) {
            return false;
        }
        final JaxbHelper helper = new JaxbHelper();
        return helper.containsStartTag(file, "velocity-producer");
    }

    /**
     * Creates an instance by reading the XML from a reader.
     * 
     * @param reader
     *            Reader to use.
     * 
     * @return New instance.
     */
    public static VelocityProducerConfig create(final Reader reader) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final VelocityProducerConfig pc = helper.create(reader, createJaxbContext());
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
    public static VelocityProducerConfig create(final File file) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final VelocityProducerConfig pc = helper.create(file, createJaxbContext());
            Contract.requireValid(pc);
            return pc;
        } catch (final UnmarshalObjectException ex) {
            throw new RuntimeException(ex);
        }
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

}

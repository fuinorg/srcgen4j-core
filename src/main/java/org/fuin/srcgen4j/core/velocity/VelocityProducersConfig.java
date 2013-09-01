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
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.MarshalObjectException;
import org.fuin.srcgen4j.commons.UnmarshalObjectException;

/**
 * Configuration for several velocity producers.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "velocity-producers")
@XmlType(propOrder = { "producerConfigs" })
public class VelocityProducersConfig {

    @XmlElement(name = "velocity-producer")
    private List<VelocityProducerConfig> producerConfigs;

    /**
     * Default constructor.
     */
    public VelocityProducersConfig() {
        super();
    }

    /**
     * Returns the list of producer configurations.
     * 
     * @return Configuration list.
     */
    public final List<VelocityProducerConfig> getProducerConfigs() {
        return producerConfigs;
    }

    /**
     * Sets the list of producer configurations to a new value.
     * 
     * @param producerConfigs
     *            Configuration list to set.
     */
    public final void setProducerConfigs(final List<VelocityProducerConfig> producerConfigs) {
        this.producerConfigs = producerConfigs;
    }

    /**
     * Adds another producer configuration to the list. If the list does not
     * exist,it will be created.
     * 
     * @param producerConfig
     *            Configuration to add - Cannot be NULL.
     */
    public final void addProducerConfig(final VelocityProducerConfig producerConfig) {
        if (producerConfigs == null) {
            producerConfigs = new ArrayList<VelocityProducerConfig>();
        }
        producerConfigs.add(producerConfig);
    }

    /**
     * Adds all configurations from another producers configuration to the list.
     * If the list does not exist,it will be created.
     * 
     * @param producersConfig
     *            Configuration to add - Cannot be NULL.
     */
    public final void addProducersConfig(final VelocityProducersConfig producersConfig) {
        final List<VelocityProducerConfig> list = producersConfig.getProducerConfigs();
        if (list != null) {
            for (final VelocityProducerConfig cfg : list) {
                addProducerConfig(cfg);
            }
        }
    }

    private static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(VelocityProducersConfig.class,
                    VelocityProducerConfig.class, TargetFile.class, Argument.class);
        } catch (final JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Checks if the given file contains a serialized velocity producers object.
     * 
     * @param file
     *            File to check.
     * 
     * @return If the file seems to contain velocity producer XML content TRUE
     *         else FALSE.
     */
    public static boolean isVelocityProducersFile(final File file) {
        if (!file.getName().endsWith(".xml")) {
            return false;
        }
        final JaxbHelper helper = new JaxbHelper();
        return helper.containsStartTag(file, "velocity-producers");
    }

    /**
     * Creates an instance by reading the XML from a reader.
     * 
     * @param reader
     *            Reader to use.
     * 
     * @return New instance.
     */
    public static VelocityProducersConfig create(final Reader reader) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final VelocityProducersConfig pcs = helper.create(reader, createJaxbContext());
            Contract.requireValid(pcs);
            return pcs;
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
    public static VelocityProducersConfig create(final File file) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final VelocityProducersConfig pcs = helper.create(file, createJaxbContext());
            Contract.requireValid(pcs);
            return pcs;
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

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

import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NotEmpty;
import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.MarshalObjectException;
import org.fuin.srcgen4j.commons.UnmarshalObjectException;

/**
 * A collection of multiple {@link ParameterizedTemplate} objects.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parameterized-templates")
@XmlType(propOrder = { "paramTemplates" })
public class ParameterizedTemplates {

    @Valid
    @NotEmpty
    @XmlElement(name = "parameterized-template")
    private List<ParameterizedTemplate> paramTemplates;

    /**
     * Default constructor.
     */
    public ParameterizedTemplates() {
        super();
    }

    /**
     * Returns the list of templates.
     * 
     * @return Template list.
     */
    public final List<ParameterizedTemplate> getParamTemplates() {
        return paramTemplates;
    }

    /**
     * Sets the list of templates to a new value.
     * 
     * @param paramTemplates
     *            Template list to set.
     */
    public final void setParamTemplates(final List<ParameterizedTemplate> paramTemplates) {
        this.paramTemplates = paramTemplates;
    }

    /**
     * Adds another template to the list. If the list does not exist,it will be
     * created.
     * 
     * @param paramTemplate
     *            Template to add - Cannot be NULL.
     */
    public final void addParamTemplate(final ParameterizedTemplate paramTemplate) {
        if (paramTemplates == null) {
            paramTemplates = new ArrayList<ParameterizedTemplate>();
        }
        paramTemplates.add(paramTemplate);
    }

    /**
     * Adds all templates from another configuration to the list. If the list
     * does not exist,it will be created.
     * 
     * @param paramTemplates
     *            Container with templates to add - Cannot be NULL.
     */
    public final void addParamTemplates(final ParameterizedTemplates paramTemplates) {
        final List<ParameterizedTemplate> list = paramTemplates.getParamTemplates();
        if (list != null) {
            for (final ParameterizedTemplate template : list) {
                addParamTemplate(template);
            }
        }
    }

    private static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(ParameterizedTemplates.class,
                    ParameterizedTemplate.class, TargetFile.class, Argument.class);
        } catch (final JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Checks if the given file contains a serialized object of this type.
     * 
     * @param file
     *            File to check.
     * 
     * @return If the file contains XML content of the correct type TRUE else
     *         FALSE.
     */
    public static boolean isParameterizedTemplatesFile(final File file) {
        if (!file.getName().endsWith(".xml")) {
            return false;
        }
        final JaxbHelper helper = new JaxbHelper();
        return helper.containsStartTag(file, "parameterized-templates");
    }

    /**
     * Creates an instance by reading the XML from a reader.
     * 
     * @param reader
     *            Reader to use.
     * 
     * @return New instance.
     */
    public static ParameterizedTemplates create(final Reader reader) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final ParameterizedTemplates pcs = helper.create(reader, createJaxbContext());
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
    public static ParameterizedTemplates create(final File file) {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final ParameterizedTemplates pcs = helper.create(file, createJaxbContext());
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

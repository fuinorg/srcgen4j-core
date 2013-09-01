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
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.srcgen4j.commons.AbstractElement;
import org.fuin.srcgen4j.commons.GeneratorConfig;
import org.fuin.srcgen4j.commons.InitializableElement;

/**
 * Configuration for the velocity generator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "velocity-generator-config")
public class VelocityGeneratorConfig extends AbstractElement implements
        InitializableElement<VelocityGeneratorConfig, GeneratorConfig> {

    @XmlAttribute(name = "templatePath")
    private String templatePath;

    /**
     * Default constructor.
     */
    public VelocityGeneratorConfig() {
        super();
    }

    /**
     * Constructor with template path.
     * 
     * @param templatePath
     *            Template path.
     */
    public VelocityGeneratorConfig(final String templatePath) {
        super();
        this.templatePath = templatePath;
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
        if (templatePath == null) {
            return null;
        }
        return new File(templatePath);
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

    @Override
    public final VelocityGeneratorConfig init(final GeneratorConfig parent,
            final Map<String, String> vars) {
        setTemplatePath(replaceVars(getTemplatePath(), vars));
        return this;
    }

}

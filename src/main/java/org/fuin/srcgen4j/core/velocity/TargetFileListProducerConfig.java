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

import java.util.Map;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.vo.TrimmedNotEmpty;
import org.fuin.srcgen4j.commons.AbstractElement;
import org.fuin.srcgen4j.commons.InitializableElement;
import org.fuin.utils4j.Utils4J;

/**
 * Configuration for a {@link TargetFileListProducer}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "target-file-list-producer")
@XmlType(propOrder = { "config", "className" })
public class TargetFileListProducerConfig extends AbstractElement implements
        InitializableElement<TargetFileListProducerConfig, ParameterizedTemplate> {

    @TrimmedNotEmpty
    @XmlAttribute(name = "class")
    private String className;

    @Valid
    @XmlAnyElement(lax = true)
    private Object config;

    private transient TargetFileListProducer tflProducer;

    private transient ParameterizedTemplate parent;

    /**
     * Default constructor.
     */
    public TargetFileListProducerConfig() {
        super();
    }

    /**
     * Returns the name of the class that implements
     * {@link TargetFileListProducer}.
     * 
     * @return Full qualified name.
     */
    public final String getClassName() {
        return className;
    }

    /**
     * Sets the name of the class that implements {@link TargetFileListProducer}
     * .
     * 
     * @param className
     *            Full qualified name.
     */
    public final void setClassName(final String className) {
        this.className = className;
    }

    /**
     * Returns the specific configuration for the target file list producer.
     * 
     * @return Configuration.
     */
    public final Object getConfig() {
        return config;
    }

    /**
     * Sets the specific configuration for the target file list producer.
     * 
     * @param config
     *            Configuration.
     */
    public final void setConfig(final Object config) {
        this.config = config;
    }

    /**
     * Returns the parent of the object.
     * 
     * @return Parent.
     */
    public final ParameterizedTemplate getParent() {
        return parent;
    }

    /**
     * Sets the parent of the object.
     * 
     * @param parent
     *            Parent.
     */
    public final void setParent(final ParameterizedTemplate parent) {
        this.parent = parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final TargetFileListProducerConfig init(final ParameterizedTemplate parent,
            final Map<String, String> vars) {
        setParent(parent);
        setClassName(replaceVars(getClassName(), vars));
        if (config instanceof InitializableElement) {
            final InitializableElement<?, TargetFileListProducerConfig> ie;
            ie = (InitializableElement<?, TargetFileListProducerConfig>) config;
            ie.init(this, vars);
        }
        return this;
    }

    /**
     * Returns an existing target file list producer instance or creates a new
     * one if it's the first call to this method.
     * 
     * @return TargetFileListProducer of type {@link #className}.
     */
    public final TargetFileListProducer getTargetFileListProducer() {
        if (tflProducer != null) {
            return tflProducer;
        }
        final Object obj = Utils4J.createInstance(className);
        if (!(obj instanceof TargetFileListProducer)) {
            throw new IllegalStateException("Expected class to be of type '"
                    + TargetFileListProducer.class.getName() + "', but was: " + className);
        }
        tflProducer = (TargetFileListProducer) obj;
        return tflProducer;
    }

}

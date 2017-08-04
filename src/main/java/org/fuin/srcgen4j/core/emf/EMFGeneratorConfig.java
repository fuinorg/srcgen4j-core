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
package org.fuin.srcgen4j.core.emf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.Nullable;
import org.fuin.srcgen4j.commons.AbstractElement;
import org.fuin.srcgen4j.commons.ArtifactFactory;
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig;
import org.fuin.srcgen4j.commons.Config;
import org.fuin.srcgen4j.commons.GeneratorConfig;
import org.fuin.srcgen4j.commons.InitializableElement;
import org.fuin.srcgen4j.commons.SrcGen4JContext;

/**
 * Configuration for a {@link EMFGenerator}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "emf-generator-config")
public class EMFGeneratorConfig extends AbstractElement implements
        InitializableElement<EMFGeneratorConfig, Config<GeneratorConfig>> {

    @Nullable
    @Valid
    @XmlElement(name = "artifact-factory")
    private List<ArtifactFactoryConfig> factoryConfigs;

    private transient List<ArtifactFactory<?>> factories;

    /**
     * Default constructor.
     */
    public EMFGeneratorConfig() {
        super();
    }

    @Override
    public final EMFGeneratorConfig init(final SrcGen4JContext context,
            final Config<GeneratorConfig> parent, final Map<String, String> vars) {

        inheritVariables(vars);
        if (factoryConfigs != null) {
            for (final ArtifactFactoryConfig factoryConfig : factoryConfigs) {
                factoryConfig.init(context, getVarMap());
            }
        }
        return this;
    }

    /**
     * Returns a list of factories for the given model type.
     * 
     * @param modelType
     *            Model type to return artifact factories for.
     * 
     * @return List of factories that accept the given model type.
     * 
     * @param <MODEL>
     *            Type of the model to create an artifact for.
     */
    @SuppressWarnings("unchecked")
    @NeverNull
    public final <MODEL> List<ArtifactFactory<MODEL>> getFactories(
            final Class<MODEL> modelType) {
        final List<ArtifactFactory<MODEL>> list = new ArrayList<ArtifactFactory<MODEL>>();
        if (factories == null) {
            factories = new ArrayList<ArtifactFactory<?>>();
            if (factoryConfigs != null) {
                for (final ArtifactFactoryConfig factoryConfig : factoryConfigs) {
                    factories.add(factoryConfig.getFactory());
                }
            }
        }
        for (final ArtifactFactory<?> factory : factories) {
            if (modelType.isAssignableFrom(factory.getModelType())) {
                list.add((ArtifactFactory<MODEL>) factory);
            }
        }
        return list;
    }

}

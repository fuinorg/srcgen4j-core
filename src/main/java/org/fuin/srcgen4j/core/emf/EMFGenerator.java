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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.fuin.srcgen4j.commons.ArtifactFactory;
import org.fuin.srcgen4j.commons.GenerateException;
import org.fuin.srcgen4j.commons.GeneratedArtifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates content based on an ECORE {@link ResourceSet}.
 */
public final class EMFGenerator extends AbstractEMFGenerator<EMFGeneratorConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(EMFGenerator.class);

    private final Set<ArtifactFactory<Notifier>> notifierFactories;

    private final Set<ArtifactFactory<ResourceSet>> resourceSetFactories;

    /**
     * Default constructor.
     */
    public EMFGenerator() {
        super();
        notifierFactories = new HashSet<ArtifactFactory<Notifier>>();
        resourceSetFactories = new HashSet<ArtifactFactory<ResourceSet>>();
    }

    @Override
    public final Class<EMFGeneratorConfig> getSpecificConfigClass() {
        return EMFGeneratorConfig.class;
    }

    @Override
    public final void init() {

        final EMFGeneratorConfig config = getSpecificConfig();

        // Add factories interested in resource sets
        final List<ArtifactFactory<ResourceSet>> rsFactories = config.getFactories(ResourceSet.class);
        for (final ArtifactFactory<ResourceSet> factory : rsFactories) {
            resourceSetFactories.add(factory);
            LOG.debug("Added resource set factory: {}", factory.getClass());
        }

        // Add factories interested in any type of notifier
        final List<ArtifactFactory<Notifier>> nfactories = config.getFactories(Notifier.class);
        for (final ArtifactFactory<Notifier> factory : nfactories) {
            notifierFactories.add(factory);
            LOG.debug("Added notifier factory for model type '{}': {}", factory.getModelType(), factory.getClass());
        }

    }

    @Override
    protected final boolean wants(final Notifier notifier) {
        for (final ArtifactFactory<Notifier> factory : notifierFactories) {
            if (factory.getModelType().isAssignableFrom(notifier.getClass())) {
                return true;
            }
        }
        return false;
    }

    private Set<ArtifactFactory<Notifier>> findFactories(final Notifier notifier) {
        final Set<ArtifactFactory<Notifier>> factories = new HashSet<ArtifactFactory<Notifier>>();
        for (final ArtifactFactory<Notifier> factory : notifierFactories) {
            if (factory.getModelType().isAssignableFrom(notifier.getClass())) {
                factories.add(factory);
            }
        }
        return factories;
    }

    @Override
    protected final void generate(@NotNull final Map<String, Object> context, @NotNull final Notifier notifier, final boolean incremental,
            final boolean preparationRun) throws GenerateException {

        LOG.debug("Generate from {}", Notifier.class.getSimpleName());

        final Set<ArtifactFactory<Notifier>> factories = findFactories(notifier);
        if (factories.size() == 0) {
            LOG.warn("Was asked to generate an artifact type I didn't request: {}", notifier.getClass());
            return;
        }

        for (final ArtifactFactory<Notifier> factory : factories) {
            if (!incremental || factory.isIncremental()) {
                LOG.debug("Generate with factory {}", factory.getClass().getSimpleName());
                final GeneratedArtifact generatedArtifact = factory.create(notifier, context, preparationRun);
                if ((generatedArtifact != null) && !preparationRun) {
                    write(generatedArtifact);
                }
            }
        }

    }

    @Override
    protected final void afterGenerate(@NotNull final Map<String, Object> context, final boolean incremental, final boolean preparationRun)
            throws GenerateException {

        LOG.debug("Generate from {}", ResourceSet.class.getSimpleName());

        for (final ArtifactFactory<ResourceSet> factory : resourceSetFactories) {
            final GeneratedArtifact generatedArtifact = factory.create(getModel(), context, preparationRun);
            if ((generatedArtifact != null) && !preparationRun) {
                write(generatedArtifact);
            }
        }

    }

}

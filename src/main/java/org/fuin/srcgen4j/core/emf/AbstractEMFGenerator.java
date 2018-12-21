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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.fuin.srcgen4j.commons.GenerateException;
import org.fuin.srcgen4j.commons.Generator;
import org.fuin.srcgen4j.core.base.AbstractGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator that uses an ECORE resource set as input.
 * 
 * @param <CONFIG>
 *            Type of the generator specific configuration - Use 'Object' if the generator does not require any configuration.
 */
public abstract class AbstractEMFGenerator<CONFIG> extends AbstractGenerator<ResourceSet, CONFIG> implements Generator<ResourceSet> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEMFGenerator.class);
    
    /**
     * default constructor.
     */
    public AbstractEMFGenerator() {
        super();
    }

    @Override
    protected final void generate(final boolean incremental) throws GenerateException {

        final Map<String, Object> context = new HashMap<String, Object>();

        for (int i = 0; i < 2; i++) {

            final boolean preparationRun = (i == 0);

            beforeGenerate(context, incremental, preparationRun);

            int total = 0;
            int wants = 0;
            final Iterator<Notifier> it = getModel().getAllContents();
            while (it.hasNext()) {
                final Notifier notifier = it.next();
                total++;
                if (wants(notifier)) {
                    wants++;
                    generate(context, notifier, incremental, preparationRun);
                }
            }
            
            if (total == 0) {
                LOG.error("EMF model contains no content (incremental={})", incremental);
            } else {
                if (wants == 0) {
                    LOG.warn("EMF model contains {} elements, but the generator wanted none (incremental={})", total, incremental);
                } else {
                    LOG.info("EMF model contains {} elements and generator wanted {} of them (incremental={})", total, wants, incremental);
                }
            }

            afterGenerate(context, incremental, preparationRun);

        }

    }

    /**
     * Called before the generation process starts. Extension point for sub classes that want to prepare something. Default is to do
     * nothing.
     * 
     * @param context
     *            Map used to store information during the generation process.
     * @param incremental
     *            If this is an incremental build TRUE, else FALSE (full build).
     * @param preparationRun
     *            TRUE if this is the preparation (dry) run, else FALSE (real generation).
     * 
     * @throws GenerateException
     *             Error when generating.
     */
    protected void beforeGenerate(@NotNull final Map<String, Object> context, final boolean incremental, final boolean preparationRun)
            throws GenerateException {
        // May be overwritten by sub classes
    }

    /**
     * Called after the generation process starts. Extension point for sub classes that want to do some clean up. Default is to do nothing.
     * 
     * @param context
     *            Map used to store information during the generation process.
     * @param incremental
     *            If this is an incremental build TRUE, else FALSE (full build).
     * @param preparationRun
     *            TRUE if this is the preparation (dry) run, else FALSE (real generation).
     * 
     * @throws GenerateException
     *             Error when generating.
     */
    protected void afterGenerate(@NotNull final Map<String, Object> context, final boolean incremental, final boolean preparationRun)
            throws GenerateException {
        // May be overwritten by sub classes
    }

    /**
     * Determines if the concrete generator is interested in a given notifier type.
     * 
     * @param notifier
     *            Type of notifier.
     * 
     * @return If the {@link #generate(Map, Notifier, boolean, boolean)} method should be called TRUE, else FALSE.
     */
    protected abstract boolean wants(@NotNull Notifier notifier);

    /**
     * Generates the appropriate content for a given notifier.
     * 
     * @param context
     *            Map used to store information during the generation process.
     * @param notifier
     *            Notifier to handle.
     * @param incremental
     *            If this is an incremental build TRUE, else FALSE (full build).
     * @param preparationRun
     *            TRUE if this is the preparation (dry) run, else FALSE (real generation).
     * 
     * @throws GenerateException
     *             Error when generating.
     */
    protected abstract void generate(@NotNull Map<String, Object> context, @NotNull Notifier notifier, boolean incremental,
            final boolean preparationRun) throws GenerateException;

}

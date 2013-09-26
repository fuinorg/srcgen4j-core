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
package org.fuin.srcgen4j.core.base;

import java.io.File;

import org.fuin.objects4j.common.Contract;
import org.fuin.srcgen4j.commons.Folder;
import org.fuin.srcgen4j.commons.GenerateException;
import org.fuin.srcgen4j.commons.Generator;
import org.fuin.srcgen4j.commons.GeneratorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for concrete generators.
 * 
 * @param <MODEL>
 *            Type of the model.
 * @param <CONFIG>
 *            Type of the generator specific configuration - Use 'Object' if the
 *            generator does not require any configuration.
 */
public abstract class AbstractGenerator<MODEL, CONFIG> implements Generator<MODEL> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractGenerator.class);

    private GeneratorConfig config;

    private MODEL model;

    /**
     * Default constructor.
     */
    public AbstractGenerator() {
        super();
    }

    @Override
    public final void generate(final GeneratorConfig config, final MODEL model)
            throws GenerateException {

        Contract.requireArgNotNull("config", config);
        Contract.requireArgNotNull("model", model);
        Contract.requireValid(config);
        Contract.requireValid(model);

        this.config = config;
        this.model = model;

        final Object obj = config.getConfig().getConfig();
        if (getSpecificConfigClass() == null) {
            if (obj != null) {
                throw new IllegalStateException("No configuration is expected, but was: "
                        + obj.getClass());
            }
        } else {
            if (obj == null) {
                throw new IllegalStateException("The configuration is expected to be of type '"
                        + getSpecificConfigClass().getName() + "', but was: null");
            }
            if (!getSpecificConfigClass().isAssignableFrom(obj.getClass())) {
                throw new IllegalStateException("The configuration is expected to be of type '"
                        + getSpecificConfigClass().getName() + "', but was: "
                        + obj.getClass().getName());
            }
        }

        LOG.info("Starting generation");

        generate();

        LOG.info("Generation finished");

    }

    /**
     * Returns the configuration.
     * 
     * @return Configuration.
     */
    public final GeneratorConfig getConfig() {
        return config;
    }

    /**
     * Returns the model.
     * 
     * @return Model.
     */
    public final MODEL getModel() {
        return model;
    }

    /**
     * Returns the class of the expected configuration class. This method can be
     * overridden to make sure the configuration is valid.
     * 
     * @return Specific configuration class or NULL if no configuration is
     *         required.
     */
    // CHECKSTYLE:OFF Empty methods do not violate the 'design for extension'
    // principle
    public Class<CONFIG> getSpecificConfigClass() {
        // CHECKSTYLE:ON
        return null;
    }

    /**
     * Returns the specific configuration. This is a shortcut for
     * {@link #getConfig().getConfig().getConfig()}.
     * 
     * @return Generator configuration or NULL.
     */
    @SuppressWarnings("unchecked")
    public final CONFIG getSpecificConfig() {
        return (CONFIG) config.getConfig().getConfig();
    }

    /**
     * Returns the target file for a given artifact type and filename. This
     * method takes care about eventually creating non existing directories or
     * protect existing files to be overridden.
     * 
     * @param artifactName
     *            Type of the artifact to create - This has to refer to an
     *            existing entry in the configuration (Like '&lt;artifact
     *            name="<i>artifactName</i>" ..&gt;').
     * @param filename
     *            Name of the target file (without a path).
     * @param logInfo
     *            Addition information to log.
     * 
     * @return File to write to or NULL if nothing should be written.
     */
    protected final GeneratedFile getTargetFile(final String artifactName, final String filename,
            final String logInfo) {

        final Folder folder = getConfig().findTargetFolder(artifactName);
        final File dir = new File(folder.getDirectory());
        final File file = new File(dir, filename);

        // Make sure the path exists
        if (!file.getParentFile().exists()) {
            if (folder.isCreate()) {
                file.getParentFile().mkdirs();
            } else {
                throw new IllegalStateException("Directory '" + file.getParentFile()
                        + "' does not exist, but configuration does not allow creation: "
                        + "<folder name=\"" + folder.getName() + "\" create=\"false\" ... />");
            }
        }

        if (file.exists() && !folder.isOverride()) {
            // Skip file because override is not allowed
            return new GeneratedFile(file, logInfo, true);
        }

        return new GeneratedFile(file, logInfo);

    }

    /**
     * Generates something using the given configuration and model.
     * 
     * @throws GenerateException
     *             Error when generating.
     */
    protected abstract void generate() throws GenerateException;

}

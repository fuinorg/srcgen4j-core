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

import org.fuin.objects4j.common.Contract;
import org.fuin.srcgen4j.commons.Config;
import org.fuin.srcgen4j.commons.ParserConfig;

/**
 * Base class for all parsers.
 * 
 * @param <CONFIG_TYPE>
 *            Type of the concrete configuration.
 */
public abstract class AbstractParser<CONFIG_TYPE> {

    private Class<CONFIG_TYPE> concreteConfigClass;

    /**
     * Constructor with class.
     * 
     * @param concreteConfigClass
     *            Type of the configuration.
     */
    public AbstractParser(final Class<CONFIG_TYPE> concreteConfigClass) {
        super();
        Contract.requireArgNotNull("concreteConfigClass", concreteConfigClass);
        this.concreteConfigClass = concreteConfigClass;
    }

    /**
     * Returns the parser configuration.
     * 
     * @param config
     *            Wrapping parser configuration that contains the concrete one.
     * 
     * @return Parser specific configuration.
     */
    @SuppressWarnings("unchecked")
    protected final CONFIG_TYPE getConcreteConfig(final ParserConfig config) {
        final Config<ParserConfig> cfg = config.getConfig();
        if (cfg == null) {
            throw new IllegalStateException(
                    "The configuration is expected to be of type '"
                            + concreteConfigClass.getName()
                            + "', but was: null");
        } else {
            if (!(concreteConfigClass.isAssignableFrom(cfg.getConfig()
                    .getClass()))) {
                throw new IllegalStateException(
                        "The configuration is expected to be of type '"
                                + concreteConfigClass.getName()
                                + "', but was: "
                                + cfg.getConfig().getClass().getName()
                                + " - Did you add the configuration class to the JXB context?");
            }
        }
        return (CONFIG_TYPE) cfg.getConfig();
    }
}

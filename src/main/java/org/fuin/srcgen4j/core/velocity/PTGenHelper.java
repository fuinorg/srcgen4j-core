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
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.SrcGen4JConfig;
import org.fuin.srcgen4j.commons.UnmarshalObjectException;

/**
 * Utility class for the {@link ParameterizedTemplateGenerator}.
 */
public final class PTGenHelper {

    private PTGenHelper() {
        // Never used
    }

    /**
     * Creates and initializes a SrcGen4J configuration from a configuration
     * file that contains ONLY generators/parsers of type
     * {@link ParameterizedTemplateParser} and
     * {@link ParameterizedTemplateGenerator}.
     * 
     * @param configFile
     *            XML configuration file to read - Cannot be NULL.
     * 
     * @return New configuration instance.
     * 
     * @throws UnmarshalObjectException
     *             Error reading the configuration.
     */
    public static SrcGen4JConfig createAndInit(final File configFile)
            throws UnmarshalObjectException {
        try {
            final JaxbHelper helper = new JaxbHelper();
            final SrcGen4JConfig config = helper.create(configFile, JAXBContext.newInstance(
                    SrcGen4JConfig.class, VelocityGeneratorConfig.class,
                    ParameterizedTemplateParserConfig.class,
                    ParameterizedTemplateGeneratorConfig.class));
            config.init(configFile.getParentFile().getCanonicalFile());
            return config;
        } catch (final IOException ex) {
            throw new UnmarshalObjectException("Error getting canonical path: "
                    + configFile.getParentFile(), ex);
        } catch (final JAXBException ex) {
            throw new UnmarshalObjectException("Error reading the configuration: " + configFile, ex);
        }
    }

}

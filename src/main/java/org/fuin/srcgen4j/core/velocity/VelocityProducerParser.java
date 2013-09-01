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

import org.fuin.srcgen4j.commons.ParseException;
import org.fuin.srcgen4j.commons.Parser;
import org.fuin.srcgen4j.commons.ParserConfig;

/**
 * Parses a given directory for XML files of type {@link VelocityProducerConfig}
 * or {@link VelocityProducersConfig} and combines all files into one model.
 */
public final class VelocityProducerParser implements Parser<VelocityProducersConfig> {

    private void populate(final VelocityProducersConfig config, final File dir) {

        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    populate(config, file);
                } else {
                    if (VelocityProducersConfig.isVelocityProducersFile(file)) {
                        final VelocityProducersConfig cfg = VelocityProducersConfig.create(file);
                        config.addProducersConfig(cfg);
                    } else if (VelocityProducerConfig.isVelocityProducerFile(file)) {
                        config.addProducerConfig(VelocityProducerConfig.create(file));
                    }
                }
            }
        }

    }

    @Override
    public final VelocityProducersConfig parse(final ParserConfig config) throws ParseException {
        final Object obj = config.getConfig();
        if (!(obj instanceof VelocityProducerParserConfig)) {
            throw new IllegalStateException("The configuration is expected to be of type '"
                    + VelocityProducerParserConfig.class.getName() + "', but was: "
                    + obj.getClass().getName());
        }
        final VelocityProducerParserConfig cfg = (VelocityProducerParserConfig) obj;
        final VelocityProducersConfig vpc = new VelocityProducersConfig();
        populate(vpc, cfg.getModelDir());
        return vpc;
    }
}

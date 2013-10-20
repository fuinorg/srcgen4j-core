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

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates if all parameters defined in {@link TargetFile#getArguments()} are
 * also defined in {@link ParameterizedTemplate#getArguments()()}.
 */
public final class TargetArgsMatchTemplateArgsValidator implements
        ConstraintValidator<TargetArgsMatchTemplateArgs, ParameterizedTemplateModel> {

    private static final Logger LOG = LoggerFactory
            .getLogger(TargetArgsMatchTemplateArgsValidator.class);

    @Override
    public final void initialize(final TargetArgsMatchTemplateArgs constraintAnnotation) {
        // Not used
    }

    @Override
    public final boolean isValid(final ParameterizedTemplateModel value,
            final ConstraintValidatorContext context) {

        LOG.debug("Validate: " + value.getTemplate());

        if (value.getTargetFiles() == null) {
            LOG.debug("No target files defined...");
            return true;
        }

        final List<Argument> allowedArgs;
        if (value.getArguments() == null) {
            allowedArgs = new ArrayList<Argument>();
        } else {
            allowedArgs = value.getArguments();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Allowed arguments:");
            for (final Argument arg : allowedArgs) {
                LOG.debug(arg.toString());
            }
        }

        final List<TargetFile> targetFiles = value.getTargetFiles();
        for (final TargetFile targetFile : targetFiles) {
            final List<Argument> args = targetFile.getArguments();
            for (final Argument arg : args) {
                if (!allowedArgs.contains(arg)) {
                    LOG.debug("Not an allowed argument: '" + arg);
                    return false;
                }
            }
        }

        LOG.debug("Validated succesfully: " + value.getTemplate());
        return true;
    }

}

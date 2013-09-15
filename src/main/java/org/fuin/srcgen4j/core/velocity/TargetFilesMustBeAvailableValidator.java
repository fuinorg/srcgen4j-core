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

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;

import org.fuin.objects4j.common.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates if at least one of
 * {@link ParameterizedTemplate#getTargetFileListProducer()} and
 * {@link ParameterizedTemplate#getTargetFiles()} is available.
 */
public final class TargetFilesMustBeAvailableValidator implements
        ConstraintValidator<TargetFilesMustBeAvailable, ParameterizedTemplate> {

    private static final Logger LOG = LoggerFactory
            .getLogger(TargetFilesMustBeAvailableValidator.class);

    @Override
    public final void initialize(final TargetFilesMustBeAvailable constraintAnnotation) {
        // Not used
    }

    @Override
    public final boolean isValid(final ParameterizedTemplate value,
            final ConstraintValidatorContext context) {

        if (value.getTargetFileListProducerConfig() == null) {
            if ((value.getTargetFiles() == null || value.getTargetFiles().size() == 0)) {
                LOG.debug("Element 'target-files' is mandatory if no element "
                        + "'target-file-list-producer' is defined");
                return false;
            }
        } else {
            final Set<ConstraintViolation<TargetFileListProducerConfig>> violations = Contract
                    .validate(value.getTargetFileListProducerConfig());
            if (!violations.isEmpty()) {
                LOG.debug("Element 'target-file-list-producer' is invalid:");
                for (final ConstraintViolation<TargetFileListProducerConfig> violation : violations) {
                    LOG.error(violation.getMessage());
                }
                return false;
            }
            if (value.getTargetFiles() != null) {
                LOG.debug("Element 'target-files' cannot exist if element "
                        + "'target-file-list-producer' is defined");
                return false;
            }
        }

        return true;
    }

}

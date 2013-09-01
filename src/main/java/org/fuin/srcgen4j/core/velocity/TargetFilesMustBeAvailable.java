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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

//CHECKSTYLE:OFF
/**
 * Validates if {@link VelocityProducerConfig#getTargetFileListProducer()} or
 * {@link VelocityProducerConfig#getTargetFiles()} is set.
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { TargetFilesMustBeAvailableValidator.class })
@Documented
public @interface TargetFilesMustBeAvailable {

    String message() default "{org.fuin.srcgen4j.core.velocity.TargetFilesMustBeAvailable.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
// CHECKSTYLE:ON

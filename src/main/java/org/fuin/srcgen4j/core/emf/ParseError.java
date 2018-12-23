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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;

/**
 * Something went wrong while parsing the DSL.
 */
public final class ParseError {

    private final String source;
    
    private final EList<Diagnostic> errors;

    /**
     * Constructor with mandatory data.
     * 
     * @param source Source (file or URI).
     * @param errors List of errors.
     */
    public ParseError(final String source, final EList<Diagnostic> errors) {
        super();
        this.source = source;
        this.errors = errors;
    }

    /**
     * Returns the source where the error happened.
     * 
     * @return Source (file or URI).
     */
    public final String getSource() {
        return source;
    }

    /**
     * Returns the list of errors.
     * 
     * @return Error list.
     */
    public final EList<Diagnostic> getErrors() {
        return errors;
    }

    @Override
    public final String toString() {
        return "ParseError [source=" + source + ", errors=" + errors + "]";
    }

    
    
}

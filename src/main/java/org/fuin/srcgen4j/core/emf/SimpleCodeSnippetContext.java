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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple map based context.
 */
public final class SimpleCodeSnippetContext implements CodeSnippetContext {

    private final CodeReferenceRegistry refReg;

    private final Set<String> imports;

    /**
     * Constructor with registry.
     * 
     * @param refReg
     *            Reference registry.
     */
    public SimpleCodeSnippetContext(final CodeReferenceRegistry refReg) {
        super();
        this.imports = new HashSet<String>();
        this.refReg = refReg;
    }

    @Override
    public final void requiresImport(final String fqn) {
        imports.add(fqn);
    }

    @Override
    public final void requiresReference(final String uniqueName) {
        String resolved = refReg.getReference(uniqueName);
        if (resolved == null) {
            resolved = uniqueName;
        }
        imports.add(resolved);
    }

    @Override
    public final String getReference(final String uniqueName) {
        String resolved = refReg.getReference(uniqueName);
        if (resolved == null) {
            resolved = uniqueName;
        }
        return resolved;
    }

    /**
     * Returns a set of required import names.
     * 
     * @return Fully qualified names to import.
     */
    public final Set<String> getImports() {
        return Collections.unmodifiableSet(imports);
    }

}

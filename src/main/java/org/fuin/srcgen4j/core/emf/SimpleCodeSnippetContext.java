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
package org.fuin.srcgen4j.core.emf;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Simple map based context.
 */
public final class SimpleCodeSnippetContext implements CodeSnippetContext {

    private final Map<String, String> references;

    private final Set<String> imports;

    /**
     * Default constructor.
     */
    public SimpleCodeSnippetContext() {
        super();
        this.imports = new HashSet<String>();
        this.references = new HashMap<String, String>();
    }

    @Override
    public final void requiresImport(final String fqn) {
        imports.add(fqn);
    }

    @Override
    public final void requiresReference(final String uniqueName) {
        references.put(uniqueName, references.get(uniqueName));
    }
    
    @Override
    public final String getReference(final String uniqueName) {
        final String fqn = references.get(uniqueName);
        if (fqn == null) {
            return uniqueName;
        }
        return fqn;
    }

    /**
     * Returns a set of required import names.
     * 
     * @return Fully qualified names to import.
     */
    public final Set<String> getImports() {
        return Collections.unmodifiableSet(imports);
    }

    /**
     * Returns a set of required references.
     * 
     * @return Fully qualified names to import.
     */
    public final Set<String> getReferences() {
        return Collections.unmodifiableSet(references.keySet());
    }

    /**
     * Resolves the references and adds them to the import list.
     * 
     * @param refReg
     *            Registry to use for resolving the references.
     * 
     * @return TRUE if all references have been resolved. 
     */
    public final boolean resolve(final CodeReferenceRegistry refReg) {
        final Iterator<String> it = references.keySet().iterator();
        while (it.hasNext()) {
            final String key = it.next();
            String fqn = references.get(key);
            if (fqn == null) {
                fqn = refReg.getReference(key);
                if (fqn != null) {
                    references.put(key, fqn);
                    imports.add(fqn);
                }
            }
        }
        return references.isEmpty();
    }

}

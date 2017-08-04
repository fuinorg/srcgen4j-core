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

/**
 * Context used by the code fragments during the generation process.
 */
public interface CodeSnippetContext {

    /**
     * Tells the context that this code fragment requires the import of a given
     * artifact. This method is used if the fragment already "knows" the name to
     * import.
     * 
     * @param fqn
     *            Fully qualified name of the artifact to import.
     */
    public void requiresImport(String fqn);

    /**
     * Tells the context that this code fragment requires the import of a given
     * artifact. This method is used if the fragment only knows the unique name.
     * 
     * @param uniqueName
     *            Name that uniquely identifies an artifact.
     */
    public void requiresReference(String uniqueName);

    /**
     * Returns a fully qualified name based on the unique reference name.
     * 
     * @param uniqueName
     *            Name that uniquely identifies an artifact.
     * 
     * @return Fully qualified name of the artifact for use in the source code
     *         or the unique name if the reference was not resolved yet.
     */
    public String getReference(String uniqueName);

}

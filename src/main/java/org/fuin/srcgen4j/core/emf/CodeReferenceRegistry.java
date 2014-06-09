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

/**
 * Maps a unique name to it's fully qualified name.
 */
public interface CodeReferenceRegistry {

    /**
     * Returns a fully qualified name based on the unique reference name.
     * 
     * @param uniqueName
     *            Name that uniquely identifies an artifact.
     * 
     * @return Fully qualified name of the artifact for use in the source code.
     */
    public String getReference(String uniqueName);

    /**
     * Sets a fully qualified name for a unique reference name.
     * 
     * @param uniqueName
     *            Name that uniquely identifies an artifact.
     * @param fqn
     *            Fully qualified name of the artifact for use in the source
     *            code.
     */
    public void putReference(String uniqueName, String fqn);
    
}

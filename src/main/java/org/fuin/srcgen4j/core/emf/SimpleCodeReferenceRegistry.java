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

import java.util.HashMap;
import java.util.Map;

/**
 * Map based implementation of the code reference registry.
 */
public final class SimpleCodeReferenceRegistry implements CodeReferenceRegistry {

    private final Map<String, String> map;
    
    /**
     * Default constructor.
     */
    public SimpleCodeReferenceRegistry() {
        super();
        map = new HashMap<String, String>();
    }

    @Override
    public final String getReference(final String uniqueName) {
        return map.get(uniqueName);
    }

    @Override
    public final void putReference(final String uniqueName, final String fqn) {
        map.put(uniqueName, fqn);
    }

}

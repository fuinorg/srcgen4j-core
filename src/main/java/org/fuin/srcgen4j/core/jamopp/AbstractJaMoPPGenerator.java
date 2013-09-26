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
package org.fuin.srcgen4j.core.jamopp;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;
import org.fuin.srcgen4j.core.base.AbstractGenerator;

/**
 * Base class for concrete JaMoPP based generators.
 * 
 * @param <CONFIG>
 *            Type of the generator specific configuration - Use 'Object' if the
 *            generator does not require any configuration.
 */
public abstract class AbstractJaMoPPGenerator<CONFIG> extends
        AbstractGenerator<ResourceSet, CONFIG> {

    /**
     * Default constructor.
     */
    public AbstractJaMoPPGenerator() {
        super();
    }

    /**
     * Tries to find a concrete classifier in a resource set.
     * 
     * @param resourceSet
     *            Resource set to use.
     * @param fullQualifiedName
     *            Full qualified name of the class.
     * 
     * @return Concrete classifier or NULL if no concrete classifier with that
     *         name was found.
     */
    public static ConcreteClassifier find(final ResourceSet resourceSet,
            final String fullQualifiedName) {
        final TreeIterator<Notifier> it = resourceSet.getAllContents();
        while (it.hasNext()) {
            final Notifier notifier = it.next();
            if (notifier instanceof CompilationUnit) {
                final CompilationUnit compilationUnit = (CompilationUnit) notifier;
                final ConcreteClassifier cc = compilationUnit
                        .getConcreteClassifier(fullQualifiedName);
                if (cc != null) {
                    return cc;
                }
            }
        }
        return null;
    }

}

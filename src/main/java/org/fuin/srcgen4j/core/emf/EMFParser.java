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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.fuin.srcgen4j.core.base.SrcGen4JFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic functionality for parsing EMF Ecore models.
 */
public abstract class EMFParser {

    private static final Logger LOG = LoggerFactory.getLogger(EMFParser.class);

    private final ResourceSet resourceSet = new ResourceSetImpl();

    private List<SrcGen4JFile> modelDirs;

    private List<String> fileExtensions;

    /**
     * Default constructor.
     */
    public EMFParser() {
        super();
    }

    /**
     * Constructor with array.
     * 
     * @param modelDirs
     *            Directory with the Ecore model files.
     * @param fileExtensions
     *            List of extensions for files to find ("mymodel", "java",
     *            "class", ...)
     */
    public EMFParser(final List<SrcGen4JFile> modelDirs, final String... fileExtensions) {
        this(modelDirs, Arrays.asList(fileExtensions));
    }

    /**
     * Constructor with list.
     * 
     * @param modelDirs
     *            Directory with the Ecore model files.
     * @param fileExtensions
     *            List of extensions for files to find ("mymodel", "java",
     *            "class", ...)
     */
    public EMFParser(final List<SrcGen4JFile> modelDirs, final List<String> fileExtensions) {
        super();
        this.modelDirs = modelDirs;
        this.fileExtensions = fileExtensions;
    }

    /**
     * Parses all model file in the directory. y
     */
    protected final void parseModelFiles() {

        if ((fileExtensions == null) || (fileExtensions.size() == 0)) {
            throw new IllegalStateException("No file extensions for EMF model files set!");
        }

        // Start parsing recursively
        if ((modelDirs == null) || (modelDirs.size() == 0)) {
            LOG.debug("No model directories to parse");
        } else {
            for (final SrcGen4JFile modelDir : modelDirs) {
                parseDir(modelDir.toFile());
            }
        }

    }

    /**
     * Tries to resolve all proxies.
     */
    protected final void resolveProxies() {
        final List<String> unresolved = new ArrayList<String>();
        if (!resolvedAllProxies(unresolved, 0)) {
            LOG.warn("Could not resolve the following proxies (" + unresolved.size() + "):");
            for (final String ref : unresolved) {
                LOG.warn("Not found: " + ref);
            }
            final Iterator<Notifier> it = resourceSet.getAllContents();
            while (it.hasNext()) {
                final Notifier next = it.next();
                if (next instanceof EObject) {
                    final EObject obj = (EObject) next;
                    if (obj.eIsProxy()) {
                        try {
                            it.remove();
                        } catch (final UnsupportedOperationException ex) {
                            LOG.error("Could not remove proxy: " + obj, ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns files that end with java and all directories. Files and
     * directories started with a "." are excluded.
     * 
     * @param dir
     *            Directory to scan.
     * 
     * @return List of files in the directory.
     */
    private File[] getFiles(final File dir) {
        final File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                final boolean pointFile = file.getName().startsWith(".");
                final String extension = FilenameUtils.getExtension(file.getName());
                return (!pointFile && fileExtensions.contains(extension)) || file.isDirectory();
            }
        });
        return files;
    }

    /**
     * Parse the directory and it's sub directory.
     * 
     * @param dir
     *            Directory to parse.
     */
    private void parseDir(final File dir) {
        LOG.debug("Parse: " + dir);
        final File[] files = getFiles(dir);
        if ((files == null) || (files.length == 0)) {
            LOG.debug("No files found in directory: " + dir);
        } else {
            for (final File file : files) {
                if (file.isFile()) {
                    final Resource resource = resourceSet.getResource(
                            URI.createFileURI(getCanonicalPath(file)), true);
                    LOG.debug("Parsed: " + resource.getURI());
                } else {
                    parseDir(file);
                }
            }
        }
    }

    private String getCanonicalPath(final File file) {
        try {
            return file.getCanonicalPath();
        } catch (final IOException ex) {
            throw new RuntimeException("Error getting canonical path: " + file, ex);
        }
    }

    private boolean resolvedAllProxies(final List<String> unresolved,
            final int resourcesProcessedBefore) {

        final List<EObject> eObjects = findAllEObjects(resourceSet);

        // Check if we discovered any new resources
        final int totalResources = resourceSet.getResources().size();
        final boolean discoveredNewResources = (totalResources != resourcesProcessedBefore);
        if (!discoveredNewResources) {
            return true;
        }

        // Resolve references
        boolean failure = false;
        int resolved = 0;
        final int count = eObjects.size();
        for (int i = 0; i < count; i++) {
            final EObject eObj = eObjects.get(i);
            if (eObj instanceof InternalEObject) {
                final InternalEObject iObj = (InternalEObject) eObj;
                for (final EObject crossRef : iObj.eCrossReferences()) {
                    final EObject resolvedRef = EcoreUtil.resolve(crossRef, resourceSet);
                    final String resolvedRefStr = getStrRef(resolvedRef);
                    if (resolvedRef.eIsProxy()) {
                        failure = true;
                        if (!unresolved.contains(resolvedRefStr)) {
                            unresolved.add(resolvedRefStr);
                        }
                    } else {
                        resolved++;
                        unresolved.remove(resolvedRefStr);
                    }
                }
                if ((i % 1000) == 0) {
                    LOG.debug("Finished " + i + " of " + count + " references");
                }
            }
        }
        LOG.debug("Cross references - Resolved: " + resolved + ", Unresolved: " + unresolved.size());
        return !failure && resolvedAllProxies(unresolved, totalResources);
    }

    private static String getStrRef(final EObject resolvedRef) {
        if (resolvedRef instanceof InternalEObject) {
            final URI uri = ((InternalEObject) resolvedRef).eProxyURI();
            if (uri != null) {
                return uri.toString();
            }
            return resolvedRef.toString();
        }
        return resolvedRef.toString();
    }

    /**
     * Returns a list of all objects.
     * 
     * @return List.
     */
    private static List<EObject> findAllEObjects(final ResourceSet resourceSet) {
        final List<EObject> list = new LinkedList<EObject>();
        for (final Iterator<Notifier> i = resourceSet.getAllContents(); i.hasNext();) {
            final Notifier next = i.next();
            if (next instanceof EObject) {
                list.add((EObject) next);
            }
        }
        return list;
    }

    /**
     * Returns the model directories.
     * 
     * @return List of directories.
     */
    protected final List<SrcGen4JFile> getModelDirs() {
        return modelDirs;
    }

    /**
     * Returns the list of file extensions.
     * 
     * @return List of extensions for files to find ("mymodel", "java", "class",
     *         ...)
     */
    protected final List<String> getFileExtensions() {
        return fileExtensions;
    }

    /**
     * Returns the resource set.
     * 
     * @return Resource set.
     */
    protected final ResourceSet getResourceSet() {
        return resourceSet;
    }

    /**
     * Sets the model directories to parse.
     * 
     * @param modelDirs
     *            List of model directories or NULL.
     */
    protected final void setModelDirs(final List<SrcGen4JFile> modelDirs) {
        this.modelDirs = modelDirs;
    }

    /**
     * Sets the list of file extensions.
     * 
     * @param fileExtensions
     *            List of extensions for files to find ("mymodel", "java",
     *            "class", ...)
     */
    protected final void setFileExtensions(final List<String> fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    /**
     * Sets the list of file extensions. If no list exists internally, it will
     * be created if necessary.
     * 
     * @param fileExtensions
     *            Array of extensions for files to find ("mymodel", "java",
     *            "class", ...)
     */
    protected final void setFileExtensions(final String... fileExtensions) {
        if (fileExtensions == null) {
            this.fileExtensions = null;
        } else {
            this.fileExtensions = new ArrayList<String>();
            this.fileExtensions.addAll(Arrays.asList(fileExtensions));
        }
    }

}

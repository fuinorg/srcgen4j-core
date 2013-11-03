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

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.JavaPackage;
import org.emftext.language.java.resource.JavaSourceOrClassFileResourceFactoryImpl;
import org.fuin.srcgen4j.commons.ParseException;
import org.fuin.srcgen4j.commons.Parser;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.srcgen4j.core.base.SrcGen4JFile;
import org.fuin.srcgen4j.core.emf.EMFParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JaMoPP based parser for Java source/class files and JARs.
 */
public final class JaMoPPParser extends EMFParser<JaMoPPParserConfig> implements
        Parser<ResourceSet> {

    private static final Logger LOG = LoggerFactory.getLogger(JaMoPPParser.class);

    private List<SrcGen4JFile> jarFiles;

    private List<SrcGen4JFile> binDirs;

    private JaMoPPParserConfig parserConfig;

    /**
     * Default constructor.
     */
    public JaMoPPParser() {
        super(JaMoPPParserConfig.class);
    }

    @Override
    public final void initialize(final SrcGen4JContext context, final ParserConfig config) {
        this.parserConfig = getConcreteConfig(config);

        jarFiles = parserConfig.getJarFiles();
        binDirs = parserConfig.getBinDirs();
        setModelDirs(parserConfig.getSrcDirs());
        setFileExtensions("java", "class");

        // Initialize JaMoPP
        if (!EPackage.Registry.INSTANCE.containsKey("http://www.emftext.org/java")) {
            EPackage.Registry.INSTANCE.put("http://www.emftext.org/java", JavaPackage.eINSTANCE);
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("java",
                    new JavaSourceOrClassFileResourceFactoryImpl());
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("class",
                    new JavaSourceOrClassFileResourceFactoryImpl());
        }

    }

    @Override
    public final ResourceSet parse() throws ParseException {

        LOG.debug("Initialize JaMoPP");

        final JavaClasspath cp = JavaClasspath.get(getResourceSet());

        // Add JARs to class path
        if ((jarFiles == null) || (jarFiles.size() == 0)) {
            LOG.debug("No JAR files to parse");
        } else {
            for (final SrcGen4JFile jarFile : jarFiles) {
                if (jarFile.exists()) {
                    final URI uri = URI.createFileURI(jarFile.getCanonicalPath());
                    LOG.debug("Register classifier JAR: " + uri);
                    cp.registerClassifierJar(uri);
                } else {
                    LOG.error("Configured JAR file does not exist: " + jarFile);
                }
            }
        }

        // Add directories to class path
        if ((binDirs == null) || (binDirs.size() == 0)) {
            LOG.debug("No binary directories to parse");
        } else {
            for (final SrcGen4JFile binDir : binDirs) {
                if (binDir.exists()) {
                    final URI uri = URI.createFileURI(binDir.getCanonicalPath());
                    LOG.debug("Register source or class file folder: " + uri);
                    cp.registerSourceOrClassFileFolder(uri);
                } else {
                    LOG.error("Configured binary directory does not exist: " + binDir);
                }
            }
        }

        parseModelFiles();

        resolveProxies();

        return getResourceSet();

    }

}

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.srcgen4j.commons.AbstractElement;
import org.fuin.srcgen4j.commons.Config;
import org.fuin.srcgen4j.commons.GeneratorConfig;
import org.fuin.srcgen4j.commons.InitializableElement;

/**
 * Configuration for a {@link JaMoPPParser}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "jamopp-parser")
public class JaMoPPParserConfig extends AbstractElement implements
        InitializableElement<JaMoPPParserConfig, Config<GeneratorConfig>> {

    @XmlElementWrapper(name = "jar-files")
    @XmlElement(name = "jar-file")
    private List<String> jarFileNames;

    @XmlElementWrapper(name = "bin-dirs")
    @XmlElement(name = "bin-dir")
    private List<String> binDirNames;

    @XmlElementWrapper(name = "src-dirs")
    @XmlElement(name = "src-dir")
    private List<String> srcDirNames;

    private transient List<File> jarFiles;
    private transient List<File> binDirs;
    private transient List<File> srcDirs;

    /**
     * Default constructor.
     */
    public JaMoPPParserConfig() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param jarFiles
     *            List of JAR files to parse or NULL.
     * @param binDirs
     *            List of binary ("classes") directories to parse or NULL.
     * @param srcDirs
     *            List of source directories to parse or NULL.
     */
    public JaMoPPParserConfig(final List<String> jarFiles, final List<String> binDirs,
            final List<String> srcDirs) {
        super();
        this.jarFileNames = jarFiles;
        this.binDirNames = binDirs;
        this.srcDirNames = srcDirs;
    }

    @Override
    public final JaMoPPParserConfig init(final Config<GeneratorConfig> parent,
            final Map<String, String> vars) {

        for (final String jarFile : jarFileNames) {
            replaceVars(jarFile, vars);
        }
        for (final String binDir : binDirNames) {
            replaceVars(binDir, vars);
        }
        for (final String srcDir : srcDirNames) {
            replaceVars(srcDir, vars);
        }

        return this;
    }

    /**
     * Returns the list of JAR file names.
     * 
     * @return JAR files to parse or NULL.
     */
    public final List<String> getJarFileNames() {
        return jarFileNames;
    }

    /**
     * Returns the list of binary directory names to parse.
     * 
     * @return Directories with '.class' files or NULL.
     */
    public final List<String> getBinDirNames() {
        return binDirNames;
    }

    /**
     * Returns the list of source folder names to parse.
     * 
     * @return Directories with '*.java' files or NULL.
     */
    public final List<String> getSrcDirNames() {
        return srcDirNames;
    }

    /**
     * Returns the list of JAR files.
     * 
     * @return JAR files to parse or NULL.
     */
    public final List<File> getJarFiles() {
        if (jarFiles == null) {
            jarFiles = new ArrayList<File>();
            for (final String name : jarFileNames) {
                jarFiles.add(new File(name));
            }
        }
        return jarFiles;
    }

    /**
     * Returns the list of binary directories to parse.
     * 
     * @return Directories with '.class' files or NULL.
     */
    public final List<File> getBinDirs() {
        if (binDirs == null) {
            binDirs = new ArrayList<File>();
            for (final String name : binDirNames) {
                binDirs.add(new File(name));
            }
        }
        return binDirs;
    }

    /**
     * Returns the list of source folders to parse.
     * 
     * @return Directories with '*.java' files or NULL.
     */
    public final List<File> getSrcDirs() {
        if (srcDirs == null) {
            srcDirs = new ArrayList<File>();
            for (final String name : srcDirNames) {
                srcDirs.add(new File(name));
            }
        }
        return srcDirs;
    }

}

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

import java.util.Collections;
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
import org.fuin.srcgen4j.commons.SrcGen4JContext;
import org.fuin.srcgen4j.core.base.SrcGen4JFile;

/**
 * Configuration for a {@link JaMoPPParser}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "jamopp-parser")
public class JaMoPPParserConfig extends AbstractElement implements
        InitializableElement<JaMoPPParserConfig, Config<GeneratorConfig>> {

    @XmlElementWrapper(name = "jar-files")
    @XmlElement(name = "file", namespace = "http://www.fuin.org/srcgen4j/core/base")
    private List<SrcGen4JFile> jarFiles;

    @XmlElementWrapper(name = "bin-dirs")
    @XmlElement(name = "file", namespace = "http://www.fuin.org/srcgen4j/core/base")
    private List<SrcGen4JFile> binDirs;

    @XmlElementWrapper(name = "src-dirs")
    @XmlElement(name = "file", namespace = "http://www.fuin.org/srcgen4j/core/base")
    private List<SrcGen4JFile> srcDirs;

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
    public JaMoPPParserConfig(final List<SrcGen4JFile> jarFiles,
            final List<SrcGen4JFile> binDirs, final List<SrcGen4JFile> srcDirs) {
        super();
        this.jarFiles = jarFiles;
        this.binDirs = binDirs;
        this.srcDirs = srcDirs;
    }

    @Override
    public final JaMoPPParserConfig init(final SrcGen4JContext context,
            final Config<GeneratorConfig> parent, final Map<String, String> vars) {

        for (final SrcGen4JFile jarFile : jarFiles) {
            jarFile.replaceVars(vars);
        }
        for (final SrcGen4JFile binDir : binDirs) {
            binDir.replaceVars(vars);
        }
        for (final SrcGen4JFile srcDir : srcDirs) {
            srcDir.replaceVars(vars);
        }

        return this;
    }

    /**
     * Returns an unmodifiable list of JAR files.
     * 
     * @return JAR files to parse or NULL.
     */
    public final List<SrcGen4JFile> getJarFiles() {
        if (jarFiles == null) {
            return null;
        }
        return Collections.unmodifiableList(jarFiles);
    }

    /**
     * Returns an unmodifiable list of binary directories to parse.
     * 
     * @return Directories with '.class' files or NULL.
     */
    public final List<SrcGen4JFile> getBinDirs() {
        if (binDirs == null) {
            return null;
        }
        return Collections.unmodifiableList(binDirs);
    }

    /**
     * Returns an unmodifiable list of source folders to parse.
     * 
     * @return Directories with '*.java' files or NULL.
     */
    public final List<SrcGen4JFile> getSrcDirs() {
        if (srcDirs == null) {
            return null;
        }
        return Collections.unmodifiableList(srcDirs);
    }

}

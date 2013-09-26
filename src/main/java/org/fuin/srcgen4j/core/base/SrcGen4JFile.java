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
package org.fuin.srcgen4j.core.base;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.srcgen4j.commons.VariableResolver;

/**
 * Represents a file.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "file")
@XmlType(propOrder = { "name", "path" })
public final class SrcGen4JFile {

    @XmlAttribute(name = "path")
    private String path;

    @XmlAttribute(name = "name")
    private String name;

    private transient File file;

    /**
     * Default constructor.
     */
    protected SrcGen4JFile() {
        super();
    }

    /**
     * Constructor with directory and name.
     * 
     * @param dir
     *            Directory - Cannot be NULL.
     * @param name
     *            Name - May be NULL.
     */
    public SrcGen4JFile(final File dir, final String name) {
        super();
        this.file = null;
        this.path = dir.toString();
        this.name = name;
    }

    /**
     * Constructor for a file (directory and file name) or a directory
     * (directory only).
     * 
     * @param dir
     *            Directory - Cannot be NULL.
     * @param name
     *            Name - May be NULL.
     */
    public SrcGen4JFile(final SrcGen4JFile dir, final String name) {
        super();
        this.file = null;
        this.path = dir.getPath();
        this.name = name;
    }

    /**
     * Constructor for a directory.
     * 
     * @param path
     *            Directory's path - Cannot be NULL.
     */
    public SrcGen4JFile(final String path) {
        super();
        this.path = path;
    }

    /**
     * Constructor for a file (or a directory).
     * 
     * @param path
     *            Directory's path (without file name) - Cannot be NULL.
     * @param name
     *            File name (without path) - May be NULL.
     */
    public SrcGen4JFile(final String path, final String name) {
        super();
        this.path = path;
        this.name = name;
    }

    /**
     * Returns the path.
     * 
     * @return Directory's path (without file name).
     */
    public final String getPath() {
        return path;
    }

    /**
     * Returns the file name.
     * 
     * @return File name (without path).
     */
    public final String getName() {
        return name;
    }

    @Override
    public final String toString() {
        if (name == null) {
            return path;
        }
        return path + "/" + name;
    }

    /**
     * Replaces all variables inside path or file name with values from a map.
     * 
     * @param vars
     *            Map with key/values (both of type <code>String</code> - May be
     *            <code>null</code>.
     */
    public final void replaceVars(final Map<String, String> vars) {
        path = VariableResolver.replaceVars(path, vars);
        if (name != null) {
            name = VariableResolver.replaceVars(name, vars);
        }
    }

    /**
     * Creates a java file from the object or returns a cached instance.
     * 
     * @return File.
     */
    public final File toFile() {
        if (file == null) {
            if (name == null) {
                file = new File(path.replace('/', File.separatorChar));
            } else {
                file = new File((path + "/" + name).replace('/', File.separatorChar));
            }
        }
        return file;
    }

    /**
     * Tests whether the file or directory denoted by this abstract pathname
     * exists.
     * 
     * @return true if and only if the file or directory denoted by this
     *         abstract pathname exists; false otherwise
     * 
     */
    public final boolean exists() {
        return toFile().exists();
    }

    /**
     * Returns the canonical pathname string.
     * 
     * @return Canonical path.
     */
    public final String getCanonicalPath() {
        try {
            return toFile().getCanonicalPath();
        } catch (final IOException ex) {
            throw new RuntimeException("Error getting canonical path: " + toFile(), ex);
        }
    }

}

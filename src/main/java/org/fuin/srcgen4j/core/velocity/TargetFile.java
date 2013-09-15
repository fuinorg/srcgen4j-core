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
package org.fuin.srcgen4j.core.velocity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.TrimmedNotEmpty;

/**
 * File to produce.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "target-file")
public final class TargetFile implements Serializable, Comparable<TargetFile> {

    private static final long serialVersionUID = 1L;

    @TrimmedNotEmpty
    @XmlAttribute
    private String path;

    @TrimmedNotEmpty
    @XmlAttribute
    private String name;

    @Valid
    @XmlElement(name = "argument")
    private List<Argument> arguments;

    /**
     * Default constructor for deserialization.
     */
    TargetFile() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param path
     *            Path without filename or NULL.
     * @param name
     *            Name without path - Cannot be NULL.
     * @param args
     *            Arguments for the template or NULL.
     */
    public TargetFile(final String path, final String name, final Argument... args) {
        super();
        Contract.requireArgNotNull("name", name);
        this.path = path;
        this.name = name;

        if (args != null) {
            this.arguments = new ArrayList<Argument>();
            for (final Argument arg : args) {
                this.arguments.add(arg);
            }
        }

    }

    /**
     * Returns the relative path of the target file.
     * 
     * @return Path or NULL.
     */
    public final String getPath() {
        return path;
    }

    /**
     * Returns the name of the target file.
     * 
     * @return Name without path - Never NULL.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns path and name.
     * 
     * @return Relative path and name.
     */
    public final String getPathAndName() {
        if (path == null) {
            return name;
        }
        return path + "/" + name;
    }

    /**
     * Returns a list of arguments to use.
     * 
     * @return Arguments for the template or NULL.
     */
    public final List<Argument> getArguments() {
        return arguments;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((path == null) ? 0 : path.hashCode());
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TargetFile other = (TargetFile) obj;
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    // CHECKSTYLE:ON

    @Override
    public final int compareTo(final TargetFile other) {
        return getPathAndName().compareTo(other.getPathAndName());
    }

}

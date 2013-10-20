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
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.vo.TrimmedNotEmpty;
import org.fuin.srcgen4j.commons.VariableResolver;

/**
 * Container for a key and a value combination.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "value", "key" })
@XmlRootElement(name = "argument")
public final class Argument implements Serializable, Comparable<Argument> {

    private static final long serialVersionUID = 1L;

    @TrimmedNotEmpty
    @XmlAttribute
    private String key;

    @TrimmedNotEmpty
    @XmlAttribute
    private String value;

    /**
     * Default constructor.
     */
    public Argument() {
        super();
    }

    /**
     * Constructor with key and value.
     * 
     * @param key
     *            Key - Cannot be NULL.
     * @param value
     *            Value - Cannot be NULL.
     */
    public Argument(final String key, final String value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key.
     * 
     * @return Key - Never NULL.
     */
    public final String getKey() {
        return key;
    }

    /**
     * Returns the value.
     * 
     * @return Value - Never NULL.
     */
    public final String getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        return key.hashCode();
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
        final Argument other = (Argument) obj;
        return key.equals(other.key);
    }

    @Override
    public final int compareTo(final Argument other) {
        return key.compareTo(other.key);
    }

    @Override
    public final String toString() {
        return key + "='" + value + "'";
    }

    /**
     * Replaces variables (if defined) in the value.
     * 
     * @param vars
     *            Variables to use.
     */
    public final void init(final Map<String, String> vars) {
        value = VariableResolver.replaceVars(getValue(), vars);
    }

}

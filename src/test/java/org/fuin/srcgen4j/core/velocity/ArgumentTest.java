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

import static org.fest.assertions.Assertions.assertThat;

import javax.xml.bind.JAXBContext;

import org.fuin.srcgen4j.commons.JaxbHelper;
import org.junit.Test;

// CHECKSTYLE:OFF
public class ArgumentTest {

    @Test
    public void testMarshalUnmarshal() throws Exception {

        // PREPARE
        final Argument testee = new Argument("key", "value");
        final JAXBContext jaxbContext = JAXBContext.newInstance(ParameterizedTemplate.class,
                TargetFile.class, Argument.class);
        final JaxbHelper helper = new JaxbHelper();

        // EXECUTE
        final String xml = helper.write(testee, jaxbContext);
        final Argument copy = helper.create(xml, jaxbContext);

        // VERIFY
        assertThat(copy.getKey()).isEqualTo(testee.getKey());
        assertThat(copy.getValue()).isEqualTo(testee.getValue());

    }

}
// CHECKSTYLE:ON

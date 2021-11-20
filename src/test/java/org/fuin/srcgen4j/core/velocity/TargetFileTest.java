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
package org.fuin.srcgen4j.core.velocity;

import static org.assertj.core.api.Assertions.assertThat;

import javax.xml.bind.JAXBContext;

import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.utils4j.jaxb.JaxbUtils;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.jupiter.api.Test;

// CHECKSTYLE:OFF
public class TargetFileTest {

    @Test
    public void testMarshalUnmarshal() throws Exception {

        // PREPARE
        final Argument arg1 = new Argument("key1", "value1");
        final Argument arg2 = new Argument("key2", "value2");
        final String path = "path";
        final String name = "name";
        final TargetFile testee = new TargetFile(path, name, arg1, arg2);
        final JAXBContext jaxbContext = JAXBContext.newInstance(TargetFile.class, Argument.class);
        final JaxbHelper helper = new JaxbHelper();

        // EXECUTE
        final String xml = helper.write(testee, jaxbContext);
        final TargetFile copy = JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext).build(), xml);

        // VERIFY
        assertThat(copy.getPath()).isEqualTo(testee.getPath());
        assertThat(copy.getName()).isEqualTo(testee.getName());
        assertThat(copy.getArguments().size()).isEqualTo(2);
        assertThat(copy.getArguments()).contains(arg1, arg2);

    }

}
// CHECKSTYLE:ON

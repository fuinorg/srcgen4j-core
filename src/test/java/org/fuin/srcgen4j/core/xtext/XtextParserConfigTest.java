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
package org.fuin.srcgen4j.core.xtext;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link XtextParserConfig}.
 */
class XtextParserConfigTest {

    // CHECKSTYLE:OFF

    @Test
    void testIsFile() throws Exception {
        assertThat(XtextParserConfig.isFile("src/main/resources")).isTrue();
        assertThat(XtextParserConfig.isFile("classpath:/src/main/resources")).isFalse();
    }

    @Test
    void testAsFile() throws Exception {
        assertThat(XtextParserConfig.asFile("src/main/resources")).isEqualTo(new File("src/main/resources").getAbsoluteFile());
    }

    @Test
    void testIsResource() throws Exception {
        assertThat(XtextParserConfig.isResource("src/main/resources")).isFalse();
        assertThat(XtextParserConfig.isResource("classpath:/src/main/resources")).isTrue();
    }

    @Test
    void testAsResource() throws Exception {
        final String str = "classpath:/src/main/resources";
        final URI uri = XtextParserConfig.asResource(str);
        assertThat(uri).isEqualTo(URI.createURI(str));
    }

    // CHECKSTYLE:ON

}

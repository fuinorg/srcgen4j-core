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

import javax.xml.bind.JAXBContext;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.linking.lazy.LazyLinkingResource;
import org.fuin.srcgen4j.commons.DefaultContext;
import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JConfig;
import org.fuin.srcgen4j.core.emf.EMFGeneratorConfig;
import org.fuin.utils4j.Utils4J;
import org.fuin.xsample.xSampleDsl.Greeting;
import org.fuin.xsample.xSampleDsl.impl.GreetingImpl;
import org.fuin.xsample.xSampleDsl.impl.ModelImpl;
import org.junit.Test;

/**
 * Test for {@link XtextParserConfig}.
 */
public class XtextParserConfigTest {

    // CHECKSTYLE:OFF

    @Test
    public void testIsFile() throws Exception {
        assertThat(XtextParserConfig.isFile("src/main/resources")).isTrue();
        assertThat(XtextParserConfig.isFile("classpath:/src/main/resources")).isFalse();
    }

    @Test
    public void testAsFile() throws Exception {
        assertThat(XtextParserConfig.asFile("src/main/resources")).isEqualTo(new File("src/main/resources").getAbsoluteFile());
    }

    @Test
    public void testIsResource() throws Exception {
        assertThat(XtextParserConfig.isResource("src/main/resources")).isFalse();
        assertThat(XtextParserConfig.isResource("classpath:/src/main/resources")).isTrue();
    }

    @Test
    public void testAsResource() throws Exception {
        final String str = "classpath:/src/main/resources";
        final URI uri = XtextParserConfig.asResource(str);
        assertThat(uri).isEqualTo(URI.createURI(str));
    }
    
    // CHECKSTYLE:ON

}

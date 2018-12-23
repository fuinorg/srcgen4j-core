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
package org.fuin.srcgen4j.core.emf;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import javax.xml.bind.JAXBContext;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.fuin.srcgen4j.commons.DefaultContext;
import org.fuin.srcgen4j.commons.GeneratorConfig;
import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JConfig;
import org.fuin.srcgen4j.core.xtext.XtextParser;
import org.fuin.srcgen4j.core.xtext.XtextParserConfig;
import org.fuin.utils4j.classpath.Handler;
import org.junit.Test;

/**
 * Test for {@link XtextParser}.
 */
public class EMFGeneratorTest {

    // CHECKSTYLE:OFF

    @Test
    public void testParse() throws Exception {

        Handler.add();
        
        final DefaultContext context = new DefaultContext();
        final File dir = new File("src/test/resources");
        final File file = new File(dir, "xtext-test-config.xml");

        final JAXBContext jaxbContext = JAXBContext.newInstance(SrcGen4JConfig.class, XtextParserConfig.class, EMFGeneratorConfig.class);
        final SrcGen4JConfig srcGen4JConfig = new JaxbHelper().create(file, jaxbContext);
        srcGen4JConfig.init(context, new File("."));
        final GeneratorConfig generatorConfig = srcGen4JConfig.getGenerators().findByName("gen1");
        final ParserConfig parserConfig = srcGen4JConfig.getParsers().getList().get(0);

        final XtextParser parser = new XtextParser();
        parser.initialize(context, parserConfig);
        final ResourceSet resourceSet = parser.parse();

        final EMFGenerator testee = new EMFGenerator();
        testee.initialize(generatorConfig);

        // TEST
        testee.generate(resourceSet, false);

        // VERIFY
        
        assertThat(new File("target/xtest-test/a/b/c/AbstractHelloUniverse.java"))
                .hasSameContentAs(new File("src/test/resources/AbstractHelloUniverse.java"));
        assertThat(new File("target/xtest-test/a/b/c/HelloUniverse.java"))
                .hasSameContentAs(new File("src/test/resources/HelloUniverse.java"));

        assertThat(new File("target/xtest-test/a/b/c/AbstractHelloWorld.java"))
                .hasSameContentAs(new File("src/test/resources/AbstractHelloWorld.java"));
        assertThat(new File("target/xtest-test/a/b/c/HelloWorld.java")).hasSameContentAs(new File("src/test/resources/HelloWorld.java"));

        assertThat(new File("target/xtest-test/a/b/c/AbstractHelloResource.java"))
                .hasSameContentAs(new File("src/test/resources/AbstractHelloResource.java"));
        assertThat(new File("target/xtest-test/a/b/c/HelloResource.java"))
                .hasSameContentAs(new File("src/test/resources/HelloResource.java"));

    }
    // CHECKSTYLE:ON

}

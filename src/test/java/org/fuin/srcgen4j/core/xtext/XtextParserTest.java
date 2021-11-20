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
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.linking.lazy.LazyLinkingResource;
import org.fuin.srcgen4j.commons.DefaultContext;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JConfig;
import org.fuin.srcgen4j.core.emf.EMFGeneratorConfig;
import org.fuin.utils4j.classpath.Handler;
import org.fuin.utils4j.jaxb.JaxbUtils;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.fuin.xsample.xSampleDsl.Greeting;
import org.fuin.xsample.xSampleDsl.impl.GreetingImpl;
import org.fuin.xsample.xSampleDsl.impl.ModelImpl;
import org.junit.Test;

/**
 * Test for {@link XtextParser}.
 */
public class XtextParserTest {

    // CHECKSTYLE:OFF

    @Test
    public void testParse() throws Exception {

        Handler.add();

        final DefaultContext context = new DefaultContext();
        final File dir = new File("src/test/resources/domain");
        final File file = new File(dir, "xtext-test-config.xml");
        final JAXBContext jaxbContext = JAXBContext.newInstance(SrcGen4JConfig.class, XtextParserConfig.class, EMFGeneratorConfig.class);
        final SrcGen4JConfig srcGen4JConfig = JaxbUtils
                .unmarshal(
                        new UnmarshallerBuilder()
                                .withContext(jaxbContext).addClasspathSchemas("/srcgen4j-commons-0_4_3.xsd",
                                        "/srcgen4j-core-base-0_4_3.xsd", "/srcgen4j-core-emf-0_4_3.xsd", "/srcgen4j-core-xtext-0_4_3.xsd")
                                .build(),
                        file);
        srcGen4JConfig.init(context, new File("."));
        final ParserConfig config = srcGen4JConfig.getParsers().getList().get(0);

        final XtextParser testee = new XtextParser();
        testee.initialize(context, config);

        // TEST
        final ResourceSet resourceSet = testee.parse();

        // VERIFY
        assertThat(resourceSet).isNotNull();

        final TreeIterator<Notifier> it = resourceSet.getAllContents();
        assertThat(it.next()).isInstanceOf(LazyLinkingResource.class);
        assertThat(it.next()).isInstanceOf(ModelImpl.class);
        final Notifier notifier = it.next();
        assertThat(notifier).isInstanceOf(GreetingImpl.class);
        final Greeting greeting = (Greeting) notifier;
        assertThat(greeting.getName()).isEqualTo("World");

    }

    // CHECKSTYLE:ON

}

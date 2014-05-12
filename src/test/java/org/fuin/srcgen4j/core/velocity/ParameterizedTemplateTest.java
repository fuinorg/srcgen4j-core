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
// CHECKSTYLE:OFF
package org.fuin.srcgen4j.core.velocity;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.fuin.srcgen4j.commons.JaxbHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParameterizedTemplateTest {

    private static TargetFile targetFile1;

    private static Argument arg1;

    private static Argument arg2;

    @BeforeClass
    public static void setUp() {
        final String path = "path";
        final String name = "name";
        arg1 = new Argument("key1", "value1");
        arg2 = new Argument("key2", "value2");
        targetFile1 = new TargetFile(path, name, arg1, arg2);
    }

    @AfterClass
    public static void tearDown() {
        arg1 = null;
        arg2 = null;
        targetFile1 = null;
    }

    @Test
    public void testMarshalUnmarshalList() throws Exception {

        // PREPARE
        final String template = "my-template";
        final ParameterizedTemplateModel testee = new ParameterizedTemplateModel(
                template, targetFile1);
        testee.addArgument(new Argument(arg1.getKey(), "ALLOWED_1"));
        testee.addArgument(new Argument(arg2.getKey(), "ALLOWED_2"));
        final JAXBContext jaxbContext = JAXBContext.newInstance(
                ParameterizedTemplateModel.class, TargetFile.class,
                Argument.class);
        final JaxbHelper helper = new JaxbHelper();

        // EXECUTE
        final String xml = helper.write(testee, jaxbContext);
        System.out.println(xml);
        final ParameterizedTemplateModel copy = helper.create(xml, jaxbContext);

        // VERIFY
        assertThat(copy.getTemplate()).isEqualTo(template);
        assertThat(copy.getTargetFiles().size()).isEqualTo(1);
        assertThat(copy.getTargetFiles()).contains(targetFile1);
        assertThat(copy.getTargetFileListProducerConfig()).isNull();
        final List<TargetFile> targetFiles = copy.createTargetFileList();
        assertThat(targetFiles.size()).isEqualTo(1);
        assertThat(targetFiles).contains(targetFile1);

    }

    @Test
    public void testMarshalUnmarshalProducer() throws Exception {

        // PREPARE

        final String template = "my-template";
        final TargetFileListProducerConfig tflProducerCfg = new TargetFileListProducerConfig();
        tflProducerCfg.setClassName(TestTargetFileProducer.class.getName());
        final ParameterizedTemplateModel testee = new ParameterizedTemplateModel(
                template, tflProducerCfg);
        final JAXBContext jaxbContext = JAXBContext.newInstance(
                ParameterizedTemplateModel.class, TargetFile.class,
                Argument.class);
        final JaxbHelper helper = new JaxbHelper();

        // EXECUTE
        final String xml = helper.write(testee, jaxbContext);
        System.out.println(xml);
        final ParameterizedTemplateModel copy = helper.create(xml, jaxbContext);

        // VERIFY
        assertThat(copy.getTemplate()).isEqualTo(template);
        assertThat(copy.getTargetFiles()).isNull();
        assertThat(copy.getTargetFileListProducerConfig()).isNotNull();
        assertThat(copy.getTargetFileListProducerConfig().getClassName())
                .isEqualTo(TestTargetFileProducer.class.getName());
        final List<TargetFile> targetFiles = copy.createTargetFileList();
        assertThat(targetFiles.size()).isEqualTo(1);
        assertThat(targetFiles).contains(targetFile1);

    }

    public static final class TestTargetFileProducer implements
            TargetFileListProducer {

        @Override
        public final List<TargetFile> createTargetFiles() {
            final List<TargetFile> files = new ArrayList<TargetFile>();
            files.add(targetFile1);
            return files;
        }

    }

}
// CHECKSTYLE:ON

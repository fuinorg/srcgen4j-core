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

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.fuin.srcgen4j.commons.DefaultContext;
import org.fuin.srcgen4j.commons.SrcGen4J;
import org.fuin.srcgen4j.commons.SrcGen4JConfig;
import org.junit.Test;

// CHECKSTYLE:OFF
public class ParameterizedTemplateGeneratorTest {

    private static final String TEST_RES_DIR = "src/test/resources";

    private static final String TARGET_DIR = "target/test-data";

    @Test
    public void testIntegration() throws Exception {

        // PREPARE
        final File expectedA = new File(TEST_RES_DIR + "/A.java");
        final File expectedB = new File(TEST_RES_DIR + "/B.java");
        final File expectedA2 = new File(TEST_RES_DIR + "/A2.java");
        final File expectedB2 = new File(TEST_RES_DIR + "/B2.java");

        final File configFile = new File(TEST_RES_DIR
                + "/velocity-test-config.xml");
        final SrcGen4JConfig config = PTGenHelper.createAndInit(
                new DefaultContext(), configFile);
        final SrcGen4J testee = new SrcGen4J(config, new DefaultContext());

        // EXECUTE
        testee.execute();

        // VERIFY

        // Generated using a list
        final File fileA = new File(TARGET_DIR, "a/A.java");
        final File fileB = new File(TARGET_DIR, "b/B.java");
        assertThat(fileA).exists();
        assertThat(fileB).exists();
        assertThat(FileUtils.contentEquals(fileA, expectedA)).isTrue();
        assertThat(FileUtils.contentEquals(fileB, expectedB)).isTrue();

        // Generated using a generator
        final File fileA2 = new File(TARGET_DIR, "a/A2.java");
        final File fileB2 = new File(TARGET_DIR, "b/B2.java");
        assertThat(fileA2).exists();
        assertThat(fileB2).exists();
        assertThat(FileUtils.contentEquals(fileA2, expectedA2)).isTrue();
        assertThat(FileUtils.contentEquals(fileB2, expectedB2)).isTrue();

    }

}
// CHECKSTYLE:ON

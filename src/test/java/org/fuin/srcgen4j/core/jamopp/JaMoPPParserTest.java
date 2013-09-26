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
package org.fuin.srcgen4j.core.jamopp;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.fuin.srcgen4j.commons.JaxbHelper;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JConfig;
import org.fuin.srcgen4j.core.base.SrcGen4JFile;
import org.junit.Test;

//CHECKSTYLE:OFF
public class JaMoPPParserTest {

    @Test
    public void testParse() throws Exception {

        // PREPARE
        final SrcGen4JFile dir = new SrcGen4JFile("src/test/jamopp");
        final SrcGen4JFile file = new SrcGen4JFile(dir, "test-config.xml");
        final JAXBContext jaxbContext = JAXBContext.newInstance(SrcGen4JConfig.class,
                JaMoPPParserConfig.class, SrcGen4JFile.class);
        final SrcGen4JConfig srcGen4JConfig = new JaxbHelper().create(file.toFile(), jaxbContext);
        srcGen4JConfig.init();
        final ParserConfig config = srcGen4JConfig.getParsers().get(0);
        assertThat(config.getConfig()).isNotNull();
        assertThat(config.getConfig().getConfig()).isNotNull();
        assertThat(config.getConfig().getConfig()).isInstanceOf(JaMoPPParserConfig.class);
        final JaMoPPParserConfig cfg = (JaMoPPParserConfig) config.getConfig().getConfig();
        assertThat(cfg.getBinDirs()).hasSize(1);
        assertThat(cfg.getJarFiles()).hasSize(1);
        assertThat(cfg.getSrcDirs()).hasSize(1);

        final List<SrcGen4JFile> srcDirs = new ArrayList<SrcGen4JFile>();
        srcDirs.add(dir);
        final JaMoPPParser testee = new JaMoPPParser(srcDirs);

        // TEST
        final ResourceSet resourceSet = testee.parse(config);

        // VERIFY
        assertThat(resourceSet).isNotNull();

    }

}
// CHECKSTYLE:ON

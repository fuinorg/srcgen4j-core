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

import java.util.ArrayList;
import java.util.List;

//CHECKSTYLE:OFF
public final class TestTFLProducer implements TargetFileListProducer {

    @Override
    public final List<TargetFile> createTargetFiles() {
        final List<TargetFile> files = new ArrayList<TargetFile>();
        files.add(new TargetFile("a", "A2.java", new Argument("name", "A2"), new Argument("pkg",
                "a")));
        files.add(new TargetFile("b", "B2.java", new Argument("name", "B2"), new Argument("pkg",
                "b")));
        return files;
    }

}
// CHECKSTYLE:ON

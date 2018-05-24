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
package org.fuin.srcgen4j.core.base;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.fuin.utils4j.Utils4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generated file.
 */
public final class GeneratedFile {

    private static final Logger LOG = LoggerFactory.getLogger(GeneratedFile.class);

    private final File file;

    private final File tmpFile;

    private final String logInfo;

    private final boolean skip;

    private boolean persisted = false;

    /**
     * Constructor with file that will NOT be skipped.
     * 
     * @param file
     *            Target file to generate to - Cannot be NULL.
     * @param logInfo
     *            Addition information to log.
     */
    public GeneratedFile(final File file, final String logInfo) {
        this(file, logInfo, false);
    }

    /**
     * Constructor with skip information.
     * 
     * @param file
     *            Target file to generate to - Cannot be NULL.
     * @param logInfo
     *            Addition information to log.
     * @param skip
     *            If the file should NOT be generated TRUE, else FALSE.
     */
    public GeneratedFile(final File file, final String logInfo, final boolean skip) {
        super();
        this.file = file;
        this.tmpFile = new File(file + ".tmp");
        if (logInfo == null) {
            this.logInfo = "";
        } else {
            this.logInfo = " [" + logInfo + "]";
        }
        this.skip = skip;
    }

    /**
     * Returns the file to generate to. This is a reference to a temporary file. Calling {@link #persist()} will copy the temporary file to
     * the target file if the content of the file is not equal.
     * 
     * @return Target file to generate to - Never NULL.
     */
    public final File getTmpFile() {
        return tmpFile;
    }

    /**
     * Returns the information if the file should be skipped.
     * 
     * @return If the file should NOT be generated TRUE, else FALSE.
     */
    public final boolean isSkip() {
        return skip;
    }

    /**
     * Compares the content of the temporary file with the possibly existing target file. If both are equal the temporary file is deleted.
     * Otherwise the old target file is deleted and the new generated file is renamed. This prevents time stamp changes for the target file
     * if nothing changed since the last generation.
     */
    public final void persist() {

        if (persisted) {
            // Do nothing if already done
            return;
        }

        try {

            // Compare new and old file
            if (FileUtils.contentEquals(tmpFile, file)) {
                LOG.debug("Omitted: {} {}", getPath(), logInfo);
                if (!tmpFile.delete()) {
                    tmpFile.deleteOnExit();
                }
            } else {
                if (!file.delete()) {
                    throw new IOException("Wasn't able to delete file " + file);
                }
                if (!tmpFile.renameTo(file)) {
                    throw new IOException("Wasn't able to rename temporary file " + tmpFile + " to " + file);
                }
                LOG.info("Generated: {} {}", getPath(), logInfo);
            }

            persisted = true;

        } catch (final IOException ex) {
            throw new RuntimeException("Error comparing content: tmp=" + tmpFile + ", target=" + file + logInfo, ex);
        }

    }

    private String getPath() {
        return Utils4J.getCanonicalPath(file);
    }

    @Override
    public final String toString() {
        return getPath();
    }

}

package org.f0w.k2i.core.util;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * I/O utils.
 */
public final class IOUtils {
    private IOUtils() {
    }

    /**
     * Check that file in given path is not null, exists and is not a directory or symlink
     *
     * @param filePath Path of file to check
     * @return Checked file path
     * @throws NullPointerException     If file path is null
     * @throws IllegalArgumentException If file not exists or not a file
     */
    public static Path checkFile(Path filePath) {
        requireNonNull(filePath, "File is null!");
        checkArgument(Files.exists(filePath), "File not exists!");
        checkArgument(Files.isRegularFile(filePath), "Not a file!");

        return filePath;
    }
}

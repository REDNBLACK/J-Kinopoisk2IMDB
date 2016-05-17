package org.f0w.k2i.core.util;

import com.google.common.hash.Hashing;
import org.f0w.k2i.core.util.exception.ExceptionUtils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.requireNonNull;
import static com.google.common.io.Files.hash;

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

    /**
     * Reads content of file in given charset.
     *
     * @param filePath File path to read from
     * @param charset Charset to use
     * @return File contents
     */
    public static String readFile(Path filePath, Charset charset) {
        return ExceptionUtils.uncheck(() -> new String(readAllBytes(filePath), charset));
    }

    /**
     * Returns SHA-256 hashcode of file.
     *
     * @param filePath File to calculate hashcode for
     * @return File hashcode.
     */
    public static String getFileHashCode(Path filePath) {
        return ExceptionUtils.uncheck(() -> hash(filePath.toFile(), Hashing.sha256()).toString());
    }
}

package org.f0w.k2i.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Jimfs;
import org.f0w.k2i.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.f0w.k2i.core.util.IOUtils.checkFile;
import static org.f0w.k2i.core.util.IOUtils.getFileHashCode;
import static org.f0w.k2i.core.util.IOUtils.readFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IOUtilsTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();

    private FileSystem fileSystem;
    private Path temporaryFile;
    private Path temporaryDirectory;

    @Before
    public void setUp() throws Exception {
        fileSystem = Jimfs.newFileSystem();
        Path root = fileSystem.getPath("/");

        temporaryDirectory = root.resolve("test_directory");
        Files.createDirectory(temporaryDirectory);

        temporaryFile = root.resolve("test_file.txt");
        Files.write(temporaryFile, ImmutableList.of("text"));
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(temporaryFile);
        Files.deleteIfExists(temporaryDirectory);
        fileSystem.close();
    }

    @Test
    public void isConstructorPrivate() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(IOUtils.class));

        TestHelper.callPrivateConstructor(IOUtils.class);
    }

    @Test
    public void checkOnExistingFile() {
        assertEquals(temporaryFile, checkFile(temporaryFile));
    }

    @Test(expected = NullPointerException.class)
    public void checkOnNullFile() throws Exception {
        checkFile(null);
    }

    @Test
    public void checkOnNotExistingFile() throws Exception {
        Path notExistingPath = Paths.get("notExistingPath");

        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("File not exists!");
        checkFile(notExistingPath);
    }

    @Test
    public void checkOnNotAFile() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Not a file!");
        checkFile(temporaryDirectory);
    }

    @Test
    public void checkReadFile() throws Exception {
        assertFalse("".equals(readFile(temporaryFile, StandardCharsets.UTF_8)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void checkGetFileHashCodeThrowsOnVirtualFS() throws Exception {
        getFileHashCode(temporaryFile);
    }
}
package org.f0w.k2i.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Jimfs;
import org.f0w.k2i.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.f0w.k2i.core.util.IOUtils.checkFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IOUtilsTest {
    private FileSystem fileSystem;
    private Path temporaryFile;
    private Path temporaryDirectory;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        fileSystem = Jimfs.newFileSystem();

        Path root = fileSystem.getPath("/");
        temporaryFile = root.resolve("test_file.txt");
        temporaryDirectory = root.resolve("test_directory");

        Files.write(temporaryFile, ImmutableList.of("text"));
        Files.createDirectory(temporaryDirectory);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(temporaryFile);
        Files.deleteIfExists(temporaryDirectory);
    }

    @Test
    public void testPrivateConstructor() throws Exception {
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
}
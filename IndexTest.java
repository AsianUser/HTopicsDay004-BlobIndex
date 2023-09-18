import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class IndexTest {
    private Index index;

    @BeforeEach
    public void setUp() {
        index = new Index();
    }

    @Test
    public void testInit() throws Exception {
        index.init();

        assertTrue(Files.exists(Paths.get(index.indexPath)));
        assertTrue(Files.exists(Paths.get(index.objectsFolderPath)));
    }

    @Test
    public void testAddAndRemove() throws Exception {
        index.init();

        String testFileName = "testFile.txt";
        index.add(testFileName);

        assertTrue(containsEntry(index.indexPath, testFileName));

        index.remove(testFileName);

        assertFalse(containsEntry(index.indexPath, testFileName));
    }

    private boolean containsEntry(String indexPath, String fileName) throws IOException {
        String content = Files.readString(Path.of(indexPath));
        return content.contains(fileName);
    }
}

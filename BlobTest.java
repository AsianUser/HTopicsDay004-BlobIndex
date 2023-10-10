import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.*;

public class BlobTest {

    private File file;

    @BeforeEach
    public void setUp() throws Exception {
        String testFilePath = "BlobTestFile.txt";
        file = new File(testFilePath);
        PrintWriter pw = new PrintWriter(file);
        pw.println("some content");
        file.createNewFile();
        pw.close();
    }

    @AfterEach
    void tearDown() throws Exception {

        // del files
        File BlobTestFile1 = new File("BlobTestFile.txt");
        BlobTestFile1.delete();
        File obj = new File("objects");
        for (File f : obj.listFiles()) {
            f.delete();
        }
        obj.delete();
    }

    @Test

    public void testConstructor() throws Exception {
        Blob blob = new Blob(file);
        String hash = blob.getHashString();
        File file2 = new File("objects/" + hash);
        System.out.println("hash: " + hash);
        assertTrue(file2.exists());
    }

    @Test
    public void testHash() throws Exception {
        Blob blob = new Blob(file);
        String myHash = blob.getHashString();
        String targetHash = "94e66df8cd09d410c62d9e0dc59d3a884e458e05";
        assertEquals(myHash, targetHash);
    }

    @Test
    public void testContents() throws Exception {
        Blob blob = new Blob(file);
        String myContents = blob.getFileContent();
        assertEquals(myContents, "some content");
    }
}

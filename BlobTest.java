import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.*;

public class BlobTest {

    private File file;

    @BeforeEach
    @DisplayName("Creating BlobTestFile.txt")
    public void setUp() throws Exception {
        String testFilePath = "BlobTestFile.txt";
        file = new File(testFilePath);
        PrintWriter pw = new PrintWriter(file);
        pw.println("some content");
        file.createNewFile();
        pw.close();
    }

    @AfterEach
    @DisplayName("Deleting BlobTestFile.txt & objects folder")
    void tearDown() throws Exception {

        // delete files
        File BlobTestFile1 = new File("BlobTestFile.txt");
        BlobTestFile1.delete();
        File obj = new File("objects");
        FileUtils.deleteDirectory(obj.getPath());
        obj.delete();
    }

    // copy-pasted from Lopez website
    @Test
    @DisplayName("Testing if Blob class creates an objects folder")
    public void testCreateFolder() throws Exception {
        // programmatically create a test file
        File file = new File("BlobTestFile.txt");
        file.createNewFile();

        // programmatically write to the test file
        // the method is a custom method written in Day 1 of Git Project
        FileUtils.writeFile("hello world", "BlobTestFile.txt", true);

        // programmatically create a blob
        Blob blob = new Blob("BlobTestFile.txt");

        // Blob should create an objects folder.
        File folder = new File("objects");

        // does the folder exist?
        assertTrue(folder.exists());
    }

    @Test
    @DisplayName("Testing if Blob class constructor creates a file in the objects folder")
    public void testConstructor() throws Exception {
        Blob blob = new Blob(file);
        String hash = blob.getHashString();
        File file2 = new File("objects/" + hash);
        System.out.println("hash: " + hash);
        // is the file existant and properly hashed?
        assertTrue(file2.exists());
    }

    @Test
    @DisplayName("Testing if Blob properly hashes the file")
    public void testHash() throws Exception {
        Blob blob = new Blob(file);
        String myHash = blob.getHashString();
        String targetHash = "94e66df8cd09d410c62d9e0dc59d3a884e458e05";
        // is Blob saving the proper hash?
        assertEquals(myHash, targetHash);
    }

    @Test
    @DisplayName("Testing if Blob properly copies contents of hashed file.")
    public void testContents() throws Exception {
        Blob blob = new Blob(file);
        String myContents = blob.getFileContent();
        // is Blob saving the proper file contents?
        assertEquals(myContents, "some content");
    }

    @Test
    @DisplayName("Testing if countCharacters() works - the method is located within FileUtils.java")
    public void testCharacterCount() throws Exception {
        Blob blob = new Blob(file);
        String myContents = blob.getFileContent();
        // does countCharacters work?
        assertEquals(FileUtils.countCharacters("objects/" + blob.getHashString()), myContents.length());
    }
}

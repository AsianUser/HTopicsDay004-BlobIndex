import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class BlobTest {
    private String testFilePath;
    private Blob blob;

    @BeforeEach
    public void setUp() {
        testFilePath = "testFile.txt";
    }

    @Test
    public void testConstructor() throws Exception {
        String file1 = "test1.txt";
        File file = new File(file1);
        file.createNewFile();
        Blob blob = new Blob(file1, file);
        String hash = blob.getHashString();
        File file2 = new File("objects/" + hash);
        System.out.println("hash: " + hash);
        assertTrue(file2.exists());
    }

    @Test
    public void testHash() throws Exception {
        String filePath = "file1.txt";
        File file = new File(filePath);
        file.createNewFile();
        PrintWriter pw = new PrintWriter(file);
        pw.println("some content");
        Blob blob = new Blob(filePath, file);
        String expectedHash = calculateSHA1(file);
        String actualHash = blob.getHashString();
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testCopyData() throws Exception {
        File testFile = new File(testFilePath);
        blob = new Blob(testFilePath);

        String expectedContent = Files.readString(Path.of(testFilePath));
        String actualContent = blob.getFileContent();

        assertEquals(expectedContent, actualContent);
    }

    private String calculateSHA1(File file) throws NoSuchAlgorithmException, IOException {
        byte[] fileData = Files.readAllBytes(file.toPath());
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(fileData);

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}

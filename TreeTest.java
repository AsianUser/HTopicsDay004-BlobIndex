import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {
    private Tree tree;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        tree = new Tree();
    }

    @Test
    public void testAdd() throws NoSuchAlgorithmException, IOException {
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        String expectedFileName = "79bfa25c08b21f0168b707f00a7b44bf4a4c07c0";
        String actualFileName = tree.fileName;

        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    public void testRemove() throws NoSuchAlgorithmException, IOException {
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        tree.remove("file1.txt");

        String expectedFileName = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String actualFileName = tree.fileName;

        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    public void testGenerateBlob() throws Exception {
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        String expectedBlobHash = "79bfa25c08b21f0168b707f00a7b44bf4a4c07c0";
        tree.generateBlob();

        File blobFile = new File("objects/" + expectedBlobHash);
        assertTrue(blobFile.exists());
    }

    @Test
    public void 

}

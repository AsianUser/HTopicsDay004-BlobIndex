import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class IndexTest {
    private Index index;

    @BeforeEach
    public void setUp() throws Exception {
        index = new Index();
        index.init();

        // create files
        File IndexTestFile1 = new File("IndexTestFile1");
        File IndexTestFile2 = new File("IndexTestFile2");
        IndexTestFile1.createNewFile();
        IndexTestFile2.createNewFile();
        PrintWriter pw1 = new PrintWriter(IndexTestFile1);
        PrintWriter pw2 = new PrintWriter(IndexTestFile2);
        pw1.write("IndexTestFile1");
        pw2.write("IndexTestFile2");
        pw1.close();
        pw2.close();
    }

    @AfterEach
    void tearDown() throws Exception {

        // del files
        File IndexTestFile1 = new File("IndexTestFile1");
        File IndexTestFile2 = new File("IndexTestFile2");
        IndexTestFile1.delete();
        IndexTestFile2.delete();

        File index = new File("Tree");
        index.delete();
        File obj = new File("objects");
        for (File f : obj.listFiles()) {
            f.delete();
        }
        obj.delete();
    }

    @Test
    public void testAddAndRemove() throws Exception {

        String testFileName = "testFile.txt";

        File temp = new File(testFileName);
        temp.createNewFile();

        index.add(testFileName);

        assertTrue(containsEntry(index.indexPath, testFileName));

        index.remove(testFileName);

        assertFalse(containsEntry(index.indexPath, testFileName));
    }

    private boolean containsEntry(String indexPath, String fileName) throws IOException {
        String content = Files.readString(Path.of(indexPath));
        return content.contains(fileName);
    }

    @Test
    void testAdd() throws Exception {
        File IndexTestFile1 = new File("IndexTestFile1");
        File IndexTestFile2 = new File("IndexTestFile2");

        index.add(IndexTestFile1.getName());

        String expected = "blob : 03dd43060849bff3cecbcd42125d3f66d58d29b3 : IndexTestFile1";

        assertEquals(FileUtils.readFile(new File("Tree")), expected);

        index.add(IndexTestFile2.getName());

        expected = "blob : 03dd43060849bff3cecbcd42125d3f66d58d29b3 : IndexTestFile1\nblob : 45d319bf47847eabb709de4ab12c134b707936ef : IndexTestFile2";

        assertEquals(FileUtils.readFile(new File("Tree")), expected);

    }

    @Test
    void testRemove() throws Exception {
        File IndexTestFile1 = new File("IndexTestFile1");
        File IndexTestFile2 = new File("IndexTestFile2");

        index.add(IndexTestFile1.getName());
        index.add(IndexTestFile2.getName());

        String expected = "blob : 45d319bf47847eabb709de4ab12c134b707936ef : IndexTestFile2";

        index.remove("IndexTestFile1");

        assertEquals(FileUtils.readFile(new File("Tree")), expected);
    }
}

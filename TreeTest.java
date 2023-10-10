import org.jcp.xml.dsig.internal.dom.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Assertions.*;

public class TreeTest {
    private Tree tree;
    private Index index;

    @BeforeEach
    public void setUp() throws Exception {
        tree = new Tree();

        index = new Index();
        index.init();

    }

    @AfterEach
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory("Objects");
        FileUtils.deleteFile("Tree");
        FileUtils.deleteDirectory("advancedTest");
        FileUtils.deleteDirectory("test1");
    }

    // LOPEZ Code
    // a good unit test makes sure tha tmy constructor works properly

    // gen a tree file

    @Test
    @DisplayName("test tree consturctor to make tree file")
    public void testTreeConstructor() throws Exception {
        Tree tree = new Tree();

        File treeFile = new File("tree");
        assertTrue(treeFile.exists());

    }

    @Test
    @DisplayName("Test if can write to tree")
    public void testWriteToTree() throws Exception {
        Tree tree = new Tree();

        File treeFile = new File("tree");
        assertTrue(treeFile.exists());

        // create bunch of files
        File testFile = new File("test.txt");
        testFile.createNewFile();

        String fileContents = FileUtils.readFile(testFile);
        String fileSHA = FileUtils.hash(fileContents);

        String newLine = "blob : " + fileSHA + " : " + testFile.getName();
        tree.addLine(newLine);

        // tree.add();
        assertEquals(newLine, tree.allConents());
    }

    @Test
    public void testAddLine() throws Exception {
        Tree tree = new Tree();
        tree.addLine("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.addLine("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        String expectedFileName = "80aaaaaea78ef9525bf854dcb1d60e2abe087221";
        String actualFileName = tree.getTreeHash();
        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    public void testRemove() throws Exception {
        Tree tree = new Tree();
        tree.addLine("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.addLine("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        tree.remove("file1.txt");

        String expectedFileName = "ee8612eaba3e603c9cb58e1d26a0b95ee3477652";
        String actualFileName = tree.getTreeHash();

        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    public void testGenerateBlob() throws Exception {
        Tree tree = new Tree();
        tree.addLine("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.addLine("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        String expectedBlobHash = "80aaaaaea78ef9525bf854dcb1d60e2abe087221";
        tree.generateBlob();

        File blobFile = new File("objects/" + expectedBlobHash);
        assertTrue(blobFile.exists());
    }

    @Test
    public void testAddDirectoryTestCase1() throws Exception {

        Tree tree = new Tree();

        // create files
        FileUtils.createDirectory("test1");

        File test1 = new File("test1");

        File examplefile1 = new File("test1", "examplefile1");
        File examplefile2 = new File("test1", "examplefile2");
        File examplefile3 = new File("test1", "examplefile3");

        FileWriter fw1 = new FileWriter(examplefile1);
        FileWriter fw2 = new FileWriter(examplefile2);
        FileWriter fw3 = new FileWriter(examplefile3);

        fw1.write("the sha of this is ... ?");
        fw2.write("zomg wut are u doing. LAWL?");
        fw3.write("LOL please dont read this.  Good job being thorough tho!");

        fw1.close();
        fw2.close();
        fw3.close();

        // run test

        String expectedText = "blob : " + FileUtils.hash("the sha of this is ... ?") + " : examplefile1\n"
                + "blob : "
                + FileUtils.hash("zomg wut are u doing. LAWL?") + " : examplefile2\n" + "blob : "
                + FileUtils.hash("LOL please dont read this.  Good job being thorough tho!")
                + " : examplefile3";

        String expectedTree = "tree : " + FileUtils.hash(expectedText) + " : test1";
        String expectedHash = FileUtils.hash(expectedTree);

        tree.addDirectory("test1");
        // assertEquals(tree.getPreHashDirectory(), expectedTree);
        // throw new Exception("" + test1.exists());

        assertEquals(expectedHash, tree.directoryHash);

    }

    @Test
    public void testAddDirectoryAdvancedTest() throws Exception {
        Tree tree = new Tree();

        File test1 = new File("advancedTest");

        if (test1.listFiles() != null) {
            for (File f : test1.listFiles()) {
                f.delete();
            }
        }
        test1.mkdir();

        File test3 = new File("advancedTest", "test3");
        test3.mkdir();
        File test5 = new File("advancedTest", "test5");
        test5.mkdir();
        for (File f : test5.listFiles()) {
            f.delete();
        }

        File advnacedtest1 = new File(test5, "advnacedtest1.txt");
        advnacedtest1.createNewFile();
        // ("test5", "advnacedtest1");

        File examplefile1 = new File("advancedTest", "examplefile1.txt");
        examplefile1.createNewFile();
        File examplefile2 = new File("advancedTest", "examplefile2.txt");
        examplefile2.createNewFile();
        File examplefile3 = new File("advancedTest", "examplefile3.txt");
        examplefile3.createNewFile();

        FileWriter fw1 = new FileWriter(examplefile1);
        FileWriter fw2 = new FileWriter(examplefile2);
        FileWriter fw3 = new FileWriter(examplefile3);
        FileWriter fw4 = new FileWriter(advnacedtest1);

        fw1.write("the sha of this is ... ?");
        fw2.write("zomg wut are u doing. LAWL");
        fw3.write("LOL please dont read this.  Good job being thorough tho!");
        fw4.write("wooow good job!");

        fw1.close();
        fw2.close();
        fw3.close();
        fw4.close();

        // run test

        String expectedHash = "eb6d9a8158b62c74f4be79e2b89d964239964861";

        tree.addDirectory("advancedTest");
        // assertEquals(tree.testString, expectedText);

        assertEquals(expectedHash, tree.directoryHash);
    }

}

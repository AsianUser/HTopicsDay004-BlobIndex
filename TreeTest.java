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

    @BeforeEach
    public void setUp() throws Exception {
        tree = new Tree();
    }

    // LOPEZ Code
    // a good unit test makes sure tha tmy constructor works properly

    // gen a tree file

    @Test
    @DisplayName("tet tree consturctor to make tree file")
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
    public void testAdd() throws Exception {
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        String expectedFileName = "79bfa25c08b21f0168b707f00a7b44bf4a4c07c0";
        String actualFileName = tree.fileName;

        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    public void testRemove() throws Exception {
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
    public void testAddDirectoryTestCase1() throws Exception {

        // create files
        File test1 = new File("test1");
        test1.mkdir();

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

        // tree.addDirectory("test1");
        // assertEquals(tree.getPreHashDirectory(), expectedTree);

        assertEquals(expectedHash, tree.addDirectory("test1"),
                tree.getPreHashDirectory() + "zzz\n\n" + expectedTree);

    }

    @Test
    public void testAddDirectoryAdvancedTest() throws Exception {
        // create files
        File test1 = new File("advancedTest");
        for (File f : test1.listFiles()) {
            f.delete();
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

        String expectedText = "blob : 6cecd98f685b1c9bfce96f2bbf3f8f381bcc717e : examplefile1.txt\nblob : 7fb1c700700603eef612e0ffedff5e1fa5af50b6 : examplefile2.txt\nblob : 7588059d9f514dcf29aec96e4b3aff9a467f7172 : examplefile3.txt\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : test3\ntree : 032544217e9609e54d911df3963bdefc53b3fdda : test5";

        String expectedTree = "tree : " + FileUtils.hash(expectedText) + " : advancedTest";
        String expectedHash = FileUtils.hash(expectedTree);

        tree.addDirectory("advancedTest");
        assertEquals(tree.testString, expectedText);

        assertEquals(expectedHash, tree.addDirectory("advancedTest"),
                tree.getPreHashDirectory() + "zzz\n\n" + expectedTree);
    }

}

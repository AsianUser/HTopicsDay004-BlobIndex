
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
    @DisplayName("Re-initializes tree & makes all files for testing")
    public void setUp() throws Exception {
        tree = new Tree();

        index = new Index();
        index.init();

        // create files
        String TreeTestFile1Name = "TreeTestFile1";
        File TreeTestFile1 = new File(TreeTestFile1Name);
        PrintWriter pw = new PrintWriter(TreeTestFile1);
        pw.println("TreeTestFile1");
        TreeTestFile1.createNewFile();
        pw.close();
        Blob TreeTestFile1Blob = new Blob(TreeTestFile1);

        String TreeTestFile2Name = "TreeTestFile2";
        File TreeTestFile2 = new File(TreeTestFile2Name);
        pw = new PrintWriter(TreeTestFile2);
        pw.println("TreeTestFile2");
        TreeTestFile2.createNewFile();
        pw.close();
        Blob TreeTestFile2Blob = new Blob(TreeTestFile2);

        String TreeTestFile3Name = "TreeTestFile3";
        File TreeTestFile3 = new File(TreeTestFile3Name);
        pw = new PrintWriter(TreeTestFile3);
        pw.println("TreeTestFile3");
        TreeTestFile3.createNewFile();
        pw.close();
        Blob TreeTestFile3Blob = new Blob(TreeTestFile3);

    }

    @AfterEach
    @DisplayName("Deletes all files & folders that may have been created")
    public void tearDown() throws Exception {
        // FileUtils.deleteDirectory("objects");
        FileUtils.deleteFile("Tree");
        FileUtils.deleteFile("TreeTestFile1");
        FileUtils.deleteFile("TreeTestFile2");
        FileUtils.deleteFile("TreeTestFile3");

        // from addDirectory tests
        FileUtils.deleteDirectory("advancedTest");
        FileUtils.deleteDirectory("test1");
    }

    // LOPEZ Code
    // a good unit test makes sure tha tmy constructor works properly

    // gen a tree file

    @Test
    @DisplayName("test tree constructor to make tree file")
    public void testTreeConstructor() throws Exception {
        Tree tree = new Tree();

        File treeFile = new File("tree");
        assertTrue(treeFile.exists());

    }

    @Test
    @DisplayName("Tests adding individual lines directly to Tree file - this is mostly a helper method")
    public void testAddLine() throws Exception {
        Tree tree = new Tree();
        tree.addLine("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.addLine("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        String expectedFileName = "80aaaaaea78ef9525bf854dcb1d60e2abe087221";
        String actualFileName = tree.getTreeHash();
        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    @DisplayName("Tests adding files to Tree file")
    public void testAdd() throws Exception {
        // assert adding via fileName works
        tree.add("TreeTestFile1");
        // System.out.println(FileUtils.readFile(new File("tree")));
        assertTrue(FileUtils.readFile(new File("Tree")).contains("TreeTestFile1"));
        // add via preformatted
        tree.add("blob : 9466c89b883caf71d2e11922122c40d903d941c1 : TreeTestFile3");
        assertTrue(FileUtils.readFile(new File("Tree"))
                .contains("blob : 9466c89b883caf71d2e11922122c40d903d941c1 : TreeTestFile3"));

        // test format
        String expectedTree = "blob : 2b9f612f7f2d2a578f6eef93d06c3b58496f8c3d : TreeTestFile1\nblob : 9466c89b883caf71d2e11922122c40d903d941c1 : TreeTestFile3";
        assertEquals(FileUtils.readFile(new File("Tree")), expectedTree);

    }

    @Test
    @DisplayName("Tests deleting files from Tree file")
    public void testRemove() throws Exception {
        // add some files
        tree.add("TreeTestFile1");
        tree.add("TreeTestFile2");
        tree.add("TreeTestFile3");

        // remove
        tree.remove("TreeTestFile2");

        // hash
        String expectedHash = FileUtils.hash(
                "blob : 2b9f612f7f2d2a578f6eef93d06c3b58496f8c3d : TreeTestFile1\nblob : 9466c89b883caf71d2e11922122c40d903d941c1 : TreeTestFile3");
        String actualHash = tree.getTreeHash();

        // assert line was indeed removed
        assertEquals(expectedHash, actualHash);

        // remove
        tree.remove("TreeTestFile1");

        // hash
        expectedHash = FileUtils.hash("blob : 9466c89b883caf71d2e11922122c40d903d941c1 : TreeTestFile3");
        actualHash = tree.getTreeHash();

        // assert line was indeed removed
        assertEquals(expectedHash, actualHash);

        // remove via SHA1
        tree.remove("9466c89b883caf71d2e11922122c40d903d941c1");

        // hash
        expectedHash = FileUtils.hash("");
        actualHash = tree.getTreeHash();

        // assert line was indeed removed
        assertEquals(expectedHash, actualHash);
    }

    @Test
    @DisplayName("Tests generating a blob from Tree file")
    public void testGenerateBlob() throws Exception {
        Tree tree = new Tree();
        // add some files
        tree.add("TreeTestFile1");
        tree.add("TreeTestFile3");

        String expectedBlobHash = FileUtils.hash(
                "blob : 2b9f612f7f2d2a578f6eef93d06c3b58496f8c3d : TreeTestFile1\nblob : 9466c89b883caf71d2e11922122c40d903d941c1 : TreeTestFile3");
        tree.save();

        File blobFile = new File("objects/" + expectedBlobHash);
        // assert that the Blob file exists
        assertTrue(blobFile.exists());

        // remove a file
        tree.remove("TreeTestFile3");

        // make another Blob for new Tree
        tree.save();

        // make sure original still exists
        assertTrue(blobFile.exists());

        expectedBlobHash = FileUtils.hash("blob : 2b9f612f7f2d2a578f6eef93d06c3b58496f8c3d : TreeTestFile1");
        blobFile = new File("objects/" + expectedBlobHash);
        // assert that the Blob file exists
        assertTrue(blobFile.exists());

    }

    @Test
    @DisplayName("Tests generating adding Directory per the TestCase1 downloaded from the hub")
    public void testAddDirectoryTestCase1() throws Exception {

        tree = new Tree();

        // create files according to the directory test from hub
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

        // String expectedTree = "tree : " + FileUtils.hash(expectedText) + " : test1";
        String expectedHash = FileUtils.hash(expectedText);

        System.out.println(tree.addDirectory("test1"));
        System.out.println(FileUtils.readFile(new File("Tree")));
        System.out.println(expectedText);
        // assertEquals(tree.getPreHashDirectory(), expectedTree);
        // throw new Exception("" + test1.exists());

        assertEquals(expectedHash, tree.directoryHash);

        // make sure Blob exists
        File dirBlob = new File("objects", expectedHash);
        assertTrue(dirBlob.exists());

    }

    @Test
    @DisplayName("Tests generating adding Directory per the advancedTest downloaded from the hub - slighty modified")
    public void testAddDirectoryAdvancedTest() throws Exception {
        tree = new Tree();

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
        // new addition -->
        File advnacedtest2 = new File(test5, "advnacedtest2.txt");
        advnacedtest2.createNewFile();

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
        FileWriter fw5 = new FileWriter(advnacedtest2);

        fw1.write("the sha of this is ... ?");
        fw2.write("zomg wut are u doing. LAWL");
        fw3.write("LOL please dont read this.  Good job being thorough tho!");
        fw4.write("wooow good job!");
        fw5.write("balls");

        fw1.close();
        fw2.close();
        fw3.close();
        fw4.close();
        fw5.close();

        // run test

        String expectedContents = "blob : 6cecd98f685b1c9bfce96f2bbf3f8f381bcc717e : examplefile1.txt\nblob : 7fb1c700700603eef612e0ffedff5e1fa5af50b6 : examplefile2.txt\nblob : 7588059d9f514dcf29aec96e4b3aff9a467f7172 : examplefile3.txt\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : test3\ntree : 657a041270147fda6ad6fdc4d16ce309a1590575 : test5";

        String expectedHash = FileUtils.hash(expectedContents);

        tree.addDirectory("advancedTest");
        // assertEquals(tree.testString, expectedText);

        System.out.println("/________\n" + FileUtils.readFile(new File("Tree")) + "\n_________/");

        assertEquals(expectedHash, tree.directoryHash);

        // make sure Blobs exists
        File dirBlob = new File("objects", expectedHash);
        assertTrue(dirBlob.exists());

        File test3Blob = new File("objects", "da39a3ee5e6b4b0d3255bfef95601890afd80709");

        File test5Blob = new File("objects", "657a041270147fda6ad6fdc4d16ce309a1590575");
        File advnacedtest1Blob = new File("objects", "62238a7c52c3f5a2dc47c252081ca7abb360f98b");
        File advnacedtest2Blob = new File("objects", "99e4f5b9e5272cc0b5ff5f29909fd508cd49e5f2 ");

        File examplefile1Blob = new File("objects", "6cecd98f685b1c9bfce96f2bbf3f8f381bcc717e");
        File examplefile2Blob = new File("objects", "7fb1c700700603eef612e0ffedff5e1fa5af50b6");
        File examplefile3Blob = new File("objects", "7588059d9f514dcf29aec96e4b3aff9a467f7172");

        assertTrue(test3Blob.exists());
        assertTrue(test5Blob.exists());
        assertTrue(advnacedtest1Blob.exists());
        assertTrue(advnacedtest2Blob.exists());
        assertTrue(examplefile1Blob.exists());
        assertTrue(examplefile2Blob.exists());
        assertTrue(examplefile3Blob.exists());
    }

    @Test
    @DisplayName("Tests generating adding Directory and then another blob")
    public void testAddDirectoryAndBlob() throws Exception {
        tree = new Tree();

        // copy paste from previous test
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

        // String expectedTree = "tree : " + FileUtils.hash(expectedText) + " : test1";
        System.out.println(expectedText);
        String expectedHash = FileUtils.hash(expectedText);

        System.out.println(tree.addDirectory("test1"));
        System.out.println("/-----" + FileUtils.readFile(new File("Tree")) + "---------------/");

        // assertEquals(tree.getPreHashDirectory(), expectedTree);
        // throw new Exception("" + test1.exists());

        assertEquals(expectedHash, tree.directoryHash);

        // make sure Blob exists
        File dirBlob = new File("objects", expectedHash);
        assertTrue(dirBlob.exists());

        // add another blob afterwards
        String TreeTestFile1Name = "TreeTestFile1";
        File TreeTestFile1 = new File(TreeTestFile1Name);
        tree.add(TreeTestFile1.getName());

        System.out.println("/-----" + FileUtils.readFile(new File("Tree")) + "---------------/");

        String expectedTreeContents = "tree : " + expectedHash
                + " : test1\nblob : 2b9f612f7f2d2a578f6eef93d06c3b58496f8c3d : TreeTestFile1";

        // assert if contents of tree file is as expected
        assertEquals(FileUtils.readFile(new File("Tree")), expectedTreeContents);

        tree.save();
        // assert if generated blob matches expected
        assertEquals(FileUtils.readFile(new File("objects", tree.getTreeHash())), expectedTreeContents);

    }

}

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTest {

    Tree tree;

    @BeforeEach
    @DisplayName("Re-initializes tree & makes all files for testing")
    void setUp() throws Exception {

        tree = new Tree();

        FileUtils.deleteFile("head");
        FileUtils.deleteFile("Tree");

        File CommitTestFile1 = new File("CommitTestFile1");
        File CommitTestFile2 = new File("CommitTestFile2");
        File CommitTestFile3 = new File("CommitTestFile3");
        File CommitTestFile4 = new File("CommitTestFile4");
        File CommitTestFile5 = new File("CommitTestFile5");
        File CommitTestFile6 = new File("CommitTestFile6");
        File CommitTestFile7 = new File("CommitTestFile7");
        File CommitTestFile8 = new File("CommitTestFile8");
        CommitTestFile1.createNewFile();
        CommitTestFile2.createNewFile();
        CommitTestFile3.createNewFile();
        CommitTestFile4.createNewFile();
        CommitTestFile5.createNewFile();
        CommitTestFile6.createNewFile();
        CommitTestFile7.createNewFile();
        CommitTestFile8.createNewFile();

        PrintWriter pw1 = new PrintWriter(CommitTestFile1);
        PrintWriter pw2 = new PrintWriter(CommitTestFile2);
        PrintWriter pw3 = new PrintWriter(CommitTestFile3);
        PrintWriter pw4 = new PrintWriter(CommitTestFile4);
        PrintWriter pw5 = new PrintWriter(CommitTestFile5);
        PrintWriter pw6 = new PrintWriter(CommitTestFile6);
        PrintWriter pw7 = new PrintWriter(CommitTestFile7);
        PrintWriter pw8 = new PrintWriter(CommitTestFile8);
        pw1.write("CommitTestFile1");
        pw2.write("CommitTestFile2");
        pw3.write("CommitTestFile3");
        pw4.write("CommitTestFile4");
        pw5.write("CommitTestFile5");
        pw6.write("CommitTestFile6");
        pw7.write("CommitTestFile7");
        pw8.write("CommitTestFile8");

        File CommitTestFolder1 = new File("CommitTestFolder1");
        File CommitTestFolder2 = new File("CommitTestFolder2");
        File CommitTestFolder3 = new File("CommitTestFolder3");
        CommitTestFolder1.mkdir();
        CommitTestFolder2.mkdir();
        CommitTestFolder3.mkdir();

        File CommitTestFile9 = new File(CommitTestFolder1.getName(), "CommitTestFile9");
        File CommitTestFile10 = new File(CommitTestFolder2.getName(), "CommitTestFile10");
        CommitTestFile9.createNewFile();
        CommitTestFile10.createNewFile();

        PrintWriter pw9 = new PrintWriter(CommitTestFile9);
        PrintWriter pw10 = new PrintWriter(CommitTestFile10);
        pw9.write("CommitTestFile9");
        pw10.write("CommitTestFile10");

        pw1.close();
        pw2.close();
        pw3.close();
        pw4.close();
        pw5.close();
        pw6.close();
        pw7.close();
        pw8.close();
        pw9.close();
        pw10.close();
    }

    @AfterEach
    @DisplayName("deletes all files used in testing")
    void tearDown() throws Exception {
        File CommitTestFile1 = new File("CommitTestFile1");
        File CommitTestFile2 = new File("CommitTestFile2");
        File CommitTestFile3 = new File("CommitTestFile3");
        File CommitTestFile4 = new File("CommitTestFile4");
        File CommitTestFile5 = new File("CommitTestFile5");
        File CommitTestFile6 = new File("CommitTestFile6");
        File CommitTestFile7 = new File("CommitTestFile7");
        File CommitTestFile8 = new File("CommitTestFile8");
        CommitTestFile1.delete();
        CommitTestFile2.delete();
        CommitTestFile3.delete();
        CommitTestFile4.delete();
        CommitTestFile5.delete();
        CommitTestFile6.delete();
        CommitTestFile7.delete();
        CommitTestFile8.delete();

        File CommitTestFolder1 = new File("CommitTestFolder1");
        File CommitTestFolder2 = new File("CommitTestFolder2");
        File CommitTestFolder3 = new File("CommitTestFolder3");
        FileUtils.deleteDirectory(CommitTestFolder1.getName());
        FileUtils.deleteDirectory(CommitTestFolder2.getName());
        FileUtils.deleteDirectory(CommitTestFolder3.getName());

        FileUtils.deleteDirectory("objects");
        FileUtils.deleteFile("head");
        FileUtils.deleteFile("Tree");
        FileUtils.deleteFile("commit");

    }

    // not really sure how else to properly test
    @Test
    @DisplayName("Test if the date is properly updated and set")
    void testGetDate() throws Exception {

        Commit c1 = new Commit("author", "summary");
        c1.commit();

        String date = c1.getDate();

        assertNotNull(date);

    }

    // i got this from a pull request; not sure what the intention of this test was
    @Test
    @DisplayName("Test if the tree file exists")
    void testMakeTree() throws Exception {

        Tree tree = new Tree();
        File a = new File("Tree");
        // File b = new File("b");

        assertEquals(a.exists(), true);
        // assertEquals(b.exists(), true);

    }

    @Test
    @DisplayName("Test if Commit can save one commit with multiple files")
    void testCreate1Commit() throws Exception {
        // add 2 files

        tree.add("CommitTestFile1");
        tree.add("CommitTestFile2");

        Commit c1 = new Commit("author", "summary");
        c1.commit();

        // checks if the commit file contains the right contents
        assertEquals(FileUtils.readFile(c1.commitFile), "f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "\n" +
                "author\n" +
                c1.getDate() + "\n" +
                "summary");

        // check if the commit blob contains the right contents
        assertEquals(FileUtils.readFile(c1.commitFile), "f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "\n" +
                "author\n" +
                c1.getDate() + "\n" +
                "summary");
    }

    @Test
    @DisplayName("Test if commit can handle 2 commits with folders")
    void testCreate2Commits() throws Exception {
        // add 2 files

        tree.add("CommitTestFile1");
        tree.add("CommitTestFile2");

        Commit c1 = new Commit("author", "summary");
        c1.commit();
        System.out.println("c1 " + c1.getCurrentCommitSHA());

        // checks if the commit file contains the right contents
        assertEquals(FileUtils.readFile(c1.commitFile),
                "f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                        "\n" +
                        "\n" +
                        "author\n" +
                        c1.getDate() + "\n" +
                        "summary");

        tree.add("CommitTestFile3");
        tree.add("CommitTestFolder1");

        Commit c2 = new Commit("author", "summary", c1.getCurrentCommitSHA());
        c2.commit();

        // checks if the commit file contains the right contents
        assertEquals(FileUtils.readFile(c2.commitFile), "8df0f059472e1bf7cf79317d3053623ffaa52077\n" +
                "55fd16d4885179d9ecaeb630ee33dde71724d930\n" +
                "\n" +
                "author\n" +
                c2.getDate() + "\n" +
                "summary");

        // check if the commit blob contains the right contents
        assertEquals(FileUtils.readFile(c1.commitBlobFile), ("f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "70e40a141d703c465cc348360ad731b693eab853\n" +
                "author\n" +
                c1.getDate() + "\n" +
                "summary"));

        // check if the commit blob contains the right contents
        assertEquals(FileUtils.readFile(c2.commitBlobFile), "8df0f059472e1bf7cf79317d3053623ffaa52077\n" +
                "55fd16d4885179d9ecaeb630ee33dde71724d930\n" +
                "\n" +
                "author\n" +
                c2.getDate() + "\n" +
                "summary");

    }

    @Test
    @DisplayName("Test if commit can handle 4 commits with folders")
    void testCreate4Commits() throws Exception {
        // add 4 files

        tree.add("CommitTestFile1");
        tree.add("CommitTestFile2");

        Commit c1 = new Commit("author", "summary");
        c1.commit();
        System.out.println("c1" + c1.getCurrentCommitSHA());

        tree.add("CommitTestFile3");
        tree.add("CommitTestFile4");
        tree.add("CommitTestFolder1");

        Commit c2 = new Commit("author", "summary", c1.getCurrentCommitSHA());
        c2.commit();

        tree.add("CommitTestFile5");
        tree.add("CommitTestFile6");
        tree.add("CommitTestFolder2");

        Commit c3 = new Commit("author", "summary", c2.getCurrentCommitSHA());
        c3.commit();

        tree.add("CommitTestFile7");
        tree.add("CommitTestFile8");

        Commit c4 = new Commit("author", "summary", c3.getCurrentCommitSHA());
        c4.commit();

        // test c1
        assertEquals("f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "4d63955f1298eefa936cee1deb02a9c8ba2eabb4\n" +
                "author\n" +
                c1.setDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c1.getCurrentCommitSHA())));

        // test c2
        assertEquals("46c5a7f675d56d8c4b56cd5aa60149ebf15b3c21\n" +
                "55fd16d4885179d9ecaeb630ee33dde71724d930\n" +
                "658d65041c89ce03462e16a358f330e113e80662\n" +
                "author\n" +
                c2.setDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c2.getCurrentCommitSHA())));

        // test c3
        assertEquals("e96af81350ca1b173271f526d428f7d7c0b8cb65\n" +
                "4d63955f1298eefa936cee1deb02a9c8ba2eabb4\n" +
                "1add18d909c9928c93193ec6ad36341c8d6d31a4\n" +
                "author\n" +
                c3.setDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c3.getCurrentCommitSHA())));

        // test c4
        assertEquals("f5444c2d977b530754dc3ac2074bcea438f372d2\n" +
                "658d65041c89ce03462e16a358f330e113e80662\n" +
                "\n" +
                "author\n" +
                c4.setDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c4.getCurrentCommitSHA())));

    }

    // @Test
    // void testSeeNext() throws Exception {

    // System.out.println(Commit.getDate());
    // Commit com = new Commit("Bo", "Cool!");

    // PrintWriter pw = new PrintWriter();
    // pw.write("lol");
    // pw.close();
    // com.commit();
    // // com.writeFile();

    // System.out.println(com.hashesToString());

    // PrintWriter pw2 = new PrintWriter();
    // pw2.write("lol2");
    // pw2.close();
    // com.commit();
    // // com.writeFile();
    // System.out.println(com.hashesToString());

    // PrintWriter pw3 = new PrintWriter();
    // pw3.write("lol3");
    // pw3.close();
    // com.commit();
    // com.writeFile();
    // System.out.println(com.hashesToString());

    // com.seePrev();
    // com.writeFile();

    // com.seeNext();
    // com.writeFile();
    // String name = com.hashes.get(2);

    // // recycle earlier code

    // assertEquals(new File(name).exists(),
    // true);

    // }

    // @Test
    // void testSeePrev() throws Exception {

    // System.out.println(Commit.getDate());
    // Commit com = new Commit("Bo", "Cool!");

    // PrintWriter pw = new PrintWriter();
    // pw.write("lol");
    // pw.close();
    // com.commitFile();
    // com.writeFile();

    // System.out.println(com.hashesToString());

    // PrintWriter pw2 = new PrintWriter();
    // pw2.write("lol2");
    // pw2.close();
    // com.commitFile();
    // com.writeFile();
    // System.out.println(com.hashesToString());

    // PrintWriter pw3 = new PrintWriter();
    // pw3.write("lol3");
    // pw3.close();
    // com.commitFile();
    // com.writeFile();
    // System.out.println(com.hashesToString());

    // com.seePrev();
    // com.writeFile();
    // String name = com.hashes.get(1);

    // // recycle earlier code

    // assertEquals(new File(name).exists(),
    // true);

    // }

}

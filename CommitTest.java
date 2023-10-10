import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommitTest {

    @BeforeEach
    void setUp() throws Exception {

        Index index = new Index();
        index.init();

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
    }

    @Test
    void testCommitFile() throws Exception {

        Commit c = new Commit("Billy", "W Billy Commit!");
        c.commit();

        // assertEquals(c.hashes.size() == 1, true);

    }

    // @Test
    // void testGenerateSHA() throws NoSuchAlgorithmException {

    // String normal = "bobby";
    // String hash = Commit.generateSHA(normal);

    // assertEquals(hash, "4501c3b0336cf2d19ed69a8d0ec436ee3f88b31b");

    // }

    // not really sure how else to properly test
    @Test
    void testGetDate() throws Exception {

        String date = Commit.getDate();

        assertNotNull(date);

    }

    @Test
    void testHashesToString() throws Exception {

        Commit c = new Commit("Billy", "W Billy Commit!");
        c.commit();
        // c.writeFile();

        PrintWriter pw = new PrintWriter("Tree");

        pw.write("UWU SUSSY BAKA");
        pw.close();
        c.commit();
        // c.writeFile();

        String list = c.hashesToString();

        assertEquals(c.hashesToString() != null, true);
        assertEquals(list.indexOf(",") == -1, false);

    }

    // not sure what intention of this test was
    @Test
    void testMakeTree() throws Exception {

        Tree tree = new Tree();
        File a = new File("Tree");
        // File b = new File("b");

        assertEquals(a.exists(), true);
        // assertEquals(b.exists(), true);

    }

    @Test
    void testCreate1Commit() throws Exception {
        // add 2 files
        Index index = new Index();
        index.init();

        index.add("CommitTestFile1");
        index.add("CommitTestFile2");

        Commit c1 = new Commit("author", "summary");
        c1.commit();

        assertEquals(FileUtils.readFile(c1.commitFile), "f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "\n" +
                "author\n" +
                Commit.getDate() + "\n" +
                "summary");
    }

    @Test
    void testCreate2Commits() throws Exception {
        // add 2 files
        Index index = new Index();
        index.init();

        index.add("CommitTestFile1");
        index.add("CommitTestFile2");

        Commit c1 = new Commit("author", "summary");
        c1.commit();
        System.out.println("c1" + c1.getCurrentCommitSHA());

        index.add("CommitTestFile3");
        index.add("CommitTestFolder1");

        Commit c2 = new Commit("author", "summary", c1.getCurrentCommitSHA());
        c2.commit();

        // test c1
        assertEquals("f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "ef556bdf4bdbd9f6a91e8e95fa4e347330e63b14\n" +
                "author\n" +
                Commit.getDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c1.getCurrentCommitSHA())));

        // test c2
        assertEquals("ef556bdf4bdbd9f6a91e8e95fa4e347330e63b14\n" +
                "f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "author\n" +
                Commit.getDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c2.getCurrentCommitSHA())));

    }

    @Test
    void testCreate4Commits() throws Exception {
        // add 4 files
        Index index = new Index();
        index.init();

        index.add("CommitTestFile1");
        index.add("CommitTestFile2");

        Commit c1 = new Commit("author", "summary");
        c1.commit();
        System.out.println("c1" + c1.getCurrentCommitSHA());

        index.add("CommitTestFile3");
        index.add("CommitTestFile4");
        index.add("CommitTestFolder1");

        Commit c2 = new Commit("author", "summary", c1.getCurrentCommitSHA());
        c2.commit();

        index.add("CommitTestFile5");
        index.add("CommitTestFile6");
        index.add("CommitTestFolder2");

        Commit c3 = new Commit("author", "summary", c2.getCurrentCommitSHA());
        c3.commit();

        index.add("CommitTestFile7");
        index.add("CommitTestFile8");

        Commit c4 = new Commit("author", "summary", c3.getCurrentCommitSHA());
        c4.commit();

        // test c1
        assertEquals("f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "\n" +
                "7f90c162a42eb01e49c03b25dade6eda932d1794\n" +
                "author\n" +
                Commit.getDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c1.getCurrentCommitSHA())));

        // test c2
        assertEquals("7f90c162a42eb01e49c03b25dade6eda932d1794\n" +
                "f5fbdf45158451c507e3601504d0c4e216789f79\n" +
                "720bb19e78f5a0b182628afd13a6ec24dc9d7199\n" +
                "author\n" +
                Commit.getDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c2.getCurrentCommitSHA())));

        // test c3
        assertEquals("720bb19e78f5a0b182628afd13a6ec24dc9d7199\n" +
                "7f90c162a42eb01e49c03b25dade6eda932d1794\n" +
                "f4f04497d708c75a91797a7f2ae5c15c99f556ff\n" +
                "author\n" +
                Commit.getDate() + "\n" +
                "summary", FileUtils.readFile(new File("objects", c3.getCurrentCommitSHA())));

        // test c4
        assertEquals("f4f04497d708c75a91797a7f2ae5c15c99f556ff\n" +
                "720bb19e78f5a0b182628afd13a6ec24dc9d7199\n" +
                "\n" +
                "author\n" +
                Commit.getDate() + "\n" +
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

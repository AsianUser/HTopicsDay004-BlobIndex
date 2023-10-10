import java.io.File;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterEach;

public class CommitTester {

    public static void main(String[] args) throws Exception {

        // create objects folder
        File obj = new File("objects");
        obj.delete();
        obj.mkdir();

        // gen files
        File commitTestFile1 = new File("commitTestFile1");
        File commitTestFile2 = new File("commitTestFile2");
        commitTestFile1.createNewFile();
        commitTestFile2.createNewFile();
        PrintWriter pw1 = new PrintWriter(commitTestFile1);
        PrintWriter pw2 = new PrintWriter(commitTestFile2);
        pw1.write("abc");
        pw2.write("tstestests");
        pw1.close();
        pw2.close();
        // add to Index
        Index index = new Index();
        index.init();

        index.add("commitTestFile1");
        index.add("commitTestFile2");

        // create commit

        Commit com = new Commit("author", "summary");
        com.commit();

        // Commit com = new Commit("Bo", "Cool!");
        // com.commitFile();
        // com.writeFile();
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
        // System.out.println(com.hashesToString());

        // com.seeNext();

        // com.writeFile();

        // com.seePrev();
        // com.writeFile();
        // com.seePrev();
        // com.writeFile();
        // com.seePrev();
        // com.writeFile();

        // Commit c = new Commit("Bo", "Cool!",
        // "597dd55fa9e8a39cbc8d18a92ecff4a02c589d9");
        // c.commitFile();
        // c.writeFile();
        // System.out.println(c.hashesToString());

        // PrintWriter pw5 = new PrintWriter("");
        // pw5.write("hehe");
        // pw5.close();
        // c.commitFile();
        // c.writeFile();
        // System.out.println(c.hashesToString());

        // c.seePrev();
        // c.writeFile();

    }

}

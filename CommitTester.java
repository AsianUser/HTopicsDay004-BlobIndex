import java.io.PrintWriter;

public class CommitTester {

    public static void main(String[] args) throws Exception {
        System.out.println(Commit.getDate());
        Commit com = new Commit("Bo", "Cool!");
        com.commitFile();
        com.writeFile();
        PrintWriter pw = new PrintWriter();
        pw.write("lol");
        pw.close();
        com.commitFile();
        com.writeFile();

        System.out.println(com.hashesToString());

        PrintWriter pw2 = new PrintWriter();
        pw2.write("lol2");
        pw2.close();
        com.commitFile();
        com.writeFile();
        System.out.println(com.hashesToString());

        PrintWriter pw3 = new PrintWriter();
        pw3.write("lol3");
        pw3.close();
        com.commitFile();
        com.writeFile();
        System.out.println(com.hashesToString());

        com.seePrev();
        com.writeFile();
        System.out.println(com.hashesToString());

        com.seeNext();

        com.writeFile();

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

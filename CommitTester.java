import java.io.PrintWriter;

public class CommitTester {

    public static void main(String[] args) throws Exception {
        System.out.println(Commit.getDate());
        Commit com = new Commit("Bo", "Cool!");
        com.commitFile();
        PrintWriter pw = new PrintWriter("/Users/lilbarbar/Desktop/Honors Topics/Bens-Amazing-Git/Tree-Objects/Tree");
        pw.write("lol");
        pw.close();
        com.commitFile();

    }

}

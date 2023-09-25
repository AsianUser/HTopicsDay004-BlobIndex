public class CommitTester {

    public static void main(String[] args) throws Exception {
        System.out.println(Commit.getDate());
        Commit com = new Commit("Bo", "Cool!");
        com.commitFile();

    }

}

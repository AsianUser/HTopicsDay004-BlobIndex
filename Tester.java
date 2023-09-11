
public class Tester {
    public static void main(String[] args) throws Exception {
        Blob test = new Blob(
                ".\\test\\test.txt");

        Index indexTest = new Index();

        indexTest.init();

        indexTest.add("test.txt");
        indexTest.add("duplicate.txt");
        indexTest.add("test2.txt");

        indexTest.remove("test.txt");

        System.out.println("tester done");

    }
}

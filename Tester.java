import java.io.File;

public class Tester {
    public static void main(String[] args) throws Exception {

        File blobTest = new File(".\\test\\test.txt");

        Blob test = new Blob(blobTest);

        Index indexTest = new Index();

        indexTest.init();

        indexTest.add("test.txt");
        indexTest.add("duplicate.txt");
        indexTest.add("test2.txt");

        indexTest.remove("test.txt");

        System.out.println("tester done");

    }
}

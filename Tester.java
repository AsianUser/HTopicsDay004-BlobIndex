import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;

public class Tester {
    public static void main(String[] args) throws Exception {

        Tree tree = new Tree();

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

        String expectedHash = "eb6d9a8158b62c74f4be79e2b89d964239964861";

        tree.addDirectory("advancedTest");
        // assertEquals(tree.testString, expectedText);

        assertEquals(expectedHash, tree.directoryHash);

    }
}

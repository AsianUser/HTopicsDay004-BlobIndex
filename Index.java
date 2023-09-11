
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Index {

    // define path to folder I want to index - this is ok
    String testFolderPath = ".\\test";

    File indexFile;
    String indexPath = "";
    String objectsFolderPath = "";

    public Index() {

    }

    public void init() throws Exception {
        indexFile = new File(testFolderPath, "index");
        indexFile.delete();
        indexFile.createNewFile();

        indexPath = indexFile.getPath();
        indexFile.createNewFile();

        // create objects folder
        File objectsFolder = new File(testFolderPath, "objects");
        objectsFolder.mkdirs();
        objectsFolderPath = objectsFolder.getPath();
    }

    public void add(File file) {

    }

    public static void main(String[] args) throws Exception {
        Index indexTest = new Index();

        indexTest.init();

        System.out.println("tester done");
    }
}


import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Index {

    // To-do: must ask about how to find target location
    String indexPath = ".\\test";

    public Index() {

    }

    public void init() throws Exception {
        // create index file
        File indexFile = new File(indexPath, "index");
        indexFile.createNewFile();

        // create objects folder
        File objectsFolder = new File(indexPath, "objects");
        objectsFolder.mkdirs();
    }

    public void add(File file) {

    }

    public static void main(String[] args) throws Exception {
        Index indexTest = new Index();

        indexTest.init();

        System.out.println("tester done");
    }
}

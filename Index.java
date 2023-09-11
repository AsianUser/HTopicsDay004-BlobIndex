
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

    public void add(String fileName) throws Exception {
        Blob addBlob = new Blob(testFolderPath + "\\" + fileName);

        String hash = addBlob.getHashString();

        // write to index
        FileWriter fw = new FileWriter(indexPath, true);
        fw.write(fileName + " : " + hash + "\n");

        System.out.println(fileName + " : " + hash + "\n");

        fw.close();
    }

    public void remove(String fileName) throws Exception {

        // look for fileName, takes hash, and delete line

        System.out.println(indexFile.canRead());
        BufferedReader bf = new BufferedReader(new FileReader(indexFile));

        // boolean found = false;
        String text = "";
        while (bf.ready()) {
            System.out.println("ready");
            String line = bf.readLine();
            // System.out.println(line.length());

            // locate name
            int index = 0;
            while (line.charAt(index) != ':' && index < line.length()) {
                // System.out.print(line.charAt(index) + "]]]");
                index++;
            }

            if (!fileName.equals(line.substring(0, index - 1))) {
                // System.out.println(line.substring(0, index - 1));
                text += line + "\n";
            }
        }

        // delete indexfile & rewrite in new
        indexFile.delete();
        indexFile.createNewFile();

        FileWriter fw = new FileWriter(indexFile);
        System.out.println("write");
        fw.write(text);

        bf.close();
        fw.close();

    }

    public static void main(String[] args) throws Exception {
        Index indexTest = new Index();

        indexTest.init();

        indexTest.add("test.txt");
        indexTest.add("duplicate.txt");
        indexTest.add("test2.txt");

        indexTest.remove("test.txt");

        System.out.println("tester done");
    }
}

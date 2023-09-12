
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Index {

    // define path to folder I want to index - this is ok
    String testFolderPath = ".\\test";

    File indexFile;
    String indexPath = ""; // path to index.txt
    String objectsFolderPath = "";

    public Index() {
        // ._.
    }

    public void init() throws Exception {
        // makes index file - if exists, delete then remake
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
        // make new blob & file in obj folder
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

        // System.out.println(indexFile.canRead());
        BufferedReader bf = new BufferedReader(new FileReader(indexFile));

        // this will be put into new index file
        String text = "";

        while (bf.ready()) {
            // System.out.println("ready");
            String line = bf.readLine();
            // System.out.println(line.length());

            // locate name
            int index = 0;
            while (line.charAt(index) != ':' && index < line.length()) {
                // System.out.print(line.charAt(index) + "]]]");
                index++;
            }

            // builds string to put in new indexfile
            if (!fileName.equals(line.substring(0, index - 1))) {
                // System.out.println(line.substring(0, index - 1));
                text += line + "\n";
            }
        }

        // delete indexfile & rewrite in new indexfile
        // might be better way to do, but "me no want think"
        indexFile.delete();
        indexFile.createNewFile();

        FileWriter fw = new FileWriter(indexFile);
        // System.out.println("write");
        fw.write(text);

        bf.close();
        fw.close();

    }

}

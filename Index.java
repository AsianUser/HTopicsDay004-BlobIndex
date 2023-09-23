
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Index {

    File indexFile;
    String indexPath; // path to index.txt
    String objectsFolderPath;

    public Index() throws IOException {
        // ._.
        objectsFolderPath = "./test/objects/";

        Path tP = Paths.get("test");
        if (!Files.exists(tP))
            Files.createDirectories(tP);

        Path oP = Paths.get(objectsFolderPath);
        if (!Files.exists(oP))
            Files.createDirectories(oP);

        // makes index file - if exists, delete then remake
        indexFile = new File("test", "index");
        indexFile.delete();
        indexFile.createNewFile();

        indexPath = indexFile.getPath();
    }

    public void add(String fileName) throws Exception {
        // make new blob & file in obj folder
        File file = new File(fileName);
        Blob addBlob = new Blob(file);

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

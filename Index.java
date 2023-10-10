
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * This class is basically all the file presently in the commit
 * As you edit your files, it changes stuff here
 */
public class Index {

    File indexFile;
    String indexPath; // path to index.txt
    String objectsFolderPath = "objects/";

    private ArrayList<String> contents;

    public Index() throws Exception {
        init();
    }

    public void init() throws Exception {

        // init vars
        contents = new ArrayList<>();

        // creates objects folder if not already existant
        Path oP = Paths.get(objectsFolderPath);
        if (!Files.exists(oP))
            Files.createDirectories(oP);

        // makes index file - if exists, delete then remake
        indexFile = new File("Tree");
        indexFile.delete();
        indexFile.createNewFile();

        indexPath = indexFile.getPath();
    }

    // adds new entry to index file
    public void add(String fileName) throws Exception {
        // make new blob & file in obj folder
        File file = new File(fileName);
        // Blob addBlob = new Blob(file);

        // String hash = addBlob.getHashString();

        String entry;

        // write to index
        if (file.isDirectory()) {
            Tree t = new Tree();
            t.addDirectory(fileName);
            String hash = t.directoryHash;
            entry = "tree : " + hash + " : " + fileName;
        } else {
            Blob addBlob = new Blob(file);

            String hash = addBlob.getHashString();
            entry = "blob : " + hash + " : " + fileName;
        }

        // if not already inside arrayList
        if (!contents.contains(entry))
            contents.add(entry);

        // writes to index file
        FileWriter fw = new FileWriter(indexFile, true);
        if (indexFile.length() != 0)
            fw.write("\n");
        fw.write(entry);

        // System.out.println(fileName + " : " + hash + "\\n");

        fw.close();
    }

    public void addDirectory(String filePath) throws Exception {

        File f = new File(filePath);
        if (!f.isDirectory())
            throw new Exception("not a valid directory");

        Tree t = new Tree();
        String treeHash = t.addDirectory(filePath);

        FileWriter fw = new FileWriter(indexFile);
        if (indexFile.length() != 0)
            fw.write("\n");
        fw.append("tree : " + treeHash + " : " + filePath);
        fw.close();
    }

    public void remove(String fileName) throws Exception {

        // look for fileName, takes hash, and delete line

        // removes from arrayList
        String entry = fileName + " : " + FileUtils.hash(FileUtils.readFile(new File(fileName)));
        contents.remove(entry);

        // System.out.println(indexFile.canRead());
        BufferedReader bf = new BufferedReader(new FileReader(indexFile));

        // this will be put into new index file
        StringBuilder text = new StringBuilder("");

        while (bf.ready()) {
            String line = bf.readLine();

            // if the line does not contain the fileName, add it to String builder

            if (!line.contains(fileName)) {
                if (text.length() > 0)
                    text.append("\n");
                text.append(line);
            }

            // // locate name
            // int index = 0;
            // while (line.charAt(index) != ':' && index < line.length()) {
            // // System.out.print(line.charAt(index) + "]]]");
            // index++;
            // }

            // // builds string to put in new indexfile
            // if (!fileName.equals(line.substring(0, index - 1))) {
            // // System.out.println(line.substring(0, index - 1));
            // text += line + "\n";
            // }
        }

        bf.close();

        // delete indexfile & rewrite in new indexfile
        indexFile.delete();
        indexFile.createNewFile();

        FileWriter fw = new FileWriter(indexFile);
        // System.out.println("write");
        fw.write(text.toString());

        fw.close();

    }

}

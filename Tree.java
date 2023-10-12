
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.security.*;

public class Tree {
    private ArrayList<String> entries;
    String combinedContents = "";
    File folder;
    // File treeDoc;
    String treeHash;

    String directoryHash;

    public Tree() throws Exception {

        entries = new ArrayList<String>();

        File tree = new File("Tree");
        tree.createNewFile();

        // set treeHash
        treeHash = FileUtils.hash(FileUtils.readFile(tree));
    }

    // appends text into Tree file
    public void addLine(String line) throws Exception {
        File f = new File("Tree");
        FileWriter fw = new FileWriter(f, true);
        if (f.length() > 0) {
            fw.append("\n");
        }
        fw.write(line);
        fw.close();

        // update SHA
        treeHash = FileUtils.hash(FileUtils.readFile(new File("Tree")));
    }

    /**
     * The LOPEZ Suggestion
     * 
     * add()
     * accept either a filename or a tree hash as a param
     * 
     * "tree : HASH : folderName"
     * 
     */
    public void add(String input) throws Exception {

        // check if type of input
        // if is a "_ _ _ _ : hash : name", check objects folder for this file.
        // if exists, just toss line right in. if not exists, error
        // else, treat as file

        File addFile = new File(input);

        // if input is not a fileName
        if (!addFile.exists()) {
            // check if "tree : " || "blob : "
            String prefix = input.substring(0, 7);
            String hashString = input.substring(7, 47);
            System.out.println(prefix + "\n" + hashString);
            File blobbedFile = new File("objects", hashString);
            // if already preformatted, toss right in
            if ((prefix.equals("tree : ") || prefix.equals("blob : ")) && blobbedFile.exists()) {
                addLine(input);
            } else {
                // does not except "hash" or "hash : name"
                throw new Exception("Invalid input");
            }

        }
        // if input is a fileName
        else {
            String entry = "";

            // if is a file
            if (addFile.isFile()) {

                // create blob
                Blob blob = new Blob(input);
                // create entry for Tree file
                entry = "blob : " + blob.getHashString() + " : " + blob.getFileName();
            }
            // if is a directory, calls addDirectory
            else if (addFile.isDirectory()) {

                entry = "tree : " + addDirectory(input) + " : " + input;
            }

            // writes into Tree file
            addLine(entry);
        }

    }

    // can take fileName, fileHash, or both if properly formatted
    public void remove(String input) throws Exception {

        StringBuilder stringBuild = new StringBuilder("");

        // look through Tree file
        BufferedReader buffReader = new BufferedReader(new FileReader("Tree"));

        while (buffReader.ready()) {
            String readLine = buffReader.readLine();

            String readHash = readLine.substring(7, 47);
            String readName = readLine.substring(50);

            // check if readLine's fileName, fileHash, or both match input
            if (!(input.equals(readHash) || input.equals(readName) || input.equals(readHash + " : " + readName))) {
                // make sure format good
                if (stringBuild.length() != 0)
                    stringBuild.append("\n" + readLine);
                else {
                    stringBuild.append(readLine);
                }
            }
        }

        buffReader.close();

        // rewrite Tree file without the removed Blob

        FileWriter fileWrite = new FileWriter("Tree");

        fileWrite.write(stringBuild.toString());

        fileWrite.close();

        // update SHA
        treeHash = FileUtils.hash(FileUtils.readFile(new File("Tree")));

    }

    // creates blob with current Tree file
    // returns the hash of the blob
    public String save() throws Exception {
        Blob tempBlob = new Blob("Tree");
        System.out.println(tempBlob.fileContents);
        treeHash = tempBlob.hash;
        return treeHash;
    }

    // returns hash / saved Tree location in obj folder
    public String getTreeHash() throws Exception {
        treeHash = FileUtils.hash(FileUtils.readFile(new File("Tree")));
        return treeHash;
    }

    // generates Blob for input folder & nested folders/files
    // returns hash of directory
    public String addDirectory(String dirPath) throws Exception {
        System.out.println("adddir");

        File folder = new File(dirPath);

        if (!folder.exists()) {
            System.out.println(folder.getAbsolutePath());
            throw new Exception("InvalidDirectoryPath");
        }

        // creates hash of directory
        // saves it
        directoryHash = traverseDirectory(folder);

        // adds results to tree file
        addLine("tree : " + directoryHash + " : " + folder.getName());

        System.out.println("/________\n" + FileUtils.readFile(new File("Tree")) + "\n_________/");

        return directoryHash;
    }

    // recursion time
    // returns the hash of the directory we input
    private String traverseDirectory(File folder) throws Exception {

        // contains all the files/trees collected
        // will use to create a tree at end
        ArrayList<String> treeProxy = new ArrayList<String>();

        for (File f : folder.listFiles()) {
            System.out.println(f.getAbsolutePath());

            if (f.isDirectory()) {
                // System.out.println("is directory");
                System.out.println(f.getName() + " " + traverseDirectory(f));

                // create tree & blob it
                String hash = traverseDirectory(f);

                String entry = "tree : " + hash + " : " + f.getName();
                treeProxy.add(entry);

            } else if (f.isFile()) {
                // System.out.println("not directory");

                // make blob
                Blob blobF = new Blob(f);

                String entry = "blob : " + blobF.getHashString() + " : " + f.getName();

                System.out.println(entry);

                treeProxy.add(entry);

            }
        }

        // manually create Blob using treeProxy
        StringBuilder treeProxyContents = new StringBuilder("");

        for (String s : treeProxy) {
            if (treeProxyContents.length() != 0) {
                treeProxyContents.append("\n");
            }
            treeProxyContents.append(s);
        }
        String treeProxyHash = FileUtils.hash(treeProxyContents.toString());

        // create Blob file
        File treeProxyBlob = new File("objects", treeProxyHash);

        FileWriter fileWriter = new FileWriter(treeProxyBlob);
        fileWriter.write(treeProxyContents.toString());
        fileWriter.close();

        System.out.println("treeproxy contents: \n" + treeProxyContents + "\n\ntreeproxy hash: \n" + treeProxyHash);

        return treeProxyHash;

    }

    // ------------------------------------------

    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
        System.out.println(tree.addDirectory("advancedTest"));
        System.out.println("\n\n" + tree.getPreHashDirectory());

    }

    // only for testing purposes
    String preHashDirectory;
    String testString;

    public String getPreHashDirectory() {
        return preHashDirectory;
    }

}

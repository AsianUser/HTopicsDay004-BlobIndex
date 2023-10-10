
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
    File treeDoc;
    String treeHash;

    String directoryHash;

    public Tree() throws Exception {

        entries = new ArrayList<String>();

        File file = new File("Tree");
        // if (!file.exists()) {
        file.delete();
        file.createNewFile();
        // }
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

        // check if input is a fileName
        File fileToAdd = new File(input);
        String newEntryForTree;

        // if input is not fileName, make sure the file mentioned exists
        if (!fileToAdd.exists()) {
            String fileType = input.substring(0, 6);

            if (fileType.equals("tree : ") || fileType.equals("blob : ")) {

                throw new FileNotFoundException("invalid add file");
            }
        }

        if (fileToAdd.isFile()) { // if is a file
            // write to Tree File
            String fileContent = FileUtils.readFile(fileToAdd);
            String hashOfFile = FileUtils.hash(fileContent);

            newEntryForTree = "blob : " + hashOfFile + " : " + fileToAdd.getName();

            // write this to Tree
            addLine(newEntryForTree);

        } else { // if is a directory
            String hashOfFile = addDirectory(input);

            newEntryForTree = "tree : " + hashOfFile + " : " + fileToAdd.getName();

            addLine(newEntryForTree);
        }

        // add to arraylist
        entries.add(newEntryForTree);

        // update SHA
        treeHash = FileUtils.hash(FileUtils.readFile(new File("Tree")));

    }

    public void remove(String input) throws Exception {

        StringBuilder sb = new StringBuilder("");
        BufferedReader br = new BufferedReader(new FileReader("Tree"));

        // goes through entire Tree file
        while (br.ready()) {
            String line = br.readLine();

            // does not include line with "input"
            if (!line.contains(input)) {
                sb.append(line);
            } else {
                // remove from arraylist
                entries.remove(line);
            }

        }
        br.close();

        // creates new Tree file wihtout the removed
        FileWriter fw = new FileWriter("Tree");
        fw.write(sb.toString());
        fw.close();

        // update SHA
        treeHash = FileUtils.hash(FileUtils.readFile(new File("Tree")));
    }

    // creates blob with current Tree file
    public void generateBlob() throws Exception {
        Blob tempBlob = new Blob("Tree");
        treeHash = tempBlob.hashFileString;
    }

    // returns saved Tree location in obj folder
    public String getTreeHash() {
        return treeHash;
    }

    public String addDirectory(String dirPath) throws Exception {
        System.out.println("adddir");

        File folder = new File(dirPath);

        if (!folder.exists()) {
            System.out.println(folder.getAbsolutePath());
            throw new Exception("InvalidDirectoryPath");
        }
        // creates Tree of directory
        Tree dir = traverseDirectory(folder);
        // saves to obj folder
        dir.generateBlob();
        // get the name of di
        directoryHash = (dir.getTreeHash());

        return directoryHash;
    }

    // recursion time
    private Tree traverseDirectory(File folder) throws Exception {

        // System.out.println(folder.getPath());

        // StringBuilder sb = new StringBuilder("");
        // contains all the files/trees collected
        // will use to create a tree at end
        ArrayList<String> contents = new ArrayList<String>();

        for (File f : folder.listFiles()) {
            System.out.println(f.getAbsolutePath());

            if (f.isDirectory()) {
                // System.out.println("isDirectory");
                System.out.println(f.getName() + " " + traverseDirectory(f));

                // create new tree obj
                Tree childTree = traverseDirectory(f);
                // create blob
                childTree.generateBlob();
                String entry = "tree : " + childTree.getTreeHash() + " : " + f.getName();
                contents.add(entry);

            } else if (f.isFile()) {
                // System.out.println("not directory");
                System.out.println("blob : " + FileUtils.hash(FileUtils.readFile(f)) + " : "
                        + f.getName());
                String entry = "blob : " + FileUtils.hash(FileUtils.readFile(f)) + " : " + f.getName();
                contents.add(entry);

            }
        }

        // create & return a Tree
        Tree temp = new Tree();
        for (String entry : contents) {
            temp.addLine(entry);
        }
        if (contents.size() == 0) {
            temp.addLine("");
        }

        Tree returned = new Tree();
        System.out.println("tree : " + temp.getTreeHash() + " : " + folder.getName());
        returned.addLine("tree : " + temp.getTreeHash() + " : " + folder.getName());

        // System.out.println("returned");
        return returned;

    }

    public String allConents() throws Exception {
        StringBuilder record = new StringBuilder("");

        BufferedReader text = new BufferedReader(new FileReader("Tree"));

        while (text.ready()) {
            record.append((char) text.read());
        }

        text.close();
        return record.toString();
    }

    // checks and updates/recreates Tree file
    // also creates blob in objects folder
    public void save() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String s : entries) {
            sb.append(s + "\n");
        }

        File temp = new File("Tree");
        temp.delete();
        temp.createNewFile();

        FileWriter fw = new FileWriter(temp);
        fw.write(sb.toString());

        // creates a new blob
        generateBlob();
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

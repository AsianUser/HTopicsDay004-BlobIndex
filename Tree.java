
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
    private List<String> entries;
    String combinedContents = "";
    File folder;
    File treeDoc;
    String treeHash;

    public Tree() throws Exception {
        File file = new File("Tree");
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void addLine(String line) throws Exception {
        FileWriter fw = new FileWriter("Tree", true);
        fw.write(line);
        fw.close();
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
            }

        }
        br.close();

        // creates new Tree file
        FileWriter fw = new FileWriter("Tree");
        fw.write(sb.toString());
        fw.close();
    }

    // creates blob with current Tree file
    public void generateBlob() throws Exception {
        Blob tempBlob = new Blob("Tree");
        treeHash = tempBlob.hashFileString;
    }

    public String getTreeHash() {
        return treeHash;
    }

    public String addDirectory(String dirPath) throws Exception {

        File folder = new File(dirPath);

        // creates text as if in tree file
        String str = traverseDirectory(folder);

        String directoryHash = FileUtils.hash(str);

        return directoryHash;
    }

    // recursion time
    private String traverseDirectory(File folder) throws Exception {

        if (!folder.exists())
            throw new Exception("InvalidDirectoryPath");

        // System.out.println(folder.getPath());

        StringBuilder sb = new StringBuilder("");

        for (File f : folder.listFiles()) {
            // System.out.println(f.getAbsolutePath());

            if (f.isDirectory()) {
                // System.out.println("isDirectory");
                System.out.println(f.getName() + " " + traverseDirectory(f));
                sb.append(traverseDirectory(f) + "\n");
            } else {
                // System.out.println("not directory");
                System.out.println("blob : " + FileUtils.hash(Blob.readFile(f)) + " : "
                        + f.getName() + "\n");
                sb.append("blob : " + FileUtils.hash(Blob.readFile(f)) + " : " + f.getName() + "\n");
            }
        }

        // remove last "\n"
        if (sb.length() > 1)
            sb.setLength(sb.length() - 1);

        testString = sb.toString();

        // return sb.toString();
        // System.out.println("/n------------/n" + sb.toString());

        String returnStr = "tree : " + FileUtils.hash(sb.toString()) + " : " +
                folder.getName();

        return returnStr;
    }

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

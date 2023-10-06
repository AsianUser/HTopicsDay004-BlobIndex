
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
    String fileName;
    String combinedContents = "";
    File folder;
    File treeDoc;

    public Tree() throws Exception {
        File file = new File("Tree");
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    // < fileName , fileSha >
    HashMap<String, String> blobs = new HashMap<String, String>();

    public Tree(String placeHolderSoNoErrorPlsDeleteLater) throws Exception {

        // String directoryHash;

        // public Tree() throws NoSuchAlgorithmException {

        // for (String entry : entries) {
        // combinedContents += entry + "\n";
        // }
        // fileName = generateSHA(combinedContents);

        // File file = new File("objects/" + fileName);
        folder = new File("./Tree-Objects");
        folder.mkdir();
        treeDoc = new File("./Tree-Objects/Tree");
        PrintWriter pw = new PrintWriter(treeDoc);

        pw.close();

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

    public void add(String fileName) throws Exception {

        File fileToAdd = new File(fileName);
        // check if taking in file or smth else
        if (!fileToAdd.exists()) {
            String isTree = fileName.substring(0, 6);
            if (isTree.equals("tree : ")) {
                throw new FileNotFoundException("invalid add file");
            }
        }

        if (fileToAdd.exists()) { // is file
            // write to Tree File
            String fileContent = FileUtils.readFile(fileToAdd);
            String hashOfFile = FileUtils.hash(fileContent);

            String newEntryForTree = "blob : " + hashOfFile + " : " + fileName;

            // write this to Tree
            FileUtils.writeFile(newEntryForTree, "Tree", true);

        } else { // is directory

        }

        // -----------------------------------------------------------------

        // Blob blob = new Blob(name);

        // String contents = blob.getFileContent();
        // blobs.put(name, Commit.generateSHA(contents));
        // printBlobs();
        // -----------------------------------------
        // // File file = new File("objects/" + fileName);
        // }

        // // not sure what this is meant to accomplish
        // public void save() {
        // for (String entry : entries) {
        // combinedContents += entry + "\n";
        // }
        // fileName = FileUtils.hash(combinedContents);
        // File file = new File("objects/" + fileName);

    }

    public void remove(String fileName) {
        blobs.remove(fileName);
        printBlobs();

    }

    public void printBlobs() {
        try {
            PrintWriter pw = new PrintWriter(
                    "Tree");

            String s = "";
            for (HashMap.Entry<String, String> entry : blobs.entrySet()) {
                s += entry.getKey() + " : " + entry.getValue() + "\n";
            }

            pw.print(s);
            pw.close();

        } catch (Exception e) {

        }

    }

    // public void add(String entry) throws NoSuchAlgorithmException, IOException {
    // File file = new File(entry);
    // // entries.add(entry);
    // combinedContents += entry + "\n";
    // // fileName = generateSHA(combinedContents);
    // writeToFile("Tree");
    // }

    // public void remove(String name) throws NoSuchAlgorithmException, IOException
    // {
    // entries.removeIf(entry -> {
    // String[] parts = entry.split(" : ");
    // if (parts.length >= 3) {
    // String typeOfFile = parts[0];
    // String shaOfFile = parts[1];
    // String optionalFileName = parts[2];
    // return shaOfFile.equals(name) || optionalFileName.equals(name);
    // }
    // return false;
    // });
    // for (String entry : entries) {
    // combinedContents += entry + "\n";
    // }
    // fileName = generateSHA(combinedContents);
    // writeToFile(fileName);
    // }

    public void writeToFile(String fileName) throws IOException, NoSuchAlgorithmException {
        String combinedContents = "";
        for (String entry : entries) {
            combinedContents += entry + "\n";
        }
        File file = new File("Tree-Objects/" + fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(combinedContents);
        }

    }

    public String allConents() throws IOException { // gotten from Chris' code when you assigned him as partner eaerlier

        StringBuilder record = new StringBuilder("");

        // FileReader fr = new FileReader ()
        BufferedReader text = new BufferedReader(new FileReader("Tree"));

        while (text.ready()) {
            record.append((char) text.read());
        }

        text.close();
        return record.toString();
    }

    public static String generateSHA(String input) throws NoSuchAlgorithmException {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * expected behavior / Tester example
     * add file 1
     * add file 2
     * remove file 1
     * generateBlob ()
     * 
     */

    public void generateBlob() throws Exception {
        Blob blob = new Blob(fileName);
    }

    // only for testing purposes
    String preHashDirectory;
    String testString;

    public String getPreHashDirectory() {
        return preHashDirectory;
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

}

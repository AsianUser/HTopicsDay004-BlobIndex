
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    private List<String> entries;
    String fileName;
    String combinedContents = "";

    String directoryHash;

    public Tree() throws NoSuchAlgorithmException {
        // for (String entry : entries) {
        // combinedContents += entry + "\n";
        // }
        // fileName = generateSHA(combinedContents);
        // File file = new File("objects/" + fileName);
    }

    // not sure what this is meant to accomplish
    public void save() {
        for (String entry : entries) {
            combinedContents += entry + "\n";
        }
        fileName = Blob.writeHashString(combinedContents);
        File file = new File("objects/" + fileName);
    }

    public void add(String entry) throws NoSuchAlgorithmException, IOException {
        entries.add(entry);
        combinedContents += entry;
        fileName = generateSHA(combinedContents);
        writeToFile(fileName);
    }

    public void remove(String name) throws NoSuchAlgorithmException, IOException {
        entries.removeIf(entry -> {
            String[] parts = entry.split(" : ");
            if (parts.length >= 3) {
                String typeOfFile = parts[0];
                String shaOfFile = parts[1];
                String optionalFileName = parts[2];
                return shaOfFile.equals(name) || optionalFileName.equals(name);
            }
            return false;
        });
        for (String entry : entries) {
            combinedContents += entry + "\n";
        }
        fileName = generateSHA(combinedContents);
        writeToFile(fileName);
    }

    public void writeToFile(String fileName) throws IOException, NoSuchAlgorithmException {
        String combinedContents = "";
        for (String entry : entries) {
            combinedContents += entry + "\n";
        }
        File file = new File("objects/" + fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(combinedContents);
        }
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

        directoryHash = Blob.writeHashString(str);

        // testing purposes only:
        preHashDirectory = str;

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
                System.out.println("blob : " + Blob.writeHashString(Blob.readFile(f)) + " : "
                        + f.getName() + "\n");
                sb.append("blob : " + Blob.writeHashString(Blob.readFile(f)) + " : " + f.getName() + "\n");
            }
        }

        // remove last "\n"
        if (sb.length() > 1)
            sb.setLength(sb.length() - 1);

        testString = sb.toString();

        // return sb.toString();
        // System.out.println("/n------------/n" + sb.toString());

        String returnStr = "tree : " + Blob.writeHashString(sb.toString()) + " : " +
                folder.getName();

        return returnStr;
    }

    public String getDirectoryHash() {
        return directoryHash;
    }

    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
        System.out.println(tree.addDirectory("advancedTest"));
        System.out.println("\n\n" + tree.getPreHashDirectory());

    }

}

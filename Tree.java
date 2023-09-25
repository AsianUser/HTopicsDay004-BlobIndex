
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

    HashMap<String, String> blobs = new HashMap();

    public Tree() throws NoSuchAlgorithmException, FileNotFoundException {
        // for (String entry : entries) {
        // combinedContents += entry + "\n";
        // }
        // fileName = generateSHA(combinedContents);

        // File file = new File("objects/" + fileName);
        folder = new File("Tree-Objects");
        folder.mkdir();
        treeDoc = new File("./Tree-Objects/Tree");
        PrintWriter pw = new PrintWriter(treeDoc);

    }

    // used my code to fix

    public void add(String name) throws Exception {
        Blob blob = new Blob("/Users/lilbarbar/Desktop/Honors Topics/Bens-Amazing-Git/" + name);

        String contents = blob.fileContent;
        blobs.put(name, Commit.generateSHA(contents));
        printBlobs();
    }

    public void remove(String fileName) {
        blobs.remove(fileName);
        printBlobs();

    }

    public void printBlobs() {
        try {
            PrintWriter pw = new PrintWriter(
                    "/Users/lilbarbar/Desktop/Honors Topics/Bens-Amazing-Git/Tree-Objects/Tree");

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
    // File file = new File("/Users/lilbarbar/Desktop/Honors
    // Topics/Bens-Amazing-Git/Tree-Objects/" + entry);
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

    public String allConents() {

        BufferedReader reader;
        String out = "";

        try {
            reader = new BufferedReader(
                    new FileReader("/Users/lilbarbar/Desktop/Honors Topics/Bens-Amazing-Git/Tree-Objects/Tree"));
            String line = reader.readLine();

            while (line != null) {
                out += line + '\n';
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
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
}

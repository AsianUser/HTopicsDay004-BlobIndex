
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    private List<String> entries;
    String fileName;
    String combinedContents = "";
    File folder;
    File treeDoc;

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

    public void add(String entry) throws NoSuchAlgorithmException, IOException {
        File file = new File("/Users/lilbarbar/Desktop/Honors Topics/Bens-Amazing-Git/Tree-Objects/" + entry);
        // entries.add(entry);
        combinedContents += entry + "\n";
        // fileName = generateSHA(combinedContents);
        writeToFile("Tree");
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
        File file = new File("Tree-Objects/" + fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(combinedContents);
        }
    }

    public String allConents() {
        return combinedContents;
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

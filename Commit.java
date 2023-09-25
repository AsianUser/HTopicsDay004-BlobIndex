import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Commit {

    private String author;
    private String summary;
    private String prevSHA;
    private String nextSHA;
    private Tree tree = new Tree();

    public Commit(String author, String summary) throws Exception {

        this.author = author;
        this.summary = summary;
        // tree.add("test.txt");
        // tree.generateBlob();

    }

    public Commit(String author, String summary, String prevSHA) throws Exception {

        this(author, summary);
        this.prevSHA = prevSHA;

    }

    public static String getDate() {
        Date date = new Date();
        return date.toString();
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

    public void commitFile() throws FileNotFoundException, NoSuchAlgorithmException {
        String currentSHA = generateSHA(tree.allConents());
        PrintWriter pw = new PrintWriter("/Users/lilbarbar/Desktop/Honors Topics/Bens-Amazing-Git/Tree-Objects/Commit");

        pw.print("Current: " + currentSHA + "\n");
        if (prevSHA != null) {
            pw.print("Previous: " + prevSHA + "\n");

        }
        if (nextSHA != null) {
            pw.print("Next: " + nextSHA + "\n");

        }
        pw.print(author + "\n" + getDate() + "\n" + summary);

        pw.close();

    }

}
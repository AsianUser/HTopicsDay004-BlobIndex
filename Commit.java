import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.ArrayList;
import java.security.*;

public class Commit {

    private String author;
    private String summary;
    private String prevSHA;
    private String currentSHA;
    private String nextSHA;
    private Tree tree = new Tree();
    private int indexOfCurrent;

    ArrayList<String> hashes = new ArrayList<>();

    private int totalCommits = 0;
    private int commitIndex = 0;

    public Commit(String author, String summary) throws Exception {

        this.author = author;
        this.summary = summary;
        // tree.add("test.txt");
        // tree.generateBlob();

        indexOfCurrent = 0;

    }

    public Commit(String author, String summary, String prevSHA) throws Exception {

        this(author, summary);
        this.prevSHA = prevSHA;
        hashes.add(prevSHA);
        indexOfCurrent = 1;

    }

    public static String getDate() {
        Date date = new Date();
        return date.toString();
    }

    public String hashesToString() {
        return hashes.toString();
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

    public void writeFile() throws FileNotFoundException {
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

    public void commitFile() throws NoSuchAlgorithmException, IOException {
        currentSHA = generateSHA(tree.allConents() + summary + getDate() + author);
        hashes.add(currentSHA);
        indexOfCurrent = hashes.indexOf(currentSHA);

        if (indexOfCurrent == hashes.size() - 1) {
            nextSHA = null;
        } else {
            nextSHA = hashes.get(indexOfCurrent + 1);
        }

        if (indexOfCurrent == 0) {
            prevSHA = null;
        } else {
            prevSHA = hashes.get(indexOfCurrent - 1);
        }

    }

    public void seeNext() throws FileNotFoundException {
        if (indexOfCurrent == hashes.size() - 1) {
            nextSHA = null;
        } else {
            indexOfCurrent++;
        }

        if (indexOfCurrent == hashes.size() - 1) {
            nextSHA = null;
        } else {
            nextSHA = hashes.get(indexOfCurrent + 1);
        }

        if (indexOfCurrent == 0) {
            prevSHA = null;
        } else {
            prevSHA = hashes.get(indexOfCurrent - 1);
        }

    }

    public void seePrev() throws FileNotFoundException {
        if (indexOfCurrent == 0) {
            prevSHA = null;
        } else {
            indexOfCurrent--;
        }
        currentSHA = hashes.get(indexOfCurrent);

        if (indexOfCurrent == hashes.size() - 1) {
            nextSHA = null;
        } else {
            nextSHA = hashes.get(indexOfCurrent + 1);
        }

        if (indexOfCurrent == 0) {
            prevSHA = null;
        } else {
            prevSHA = hashes.get(indexOfCurrent - 1);
        }

    }

    public void add(String newFile) throws Exception {
        tree.add(newFile);
    }

}
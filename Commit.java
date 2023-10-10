import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.ArrayList;
import java.security.*;

public class Commit {

    private String author = "";
    private String summary = "";
    private String date;
    private String prevCommitSHA = "";
    private String currentCommitSHA = "";
    private String nextCommitSHA = "";
    private int indexOfCurrent;

    ArrayList<String> hashes = new ArrayList<>();

    private int totalCommits = 0;
    private int commitIndex = 0;

    private Tree commitTree;
    private String treeHash;

    // finished product for this commit
    private File commitFile;

    // no prev commit
    public Commit(String author, String summary) throws Exception {

        this.author = author;
        this.summary = summary;
        // tree.add("test.txt");
        // tree.generateBlob();
        treeHash = createTree();

    }

    // has prev commit
    public Commit(String author, String summary, String prevSHA) throws Exception {

        this(author, summary);
        this.prevCommitSHA = prevSHA;

        treeHash = createTree();

    }

    public void setAuthor(String input) {
        author = input;
    }

    public void setSummary(String input) {
        summary = input;
    }

    // gets date
    public static String getDate() {
        Date date = new Date();
        return date.toString().substring(0, 11) + "2023";
    }

    // create Tree & return its hash
    private String createTree() throws Exception {
        // create new Tree
        commitTree = new Tree();
        // read index
        BufferedReader bf = new BufferedReader(new FileReader("Index"));
        while (bf.ready()) {
            // reads whats in index into the Tree file
            commitTree.addLine(bf.readLine());
        }
        bf.close();
        // return
        return commitTree.getTreeHash();
    }

    public String hashesToString() {
        return hashes.toString();
    }

    // actually commit to obj file
    public void commit() throws Exception {
        // gets current sha
        currentCommitSHA = createTree();
        // gets date - this way date is consistent
        date = getDate();

        // ----------------

        hashes.add(currentCommitSHA);

        indexOfCurrent = hashes.indexOf(currentCommitSHA);

        if (indexOfCurrent == hashes.size() - 1) {
            nextCommitSHA = null;
        } else {
            nextCommitSHA = hashes.get(indexOfCurrent + 1);
        }

        if (indexOfCurrent == 0) {
            prevCommitSHA = null;
        } else {
            prevCommitSHA = hashes.get(indexOfCurrent - 1);
        }

        // -------------

        // creates file to put into objects folder;
        createCommitContent();
        commitFile = new File("objects", currentCommitSHA);

        // make new file
        commitFile.createNewFile();

        // write contents
        FileWriter fw = new FileWriter(commitFile);
        fw.write(createCommitContent());
        fw.close();

    }

    private void createCurrentCommitSHA() throws Exception {
        if (treeHash == null) {
            throw new Exception("Hash of tree is null");
        }
        // hashes --> current commit sha
        // skips over line 3
        currentCommitSHA = FileUtils.hash(treeHash + "\n" + prevCommitSHA + "\n" + author + "\n" + date + "\n"
                + summary);
        // return currentCommitSHA;
    }

    private String createCommitContent() throws Exception {
        if (treeHash == null) {
            throw new Exception("Hash of tree is null");
        }
        // return the text within the current Commit file
        return (treeHash + "\n" + prevCommitSHA + "\n" + nextCommitSHA + "\n" + author + "\n" + date + "\n"
                + summary);
    }

    /**
     * 
     * TRAVERSAL METHODS
     * 
     */

    public void seeNext() throws FileNotFoundException {
        if (indexOfCurrent >= hashes.size() - 1) {
            nextCommitSHA = null;
        } else {
            indexOfCurrent++;
        }

        currentCommitSHA = hashes.get(indexOfCurrent);

        if (indexOfCurrent == hashes.size() - 1) {
            nextCommitSHA = null;
        } else {
            nextCommitSHA = hashes.get(indexOfCurrent + 1);
        }

        if (indexOfCurrent == 0) {
            prevCommitSHA = null;
        } else {
            prevCommitSHA = hashes.get(indexOfCurrent - 1);
        }

    }

    public void seePrev() throws FileNotFoundException {
        if (indexOfCurrent == 0) {
            // move back if we can...

        } else {
            indexOfCurrent--;
        }

        currentCommitSHA = hashes.get(indexOfCurrent);

        if (indexOfCurrent == hashes.size() - 1) {
            nextCommitSHA = null;
        } else {
            nextCommitSHA = hashes.get(indexOfCurrent + 1);
        }

        if (indexOfCurrent == 0) {
            prevCommitSHA = null;
        } else {
            prevCommitSHA = hashes.get(indexOfCurrent - 1);
        }

    }

    // public String makeTree() throws NoSuchAlgorithmException, IOException {
    // tree = new Tree();
    // return generateSHA(tree.allConents());
    // }

}
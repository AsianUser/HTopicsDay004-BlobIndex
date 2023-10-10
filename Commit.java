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
import java.text.SimpleDateFormat;
import java.util.*;
import java.security.*;

public class Commit {

    private String author = "";
    private String summary = "";
    private String date;
    private String prevCommitSHA;
    private String currentCommitSHA = "";
    private String nextCommitSHA = "";

    private int indexOfCurrent;
    ArrayList<String> hashes = new ArrayList<>();

    // private int totalCommits = 0;
    // private int commitIndex = 0;

    private Tree commitTree;
    private String treeHash;

    // point to file for this commit in objects folder
    File commitFile;

    // no prev commit
    public Commit(String author, String summary) throws Exception {

        this.author = author;
        this.summary = summary;
        treeHash = createTree();

    }

    // has prev commit
    public Commit(String author, String summary, String prevSHA) throws Exception {

        this(author, summary);
        this.prevCommitSHA = prevSHA;
        System.out.println("constuct prevcom " + prevCommitSHA);
        treeHash = createTree();

        // HEAD file thing
        File head = new File("head");
        head.createNewFile();
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

    public String getCurrentCommitSHA() {
        return currentCommitSHA;
    }

    // create Tree & return its hash
    // for an "empty file"
    private String createTree() throws Exception {
        // create new Tree
        commitTree = new Tree();
        // read index
        BufferedReader bf = new BufferedReader(new FileReader("index"));
        while (bf.ready()) {
            // reads whats in index into the Tree file
            commitTree.addLine(bf.readLine());
        }
        bf.close();

        // if has parent commit
        if (prevCommitSHA != null && prevCommitSHA.length() > 0) {
            String prevCommitContent = FileUtils.readFile(new File("objects/" + prevCommitSHA));
            String prevCommitTree = prevCommitContent.substring(0, 40);

            commitTree.addLine("tree : " + prevCommitTree);
        }

        // return
        commitTree.generateBlob();
        treeHash = commitTree.getTreeHash();
        System.out.println(treeHash);
        return treeHash;
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

        // if (indexOfCurrent == 0) {
        // prevCommitSHA = null;
        // } else {
        // prevCommitSHA = hashes.get(indexOfCurrent - 1);
        // }

        // -------------

        // creates file to put into objects folder;
        commitFile = new File("objects", currentCommitSHA);
        System.out.println("prevcomsha - " + prevCommitSHA);

        // make new file
        commitFile.createNewFile();

        // write contents
        FileWriter fw = new FileWriter(commitFile);
        fw.write(createCommitFileContent());
        fw.close();

        // clear out index file post commit
        Index i = new Index();
        i.init();

        // update previous Commit (if possible)
        if (prevCommitSHA != null && prevCommitSHA.length() > 0) {
            // locates previous commit
            System.out.println("debug " + prevCommitSHA);
            File prevCommit = new File("objects", prevCommitSHA);
            StringBuilder sb = new StringBuilder("");
            BufferedReader br = new BufferedReader(new FileReader(prevCommit));
            int line = 1;
            // updates line 3 to have nextCommit
            while (br.ready()) {
                if (line == 1)
                    sb.append(br.readLine());
                else if (line != 3) {
                    sb.append("\n" + br.readLine());
                } else {
                    br.readLine();
                    sb.append("\n" + currentCommitSHA);
                }

                line++;
            }
            br.close();

            // replaces old commit with new contents
            fw = new FileWriter(prevCommit);
            fw.write(sb.toString());
            fw.close();
        }
        // update Head file
        File head = new File("head");
        fw = new FileWriter(head);
        fw.write(currentCommitSHA);
        fw.close();

    }

    private void createCurrentCommitSHA() throws Exception {
        if (treeHash == null) {
            throw new Exception("Hash of tree is null");
        }
        // hashes --> current commit sha
        // skips over line 3
        currentCommitSHA = FileUtils.hash(treeHash + "\n" + ((prevCommitSHA == null) ? ""
                : prevCommitSHA) + "\n" + author + "\n" + date + "\n"
                + summary);
        // return currentCommitSHA;
    }

    private String createCommitFileContent() throws Exception {
        if (treeHash == null) {
            throw new Exception("Hash of tree is null");
        }
        // return the text within the current Commit file
        return (treeHash + "\n" + ((prevCommitSHA == null) ? ""
                : prevCommitSHA) + "\n"
                + ((nextCommitSHA == null) ? ""
                        : nextCommitSHA)
                + "\n" + author + "\n" + date + "\n"
                + summary);
    }

    // gets Commit's Tree based on Commit's SHA1
    public String getTree(String SHA1) throws Exception {
        // returns line 1
        BufferedReader br = new BufferedReader(new FileReader("objects/" + SHA1));
        if (br.ready()) {
            String firstLine = br.readLine();
            br.close();
            return firstLine;
        } else {
            br.close();
            throw new Exception("SHA1 invalid");
        }
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

        // if (indexOfCurrent == 0) {
        // prevCommitSHA = null;
        // } else {
        // prevCommitSHA = hashes.get(indexOfCurrent - 1);
        // }

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

        // if (indexOfCurrent == 0) {
        // prevCommitSHA = null;
        // } else {
        // prevCommitSHA = hashes.get(indexOfCurrent - 1);
        // }

    }

    // public String makeTree() throws NoSuchAlgorithmException, IOException {
    // tree = new Tree();
    // return generateSHA(tree.allConents());
    // }

}
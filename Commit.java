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
    // hash of Tree file
    private String treeHash;
    private String nextCommitSHA = "";

    // name of Commit Blob
    private String currentCommitSHA = "";

    // point to file - not in objects folder
    File commitFile;
    File commitBlobFile;
    String commitFileContents;

    // point to Tree file
    Tree tree = new Tree();
    // point to head file
    File head;

    // no prev commit
    public Commit(String author, String summary) throws Exception {

        this.author = author;
        this.summary = summary;
        treeHash = hashTree();

        // HEAD file
        head = new File("head");
        head.createNewFile();

        // check if there is a prev Commit or not
        BufferedReader buffReader = new BufferedReader(new FileReader(head));
        if (buffReader.ready()) {
            prevCommitSHA = buffReader.readLine();
        }
        buffReader.close();

        // create Commit file - not in objects folder
        commitFile = new File("commit");
        commitFile.createNewFile();

        // write latest info into it
        String commitContent = updateCommitFileContent();
        FileWriter fileWrite = new FileWriter(commitFile);
        fileWrite.write(commitContent);
        fileWrite.close();

    }

    // has prev commit
    public Commit(String author, String summary, String prevSHA) throws Exception {

        this(author, summary);
        this.prevCommitSHA = prevSHA;
        System.out.println("constuct prevcom " + prevCommitSHA);
        treeHash = hashTree();

        // update head file
        head = new File("head");
        head.createNewFile();

        FileWriter fileWrite = new FileWriter(head);
        fileWrite.write(prevSHA);
        fileWrite.close();

        // create Commit file - not in objects folder
        commitFile = new File("commit");
        commitFile.createNewFile();

        // write latest info into it
        String commitContent = updateCommitFileContent();
        fileWrite = new FileWriter(commitFile);
        fileWrite.write(commitContent);
        fileWrite.close();
    }

    // Setters & Getters
    public void setAuthor(String input) {
        author = input;
    }

    public void setSummary(String input) {
        summary = input;
    }

    // updates date ("weekday month day year")
    public String setDate() {
        date = new Date().toString().substring(0, 11) + "2023";
        return date;

    }

    // returns saved date
    public String getDate() {
        return date;
    }

    public String getCurrentCommitSHA() {
        return currentCommitSHA;
    }

    // return hash of Tree file
    private String hashTree() throws Exception {

        // read index & adjust Tree accordingly
        /**
         * // read index & adjust Tree accordingly
         * BufferedReader bf = new BufferedReader(new FileReader("Tree"));
         * while (bf.ready()) {
         * // reads whats in index into the Tree file
         * tree.addLine(bf.readLine());
         * }
         * bf.close();
         */

        /**
         * // if has parent commit, update Tree file to include
         * if (prevCommitSHA != null) {
         * String prevCommitContent = FileUtils.readFile(new File("objects/" +
         * prevCommitSHA));
         * String prevCommitTree = prevCommitContent.substring(0, 40);
         * 
         * // go through the old Tree file and add everything into current one
         * tree.addLine("tree : " + prevCommitTree);
         * }
         */

        // return
        // note: does not blobify yet
        treeHash = tree.getTreeHash();
        System.out.println(treeHash);
        return treeHash;
    }

    // actually commit to obj file
    public void commit() throws Exception {

        // gets current sha
        currentCommitSHA = updateCurrentCommitSHA();

        // blobs Tree file
        tree.save();

        // creates Blob from commit file
        Blob commitBlob = new Blob(commitFile);
        // point to current Blob file
        commitBlobFile = new File("objects", currentCommitSHA);

        // update previous Commit (if possible)
        if (prevCommitSHA != null) {
            // locates previous commit
            System.out.println("debug " + prevCommitSHA + " + " + currentCommitSHA);
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

            // rewrites prev commit
            FileWriter fw = new FileWriter(prevCommit);
            fw.write(sb.toString());
            fw.close();
        }

        // update Head file
        File head = new File("head");
        FileWriter fw = new FileWriter(head);
        fw.write(currentCommitSHA);
        fw.close();

    }

    // hashes commit file
    private String updateCurrentCommitSHA() throws Exception {
        updateCommitFileContent();

        currentCommitSHA = FileUtils.hash(commitFileContents);

        return currentCommitSHA;
    }

    // updates commit file with proper data & returns the new contents
    private String updateCommitFileContent() throws Exception {

        // gets date
        date = setDate();

        // updates treeHash
        treeHash = tree.getTreeHash();

        commitFileContents = (treeHash + "\n" +
                ((prevCommitSHA == null) ? "" : prevCommitSHA) + "\n"
                + ((nextCommitSHA == null) ? "" : nextCommitSHA) + "\n"
                + author + "\n" + date + "\n" + summary);
        // updates commit file
        FileWriter fileWrite = new FileWriter(commitFile);
        fileWrite.write(commitFileContents);
        fileWrite.close();

        // return the text for the current Commit file
        return commitFileContents;
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
     * CheckOut Method
     * - if made, can skip Honors Track
     */

    public static void checkOut(String commitHash) {

    }

    /**
     * 
     * TRAVERSAL METHODS
     * 
     */

    // public void seeNext() throws FileNotFoundException {
    // if (indexOfCurrent >= hashes.size() - 1) {
    // nextCommitSHA = null;
    // } else {
    // indexOfCurrent++;
    // }

    // currentCommitSHA = hashes.get(indexOfCurrent);

    // if (indexOfCurrent == hashes.size() - 1) {
    // nextCommitSHA = null;
    // } else {
    // nextCommitSHA = hashes.get(indexOfCurrent + 1);
    // }

    // // if (indexOfCurrent == 0) {
    // // prevCommitSHA = null;
    // // } else {
    // // prevCommitSHA = hashes.get(indexOfCurrent - 1);
    // // }

    // }

    // public void seePrev() throws FileNotFoundException {
    // if (indexOfCurrent == 0) {
    // // move back if we can...

    // } else {
    // indexOfCurrent--;
    // }

    // currentCommitSHA = hashes.get(indexOfCurrent);

    // if (indexOfCurrent == hashes.size() - 1) {
    // nextCommitSHA = null;
    // } else {
    // nextCommitSHA = hashes.get(indexOfCurrent + 1);
    // }

    // // if (indexOfCurrent == 0) {
    // // prevCommitSHA = null;
    // // } else {
    // // prevCommitSHA = hashes.get(indexOfCurrent - 1);
    // // }

    // }

    // public String makeTree() throws NoSuchAlgorithmException, IOException {
    // tree = new Tree();
    // return generateSHA(tree.allConents());
    // }

}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

    static boolean testMode = false;

    // help me with testing
    static boolean setTestMode() throws Exception {

        return testMode;
    }

    // Git: day 2
    static int countCharacters(String fileName) throws Exception {
        int charCount = 0;
        BufferedReader buffReader = new BufferedReader(new FileReader(fileName));
        while (buffReader.ready()) {
            buffReader.read();
            charCount++;
        }
        buffReader.close();
        return charCount;
    }

    static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Boolean isFirst = true;
        while (br.ready()) {
            line = br.readLine();
            if (isFirst) {
                sb.append(line);
                isFirst = false;
            } else
                sb.append("\n" + line);
        }
        br.close();
        return sb.toString();

    }

    static void writeFile(String str, File file, boolean append) throws Exception {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file, append);
        fw.write(str);
        fw.close();
    }

    static void writeFile(String str, String fileName, boolean append) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(new File(fileName), append);
        fw.write(str);
        fw.close();
    }

    static String hash(String input) throws Exception {
        // Create a MessageDigest instance for SHA-1
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        // Convert the input string to bytes using UTF-8 encoding
        byte[] bytes = input.getBytes("UTF-8");

        // Update the MessageDigest with the input bytes
        sha1.update(bytes);

        // Generate the SHA-1 hash
        byte[] hashBytes = sha1.digest();

        // Convert the hash bytes to a hexadecimal representation
        StringBuilder hexHash = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexHash.append("0");
            }
            hexHash.append(hex);
        }

        // Return the hexadecimal SHA-1 hash
        String thing = hexHash.toString();
        while (thing.length() < 40) {
            thing = "0" + thing;
        }
        return thing;

    }

    // deletes a directory with files inside
    static void deleteDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (dir.listFiles() == null) {
            dir.delete();
        } else {
            for (File f : dir.listFiles()) {
                f.delete();
                if (f.isDirectory())
                    deleteDirectory(f.getPath());
            }
            dir.delete();
        }
    }

    // deletes a file
    static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    static void createFile(String fileName) throws Exception {
        File f = new File(fileName);
        f.delete();
        f.createNewFile();
    }

    static void createDirectory(String dirName) throws Exception {
        File dir = new File(dirName);
        deleteDirectory(dirName);
        dir.mkdir();
    }

    // intakes a hash & recreates all files according to the ver in that hash
    static void checkout(String targetCommitHash) throws Exception {
        // locate the commit
        File targetCommitFile = new File("objects", targetCommitHash);
        System.out.println("targetCommitFile: " + targetCommitFile.getPath());
        System.out.println(FileUtils.readFile(targetCommitFile));

        if (!targetCommitFile.exists()) {
            throw new Exception("invalid targetCommitHash");
        }

        BufferedReader buffReader = new BufferedReader(new FileReader(targetCommitFile));

        String targetTreeHash = buffReader.readLine();

        buffReader.close();

        traverse("", targetTreeHash);

    }

    // recursively move through a tree
    static void traverse(String parentPath, String tree) throws Exception {
        // read the hash tree inside of objects folder
        BufferedReader buffReader = new BufferedReader(new FileReader(new File("objects", tree)));

        while (buffReader.ready()) {
            // identify type, hash, and fileName
            String readLine = buffReader.readLine();

            String type = readLine.substring(0, 4);
            String hash = readLine.substring(7, 47);
            String fileName = readLine.substring(50);

            // if blob
            // find file of fileName
            // replace contents with contents of hash
            if (type.equals("blob")) {

                // file outside of objects
                File targetFile;
                // System.out.println(fileName);

                if (!parentPath.equals("")) {
                    File parentFile = new File(parentPath);
                    parentFile.mkdir();

                    targetFile = new File(parentPath, fileName);
                    targetFile.createNewFile();
                } else {
                    // point to currently existing file
                    targetFile = new File(fileName);
                    targetFile.createNewFile();
                }

                // copy contents of hashed file to target file
                FileWriter fileWriter = new FileWriter(targetFile);

                String originalText = readFile(new File("objects", hash));

                System.out.println("blobFile: " + targetFile.getPath());
                System.out.println("targetFile contents " + readFile(targetFile));
                System.out.println("orig: " + originalText);

                fileWriter.write(originalText);
                fileWriter.close();
            }

            // if dir
            // traverse it
            else if (type.equals("tree")) {

                File dirFile = new File(parentPath, fileName);
                dirFile.mkdir();

                System.out.println("traverse recusion");

                // the "/" at the front of the path throws everything off
                traverse(dirFile.getPath().substring(1), hash);
            }

        }

        buffReader.close();
    }

}

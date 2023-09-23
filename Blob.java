
// all sha-1 imports
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Blob {

    String hashFileString, fileContent, folderPath, fileName;

    public Blob(File originalFile) throws Exception {
        fileName = originalFile.getName();
        hashFileString = getHashString(fileName);

        // // takes path & locates parent's path
        // // removes chars until locates "\\"
        // int i = filePath.length() - 1;
        // while (filePath.charAt(i) != '\\' && i > 0) {
        // // System.out.println(path.charAt(i));
        // i--;
        // }
        // // removes 2 chars
        // // System.out.println(path.substring(0, i));
        // String objectsFolderPath = filePath.substring(0, i);

        // creates folder if does not exist
        // makes directory if no exist
        folderPath = "objects/";
        Path oP = Paths.get(folderPath); // creates Path
        if (!Files.exists(oP)) // creates file if directory doesnt exist
            Files.createDirectories(oP); // creates Path
        // File objectsFolder = new File(folderPath, "objects");
        // objectsFolder.mkdirs();
        File file = new File(folderPath + hashFileString);
        Path hP = Paths.get(hashFileString);
        if (!Files.exists(hP))
            file.createNewFile();

        // // hash
        // // hashFileString = hash(new File(filePath));

        // // create file
        // String blobFile = objectsFolderPath + "\\objects\\" + hashFileString +
        // ".txt";
        // // System.out.println(blobFile);
        // File blob = new File(blobFile);
        // // if exists, wont create
        // blob.createNewFile();

        // // write text to file
        copyData(originalFile, file);

    }

    public String getHashString() {
        return hashFileString;
    }

    public String getFileContent() {
        return fileContent;
    }

    private String getHashString(String input) {
        try {
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
            return hexHash.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // Handle exceptions (e.g., NoSuchAlgorithmException,
            // UnsupportedEncodingException)
            e.printStackTrace();
            return null;
        }
    }

    // copies data/text from one method to the next
    public void copyData(File fromFile, File toFile) throws Exception {
        String fileData = "";

        // creates string
        BufferedReader buffReader = new BufferedReader(new FileReader(fromFile));
        while (buffReader.ready()) {
            int chara = buffReader.read();
            fileData += (char) chara;
        }

        buffReader.close();

        // actually writes
        PrintWriter pw = new PrintWriter(toFile);
        pw.write(fileData);

        pw.close();
    }

}
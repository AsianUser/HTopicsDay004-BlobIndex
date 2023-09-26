
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.nio.file.*;

public class Blob {

    String hashFileString, fileContents, folderPath, fileName;

    public Blob(File originalFile) throws Exception {
        fileName = originalFile.getName();
        fileContents = readFile(originalFile);
        hashFileString = writeHashString(fileContents);
        folderPath = "objects/";

        Path oP = Paths.get(folderPath);
        if (!Files.exists(oP))
            Files.createDirectories(oP);

        File file = new File(folderPath + hashFileString);
        Path hP = Paths.get(hashFileString);
        if (!Files.exists(hP))
            file.createNewFile();

        copyData(file);

    }

    // file name ver
    public Blob(String fileName) throws Exception {

        File originalFile = new File(fileName);

        fileName = originalFile.getName();
        fileContents = readFile(originalFile);
        hashFileString = writeHashString(fileContents);
        folderPath = "objects/";

        Path oP = Paths.get(folderPath);
        if (!Files.exists(oP))
            Files.createDirectories(oP);

        File file = new File(folderPath + hashFileString);
        Path hP = Paths.get(hashFileString);
        if (!Files.exists(hP))
            file.createNewFile();

        copyData(file);

    }

    public String getHashString() {
        return hashFileString;
    }

    public String getFileContent() {
        return fileContents;
    }

    // copies data/text from one method to the next
    private void copyData(File toFile) throws Exception {
        PrintWriter pw = new PrintWriter(toFile);
        pw.write(fileContents);
        pw.close();
    }

    private String writeHashString(String input) {
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

    private String readFile(File file) throws IOException {
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

}
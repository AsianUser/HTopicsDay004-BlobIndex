
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
        FileWriter fw = new FileWriter(file, append);
        fw.write(str);
        fw.close();
    }

    static void writeFile(String str, String fileName, boolean append) throws Exception {
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
        return hexHash.toString();

    }
}

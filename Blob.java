
// all sha-1 imports
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Blob {
    public Blob(String filePath) throws Exception {

        // takes path & locates parent's path
        // removes chars until locates "\\"
        int i = filePath.length() - 1;
        while (filePath.charAt(i) != '\\') {
            // System.out.println(path.charAt(i));
            i--;
        }
        // removes 2 chars
        // System.out.println(path.substring(0, i));
        String objectsFolderPath = filePath.substring(0, i);

        // creates folder if does not exist
        File theDir = new File(objectsFolderPath, "objects");
        theDir.mkdirs();

        // hash
        String hash = hash(new File(filePath));

    }

    public String hash(File file) throws Exception {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];

            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;

            byte[] mdbytes = md.digest();

            // convert the byte to hex format
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }

            fis.close();

            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) throws Exception {
        Blob test = new Blob(
                ".\\test\\test.txt");
    }
}
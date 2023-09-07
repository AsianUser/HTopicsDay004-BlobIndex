
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

    }

    public static void main(String[] args) throws Exception {
        Blob test = new Blob(
                ".\\test\\test.txt");
    }
}
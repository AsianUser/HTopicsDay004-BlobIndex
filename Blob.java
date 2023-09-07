
// all sha-1 imports
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Blob {

    String hashFileString;

    public Blob(String filePath) throws Exception {

        // takes path & locates parent's path
        // removes chars until locates "\\"
        int i = filePath.length() - 1;
        while (filePath.charAt(i) != '\\' || i >= 0) {
            // System.out.println(path.charAt(i));
            i--;
        }
        // removes 2 chars
        // System.out.println(path.substring(0, i));
        String objectsFolderPath = filePath.substring(0, i);

        // creates folder if does not exist
        File objectsFolder = new File(objectsFolderPath, "objects");
        objectsFolder.mkdirs();

        // hash
        hashFileString = hash(new File(filePath));

        // create file
        String blobFile = objectsFolderPath + "\\objects\\" + hashFileString + ".txt";
        System.out.println(blobFile);
        File blob = new File(blobFile);
        // if exists, wont create
        blob.createNewFile();

        // write text to file
        copyData(new File(filePath), blob);

    }

    public String getHashString() {
        return hashFileString;
    }

    // To-do: ask is this ho wthis is supposed to work
    public String hash(File file) throws Exception {

        // hashes an entire file, not just insides
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

    public void copyData(File fromFile, File toFile) throws Exception {
        String fileData = "";

        BufferedReader buffReader = new BufferedReader(new FileReader(fromFile));
        while (buffReader.ready()) {
            int chara = buffReader.read();
            fileData += (char) chara;
        }

        buffReader.close();

        PrintWriter pw = new PrintWriter(toFile);
        // prepare
        pw.write(fileData);

        pw.close();
    }

    public static void main(String[] args) throws Exception {
        Blob test = new Blob(
                ".\\test\\test.txt");
    }
}
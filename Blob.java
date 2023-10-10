
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.nio.file.*;

public class Blob {

    String hashFileString, fileContents, folderPath, fileName;

    // Parameter: File
    public Blob(File originalFile) throws Exception {

        fileName = originalFile.getName();
        fileContents = FileUtils.readFile(originalFile);
        hashFileString = FileUtils.hash(fileContents);
        folderPath = "objects/";

        // write text to file
        Path oP = Paths.get(folderPath);
        if (!Files.exists(oP))
            Files.createDirectories(oP);

        File file = new File(folderPath + hashFileString);
        Path hP = Paths.get(hashFileString);
        if (!Files.exists(hP))
            file.createNewFile();

        copyData(file);

    }

    // Parameter: String
    public Blob(String fileName) throws Exception {

        File originalFile = new File(fileName);

        fileName = originalFile.getName();
        fileContents = FileUtils.readFile(originalFile);
        hashFileString = FileUtils.hash(fileContents);
        folderPath = "objects/";

        // write text to file
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

    // static String readFile(File file) throws IOException {
    // StringBuilder sb = new StringBuilder();
    // BufferedReader br = new BufferedReader(new FileReader(file));
    // String line;
    // Boolean isFirst = true;
    // while (br.ready()) {
    // line = br.readLine();
    // if (isFirst) {
    // sb.append(line);
    // isFirst = false;
    // } else
    // sb.append("\n" + line);
    // }
    // br.close();
    // return sb.toString();

    // }

    // public static String getSHA1(String input) throws NoSuchAlgorithmException {

    // MessageDigest mDigest = MessageDigest.getInstance("SHA1");
    // byte[] result = mDigest.digest(input.getBytes());
    // StringBuffer sb = new StringBuffer();
    // for (int i = 0; i < result.length; i++) {
    // sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    // }

    // return sb.toString();
    // }

}

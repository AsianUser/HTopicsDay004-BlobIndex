
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.nio.file.*;

public class Blob {

    String hash, fileContents, objectsFolderPath, fileName;

    // Parameter: File
    public Blob(File originalFile) throws Exception {

        fileName = originalFile.getName();
        fileContents = FileUtils.readFile(originalFile);
        hash = FileUtils.hash(fileContents);
        objectsFolderPath = "objects/";

        // creates objects folder
        Path oP = Paths.get(objectsFolderPath);
        if (!Files.exists(oP))
            Files.createDirectories(oP);

        File file = new File(objectsFolderPath, hash);
        Path hP = Paths.get(hash);
        if (!Files.exists(hP))
            file.createNewFile();

        // write text to file
        copyData(file);

    }

    // Parameter: String
    public Blob(String fileName) throws Exception {

        File originalFile = new File(fileName);

        this.fileName = fileName;
        fileContents = FileUtils.readFile(originalFile);
        hash = FileUtils.hash(fileContents);
        objectsFolderPath = "objects/";

        // creates objects folder
        Path oP = Paths.get(objectsFolderPath);
        if (!Files.exists(oP))
            Files.createDirectories(oP);

        File file = new File(objectsFolderPath, hash);
        Path hP = Paths.get(hash);
        if (!Files.exists(hP))
            file.createNewFile();

        // write text to file
        copyData(file);

    }

    public String getHashString() {
        return hash;
    }

    public String getFileContent() {
        return fileContents;
    }

    public String getFileName() {
        return fileName;
    }

    // public String getSHAString

    // copies data/text from one method to the next
    private void copyData(File toFile) throws Exception {
        PrintWriter pw = new PrintWriter(toFile);
        pw.write(fileContents);
        pw.close();
    }

}

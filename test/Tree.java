package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    private List<String> entries;

    public Tree() {
        entries = new ArrayList<>();
    }

    public void add(String entry) {
        entries.add(entry);
    }

    public void remove(String name) {
        entries.removeIf(entry -> entry.contains(name));
    }

    public void writeToFile(String fileName) throws IOException, NoSuchAlgorithmException {
        File file = new File("objects/" + fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < entries.size(); i++) {
                writer.write(entries.get(i));
                if (i < entries.size() - 1) {
                    writer.newLine();
                }
            }
        }
    }
}

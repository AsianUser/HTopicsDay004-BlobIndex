package test;

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

}

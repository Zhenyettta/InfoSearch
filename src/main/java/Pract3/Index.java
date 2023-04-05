package Pract3;

import java.io.*;
import java.util.*;

import static Pract2.Main2Task.mapPutter;

public class Index {
    public Map<String, List<String>> map1;
    public Map<String, String> map;
    public Index() {
        File folder = new File("src/main/java/texts/important");
        System.out.println(folder.isDirectory());
        map = Collections.synchronizedMap(new TreeMap<>());
        List<File> files = Arrays.stream(folder.listFiles()).sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replaceAll("[a-zA-Z]+|\\.", "")))).toList();

        for (File file : files) {
            Scanner scan;
            try {
                scan = new Scanner(file);
                map1 = mapPutter(file, scan, map);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

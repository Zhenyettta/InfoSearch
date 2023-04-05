package Pract2;

import java.io.*;
import java.util.*;

import static Pract2.NormalToPoland.createPolandReverse;


public class Main2Task {
    public static void main(String[] args) throws IOException {
        File folder = new File("src/main/java/texts/important");
        File fileOutput = new File("src/main/java/texts/vocabulary.txt");
        List<File> files = Arrays.stream(folder.listFiles()).sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replaceAll("[a-zA-Z]+|\\.", "")))).toList();
        Map<String, String> map = Collections.synchronizedMap(new TreeMap<>());
        fileOutput.delete();
        fileOutput.createNewFile();


        for (File file : files) {
            Scanner scan;
            try {
                scan = new Scanner(file);
                mapPutter(file, scan, map);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileOutput), 1200000)) {
            map.forEach((s, v) -> {
                try {
                    bufferedWriter.write(s + " " + v + System.lineSeparator());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        booleanSearch(map);
    }

    public static Map<String, List<String>> mapPutter(File file, Scanner scan, Map<String, String> map) {
        Map<String, List<String>> map1 = new HashMap<>();
        while (scan.hasNext()) {
            String word = scan.next();
            word = word.replaceAll("[\"\\[\\]()<+.\\-}{ …=—„,:%;―?“!*»№«”_/]+|-(?!\\S)|–+| |^-|^'", "");
            word = word.toLowerCase();
            if (map.containsKey(word) && !map.get(word).contains(file.getName()))
                map.replace(word, map.get(word) + " " + file.getName());
            else if (!map.containsKey(word) && !word.isEmpty()) map.put(word, file.getName());
        }
        map.forEach((s, v) -> map1.put(s, List.of(v.split(" "))));

        return map1;
    }

    private static void booleanSearch(Map<String, String> map) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Input request");
            String request = scan.nextLine();
            createPolandReverse(request, map);
        }
    }
}




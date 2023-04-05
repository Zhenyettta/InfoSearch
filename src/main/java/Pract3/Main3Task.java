package Pract3;

import java.io.*;
import java.util.*;

public class Main3Task {
    static String first_word;
    static String second_word;


    public static void main(String[] args) throws IOException {
        File folder = new File("src/texts/important");
        File fileOutput = new File("src/texts/vocabulary_2Words.txt");
        Map<String, String> map = Collections.synchronizedMap(new TreeMap<>());
        List<File> files = Arrays.stream(folder.listFiles()).sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replaceAll("[a-zA-Z]+|\\.", "")))).toList();
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
        System.out.println(fileOutput.length() / 1024 + "KB");
        System.out.println(map.size() + " words");
        phraseSearch(map);
    }


    private static void mapPutter(File file, Scanner scan, Map<String, String> map) {
        while (scan.hasNext()) {
            try {
                if (first_word == null) {
                    first_word = scan.next();
                }
                second_word = scan.next();

                first_word = first_word.replaceAll("[\"\\[\\]()<+.\\-}{ …=—„,:%;―?“!*»№«”_/]+|-(?!\\S)|–+| |^-|^'", "");
                second_word = second_word.replaceAll("[\"\\[\\]()<+.\\-}{ …=—„,:%;―?“!*»№«”_/]+|-(?!\\S)|–+| |^-|^'", "");
                first_word = first_word.toLowerCase();
                second_word = second_word.toLowerCase();
                String word = first_word + " " + second_word;
                if (second_word.isBlank() || first_word.isBlank()) continue;
                if (map.containsKey(word) && map.containsKey(word) && !map.get(word).contains(file.getName()))
                    map.replace(word, map.get(word) + " " + file.getName());
                else if (!map.containsKey(word)) map.put(word, file.getName());
                first_word = second_word;
            } catch (NoSuchElementException ignored) {

            }
        }
    }

    private static void phraseSearch(Map<String, String> map) {
        Scanner scan = new Scanner(System.in);
        while (true) {
        System.out.println("Input request");
        String request = scan.nextLine().toLowerCase();
        System.out.println(map.getOrDefault(request, "No such phrase"));
        }
    }
}




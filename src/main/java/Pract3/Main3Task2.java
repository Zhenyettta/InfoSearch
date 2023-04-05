package Pract3;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Main3Task2 {

    static int pos;
    static Map<String, String> map;

    public static Map<String, String> getMap() throws IOException {
        main(new String[0]);
        return map;
    }

    public static LinkedHashMap<String, ArrayList<Integer>> getPostingMapFromFile(String key) {
        LinkedHashMap<String, ArrayList<Integer>> newMap = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("text\\d+.txt:");
        String file = null;
        String term = map.get(key);
        String[] line = term.split(" ");
        ArrayList<Integer> posting = new ArrayList<>();
        for (String value : line) {
            if (value.matches(pattern.pattern())) {
                file = value;
                if (posting.size() == 0)
                    continue;
                newMap.put(file.replaceAll(":", ""), posting);
                posting = new ArrayList<>();
                continue;
            }
            posting.add(Integer.valueOf(value));
        }


        newMap.put(file.replaceAll(":", ""), posting);
        return newMap;
    }

    public static void main(String[] args) throws IOException {
        File folder = new File("src/main/java/texts/important");
        File fileOutput = new File("src/main/java/texts/inverted_coord.txt");
        map = new TreeMap<>();
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
//        phraseSearch(map);
    }


    public static void mapPutter(File file, Scanner scan, Map<String, String> map) {
        pos = 0;
        Pattern pattern = Pattern.compile("[\"\\[\\]()<+.\\-}{ …=—„,:%>;#|~\\\\@&^―?\\d“!*»$№«”_/]+|-(?!\\S)|–+| |^-|^'");
        while (scan.hasNext()) {
            String first_word = scan.next();
            StringBuilder builder = new StringBuilder(first_word.length());
            for (char c : first_word.toLowerCase().toCharArray()) {
                if (!pattern.matcher(String.valueOf(c)).matches()) {
                    builder.append(c);
                }
            }
            first_word = builder.toString();

                if (first_word.isBlank()) continue;
                if (map.containsKey(first_word) && !map.get(first_word).contains(file.getName()))
                    map.replace(first_word, map.get(first_word) + " " + file.getName() + ": " + pos);
                else if (map.containsKey(first_word) && map.get(first_word).contains(file.getName()))
                    map.replace(first_word, map.get(first_word) + " " + pos);
                else if (!map.containsKey(first_word)) map.put(first_word, file.getName() + ": " + pos);
                pos++;

        }
    }

    private static void phraseSearch(Map<String, String> map) {
        String regex = "text\\d+.txt";
        Scanner scan = new Scanner(System.in);

        System.out.println("Input request");
        String request = scan.nextLine().toLowerCase();
        String[] words = request.split(" ");

        Map<String, String> positions = getPositions(map, words, regex);
        findOccurrences(positions, words);
    }

    private static Map<String, String> getPositions(Map<String, String> map, String[] words, String regex) {
        Map<String, String> positions = new HashMap<>();

        StringBuilder pos = new StringBuilder();
        for (String word : words) {
            pos.append(map.get(word)).append(" ");
        }
        pos = new StringBuilder(pos.toString().replaceAll(":", ""));

        String[] pairs = pos.toString().split(" ");
        for (int i = 0; i < pairs.length; i++) {
            String key = "";
            if (pairs[i].matches(regex)) key = pairs[i];
            try {
                while (!(pairs[i + 1]).matches(regex)) {
                    i++;
                    if (!positions.containsKey(key)) positions.put(key, pairs[i]);
                    else positions.replace(key, positions.get(key) + " " + pairs[i]);
                }
            } catch (Exception ignored) {
            }
        }
        return positions;
    }

    private static void findOccurrences(Map<String, String> positions, String[] words) {
        positions.forEach((s, v) -> {
            List<String> consecutive = Arrays.stream(v.split(" "))
                    .sorted(Comparator.comparingInt(Integer::parseInt))
                    .toList();

            int consecutiveCount = words.length;
            StringBuilder poss = new StringBuilder();
            for (int i = 0; i < consecutive.size() - consecutiveCount; i++) {
                int value = 0;
                boolean isConsecutive;
                int dif = 1;
                for (int j = i; j < i + consecutiveCount; j++) {
                    if (Integer.parseInt(consecutive.get(i)) + dif == (Integer.parseInt(consecutive.get(j)))) {
                        value = Integer.parseInt(consecutive.get(i));
                        isConsecutive = false;
                        dif++;
                        poss.append(Integer.parseInt(consecutive.get(j))).append(" ");
                    } else {
                        isConsecutive = true;
                        poss = new StringBuilder();
                    }
                    if (!isConsecutive && dif == consecutiveCount) {
                        System.out.println("Found occurrences at " + s + ": " + value + " " + poss);
                        poss = new StringBuilder();
                    }
                }
            }
        });
    }
}




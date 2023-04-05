package Pract2;



import Pract2.NormalToPoland;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainCopy {


    public static void main(String[] args) throws IOException {
        File folder = new File("src/texts/important");
        File fileOutput = new File("src/texts/vocabulary.txt");
        File fileTermDocumentOutput = new File("src/texts/term-document.txt");
        Map<String, String> map = Collections.synchronizedMap(new TreeMap<>());
        List<File> files = Arrays.stream(folder.listFiles()).sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replaceAll("[a-zA-Z]+|\\.", "")))).toList();
        fileOutput.delete();
        fileOutput.createNewFile();
        fileTermDocumentOutput.delete();
        fileTermDocumentOutput.createNewFile();


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

        termDocWriter(folder, fileTermDocumentOutput, map);
        booleanSearch(map);
    }

    private static void termDocWriter(File folder, File fileTermDocumentOutput, Map<String, String> map) {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileTermDocumentOutput)), true)) {
            List<String> files = Arrays.stream(folder.listFiles()).map(File::getName).sorted(Comparator.comparingInt(o -> Integer.parseInt(o.replaceAll("[a-zA-Z]+|\\.", "")))).toList();
            printWriter.printf("%s", "term");
            printWriter.printf("%60s", files.get(0));
            AtomicBoolean check = new AtomicBoolean(true);

            for (int i = 1; i < folder.listFiles().length; i++) {
                printWriter.printf("%15s", files.get(i));
            }
            printWriter.print(System.lineSeparator());

            map.forEach((s, v) -> {
                printWriter.print(s);
                String line = v.replaceAll("[a-zA-Z]+|\\.", "");
                for (int i = 0; i <= 9; i++) {
                    if (check.get()) {
                        if (line.contains(i + 1 + " ")) {
                            printWriter.printf("%" + (59 - s.length()) + "s", "1");
                            check.set(false);
                        } else {
                            printWriter.printf("%" + (59 - s.length()) + "s", "0");
                            check.set(false);
                        }
                    } else {
                        if (line.contains(i + 1 + " ")) {
                            printWriter.printf("%" + 15 + "s", "1");
                        } else {
                            printWriter.printf("%" + 15 + "s", "0");
                        }
                    }
                }
                printWriter.print(System.lineSeparator());
                check.set(true);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mapPutter(File file, Scanner scan, Map<String, String> map) {
        while (scan.hasNext()) {
            String word = scan.next();
            word = word.replaceAll("[\"\\[\\]()<+.\\-}{ …=—„,:%;―?“!*»№«”_/]+|-(?!\\S)|–+| |^-|^'", "");
            word = word.toLowerCase();
            if (map.containsKey(word) && !map.get(word).contains(file.getName()))
                map.replace(word, map.get(word) + " " + file.getName());
            else if (!map.containsKey(word) && !word.isEmpty()) map.put(word, file.getName());
        }
    }

    private static void booleanSearch(Map<String, String> map) {
        Scanner scan = new Scanner(System.in);
//        while (true) {
        System.out.println("Input request");
        String request = scan.nextLine();
        NormalToPoland.createPolandReverse(request, map);
//        }
    }
}




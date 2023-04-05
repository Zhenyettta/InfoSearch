package Pract5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class SPIMI {
    private final long memorySize;
    private int blockNumber;
    private List<String> blockPostingsList;
    public static Map<String, List<String>> dictionary;


    public SPIMI(long memorySize) {
        this.memorySize = memorySize;
    }

    public static void count(Map<String, List<String>> mergedDict) {
        System.out.println("Number of terms : " + mergedDict.size());
    }

    public static List<String> mergeBlocks(Map<String, List<String>> map1, Map<String, List<String>> map2) {
        Set<String> keys1 = map1.keySet();
        Set<String> keys2 = map2.keySet();

        Iterator<String> iter1 = keys1.iterator();
        Iterator<String> iter2 = keys2.iterator();

        List<String> merged = new ArrayList<>();

        String value1 = (iter1.hasNext() ? iter1.next() : null);
        String value2 = (iter2.hasNext() ? iter2.next() : null);

        while (value1 != null || value2 != null) {
            if (value2 == null) {
                merged.add(value1);
                value1 = (iter1.hasNext() ? iter1.next() : null);

            } else if (value1 == null) {
                merged.add(value2);
                value2 = (iter2.hasNext() ? iter2.next() : null);

            } else if (value1.equals(value2)) {
                merged.add(value1);
                value1 = (iter1.hasNext() ? iter1.next() : null);
                value2 = (iter2.hasNext() ? iter2.next() : null);

            } else if (value1.compareTo(value2) <= 0) {
                merged.add(value1);
                value1 = (iter1.hasNext() ? iter1.next() : null);

            } else {
                merged.add(value2);
                value2 = (iter2.hasNext() ? iter2.next() : null);
            }
        }

        return merged;

    }


    public static List<String> mergeOrdered(List<String> list0, List<String> list1) {
        list0.addAll(list1);
        ArrayList<String> res = new ArrayList<>();
        list0.forEach(s -> {
            if (!res.contains(s)) res.add(s);
        });
        return res;
    }


    private static List<String> getPostingsFromLine(String line) {
        List<String> postingsList = new ArrayList<>();
        line = line.replace(']', Character.MIN_VALUE);
        String[] lineComponents = line.split(" : \\[");
        String[] postings = lineComponents[1].split(",");

        for (String s : postings) {
            s = s.trim();
            postingsList.add(s);
        }

        return postingsList;
    }

    public List<String> addToDictionary(Map<String, List<String>> dictionary, String term) {
        List<String> postingsList = new ArrayList<>();
        dictionary.put(term, postingsList);
        return postingsList;
    }

    public void SPIMIAgorithm(File folder) throws FileNotFoundException {
        dictionary = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("[\"\\[\\]()<+.\\-}{ …=—„,:%>;#|~\\\\@&^―?\\d“!*»$№«”_/]+|-(?!\\S)|–+| |^-|^'");
        List<File> files = Arrays.stream(Objects.requireNonNull(folder.listFiles())).sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replaceAll("[a-zA-Z]+|\\.", "")))).toList();
        for (File file : files) {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                if (isMemoryAvailable()) {
                    List<String> postingsList;

                    String word = scan.next();
                    StringBuilder builder = new StringBuilder(word.length());
                    for (char c : word.toLowerCase().toCharArray()) {
                        if (!pattern.matcher(String.valueOf(c)).matches()) {
                            builder.append(c);
                        }
                    }
                    word = builder.toString();
                    if (!dictionary.containsKey(word)) {
                        postingsList = this.addToDictionary(dictionary, word);
                    } else {
                        postingsList = dictionary.get(word);
                    }
                    if (!postingsList.contains(file.getName())) postingsList.add(file.getName());
                } else {
                    sortAndWriteBlockToFile(dictionary);
                }
            }
            Runtime.getRuntime().gc();
            Runtime.getRuntime().gc();
        }
        sortAndWriteBlockToFile(dictionary);
    }


    public void mergeAllBlocks() throws IOException {
         dictionary = new LinkedHashMap<>();
        for (int i = 1; i <= this.blockNumber; i++) {
            Map<String, List<String>> blockDictionary = readBlockAndConvertToDictionary("block" + i + ".txt");
            Map<String, List<String>> mergedBlocks = new LinkedHashMap<>();
            List<String> mergedSortedTerms = mergeBlocks(dictionary, blockDictionary);
            for (String term : mergedSortedTerms) {
                if (dictionary.get(term) != null && blockDictionary.get(term) != null) {
                    mergedBlocks.put(term, mergeOrdered(dictionary.get(term), blockDictionary.get(term)));
                } else if (dictionary.get(term) != null) {
                    mergedBlocks.put(term, dictionary.get(term));
                } else {
                    mergedBlocks.put(term, blockDictionary.get(term));
                }
            }
            dictionary = mergedBlocks;
        }
        writeDictionary(dictionary);
        count(dictionary);
    }

    private void writeDictionary(Map<String, List<String>> dictionary) throws IOException {
        this.blockNumber++;
        Path file = Paths.get("src/main/java/texts/dictionary.txt");
        BufferedWriter writer = Files.newBufferedWriter(file);
        dictionary.remove("");
        List<String> keys = new ArrayList<>(dictionary.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String index = key + " : " + dictionary.get(key).toString();
            writer.write(index);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }


    private void sortAndWriteBlockToFile(Map<String, List<String>> dictionary) {
        this.blockNumber++;
        Path file = Paths.get("src/main/java/texts/blocksSPIMI/block" + this.blockNumber + ".txt");
        dictionary.remove("");
        List<String> keys = new ArrayList<>(dictionary.keySet());
        Collections.sort(keys);

        StringBuilder contentBuilder = new StringBuilder();
        for (String key : keys) {
            Collections.sort(dictionary.get(key));
            String index = key + " : " + dictionary.get(key).toString();
            contentBuilder.append(index).append("\n");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(contentBuilder.toString());
            SPIMI.dictionary.clear();
            Runtime.getRuntime().gc();
            Runtime.getRuntime().gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Map<String, List<String>> readBlockAndConvertToDictionary(String blockFileName) {
        Map<String, List<String>> blockDictionary = new LinkedHashMap<>();
        try (Stream<String> stream = Files.lines(Path.of("src/main/java/texts/blocksSPIMI/" + Paths.get(blockFileName)))) {
            this.blockPostingsList = new ArrayList<>();
            stream.forEach(line -> {
                String term = line.split(" :")[0];
                this.blockPostingsList = getPostingsFromLine(line);
                blockDictionary.put(term, blockPostingsList);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blockDictionary;
    }

    private boolean isMemoryAvailable() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024;
        return usedMemory <= memorySize;
    }
}
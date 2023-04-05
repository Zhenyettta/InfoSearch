package Pract4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ThreeGramIndex {
    private final Map<String, Set<String>> index;

    public ThreeGramIndex(ArrayList<String> dictionary) {
        this.index = new HashMap<>();
        buildIndex(dictionary);
        printIndex("src/main/java/texts/ThreeGramIndex.txt");
    }

    private void buildIndex(ArrayList<String> dictionary) {
        for (String word : dictionary) {
            String paddedWord = "&" + word + "&"; //додаємо на початок і кінець слова &
            for (int i = 0; i < paddedWord.length() - 2; i++) {
                String gram = paddedWord.substring(i, i + 3); //розбиваємо слова на три грами
                if (!index.containsKey(gram)) {
                    index.put(gram, new HashSet<>());
                }
                index.get(gram).add(word);
            }
        }
    }

    public ArrayList<String> searchIndex(List<String> queryIndexes) {
        ArrayList<String> result = new ArrayList<>();
        Set<String> matchedWords = new HashSet<>();
        for (String queryIndex : queryIndexes) { //проходимо по кожному з 3-грам індексу
            Set<String> words = index.getOrDefault(queryIndex, Collections.emptySet()); //знаходимо всі слова, що відповідають індексу
            matchedWords.addAll(words);
        }
        for (String word : matchedWords) { //перебираємо всі слова
            boolean match = true;
            for (String queryIndex : queryIndexes) {
                //перевіряємо чи містяться вони у всіх 3-грамних індексах
                if (!index.getOrDefault(queryIndex, Collections.emptySet()).contains(word)) {
                    match = false;
                    break;
                }
            }
            if (match && !result.contains(word)) {
                result.add(word);
            }
        }
        return result;
    }

    public ArrayList<String> createIndex(String query) {
        ArrayList<String> queryIndex = new ArrayList<>();
        query = query.toLowerCase();
        query = "&" + query + "&";
        for (int i = 0; i < query.length() - 2; i++) {
            String trigram = query.substring(i, i + 3);
            if (!trigram.contains("*")) {
                queryIndex.add(trigram);
            }
        }
        return queryIndex;
    }

    public void printIndex(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String gram : index.keySet()) {
                writer.write(gram + ": ");
                Set<String> words = index.get(gram);
                for (String word : words) {
                    writer.write(word + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
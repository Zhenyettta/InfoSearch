package Pract4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermutedIndex {
    private Map<String, List<String>> index;

    public PermutedIndex(ArrayList<String> dictionary) {
        this.index = new HashMap<>();
        buildPermutationIndex(dictionary);
        writePermutationIndexToFile("src/main/java/texts/permuted.txt");
    }

    private void buildPermutationIndex(List<String> dictionary) {
        index = new HashMap<>();

        for (String word : dictionary) {
            List<String> permutations = new ArrayList<>();
            String wordWithEndSymbol = word + "$"; //додаємо в кінець слова знак
            for (int i = 0; i < wordWithEndSymbol.length(); i++) {
                String permutation = wordWithEndSymbol.substring(i) + wordWithEndSymbol.substring(0, i); //робимо циклічно всі перестановки
                permutations.add(permutation);
            }
            index.put(word, permutations);
        }
    }

    public List<String> search(String query) {
        List<String> result = new ArrayList<>();
        String queryWithEndSymbol = query + "$"; //додаємо в кінець запиту знак
        int starIndex = query.indexOf('*');

        if (starIndex >= 0 && query.indexOf('*', starIndex + 1) >= 0) {
            int secondStarIndex = query.indexOf("*", starIndex + 1);
            String rotatedQuery = queryWithEndSymbol.substring(secondStarIndex + 1) + queryWithEndSymbol.substring(0, starIndex); //циклічна перестановка (перша * в кінець)
            System.out.println(rotatedQuery);
            for (Map.Entry<String, List<String>> entry : index.entrySet()) {
                List<String> permutations = entry.getValue();
                for (String permutation : permutations) {
                    if (permutation.contains(rotatedQuery) && permutation.contains(queryWithEndSymbol.substring(starIndex + 1, secondStarIndex))) { //шукаємо перестановку, що містить запит
                        result.add(entry.getKey()); //додаємо слово до результату
                        break;
                    }
                }
            }
        } else {
            String rotatedQuery = queryWithEndSymbol.substring(starIndex + 1) + queryWithEndSymbol.substring(0, starIndex); //циклічна перестановка
            System.out.println(rotatedQuery);
            for (Map.Entry<String, List<String>> entry : index.entrySet()) {
                List<String> permutations = entry.getValue();
                for (String permutation : permutations) {
                    if (permutation.contains(rotatedQuery)) { //шукаємо перестановку, що містить запит
                        result.add(entry.getKey()); //додаємо слово до результату
                        break;
                    }
                }
            }
        }
        return result;
    }

    public void writePermutationIndexToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, List<String>> entry : index.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
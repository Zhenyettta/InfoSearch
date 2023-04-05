package Pract6;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FrontCompress {
    public static ArrayList<String> compress(ArrayList<String> dict) {
        ArrayList<String> compressed = new ArrayList<>();
        String prev = "";
        int commonPrefixLength;

        for (int i = 0; i < dict.size(); i++) {
            String curr = dict.get(i);
            if (i == 0) {
                compressed.add(curr.length() + curr);
            } else {
                commonPrefixLength = getCommonPrefixLength(prev, curr);
                String suffix = curr.substring(commonPrefixLength);
                compressed.add(commonPrefixLength + "*" + suffix.length() + "$" + suffix);
            }
            prev = curr;
        }
        return compressed;
    }

    public static ArrayList<String> decompress(ArrayList<String> compressed) {
        ArrayList<String> dict = new ArrayList<>();
        String prev = "";

        for (String token : compressed) {
            if (!token.contains("*")) {
                dict.add(token.substring(1));
            } else {
                int starIndex = token.indexOf("*");
                int dollarIndex = token.indexOf("$");
                int prefixLength = Integer.parseInt(token.substring(0, starIndex));
                String prefix = prev.substring(0, prefixLength);
                String suffix = token.substring(dollarIndex + 1);
                dict.add(prefix + suffix);
            }

            prev = dict.get(dict.size() - 1);
        }

        return dict;
    }

    private static int getCommonPrefixLength(String s1, String s2) {
        int i = 0;
        while (i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }

    public static void saveToFile(ArrayList<String> compressed, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);

        for (String token : compressed) {
            writer.write(token);
            writer.write(System.lineSeparator()); // Додати роздільник між записами
        }

        writer.close();
    }
}
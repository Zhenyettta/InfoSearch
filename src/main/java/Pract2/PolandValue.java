package Pract2;

import java.util.*;

public class PolandValue {
    static void calculate(String line, Map<String, String> map) {
        String[] words = line.toLowerCase().split(" ");
        Stack<String> stack = new Stack<>();

        for (String word : words) {
            if (!NormalToPoland.isOperator(word)) {
                stack.push(map.get(word));
            }
            if (NormalToPoland.isOperator(word)) {
                stackOperation(stack, word);
            }
        }
        System.out.println(stack.pop());
    }

    private static void stackOperation(Stack<String> stack, String word) {
        String first = stack.pop();
        String second = stack.pop();
        String result = switch (word) {
            case "or" -> removeDuplicates(first + " " + second);
            case "and" -> andMethod(first, second);
            case "not" -> notMethod(second, first);
            default -> "default";
        };
        stack.push(result);
    }

    private static String removeDuplicates(String answer) {
        StringBuilder result = new StringBuilder();
        List<String> wordArr = List.of(answer.split(" "));
        TreeSet<String> set = new TreeSet<>(new MyComparator());
        set.addAll(wordArr);

        for (String word : set) {
            result.append(word).append(" ");
        }

        return result.toString().trim();
    }


    static class MyComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            return Integer.parseInt(o1.replaceAll("[a-zA-Z]+|\\.", "")) -
                    Integer.parseInt(o2.replaceAll("[a-zA-Z]+|\\.", ""));
        }
    }

    public static boolean lineContainsOther(String first, String second) {
        boolean check = false;
        List<String> firstWord = List.of(first.split(" "));
        List<String> secondWord = List.of(second.split(" "));

        for (String word : firstWord) {
            if (secondWord.contains(word)) check = true;
        }

        return check;
    }

    public static String andMethod(String first, String second) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> firstWord = List.of(first.split(" "));
        List<String> secondWord = List.of(second.split(" "));

        if (lineContainsOther(first, second)) {
            for (String word : firstWord) {
                if (secondWord.contains(word)) {
                    stringBuilder.append(word).append(" ");
                }
            }
            return removeDuplicates(stringBuilder.toString().trim());
        }

        System.out.println("No result for AND operation");
        System.exit(0);
        return "";
    }

    public static String notMethod(String first, String second) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> firstWord = List.of(first.split(" "));
        List<String> secondWord = List.of(second.split(" "));

        for (String word : firstWord) {
            if (!secondWord.contains(word)) {
                stringBuilder.append(word).append(" ");
            }
        }
        if(stringBuilder.toString().isBlank()) {
            System.out.println("There is no result for NOT operation");
            System.exit(0);
        }
        return removeDuplicates(stringBuilder.toString().trim());
    }
}

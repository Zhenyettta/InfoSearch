package Pract2;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class NormalToPoland {
    public static void createPolandReverse(String line, Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] words = line.toLowerCase().split(" ");
        Stack<String> stack = new Stack<>();

        for (String word : words) {
            if (!isOperator(word)) {
                mapContains(map, word);
                stringBuilder.append(word).append(" ");
            }

            if (isOperator(word)) {
                while (!stack.isEmpty() && isOperator(stack.peek()) && operatorPriority(word) <= operatorPriority(stack.peek())) {
                    stringBuilder.append(stack.pop()).append(" ");
                }
                stack.push(word);
            }
        }
        while (!stack.isEmpty())
            stringBuilder.append(stack.pop()).append(" ");
       PolandValue.calculate(String.valueOf(stringBuilder), map);
    }

    static int operatorPriority(String str) {
        return switch (str) {
            case "not" -> 2;
            case "and" -> 1;
            default -> 0;
        };
    }

    static boolean isOperator(String str) {
        return Objects.equals(str, "and") || Objects.equals(str, "or") || Objects.equals(str, "not");
    }

    static void mapContains(Map<String, String> map, String word) {
        if (!map.containsKey(word)) {
            System.out.println("No such word in dictionary");
            System.exit(0);
        }

    }

}
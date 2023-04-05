package Pract4;

import Pract3.Main3Task2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


public class Main {
    static Map<String, String> map;
    public static ArrayList<String> finalArr;

    public static void main(String[] args) throws IOException {
        map = Main3Task2.getMap();
        finalArr = new ArrayList<>(map.keySet());

        BinaryTree tree = new BinaryTree(finalArr);
        System.out.println(tree.search("прив*"));

        new PermutedIndex(finalArr);
        ThreeGramIndex threeGram = new ThreeGramIndex(finalArr);

        System.out.println("Введіть запит");
        Scanner ln = new Scanner(System.in);
        String query = ln.nextLine();
        ArrayList<String> queryThreeGrams = threeGram.createIndex(query);
        System.out.println(queryThreeGrams);
        System.out.println(threeGram.searchIndex(queryThreeGrams));

    }

}
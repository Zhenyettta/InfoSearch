package Pract4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryTree {
    static TreeNode root;

    static class TreeNode {
        String item;
        TreeNode left;
        TreeNode right;

        TreeNode(String str) {
            item = str;
        }
    }

    public BinaryTree(List<String> words) {
        List<String> copy = new ArrayList<>(words);
        Collections.shuffle(copy);
        for (String word : copy) {
            insertWord(word);
        }
    }

    static void insertWord(String newItem) {
        if (root == null) { // якщо корінь дерева ще не існує, то створюємо новий вузол зі значенням newItem та повертаємо з методу
            root = new TreeNode(newItem);
            return;
        }
        // встановлюємо початкове значення для вузла runner на корінь дерева
        TreeNode runner = root;
        while (true) {
            // якщо знайдено вузол з таким самим значенням, то повертаємо з методу, бо вставка не потрібна
            if (newItem.equals(runner.item)) {
                return;
            }
            // якщо новий елемент менше поточного вузла, то переходимо до лівого піддерева
            if (newItem.compareTo(runner.item) < 0) {
                if (runner.left == null) { //якщо лівого піддерева не існує, то створюємо новий вузол та повертаємо з методу
                    runner.left = new TreeNode(newItem);
                    return;
                }
                runner = runner.left; //якщо ліве піддерево існує, то продовжуємо пошук з новим поточним вузлом (лівим нащадком поточного вузла)
            } else {
                if (runner.right == null) { //якщо новий елемент більше поточного вузла, то переходимо до правого піддерева
                    runner.right = new TreeNode(newItem);
                    return;
                }
                runner = runner.right;//якщо праве піддерево існує, то продовжуємо пошук з новим поточним вузлом (правим нащадком поточного вузла)
            }
        }
    }

    public List<String> search(String query) {
        query = query.toLowerCase();
        List<String> results = new ArrayList<>();
        if (query.endsWith("*")) {
            String prefix = query.substring(0, query.length() - 1); //зірочка відкидається
            searchSubtree(root, prefix, results);
        } else {
            System.out.println("Invalid input");
        }
        return results;
    }

    //пошук у піддереві вузла з прифіксом та додає резудбтати
    private void searchSubtree(TreeNode node, String prefix, List<String> results) {
        if (node == null) {
            return;
        }
        //якщо значення item вузла node починається з префіксу prefix,
        // то значення item додається до списку results, і функція викликає себе рекурсивно для лівого та правого піддерева.
        if (node.item.startsWith(prefix)) {
            results.add(node.item);
            searchSubtree(node.left, prefix, results);
            searchSubtree(node.right, prefix, results);
            //якщо префікс менший за значення item вузла node, то функція рекурсивно викликається для лівого піддерева node.left.
        } else if (prefix.compareTo(node.item) < 0) {
            searchSubtree(node.left, prefix, results);
        } else {
            searchSubtree(node.right, prefix, results);
        }
    }
}

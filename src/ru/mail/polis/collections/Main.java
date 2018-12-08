package ru.mail.polis.collections;

import java.util.Random;

import ru.mail.polis.collections.set.sorted.todo.AVLTree;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 1; i < 10; i++) {
            tree.add(i);
            tree.printTree();
            Thread.sleep(100);
        }


    }
}

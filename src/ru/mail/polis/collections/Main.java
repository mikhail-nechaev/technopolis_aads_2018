package ru.mail.polis.collections;

import java.util.Random;

import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;
import ru.mail.polis.collections.set.sorted.todo.AVLTree;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //9 4 8
        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 1; i < 10; i++) {
            int x = new Random().nextInt(10);
            System.out.print("Can i add " + x + " ? " + tree.add(x) + " AVL ? " );
            try {
                tree.checkBalance();
                System.out.println("yeap");
            }catch (UnbalancedTreeException e){
                System.out.println("NO");
            }
            Thread.sleep(100);
        }


    }
}

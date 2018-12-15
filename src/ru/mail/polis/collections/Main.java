package ru.mail.polis.collections;

import java.util.Iterator;
import java.util.ListIterator;

import ru.mail.polis.collections.list.todo.ArrayDequeFull;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;
import ru.mail.polis.collections.list.todo.ArrayPriorityQueueSimple;
import ru.mail.polis.collections.list.todo.LinkedDequeSimple;
import ru.mail.polis.collections.set.hash.todo.OpenHashTable;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;
import ru.mail.polis.collections.set.sorted.todo.AVLTree;
import ru.mail.polis.collections.set.sorted.todo.RedBlackTree;

public class Main {

    public static void main(String[] args) {




       /* RedBlackTree redBlackTree = new RedBlackTree();
        try {
            redBlackTree.add(1);
            redBlackTree.add(2);
            redBlackTree.add(3);
            redBlackTree.add(4);
            redBlackTree.add(5);
            redBlackTree.add(6);
            redBlackTree.add(7);
            redBlackTree.add(8);
            redBlackTree.add(12);
            redBlackTree.add(11);
            redBlackTree.add(10);
            redBlackTree.add(9);
            redBlackTree.add(8);
            System.out.println(redBlackTree.contains(100));
            System.out.println(redBlackTree.size());
            redBlackTree.checkBalance();
            redBlackTree.remove(3);
            redBlackTree.checkBalance();
            redBlackTree.remove(7);
            redBlackTree.checkBalance();
            redBlackTree.remove(12);
            redBlackTree.checkBalance();
            redBlackTree.remove(19);
            redBlackTree.checkBalance();
            redBlackTree.remove(2);
            redBlackTree.checkBalance();
            redBlackTree.remove(1);
            redBlackTree.checkBalance();
            redBlackTree.remove(9);
            redBlackTree.checkBalance();
            redBlackTree.remove(25);
            redBlackTree.checkBalance();
            redBlackTree.remove(33);
            redBlackTree.checkBalance();
            System.out.println(redBlackTree.isEmpty());
        }
        catch (UnbalancedTreeException e){
            e.printStackTrace();
        }







        /*ArrayDequeFull arrayDequeFull = new ArrayDequeFull();
        arrayDequeFull.addFirst(4);
        arrayDequeFull.addFirst(8);
        arrayDequeFull.removeFirst();
        System.out.println(arrayDequeFull.getFirst());
        System.out.println(arrayDequeFull.getLast());*/




    }
}

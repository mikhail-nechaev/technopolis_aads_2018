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
import ru.mail.polis.collections.set.sorted.todo.AVLTreeIterable;
import ru.mail.polis.collections.set.sorted.todo.RedBlackTree;

public class Main {

    public static void main(String[] args) {


        AVLTreeIterable avlTreeIterable = new AVLTreeIterable();
        for(int i = 0; i < 20; i++){
            avlTreeIterable.add(i);
        }
        Iterator iterator = avlTreeIterable.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}

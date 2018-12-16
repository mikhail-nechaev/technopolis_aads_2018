package ru.mail.polis.collections.custom;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import ru.mail.polis.collections.list.todo.ArrayDequeFull;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;

/*
 * Created by Nechaev Mikhail
 * Since 27/11/2018.
 */
public class Stub {
    public static void main(String[] args) {
        int[] x = {1, 2, 3, 4, 5};
        int head = 3;
        x[head = (head - 1)] = 10;
        System.out.println(Arrays.toString(x));

        System.out.println((3 - 0) & (7));

        ArrayDequeSimple<String> arr = new ArrayDequeSimple<>();
        arr.addFirst("A"); //[A
        arr.addLast("B"); //[A B]
        arr.addLast("C"); //[A B C]
        arr.addFirst("D"); //[D A B C]
        arr.removeLast(); //c
        arr.removeFirst(); //d
        arr.removeFirst(); //a
        arr.removeFirst(); //b


        ArrayDequeFull<String> arr1 = new ArrayDequeFull<>();

        arr1.addFirst("A");
        arr1.addLast("A");
        arr1.offerFirst("A");
        arr1.offerLast("A");
        arr1.add("A");
        arr1.offer("A");
        arr1.addAll(Collections.singleton("A"));
        arr1.push("A");

        System.out.println(arr1.size());

        arr1.removeFirst();
        arr1.removeLast();
        arr1.pollFirst();
        arr1.pollLast();
        arr1.removeFirstOccurrence("A");
        arr1.removeLastOccurrence("A");
        arr1.poll();
        arr1.pop();

        System.out.println(arr1.size());

        ArrayDequeFull<String> arrayDequeFull = new ArrayDequeFull<>();
        arrayDequeFull.addFirst("A");
        Iterator iterator = arrayDequeFull.iterator();
        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());
        Iterator dit = arrayDequeFull.descendingIterator();
        System.out.println(dit.hasNext());
        System.out.println(dit.next());
    }
}

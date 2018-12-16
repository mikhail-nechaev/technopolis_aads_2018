package main;

import java.util.Iterator;

import org.junit.Assert;

import ru.mail.polis.collections.list.IDeque;
import ru.mail.polis.collections.list.IQueue;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;
import ru.mail.polis.collections.list.todo.LinkedDequeSimple;

public class Main {
    public static void main(String[] args) {
        IDeque<String> deque = new LinkedDequeSimple<>();
        deque.addFirst("L");
        deque.addFirst("M");
        deque.addFirst("F");
        Iterator<String> iterator = deque.iterator();

        System.out.println(iterator.hasNext()); //true
        System.out.println(iterator.next()); // F
        System.out.println(iterator.hasNext()); // true
        System.out.println(iterator.next()); // M
        iterator.remove();
        System.out.println(iterator.hasNext()); // true
        System.out.println(iterator.next()); // L
    }
}

package ru.mail.polis.collections.custom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.polis.collections.list.IDeque;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;

import java.util.ArrayDeque;
import java.util.Deque;

/*
 * Created by Nechaev Mikhail
 * Since 27/11/2018.
 */
public class Stub {

    private IDeque<Integer> deque;
    private Deque<Integer> javaDeque;

    @Before
    public void init() {
        deque = new ArrayDequeSimple<>();
        javaDeque = new ArrayDeque<>();
    }

    @Test
    public void addFirstTest() {
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
        }
        Assert.assertEquals(10, deque.size());
    }
}

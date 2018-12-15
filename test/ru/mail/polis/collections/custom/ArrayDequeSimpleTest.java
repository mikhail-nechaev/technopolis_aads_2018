package ru.mail.polis.collections.custom;

import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Test;

import ru.mail.polis.collections.list.todo.ArrayDequeSimple;

public class ArrayDequeSimpleTest {

    private ArrayDequeSimple<Integer> deque = new ArrayDequeSimple<>();

    @Test
    public void firstTest() {
        for (int i = 0; i <= 32; i++) {
            deque.addFirst(i);
        }
        Assert.assertEquals(33, deque.size());
        for (int i = 32; i >= 0; i--) {
            Assert.assertEquals(i, (int) deque.getFirst());
            Assert.assertEquals(i, (int) deque.removeFirst());
        }
        Assert.assertTrue(deque.isEmpty());
    }

    @Test
    public void LastTest() {
        for (int i = 0; i <= 32; i++) {
            deque.addLast(i);
        }
        Assert.assertEquals(33, deque.size());
        for (int i = 32; i >= 0; i--) {
            Assert.assertEquals(i, (int) deque.getLast());
            Assert.assertEquals(i, (int) deque.removeLast());
        }
        Assert.assertTrue(deque.isEmpty());
    }

    @Test
    public void forEachTest() {
        for (int i = 0; i <= 32; i++) {
            deque.addLast(i);
        }
        int i = 0;
        for (int current : deque) {
            Assert.assertEquals(i, current);
            i++;
        }
    }

    @Test
    public void iteratorLastTest() {
        for (int i = 0; i <= 32; i++) {
            deque.addLast(i);
        }
        ListIterator iterator = deque.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertTrue(iterator.hasPrevious());
        Assert.assertEquals(0, iterator.previousIndex());
        Assert.assertEquals(0, iterator.previous());
        Assert.assertFalse(iterator.hasPrevious());

        for (int i = 1; i <= 32; i++) {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertEquals(i, iterator.nextIndex());
            Assert.assertEquals(i, iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(33, iterator.nextIndex());

        for (int i = 31; i >= 0; i--) {
            Assert.assertTrue(iterator.hasPrevious());
            Assert.assertEquals(i, iterator.previousIndex());
            Assert.assertEquals(i, iterator.previous());
        }
        Assert.assertFalse(iterator.hasPrevious());
        Assert.assertEquals(-1, iterator.previousIndex());
    }

    @Test
    public void iteratorFirstTest() {
        for (int i = 32; i >= 0; i--) {
            deque.addFirst(i);
        }
        ListIterator iterator = deque.iterator();

        for (int i = 0; i <= 32; i++) {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertEquals(i, iterator.nextIndex());
            Assert.assertEquals(i, iterator.next());
            iterator.set(32-i);
        }

        for (int i = 31; i >= 0; i--) {
            Assert.assertTrue(iterator.hasPrevious());
            Assert.assertEquals(i, iterator.previousIndex());
            Assert.assertEquals(32 - i, iterator.previous());
        }
    }

    @Test
    public void iteratorRemoveTest() {
        for (int i = 0; i <= 10; i++) {
            deque.addLast(i);
        }
        ListIterator iterator = deque.iterator();

        for (int i = 0; i <= 10; i++) {
            Assert.assertEquals(11 - i, deque.size());
            iterator.next();
            iterator.remove();
        }
        Assert.assertTrue(deque.isEmpty());
    }
}
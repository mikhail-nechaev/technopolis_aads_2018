package ru.mail.polis.collections.set.sorted;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.mail.polis.collections.set.sorted.todo.AVLTreeIterable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TestAVLTreeIterable {

    private ISortedSetIterable<Integer> sortedSet = new AVLTreeIterable<>();

    @Before
    public void reset() {
        sortedSet.clear();
    }

    @Test
    public void test1() {
        sortedSet.add(1);
        Assert.assertTrue(sortedSet.iterator().next() == 1);
    }

    @Test
    public void test2() {
        for (int i = 10; i >= 0; i--) {
            sortedSet.add(i);
        }
        Iterator<Integer> iterator = sortedSet.iterator();
        for (int i = 0; i <= 10; i++) {
            int next = iterator.next();
            Assert.assertTrue("i = " + i + ", n = " + next, next == i);
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void test3() {
        sortedSet.add(1);
        Assert.assertTrue(sortedSet.descendingIterator().next() == 1);
    }

    @Test
    public void test4() {
        for (int i = 10; i >= 0; i--) {
            sortedSet.add(i);
        }
        Iterator<Integer> iterator = sortedSet.descendingIterator();
        for (int i = 10; i >= 0; i--) {
            int next = iterator.next();
            Assert.assertTrue("i = " + i + ", n = " + next, next == i);
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void test5() {
        sortedSet.iterator().next();
    }

    @Test(expected = NoSuchElementException.class)
    public void test6() {
        sortedSet.descendingIterator().next();
    }
}

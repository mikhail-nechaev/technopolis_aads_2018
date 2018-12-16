package ru.mail.polis.collections.set.sorted;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.polis.collections.set.sorted.todo.AVLTreeIterable;

import java.util.Iterator;

public class TestAVLTreeIterableRemove {

    private ISortedSetIterable<Integer> sortedSet = new AVLTreeIterable<>();

    @Before
    public void reset() {
        sortedSet.clear();
    }

    @Test(expected = IllegalStateException.class)
    public void test1() {
        sortedSet.iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void test2() {
        sortedSet.descendingIterator().remove();
    }

    @Test
    public void test3() {
        sortedSet.add(1);
        Iterator<Integer> iterator = sortedSet.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == 1);
        iterator.remove();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertTrue(sortedSet.isEmpty());
    }

    @Test
    public void test4() {
        sortedSet.add(1);
        Iterator<Integer> iterator = sortedSet.descendingIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == 1);
        iterator.remove();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertTrue(sortedSet.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void test5() {
        sortedSet.add(1);
        Iterator<Integer> iterator = sortedSet.iterator();
        iterator.remove();
        iterator.remove();
    }

    @Test(expected = IllegalStateException.class)
    public void test6() {
        sortedSet.add(1);
        Iterator<Integer> iterator = sortedSet.descendingIterator();
        iterator.remove();
        iterator.remove();
    }

    @Test
    public void test7() {
        for (int i = 0; i < 10; i++) {
            sortedSet.add(i);
        }
        Iterator<Integer> desc = sortedSet.descendingIterator();
        for (int i = 9; i >= 5; i++) {
            Assert.assertTrue(desc.next() == i);
            desc.remove();
        }
        Iterator<Integer> asc = sortedSet.iterator();
        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(asc.next() == i);
        }
        Assert.assertFalse(asc.hasNext());
    }
}

package ru.mail.polis.collections.iterator;

import org.junit.Assert;
import org.junit.Test;
import ru.mail.polis.collections.iterator.todo.IntegerIncreasingSequencePeekingIterator;

import java.util.NoSuchElementException;

/**
 * Класс тестирующий {@link IntegerIncreasingSequencePeekingIterator}
 */
public class TestIntegerIncreasingSequencePeekingIterator {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFirstLast1() {
        new IntegerIncreasingSequencePeekingIterator(1, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFirstLast2() {
        new IntegerIncreasingSequencePeekingIterator(Integer.MAX_VALUE, Integer.MIN_VALUE, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorStep1() {
        new IntegerIncreasingSequencePeekingIterator(1, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorStep2() {
        new IntegerIncreasingSequencePeekingIterator(1, 2, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorStep3() {
        new IntegerIncreasingSequencePeekingIterator(1, 2, Integer.MIN_VALUE);
    }

    @Test
    public void testOneElement() {
        IIncreasingSequenceIterator<Integer> iterator = new IntegerIncreasingSequencePeekingIterator(1, 1, 1);
        checkFirst(iterator, 1);
        checkLast(iterator);
    }

    @Test
    public void testOverflow() {
        int step = 100;
        int first = Integer.MAX_VALUE - step;
        int last = Integer.MAX_VALUE;
        IIncreasingSequenceIterator<Integer> iterator = new IntegerIncreasingSequencePeekingIterator(first, last, step);
        checkFirst(iterator, first);
        checkIncrease(iterator, first, last, step);
        checkLast(iterator);
    }

    @Test
    public void testIncreasing() {
        int first = Integer.MIN_VALUE;
        int last = 1;
        int step = 10;
        IIncreasingSequenceIterator<Integer> iterator = new IntegerIncreasingSequencePeekingIterator(first, last, step);
        checkFirst(iterator, first);
        Assert.assertTrue(iterator.hasNext());
        checkIncrease(iterator, first, last, step);
        checkLast(iterator);
    }

    @Test
    public void testCompare1() {
        IIncreasingSequenceIterator<Integer> it1 = new IntegerIncreasingSequencePeekingIterator(1, 1, 1);
        IIncreasingSequenceIterator<Integer> it2 = new IntegerIncreasingSequencePeekingIterator(2, 2, 1);
        Assert.assertTrue(it1.compareTo(it2) < 0);
    }

    @Test
    public void testCompare2() {
        IIncreasingSequenceIterator<Integer> it1 = new IntegerIncreasingSequencePeekingIterator(1, 1, 1);
        IIncreasingSequenceIterator<Integer> it2 = new IntegerIncreasingSequencePeekingIterator(1, 1, 1);
        Assert.assertTrue(it1.compareTo(it2) == 0);
    }

    @Test
    public void testCompare3() {
        IIncreasingSequenceIterator<Integer> it1 = new IntegerIncreasingSequencePeekingIterator(2, 2, 1);
        IIncreasingSequenceIterator<Integer> it2 = new IntegerIncreasingSequencePeekingIterator(1, 1, 1);
        Assert.assertTrue(it1.compareTo(it2) > 0);
    }

    @Test
    public void testCompare4() {
        IIncreasingSequenceIterator<Integer> it1 = new IntegerIncreasingSequencePeekingIterator(10, 20, 1);
        IIncreasingSequenceIterator<Integer> it2 = new IntegerIncreasingSequencePeekingIterator(1, 30, 1);
        for (int i = 1; i < 10; i++) {
            Assert.assertTrue(it2.hasNext());
            Assert.assertTrue("" + i, it2.compareTo(it1) < 0);
            it2.next();
        }
        for (int i = 10; i <= 20; i++) {
            System.out.print(i + " ");
            Assert.assertTrue(it1.hasNext());
            Assert.assertTrue(it2.hasNext());
            Assert.assertTrue("" + i, it1.compareTo(it2) == 0);
            System.out.println(it1.next());
            it2.next();
        }

        for (int i = 21; i <= 30; i++) {
            Assert.assertTrue(it2.hasNext());
            Assert.assertTrue("" + i, it2.compareTo(it1) > 0);
            it2.next();
        }
    }

    @Test
    public void testCompare5() {
        IIncreasingSequenceIterator<Integer> it1 = new IntegerIncreasingSequencePeekingIterator(1, 1, 1);
        IIncreasingSequenceIterator<Integer> it2 = new IntegerIncreasingSequencePeekingIterator(100, 100, 1);
        Assert.assertTrue(it1.compareTo(it2) < 0);
        Assert.assertTrue(it2.hasNext());
        Assert.assertTrue(it2.next() == 100);
        Assert.assertFalse(it2.hasNext());
        Assert.assertTrue(it1.compareTo(it2) > 0);
    }

    @Test
    public void testBound() {
        IIncreasingSequenceIterator<Integer> iterator = new IntegerIncreasingSequencePeekingIterator(
                Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE
        );
        checkFirst(iterator, Integer.MIN_VALUE);
        checkIncrease(iterator, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        checkLast(iterator);
    }

    private void checkFirst(IIncreasingSequenceIterator<Integer> iterator, int first) {
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.peek() == first);
        Assert.assertTrue(iterator.next() == first);
    }

    private void checkIncrease(IIncreasingSequenceIterator<Integer> iterator, int lastCurrent, int last, int step) {
        int current;
        int i = 0;
        while (iterator.hasNext()) {
           if(i % 100000000 == 0) System.out.println( i++ );

            Assert.assertTrue(lastCurrent != last);
            current = iterator.peek();
            checkStep(iterator, lastCurrent, current, step);
            lastCurrent = current;
        }
    }

    private void checkStep(IIncreasingSequenceIterator<Integer> iterator, int lastCurrent, int current, int step) {
        Assert.assertTrue(current == iterator.next());
        Assert.assertTrue("c = " + current + ", lc = " + lastCurrent, current > lastCurrent);
        Assert.assertTrue((long) current - lastCurrent <= step);
    }

    private void checkLast(IIncreasingSequenceIterator<Integer> iterator) {
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.peek();
            Assert.fail();
        } catch (NoSuchElementException expected) {
            /* empty */
        }
        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException expected) {
            /* empty */
        }
    }

}

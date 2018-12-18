package ru.mail.polis.collections.iterator;

import org.junit.Assert;
import org.junit.Test;
import ru.mail.polis.collections.iterator.todo.IntegerIncreasingSequencePeekingIterator;
import ru.mail.polis.collections.iterator.todo.MergingPeekingIncreasingIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Класс тестирующий {@link MergingPeekingIncreasingIterator}
 */
public class TestMergingPeekingIncreasingIterator {

    @Test
    public void testEmpty() {
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator();
        checkLast(iterator);
    }

    @Test
    public void testSuccessiveSequence1() {
        checkSuccessiveSequenceWithLast(
                new MergingPeekingIncreasingIterator(
                        new IntegerIncreasingSequencePeekingIterator(1, 10, 1)
                ), 1, 10
        );
    }

    @Test
    public void testSuccessiveSequence2() {
        checkSuccessiveSequenceWithLast(
                new MergingPeekingIncreasingIterator(
                        new IntegerIncreasingSequencePeekingIterator(1, 10, 1),
                        new IntegerIncreasingSequencePeekingIterator(11, 20, 1)
                ), 1, 20
        );
    }

    @Test
    public void testSuccessiveSequence3() {
        checkSuccessiveSequenceWithLast(
                new MergingPeekingIncreasingIterator(
                        new IntegerIncreasingSequencePeekingIterator(11, 20, 1),
                        new IntegerIncreasingSequencePeekingIterator(1, 10, 1)
                ), 1, 20
        );
    }

    @Test
    public void testSuccessiveSequence4() {
        checkSuccessiveSequenceWithLast(
                new MergingPeekingIncreasingIterator(
                        new IntegerIncreasingSequencePeekingIterator(21, 30, 1),
                        new IntegerIncreasingSequencePeekingIterator(1, 10, 1),
                        new IntegerIncreasingSequencePeekingIterator(11, 20, 1)
                ), 1, 30
        );
    }

    @Test
    public void testIncrease1() {
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator(
                new IntegerIncreasingSequencePeekingIterator(1, 30, 1),
                new IntegerIncreasingSequencePeekingIterator(50, 70, 1)
        );
        checkSuccessiveSequence(iterator, 1, 30);
        checkSuccessiveSequenceWithLast(iterator, 50, 70);
    }

    @Test
    public void testIncrease2() {
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator(
                new IntegerIncreasingSequencePeekingIterator(600, 700, 1),
                new IntegerIncreasingSequencePeekingIterator(50, 70, 1)
        );
        checkSuccessiveSequence(iterator, 50, 70);
        checkSuccessiveSequenceWithLast(iterator, 600, 700);
    }

    @Test
    public void testIntersect1() {
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator(
                new IntegerIncreasingSequencePeekingIterator(1, 30, 1),
                new IntegerIncreasingSequencePeekingIterator(1, 30, 1)
        );
        checkSuccessiveSequence(iterator, 1, 30, 2);
        checkLast(iterator);
    }

    @Test
    public void testIntersect2() {
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator(
                new IntegerIncreasingSequencePeekingIterator(1, 30, 1),
                new IntegerIncreasingSequencePeekingIterator(10, 40, 1)
        );
        checkSuccessiveSequence(iterator, 1, 9);
        checkSuccessiveSequence(iterator, 10, 30, 2);
        checkSuccessiveSequenceWithLast(iterator, 31, 40);
    }

    @Test
    public void testIntersect3() {
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator(
                new IntegerIncreasingSequencePeekingIterator(50, 90, 1),
                new IntegerIncreasingSequencePeekingIterator(10, 60, 1)
        );
        checkSuccessiveSequence(iterator, 10, 49);
        checkSuccessiveSequence(iterator, 50, 60, 2);
        checkSuccessiveSequenceWithLast(iterator, 61, 90);
    }

    @Test
    public void testComplex() {
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator(
                new IntegerIncreasingSequencePeekingIterator(10, 20, 1),
                new IntegerIncreasingSequencePeekingIterator(10, 20, 1),
                new IntegerIncreasingSequencePeekingIterator(1, 15, 1),
                new IntegerIncreasingSequencePeekingIterator(15, 30, 1),
                new IntegerIncreasingSequencePeekingIterator(-10, 0, 1),
                new IntegerIncreasingSequencePeekingIterator(50, 60, 1)
        );
        checkSuccessiveSequence(iterator, -10, 9);
        checkSuccessiveSequence(iterator, 10, 14, 3);
        checkSuccessiveSequence(iterator, 15, 15, 4);
        checkSuccessiveSequence(iterator, 16, 20, 3);
        checkSuccessiveSequence(iterator, 21, 30);
        checkSuccessiveSequenceWithLast(iterator, 50, 60);
    }

    @Test
    public void testRandom() {
        Random random = new Random();
        int iteratorCount = 10 + random.nextInt(10);
        IntegerIncreasingSequencePeekingIterator[] iterators
                = new IntegerIncreasingSequencePeekingIterator[iteratorCount];

        SortedSet<Integer> ranges = new TreeSet<>();
        for (int i = 0; i < iteratorCount; i++) {
            int first = random.nextInt(Integer.MAX_VALUE);
            int last = first + random.nextInt(1 + (Integer.MAX_VALUE - first));
            int step = 1 + random.nextInt(Integer.MAX_VALUE);
            ranges.add(first);
            ranges.add(last);
            iterators[i] = new IntegerIncreasingSequencePeekingIterator(first, last, step);
        }
        Iterator<Integer> iterator = new MergingPeekingIncreasingIterator(iterators);
        SortedSet<Integer> values = new TreeSet<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        Assert.assertEquals(values.first(), ranges.first());
        Assert.assertEquals(values.last(), ranges.last());
        Assert.assertTrue(values.containsAll(ranges));
    }

    private void checkSuccessiveSequence(Iterator<Integer> iterator, int from, int to) {
        checkSuccessiveSequence(iterator, from, to, 1);
    }

    private void checkSuccessiveSequence(Iterator<Integer> iterator, int from, int to, int duplicateCount) {
        for (int current = from; current <= to; current++) {
            for (int i = 0; i < duplicateCount; i++) {
                Assert.assertTrue("c = " + current + ", d = " + duplicateCount, iterator.hasNext());
                Assert.assertTrue("c = " + current + ", d = " + duplicateCount, current == iterator.next());
            }
        }
    }

    private void checkSuccessiveSequenceWithLast(Iterator<Integer> iterator, int from, int to) {
        checkSuccessiveSequence(iterator, from, to);
        checkLast(iterator);
    }

    private void checkLast(Iterator<Integer> iterator) {
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
        } catch (NoSuchElementException e) {
            /* empty */
        }
    }
}

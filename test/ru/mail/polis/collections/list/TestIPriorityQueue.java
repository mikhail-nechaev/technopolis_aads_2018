package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.list.todo.ArrayPriorityQueueSimple;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */

/**
 * Класс тестирующий интерфейс {@link IPriorityQueue<Integer>} на основе {@link ArrayPriorityQueueSimple<>}
 */
@RunWith(value = Parameterized.class)
public class TestIPriorityQueue {

    private IPriorityQueue<Integer> testPQ;

    @Parameterized.Parameter(0)
    public Comparator<Integer> comparator;
    @Parameterized.Parameter(1)
    public String name;

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Comparator.naturalOrder(), "NATURAL"},
                {Comparator.reverseOrder(), "REVERSE"},
                {Comparator.comparingInt((Integer v) -> v % 2).thenComparingInt(v -> v), "EVEN_FIRST"},
                {(Comparator) (v1, v2) -> 0, "ALL_EQUALS"},
        });
    }

    @Before
    @SuppressWarnings("unchecked")
    public void init() {
        try {
            testPQ = (IPriorityQueue<Integer>) ArrayPriorityQueueSimple.class.getConstructor(Comparator.class).newInstance(comparator);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void isEmpty() {
        Assert.assertTrue(testPQ.isEmpty());
        Assert.assertEquals(testPQ.size(), 0);
    }

    @Test
    public void isNotEmpty() {
        testPQ.add(0);
        Assert.assertFalse(testPQ.isEmpty());
        Assert.assertEquals(testPQ.size(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void addNull() {
        testPQ.add(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void noElement() {
        testPQ.element();
    }

    @Test(expected = NoSuchElementException.class)
    public void noRemove() {
        testPQ.remove();
    }

    @Test
    public void min1() {
        testPQ.add(Integer.MAX_VALUE);
        testPQ.add(Integer.MIN_VALUE);

        Assert.assertTrue(comparator.compare(
                min(Integer.MIN_VALUE, Integer.MAX_VALUE),
                testPQ.element()
        ) == 0);
    }

    @Test
    public void min2() {
        testPQ.add(10);
        Assert.assertTrue(10 == testPQ.element());
        testPQ.add(1);
        remove(min(1, 10));
        remove(max(1, 10));
        isEmpty();
    }

    @Test
    public void min3() {
        PriorityQueue<Integer> truePQ = new PriorityQueue<>(comparator);
        testPQ.add(10);
        remove(10);
        isEmpty();
        add(truePQ, testPQ, 9);
        Assert.assertTrue(9 == testPQ.element());
        add(truePQ, testPQ, 8);
        Assert.assertTrue(element(truePQ, testPQ));
        add(truePQ, testPQ, 1);
        Assert.assertTrue(element(truePQ, testPQ));
        add(truePQ, testPQ, 2);
        Assert.assertTrue(element(truePQ, testPQ));
        add(truePQ, testPQ, 3);
        Assert.assertTrue(element(truePQ, testPQ));
        add(truePQ, testPQ, 6);
        while (!testPQ.isEmpty()) {
            Assert.assertTrue(element(truePQ, testPQ));
            Assert.assertTrue(poll(truePQ, testPQ));
        }
        isEmpty();
    }

    private void remove(int min) {
        Assert.assertTrue(comparator.compare(min, testPQ.element()) == 0);
        Assert.assertTrue(comparator.compare(min, testPQ.remove()) == 0);
    }

    private void add(PriorityQueue<Integer> truePQ, IPriorityQueue<Integer> testPQ, int x) {
        truePQ.add(x);
        testPQ.add(x);
    }

    private boolean element(PriorityQueue<Integer> truePQ, IPriorityQueue<Integer> testPQ) {
        return eq(truePQ.element(), testPQ.element());
    }

    private boolean poll(PriorityQueue<Integer> truePQ, IPriorityQueue<Integer> testPQ) {
        return eq(truePQ.remove(), testPQ.remove());
    }

    private boolean eq(int x, int y) {
        return comparator.compare(x, y) == 0;
    }

    private int min(int x, int y) {
        return comparator.compare(x, y) <= 0 ? x : y;
    }

    private int max(int x, int y) {
        return comparator.compare(x, y) <= 0 ? y : x;
    }
}

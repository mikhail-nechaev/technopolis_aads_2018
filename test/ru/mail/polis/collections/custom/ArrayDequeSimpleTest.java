package ru.mail.polis.collections.custom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.mail.polis.collections.list.todo.ArrayDequeSimple;

import static org.junit.Assert.*;

public class ArrayDequeSimpleTest {
    private Integer[] actual;
    private ArrayDequeSimple<Integer> expected;
    private int size;
    private int first;
    private int last;

    @Before
    public void setUp() throws Exception {
        //                     0  1  2     3     4     5     6     7  8  9
        actual = new Integer[]{0, 2, 3, null, null, null, null, null, 4, 1};
        expected = new ArrayDequeSimple<>();
        expected.addFirst(0);
        expected.addLast(1);
        expected.addFirst(2);
        expected.addFirst(3);
        expected.addLast(4);
        size = expected.size();
        first = expected.getFirstCursor();
        last = expected.getLastCursor();
    }

    @Test
    public void addFirst() {
        Integer[] actual = new Integer[10];
        ArrayDequeSimple<Integer> expected = new ArrayDequeSimple<>();

        actual[0] = 0;
        expected.addFirst(0);
        Assert.assertArrayEquals(expected.getDeque(), actual);

        actual[1] = 1;
        expected.addFirst(1);
        Assert.assertArrayEquals(expected.getDeque(), actual);

        actual = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                10, null, null, null, null, null, null, null, null, null};
        for (int i = 2; i < 11; i++) {
            expected.addFirst(i);
        }
        Assert.assertArrayEquals(expected.getDeque(), actual);

    }

    @Test
    public void removeFirst() {
        Integer i = expected.removeFirst();
        //first--;
        size--;
        Assert.assertEquals(i, actual[2]);
        //Assert.assertEquals(first, expected.getFirstCursor());
        Assert.assertEquals(size, expected.size());
    }

    @Test
    public void getFirst() {
        Assert.assertEquals(expected.getFirst(), (Integer) 3);
    }

    @Test
    public void addLast() {
        expected.addLast(5);
        actual[7] = 5;
        Assert.assertArrayEquals(expected.getDeque(), actual);

        expected.addLast(6);
        actual[6] = 6;
        Assert.assertArrayEquals(expected.getDeque(), actual);

        Integer[] actual = new Integer[10];
        ArrayDequeSimple<Integer> expected = new ArrayDequeSimple<>();
        actual[9] = 0;
        expected.addLast(0);
        Assert.assertArrayEquals(expected.getDeque(), actual);
    }

    @Test
    public void removeLast() {
        Integer i = expected.removeLast();
        //last++;
        size--;
        Assert.assertEquals(i, actual[8]);
        //Assert.assertEquals(last, expected.getLastCursor());
        Assert.assertEquals(size, expected.size());
    }

    @Test
    public void getLast() {
        Assert.assertEquals(expected.getLast(), (Integer) 4);
    }

    @Test(expected = NullPointerException.class)
    public void contains() {
        expected.contains(null);

        Assert.assertTrue(expected.contains(2));
        Assert.assertFalse(expected.contains(7));
    }

    @Test
    public void clear() {
        expected.clear();
        Integer[] actual = new Integer[10];
        Assert.assertArrayEquals(expected.getDeque(), actual);

        ArrayDequeSimple<Integer> expected = new ArrayDequeSimple<>(12);
        actual = new Integer[12];
        expected.addLast(2);
        expected.addFirst(3);
        expected.clear();
        Assert.assertArrayEquals(expected.getDeque(), actual);
    }

    @Test
    public void iterator() {
        StringBuilder s = new StringBuilder();
        for (Integer i: expected) {
            s.append(i.toString() + " ");
        }
        String actual = "3 2 0 1 4 ";
        Assert.assertEquals(s.toString(), actual);
    }
}
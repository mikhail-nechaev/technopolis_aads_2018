package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.polis.collections.list.todo.ArrayPriorityQueueSimple;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Created by Nechaev Mikhail
 * Since 16/11/2018.
 */
public class TestIPriorityQueueRemove {

    private IPriorityQueue<Integer> pq = new ArrayPriorityQueueSimple<>();

    @Before
    public void reset() {
        pq.clear();
    }

    @Test(expected = IllegalStateException.class)
    public void test1() {
        pq.iterator().remove();
    }

    @Test
    public void test2() {
        pq.add(1);
        Assert.assertTrue(pq.size() == 1);
        Assert.assertTrue(pq.element() == 1);
        Iterator<Integer> iterator = pq.iterator();
        while (iterator.hasNext()) {
            Assert.assertTrue(iterator.next() == 1);
            iterator.remove();
        }
        Assert.assertTrue(pq.isEmpty());
    }

    @Test
    public void test3() {
        pq.add(1);
        pq.add(2);
        pq.add(3);
        pq.add(4);
        pq.add(5);
        Assert.assertTrue(pq.size() == 5);
        Iterator<Integer> iterator = pq.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        Assert.assertTrue(pq.isEmpty());
    }

    @Test
    public void test4() {
        for (int i = 0; i < 100; i++) {
            pq.add(i);
        }
        Iterator<Integer> iterator = pq.iterator();
        SortedSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(iterator.next());
            iterator.remove();
        }
        Assert.assertTrue(pq.isEmpty());
        Assert.assertTrue("first = " + set.first(), set.first() == 0);
        Assert.assertTrue("last = " + set.last(), set.last() == 99);
        Assert.assertTrue("size = " + set.size(), set.size() == 100);
    }

    @Test
    public void test5() {
        List<Integer> data = IntStream.range(0, 20)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(data);
        pq = new ArrayPriorityQueueSimple<>(data);
        Iterator<Integer> iterator = pq.iterator();
        SortedSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < 20; i++) {
            set.add(iterator.next());
            iterator.remove();
        }
        Assert.assertTrue(pq.isEmpty());
        Assert.assertTrue("first = " + set.first(), set.first() == 0);
        Assert.assertTrue("last = " + set.last(), set.last() == 19);
        Assert.assertTrue("size = " + set.size(), set.size() == 20);
    }

    @Test(expected = IllegalStateException.class)
    public void test6() {
        pq.add(1);
        Iterator<Integer> iterator = pq.iterator();
        iterator.remove();
        iterator.remove();
    }
}

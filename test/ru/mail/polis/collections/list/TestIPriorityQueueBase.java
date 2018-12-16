package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Test;
import ru.mail.polis.collections.list.todo.ArrayPriorityQueueSimple;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Created by Nechaev Mikhail
 * Since 16/11/2018.
 */
public class TestIPriorityQueueBase {

    IPriorityQueue<Integer> testPQ;

    @Test
    public void cons1() {
        List<Integer> data = IntStream.range(0, 20)
                .boxed()
                .collect(Collectors.toList());
        testPQ = new ArrayPriorityQueueSimple<>(data);
        Assert.assertTrue(testPQ.element() == 0);
    }

    @Test
    public void cons2() {
        List<Integer> data = IntStream.range(0, 20)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(data);
        testPQ = new ArrayPriorityQueueSimple<>(data);
        Assert.assertTrue(testPQ.element() == 0);
    }

    @Test
    public void cons3() {
        List<Integer> data = IntStream.range(0, 20)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(data);
        testPQ = new ArrayPriorityQueueSimple<>(data);
        for (int i = 0; i < 20; i++) {
            Assert.assertTrue(testPQ.remove() == i);
        }
    }

    @Test(expected = NullPointerException.class)
    public void cons4() {
        testPQ = new ArrayPriorityQueueSimple<>((Collection<Integer>) null);
    }

    @Test(expected = NullPointerException.class)
    public void cons5() {
        testPQ = new ArrayPriorityQueueSimple<>((Comparator<Integer>) null);
    }

    @Test(expected = NullPointerException.class)
    public void cons6() {
        testPQ = new ArrayPriorityQueueSimple<>(null, null);
    }

    @Test
    public void contains1() {
        testPQ = new ArrayPriorityQueueSimple<>();
        testPQ.add(10);
        testPQ.add(11);
        testPQ.add(12);
        Assert.assertTrue(testPQ.contains(11));
    }
}

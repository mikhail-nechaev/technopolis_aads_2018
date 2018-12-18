package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */
public abstract class AbstractIDequeTest extends AbstractIQueueTest {

    @Override
    public abstract IDeque<String> get();

    @Test(expected = NullPointerException.class)
    public void addFirstNull() {
        get().addFirst(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void getLastEmpty() {
        get().getLast();
    }

    @Test(expected = NoSuchElementException.class)
    public void removeLastEmpty() {
        get().removeLast();
    }

    @Test
    public void deq1() {
        get().addFirst("A"); //[A
        get().addLast("B"); //[A B]
        get().addLast("C"); //[A B C]
        get().addFirst("D"); //[D A B C]
        Assert.assertEquals("C", get().removeLast());
        Assert.assertEquals("D", get().removeFirst());
        Assert.assertEquals("A", get().removeFirst());
        Assert.assertEquals("B", get().removeFirst());
    }

    @Test
    public void deq2() {
        IDeque<String> deque = get();
        deque.addFirst("A");
        Assert.assertEquals("A", deque.getLast());
        Assert.assertEquals("A", deque.removeLast());
        Assert.assertTrue(deque.size() == 0);
        Assert.assertTrue(deque.isEmpty());
    }

    @Test
    public void deq3() {
        IDeque<String> deque = get();
        deque.addLast("A");
        Assert.assertEquals("A", deque.getFirst());
        Assert.assertEquals("A", deque.removeFirst());
        Assert.assertTrue(deque.size() == 0);
        Assert.assertTrue(deque.isEmpty());
    }

    @Test
    public void deq4() {
        IDeque<String> deque = get();
        deque.addFirst("A");
        deque.addFirst("B");
        Assert.assertEquals("A", deque.getLast());
        Assert.assertEquals("A", deque.removeLast());
        Assert.assertEquals("B", deque.getLast());
        Assert.assertEquals("B", deque.removeLast());
    }

    @Test
    public void deq5() {
        IDeque<String> deque = get();
        deque.addLast("A");
        deque.addLast("B");
        Assert.assertEquals("A", deque.getFirst());
        Assert.assertEquals("A", deque.removeFirst());
        Assert.assertEquals("B", deque.getFirst());
        Assert.assertEquals("B", deque.removeFirst());
    }

    @Test
    public void deq6() {
        IDeque<String> deque = get();
        deque.addLast("L1");
        deque.addLast("L2");
        deque.addFirst("F1");
        deque.addFirst("F2");
        Assert.assertTrue(deque.contains("L1"));
        Assert.assertTrue(deque.contains("L2"));
        Assert.assertTrue(deque.contains("F1"));
        Assert.assertTrue(deque.contains("F2"));
        deque.clear();
        Assert.assertTrue(deque.isEmpty());
        Assert.assertFalse(deque.contains("L1"));
        Assert.assertFalse(deque.contains("L2"));
        Assert.assertFalse(deque.contains("F1"));
        Assert.assertFalse(deque.contains("F2"));
    }

    @Test(expected = NoSuchElementException.class)
    public void iter1() {
        get().iterator().next();
    }

    @Test
    public void iter2() {
        Assert.assertFalse(get().iterator().hasNext());
    }

    @Test
    public void iter3() {
        IDeque<String> deque = get();
        deque.addFirst("A");
        Iterator<String> iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("A", iterator.next());
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException expected) {

        }
    }

    @Test
    public void iter4() {
        IDeque<String> deque = get();
        deque.addLast("A");
        Iterator<String> iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("A", iterator.next());
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException expected) {

        }
    }

}

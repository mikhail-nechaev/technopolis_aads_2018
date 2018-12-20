package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
public abstract class AbstractIDequeRemoveTest {

    public abstract IDeque<String> get();

    @Test(expected = IllegalStateException.class)
    public void iterRemove1() {
        get().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iterRemove2() {
        IDeque<String> deque = get();
        deque.addFirst("A");
        Iterator<String> iterator = deque.iterator();
        Assert.assertEquals("A", iterator.next());
        iterator.remove();
        iterator.remove();
    }

    @Test
    public void iterRemove3() {
        IDeque<String> deque = get();
        deque.addFirst("A");
        Iterator<String> iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("A", iterator.next());
        iterator.remove();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertTrue(deque.isEmpty());
    }

    @Test
    public void iterRemove4() {
        IDeque<String> deque = get();
        deque.addLast("A");
        Iterator<String> iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("A", iterator.next());
        iterator.remove();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertTrue(deque.isEmpty());
    }

    @Test
    public void iterRemove5() {
        IDeque<String> deque = get();
        deque.addFirst("L");
        deque.addFirst("M");
        deque.addFirst("F");
        Iterator<String> iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("F", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("M", iterator.next());
        iterator.remove();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("L", iterator.next());
        Assert.assertTrue(deque.size() == 2);
        iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("F", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("L", iterator.next());
    }

    @Test
    public void iterRemove6() {
        final int size = 3;
        String[] values = new String[]{"A", "B", "C"};
        IDeque<String> deque = get();
        for (int i = 0; i < size; i++) {
            deque.addLast(values[i]);
        }
        Assert.assertTrue(deque.size() == size);
        Iterator<String> iterator = deque.iterator();
        for (int i = 0; i < size; i++) {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertEquals(values[i], iterator.next());
            iterator.remove();
            Assert.assertTrue(deque.size() == size - i - 1);
        }
    }
}

package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Test;
import ru.mail.polis.collections.AbstractICollectionTest;

import java.util.NoSuchElementException;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */
public abstract class AbstractIQueueTest extends AbstractICollectionTest {

    @Override
    public abstract IQueue<String> get();

    @Test(expected = NullPointerException.class)
    public void addLastNull() {
        get().addLast(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void getFirstEmpty() {
        get().getFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void removeFirstEmpty() {
        get().removeFirst();
    }

    @Test
    public void add() {
        get().addLast("A");
    }

    @Test
    public void addContainsRemove() {
        IQueue<String> queue = get();
        queue.addLast("A");
        Assert.assertTrue(queue.contains("A"));
        Assert.assertFalse(queue.contains("B"));
        Assert.assertEquals("A", queue.removeFirst());
        Assert.assertFalse(queue.contains("B"));
        super.empty();
        super.emptySize();
    }

    @Test
    public void queue() {
        IQueue<String> queue = get();
        queue.addLast("A");
        queue.addLast("B");
        queue.addLast("C");
        Assert.assertTrue(queue.contains("A"));
        Assert.assertTrue(queue.contains("B"));
        Assert.assertTrue(queue.contains("C"));
        Assert.assertFalse(queue.contains("D"));
        Assert.assertEquals("A", queue.removeFirst());
        Assert.assertEquals("B", queue.removeFirst());
        Assert.assertEquals("C", queue.removeFirst());
        Assert.assertFalse(queue.contains("A"));
        Assert.assertFalse(queue.contains("B"));
        Assert.assertFalse(queue.contains("C"));
    }

}

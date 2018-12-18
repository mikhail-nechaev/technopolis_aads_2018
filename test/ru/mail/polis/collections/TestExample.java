package ru.mail.polis.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;

/*
 * Created by Nechaev Mikhail
 * Since 27/11/2018.
 */
public class TestExample {

    private ICollection<String> collection;

    @Before //Запускается перед запуском каждого теста
    public void init() {
        collection = new ArrayDequeSimple<>();
    }

    @Test
    public void empty() {
        Assert.assertTrue(collection.isEmpty());
    }

    @Test
    public void emptySize() {
        Assert.assertTrue(0 == collection.size());
    }

    @Test
    public void clear() {
        collection.clear();
        Assert.assertTrue(collection.isEmpty());
        Assert.assertTrue(0 == collection.size());
    }

    @Test
    public void contains() {
        Assert.assertFalse(collection.contains("A"));
    }
}

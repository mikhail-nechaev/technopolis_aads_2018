package ru.mail.polis.collections;

import org.junit.Assert;
import org.junit.Test;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */
public abstract class AbstractICollectionTest {

    public abstract ICollection<String> get();

    @Test
    public void empty() {
        Assert.assertTrue(get().isEmpty());
    }

    @Test
    public void emptySize() {
        Assert.assertTrue(0 == get().size());
    }

    @Test
    public void clear() {
        get().clear();
        Assert.assertTrue(get().isEmpty());
        Assert.assertTrue(0 == get().size());
    }

    @Test
    public void contains() {
        Assert.assertFalse(get().contains("A"));
    }
}

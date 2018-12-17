package ru.mail.polis.collections.set.hash;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.polis.collections.set.hash.todo.OpenHashTable;

import java.util.Iterator;

/*
 * Created by Nechaev Mikhail
 * Since 16/11/2018.
 */
public class TestHastTableIteratorRemove {

    private IOpenHashTable<IOpenHashTableEntity> openHashTable;

    @Before //Запускается перед запуском каждого теста
    public void createSortedSets() {
        openHashTable = new OpenHashTable<>();
    }

    @Test(expected = IllegalStateException.class)
    public void test1() {
        openHashTable.iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void test2() {
        openHashTable.add(new BrokenStudent(0));
        Iterator<IOpenHashTableEntity> iterator = openHashTable.iterator();
        iterator.remove();
        iterator.remove();
    }

    @Test
    public void test3() {
        IOpenHashTableEntity broken = new BrokenStudent(0);
        openHashTable.add(broken);
        Assert.assertTrue(openHashTable.size() == 1);
        Iterator<IOpenHashTableEntity> iterator = openHashTable.iterator();
        iterator.next();
        iterator.remove();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertTrue(openHashTable.isEmpty());
    }
}

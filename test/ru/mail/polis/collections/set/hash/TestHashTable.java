package ru.mail.polis.collections.set.hash;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.polis.collections.set.AbstractSetTest;
import ru.mail.polis.collections.set.ISet;
import ru.mail.polis.collections.set.hash.todo.OpenHashTable;
import ru.mail.polis.collections.set.hash.todo.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static com.sun.tools.javac.util.List.of;

/**
 * Created by Nechaev Mikhail
 * Since 07/12/2018.
 */
public class TestHashTable extends AbstractSetTest {

    private final List<IOpenHashTableEntity> entities = of(
            //uniq
            new Student(0, "A", "CDCD", Student.Gender.MALE, LocalDate.of(1990, 1, 1), 1000),
            new Student(0, "AA", "CCDD", Student.Gender.FEMALE, LocalDate.of(1993, 2, 2), 1001),
            new Student(0, "AAA", "CD", Student.Gender.FEMALE, LocalDate.of(1997, 3, 1), 1002),
            new Student(0, "AAAA", "C", Student.Gender.FEMALE, LocalDate.of(1990, 4, 7), 1003),
            //double
            new Student(0, "AAA", "CD", Student.Gender.FEMALE, LocalDate.of(1997, 3, 1), 1002),
            //uniq
            new Student(0, "B", "CCC", Student.Gender.MALE, LocalDate.of(1999, 4, 5), 1000),
            new Student(0, "BB", "DC", Student.Gender.MALE, LocalDate.of(1999, 5, 1), 1001),
            new Student(0, "BBB", "DD", Student.Gender.MALE, LocalDate.of(1990, 6, 20), 1002),
            //double
            new Student(0, "BB", "DC", Student.Gender.MALE, LocalDate.of(1999, 5, 1), 1001),
            new Student(0, "BBB", "DD", Student.Gender.MALE, LocalDate.of(1990, 6, 20), 1002),
            new Student(0, "A", "CDCD", Student.Gender.MALE, LocalDate.of(1990, 1, 1), 1000),
            //uniq
            new Student(0, "BBBB", "DDD", Student.Gender.FEMALE, LocalDate.of(1997, 1, 4), 1003),
            new Student(0, "AB", "D", Student.Gender.MALE, LocalDate.of(1993, 1, 1), 1004),
            new Student(0, "AABB", "DDC", Student.Gender.MALE, LocalDate.of(1999, 8, 1), 1005),
            new Student(0, "ABAB", "CDD", Student.Gender.MALE, LocalDate.of(1990, 1, 3), 1006),
            //double
            new Student(0, "AB", "D", Student.Gender.MALE, LocalDate.of(1993, 1, 1), 1004),
            new Student(0, "BBBB", "DDD", Student.Gender.FEMALE, LocalDate.of(1997, 1, 4), 1003),
            new Student(0, "AAA", "CD", Student.Gender.FEMALE, LocalDate.of(1997, 3, 1), 1002)
    );

    private Set<IOpenHashTableEntity> validSet;
    private IOpenHashTable<IOpenHashTableEntity> testSet;

    @Override
    protected boolean isRemoveEnabled() {
        return true;
    }

    @Before //Запускается перед запуском каждого теста
    public void createSortedSets() {
        validSet = new HashSet<>();
        testSet = new OpenHashTable<>();
    }

    @Test
    public void test01custom() {
        Assert.assertTrue(testSet.add(entities.get(0)));
        Assert.assertTrue(testSet.size() == 1);
        Assert.assertFalse(testSet.isEmpty());
    }

    @Test
    public void test02custom() {
        Assert.assertTrue(testSet.add(entities.get(2)));
        Assert.assertTrue(testSet.size() == 1);
        Assert.assertFalse(testSet.isEmpty());
        Assert.assertFalse(testSet.add(entities.get(4)));
        Assert.assertTrue(testSet.size() == 1);
        Assert.assertFalse(testSet.isEmpty());
    }

    @Test
    public void test03custom() {
        for (int i = 0; i < entities.size(); i++) {
            check(validSet, testSet, entities.get(i), TransformOperation.ADD);
        }
    }

    @Test
    public void test04hashCustom() {
        checkHash(entities.get(0), testSet.tableSize());
    }

    @Test
    public void test05hashCustom() {
        for (int i = 0; i < entities.size(); i++) {
            checkHash(entities.get(i), testSet.tableSize());
        }
    }

    @Test
    public void test01random() {
        for (int i = 0; i < 5; i++) {
            check(validSet, testSet, StudentGenerator.generate(), TransformOperation.ADD);
        }
    }

    @Test
    public void test02random() {
        List<IOpenHashTableEntity> values = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            IOpenHashTableEntity entity = StudentGenerator.generate();
            values.add(entity);
            check(validSet, testSet, entity, TransformOperation.ADD);
        }
        for (int i = 19; i >= 0; i--) {
            check(validSet, testSet, values.get(i), TransformOperation.REMOVE);
        }
    }

    @Test
    public void test03random() {
        List<IOpenHashTableEntity> values = new ArrayList<>(20);
        for (int i = 0; i < 1000; i++) {
            IOpenHashTableEntity entity = StudentGenerator.generate();
            values.add(entity);
            check(validSet, testSet, entity, TransformOperation.ADD);
        }
        for (int i = 0; i < 1000; i++) {
            check(validSet, testSet, values.get(i), TransformOperation.REMOVE);
        }
    }

    @Test
    public void test04hashRandom() {
        int tableSize = testSet.tableSize();
        IOpenHashTableEntity entity = StudentGenerator.generate();
        checkHash(entity, tableSize);
    }

    @Test
    public void test05hashRandom() {
        int lastTableSize = testSet.tableSize();
        for (int i = 0; i < 1000; i++) {
            IOpenHashTableEntity entity = StudentGenerator.generate();
            testSet.add(entity);
            if (lastTableSize != testSet.tableSize()) {
                lastTableSize = testSet.tableSize();
                checkHash(entity, lastTableSize);
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterator1() {
        testSet.iterator().next();
    }

    @Test
    public void testIterator2() {
        Assert.assertFalse(testSet.iterator().hasNext());
    }

    @Test(timeout = 1000L)
    public void testIterator3() {
        for (int i = 0; i < 4; i++) {
            testSet.add(entities.get(i));
            validSet.add(entities.get(i));
        }
        Iterator<IOpenHashTableEntity> iterator = testSet.iterator();
        while (iterator.hasNext()) {
            validSet.remove(iterator.next());
        }
        Assert.assertTrue(validSet.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class, timeout = 5000L)
    public void testBroken() {
        testSet.add(new BrokenStudent(0));
        testSet.add(new BrokenStudent(0));
    }

    private void check(Set<IOpenHashTableEntity> validSet, ISet<IOpenHashTableEntity> testSet, IOpenHashTableEntity value, TransformOperation transformOperation) {
        checkSizeAndContains(validSet, testSet, value);
        checkTransformOperation(validSet, testSet, value, transformOperation);
        checkSizeAndContains(validSet, testSet, value);
        checkTransformOperation(validSet, testSet, value, transformOperation);
        checkSizeAndContains(validSet, testSet, value);
    }

    private void checkHash(IOpenHashTableEntity entity, int tableSize) {
        SortedSet<Integer> idx = new TreeSet<>();
        IntStream.range(0, tableSize).forEach((probId) -> idx.add(entity.hashCode(tableSize, probId)));
        Assert.assertTrue("tableSize = " + tableSize,
                idx.first() == 0 && idx.last() == tableSize - 1 && idx.size() == tableSize
        );
    }

}

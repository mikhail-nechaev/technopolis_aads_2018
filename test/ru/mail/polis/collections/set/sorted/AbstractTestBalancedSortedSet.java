package ru.mail.polis.collections.set.sorted;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.polis.collections.set.AbstractSetTest;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractTestBalancedSortedSet extends AbstractSetTest {

    private static final Random RANDOM = new Random();

    private SortedSet<Integer> valid;
    private ISelfBalancingSortedTreeSet<Integer> tested;

    protected abstract Class<?> getTestClass();

    protected abstract Comparator<Integer> getComparator();

    @Before //Запускается перед запуском каждого теста
    public void createISortedSets() {
        valid = createValid(getComparator());
        tested = createTested(getTestClass(), getComparator());
    }

    @Test
    public void test01_empty() {
        checkFirstAndLast(valid, tested);
        checkSizeAndContains(valid, tested, 0);
    }

    @Test(expected = NullPointerException.class)
    public void test01_null() {
        tested.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void test01_nullContains() {
        tested.contains(null);
    }

    @Test(expected = NullPointerException.class)
    public void test01_nullRemove() {
        tested.remove(null);
    }

    @Test
    public void test02_1() {
        Assert.assertTrue(tested.add(1));
    }

    @Test
    public void test02_2() {
        Assert.assertTrue(tested.add(1));
        Assert.assertFalse(tested.add(1));
    }

    @Test
    public void test02_3() {
        Assert.assertTrue(tested.add(1));
        Assert.assertEquals(getComparator().compare(1, 2) != 0, tested.add(2));
    }

    @Test
    public void test02_4() {
        tested.add(1);
        tested.add(2);
        tested.add(3);
        try {
            tested.checkBalance();
        } catch (UnbalancedTreeException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void test02_5() {
        check(valid, tested, 5, TransformOperation.ADD);
    }

    @Test
    public void test03_1() {
        for (int value = 0; value < 10; value++) {
            System.out.println(value);
            check(valid, tested, value, TransformOperation.ADD);
        }
    }

    @Test
    public void test03_AddWithRemove() {
        check(valid, tested, 5, TransformOperation.ADD);
        check(valid, tested, 5, TransformOperation.REMOVE);
    }

    @Test
    public void test04_someAddWithRemove() {
        for (int value = 0; value < 10; value++) {
            check(valid, tested, value, TransformOperation.ADD);
        }
        for (int value = 0; value < 10; value++) {
            check(valid, tested, value, TransformOperation.REMOVE);
        }
    }

    @Test
    public void test05_small() {
        for (int value = 0; value < 10; value++) {
            check(valid, tested, value, TransformOperation.ADD);
        }
        for (int value = 10; value >= 0; value--) {
            check(valid, tested, value, TransformOperation.REMOVE);
        }
        for (int value = 0; value >= -10; value--) {
            check(valid, tested, value, TransformOperation.REMOVE);
        }
        for (int value = -10; value < 0; value++) {
            check(valid, tested, value, TransformOperation.ADD);
        }
    }

    @Test
    public void test06_middle() {
        for (int value = 0; value < 15; value++) {
            check(valid, tested, value, TransformOperation.ADD);
        }
        for (int value = 15; value < 30; value++) {
            check(valid, tested, value, TransformOperation.ADD);
            if (value % 2 == 0) {
                check(valid, tested, value, TransformOperation.REMOVE);
            }
        }
        for (int value = 100; value >= 0; value--) {
            check(valid, tested, value, TransformOperation.REMOVE);
        }
    }

    @Test
    public void test07_bigRandom() {
        for (int i = 0; i < 1000; i++) {
            check(valid, tested, RANDOM.nextInt(1000), TransformOperation.ADD);
        }
        for (int i = 0; i < 1000; i++) {
            check(valid, tested, RANDOM.nextInt(1000), TransformOperation.REMOVE);
        }
    }

    private <E> void check(SortedSet<E> valid, ISelfBalancingSortedTreeSet<E> tested, E value, TransformOperation transformOperation) {
        checkFirstAndLast(valid, tested);
        checkTransformOperation(valid, tested, value, transformOperation);
        checkBalanced(tested);
        checkSizeAndContains(valid, tested, value);
        checkTransformOperation(valid, tested, value, transformOperation);
        checkBalanced(tested);
        checkSizeAndContains(valid, tested, value);
        checkFirstAndLast(valid, tested);
    }

    private <E> void checkBalanced(ISelfBalancingSortedTreeSet tested) {
        try {
            tested.checkBalance();
        } catch (UnbalancedTreeException e) {
            Assert.fail(e.getMessage());
        }
    }

    private <E> void checkFirstAndLast(SortedSet<E> valid, ISortedSet<E> tested) {
        if (valid.isEmpty()) {
            try {
                tested.first();
                Assert.fail("NoSuchElementException - first");
            } catch (NoSuchElementException e) {
                /* empty */
            }
            try {
                tested.last();
                Assert.fail("NoSuchElementException - last");
            } catch (NoSuchElementException e) {
                /* empty */
            }
        } else {
            Assert.assertEquals("first", valid.first(), tested.first());
            Assert.assertEquals("last", valid.last(), tested.last());
        }
    }

    @SuppressWarnings("unchecked")
    private SortedSet<Integer> createValid(Comparator<Integer> comparator) {
        try {
            return (SortedSet<Integer>) TreeSet.class.getConstructor(Comparator.class).newInstance(comparator);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private ISelfBalancingSortedTreeSet<Integer> createTested(Class<?> clazz, Comparator<Integer> comparator) {
        try {
            return (ISelfBalancingSortedTreeSet<Integer>) clazz.getConstructor(Comparator.class).newInstance(comparator);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

}

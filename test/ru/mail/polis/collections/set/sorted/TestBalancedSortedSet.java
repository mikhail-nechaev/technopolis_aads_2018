package ru.mail.polis.collections.set.sorted;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.TestHelper;
import ru.mail.polis.collections.set.AbstractSetTest;
import ru.mail.polis.collections.set.sorted.todo.AVLTree;
import ru.mail.polis.collections.set.sorted.todo.RedBlackTree;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Nechaev Mikhail
 * Since 07/12/2018.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(value = Parameterized.class)
public class TestBalancedSortedSet extends AbstractSetTest {

    private static final Random RANDOM = new Random();

    private static final Object[][] classes = new Object[][]{
            {AVLTree.class, Boolean.TRUE},
            {RedBlackTree.class, Boolean.FALSE},
            {RedBlackTree.class, Boolean.TRUE},
    };

    @Parameterized.Parameter()
    public Class<?> testClass;
    @Parameterized.Parameter(1)
    public boolean removeEnabled;
    @Parameterized.Parameter(2)
    public Comparator<Integer> comparator;
    @Parameterized.Parameter(3)
    public String comparatorName;

    private SortedSet<Integer> valid;
    private ISelfBalancingSortedTreeSet<Integer> tested;

    @Parameterized.Parameters(name = "{0}. remove == {1}. comparator is {3}")
    public static Collection<Object[]> data() {
        Collection<Object[]> comparators = TestHelper.COMPARATORS;
        Object[][] objects = new Object[classes.length * comparators.size()][4];
        int index = 0;
        for (Object[] testClass : classes) {
            for (Object[] comparator : comparators) {
                objects[index++] = new Object[]{
                        testClass[0],
                        testClass[1],
                        comparator[0],
                        comparator[1],
                };
            }
        }
        return Arrays.asList(objects);
    }

    @Override
    protected boolean isRemoveEnabled() {
        return removeEnabled;
    }

    @Before //Запускается перед запуском каждого теста
    public void createISortedSets() {
        valid = createValid();
        tested = createTested(testClass);
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
        Assert.assertEquals(comparator.compare(1, 2) != 0, tested.add(2));
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
    private SortedSet<Integer> createValid() {
        try {
            return (SortedSet<Integer>) TreeSet.class.getConstructor(Comparator.class).newInstance(comparator);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private ISelfBalancingSortedTreeSet<Integer> createTested(Class<?> clazz) {
        try {
            return (ISelfBalancingSortedTreeSet<Integer>) clazz.getConstructor(Comparator.class).newInstance(comparator);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

}

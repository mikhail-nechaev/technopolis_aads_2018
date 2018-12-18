package ru.mail.polis.collections.set.sorted;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.TestHelper;
import ru.mail.polis.collections.set.sorted.todo.AVLTree;
import ru.mail.polis.collections.set.sorted.todo.RedBlackTree;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by Nechaev Mikhail
 * Since 07/12/2018.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(value = Parameterized.class)
public class TestBalancedSortedSet extends AbstractTestBalancedSortedSet {

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
    protected Class<?> getTestClass() {
        return testClass;
    }

    @Override
    protected boolean isRemoveEnabled() {
        return removeEnabled;
    }

    @Override
    protected Comparator<Integer> getComparator() {
        return comparator;
    }
}

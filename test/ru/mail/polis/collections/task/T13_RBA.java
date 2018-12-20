package ru.mail.polis.collections.task;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.TestHelper;
import ru.mail.polis.collections.set.sorted.AbstractTestBalancedSortedSet;
import ru.mail.polis.collections.set.sorted.todo.RedBlackTree;

import java.util.Collection;
import java.util.Comparator;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(value = Parameterized.class)
public class T13_RBA extends AbstractTestBalancedSortedSet {

    @Parameterized.Parameter(0)
    public Comparator<Integer> comparator;
    @Parameterized.Parameter(1)
    public String comparatorName;

    @Parameterized.Parameters(name = "comparator is {1}")
    public static Collection<Object[]> data() {
        return TestHelper.COMPARATORS;
    }

    @Override
    protected Class<?> getTestClass() {
        return RedBlackTree.class;
    }

    @Override
    protected boolean isRemoveEnabled() {
        return false;
    }

    @Override
    protected Comparator<Integer> getComparator() {
        return comparator;
    }
}

package ru.mail.polis.collections.task;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.TestHelper;
import ru.mail.polis.collections.set.sorted.AbstractTestBalancedSortedSet;
import ru.mail.polis.collections.set.sorted.todo.AVLTree;

import java.util.Collection;
import java.util.Comparator;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(value = Parameterized.class)
public class T10_AVL extends AbstractTestBalancedSortedSet {

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
        return AVLTree.class;
    }

    @Override
    protected boolean isRemoveEnabled() {
        return true;
    }

    @Override
    protected Comparator<Integer> getComparator() {
        return comparator;
    }
}

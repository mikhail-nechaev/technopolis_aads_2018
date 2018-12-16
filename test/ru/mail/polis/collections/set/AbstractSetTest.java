package ru.mail.polis.collections.set;

import org.junit.Assert;

import java.util.Set;

/**
 * Created by Nechaev Mikhail
 * Since 07/12/2018.
 */
public class AbstractSetTest {

    protected <E> void checkTransformOperation(Set<E> validSet, ISet<E> testSet, E value, TransformOperation transformOperation) {
        if (TransformOperation.ADD == transformOperation) {
            Assert.assertEquals("add", validSet.add(value), testSet.add(value));
        } else if (isRemoveEnabled()) {
            Assert.assertEquals("remove", validSet.remove(value), testSet.remove(value));
        }
    }

    protected <E> void checkSizeAndContains(Set<E> validSet, ISet<E> testSet, E value) {
        Assert.assertEquals("size", validSet.size(), testSet.size());
        Assert.assertEquals("contains", validSet.contains(value), testSet.contains(value));
    }

    protected boolean isRemoveEnabled() {
        return false;
    }

    protected enum TransformOperation {
        ADD, REMOVE
    }
}

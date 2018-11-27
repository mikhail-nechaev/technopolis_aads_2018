package ru.mail.polis.collections.set.sorted;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * @param <E> the type of elements maintained by this set
 */
public interface ISelfBalancingTreeSet<E> extends ISortedSet<E> {

    /**
     * Traverse sorted and check balance
     *
     * @throws UnbalancedTreeException if the sorted is unbalanced
     */
    void checkBalance() throws UnbalancedTreeException;
}

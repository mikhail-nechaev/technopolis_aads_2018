package ru.mail.polis.collections.set.sorted;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * Self balancing sorted tree set
 *
 * @param <E> the type of elements maintained by this set
 */
public interface ISelfBalancingSortedTreeSet<E> extends ISortedSet<E> {

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * Complexity = O(log(n))
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws NullPointerException if the specified element is null
     */
    boolean add(E value);

    /**
     * Removes the specified element from this set if it is present.
     *
     * Complexity = O(log(n))
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    boolean remove(E value);

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * Complexity = O(log(n))
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    boolean contains(E value);

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * Complexity = O(log(n))
     *
     * @return the first (lowest) element currently in this set
     * @throws java.util.NoSuchElementException if this set is empty
     */
    E first();

    /**
     * Returns the last (highest) element currently in this set.
     *
     * Complexity = O(log(n))
     *
     * @return the last (highest) element currently in this set
     * @throws java.util.NoSuchElementException if this set is empty
     */
    E last();

    /**
     * Traverse self balanced tree set and check balance correctness.
     *
     * @throws UnbalancedTreeException if the self balanced tree set is unbalanced
     */
    void checkBalance() throws UnbalancedTreeException;
}

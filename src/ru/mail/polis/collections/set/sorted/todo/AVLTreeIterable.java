package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class AVLTreeIterable<E extends Comparable<E>> extends AVLTree<E> implements ISortedSetIterable<E> {


    /**
     * Adds the specified element to this set if it is not already present.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(E value) {
        return false;
    }

    /**
     * Removes the specified element from this set if it is present.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean remove(E value) {
        return false;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     * <p>
     * Complexity = O(log(n))
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(E value) {
        return false;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {

    }

    /**
     * Returns the first (lowest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() {
        return null;
    }

    /**
     * Returns the last (highest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() {
        return null;
    }

    /**
     * Traverse self balanced tree set and check balance correctness.
     *
     * @throws UnbalancedTreeException if the self balanced tree set is unbalanced
     */
    @Override
    public void checkBalance() throws UnbalancedTreeException {

    }

    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }
}
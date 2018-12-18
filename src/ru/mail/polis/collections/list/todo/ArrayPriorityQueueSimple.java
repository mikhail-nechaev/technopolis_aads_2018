package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
@SuppressWarnings("unchecked")
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    /**
     * Inserts the specified element into this priority queue.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E value) {

    }

    /**
     * Retrieves and removes the head of this queue.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public E remove() {
        return null;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     * <p>
     * Complexity = O(1)
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public E element() {
        return null;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        return null;
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
}
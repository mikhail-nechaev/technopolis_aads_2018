package ru.mail.polis.collections.list;

import ru.mail.polis.collections.ICollectionIterable;

import java.util.Iterator;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * <p>PriorityQueue is <a href="https://en.wikipedia.org/wiki/Binary_heap">heap</a>
 *
 * <p>A binary heap is defined as a binary sorted with two additional constraints:
 * <p> Shape property: a binary heap is a complete binary sorted;
 * that is, all levels of the sorted, except possibly the last one (deepest) are fully filled,
 * and, if the last level of the sorted is not complete, the nodes of that level are filled from left to right.
 * <p>Heap property: the key stored in each node is either
 * greater than or equal to
 * or less than or equal to
 * the keys in the node's children, according to some total order.
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public interface IPriorityQueue<E extends Comparable<E>> extends ICollectionIterable<E> {

    /**
     * Inserts the specified element into this priority queue.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    void add(E value);

    /**
     * Retrieves and removes the head of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    E remove();

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    E element();

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    Iterator<E> iterator();
}

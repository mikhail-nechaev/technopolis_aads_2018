package ru.mail.polis.collections.list;

import java.util.Iterator;

import ru.mail.polis.collections.ICollectionIterable;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * Queue is FIFO - First In First Out
 * <p>
 * [get/remove]First - [head/first]-[...]-...-[...]-[tail/last] - [add]Last
 *
 * @param <E> the type of elements maintained by this queue
 */
public interface IQueue<E> extends ICollectionIterable<E> {

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    void addLast(E value);

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    E getFirst();

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E removeFirst();

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    Iterator<E> iterator();
}

package ru.mail.polis.collections.list;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * Deque is LIFO + FIFO - Last/First In First Out
 * <p>
 * [add/get/remove]First - [head/first]-[...]-...-[...]-[tail/last] - [add/get/remove]Last
 *
 * @param <E> the type of elements maintained by this deque
 */
public interface IDeque<E> extends IQueue<E> {

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    void addFirst(E value);

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E getLast();

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E removeLast();
}

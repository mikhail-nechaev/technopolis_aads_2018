package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    /**
     * Elements of deque
     */
    private Object[] deque = new Object[16];

    /**
     * Numbers of elements
     */

    private int N = 0;

    /**
     * The index of start deque
     */

    private int first = 0;

    /**
     * The index of end deque
     */

    private int last = 0;

    public static void main(String[] args) {
        new ArrayDequeSimple<Integer>().clear();
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (N == 0) {
            throw new UnsupportedOperationException("todo: implement this");
        }
        N--;
        return (E) deque[first++];
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (N == 0) {
            throw new UnsupportedOperationException("todo: implement this");
        }
        return (E) deque[first];
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (N == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        if (last == 0) {
            N--;
            last = first + N - 1;
            return (E) deque[0];
        } else {
            N--;
            return (E) deque[last--];
        }
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (N == 0) {
            throw new NoSuchElementException("Deque is empty");
        }

        return (E) deque[last];
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
        if (value == null) {
            throw new NullPointerException("Specified element is null");
        }

        final Object[] res = deque;

        if (last >= first) {
            for (int i = first; i < last; i++) {
                if (value.equals(res[i])) {
                    return true;
                }
            }
        } else {
            for (int i = first; i < first + N - last - 1; i++) {
                if (value.equals(res[i])) {
                    return true;
                }
            }
            for (int i = 0; i < last + 1; i++) {
                if (value.equals(res[i])) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return this.N;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return N > 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        N = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("todo: implement this");
    }
}

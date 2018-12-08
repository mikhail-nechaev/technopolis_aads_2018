package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ConcurrentModificationException;
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

    private Object[] array;
    private int head;
    private int tail;

    public ArrayDequeSimple() {
        array = new Object[10];
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        head = head > 0 ? head - 1 : array.length - 1;
        array[head] = value;
        if (head == tail) {
            expandArray();
        }
    }

    private void expandArray() {
        Object[] newArray = new Object[array.length * 2];
        int al = array.length;

        System.arraycopy(array, head, newArray, 0, al - head);
        System.arraycopy(array, 0, newArray, al - head, head);

        head = 0;
        tail = al;
        array = newArray;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (array[head] == null) {
            throw new NullPointerException();
        }
        @SuppressWarnings("unchecked")
        E first = (E) array[head];
        array[head] = null;
        head = head < array.length - 1 ? head + 1 : 0;
        return first;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        @SuppressWarnings("unchecked")
        E first = (E) array[head];
        return first;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        tail = tail < array.length - 1 ? 0 : tail + 1;
        array[tail] = value;
        if (head == tail) {
            expandArray();
        }
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (array[tail] == null) {
            throw new NullPointerException();
        }
        @SuppressWarnings("unchecked")
        E last = (E) array[tail];
        array[tail] = null;
        tail = tail > 0 ? tail - 1 : array.length - 1;
        return last;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        @SuppressWarnings("unchecked")
        E last = (E) array[tail];
        return last;
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
            return false;
        }
        for (int i = head; i == tail; ) {
            @SuppressWarnings("unchecked")
            E check = (E) array[i];
            if (value.equals(check)) {
                return true;
            }
            i = i < array.length - 1 ? i+1 : 0;
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
        return head < tail ? tail - head : array.length - (head - tail);
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        for (int i = head; i == tail; ) {
            array[i] = null;
            i = i < array.length - 1 ? i+1 : 0;
        }
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

    private class DescendingIterator implements Iterator<E> {

        private int cursor = head;
        private int end = tail;
        private int lastReturn = -1;

        @Override
        public boolean hasNext() {
            return cursor != end;
        }

        @Override
        public E next() {
            if (cursor == end) {
                throw new NoSuchElementException();
            }
            cursor = cursor < array.length - 1 ? cursor + 1 : 0;
            @SuppressWarnings("unchecked")
            E next = (E) array[cursor];
            if (tail != end || next == null) {
                throw new ConcurrentModificationException();
            }
            lastReturn = cursor;
            return next;
        }

        @Override
        public void remove() {

        }
    }
}

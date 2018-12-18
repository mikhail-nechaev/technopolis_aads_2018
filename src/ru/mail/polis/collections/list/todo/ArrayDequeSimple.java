package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    private static final int INITIAL_CAPACITY = 8;

    Object[] array;

    int head;
    int tail;

    int size;

    public ArrayDequeSimple() {
        init();
    }

    private void init() {
        this.array = new Object[INITIAL_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    private void grow() {
        if (array.length == Integer.MAX_VALUE) {
            throw new IllegalStateException("Deque is too big");
        }
        Object[] newArray = new Object[array.length * 2];
        for (int i = 0; i < array.length-1; i++) {
            newArray[i] = array[head];
            head = (head + 1) % array.length;
        }
        head = 0;
        tail = array.length;
        array = newArray;
    }

    private void throwExceptionIfEmpty() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty");
        }
    }

    private void growIfNecessary() {
        if (size == array.length) {
            grow();
        }
    }

    protected void throwExceptionIfArgumentIsNull(Object value) {
        if (value == null) {
            throw new NullPointerException("Can't store null in ArrayDeque");
        }
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        throwExceptionIfArgumentIsNull(value);
        growIfNecessary();
        head = (head - 1 + array.length) % array.length;
        array[head] = value;
        size++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        throwExceptionIfEmpty();
        Object tmp = array[head];
        array[head] = null;
        head = (head + 1) % array.length;
        size--;
        return (E) tmp;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        throwExceptionIfEmpty();
        return (E) array[head];
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        throwExceptionIfArgumentIsNull(value);
        growIfNecessary();
        array[tail] = value;
        tail = (tail + 1) % array.length;
        size++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        throwExceptionIfEmpty();
        Object tmp = array[(tail - 1 + array.length) % array.length];
        array[(tail - 1 + array.length) % array.length] = null;
        tail = (tail - 1 + array.length) % array.length;
        size--;
        return (E) tmp;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        throwExceptionIfEmpty();
        return (E) array[(tail - 1 + array.length) % array.length];
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
        throwExceptionIfArgumentIsNull(value);
        for (Object o: array) {
            if (Objects.equals(o, value)) {
                return true;
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
        return size;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        init();
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<E> {

        private int ptr;
        private boolean methodNextWasCalled;
        private int stepsMade;

        ArrayDequeIterator() {
            ptr = (head - 1 + array.length) % array.length;
            stepsMade = 0;
            methodNextWasCalled = false;
        }

        @Override
        public boolean hasNext() {
            return stepsMade < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            ptr = (ptr + 1) % array.length;
            stepsMade++;
            methodNextWasCalled = true;
            return (E) array[ptr];
        }


        @Override
        public void remove() {
            if (!methodNextWasCalled) {
                throw new IllegalStateException();
            }
            methodNextWasCalled = false;
            Object[] newArray = new Object[array.length];
            int newPtrPosition = 0;
            for (int i = 0; i < array.length-2; i++) {
                if (head == ptr) {
                    head = (head + 1) % array.length;
                    newPtrPosition = i - 1;
                }
                newArray[i] = array[head];
                head = (head + 1) % array.length;
            }
            ptr = newPtrPosition;
            head = 0;
            size--;
            tail = size;
            array = newArray;
            stepsMade--;
        }
    }
}

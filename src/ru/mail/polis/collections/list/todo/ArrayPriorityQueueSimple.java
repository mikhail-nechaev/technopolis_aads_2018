package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.Arrays;
import java.util.Collection;
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
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;

    private final static int INITIAL_CAPACITY = 8;

    private Object[] array;
    private int size;

    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection.
     *
     * You may consider that all elements in collection is not a null.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @throws NullPointerException if the specified collection is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection) {
        this(collection, Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        init();
    }

    private void init() {
        array = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection
     *  that orders its elements according to the specified comparator.
     *
     * You may consider that all elements in collection is not a null.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        size = collection.size();
        array = Arrays.copyOf(collection.toArray(), collection.size()); // не степень двойки
        for (int i = array.length / 2; i > -1 ; i--) {
            siftDown(i);
        }

    }

    private void siftDown(int i) {
        while (2 * i + 1 < size) {
            int left = 2* i + 1;
            int right = 2* i + 2;
            int j = left;
            if (right < size && comparator.compare((E)array[right], (E)array[left]) < 0) {
                j = right;
            }
            if (comparator.compare((E) array[i], (E) array[j]) <= 0) {
                break;
            }
            Object tmp = array[i];
            array[i] = array[j];
            array[j] = (E) tmp;
            i = j;
        }
    }

    //??? условие при выходе в корень
    private void siftUp(int i) {
        while ((comparator.compare((E) array[i], (E) array[(i - 1) / 2]) < 0) && (i > 0)) {
            Object tmp = array[i];
            array[i] = array[(i - 1) / 2];
            array[(i - 1) / 2] = (E) tmp;
            i = (i - 1) / 2;
        }
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * Complexity = O(log(n))
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E value) {
        throwExceptionIfArgumentIsNull(value);
        if (size == array.length) {
            grow();
        }
        array[size] = value;
        siftUp(size);
        size++;
    }

    private void grow() {
        if (array.length * 2 < 0) {
            throw new IllegalStateException();
        }
        array = Arrays.copyOf(array, array.length * 2);
    }

    /**
     * Retrieves and removes the head of this queue.
     *
     * Complexity = O(log(n))
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E remove() {
        E tmp = element();
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;
        siftDown(0);
        return tmp;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * Complexity = O(1)
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return (E) array[0];
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * Complexity = O(n)
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

    private void throwExceptionIfArgumentIsNull(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
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
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<E> {

        private int pointer;
        private boolean methodNextWasCalled;

        public HeapIterator() {
            methodNextWasCalled = false;
            pointer = -1;
        }

        @Override
        public boolean hasNext() {
            return pointer < size - 1;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            pointer++;
            methodNextWasCalled = true;
            return (E) array[pointer];
        }

        @Override
        public void remove() {
            if (!methodNextWasCalled) {
                throw new IllegalStateException();
            }
            methodNextWasCalled = false;
            array[pointer] = array[size - 1];
            array[size - 1] = null;
            size--;
            siftDown(pointer);
            pointer--;
        }
    }
}

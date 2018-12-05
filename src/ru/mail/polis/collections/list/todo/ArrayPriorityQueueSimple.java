package ru.mail.polis.collections.list.todo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import ru.mail.polis.collections.list.IPriorityQueue;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {
    private E[] data;
    private final Comparator<E> comparator;
    private int size = 0;
    private int modCount = 0;

    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection.
     * <p>
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
        data = (E[]) new Comparable[10];
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection
     * that orders its elements according to the specified comparator.
     * <p>
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        if (collection == null) {
            throw new NullPointerException();
        }
        data = (E[]) new Comparable[collection.size() * 2];
        for (E element : collection) {
            data[size++] = element;
        }
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }
    }

    private boolean greaterOrEqual(int i, int j) {
        return comparator == null
                ? data[i].compareTo(data[j]) >= 0
                : comparator.compare(data[i], data[j]) >= 0;
    }

    public void siftDown(int i) {
        int minIndex = i;
        if (2 * i + 1 < size() && greaterOrEqual(minIndex, 2 * i + 1)) {
            minIndex = 2 * i + 1;
        }
        if (2 * i + 2 < size() && greaterOrEqual(minIndex, 2 * i + 2)) {
            minIndex = 2 * i + 2;
        }
        if (minIndex != i) {
            swap(data, i, minIndex);
            siftDown(minIndex);
        }
    }

    public void siftUp(int i) {
        if (i != 0) {
            final int parent = (i - (2 - i & 1)) / 2;
            if (greaterOrEqual(i, parent)) {
                return;
            } else {
                swap(this.data, i, parent);
                siftUp(parent);
            }
        }
    }


    public void swap(E[] a, int i, int j) {
        E tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private void resize(int newSize) {
        data = Arrays.copyOf(data, newSize);
    }

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
        if (size() == data.length) {
            resize(data.length << 1);
        }
        data[size()] = Objects.requireNonNull(value);
        siftUp(size());
        size++;
        modCount++;
    }

    /**
     * Retrieves and removes the head of this queue.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E result = data[0];
        swap(data, 0, size() - 1);
        size--;
        siftDown(0);
        modCount++;
        return result;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     * <p>
     * Complexity = O(1)
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return data[0];
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     * <p>
     * Complexity = O(n)
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (data[i].equals(value)) {
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
        return size() == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayPriorityQueueIterator();
    }

    private class ArrayPriorityQueueIterator implements Iterator<E> {
        private int currentPosition = 1;
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return currentPosition <= size();
        }

        @Override
        public E next() {
            if (currentPosition > size()) {
                throw new NoSuchElementException();
            }
            E tmp = data[currentPosition - 1];
            currentPosition++;
            return tmp;
        }

        @Override
        public void remove() {
            checkForComodification();
            if(currentPosition == 2) {
                throw new IllegalStateException();
            }
            swap(data, currentPosition - 2, size() - 1);
            for (int i = size / 2; i >= 0; i--) {
                siftDown(i);
            }
            modCount++;
            expectedModCount++;
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}

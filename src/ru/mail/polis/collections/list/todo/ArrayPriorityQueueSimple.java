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
    private Object[] array;
    private int size;
    private int capacity = 8;
    private final Comparator<E> comparator;

    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection.
     * <p>
     * You may consider that all elements in collection is not a null.
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
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        this.array = new Object[capacity];
        this.size = 0;
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection
     * that orders its elements according to the specified comparator.
     * <p>
     * You may consider that all elements in collection is not a null.
     * <p>
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        if (collection == null || comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        this.size = collection.size();
        this.capacity = size;
        System.arraycopy(collection, 0, array, 0, size);
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }

    }

    private Object swap(Object x, Object y) {
        return x;
    }

    private void siftDown(int index) {
        if (index > size / 2) {
            return;
        }
        int newIndex = index;
        int firstChild = 2 * index + 1;
        int secondChild = 2 * index + 2;
        if (firstChild < size && comparator.compare((E) array[newIndex], (E) array[firstChild]) > 0) {
            newIndex = firstChild;
        }
        if (secondChild < size && comparator.compare((E) array[newIndex], (E) array[secondChild]) > 0) {
            newIndex = secondChild;
        }
        if (newIndex != index) {
            array[newIndex] = swap(array[index], array[index] = array[newIndex]);
            siftDown(newIndex);
        }
    }

    private void siftUp(int index) {
        if (index == 0) {
            return;
        }
        int parent = (index - 1) / 2;
        if (comparator.compare((E) array[parent], (E) array[index]) > 0) {
            array[parent] = swap(array[index], array[index] = array[parent]);
            siftUp(parent);
        }
    }

    private void resize() {
        array = Arrays.copyOf(array, capacity <<= 1);
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
        if (value == null) {
            throw new NullPointerException();
        }
        if (size == capacity) {
            resize();
        }
        array[size] = value;
        siftUp(size);
        size++;


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
        size--;
        array[0] = swap(array[size], array[size] = array[0]);
        siftDown(0);
        return (E) array[size];
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
        return (E) array[0];
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
        for (int i = 0; i < size; i++) {
            if (value.equals(array[i])) {
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
        size = 0;
    }

    private void removeElement(int index) {
        size--;
        array[index] = swap(array[size], array[size] = array[index]);
        siftDown(index);
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int nextIndex = 0;
            private int lastNextIndex = -1;
            private int size = size();

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                lastNextIndex = nextIndex++;
                return (E) array[lastNextIndex];
            }

            @Override
            public void remove() {
                if (lastNextIndex == -1) {
                    throw new IllegalStateException();
                }
                removeElement(lastNextIndex);
                lastNextIndex = -1;
            }
        };
    }
}

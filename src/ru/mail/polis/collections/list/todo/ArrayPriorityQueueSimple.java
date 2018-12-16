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
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {
    int size;
    private final E VAL = null;
    private final int DEFAULT_SIZE = 10;
    private Object[] a;
    private final Comparator<E> comparator;
    private int[] powers;

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
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        a = new Object[DEFAULT_SIZE];
        size = 0;
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
        if (collection == null || comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        a = collection.toArray();
        powers = new int[30];
        int index = 1;
        while (a.length > index) {
            index *= 2;
        }

        index /= 2;
        int beginIndex = index >= a.length ? a.length - 1 : index;
        for (int i = beginIndex; i > -1; i--) {
            siftDown(i);
        }
        size = a.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Object o : a) {
            sb.append(o).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    private void swap(int i, int j) {
        Object tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private int compare(Object o1, Object o2) {
        return comparator.compare((E) o1, (E) o2);
    }

    private void siftDown(int i) {
        int first = 2 * i + 1 < a.length && a[2 * i + 1] != VAL ? 2 * i + 1 : -1;
        int sec = 2 * i + 2 < a.length && a[2 * i + 2] != VAL ? 2 * i + 2 : -1;
        if (first == -1 && sec == -1) {
            return;
        }

        if (first == -1) {
            if (compare(a[i], a[sec]) > 0) {
                swap(i, sec);
                siftDown(sec);
            }
            return;
        }

        if (sec == -1) {
            if (compare(a[i], a[first]) > 0) {
                swap(i, first);
                siftDown(first);
            }
            return;
        }

        int minIndex = compare(a[first], a[sec]) > 0 ? sec : first;
        if (compare(a[i], a[minIndex]) > 0) {
            swap(i, minIndex);
            siftDown(minIndex);
        }

    }

    private void siftUp(int i) {
        if (i <= 0) {
            return;
        }
        int parent = i % 2 == 0 ? (i - 2) / 2 : (i - 1) / 2;
        if (compare(a[parent], a[i]) > 0) {
            swap(i, parent);
            siftUp(parent);
        }
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
        a[size] = value;
        siftUp(size);
        size++;

        if (isFull()) {
            increaseHeap();
        }
    }

    private void increaseHeap() {
        a = Arrays.copyOf(a, a.length << 1);
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
        E root = element();
        size--;

        a[0] = a[size];
        a[size] = VAL;
        siftDown(0);

        if (isSmall()) {
            reductionHeap();
        }
        return root;
    }

    private void reductionHeap() {
        a = Arrays.copyOf(a, size);
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
        return (E) a[0];
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
        for (int i = 0; i < a.length; i++) {
            if (value.equals(a[i])) {
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

    private boolean isFull() {
        return size == a.length;
    }

    private boolean isSmall() {
        return size <= a.length / 2 && size > 10;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        a = new Object[DEFAULT_SIZE];
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
        return new Iterator<E>() {

            int nextIndex = 0;
            int lastReturnedIndex = -1;
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                lastReturnedIndex = nextIndex;
                nextIndex++;
                canRemove = true;
                return (E) a[lastReturnedIndex];
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                canRemove = false;
                size--;
                a[lastReturnedIndex] = a[size];
                a[size] = VAL;
                siftDown(lastReturnedIndex);
                nextIndex = lastReturnedIndex;
            }
        };
    }
}

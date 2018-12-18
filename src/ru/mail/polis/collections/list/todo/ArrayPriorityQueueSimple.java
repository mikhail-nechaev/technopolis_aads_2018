package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.*;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on data priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private int count;
    private Object[] data;
    private final int DEFAULT = 16;
    private final Comparator<E> comparator;

    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates data {@code IPriorityQueue} containing the elements in the specified collection.
     * <p>
     * You may consider that all elements in collection is not data null.
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
     * Creates data {@code IPriorityQueue} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        data = new Object[DEFAULT];
        count = 0;
    }

    /**
     * Creates data {@code IPriorityQueue} containing the elements in the specified collection
     * that orders its elements according to the specified comparator.
     * <p>
     * You may consider that all elements in collection is not data null.
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
        data = collection.toArray();
        int index = 1;
        while (data.length > index) {
            index *= 2;
        }
        index /= 2;
        int beginIndex = index >= data.length ? data.length - 1 : index;
        for (int i = beginIndex; i > -1; i--) {
            siftDown(i);
        }
        count = data.length;
    }

    private void swap(int i, int j) {
        Object tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private int compare(Object o1, Object o2) {
        return comparator.compare((E) o1, (E) o2);
    }

    @SuppressWarnings("Duplicates")
    private void siftDown(int i) {
        int first = 2 * i + 1 < data.length && data[2 * i + 1] != null ? 2 * i + 1 : -1;
        int sec = 2 * i + 2 < data.length && data[2 * i + 2] != null ? 2 * i + 2 : -1;
        if (first == -1 && sec == -1) {
            return;
        }

        if (first == -1) {
            if (compare(data[i], data[sec]) > 0) {
                swap(i, sec);
                siftDown(sec);
            }
            return;
        }

        if (sec == -1) {
            if (compare(data[i], data[first]) > 0) {
                swap(i, first);
                siftDown(first);
            }
            return;
        }

        int minIndex = compare(data[first], data[sec]) > 0 ? sec : first;
        if (compare(data[i], data[minIndex]) > 0) {
            swap(i, minIndex);
            siftDown(minIndex);
        }

    }

    private void siftUp(int i) {
        if (i <= 0) {
            return;
        }
        int parent = i % 2 == 0 ? (i - 2) / 2 : (i - 1) / 2;
        if (compare(data[parent], data[i]) > 0) {
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
        if (count == data.length) {
            data = Arrays.copyOf(data, data.length << 1);
        }
        data[count] = value;
        siftUp(count);
        count++;
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
        count--;

        data[0] = data[count];
        data[count] = null;
        siftDown(0);

        if (count <= data.length / 2 && count > 10) {
            data = Arrays.copyOf(data, count);
        }
        return root;
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
        return (E) data[0];
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, NULVAL) == true}
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
        for (Object o : data) {
            if (value.equals(o)) {
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
        return count;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        data = new Object[DEFAULT];
        count = 0;
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
                return nextIndex < count;
            }

            @Override
            public E next() {
                lastReturnedIndex = nextIndex;
                nextIndex++;
                canRemove = true;
                return (E) data[lastReturnedIndex];
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                canRemove = false;
                count--;
                data[lastReturnedIndex] = data[count];
                data[count] = null;
                siftDown(lastReturnedIndex);
                nextIndex = lastReturnedIndex;
            }
        };
    }
}
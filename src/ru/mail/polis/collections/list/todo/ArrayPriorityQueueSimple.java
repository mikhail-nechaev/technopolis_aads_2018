package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.*;

/**
 * Resizable queue implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - expand as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private Object[] queue;
    private int size;
    private final int DEFAULT_SIZE = 10;
    private final Comparator<E> comparator;

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
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        queue = new Object[DEFAULT_SIZE];
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
        if (collection == null || comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        queue = new Object[DEFAULT_SIZE];
        size = 0;
        for (E elem : collection) {
            add(elem);
        }
    }

    private void expand() {
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity << 1;
        queue = Arrays.copyOf(queue, newCapacity);
    }

    private void siftUp(int k, E value) {
        while (k > 0) {
            int parent = k % 2 == 0 ? (k - 2) / 2 : (k - 1) / 2;
            @SuppressWarnings("unchecked")
            E parentValue = (E) queue[parent];
            if (comparator.compare(value, parentValue) >= 0) {
                break;
            }
            queue[k] = parentValue;
            k = parent;
        }
        queue[k] = value;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E value) {
        int halfSize = size % 2 == 0 ? size / 2 : (size - 1) / 2;
        while (k < halfSize) {
            int left = 2 * k + 1;
            int right = left + 1;
            int childIndex = left;
            E childValue = (E) queue[childIndex];
            if (right < size && comparator.compare(childValue, (E) queue[right]) > 0) {
                childValue = (E) queue[right];
                childIndex = right;
            }
            if (comparator.compare(value, childValue) <= 0) {
                break;
            }
            queue[k] = childValue;
            k = childIndex;
        }
        queue[k] = value;
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
        if (value == null) {
            throw new NullPointerException();
        }
        int i = size;
        if (i >= queue.length) {
            expand();
        }
        siftUp(i, value);
        size++;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E head = element();

        queue[0] = queue[size - 1];
        siftDown(0, (E) queue[0]);
        size--;
        return head;
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
            throw new NoSuchElementException();
        }
        return (E) queue[0];
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
        if (value == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < queue.length; i++) {
            if (value.equals(queue[i])) {
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
        queue = new Object[DEFAULT_SIZE];
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
        return new PriorityQueueIterator();
    }

    private final class PriorityQueueIterator implements Iterator<E> {

        private int cursor;
        private int lastReturned = -1;
        boolean canRemove = false;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            lastReturned = cursor;
            cursor++;
            canRemove = true;
            return (E) queue[lastReturned];
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            queue[lastReturned] = queue[size--];
            siftDown(lastReturned, (E) queue[lastReturned]);
        }
    }
}

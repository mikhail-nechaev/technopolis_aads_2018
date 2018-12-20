package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.*;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;

    private static final int DEFAULT_CAPASITY = 8;
    private Object[] elements = new Object[DEFAULT_CAPASITY];
    int size;
    int capasity = DEFAULT_CAPASITY;

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
        if (comparator == null) throw new NullPointerException();
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        this.elements = new Object[capasity];
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
        if (collection == null || comparator == null) throw new NullPointerException();
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        this.size = collection.size();
        this.capasity = size;
        this.elements = new Object[capasity];
        System.arraycopy(collection.toArray(), 0, elements, 0, size);
        for (int i = size / 2 - 1; i >= 0; i--) {
            shiftDown(i);
        }
    }

    private void shiftDown(int index) {
        if (index > size / 2) return;
        int newIndex = index;
        int firstChild = 2 * index + 1;
        int secondChild = 2 * index + 2;
        if (firstChild < size && comparator.compare((E) elements[newIndex], (E) elements[firstChild]) > 0)
            newIndex = firstChild;
        if (secondChild < size && comparator.compare((E) elements[newIndex], (E) elements[secondChild]) > 0)
            newIndex = secondChild;
        if (newIndex != index) {
            elements[newIndex] = swap(elements[index], elements[index] = elements[newIndex]);
            shiftDown(newIndex);
        }
    }

    private Object swap(Object x, Object y) {
        return x;
    }

    private void shiftUp(int index) {
        if (index == 0) {
            return;
        }
        int parent = (index - 1) / 2;
        if (comparator.compare((E) elements[parent], (E) elements[index]) > 0) {
            elements[parent] = swap(elements[index], elements[index] = elements[parent]);
            shiftUp(parent);
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
        if (value == null) throw new NullPointerException();
        if (size == capasity) resize();
        elements[size] = value;
        shiftUp(size);
        size++;
    }

    private void resize() {
        elements = Arrays.copyOf(elements, capasity <<= 1);
    }

    /**
     * Retrieves and removes the head of this queue.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public E remove() {
        if (isEmpty()) throw new NoSuchElementException();
        size--;
        elements[0] = swap(elements[size], elements[size] = elements[0]);
        shiftDown(0);
        return (E) elements[size];
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     * <p>
     * Complexity = O(1)
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public E element() {
        if (isEmpty()) throw new NoSuchElementException();
        return (E) elements[0];
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
        if (value == null) throw new NullPointerException();
        for (int i = 0; i < size; i++) {
            if (comparator.compare((E) elements[i], value) == 0) {
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
        shiftDown(index);
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
                if (!hasNext()) throw new NoSuchElementException();
                lastNextIndex = nextIndex++;
                return (E) elements[lastNextIndex];
            }

            @Override
            public void remove() {
                if (lastNextIndex == -1) throw new IllegalStateException();
                removeElement(lastNextIndex);
                lastNextIndex = -1;
            }
        };
    }
}

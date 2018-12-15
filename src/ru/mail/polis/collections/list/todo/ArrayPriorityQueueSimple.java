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

    private static final int DEFAULT_LENGTH = 10;
    private Object[] values = new Object[DEFAULT_LENGTH];
    private int lenght = 0;

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
        Iterator<E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            add(iterator.next());
        }
        //todo: do some stuff with collection
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
        values[lenght] = value;
        shiftUp(lenght);
        lenght++;
        checkCapacity();
    }

    private void shiftUp(int children) {
        int index = children;
        int parent = index % 2 == 0 ? (index - 2) / 2 : (index - 1) / 2;
        while (parent >= 0 && comparator.compare((E) values[index], (E) values[parent]) < 0) {
            E copy = (E) values[index];
            values[index] = values[parent];
            values[parent] = copy;
            index = parent;
            parent = index % 2 == 0 ? (index - 2) / 2 : (index - 1) / 2;
        }
    }

    private void shiftDown(int children) {
        int index = children;
        while (index * 2 + 1 < lenght) {
            if (index * 2 + 2 < values.length && comparator.compare((E) values[index * 2 + 2], (E) values[index * 2 + 1]) < 0) {
                E copy = (E) values[index];
                values[index] = values[index * 2 + 2];
                values[index * 2 + 2] = copy;
                index = index * 2 + 2;
            } else {
                E copy = (E) values[index];
                values[index] = values[index * 2 + 1];
                values[index * 2 + 1] = copy;
                index = index * 2 + 1;
            }
        }
    }

    private void updateArraySize(int newSize) {
        if (newSize <= DEFAULT_LENGTH) {
            if (values.length != DEFAULT_LENGTH) {
                values = Arrays.copyOf(values, DEFAULT_LENGTH);
            }
        } else {
            values = Arrays.copyOf(values, newSize);
        }
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
        E result = (E) values[0];
        values[0] = values[lenght - 1];
        lenght--;
        shiftDown(0);
        checkCapacity();
        return result;
    }

    private void checkCapacity() {
        if (lenght + 1 >= values.length) {
            updateArraySize(values.length * 2);
        }
        if (lenght * 2 <= values.length - 1) {
            updateArraySize(values.length / 2);
        }
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
        return (E) values[0];
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
        for (int i = 0; i < lenght; i++) {
            if (comparator.compare((E) values[i], value) == 0) {
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
        return lenght;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return lenght == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        lenght = 0;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        Iterator<E> iterator = new Iterator<E>() {
            int index = 0;
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                canRemove = false;
                return index < size();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                canRemove = true;
                return (E) values[index++];
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                values[index - 1] = values[size() - 1];
                shiftDown(index - 1);
                lenght--;
                canRemove = false;
            }
        };
        return iterator;
    }
}
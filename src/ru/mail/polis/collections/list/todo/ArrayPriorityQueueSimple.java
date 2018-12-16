package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

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
@SuppressWarnings("unchecked")
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {
    private Comparator<E> comparator;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 50;
    /**
     * Isn`t fixed, expandable with resize method
     */
    private Comparable<E>[] heap;

    /**
     * Array for the heap
     */

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
        this(Comparator.naturalOrder());
        if (collection == null) {
            throw new NullPointerException();
        }
        this.size = collection.size();
        //Sift-up to minimize
        for (E item : collection) {
            add(item);
        }
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
        size = 0;
        heap = new Comparable[DEFAULT_CAPACITY + 1];
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
        this(comparator);
        this.size = collection.size();
        for (E item : collection) {
            add(item);
        }
    }

    /**
     * Compares left and right nodes during heap-walking
     */
    private int compare(Comparable<E> left, Comparable<E> right) {

        return comparator.compare((E) left, (E) right);

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
        if (this.size >= heap.length) {
            this.resize();
        }
        heap[this.size] = value;
        sink(size);
        size++;
    }

    private void sink(int index) {
        if (index <= 0) {
            return;
        }
        //int parrentIndex = (index - 1) / 2;
        int parentIndex = index % 2 == 0 ? (index - 2) / 2 : (index - 1) / 2;
        if (compare(heap[index], heap[parentIndex])< 0) {
            E changed = (E) heap[parentIndex];
            heap[parentIndex] = heap[index];
            heap[index] = changed;
            sink(parentIndex);
        }
    }

    private void resize() {
        E[] newHeap;
        newHeap = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < heap.length; i++) {
            newHeap[i] = (E) heap[i];
        }
        heap = newHeap;
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
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        E removed = (E) heap[0];
        heap[0] = heap[size - 1];
        swim(0);
        size--;
        return removed;
    }

    private int childrens(int index) {
        if ((index * 2 + 1) >= size) {
            return 0;
        }

        if ((index * 2 + 1) == (size - 1)) {
            return 1;
        }

        return 2;
    }

    private void swim(int sinkerIndex) {
        int childIndex = sinkerIndex * 2 + 1;

        switch (childrens(sinkerIndex)) {
        case 2:
            if (compare(heap[childIndex], heap[sinkerIndex * 2 + 2]) > 0) {
                childIndex = (sinkerIndex * 2 + 2);
            }
            break;
        case 0:
            return;
        }


        if (compare(heap[sinkerIndex], heap[childIndex]) > 0) {
            E changed;
            changed = (E) heap[childIndex];
            heap[childIndex] = heap[sinkerIndex];
            heap[sinkerIndex] = changed;
            swim(childIndex);

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
    @SuppressWarnings("unchecked")
    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E head = (E) heap[0];
        return head;
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
        boolean flag = false;
        for (int i = 0; i < this.size; i++) {
            if (this.heap[i] == value) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        if (this.heap == null) {
            throw new NullPointerException();
        }
        return this.size;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        this.size = 0;
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

    private class PriorityQueueIterator implements Iterator<E> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public E next() {
            if (hasNext()) {
                E result = (E) heap[index];
                index++;
                return result;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {

        }
    }
}

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
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {
    private Comparator<E> comparator;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 50; /** Isn`t fixed, expandable with resize method */
    private Comparable<E> [] heap;                              /** Array for the heap */

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
        this(Comparator.naturalOrder());
        if (collection == null) throw new NullPointerException();
        this.size = collection.size();
        for (E item : collection)
            add(item);
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
        size = 0;
        heap = new Comparable[DEFAULT_CAPACITY + 1];
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
        this(comparator);
        this.size = collection.size();
        for (E item : collection)
            add(item);
    }

    /**
     * Compares left and right nodes during heap-walking
     */
    private int compare(Comparable<E> left, Comparable<E> right) {
        if (comparator == null) return ((Comparable)left).compareTo((Comparable)right);
        else
            return comparator.compare((E) left, (E) right);
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
        if (value == null) throw new NullPointerException();
        if (this.size >= heap.length) this.resize();
        heap[this.size] = value;
        swim(size);
        size++;
    }

    private void swim (int index) {
        int parentIndex = (index - 1) / 2;
        if (compare (heap[index], heap[parentIndex]) == 1){
            E changed;
            changed = (E) heap[parentIndex];
             heap[parentIndex] = heap[index];
             heap[index] = changed;
             swim(parentIndex);
        }
    }

    private void resize() {
        E [] newHeap;
        newHeap = (E[]) new Object[heap.length * 2];
        for (int i = 0; i < heap.length; i++)
            newHeap[i] = (E) heap[i];
        heap = newHeap;
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
        if (this.size == 0) throw new NoSuchElementException();
        E removed = (E) heap[0];
        heap[0] = heap[size - 1];
        sink(0);
        size--;
        return removed;
    }

    private void sink(int sinkerIndex) {
        int childIndex;
        if ((sinkerIndex * 2 + 1) < (sinkerIndex * 2 + 2))
            childIndex = (sinkerIndex * 2) + 1;
        else
            childIndex = (sinkerIndex * 2 + 2);
        if (compare(heap[sinkerIndex], heap[childIndex]) == -1) {
            E changed;
            changed =(E)  heap[childIndex];
            heap[childIndex] = heap[sinkerIndex];
            heap[sinkerIndex] = changed;
            sink(childIndex);
        }
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
        if (this.heap == null) throw new NoSuchElementException();
        E head = (E)  heap[0];
        return head;
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
        if (value == null) throw new NullPointerException();
        boolean flag = false;
        for (int i = 0; i < this.size; i++)
            if (this.heap[i] == value) {
                flag = true;
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
        if (this.heap == null) throw new NullPointerException();
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
            }
            else
                throw new NoSuchElementException();
        }
    }
}

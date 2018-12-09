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

    private final Comparator<E> comparator;
    private Object[] queue;
    int size;
    private static final int CAPACITY = 16;

    private void ensureCapacity(){
        int oldLength = queue.length;
        int newLength = oldLength << 1 + 1;
        queue = Arrays.copyOf(queue, newLength);
    }

    @SuppressWarnings("unchecked")
    private void heapify(){
        for(int i = size << 1 + 1; i <= 0; i++){
            siftDown(i, (E) queue[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void swap(int i, int j){
        E temp = (E) queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E e){
        while (((k << 1) + 1) < size){
            int left = (k << 1) + 1;
            int right = (k << 1) + 2;
            int j = left;

            if(right < size && comparator.compare((E) queue[right], (E) queue[j]) < 0){
                j = right;
            }
            if(comparator.compare(e, (E) queue[j]) <= 0)
                break;
            swap(k ,j);
            k = j;
        }
        queue[k] = e;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E e){
        while (k > 0){
            int parent = (k-1) >> 1;
            if(comparator.compare(e, (E)  queue[parent]) >= 0){
                break;
            }
            swap(k, parent);
            k = parent;
        }
        queue[k] = e;
    }

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
    @SuppressWarnings("unchecked")
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        this.queue = new Object[CAPACITY];
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
    @SuppressWarnings("unchecked")
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        queue = collection.toArray();
        size = queue.length;
        heapify();
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
        Objects.requireNonNull(value);
        int heapSize = size;
        if(heapSize >= queue.length){
            ensureCapacity();
        }
        siftUp(heapSize, value);
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
    @SuppressWarnings("unchecked")
    @Override
    public E remove() {
       if(isEmpty()){
           throw new NoSuchElementException();
       }
       E value = (E) queue[0];
       queue[0] = queue[size - 1];
       queue[size - 1] = null;
       size--;
       siftDown(0, (E) queue[0]);
       return value;
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
    @SuppressWarnings("unchecked")
    public E element() {
        if(isEmpty()){
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
        Objects.requireNonNull(value);
        for(int i = 0; i < size; i++){
            if(value.equals(queue[i])){
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
        for (int i = 0; i < size; i++)
            queue[i] = null;
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
        throw new UnsupportedOperationException("todo: implement this");
    }

    private class PriorityQueueIterator implements Iterator<E>{

        private int cursor;
        private int lastReturned = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (cursor < size)
                return (E) queue[lastReturned = cursor++];
            throw new NoSuchElementException();
        }


    }
}

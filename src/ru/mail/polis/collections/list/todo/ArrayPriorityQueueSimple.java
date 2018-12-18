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

    private E[] heap;
    private int size;

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
    public ArrayPriorityQueueSimple(Collection<E> collection) throws NullPointerException {
        this(collection, Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        if (comparator == null){
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        heap = (E[]) new Comparable[10];
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
        if (collection == null || comparator == null){
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        heap = (E[]) collection.toArray(new Comparable[0]);
        size = heap.length;
        for (int pos = size / 2 - 1; pos >= 0; pos --){
            siftDown(pos);
        }
    }


    private void siftDown(int pos){
        while (true){
            int child = 2 * pos + 1;
            if (child >= size){
                break;
            }
            if (child + 1 < size && comparator.compare(heap[child + 1], heap[child]) < 0){
                child ++;
            }
            if (comparator.compare(heap[pos],heap[child]) <= 0){
                break;
            }
            swap(pos, child);
            pos = child;
        }
    }

    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
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
        if (value == null){
            throw new NullPointerException();
        }
        if (size + 1 > heap.length){
            alloc();
        }
        heap[size] = value;
        siftUp(size++);
    }

    private void alloc(){
        int newLength = heap.length * 2;
        E[] newArray = (E[]) new Comparable[newLength];
        System.arraycopy(heap,0,newArray,0, heap.length);
        heap = newArray;
    }

    private void siftUp(int pos) {
        while (pos > 0){
            int parent = (pos - 1) / 2;
            if (comparator.compare(heap[pos], heap[parent]) >= 0){
                break;
            }
            swap(pos,parent);
            pos = parent;
        }
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
        if (size == 0){
            throw new NoSuchElementException();
        }
        E removed = heap[0];
        heap[0] = heap[--size];
        siftDown(0);
        return removed;
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
        if (size == 0){
            throw new NoSuchElementException();
        }
        return heap[0];
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
        if (value == null){
            throw new NullPointerException();
        }
        for (E el : heap){
            if (Objects.equals(el,value)){
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

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            public int currentIndex = 0;
            @Override
            public boolean hasNext() {
                if (size == 0){
                    return false;
                }
                return currentIndex + 1 <= size;
            }

            @Override
            public E next() {
                return heap[currentIndex++];
            }
            @Override
            public void remove(){
                if (size > 0){
                    currentIndex = currentIndex == 0 ? 0 : currentIndex - 1;
                    heap[currentIndex] = heap[--size];
                    siftDown(currentIndex);
                } else {
                    throw new IllegalStateException();
                }
            }
        };
    }
}

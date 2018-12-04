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
    private E[] data;
    private final Comparator<E> comparator;
    private int size = 0;

    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }
    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection.
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
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        data = (E[]) new Comparable[10];
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection
     *  that orders its elements according to the specified comparator.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        if(collection == null){
            throw new NullPointerException();
        }
        data = (E[])new Comparable[collection.size()*2];
        for(E element : collection){
            data[size++] = element;
        }
        for(int i = size/2; i >= 0; i--){
            siftDown(i);
        }
    }

    private boolean greaterOrEqual(int i, int j) {
        return comparator == null
                ? data[i].compareTo(data[j]) >= 0
                : comparator.compare(data[i], data[j]) >= 0;
    }

    public void siftDown(int i){
        if(2*i+2 < size()){
            if(greaterOrEqual(i,2*i+1) && greaterOrEqual(i, 2*i+2)) { return; }
            else if(greaterOrEqual(2*i+1,2*i+2)){
                swap(this.data, i, 2*i+1);
                siftDown(2*i+1);
            }
            else{
                swap(this.data, i, 2*i+2);
                siftDown(2*i+2);
            }
        }
        else if(2*i+1 < size()){
            if(greaterOrEqual(i, 2*i+1)){return;}
            else{
                swap(this.data, i, 2*i+1);
                siftDown(2*i+1);
            }
        }
    }

    public void siftUp(int i){
        if(i != 0) {
            if (i % 2 == 0) {
                if (greaterOrEqual((i - 2) / 2,i)) {
                    return;
                } else {
                    swap(this.data, i, (i - 2) / 2);
                    siftUp((i - 2) / 2);
                }
            } else {
                if (greaterOrEqual((i - 1) / 2,i)) {
                    return;
                } else {
                    swap(this.data, i, (i - 1) / 2);
                    siftUp((i - 1) / 2);
                }
            }
        }
    }

    public void swap(E []a, int i, int j){
        E tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private void resize(int newSize) {
        data = Arrays.copyOf(data,newSize);
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
        if(size() == data.length){
            resize(data.length << 1);
        }
        data[size()] = Objects.requireNonNull(value);
        siftUp(size());
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
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        E result = data[0];
        swap(data, 0, size()-1);
        size--;
        siftDown(0);
        return result;
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
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return data[0];
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
        if(value == null){
            throw new NullPointerException();
        }
        if(isEmpty()){
            return false;
        }
        for(int i = 0; i < size(); i++){
            if(data[i].equals(value)){
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
        return size() == 0;
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
        return new ArrayPriorityQueueIterator();
    }
    private class ArrayPriorityQueueIterator implements Iterator<E> {

        private int currentPosition = 1;

        @Override
        public boolean hasNext() {
            return currentPosition <= size();
        }

        @Override
        public E next() {
            if(currentPosition > size()){
                throw new NoSuchElementException();
            }
            E tmp = data[currentPosition-1];
            currentPosition++;
            return tmp;
        }

    }
}

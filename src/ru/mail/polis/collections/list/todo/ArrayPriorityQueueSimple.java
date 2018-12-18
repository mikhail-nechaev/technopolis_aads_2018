package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    E[] queue;
    int maxSize = 10;
    int numberItems = 0;

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

        this.comparator = Objects.requireNonNull(comparator, "comparator");
        queue = (E[]) new Comparable[maxSize];
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
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        if (collection == null || comparator == null) {
            throw new NullPointerException();
        }
        queue = (E[]) new Comparable[maxSize];
        Iterator<E> iterator=collection.iterator();
        while(iterator.hasNext()){
            add(iterator.next());
        }
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
        int i;
        if (numberItems == 0) {
            queue[numberItems++] = value;
        } else {
            for (i = numberItems - 1; i >= 0; i--) {
                if (comparator.compare( value,  queue[i]) > 0) {
                    queue[i+1] = queue[i];
                } else {
                    break;
                }

            }
            queue[i+1] = value;
            numberItems++;
            if (numberItems == maxSize) {
                increaseSize();
            }
        }
    }

    public void increaseSize(){
        E[] array = queue.clone();
        queue = (E[]) new Comparable[maxSize * 2];
        for (int i = 0; i < maxSize; i++) {
            queue[i] = array[i];
        }
        maxSize = maxSize * 2;
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
        return queue[--numberItems];
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
        return queue[numberItems - 1];
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
        if (value==null){
            throw new NullPointerException();
        }
        for(int i = 0; i < numberItems;i++){
            if (comparator.compare( value, queue[i]) == 0){
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
        return numberItems;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return numberItems == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        numberItems = 0;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        Iterator<E> iterator=new Iterator<E>() {
            int pointer = 0;
            boolean check=false;
            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                check = false;
                return pointer < size();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                check = true;
                return queue[pointer++];
            }

            /**
             * Removes from the underlying collection the last element returned
             * by this iterator (optional operation).  This method can be called
             * only once per call to {@link #next}.
             * <p>
             * The behavior of an iterator is unspecified if the underlying collection
             * is modified while the iteration is in progress in any way other than by
             * calling this method, unless an overriding class has specified a
             * concurrent modification policy.
             * <p>
             * The behavior of an iterator is unspecified if this method is called
             * after a call to the {@link #forEachRemaining forEachRemaining} method.
             *
             * @throws UnsupportedOperationException if the {@code remove}
             *                                       operation is not supported by this iterator
             * @throws IllegalStateException         if the {@code next} method has not
             *                                       yet been called, or the {@code remove} method has already
             *                                       been called after the last call to the {@code next}
             *                                       method
             * @implSpec The default implementation throws an instance of
             * {@link UnsupportedOperationException} and performs no other action.
             */
            @Override
            public void remove() {
                if (!check) throw new IllegalStateException();
                pointer--;
                for (int i = pointer; i < numberItems+1 ; i++) {
                    queue[i] = queue[i + 1];
                }
                numberItems--;
                check=false;

            }
        };
        return iterator;
    }


}

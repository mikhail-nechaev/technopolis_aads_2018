package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;
    private Object[] priorityQueue;
    private int N = 0;

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
        if (collection == null) {
            throw new NullPointerException("Specified collection is null");
        }
        priorityQueue = collection.toArray();
        N = priorityQueue.length;
        heapify(priorityQueue);
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
     * You may consider that all elements in collection is not a null.
     * <p>
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        if (collection == null) {
            throw new NullPointerException("Specified collection or comparator is null");
        }
        priorityQueue = collection.toArray();
        N = priorityQueue.length;
        heapify(priorityQueue);
    }

    private void grow() {
        int newLength = N == 0 ? 4 : N + N / 2;
        Object[] newArray = new Object[newLength];
        if (N > 0) {
            System.arraycopy(priorityQueue, 0, newArray, 0, N);
        }
        priorityQueue = newArray;
    }

    private void heapify(Object[] arr) {
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            swiftDown(i);
        }
    }


    private void swiftDown(int i) {

        int leftInd = 2 * i + 1;
        int rightInd = 2 * i + 2;

        int min = i;

        if (leftInd < N && comparator.compare((E) priorityQueue[leftInd], (E) priorityQueue[i]) < 0) {
            min = leftInd;
        }

        if (rightInd < N && comparator.compare((E) priorityQueue[rightInd], (E) priorityQueue[min]) < 0) {
            min = rightInd;
        }

        if (min != i) {
            Object tmp = priorityQueue[i];
            priorityQueue[i] = priorityQueue[min];
            priorityQueue[min] = tmp;
            swiftDown(min);
        }

    }

    private void swiftUp(int i) {
        int parent = (i - 1) / 2;

        if (comparator.compare((E) priorityQueue[i], (E) priorityQueue[parent]) < 0 && i != 0) {
            Object tmp = priorityQueue[i];
            priorityQueue[i] = priorityQueue[parent];
            priorityQueue[parent] = tmp;
            swiftUp(parent);
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
            throw new NullPointerException("Specified element is null");
        }
        if (priorityQueue == null || priorityQueue.length == N) {
            grow();
        }
        priorityQueue[N] = value;
        swiftUp(N++);
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
            throw new NoSuchElementException("Queue is empty");
        }
        E tmp = element();
        priorityQueue[0] = priorityQueue[N - 1];
        --N;
        if (!isEmpty()) {
            swiftDown(0);
        }
        return tmp;
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
            throw new NoSuchElementException("Queue is empty");
        }
        return (E) priorityQueue[0];
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
        for (int i = 0; i < N; i++) {
            if (value.equals(priorityQueue[i])) {
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
        return N;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        N = 0;
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

        private int i = N;
        private int target = 0;
        private int previousTarget;
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            previousTarget = target;
            i--;
            canRemove = true;
            return (E) priorityQueue[target++];
        }

        @Override
        public void remove() {
            if (N == 0 || !canRemove){
                throw new IllegalStateException();
            }
            priorityQueue[previousTarget] = priorityQueue[N - 1];
            N--;
            target--;
            if (!isEmpty()){
                swiftDown(previousTarget);
            }
        }
    }
}

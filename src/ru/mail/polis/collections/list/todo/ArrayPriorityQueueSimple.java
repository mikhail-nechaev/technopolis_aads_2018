package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on heap priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;

    private Object[] heap;
    private int arraySize;
    private int size; // Кол-во элементов в коллекции

//    public Heap(int length) {
//        this.heap = new int[length];
//        this.length = length;
//        this.size = 0;
//    }

    private void siftUp(int i) {
        while (comparator.compare((E) heap[i], (E) heap[(i - 1) / 2]) < 0) {
            E e = (E) heap[i];
            heap[i] = heap[(i - 1) / 2];
            heap[(i - 1) / 2] = e;
            i = (i - 1) / 2;

        }
    }

    private void siftDown(int i) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int j = left;
            if (right < size && (comparator.compare((E) heap[right], (E) heap[left]) < 0)) {
                j = right;
            }
            if (comparator.compare((E) heap[i], (E) heap[j]) <= 0) {
                return;
            }
            E e = (E) heap[i];
            heap[i] = heap[j];
            heap[j] = e;
            i = j;
        }
    }

    /**
     * Увеличение размеров массива в 2 раза с копирыванием элементов
     */
    private void resize() {
        arraySize *= 2;
        Object[] heap = new Object[arraySize];
        for (int i = 0; i < size; i++) {
            heap[i] = this.heap[i];
        }
        this.heap = heap;
    }

    /**
     * Проверка на заполненность очереди.
     * Применять в начале методов с добавлением элементов.
     */
    private void checkFullHeap() {
        if (size == arraySize) {
            resize();
        }
    }



    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates heap {@code IPriorityQueue} containing the elements in the specified collection.
     *
     * You may consider that all elements in collection is not heap null.
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
     * Creates heap {@code IPriorityQueue} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        arraySize = 15;
        size = 0;
        heap = new Object[arraySize];
        this.comparator = Objects.requireNonNull(comparator, "comparator");

    }

    /**
     * Creates heap {@code IPriorityQueue} containing the elements in the specified collection
     *  that orders its elements according to the specified comparator.
     *
     * You may consider that all elements in collection is not heap null.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        arraySize = collection.size();
        size = 0;
        heap = new Object[arraySize];
        for (E e: collection) {
            heap[size++] = e;
        }
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
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
        if (value == null) {
            throw new NullPointerException();
        }
        checkFullHeap();
        heap[size] = value;
        siftUp(size++);
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
        Object first = heap[0];
        heap[0] = heap[--size];
        siftDown(0);
        return (E) first;
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
        return (E) heap[0];
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
        for (Object e: heap) {
            if (((E) e).equals(value)) {
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
        heap = new Object[arraySize];
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
        return new Iterator<E>() {
            int cursor = 0;
            boolean doNext = false;
            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                doNext = true;
                E e = (E) heap[cursor];
                incCursor();
                return e;
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
                if (!doNext) {
                    throw new IllegalStateException();
                }
                doNext = false;
                heap[decCursor()] = heap[--size];
                //heap[size] = null;
                siftDown(cursor);
            }

            private int incCursor() {
                int c = cursor * 2 + 1;
                if (c > size) {
                    decCursor();
                    c = cursor * 2 + 2;
                }
                cursor = c;
                return cursor;
            }
            private int decCursor() {
                cursor = cursor / 2;
                return cursor;
            }
        };
    }
}

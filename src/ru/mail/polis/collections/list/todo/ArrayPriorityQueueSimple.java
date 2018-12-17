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
@SuppressWarnings("unchecked")
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;

    private E[] arrayPriorityQueue;

    private int arrayLength;

    private static final int MIN_INITIAL_CAPACITY = 32;

    private static final float LOAD_FACTOR = (float) 0.8;

    private void doubleCapacity() {
        arrayPriorityQueue = Arrays.copyOf(arrayPriorityQueue, arrayPriorityQueue.length << 1);
    }


    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }


    public ArrayPriorityQueueSimple(Collection<E> collection) {
        this(collection, Comparator.naturalOrder());
    }


    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        arrayPriorityQueue = (E[]) new Comparable[MIN_INITIAL_CAPACITY];
        arrayLength = 0;
    }


    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        if (comparator == null || collection == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        arrayLength = 0;
        arrayPriorityQueue = (E[]) new Comparable[collection.size() + MIN_INITIAL_CAPACITY];
        for (E value : collection) {
            arrayPriorityQueue[arrayLength] = value;
            arrayLength ++;
        }
        for (int i = (arrayLength / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }


    @Override
    public void add(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        float occupancy = (float) arrayLength / (float) arrayPriorityQueue.length;

        if (occupancy >= LOAD_FACTOR) {
            doubleCapacity();
        }
        arrayLength++;
        arrayPriorityQueue[arrayLength - 1] = value;
        siftUp(arrayLength - 1);
    }


    private void siftUp(int index) {
        if(index <= 0) return;
        int parent;

        if (index % 2 == 0) {
            parent = (index - 2) / 2;
        } else {
            parent = (index - 1) / 2;
        }

        if (comparator.compare(arrayPriorityQueue[parent], arrayPriorityQueue[index]) > 0) {
            E tmp = arrayPriorityQueue[parent];
            arrayPriorityQueue[parent] = arrayPriorityQueue[index];
            arrayPriorityQueue[index] = tmp;
            siftUp(parent);
        }

    }

    private void siftDown(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        //Ищем большего сына, если такой есть
        int smallest = index;
        if ( left < arrayPriorityQueue.length && arrayPriorityQueue[left] != null && comparator.compare(arrayPriorityQueue[left], arrayPriorityQueue[smallest]) < 0)// сравниваем левый с текущим
        {
            smallest = left; // если левый больше, то больший - левый
        }
        if (right < arrayPriorityQueue.length &&arrayPriorityQueue[right] != null &&  comparator.compare(arrayPriorityQueue[right], arrayPriorityQueue[smallest]) < 0) // сравниваем правый с текущим
        {
            smallest = right; // если правый больше, то больший - правый
        }
        //если меньший != текущий, то меняем местами с текущим (проталкиваем) и вызываем heapfy уже для него
        if (smallest != index) {
            E c = arrayPriorityQueue[index];
            arrayPriorityQueue[index] = arrayPriorityQueue[smallest];
            arrayPriorityQueue[smallest] = c;
            siftDown(smallest);
        }

    }


    @Override
    public E remove()  throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        //запоминаем значение корня

        E tmp = arrayPriorityQueue[0];
        arrayPriorityQueue[0] = arrayPriorityQueue[arrayLength - 1];
        arrayPriorityQueue[arrayLength - 1] = null;

        siftDown(0);

        arrayLength--;
        return tmp;
    }


    @Override
    public E element() throws NoSuchElementException {
        if (arrayLength == 0) {
            throw new NoSuchElementException();
        }
        return arrayPriorityQueue[0];
    }


    @Override
    public boolean contains(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        for (E anArrayPriorityQueue : arrayPriorityQueue) {
            if (comparator.compare((E) value, anArrayPriorityQueue) == 0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int size() {
        return arrayLength;
    }


    @Override
    public boolean isEmpty() {
        return arrayPriorityQueue[0] == null;
    }


    @Override
    public void clear() {
        for (int i = 0; i < arrayLength; i++) {
            arrayPriorityQueue[i] = null;
        }
    }


    private void removeAt(int index)  {

        if (arrayLength <= 0) {
            throw new NoSuchElementException();
        }
        arrayPriorityQueue[index] = arrayPriorityQueue[arrayLength - 1];
        arrayPriorityQueue[arrayLength - 1] = null;
        arrayLength--;
        siftDown(0);

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

            private int index = 0;
            private int cursor = -1;

            @Override
            public boolean hasNext() {
                return index < arrayLength;
            }

            @Override
            public E next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return arrayPriorityQueue[cursor = index++];
            }

            @Override
            public void remove() {
                if (cursor < 0) {
                    throw new IllegalStateException();
                }
                index--;
                removeAt(index);
                cursor = -1;
            }
        };
    }
}

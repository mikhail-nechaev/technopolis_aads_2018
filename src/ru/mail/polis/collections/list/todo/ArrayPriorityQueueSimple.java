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
@SuppressWarnings("unchecked")
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;

    private E [] arrayPriorityQueue;

    private int arrayLength;

    private static final int MIN_INITIAL_CAPACITY = 8;

    private static final float LOAD_FACTOR = (float) 0.8;

    private void doubleCapacity(){
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
        arrayPriorityQueue = (E[]) new Comparable[MIN_INITIAL_CAPACITY];
        arrayLength = 0;
        for (E e : collection) {
            add(e);
        }
    }


    @Override
    public void add(E value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        float occupancy = (float) arrayLength / (float) arrayPriorityQueue.length;

        if(occupancy >= LOAD_FACTOR){
            doubleCapacity();
        }
        arrayLength++;
        arrayPriorityQueue[arrayLength - 1] = value;
        for (int i = (arrayLength / 2)-1; i >= 0; --i) // вызываем heapfy для родителя
        {
            heapfy(i);
        }
    }



    private void heapfy(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        //Ищем меньшего сына, если такой есть
        int smallest = index;
        if(arrayPriorityQueue[left] != null &&left < arrayPriorityQueue.length && comparator.compare(arrayPriorityQueue[left], arrayPriorityQueue[index]) < 0)// сравниваем левый с текущим
        {
            smallest = left; // если левый меньше, то меньший - левый
        }
        if(arrayPriorityQueue[right] != null && right < arrayPriorityQueue.length && comparator.compare(arrayPriorityQueue[right], arrayPriorityQueue[index]) < 0) // сравниваем правый с текущим
        {
            smallest = right; // если правый меньше, то меньший - правый
        }
        //если меньший != текущий, то меняем местами с текущим (проталкиваем) и вызываем heapfy уже для него
        if(smallest != index)
        {
            E c = arrayPriorityQueue[index];
            arrayPriorityQueue[index] =  arrayPriorityQueue[smallest];
            arrayPriorityQueue[smallest] = c;
            heapfy(index);
        }
    }


    @Override
    public E remove() throws NoSuchElementException {
        if (arrayLength <= 0){
            throw new NoSuchElementException();
        }
        assert(arrayPriorityQueue.length != 0);
        //запоминаем значение корня

        E tmp = arrayPriorityQueue[0];
        arrayPriorityQueue[0] = arrayPriorityQueue[arrayLength-1];
        arrayPriorityQueue[arrayLength - 1] = null;
        if(arrayLength != 0)
        {
            heapfy(0); // heapfy с головы
        }
        arrayLength--;
        return tmp;
    }


    @Override
    public E element()throws NoSuchElementException{
        if(arrayLength == 0){
            throw new NoSuchElementException();
        }
        return arrayPriorityQueue[0];
    }


    @Override
    public boolean contains(Object value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        for (E anArrayPriorityQueue : arrayPriorityQueue) {
            if (comparator.compare((E) value,anArrayPriorityQueue) == 0) {
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
        return arrayLength == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < arrayLength; i++){
            arrayPriorityQueue[i] = null;
        }
    }


    private void delete(int index){
        arrayPriorityQueue[0] = arrayPriorityQueue[index];
        arrayPriorityQueue[index] = null;
        if(arrayLength != 0)
        {
            heapfy(0);
        }
        arrayLength--;
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

            private int index;
            private int cursor = -1;
            private int size = arrayLength;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() throws NoSuchElementException  {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                index = index + 1;
                cursor = index;
                return arrayPriorityQueue[cursor];
            }

            @Override
            public void remove() throws IllegalStateException{
                if(cursor == -1 ){
                    throw new IllegalStateException();
                }
                delete(cursor);
                cursor = -1;
            }
        };
    }
}

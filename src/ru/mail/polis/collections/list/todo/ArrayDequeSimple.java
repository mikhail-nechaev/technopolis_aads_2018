package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
@SuppressWarnings("unchecked")
public class ArrayDequeSimple<E> implements IDeque<E> {


    protected E [] arrayDeque;
    protected int head;
    protected int tail;
    protected static final int MIN_INITIAL_CAPACITY = 8;


    public ArrayDequeSimple(){
        arrayDeque = (E[])new Object[16];
    }

    public ArrayDequeSimple(int numElements){
        allocateElements(numElements);
    }

    private void checkInvariants() {
        assert arrayDeque[tail] == null;
        assert head == tail ? arrayDeque[head] == null :
                (arrayDeque[head] != null &&
                        arrayDeque[(tail - 1) & (arrayDeque.length - 1)] != null);
        assert arrayDeque[(head - 1) & (arrayDeque.length - 1)] == null;
    }


    protected boolean delete(int i) {
        checkInvariants();
        final Object[] elements = this.arrayDeque;
        final int mask = elements.length - 1;
        final int h = head;
        final int t = tail;
        final int front = (i - h) & mask;
        final int back  = (t - i) & mask;


        if (front >= ((t - h) & mask))
            throw new ConcurrentModificationException();


        if (front < back) {
            if (h <= i) {
                System.arraycopy(elements, h, elements, h + 1, front);
            } else {
                System.arraycopy(elements, 0, elements, 1, i);
                elements[0] = elements[mask];
                System.arraycopy(elements, h, elements, h + 1, mask - h);
            }
            elements[h] = null;
            head = (h + 1) & mask;
            return false;
        } else {
            if (i < t) {
                System.arraycopy(elements, i + 1, elements, i, back);
                tail = t - 1;
            } else {
                System.arraycopy(elements, i + 1, elements, i, mask - i);
                elements[mask] = elements[0];
                System.arraycopy(elements, 1, elements, 0, t);
                tail = (t - 1) & mask;
            }
            return true;
        }
    }


    private static int calculateSize(int numElements){
        int initialCapacity =  MIN_INITIAL_CAPACITY;
        if(numElements >= initialCapacity){
            initialCapacity = numElements;
            initialCapacity = initialCapacity | (initialCapacity >>> 1);
            initialCapacity = initialCapacity | (initialCapacity >>> 2);
            initialCapacity = initialCapacity | (initialCapacity >>> 4);
            initialCapacity = initialCapacity | (initialCapacity >>> 8);
            initialCapacity = initialCapacity | (initialCapacity >>> 16);
            initialCapacity++;
        }
        return initialCapacity;
    }

    private void allocateElements(int numElements){
        arrayDeque = (E[]) new Object[calculateSize(numElements)];
    }
    private void doubleCapacity(){
        assert head == tail;
        int p = head;
        int n = arrayDeque.length;
        int r = n-p;
        int newCapacity = n << 1;
        if(newCapacity < 0){
            throw new IllegalStateException("Sorry, deque too big");
        }
        E[] a = (E[]) new Object[newCapacity];
        System.arraycopy(arrayDeque, p, a, 0, r); // скопировали в массив a, начиная с p(head) в 0 позицию массива назначения
        //длинной r(head - tail)
        System.arraycopy(arrayDeque, 0, a, r, p);
        arrayDeque = a;
        head = 0;
        tail = n;
    }

    @Override
    public void addFirst(E value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        arrayDeque[head = (head - 1) & (arrayDeque.length - 1)] = value;
        if(head == tail){
            doubleCapacity();
        }
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() throws NoSuchElementException{
        int h = head;
        E res = arrayDeque[h];
        if(res == null){
            throw new NoSuchElementException();
        }
        arrayDeque[h] = null;
        head = (h + 1) & (arrayDeque.length - 1);
        return res;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() throws NoSuchElementException{
        if (arrayDeque[head] == null) {
            throw new NoSuchElementException();
        }
        return arrayDeque[head];
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) throws NullPointerException{
        if (value == null) {
            throw new NullPointerException();
        }
        arrayDeque[tail] = value;
        if ((tail = (tail + 1) & (arrayDeque.length - 1)) == head)
            doubleCapacity();
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() throws NoSuchElementException{
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        int t = (tail - 1) & (arrayDeque.length - 1);
        E result = arrayDeque[t];
        if (result == null)
            return null;
        arrayDeque[t] = null;
        tail = t;
        return result;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() throws NoSuchElementException {
        E result = arrayDeque[(tail - 1) & (arrayDeque.length - 1)];
        if (result == null)
            throw new NoSuchElementException();
        return result;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(Object value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        int mask = arrayDeque.length - 1;
        int i = head;
        Object x;
        while ( (x = arrayDeque[i]) != null) {
            if (value.equals(x))
                return true;
            i = (i + 1) & mask;
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
        return (tail - head) & (arrayDeque.length - 1);
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return ((tail - head) & (arrayDeque.length - 1)) == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        int h = head;
        int t = tail;
        if (h != t) {
            head = tail = 0;
            int i = h;
            int mask = arrayDeque.length - 1;
            do {
                arrayDeque[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = head;
            private int cursor = -1;
            private int tailIterator = tail;

            @Override
            public boolean hasNext() {
                return index != tailIterator;
            }

            @Override
            public E next() throws ConcurrentModificationException, NoSuchElementException {
                if (index == tailIterator) {
                    throw new NoSuchElementException();
                }
                E result = arrayDeque[index];
                if (tailIterator != tail || result == null) {
                    throw new ConcurrentModificationException();
                }
                cursor = index;
                index = (index + 1) & (arrayDeque.length - 1);
                return result;
            }
            @Override
            public void remove(){
                if (cursor < 0)
                    throw new IllegalStateException();
                if (delete(cursor)) {
                    cursor = (cursor - 1) & (arrayDeque.length - 1);
                    tailIterator = tail;
                }
                cursor = -1;
            }
        };
    };
}

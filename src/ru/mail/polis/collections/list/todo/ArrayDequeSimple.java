package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E extends Comparable<E>> implements IDeque<E> {
    private E [] arrayDeque;
    private int head;
    private int tail;
    private static final int MIN_INITIAL_CAPACITY = 8;


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


    private boolean delete(int i) {
        checkInvariants();
        final Object[] elements = this.arrayDeque;
        final int mask = elements.length - 1;
        final int h = head;
        final int t = tail;
        final int front = (i - h) & mask;
        final int back  = (t - i) & mask;

        // Invariant: head <= i < tail mod circularity
        if (front >= ((t - h) & mask))
            throw new ConcurrentModificationException();

        // Optimize for least element motion
        if (front < back) {
            if (h <= i) {
                System.arraycopy(elements, h, elements, h + 1, front);
            } else { // Wrap around
                System.arraycopy(elements, 0, elements, 1, i);
                elements[0] = elements[mask];
                System.arraycopy(elements, h, elements, h + 1, mask - h);
            }
            elements[h] = null;
            head = (h + 1) & mask;
            return false;
        } else {
            if (i < t) { // Copy the null tail as well
                System.arraycopy(elements, i + 1, elements, i, back);
                tail = t - 1;
            } else { // Wrap around
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



    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
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
    public E removeFirst() {
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
    public E getFirst() {
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
    public void addLast(E value) {
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
    public E removeLast() {
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
    public E getLast() {
        E result = arrayDeque[(tail - 1) & (arrayDeque.length - 1)];
        if (result == null)
            throw new NoSuchElementException();
        return arrayDeque[arrayDeque.length-1];
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
    public boolean contains(Object value) {
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
        return arrayDeque.length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return arrayDeque.length == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        int h = head;
        int t = tail;
        if (h != t) { // clear all cells
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
    public ListIterator<E> iterator()  {

        return new ListIterator<E>() {

            private int position = head;
            private int border = tail;


            @Override
            public boolean hasNext() {
                return position != border;
            }

            @Override
            public E next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                position = position + 1;
                return arrayDeque[position];
            }

            @Override
            public boolean hasPrevious() {
                return position != head;
            }

            @Override
            public E previous() {
                if(!hasPrevious()){
                    throw new NoSuchElementException();
                }
                position = position - 1;
                return arrayDeque[position];
            }

            @Override
            public int nextIndex() {
                int index = position + 1;
                return (Integer) arrayDeque[index];
            }

            @Override
            public int previousIndex() {
                int index = position - 1;
                return (Integer) arrayDeque[index];
            }

            @Override
            public void remove() {
                if(position < 0) {
                    throw new IllegalStateException();
                }
                delete(position);
            }

            @Override
            public void set(E e) {
                arrayDeque[position] = e;
            }

            @Override
            public void add(E e) {
                addLast(e);
            }
        };
    }


}

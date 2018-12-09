package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;


import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    protected E[] elements;
    protected int head;
    protected int tail;
    private static final int CAPACITY = 16;

    protected void ensureCapacity(){
        int oldLength = elements.length;
        int newLength = oldLength << 1 + 1;
        final E[] es = elements = Arrays.copyOf(elements, newLength);
        int newPosHead = newLength - oldLength + head;
        System.arraycopy(es, head,
                es, newPosHead,
                oldLength - head);
        for(int i = head; i < oldLength; i++){
            elements[i] = null;
        }
        head = newPosHead;
    }

    protected int dec(int i){
        if(--i < 0) i = elements.length - 1;
        return i;
    }

    protected int inc(int i){
        if(++i >= elements.length) i = 0;
        return i;
    }

    protected int getNumElementsBetween(int i, int j){
        if((i -= j) < 0) i += elements.length;
        return i;
    }

    protected boolean delete(int i){
        if(head <= i) {
            final int numFrontElements = getNumElementsBetween(i, head);
            System.arraycopy(elements, head, elements, i, numFrontElements);
            elements[head] = null;
            head = inc(head);
            return false;
        } else {
            final int numElementsBack = getNumElementsBetween(tail, i);
            System.arraycopy(elements, i+1, elements, i, numElementsBack);
            elements[tail] = null;
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayDequeSimple(){
        this.elements = (E[]) new Object[CAPACITY];
    }


    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        elements[head = dec(head)] = Objects.requireNonNull(value);
        if(head == tail){
            ensureCapacity();
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
        E e = elements[head];
        if(e == null) {
            throw new NoSuchElementException();
        }
        elements[head] = null;
        head = inc(head);
        return e;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        E e = elements[head];
        if(e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        elements[tail] = Objects.requireNonNull(value);
        if(head == (tail = inc(tail))){
            ensureCapacity();
        }
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        E e = elements[dec(tail)];
        if(e == null)
            throw new NoSuchElementException();
        elements[tail = dec(tail)] = null;
        return e;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        E e = elements[dec(tail)];
        if(e == null)
            throw new NoSuchElementException();
        return e;
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
    public boolean contains(E value) {
        Objects.requireNonNull(value);
        ListIterator<E> iterator = new ArrayDequeIterator();
        for(; iterator.hasNext();){
            if(value.equals(iterator.next())){
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
        return  (tail - head < 0)
                ? tail - head + elements.length
                : tail - head;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        for(int i = 0; i < elements.length; i++){
            elements[i] = null;
        }
        head = tail = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    protected class ArrayDequeIterator implements ListIterator<E> {
        int cursor = head;
        int remaining = size();
        int nextIndex;
        int lastIndexReturned = -1;

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E e = elements[cursor];
            cursor = inc(lastIndexReturned = cursor);
            remaining--;
            nextIndex++;
            return e;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            E e = elements[cursor = lastIndexReturned];
            lastIndexReturned = (lastIndexReturned == head) ? -1 : dec(lastIndexReturned);
            remaining++;
            nextIndex--;
            return e;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastIndexReturned < 0) {
                throw new IllegalStateException();
            }
            if (delete(lastIndexReturned)) {
                cursor = dec(cursor);
            }
            nextIndex--;
            lastIndexReturned = -1;
        }

        @Override
        public void set(E e) {
            if (lastIndexReturned < 0) {
                throw new IllegalStateException();
            }
            elements[lastIndexReturned] = e;
        }

        @Override
        public void add(E e) {
            Objects.requireNonNull(e);
            if (cursor >= head || cursor == 0) {
                if (dec(head) == tail) {
                    ensureCapacity();
                }
                int numFrontElements = getNumElementsBetween(dec(cursor), head = dec(head));
                System.arraycopy(elements, inc(head), elements, head, numFrontElements);
            } else {
                if (inc(tail) == head) {
                    ensureCapacity();
                }
                int numBackElements = getNumElementsBetween(tail = inc(tail), cursor);
                System.arraycopy(elements, cursor, elements, cursor = inc(cursor), numBackElements);
            }
            elements[dec(cursor)] = e;
            nextIndex++;
            lastIndexReturned = -1;
        }
    }
}

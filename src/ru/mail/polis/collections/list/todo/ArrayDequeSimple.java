package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    Object[] array;
    int head;
    int tail;
    int size;

    public ArrayDequeSimple() {
        array = new Object[10];
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {

        } else {
            head = head == 0 ? array.length - 1 : head - 1;
        }
        array[head] = value;
        size++;
        if (head == tail && size > 1) {
            expandArray();
        }
    }

    private void expandArray() {
        Object[] newArray = new Object[array.length * 2];
        int al = array.length;

        System.arraycopy(array, head, newArray, 0, al - head);
        System.arraycopy(array, 0, newArray, al - head, head);

        head = 0;
        tail = al;
        array = newArray;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E first = (E) array[head];
        array[head] = null;
        head = head == array.length - 1 ? 0: head + 1;
        size--;
        return first;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E first = (E) array[head];
        return first;
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
        if (isEmpty()) {

        } else {
            tail = tail == array.length - 1 ? 0 : tail + 1;
        }
        array[tail] = value;
        size++;
        if (head == tail && size > 1) {
            expandArray();
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E last = (E) array[tail];
        array[tail] = null;
        tail = tail == 0 ? array.length - 1 : tail - 1;
        size--;
        return last;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E last = (E) array[tail];
        return last;
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
        if (value == null) {
            throw new NullPointerException();
        }
        for (int i = head; i == tail; ) {
            @SuppressWarnings("unchecked")
            E check = (E) array[i];
            if (value.equals(check)) {
                return true;
            }
            i = i == array.length - 1 ? 0 : i+1;
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
        for (int i = head; i == tail; ) {
            array[i] = null;
            i = i < array.length - 1 ? i+1 : 0;
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
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<E> {

        int lastIndex = -1, lastRealIndex = head - 1;
        int nextIndex = 0, nextRealIndex = head;
        int size = ArrayDequeSimple.this.size;
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            canRemove = true;
            lastIndex = nextIndex;
            lastRealIndex = nextRealIndex;
            nextIndex++;
            nextRealIndex = ++nextRealIndex % array.length;
            return (E) ArrayDequeSimple.this.array[lastRealIndex];
        }


        @Override
        public void remove() {

            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;
            //ArrayDequeSimple.this.delete(lastRealIndex);
        }
//        private int cursor = head;
//        private int end = tail;
//        private int lastReturn = -1;
//
//        @Override
//        public boolean hasNext() {
//            return cursor != end;
//        }
//
//        @Override
//        public E next() {
//            if (cursor == end) {
//                throw new NoSuchElementException();
//            }
//            cursor = cursor == array.length - 1 ? 0 : cursor + 1;
//            @SuppressWarnings("unchecked")
//            E next = (E) array[cursor];
//            if (tail != end || next == null) {
//                throw new ConcurrentModificationException();
//            }
//            lastReturn = cursor;
//            return next;
//        }
//
//        @Override
//        public void remove() {
//
//            if (lastReturn == tail) {
//                removeLast();
//            } else if (lastReturn == head) {
//                removeFirst();
//            } else {
//                lastReturn.prev.next = lastReturn.next;
//                lastReturn.next.prev = lastReturn.prev;
//                size--;
//            }
//        }
    }
}

package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    protected Node start = null;
    protected Node end = null;
    protected int size = 0;

    protected class Node {
        public Node previous = null;
        public Node next = null;
        public E value;

        public Node(E value) {
            this.value = value;
        }
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) throw new NullPointerException();
        if (size == 0) {
            start = end = new Node(value);
        } else {
            start.previous = new Node(value);
            start.previous.next = start;
            start = start.previous;
        }
        size++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        E value = start.value;
        if (size == 1) {
            start = end = null;
        } else {
            start = start.next;
            start.previous = null;
        }
        size--;
        return value;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return start.value;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null) throw new NullPointerException();
        if (size == 0) {
            start = end = new Node(value);
        } else {
            end.next = new Node(value);
            end.next.previous = end;
            end = end.next;
        }
        size++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        E value = end.value;
        if (size == 1) {
            start = end = null;
        } else {
            end = end.previous;
            end.next = null;
        }
        size--;
        return value;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return end.value;
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
        if (value == null) throw new NullPointerException();
        for (E elem : this) {
            if (elem == value) return true;
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
        start = null;
        end = null;
        size = 0;
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
            Node next = start;
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) throw new NoSuchElementException();
                E value = next.value;
                next = next.next;
                canRemove = true;
                return value;
            }

            @Override
            public void remove() {
                if (!canRemove) throw new IllegalStateException();
                if (next == null) {
                    removeLast();
                } else if (next.previous.previous == null) {
                    removeFirst();
                } else {
                    next.previous.previous.next = next;
                    next.previous = next.previous.previous;
                    size--;
                }
                canRemove =  false;
            }
        };
    }
}

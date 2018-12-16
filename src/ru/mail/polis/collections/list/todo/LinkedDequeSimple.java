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
    private class Node<E> {
        private E current;
        private Node<E> next, prev;

        Node(E current) {
            this.current = current;
        }
    }

    private int size;
    private Node<E> first, last;

    public LinkedDequeSimple() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException();
        }

        if (isEmpty()) {
            first = last = new Node(value);
            size++;
            return;
        }


        Node<E> oldfirst = first;
        first = new Node(value);
        first.next = oldfirst;
        oldfirst.prev = first;
        size++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    @SuppressWarnings("unchecked")
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E res = (E) first.current;
        first = first.next;
        if (first != null) {
            first.prev = null;
        }
        size--;
        if (isEmpty()) {
            first = last = null;
        }
        return res;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) first.current;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addLast(E value) {
        if (value == null) {
            throw new NullPointerException();
        }

        if (isEmpty()) {
            first = last = new Node(value);
            size++;
            return;
        }
        Node oldlast = last;
        last = new Node(value);
        oldlast.next = last;
        last.prev = oldlast;
        size++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    @SuppressWarnings("unchecked")
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E res = (E) last.current;
        last = last.prev;
        if (last != null) {
            last.next = null;
        }
        if (isEmpty()) {
            first = last = null;
        }
        size--;
        return res;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    @SuppressWarnings("unchecked")
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) last.current;
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
        Node<E> checked = first;
        while (checked != null) {
            if (checked.current.equals(value)) {
                return true;
            }
            checked = checked.next;
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
        return (size == 0);
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        size = 0;
        first = last = null;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeSimpleIterator<E>(first);
    }

    private class LinkedDequeSimpleIterator<E> implements Iterator<E> {
        private Node<E> cur;

        public LinkedDequeSimpleIterator(Node<E> prev) {
            cur = prev;
        }

        @Override
        public boolean hasNext() {
            return (cur != null);
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E newCur = cur.current;
            cur = cur.next;
            return newCur;
        }
    }
}

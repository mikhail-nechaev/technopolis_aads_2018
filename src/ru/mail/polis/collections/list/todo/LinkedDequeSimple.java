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

    private Node head;

    private Node tail;

    private int N = 0;

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException("Specified element is null");
        }

        Node node = new Node(value);

        Node tmp = head;

        head = node;

        head.next = tmp;

        if (N == 0) {
            tail = head;
        } else {
            tmp.previous = head;
        }

        N++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (N == 0) {
            throw new NoSuchElementException("Deque is empty");
        }

        E first = head.value;

        head = head.next;
        N--;

        if (N == 0) {
            tail = null;
        } else {
            head.previous = null;
        }

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
            throw new NoSuchElementException("Deque is empty");
        }
        return head.value;
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
            throw new NullPointerException("Specified element is null");
        }

        Node node = new Node(value);

        if (N == 0) {
            head = node;
        } else {
            tail.next = node;
            node.previous = tail;
        }

        tail = node;
        N++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (N == 0) {
            throw new NoSuchElementException("Deque is empty");
        }

        E last = tail.value;

        if (N == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.previous;
            tail.next = null;
        }

        N--;

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
            throw new NoSuchElementException("Deque is empty");
        }
        return tail.value;
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
            throw new NullPointerException("Specified element is null");
        }

        Node target = head;

        while (target != null) {
            if (value.equals(target.value)){
                return true;
            }

            target = target.next;
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
        head = null;
        tail = null;
        N = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }

    private class LinkedDequeIterator implements Iterator<E> {


        private Node target = head;

        @Override
        public boolean hasNext() {
            return target != null;
        }

        @Override
        public E next() {
            if (hasNext()) {
                E tmp = target.value;
                target = target.next;
                return tmp;
            } else {
                throw new NoSuchElementException("Deque is empty");
            }
        }
    }

    private class Node {

        E value;

        Node next;
        Node previous;


        Node(E value) {
            this.value = value;
        }
    }
}

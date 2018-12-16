package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    private static class Node<T> {
        private Node<T> previous;
        private Node<T> next;
        private T value;

        Node(Node<T> previous, T value, Node<T> next) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }

        Node() {
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedDequeSimple() {
        head = null;
        tail = null;
        size = 0;
    }

    private void throwNPEIfNull(Object o) {
        if (o == null) {
            throw new NullPointerException("Adding null is not supported");
        }
    }

    private void throwExceptionIfEmpty() {
        if (size == 0) {
            throw new NoSuchElementException("The Deque is empty");
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
        throwNPEIfNull(value);
        if (size == 0) {
            head = new Node<>(null, value, null);
            tail = head;
        } else {
            head = new Node<>(null, value, head);
            head.next.previous = head;
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
        throwExceptionIfEmpty();
        E tmp = head.value;
        head = head.next;
        if (size == 1) {
            tail = null;
        }
        size--;
        return tmp;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
       throwExceptionIfEmpty();
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
        throwNPEIfNull(value);
        if (size == 0) {
            head = new Node<>(null, value, null);
            tail = head;
        } else {
            tail = new Node<>(tail, value, null);
            tail.previous.next = tail;
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
        throwExceptionIfEmpty();
        E tmp = tail.value;
        tail = tail.previous;
        if (size == 1) {
            head = null;
        }
        size--;
        return tmp;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        throwExceptionIfEmpty();
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
        throwNPEIfNull(value);
        if (isEmpty()) {
            return false;
        }
        Node<E> tmp = new Node<E>(null, null, head);
        while (tmp.next != null) {
            tmp = tmp.next;
            if (Objects.equals(value, tmp.value)) {
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
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
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
        return new LinkedDequeIterator();
    }

    private class LinkedDequeIterator implements Iterator<E> {

        private Node<E> current;
        private boolean methodNextWasCalled;

        LinkedDequeIterator() {
            methodNextWasCalled = false;
            current = new Node<>(null, null, null);
            if (size != 0) {
                current.next = head;
            }
        }

        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next element in the deque");
            }
            methodNextWasCalled = true;
            current = current.next;
            return current.value;
        }

        @Override
        public void remove() {
            if (!methodNextWasCalled || isEmpty()) {
                throw new IllegalStateException();
            }
            methodNextWasCalled = false;
            if (current == head) {
                removeFirst();
                return;
            }
            if (current == tail) {
                removeLast();
                return;
            }
            current.previous.next = current.next;
            current.next.previous = current.previous;
            current = current.previous;
            size--;
        }
    }
}

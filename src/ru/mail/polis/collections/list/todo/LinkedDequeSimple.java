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

    protected class Node {
        public Node previous;
        public Node next;
        public E value;

        public Node(E value) {
            this.value = value;
        }
    }

    protected Node first;
    protected Node last;
    protected int length;

    public LinkedDequeSimple() {
        this.first = null;
        this.last = null;
        this.length = 0;
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
        if (size() == 0) {
            first = last = new Node(value);
            length++;
        } else {
            first.next = new Node(value);
            first.next.previous = first;
            first = first.next;
            length++;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E result = first.value;
        if (size() == 1) {
            first = last = null;
        } else {
            first = first.previous;
            first.next.previous = null;
            first.next = null;
        }
        length--;
        return result;
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
        return first.value;
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
        if (size() == 0) {
            first = last = new Node(value);
            length++;
        } else {
            last.previous = new Node(value);
            last.previous.next = last;
            last = last.previous;
            length++;
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
        E result = last.value;
        if (size() == 1) {
            first = last = null;
        } else {
            last = last.next;
            last.previous.next = null;
            last.previous = null;
        }

        length--;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return last.value;
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
        for (E node : this) {
            if (value.equals(node)) {
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
        return length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        first = null;
        last = null;
        length = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            Node current = first;
            boolean remove = false;

            @Override
            public boolean hasNext() {
                remove = false;
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E result = current.value;
                current = current.previous;
                remove = true;
                return result;
            }

            @Override
            public void remove() {
                if (!remove) {
                    throw new IllegalStateException();
                }
                if (current == null) {
                    length--;
                } else {
                    LinkedDequeSimple.this.remove(current.next);
                }
                remove = false;
            }
        };
    }

    protected void remove(Node currentNode) {
        if (currentNode.next != null) {
            currentNode.next.previous = currentNode.previous;
        }
        if (currentNode.previous != null) {
            currentNode.previous.next = currentNode.next;
        }
        length--;
        if (size() == 0) {
            clear();
        }
    }
}

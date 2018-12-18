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
        Node next, previuos;
        E value;
    }

    Node head, tail;
    int size;

    public LinkedDequeSimple() {
        size = 0;
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

        if (isEmpty()) {
            head = tail = new Node();
            head.value = value;
        } else {
            Node current = new Node();
            current.value = value;
            current.next = head;
            head.previuos = current;
            head = current;
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
        if (isEmpty()) throw new NoSuchElementException();

        E value = head.value;
        if (head == tail) head = tail = null;
        else {
            head = head.next;
            head.previuos = null;
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
        if (isEmpty()) throw new NoSuchElementException();

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
        if (value == null) throw new NullPointerException();

        if (isEmpty()) {
            head = tail = new Node();
            tail.value = value;
        } else {
            Node current = new Node();
            current.value = value;
            tail.next = current;
            current.previuos = tail;
            tail = current;
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
        if (isEmpty()) throw new NoSuchElementException();

        E value = tail.value;
        if (head == tail) head = tail = null;
        else {
            tail = tail.previuos;
            tail.next = null;
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
        if (isEmpty()) throw new NoSuchElementException();

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
        if (value == null)
            throw new NullPointerException();

        Node current = head;
        while (current != null) {
            if (current.value.equals(value))
                return true;

            current = current.next;
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
        head = tail = null;
        size = 0;
    }

    void removeElement(Node curNode) {
        if (curNode == head) {
            removeFirst();
            return;
        }
        if (curNode == tail) {
            removeLast();
            return;
        }
        curNode.previuos.next = curNode.next;
        curNode.next.previuos = curNode.previuos;
        size--;
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
            private Node nextNode = head;
            private Node lastNextNode = null;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();

                lastNextNode = nextNode;
                nextNode = nextNode.next;
                return lastNextNode.value;
            }

            @Override
            public void remove() {
                if (lastNextNode == null) throw new IllegalStateException();

                removeElement(lastNextNode);
                lastNextNode = null;
            }
        };
    }
}
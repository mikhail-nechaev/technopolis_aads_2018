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
        E value;
        Node next, prev;
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
        if (value == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            head = tail = new Node();
            head.value = value;
        } else {
            Node cur = new Node();
            cur.value = value;
            cur.next = head;
            head.prev = cur;
            head = cur;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E value = head.value;
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
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
            throw new NullPointerException();
        }
        if (isEmpty()) {
            head = tail = new Node();
            tail.value = value;
        } else {
            Node cur = new Node();
            cur.value = value;
            tail.next = cur;
            cur.prev = tail;
            tail = cur;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E value = tail.value;
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
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
            throw new NullPointerException();
        }
        Node cur = head;
        while (cur != null) {
            if (cur.value.equals(value)) {
                return true;
            }
            cur = cur.next;
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

    protected void removeElement(Node curNode) {
        if (curNode == head) {
            removeFirst();
            return;
        }
        if (curNode == tail) {
            removeLast();
            return;
        }
        curNode.prev.next = curNode.next;
        curNode.next.prev = curNode.prev;
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
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastNextNode = nextNode;
                nextNode = nextNode.next;
                return lastNextNode.value;
            }

            @Override
            public void remove() {
                if (lastNextNode == null) {
                    throw new IllegalStateException();
                }
                removeElement(lastNextNode);
                lastNextNode = null;
            }
        };
    }
}

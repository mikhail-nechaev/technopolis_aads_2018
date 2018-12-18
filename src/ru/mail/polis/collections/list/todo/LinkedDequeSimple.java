package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;


import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    class Node {
        E value;
        Node prev;
        Node next;
    }

    Node head;

    Node tail;

    int count;
    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null)
            throw new NullPointerException();
        Node node = new Node();
        node.value = value;
        if (isEmpty() || (head == null && tail == null))
            head = tail = node;
        else if (size() == 1 && head != null && tail != null) {
            node.next = tail;
            tail.prev = node;
            head = node;
        } else if (size() > 1) {
            head.prev = node;
            node.next = head;
            head = node;
        }

        count++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {

        if (isEmpty())
            throw new NoSuchElementException();
        Node node = head;
        if (size() == 1 && head != null && tail != null) {
            head = tail = null;
        } else if (size() > 1) {
            head.next.prev = null;
            head = head.next;

        }
        count--;
        return node.value;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
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
        if (value == null)
            throw new NullPointerException();
        Node node = new Node();
        node.value = value;
        if (isEmpty() || (head == null && tail == null))
            head = tail = node;
        else if (size() == 1 && head != null && tail != null) {
            node.prev = tail;
            tail.next = node;
            tail = node;
        } else if (size() > 1) {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        count++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {

        if (isEmpty())
            throw new NoSuchElementException();
        Node node = tail;
        if (size() == 1 && head != null && tail != null) {
            head = tail = null;
        } else if (size() > 1) {
            tail.prev.next = null;
            tail = tail.prev;
        }
        count--;
        return node.value;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {


        if (isEmpty())
            throw new NoSuchElementException();
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
        Node next;
        Node node = head;
        while (node != null) {
            next = node.next;
            if (node.value.equals(value))
                return true;
            node = next;
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
        return count;
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
        count = 0;
        head = tail = null;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public ListIterator<E> iterator() {
        return new LinkedListIterator();
    }

    class LinkedListIterator implements ListIterator<E> {


        Node cursor = new Node();

        int index = -1;

        int remaining = size();

        int done = 0;

        public LinkedListIterator() {
            cursor.next = head;
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            cursor = cursor.next;
            remaining--;
            done++;
            index++;
            return cursor.value;
        }

        @Override
        public boolean hasPrevious() {
            return done > 0;
        }

        @Override
        public E previous() {
            cursor = cursor.prev;
            remaining++;
            done--;
            index--;
            return cursor.value;
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index == -1 ? -1 : (index - 1);
        }

        @Override
        public void remove() {
            if (index == -1)
                throw new IllegalStateException();
            if (index == 0) {
                removeFirst();
                cursor = new Node();
                cursor.next = head;
                index--;
            } else {
                if (hasNext()) {
                    cursor.next.prev = cursor.prev;
                    cursor.prev.next = cursor.next;
                    cursor.value = null;
                }
                done--;
                count--;

            }

        }

        @Override
        public void set(E e) {
            cursor.value = e;
        }

        @Override
        public void add(E e) {
            if (index == -1) {
                addFirst(e);
                cursor = head;
            } else {
                Node node = new Node();
                node.value = e;
                node.prev = cursor;
                node.next = cursor.next;
                if (tail == cursor)
                    tail = node;
                cursor.next = node;
                count++;
            }
            remaining++;
        }
    }
}
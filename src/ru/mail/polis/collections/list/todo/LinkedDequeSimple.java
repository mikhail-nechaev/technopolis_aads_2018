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

    class Node<E> {
        E value;
        Node<E> prev, next;
    }

    Node<E> start, end;
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
        Node<E> node = new Node<>();
        node.value = value;
        if (isEmpty()) {
            start = end = node;
        } else {
            node.next = start;
            start.prev = node;
            start = node;
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
        E value = start.value;
        if (start == end) {
            start = end = null;
        } else {
            start = start.next;
            start.prev = null;
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
        Node<E> node = new Node<>();
        node.value = value;
        if (isEmpty()) {
            start = end = node;
        } else {
            end.next = node;
            node.prev = end;
            end = node;
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
        E value = end.value;
        if (start == end) {
            start = end = null;
        } else {
            end = end.prev;
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
        if (isEmpty()) throw new NoSuchElementException();
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
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            if (value.equals(iterator.next())) return true;
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
        start = end = null;
        size = 0;
    }

    protected void removeElement(Node node) {
        if (node == start) {
            removeFirst();
            return;
        }
        if (node == end) {
            removeLast();
            return;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
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
            private Node nextNode = start;
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
                return (E) lastNextNode.value;
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

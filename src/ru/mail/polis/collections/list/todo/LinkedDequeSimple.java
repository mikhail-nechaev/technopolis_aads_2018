package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.*;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    private int linkedSize = 0;
    private Node<E> head;
    private Node<E> tail;
    private int modCount = 0;

    public class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        public Node(Node<E> prev, E element, Node<E> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
    public LinkedDequeSimple() {
        this.head = null;
        this.tail = null;
    }
    @Override
    public void addFirst(E value) {
        Objects.requireNonNull(value);
        Node<E> h = head;
        Node<E> newNode = new Node<>(null, value, h);
        head = newNode;
        if (h == null)
            tail = newNode;
        else
            h.prev = newNode;
        linkedSize++;
        modCount++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        E element = head.element;
        Node<E> next = head.next;
        head.element = null;
        head.next = null;
        head = next;
        if (next == null)
            tail = null;
        else
            next.prev = null;
        linkedSize--;
        modCount++;
        return element;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.element;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        Objects.requireNonNull(value);
        Node<E> node = tail;
        Node<E> node1 = new Node<>(node, value, null);
        tail = node1;
        if (node == null)
            head = node1;
        else
            node.next = node1;
        linkedSize++;
        modCount++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        E element = tail.element;
        Node<E> prev = tail.prev;
        tail.element = null;
        tail.prev = null;
        tail = prev;
        if (prev == null)
            head = null;
        else
            prev.next = null;
        linkedSize--;
        modCount++;
        return element;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.element;
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
        Objects.requireNonNull(value);
        for (Node<E> x = head; x != null; x = x.next) {
            if (value.equals(x.element))
                return true;
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
        return linkedSize;
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
        linkedSize = 0;
        head = null;
        tail = null;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeSimpleIterator();
    }
    class LinkedDequeSimpleIterator implements Iterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex = 0;
        private int expectedModCount = modCount;

        public LinkedDequeSimpleIterator() {
            next = head;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size();
        }

        @Override
        public E next() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.element;
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (lastReturned == null)
                throw new IllegalStateException();

            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        public E previous() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (!hasPrevious())
                throw new NoSuchElementException();
            if (next == null){
                lastReturned = tail;
                next = tail;
            } else{
                lastReturned = next.prev;
                next = next.prev;
            }
            nextIndex--;
            return lastReturned.element;
        }

        E unlink(Node<E> x) {
            E element = x.element;
            Node<E> next = x.next;
            Node<E> prev = x.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.element = null;
            linkedSize--;
            modCount++;
            return element;
        }
    }
}

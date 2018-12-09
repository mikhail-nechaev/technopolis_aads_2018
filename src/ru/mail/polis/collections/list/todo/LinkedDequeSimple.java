package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;


import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import java.util.Iterator;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    protected int size;
    protected Node<E> head;
    protected Node<E> tail;

    protected static final class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
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
        final Node<E> h = head;
        head = new Node<>(null, Objects.requireNonNull(value), h);
        if (h == null) {
            tail = head;
        } else {
            h.prev = head;
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
        if(head == null)
            throw new NoSuchElementException();
        final E e = head.item;
        final Node<E> next = head.next;
        head.item = null;
        head.next = null;
        head = next;
        if (next == null) {
            tail = null;
        } else {
            next.prev = null;
        }
        size--;
        return e;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if(head == null)
            throw new NoSuchElementException();
        return head.item;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        final Node<E> t = tail;
        tail = new Node<>(t, Objects.requireNonNull(value), null);
        if (t == null) {
            head = tail;
        } else {
            t.next = tail;
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
        if(tail == null)
            throw new NoSuchElementException();
        final E e = tail.item;
        final Node<E> prev = tail.prev;
        tail.item = null;
        tail.prev = null;
        tail = prev;
        if (prev == null) {
            head = null;
        } else {
            prev.next = null;
        }
        size--;
        return e;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if(tail == null)
            throw new NoSuchElementException();
        return tail.item;
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
        for(Node<E> x = head; x != null; x = x.next){
            if(x.item.equals(value)){
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
        Node<E> node = head;
        while(node != null){
            node.prev = null;
            node.item = null;
            node = node.next;
        }
        head = tail = null;
        size = 0;
    }

    protected void insert(E e, Node<E> node){
        final Node<E> prev = node.prev;
        final Node<E> newNode = new Node<>(prev, e, node);
        node.prev = newNode;
        if(prev == null){
            head = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
    }

    protected E delete(Node<E> node){
        final Node<E> prev = node.prev;
        final Node<E> next = node.next;
        final E item = node.item;

        if(prev == null){
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if(next == null){
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.item = null;
        size--;
        return item;
    }

    private Node<E> getNode(int index){
        Node<E> node = head;
        for(int i = 0; i < index; i++){
            node = node.next;
        }
        return node;
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

    protected class LinkedDequeIterator implements ListIterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;

        public LinkedDequeIterator() {
            next = head;
        }

        public LinkedDequeIterator(int index) {
            next = (index == size) ? null : getNode(index);
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            lastReturned = next = (next == null) ? tail : next.prev;
            nextIndex--;
            return lastReturned.item;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            Node<E> lastNext = lastReturned.next;
            delete(lastReturned);
            if (next == lastReturned) {
                next = lastNext;
            } else {
                nextIndex--;
            }
            lastReturned = null;
        }

        @Override
        public void set(E e) {
            if (lastReturned == null) {
                throw new NoSuchElementException();
            }
            lastReturned.item = Objects.requireNonNull(e);
        }

        @Override
        public void add(E e) {
            Objects.requireNonNull(e);
            if (next == null) {
                addLast(e);
            } else {
                insert(e, next);
            }
            nextIndex++;
            lastReturned = null;
        }
    }
}

package ru.mail.polis.collections.list.todo;

import java.util.*;

public class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E> {

    public LinkedDequeFull() {
        super();
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        super.addLast(e);
        return true;
    }

    @Override
    public E pollFirst() {
        if (isEmpty())
            return null;
        return super.removeFirst();
    }

    @Override
    public E pollLast() {
        if (isEmpty())
            return null;
        return super.removeLast();
    }

    @Override
    public E peekFirst() {
        if (isEmpty())
            return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (isEmpty())
            return null;
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (isEmpty())
            return false;
        Node node = head;
        while (node != null) {
            if (node.value.equals(o)) {
                delete(node);
                return true;
            }
            node = node.next;
        }
        return false;
    }

    private void delete(Node node) {
        Node prev = node.prev;
        Node next = node.next;
        if (prev == null) {
            removeFirst();
        } else if (next == null) {
            removeLast();
        } else {
            node.value = null;
            next.prev = prev;
            prev.next = next;
            count--;
        }
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (isEmpty())
            return false;
        Node node = tail;
        while (tail != null) {
            if (node.value.equals(o)) {
                delete(node);
                return true;
            }
            node = node.prev;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        if (isEmpty())
            return null;
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c);
        int s = size();
        c.forEach(this::addLast);
        return size() > s;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (Object element :
                c) {
            while (contains(element)) {
                removeFirstOccurrence(element);
            }

        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        Node node = head;
        while (node != null) {
            Node tmp = node.next;
            Object item = node.value;
            if (!c.contains(item)) {
                while (contains(item)) {
                    removeFirstOccurrence(item);
                }
            }
            node = tmp;
        }

        return true;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        E value = (E) o;
        if (value == null)
            throw new NullPointerException();
        Node node = head;
        while (node != null) {
            if (node.value.equals(value))
                return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[count];
        int i = 0;
        Node node = head;
        while (node != null) {
            a[i++] = node.value;
            node = node.next;
        }
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) toArray();
    }


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
        super.addFirst(value);
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
        if (head == null)
            return null;
        return pollFirst();
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        return super.getFirst();
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
        offerLast(value);
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
        if (tail == null)
            return null;
        return pollLast();
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        return super.getLast();
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

    @Override
    public ListIterator<E> descendingIterator() {
        return new LinkedListIterator();
    }

    @Override
    public ListIterator<E> iterator() {
        return descendingIterator();
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
        super.clear();
    }


}

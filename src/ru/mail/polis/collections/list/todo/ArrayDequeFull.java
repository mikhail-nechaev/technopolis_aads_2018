package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {
     /** Isn`t fixed, expandable with resize method, power of two */
     /** Array for the deque */
    /** next two are indexes*/


    ArrayDequeFull() {
        super();
    }

    ArrayDequeFull(int startCapacity) {
        super(startCapacity);
    }

    ArrayDequeFull(Collection<E> collection) {
        super(collection);
    }


    @Override
    public boolean offerFirst(E e) {
        if (e == null) throw new NullPointerException("Specified elem is null");
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null) throw new NullPointerException();
        addLast(e);
        return true;
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) return null;
        return removeFirst();
    }

    @Override
    public E pollLast() {
        if (isEmpty()) return null;
        return removeLast();
    }

    @Override
    public E peekFirst() {
        if (isEmpty()) return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (isEmpty()) return null;
        return getLast();
    }

    @Override
    public E remove() {
        if (isEmpty()) throw new NoSuchElementException();
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (isEmpty()) throw new NoSuchElementException("No elements in dequeue");
        int present = dequeue.length - 1;
        int newHead = head;
        E val = dequeue[newHead];
        while (val != null) {
            for (int i=newHead; i < dequeue.length - 1; i++) {
                if (o.equals(val)) {
                    removeByIndex(i);
                    return true;
                }
                i = (i + 1) & present;
                val = dequeue[newHead];
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (isEmpty()) throw new NoSuchElementException("No elements in dequeue");
        int present = dequeue.length - 1;
        int newTail = tail;
        E val = dequeue[newTail];
        while (val != null) {
            for (int i=newTail; i < dequeue.length - 1; i--) {
                if (o.equals(val)) {
                    return removeByIndex(i);
//                    return true;
                }
                i = (i + 1) & present;
                val = dequeue[newTail];
            }
        }
        return false;
    }

    //Removes elem specified by index and connects splitted parts of dequeue in one
    private boolean removeByIndex(int index) {
        E[] dequeue = this.dequeue;
        int h = head;
        int t = tail;
        int present = dequeue.length - 1;
        int offset = (index - h) & present;
        int finish = (tail - index) & present;

        if (offset < finish) {
            if (h <= index) {
                System.arraycopy(dequeue, h, dequeue, h + 1, offset);
            }
            else {
                System.arraycopy(dequeue, 0, dequeue, 1, index);
                dequeue[0] = dequeue[present];
                System.arraycopy(dequeue, h, dequeue, h + 1, present - h);
            }
            dequeue[h] = null;
            head = (h + 1) & present;
            return false;
        }
        else {
            if (index < t) {
                System.arraycopy(dequeue, index + 1, dequeue, index, finish);
                tail = t - 1;
            }
            else {
                System.arraycopy(dequeue, index + 1, dequeue, index, present - index);
                dequeue[present] = dequeue[0];
                System.arraycopy(dequeue, 1, dequeue, 0, t);
                tail = (t - 1) & present;
            }
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) throw new NullPointerException();
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        return offerLast(e);
    }

    @Override
    public E poll() {
        if (isEmpty()) return null;
        return pollFirst();
    }

    @Override
    public E element() {
        if (isEmpty()) throw new NoSuchElementException();
        return getFirst();
    }

    @Override
    public E peek() {
        if (isEmpty()) return null;
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void push(E e) {
        if (e == null) throw new NullPointerException();
        addFirst(e);
    }

    @Override
    public E pop() {
        if (isEmpty()) throw new NoSuchElementException();
        return removeFirst();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (isEmpty()) throw new UnsupportedOperationException("No elements in dequeue");
        int present = dequeue.length - 1;
        int newHead = head;
        E val = dequeue[newHead];
        while (val != null) {
            for (int i=newHead; i < dequeue.length - 1; i++) {
                if (o.equals(val)) {
                    return true;
                }
                newHead = (i + 1) & present;
                val = dequeue[newHead];
            }
        }
        return false;
    }

    public E getByIndex(int index) {
        if (isEmpty()) throw new UnsupportedOperationException("No elements in dequeue");

            for (int i=head; i < (dequeue.length - 1) % DEFAULT_CAPACITY; i++) {


        }
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DequeueFullIterator();

    }

    private class DequeueFullIterator implements Iterator<E> {
        private int next = 0;
        private int prev = -1;
        private int current = head - 1;
        private int future = head;
        int size = size();

        @Override
        public boolean hasNext() {
            return (next < size);
        }

        @Override
        public E next() {
            prev = next;
            next++;
            current = future;
            future = (future + 1) % DEFAULT_CAPACITY;
            return getByIndex(current);
        }

        @Override
        public void remove() {
            if (!hasNext()) throw new IllegalStateException();
            if (isEmpty()) throw new NoSuchElementException();
            removeByIndex(current);
        }
    }
}

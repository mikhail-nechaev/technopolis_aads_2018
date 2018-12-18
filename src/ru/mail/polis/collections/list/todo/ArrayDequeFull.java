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
        for (Object item : c) {
            this.add((E) item);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Iterator<E> it = iterator();
        int deleted = 0;
        for (E item = it.next(); it.hasNext(); item = it.next()) {
            if (c.contains(item)) {
                it.remove();
                deleted++;
            }
        }
        return (deleted != 0);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Iterator<E> it = iterator();
        int deleted = 0;
        for (E item = it.next(); it.hasNext(); item = it.next()) {
            if (c.contains(item) == false) {
                it.remove();
                deleted++;
            }
        }
        return (deleted != 0);
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
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
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

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        return toArray(res);

    }

    @Override
    public <T> T[] toArray(T[] a) {
        int l,r; //pointers on left and right parts of newDequeue in oldDequeue
        if (head < tail) {
            r = dequeue.length - head;
            l = head;
        }
        else {
            l = dequeue.length - head;
            r = dequeue.length - tail;
        }
        System.arraycopy(dequeue, l, a, 0, head);
        System.arraycopy(dequeue, r, a, head + 1, r - 1);
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DequeueFullIterator();

    }

    private class DequeueFullIterator implements Iterator<E> {
        private int next = 0;
        private int prev = -1;
        private int future = head - 1;
        private int current = head;
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
            future = (future - 1) % dequeue.length;
            E res = dequeue[(head + current) % dequeue.length];
            return res;
        }
    }
}

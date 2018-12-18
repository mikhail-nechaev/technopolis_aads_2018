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
    /**
     * next two are indexes
     */


    public ArrayDequeFull() {
        super();
    }

    public ArrayDequeFull(int startCapacity) {
        super(startCapacity);
    }

    public ArrayDequeFull(Collection<E> collection) {
        super(collection);
    }


    @Override
    public boolean offerFirst(E e) {
        if (e == null) {
            throw new NullPointerException("Specified elem is null");
        }
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        addLast(e);
        return true;
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        return removeLast();
    }

    @Override
    public E peekFirst() {
        if (isEmpty()) {
            return null;
        }
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (isEmpty()) {
            return null;
        }
        return getLast();
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int pos = findFirstOccurence(o);
        if (pos != -1) {
            removeByIndex(pos);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        for (int i = 0, index = head; i < size; i++, index = (index - 1 + dequeue.length) % dequeue.length) {
            if (dequeue[index].equals(o)) {
                removeByIndex(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        return offerLast(e);
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        return pollFirst();
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getFirst();
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
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
        if (e == null) {
            throw new NullPointerException();
        }
        addFirst(e);
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
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
        return findFirstOccurence(o) != -1;
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        return toArray(res);

    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length >= dequeue.length) {
            copyToBeginning(a);
            return a;
        }
        T[] biggerA = (T[]) new Object[dequeue.length];
        copyToBeginning(biggerA);
        return biggerA;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DequeueDescendingIterator();

    }

    private class DequeueDescendingIterator implements Iterator<E> {
        private int iteratedValuesCounter = 0;
        private int current = head;

        @Override
        public boolean hasNext() {
            return (iteratedValuesCounter < size);
        }

        @Override
        public E next() {
            iteratedValuesCounter++;
            E res = dequeue[(current + dequeue.length) % dequeue.length];
            current--;
            return res;
        }
    }
}

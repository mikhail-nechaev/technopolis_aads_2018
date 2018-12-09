package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        Iterator<E> iter = this.iterator();
        while (iter.hasNext()) {
            Object check = iter.next();
            if (check.equals(o)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        Iterator<E> iter = this.descendingIterator();
        while (iter.hasNext()) {
            Object check = iter.next();
            if (check.equals(o)) {
                iter.remove();
                return true;
            }
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
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                remove(o);
            }
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
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        Iterator<E> iter = this.iterator();
        while (iter.hasNext()) {
            Object check = iter.next();
            if (check.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        return toArray(array);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> iter = this.iterator();
        int i = 0;
        for (E elem = iter.next(); iter.hasNext(); elem = iter.next(), i++) {
            a[i] = (T) elem;
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescLinkedIterator<>();
    }

    private class DescLinkedIterator<T extends E> implements Iterator<E> {

        private Node<E> lastReturned;
        private Node<E> nextElem = tail;

        @Override
        public boolean hasNext() {
            return nextElem != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = nextElem;
            nextElem = nextElem.prev;
            return lastReturned.elem;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            if (lastReturned == tail) {
                removeLast();
            } else if (lastReturned == head) {
                removeFirst();
            } else {
                lastReturned.prev.next = lastReturned.next;
                lastReturned.next.prev = lastReturned.prev;
                size--;
            }
        }
    }
}

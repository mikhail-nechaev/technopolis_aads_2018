package ru.mail.polis.collections.list.todo;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {

    public ArrayDequeFull() {
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
        @SuppressWarnings("unchecked")
        E first = (E) array[head];
        removeFirst();
        return first;
    }

    @Override
    public E pollLast() {
        @SuppressWarnings("unchecked")
        E last = (E) array[tail];
        removeLast();
        return last;
    }

    @Override
    public E peekFirst() {
        return getFirst();
    }

    @Override
    public E peekLast() {
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = head; i == tail; ) {
            @SuppressWarnings("unchecked")
            Object check = array[i];
            if (o.equals(check)) {
                remove(o);
                return true;
            }
            i = i < array.length - 1 ? i+1 : 0;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = tail; i == head; ) {
            @SuppressWarnings("unchecked")
            Object check = array[i];
            if (o.equals(check)) {
                remove(o);
                return true;
            }
            i = i > 0 ? i-1 : array.length - 1;
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
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = head; i == tail; ) {
            if (o.equals(array[i])) {
                return true;
            }
            i = i < array.length - 1 ? i+1 : 0;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        if (a.length < array.length) {
            return (T[]) Arrays.copyOf(array, array.length, a.getClass());
        }
        System.arraycopy(array, 0, a, 0, array.length);
        if (a.length > array.length) {
            a[array.length] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }
    //todo: remove <abstract> modifier and implement
    private class DescendingIterator implements Iterator<E> {

        private int cursor = tail;
        private int end = head;
        private int lastReturn = -1;

        @Override
        public boolean hasNext() {
            return cursor != end;
        }

        @Override
        public E next() {
            if (cursor == end) {
                throw new NoSuchElementException();
            }
            cursor = cursor > 0 ? cursor - 1 : array.length - 1;
            @SuppressWarnings("unchecked")
            E next = (E) array[cursor];
            if (head != end || next == null) {
                throw new ConcurrentModificationException();
            }
            lastReturn = cursor;
            return next;
        }

        @Override
        public void remove() {
            if (lastReturn < 0) {
                throw new IllegalStateException();
            }
            if (!ArrayDequeFull.this.remove(lastReturn)) {
                cursor = cursor < array.length - 1 ? cursor + 1 : 0;
            }
            lastReturn = -1;
        }
    }
}

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
        if (contains(o)) {
            int index = indexOf(o);
            delete(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int exit = head == 0 ? array.length - 1 : head - 1;
        for (int i = tail; ; ) {
            @SuppressWarnings("unchecked")
            Object check = array[i];
            if (o.equals(check)) {
                delete(i);
                return true;
            }
            i = i > 0 ? i-1 : array.length - 1;
            if (i == exit) break;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        return offerLast(e);
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
        Iterator<E> iterator = iterator();
        int count = 0;
        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next()) {
            if (c.contains(elem)) {
                iterator.remove();
                count++;
            }
        }
        return count >= c.size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Iterator<E> iterator = iterator();
        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next()) {
            if (!c.contains(elem)) {
                iterator.remove();
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int exit = tail == array.length - 1 ? 0 : tail + 1;
        for (int i = head; ; ) {
            if (o.equals(array[i])) {
                return true;
            }
            i = i == array.length - 1 ? 0 : i+1;
            if (i == exit) break;
        }
        return false;
    }

    public int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        int exit = tail == array.length - 1 ? 0 : tail + 1;
        for (int i = head; ; ) {
            if (o.equals(array[i])) {
                return i;
            }
            i = i == array.length - 1 ? 0 : i+1;
            if (i == exit) break;
        }
        return -1;
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
            if (!ArrayDequeFull.this.remove(array[lastReturn])) {
                cursor = cursor < array.length - 1 ? cursor + 1 : 0;
            }
            lastReturn = -1;
        }
    }
}

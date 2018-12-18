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
        if (e == null) throw new NullPointerException();
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
        return removeFirst();
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
        if (o == null) throw new NullPointerException();
        Node item = first;
        while (item != null) {
            if (item.current.equals(o)) {
                remove(item);
                return true;
            }
            else item = item.next;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) throw new NullPointerException();
        Node item = last;
        while (item != null) {
            if (item.current.equals(o)) {
                remove(item);
                return true;
            }
            else item = item.prev;
        }
        return false;
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
        if (isEmpty()) return null;
        return getFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (Object item: c) {
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
        for (Object item : c) {
            if (contains(item) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        Node item = first;
        while (item != null) {
            if (item.current.equals(o)) {
                return true;
            }
            item = item.next;
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
        Iterator<E> it = iterator();
        int index = 0;
        for (E item = it.next(); it.hasNext(); item = it.next(), index++) {
            a[index] = (T) item;
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {

            private Node future, current = last;

            @Override
            public boolean hasNext() {
                return current.prev != null;
            }

            @Override
            public E next() {
                if (hasNext() == false) throw new NoSuchElementException();
                current = future;
                future = current.prev;
                return (E) current.current;
            }
        };
    }
    }

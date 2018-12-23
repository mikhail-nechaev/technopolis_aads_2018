package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E> {
    @Override
    public boolean offerFirst(E e) {
        if (e == null) throw new NullPointerException();
        this.addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null) throw new NullPointerException();
        this.addLast(e);
        return true;
    }

    @Override
    public E pollFirst() {
        return isEmpty() ? null : removeFirst();
    }

    @Override
    public E pollLast() {
        return isEmpty() ? null : removeLast();
    }

    @Override
    public E peekFirst() {
        return isEmpty() ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return isEmpty() ? null : getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) throw new NullPointerException();
        Iterator iterator = iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(o)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) throw new NullPointerException();
        Iterator iterator = descendingIterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(o)) {
                iterator.remove();
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
        addLast(e);
        return true;
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
        if (isEmpty()) throw new NoSuchElementException();
        return peekFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        c.forEach(this::addLast);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        AtomicBoolean isCollectionChanged = new AtomicBoolean(false);
        c.forEach(it -> {
            while (this.contains(it)) {
                remove(it);
                isCollectionChanged.set(true);
            }
        });
        return isCollectionChanged.get();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        AtomicBoolean isCollectionChanged = new AtomicBoolean(false);
        Iterator iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                isCollectionChanged.set(true);
            }
        }
        return isCollectionChanged.get();
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
        for (Object elem : c) {
            if (!contains(elem)) return false;
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) throw new NullPointerException();
        for (Object i : this) {
            if (i.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (Object elem : this) {
            result[i] = elem;
            i++;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] result = a.length >= size() ? a : (T[]) new Object[size()];
        int i = 0;
        for (Object elem : this) {
            result[i] = (T) elem;
            i++;
        }
        return result;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node next = end;
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (next == null) throw new NoSuchElementException();
                E value = next.value;
                next = next.previous;
                canRemove = true;
                return value;
            }

            @Override
            public void remove() {
                if (!canRemove) throw new IllegalStateException();
                if (next == null) {
                    removeFirst();
                } else if (next.next.next == null) {
                    removeLast();
                } else {
                    next.next.next.previous = next;
                    next.next = next.next.next;
                    size--;
                }
                canRemove =  false;
            }
        };
    }
}

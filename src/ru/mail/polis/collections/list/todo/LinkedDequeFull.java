package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E> {

    @Override
    public boolean offerFirst(E e) {
        throwNPEIfNull(e);
        try {
            addFirst(e);
        } catch (IllegalStateException exception) {
            return false;
        }
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        throwNPEIfNull(e);
        try {
            addLast(e);
        } catch (IllegalStateException exception) {
            return false;
        }
        return true;
    }

    @Override
    public E pollFirst() {
        try {
            return removeFirst();
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public E pollLast() {
        try {
            return removeLast();
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public E peekFirst() {
        try {
            return getFirst();
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public E peekLast() {
        try {
            return getLast();
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throwNPEIfNull(o);
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if(Objects.equals(iterator.next(), o)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throwNPEIfNull(o);
        Iterator<E> iterator = descendingIterator();
        while (iterator.hasNext()) {
            if(Objects.equals(iterator.next(), o)) {
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
        for (E e: c) {
            throwNPEIfNull(e);
            addLast(e);
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


    //    not done yet
    @Override
    public boolean contains(Object o) {
        throwNPEIfNull(o);
        for (E e : this) {
            if (Objects.equals(o, e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    private class DescendingIterator implements Iterator<E> {

        private Node<E> current;
        private boolean methodNextWasCalled;

        private DescendingIterator() {
            methodNextWasCalled = false;
            current = new Node<>(null, null, null);
            if (size != 0) {
                current.previous = tail;
            }
        }

        @Override
        public boolean hasNext() {
            return current.previous != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next element in the deque");
            }
            methodNextWasCalled = true;
            current = current.previous;
            return current.value;
        }

        @Override
        public void remove() {
            if (!methodNextWasCalled || isEmpty()) {
                throw new IllegalStateException();
            }
            methodNextWasCalled = false;
            if (current == head) {
                removeFirst();
                return;
            }
            if (current == tail) {
                removeLast();
                return;
            }
            current.previous.next = current.next;
            current.next.previous = current.previous;
            current = current.next;
            size--;
        }
    }

    @Override
    public Object[] toArray() {
        Iterator<E> iterator = iterator();
        Object[] tmp = new Object[size()];
        for (Object o: tmp) {
            o = iterator.next();
        }
        return tmp;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) {
            Object[] newArray = new Object[size()];
            Iterator<E> iterator = iterator();
            for (Object o: newArray) {
                o = (T) iterator.next();
            }
            return (T[]) newArray;
        }
        Iterator<E> iterator = iterator();
        int i = 0;
        while (iterator.hasNext()) {
            a[i] = (T) iterator.next();
            i++;
        }
        if (a.length > size()) {
            a[i] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c) {
            throwNPEIfNull(o);
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for (Object o: c) {
            throwNPEIfNull(o);
            res = res || remove(o);
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Iterator<E> iterator = iterator();
        boolean res = false;
        while (iterator.hasNext()) {
            E tmp = iterator.next();
            if (!c.contains(tmp)) {
                iterator.remove();
                res = true;
            }
        }
        return res;
    }
}

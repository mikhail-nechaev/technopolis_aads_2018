package ru.mail.polis.collections.list.todo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {


    @Override
    public boolean offerFirst(E e) {
        throwExceptionIfArgumentIsNull(e);
        try {
            addFirst(e);
        } catch (IllegalStateException exception) {
            return false;
        }
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        throwExceptionIfArgumentIsNull(e);
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
        throwExceptionIfArgumentIsNull(o);
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
        throwExceptionIfArgumentIsNull(o);
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
            throwExceptionIfArgumentIsNull(e);
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

    @Override
    public boolean contains(Object o) {
        throwExceptionIfArgumentIsNull(o);
        for (Object obj: array) {
            if (Objects.equals(obj, o)) {
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

        private int ptr;
        private boolean methodNextWasCalled;
        private int stepsMade;

        private DescendingIterator() {
            methodNextWasCalled = false;
            stepsMade = 0;
            ptr = tail;
        }

        @Override
        public boolean hasNext() {
            return stepsMade < size();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            ptr = (ptr - 1 + array.length) % array.length;
            stepsMade++;
            methodNextWasCalled = true;
            return (E) array[ptr];
        }

        @Override
        public void remove() {
            if (!methodNextWasCalled) {
                throw new IllegalStateException();
            }
            methodNextWasCalled = false;
            Object[] newArray = new Object[array.length];
            int newPtrPosition = 0;
            for (int i = 0; i < array.length-2; i++) {
                if (head == ptr) {
                    head = (head + 1) % array.length;
                    newPtrPosition = i;
                }
                newArray[i] = array[head];
                head = (head + 1) % array.length;
            }
            ptr = newPtrPosition;
            head = 0;
            size--;
            tail = size;
            array = newArray;
            stepsMade--;
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
        Object[] linearized = new Object[array.length];
        int ptr = head;
        for (int i = 0; i < array.length; i++) {
            linearized[i] = array[ptr];
            ptr = (ptr + 1) % array.length;
        }
        if (a.length < size) {
            return (T[]) Arrays.copyOf(linearized, size, a.getClass());
        }
        System.arraycopy(linearized, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c) {
            throwExceptionIfArgumentIsNull(o);
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
            throwExceptionIfArgumentIsNull(o);
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

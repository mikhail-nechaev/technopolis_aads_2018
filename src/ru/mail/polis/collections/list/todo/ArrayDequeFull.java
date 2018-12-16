package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {
    @Override
    public boolean offerFirst(E e) {
        if (e == null) return false;
        this.addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null) return false;
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
        return isEmpty() ? null : array[startPointer];
    }

    @Override
    public E peekLast() {
        return isEmpty() ? null : array[endPointer];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
            ListIterator iterator = iterator();
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
        ListIterator iterator = iterator();
        boolean hasElement = false;
        Object lastElem = null;
        while (iterator.hasNext()) {
            lastElem = iterator.next();
            if (lastElem.equals(o)) {
                hasElement = true;
            }
        }
        if (lastElem == null) return false;
        if (lastElem.equals(o)) {
            iterator.remove();
            return true;
        }
        if (hasElement) {
            while (iterator.hasPrevious()) {
                if (iterator.previous().equals(o)) {
                    iterator.remove();
                    return true;
                }
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
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
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
        return new Iterator<E>() {
            int pointer = -1;

            private int shiftPrevious(int index) {
                return (index == 0) ? array.length - 1 : index - 1;
            }

            @Override
            public boolean hasNext() {
                if (size() == 0) return false;
                if (pointer == -1) return true;
                if (pointer == startPointer) return false;
                return true;
            }

            @Override
            public E next() {
                if (pointer == -1) pointer = endPointer;
                else pointer = shiftPrevious(pointer);
                return array[pointer];
            }
        };
    }
}

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
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (o.equals(iterator.next())) {
                iterator.remove();
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
        Iterator<E> iterator = descendingIterator();
        while (iterator.hasNext()) {
            if (o.equals(iterator.next())) {
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
        offerLast(e);
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
        for (Object value : c) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E value : c) {
            addLast(value);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (c.contains(iterator.next())) {
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean contains(Object o) {
        return super.contains((E) o);
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        return toArray(a);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> iterator = this.iterator();
        int i = 0;
        for (E value = iterator.next(); iterator.hasNext(); value = iterator.next(), i++) {
            a[i] = (T) value;
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node nextNode = end;
            private Node lastNextNode;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastNextNode = nextNode;
                nextNode = nextNode.prev;
                return (E) lastNextNode.value;
            }

            @Override
            public void remove() {
                if (lastNextNode == null) {
                    throw new IllegalStateException();
                }
                removeElement(lastNextNode);
                lastNextNode = null;
            }
        };
    }
}
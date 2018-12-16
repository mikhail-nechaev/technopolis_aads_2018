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
        Node elem = head;

        while (elem != null) {
            if (elem.data.equals(o)) {
                removeNode(elem);
                return true;
            }
            elem = elem.right;
        }

        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node elem = tail;
        if (o == null) {
            throw new NullPointerException();
        }

        while (elem != null) {
            if (elem.data.equals(o)) {
                return removeNode(elem);
            }
            elem = elem.left;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        return offerFirst(e);
    }

    @Override
    public boolean offer(E e) {
        return offerFirst(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }

        return getFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::add);
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
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }




    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        boolean check = false;
        Node elem = head;
        while (elem != null) {
            Node next = elem.right;
            if (elem.data.equals(o)) {
                removeNode(elem);
                check = true;
            }
            elem = next;
        }
        return check;
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
        return indexOf(o) > -1;
    }


    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        return toArray(array);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> iterator = iterator();
        int i = 0;
        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next(), i++) {
            a[i] = (T) elem;
        }
        return a;
    }


    @Override
    public Iterator<E> descendingIterator() {

        return new Iterator<E>() {
            private Node lastReturned, nextElem = tail;

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
                nextElem = nextElem.left;
                return lastReturned.data;
            }


            @Override
            public void remove() {
                LinkedDequeFull.this.removeNode(lastReturned);
            }
        };

    }
}

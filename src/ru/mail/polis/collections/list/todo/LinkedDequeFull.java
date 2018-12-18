package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("ALL")
public class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E> {

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
        return size() == 0 ? null : removeFirst();
    }

    @Override
    public E pollLast() {
        return size() == 0 ? null : removeLast();
    }

    @Override
    public E peekFirst() {
        return size() == 0 ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return size() == 0 ? null : getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            return false;
        }
        Node head = first;
        while (head != null) {
            if (head.value.equals(o)) {
                remove(head);
                return true;
            }
            head = head.previous;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            return false;
        }
        Node tail = last;
        while (tail != null) {
            if (tail.value.equals(o)) {
                remove(tail);
                return true;
            }
            tail = tail.next;
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
        if (c == null || c.contains(null)) {
            throw new NullPointerException();
        }
        for (E o : c) {
            addLast(o);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.contains(null)) {
            throw new NullPointerException();
        }
        boolean result = false;
        if (isEmpty()) {
            return result;
        }
        for (Object o : c) {
            while (first != null) {
                if (first.value.equals(o)) {
                    remove(first);
                    result = true;
                }
                first = first.previous;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null || c.contains(null)) {
            throw new NullPointerException();
        }
        boolean result = false;
        if (isEmpty()) {
            return result;
        }
        for (E o : this) {
            for (Object e : c) {
                if (!((Node) o).value.equals(e)) {
                    remove(o);
                    result = true;
                }
            }
        }
        return result;
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
        if (c == null || c.contains(null)) {
            throw new NullPointerException();
        }
        if (c.isEmpty()) {
            return true;
        }
        if (isEmpty()) {
            return false;
        }
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
        for (E element : this) {
            if (element.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (E o : this) {
            result[i++] = o;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        if (size() > a.length) {
            a = (T[]) new Object[size()];
        }
        System.arraycopy(a, 0, toArray(), 0, size());
        for (int i = size(); i < a.length; i++) {
            a[i] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            Node current = last;
            boolean remove = false;

            @Override
            public boolean hasNext() {
                remove = false;
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E result = current.value;
                current = current.next;
                remove = true;
                return result;
            }

            @Override
            public void remove() {
                if (!remove) {
                    throw new IllegalStateException();
                }
                if (current == null) {
                    length--;
                } else {
                    LinkedDequeFull.this.remove(current.next);
                }
                remove = false;
            }
        };
    }
}
package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {


    public ArrayDequeFull() {
        super();
    }

    public ArrayDequeFull(int capacity) {
        super(capacity);
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
        if (contains(o)) {
            int index = indexOf(o);
            delete(index);
            return true;
        }
        return false;
    }

    private int indexOf(Object o) {
        for (int i = head, index = 0; index < size; i = ++i % capacity, index++) {
            if (o.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (contains(o)) {
            for (int i = tail, index = 0; index < size; index++, i = i == 0 ? capacity - 1 : i - 1) {
                if (o.equals(array[i])) {
                    delete(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        return offerLast(e);
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
        for (int i = head, index = 0; index < size; i = ++i % capacity, index++) {
            if (o.equals(array[i])) {
                delete(i);
                check = true;
            }
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
        return indexOf(o) >= 0;
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        return toArray(a);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (head < tail) {
            System.arraycopy(array, head, a, 0, a.length);
        } else {
            System.arraycopy(array, head, a, 0, array.length - head);
            System.arraycopy(array, 0, a, array.length - head, tail + 1);
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) array[(head + index) % capacity];
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int nextIndex = 0;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                nextIndex++;
                return get(nextIndex - 1);
            }
        };
    }
}

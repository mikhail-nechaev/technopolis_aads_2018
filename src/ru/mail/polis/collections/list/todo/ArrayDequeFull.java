package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {
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
        if (o == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            return false;
        }
        Iterator<E> iterator = iterator();
        return iterate(o, iterator);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            return false;
        }
        Iterator<E> iterator = descendingIterator();
        return iterate(o, iterator);
    }

    private boolean iterate(Object o, Iterator<E> iterator) {
        while (iterator.hasNext()) {
            E temp = iterator.next();
            if (temp.equals(o)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
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
        Iterator<E> iterator = iterator();
        for (Object o : c) {
            while (iterator.hasNext()) {
                if (iterator.next().equals(o)) {
                    iterator.remove();
                    result = true;
                }
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
                if (!o.equals(e)) {
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
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (isEmpty()) {
            return false;
        }
        int i = 0;
        int last = this.last;
        while (i < size()) {
            if (last + 1 == data.length) {
                last = 0;
            } else {
                last++;
            }
            if (o.equals(data[last])) {
                return true;
            }
            i++;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (Object o : this) {
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
            int tail = last;
            int size = size();
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                canRemove = false;
                return size != 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (tail + 1 == data.length) {
                    tail = 0;
                } else {
                    tail++;
                }
                size--;
                canRemove = true;
                return (E) data[tail];
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                ArrayDequeFull.this.remove(size);
                if (tail < first && tail > 0) {
                    tail--;
                }
                canRemove = false;
            }
        };
    }
}

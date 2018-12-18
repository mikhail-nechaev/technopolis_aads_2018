package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {

    public ArrayDequeFull() {
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
        if (start <= end) {
            System.arraycopy(elements, start, a, 0, size);
        } else {
            System.arraycopy(elements, start, a, 0, size - end);
            System.arraycopy(elements, 0, a, size - start, end + 1);
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int tail = ArrayDequeFull.this.end;
            private int nextIndex = 0;
            private int lastNextIndex = -1;
            private int size = size();

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                lastNextIndex = nextIndex++;
                return (E) elements[(tail - lastNextIndex + capasity) % capasity];
            }

            @Override
            public void remove() {
                if (lastNextIndex == -1) {
                    throw new IllegalStateException();
                }
                removeElement((tail - lastNextIndex + capasity) % capasity);
                lastNextIndex = -1;
            }
        };
    }
}

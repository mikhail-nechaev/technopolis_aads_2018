package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {

    @Override
    public boolean offerFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null)
            throw new NullPointerException();
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
        if (o == null)
            throw new NullPointerException();
        if (!contains(o))
            return false;

        for (int i = head + 1; i != tail; i = (i + 1) % values.length){
            if (values[i].equals(o)){
                deleteByIndex(i);
                break;
            }
        }
        return true;

    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (!contains(o))
            return false;

        for (int i = tail - 1 + values.length; i % values.length != head; i--){
            int temp = i % values.length;
            if (values[temp].equals(o)){
                deleteByIndex(temp);
                break;
            }
        }
        return true;
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
        if (c == null)
            throw new NullPointerException();
        if (c.isEmpty())
            return true;
        for (Object obj : c) {
            if (obj == null)
                throw new NullPointerException();
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c.isEmpty())
            return true;
        for (E obj : c) {
            if (obj == null)
                throw new NullPointerException();
            return add(obj);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();
        if (c.isEmpty())
            return false;

        boolean check = false;

        for (Object obj : c) {
            if (contains(obj)){
                if (remove(obj))
                    check = true;
            }
        }
        return check;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();
        if (c.isEmpty()){
            clear();
            return true;
        }

        boolean check = false;

        Iterator<E> iterator = iterator();

        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next()) {
            if (!c.contains(elem)) {
                iterator.remove();
                check = true;
            }
        }

        return check;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            throw new NullPointerException("Element is null");
        if (values == null)
            throw new IllegalStateException("Array is null");
        if (lenght == 0)
            return false;

        boolean contains = false;
        int i = head + 1;
        if (i == values.length)
            i = 0;
        while (i != tail){
            if (values[i].equals(o)){
                contains = true;
                break;
            }
            i++;
            if (i == values.length)
                i = 0;
        }
        return contains;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[lenght];
        return toArray(array);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (head < tail) {
            System.arraycopy(values, head, a, 0, a.length);
        } else {
            System.arraycopy(values, head, a, 0, values.length - head);
            System.arraycopy(values, 0, a, values.length - head, tail + 1);
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {

            private int currentIndex = tail;

            @Override
            public boolean hasNext() {
                return !(moveIter() == head);
            }

            @Override
            public E next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                currentIndex = moveIter();
                return (E) values[currentIndex];
            }

            private int moveIter(){
                int newIndex = currentIndex--;
                if (newIndex == -1)
                    newIndex = values.length - 1;
                return newIndex;
            }

        };
    }

}

package ru.mail.polis.collections.list.todo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;

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
        if (isEmpty())
            return null;
        return removeFirst();
    }

    @Override
    public E pollLast() {
        if (isEmpty())
            return null;
        return removeLast();
    }

    @Override
    public E peekFirst() {
        if (isEmpty())
            return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (isEmpty())
            return null;
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Objects.requireNonNull(o);
        Iterator<E> it = descendingIterator();
        while(it.hasNext()){
            if(o.equals(it.next())) {
                it.remove();
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
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        if (isEmpty())
            return null;
        return removeFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        if (isEmpty())
            return null;
        return getFirst();
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
        Objects.requireNonNull(o);
        Iterator<E> it = iterator();
        while(it.hasNext()){
            if(o.equals(it.next())){
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> col) {
        if(col == null){
            throw new NullPointerException();
        }
        Iterator it = col.iterator();
        while(it.hasNext()){
            if(!contains(it.next())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> col) {
        Object[] objects = col.toArray();
        if(objects.length == 0){
            return false;
        }
        for(Object e : objects){
            addLast((E)e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> col) {
        Objects.requireNonNull(col);
        boolean res = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (col.contains(it.next())) {
                it.remove();
                res = true;
            }
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> col) {
        Objects.requireNonNull(col);
        boolean res = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!col.contains(it.next())) {
                it.remove();
                res = true;
            }
        }
        return res;
    }

    @Override
    public boolean contains(Object o) {
        return super.contains((E)o);
    }

    @Override
    public Object[] toArray() {
        Object[] objects = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < objects.length; i++) {
            if (! it.hasNext())
                return Arrays.copyOf(objects, i);
            objects[i] = it.next();
        }
        return objects;
    }

    @Override
    public <T> T[] toArray(T[] t) {
        Iterator<E> it = iterator();
        for (int i = 0; i < t.length; i++) {
            if (! it.hasNext())
                return Arrays.copyOf(t, i);
            t[i] = (T)it.next();
        }
        return t;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new LinkedDequeFullDescendingIterator();
    }
    private class LinkedDequeFullDescendingIterator implements Iterator<E>{
        LinkedDequeSimpleIterator it;

        LinkedDequeFullDescendingIterator() {
            it = new LinkedDequeSimpleIterator();
            while(it.hasNext()) {
                it.next();
            }
        }
        @Override
        public boolean hasNext() {
            return it.hasPrevious();
        }

        @Override
        public E next() {
            return it.previous();
        }

        @Override
        public void remove() {
            it.remove();
        }
    }
}

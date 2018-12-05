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
        return isEmpty() ? null : removeFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return isEmpty() ? null : getFirst();
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
    public boolean containsAll(Collection<?> c) {
        if(c == null){
            throw new NullPointerException();
        }
        Iterator it = c.iterator();
        while(it.hasNext()){
            if(!contains(it.next())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if(a.length == 0){
            return false;
        }
        for(Object e : a){
            addLast((E)e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean contains(Object o) {
        return super.contains((E)o);
    }

    @Override
    public Object[] toArray() {
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        return r;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> it = iterator();
        for (int i = 0; i < a.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(a, i);
            a[i] = (T)it.next();
        }
        return a;
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


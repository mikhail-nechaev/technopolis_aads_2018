package ru.mail.polis.collections.list.todo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
        final Node<E> f = head;

        if(f == null){
            return null;
        } else {
            final Node<E> next = f.next;
            final E value = f.item;
            head = next;

            if(next == null){
                tail = null;
            } else {
                next.prev = null;
            }

            f.next = null;
            f.item = null;

            size--;
            return value;
        }
    }

    @Override
    public E pollLast() {
        final Node<E> l = tail;
        if(l == null){
            return null;
        } else {
            final Node<E> prev = l.prev;
            final E value = l.item;
            tail = prev;

            if(prev == null){
                head = null;
            } else {
                prev.next = null;
            }

            l.prev = null;
            l.item = null;

            size--;
            return value;
        }
    }

    @Override
    public E peekFirst() {
        final Node<E> f = head;
        return (f == null) ? null : f.item;
    }

    @Override
    public E peekLast() {
        final Node<E> t = tail;
        return (t == null) ? null : t.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Objects.requireNonNull(o);
        ListIterator<E> iterator = new LinkedDequeIterator();
        for(; iterator.hasNext();){
            if(o.equals(iterator.next())){
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Objects.requireNonNull(o);
        Iterator<E> iterator = new DescendingIterator();
        for(; iterator.hasNext();){
            if(o.equals(iterator.next())){
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
        Objects.requireNonNull(c);
        int sizeBefore = size();
        c.forEach(this::addLast);
        return size() == sizeBefore + c.size();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return removeIf(c::contains);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return removeIf(e -> !c.contains(e));
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
        Objects.requireNonNull(c);
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return super.contains((E) o);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        List<E> list = new ArrayList<>(size());
        list.addAll(this);
        return list.toArray(a);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    private class DescendingIterator implements Iterator<E>{
        private final ListIterator<E> itr = new LinkedDequeIterator(size);
        public boolean hasNext() {
            return itr.hasPrevious();
        }
        public E next() {
            return itr.previous();
        }
        public void remove() {
            itr.remove();
        }
    }
}

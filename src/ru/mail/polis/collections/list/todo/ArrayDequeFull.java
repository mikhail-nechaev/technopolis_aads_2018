package ru.mail.polis.collections.list.todo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

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
        E e = elements[head];
        if(e != null){
            elements[head] = null;
            head = inc(head);
        }
        return e;
    }

    @Override
    public E pollLast() {
        E e = elements[dec(tail)];
        if(e != null){
            elements[tail = dec(tail)] = null;
        }
        return e;
    }

    @Override
    public E peekFirst() {
        if(isEmpty()) return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if(isEmpty()) return null;
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Objects.requireNonNull(o);
        ListIterator<E> iterator = new ArrayDequeIterator();
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
        ListIterator<E> iterator = new DescendingIterator();
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
        return size() >= sizeBefore;
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
        List<E> list = new ArrayList<>(size());
        list.addAll(this);
        return list.toArray();
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

    private class DescendingIterator implements ListIterator<E> {
        int cursor = dec(tail);
        int remaining = size();
        int nextIndex;
        int lastIndexReturned = -1;

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            E e = elements[cursor];
            cursor = dec(lastIndexReturned = cursor);
            remaining--;
            nextIndex++;
            return e;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if(!hasPrevious()){
                throw new NoSuchElementException();
            }
            E e = elements[cursor = lastIndexReturned];
            lastIndexReturned = (lastIndexReturned == tail) ? -1 : inc(lastIndexReturned);
            remaining++;
            nextIndex--;
            return e;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if(lastIndexReturned < 0){
                throw new IllegalStateException();
            }
            if(!delete(lastIndexReturned)){
                cursor = dec(cursor);
            }
            nextIndex--;
            lastIndexReturned = -1;
        }

        @Override
        public void set(E e) {
            if(lastIndexReturned < 0){
                throw new IllegalStateException();
            }
            elements[lastIndexReturned] = e;
        }

        @Override
        public void add(E e) {
            Objects.requireNonNull(e);
            if(cursor <= tail || cursor == elements.length - 1){
                if(inc(tail) == head){
                    ensureCapacity();
                }
                int numBackElements = getNumElementsBetween(tail = inc(tail), inc(inc(cursor)));
                System.arraycopy(elements, inc(cursor), elements, inc(inc(cursor)), numBackElements);

            } else {
                if(dec(head) == tail){
                    ensureCapacity();
                }
                int numFrontElements = getNumElementsBetween(cursor, head = dec(head));
                System.arraycopy(elements, inc(head), elements, head, numFrontElements);
                cursor = dec(cursor);
            }
            elements[inc(cursor)] = e;
            nextIndex++;
            lastIndexReturned = -1;
        }
    }
}

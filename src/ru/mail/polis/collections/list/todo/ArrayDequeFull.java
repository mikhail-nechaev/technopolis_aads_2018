package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

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
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
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
        if(o != null){
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if(c == null){
            throw new NullPointerException();
        }
        Iterator i = c.iterator();
        while(i.hasNext()){
            if(!contains(i.next())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return super.contains((E)o);
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }
}

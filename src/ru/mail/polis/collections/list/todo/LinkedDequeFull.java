package ru.mail.polis.collections.list.todo;


import java.util.Collection;
import java.util.Deque;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;


public  class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E> {

    public LinkedDequeFull() {
        super();
    }


    @Override
    public void addFirst(E value)  {
        super.addFirst(value);
    }


    @Override
    public E removeFirst() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return super.removeFirst();
    }


    @Override
    public E getFirst() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return super.getFirst();
    }

    @Override
    public void addLast(E value)  {
        super.addLast(value);
    }

    @Override
    public E removeLast() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return super.removeLast();
    }


    @Override
    public E getLast() throws NoSuchElementException{
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return super.getLast();
    }


    @Override
    public boolean contains(Object value) {
        return super.contains((E) value);
    }


    @Override
    public int size() throws NoSuchElementException {

        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }


    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return super.iterator();
    }

    @Override
    public boolean offerFirst(E e){
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
        if(isEmpty()){
            return null;
        }
        return removeFirst();
    }


    @Override
    public E pollLast() {
        if(isEmpty()){
            return null;
        }
        return removeLast();
    }


    @Override
    public E peekFirst() {
        if(isEmpty()){
            return null;
        }
        return getFirst();
    }


    @Override
    public E peekLast() {
        if(isEmpty()){
            return null;
        }
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) throws NullPointerException {
        if(o == null){
            throw new NullPointerException();
        }
        Iterator <E> iterator = iterator();
        while (iterator.hasNext()){
            if(iterator.next() == o){
                iterator.remove();
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean removeLastOccurrence(Object o) throws NullPointerException {
        if(o == null){
            throw new NullPointerException();
        }
        Iterator<E> iterator = descendingIterator();
        while (iterator.hasNext()){
            if(iterator.next() == o){
                iterator.remove();
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean add(E e) throws NullPointerException{
        if(e == null){
            throw new NullPointerException();
        }
        return offerLast(e);
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    protected void removeNode(Node current) throws IllegalStateException {
        super.removeNode(current);
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
    public E element()  {
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
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node index = tail;
            private Node cursor = null;
            @Override
            public boolean hasNext() {
                return index != null;
            }

            @Override
            public E next() throws NoSuchElementException {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                cursor = index;
                index = index.previous;
                return  cursor.value;
            }
            @Override
            public void remove()throws IllegalStateException {
                if(cursor == null){
                    throw new IllegalStateException();
                }
                LinkedDequeFull.this.removeNode(cursor);
                cursor = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arrayDeq = new Object[length];
        return toArray(arrayDeq);
    }


    @Override
    public <T> T[] toArray(T[] a) {
        Node current = head;
        IntStream.range(0, length).forEach(i -> a[i] = (T) current.value);
        return a;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object values : c){
            if(contains(values)){
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean addAll(Collection<? extends E> c) {
        for(E values: c){
            addLast(values);
        }
        return true;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isRemove = false;
        Iterator iterator = iterator();
        while (true){
            if (!iterator.hasNext()) {
                break;
            }
            if(c.contains(iterator.next())){
                iterator.remove();
                isRemove = true;
            }
        }
        return isRemove;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isRemove = false;
        Iterator iterator = iterator();
        while (true){
            if (!iterator.hasNext()) {
                break;
            }
            if(!c.contains(iterator.next())){
                iterator.remove();
                isRemove = true;
            }
        }
        return isRemove;
    }
}

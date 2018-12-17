package ru.mail.polis.collections.list.todo;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {


    public ArrayDequeFull() {
        super();
    }

    public ArrayDequeFull(int numElements) {
        super(numElements);
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
    protected boolean delete(int i) {
        return super.delete(i);
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
        if (o == null) {
            throw new NullPointerException();
        }
        int wall = arrayDeque.length - 1;
        int i = head;
        Object x;
        while (true) {
            if ((x = arrayDeque[i]) == null) {
                break;
            }
            if (o.equals(x)) {
                delete(i);
                return true;
            }
            i = (i + 1) & wall;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) throws NullPointerException {
        if (o == null) {
            throw new NullPointerException();
        }
        int wall = arrayDeque.length - 1;
        int i = (tail - 1) & wall;
        Object x;
        while (true) {
            if ((x = arrayDeque[i]) == null) {
                break;
            }
            if (o.equals(x)) {
                delete(i);
                return true;
            }
            i = (i - 1) & wall;
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
        return new Iterator<E> () {

            private int index = tail;
            private int wall = head;
            private int cursor = -1;

            public boolean hasNext() {
                return index != wall;
            }

            public E next() {
                if (index == wall)
                    throw new NoSuchElementException();
                index = (index - 1) & (arrayDeque.length - 1);
                E result = arrayDeque[index];
                if (head != wall || result == null)
                    throw new ConcurrentModificationException();
                cursor = index;
                return result;
            }

            public void remove() {
                if (cursor < 0)
                    throw new IllegalStateException();
                if (!delete(cursor)) {
                    index = (index + 1) & (arrayDeque.length - 1);
                    wall = head;
                }
                cursor = -1;
            }

        };
    }



    @Override
    public Object[] toArray() {
        Object[] nArray = new Object[size()];
        Iterator<E> it = iterator();
        int i = 0;
        while (i < nArray.length) {
            if (!it.hasNext())
                return Arrays.copyOf(nArray, i);
            nArray[i] = it.next();
            i++;
        }
        return nArray;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> it = iterator();
        int i = 0;
        while (i < a.length) {
            if (!it.hasNext())
                return Arrays.copyOf(a, i);
            a[i] = (T)it.next();
            i++;
        }
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
        for(E values : c ){
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

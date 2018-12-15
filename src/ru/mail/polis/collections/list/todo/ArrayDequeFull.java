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
        if (!isEmpty()){
            return removeFirst();
        } else {
            return null;
        }
    }

    @Override
    public E pollLast() {
        if (!isEmpty()){
            return removeLast();
        } else {
            return null;
        }
    }

    @Override
    public E peekFirst() {
        if (!isEmpty()){
            return getFirst();
        } else {
            return null;
        }
    }

    @Override
    public E peekLast() {
        if (!isEmpty()){
            return getLast();
        } else {
            return null;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null){
            throw new NullPointerException();
        }
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()){
            if (o.equals(iterator.next())){
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null){
            throw new NullPointerException();
        }
        Iterator<E> descendingIterator = descendingIterator();
        while (descendingIterator.hasNext()){
            if (o.equals(descendingIterator.next())){
                descendingIterator.remove();
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
        Iterator<E> iterator = (Iterator<E>) c.iterator();
        while (iterator.hasNext()){
            this.addLast(iterator.next());
        }
        return true;
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
    public boolean contains(Object o) {
        return super.contains((E)o);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new ArrayDequeDescendingIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        Iterator<E> iterator = iterator();
        for (int i = 0; i < result.length; i++) {
            if (iterator.hasNext()){
                result[i] = iterator.next();
            }
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size()){
            a = (T[])new Object[size()];
        }
        Iterator<E> iterator = iterator();
        for (int i = 0; i < size(); i++) {
            if (iterator.hasNext()){
                a[i] = (T) iterator.next();
            }
        }
        for (int i = size(); i < a.length; i++){
            a[i] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.isEmpty() || c.contains(null)){
            throw new NullPointerException();
        }
        Iterator<E> iterator = (Iterator<E>) c.iterator();
        while (iterator.hasNext()){
            if (!contains(iterator.next())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;
        if (c.isEmpty() || c.contains(null)){
            throw new NullPointerException();
        }
        Iterator<E> iterator = (Iterator<E>) c.iterator();
        while (iterator.hasNext()){
            E forRemove = iterator.next();
            while (contains(forRemove)){
                remove(forRemove);
                isModified = true;
            }
        }
        return isModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isModified = false;
        if (c.isEmpty() || c.contains(null)){
            throw new NullPointerException();
        }

        for (E forRemove : this) {
            if (!c.contains(forRemove)) {
                while (contains(forRemove)) {
                    remove(forRemove);
                    isModified = true;
                }
            }
        }
        return isModified;
    }

    private class ArrayDequeDescendingIterator implements Iterator<E> {

        private int i = N;
        private int target = tail;
        private int previousTarget;

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public E next() {
            if (hasNext()) {
                previousTarget = target;
                Object value = deque[target--];

                i--;

                if (target == -1) {
                    target = deque.length - 1;
                }

                return (E) value;
            } else {
                throw new NoSuchElementException("Deque is empty");
            }
        }

        @Override
        public void remove() {

            if (N == 0 || target == tail) {
                throw new IllegalStateException();
            }

            int targetInd = previousTarget;

            if (targetInd > tail){
                int j;
                for (j = targetInd; j > head; j--){
                    deque[j] = deque[j - 1];
                }
                head = head == deque.length - 1 ? 0 : head + 1;
                target++;
            } else {
                int j;
                for (j = targetInd; j < tail; j++) {
                    deque[j] = deque[j + 1];
                }
                tail = tail == 0 ? deque.length - 1 : tail - 1;
            }

            N--;

        }
    }
}

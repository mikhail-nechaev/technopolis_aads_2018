package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedDequeSimple<E> implements IDeque<E> {

    class Node<E> {

        E value;
        Node<E> prev;
        Node<E> next;

        Node(E initValue) {
            this.value = initValue;
        }
    }

    Node<E> first;
    Node<E> last;
    int length;

    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node<>(value);
        if (first == null && last == null) {
            first = last = node;
        } else if (first == last) {
            node.next = last;
            last.prev = node;
            first = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }
        length++;
    }

    @Override
    public E removeFirst() {
        if (first == null && last == null) {
            throw new NoSuchElementException();
        }
        E first = this.first.value;
        if (this.first == last) {
            this.first = null;
            last = null;
        } else {
            this.first = this.first.next;
            this.first.prev = null;
        }
        length--;
        return first;
    }

    @Override
    public E getFirst() {
        if (first == null && last == null) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    @Override
    public void addLast(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node<>(value);
        if (first == null && last == null) {
            first = last = node;
        } else if (first == last) {
            node.prev = first;
            first.next = node;
            last = node;
        } else {
            node.prev = last;
            last.next = node;
            last = node;
        }
        length++;
    }

    @Override
    public E removeLast() {
        if (first == null && last == null) {
            throw new NoSuchElementException();
        }
        E last = this.last.value;
        if (first == this.last) {
            first = null;
            this.last = null;
        } else {
            this.last = this.last.prev;
            this.last.next = null;
        }
        length--;
        return last;
    }

    @Override
    public E getLast() {
        if (first == null && last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
    }

    @Override
    public boolean contains(E value) {
        Iterator<E> iter = this.iterator();
        while (iter.hasNext()) {
            E check = iter.next();
            if (check.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public void clear() {
        first = last = null;
        length = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator<>();
    }

    private class LinkedIterator<T extends E> implements Iterator<E> {

        private Node<E> lastReturned;
        private Node<E> nextElem = first;

        @Override
        public boolean hasNext() {
            return nextElem != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = nextElem;
            nextElem = nextElem.next;
            return lastReturned.value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            if (lastReturned == last) {
                removeLast();
                lastReturned = null;
            } else if (lastReturned == first) {
                removeFirst();
                lastReturned = null;
            } else {
                lastReturned.prev.next = lastReturned.next;
                lastReturned.next.prev = lastReturned.prev;
                length--;
            }
        }
    }
}
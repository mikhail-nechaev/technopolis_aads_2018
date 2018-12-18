package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;


import java.util.NoSuchElementException;
import java.util.Iterator;



@SuppressWarnings("unchecked")
public class LinkedDequeSimple<E> implements IDeque<E> {

    Node head;
    Node tail;
    int length;

    public LinkedDequeSimple() {
        tail = null;
        head = null;
        length = 0;
    }

    class Node {
        E value;
        Node previous;
        Node next;

        Node(E value) {
            this.value = value;
        }
    }

    protected void removeNode(Node current) throws IllegalStateException {
        if(current == head){
            removeFirst();
            return;
        }
        if(current == tail){
            removeLast();
            return;
        }
        if (current.previous == null || current.next == null){
            throw new IllegalStateException();
        }
        current.previous.next = current.next;
        current.next.previous = current.previous;
        length --;
    }



    @Override
    public void addFirst(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        Node newFirst = new Node(value);

        if (head != null) {
            newFirst.next = head;
            head.previous = newFirst;
        }
        head = newFirst;
        if (tail == null) tail = head;
        length++;
    }


    @Override
    public E removeFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        E value = head.value;
        head = head.next;
        if (length == 1) {
            head = tail = null;
        }
        if (head == null)
            tail = null;

        else
            head.previous = null;
        length--;
        return value;
    }


    @Override
    public E getFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        return head.value;
    }

    @Override
    public void addLast(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        Node newLast = new Node(value);

        if (tail != null) {
            newLast.previous = tail;
            tail.next = newLast;
        }
        tail = newLast;
        if (head == null) head = tail;
        length++;
    }


    @Override
    public E removeLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        E value = tail.value;
        tail = tail.previous;
        if (tail == null)
            head = null;
        else
            tail.next = null;
        length--;
        return value;
    }


    @Override
    public E getLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        return tail.value;
    }


    @Override
    public boolean contains(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        Node current = head;
        while (current != null) {
            if (current.value == value) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return length;
    }


    @Override
    public boolean isEmpty() {
        return tail == null && head == null;
    }


    @Override
    public void clear() {
        head = null;
        tail = null;
        length = 0;
    }

    @Override
    public Iterator<E> iterator() {

        return new Iterator<>() {
            private Node index = head;
            private Node cursor = null;



            @Override
            public E next() throws NoSuchElementException {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                cursor = index;
                index = index.next;
                return cursor.value;
            }


            @Override
            public void remove() throws IllegalStateException {

                if(cursor == null){
                    throw new IllegalStateException();
                }
                LinkedDequeSimple.this.removeNode(cursor);
            }


            @Override
            public boolean hasNext() {
                return index != null;
            }
        };
    }
}
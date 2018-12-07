package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    private class Node {
        E value;
        Node previous;
        Node next;

        Node(E value){
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int length;

    LinkedDequeSimple(){
        tail = null;
        head = null;
        length = 0;
    }

    void RemoveNode(Node curr){
        curr.previous.next = curr.next;
        curr.next.previous = curr.previous;
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst (E value) throws NullPointerException {
        if(value == null){
            throw  new NullPointerException();
        }
        Node newFirst = new Node(value);

        if (head != null) {
            newFirst.next = head;
            head.previous = newFirst;
        }
        head = newFirst;
        if (tail == null) tail = head;
        length ++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() throws NoSuchElementException {
        if(length == 0){
            throw new NoSuchElementException();
        }
        E value = head.value;
        head = head.next;
        if(length == 1){
            head = tail = null;
        }
        if(head == null)
            tail = null;

        else
            head.previous = null;
        length --;
        return value;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() throws NoSuchElementException {
        if(length == 0){
            throw  new NoSuchElementException();
        }
        return head.value;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) throws NullPointerException {
        if(value == null){
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

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() throws NoSuchElementException {
        if(length == 0){
            throw  new NoSuchElementException();
        }
        E value = tail.value;
        tail = tail.previous;
        if(tail == null)
            head = null;
        else
            tail.next = null;
        length--;
        return value;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() throws NoSuchElementException{
        if(length == 0){
            throw  new NoSuchElementException();
        }
        return tail.value;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(Object value) throws NullPointerException{
        if((E)value == null){
            throw new NullPointerException();
        }
        Node current = head;
        while(current != tail){
            current = current.next;
            if(current.value == value){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public ListIterator<E> iterator() {
             return new ListIterator<E>() {
                 private Node current = head;


                 int index = -1;
/*
                 private void increment(){
                     head++;
                     if (head == size) {

                         head = 0;
                     }
                 }*/

                 @Override
                 public boolean hasNext() {
                     return current != null;
                 }

                 @Override
                 public E next() throws NoSuchElementException, NullPointerException{
                     if(current == null){
                         throw new NullPointerException();
                     }
                     if(!hasNext()) {
                         throw  new NoSuchElementException();

                     }
                     current = current.next;
                     return current.value;
                 }

                 @Override
                 public boolean hasPrevious() {
                     return false;
                 }

                 @Override
                 public E previous() throws NoSuchElementException, NullPointerException{
                     if(current == null){
                         throw new NullPointerException();
                     }
                     if(!hasPrevious()) {
                         throw  new NoSuchElementException();

                     }
                     current = current.previous;
                     return current.value;
                 }

                 @Override
                 public int nextIndex() {
                     return index + 1;
                 }

                 @Override
                 public int previousIndex() {
                     return 0;
                 }

                 @Override
                 public void remove() { //?
                     Node tmp = current.next;
                     Node tmp2 = current.previous;

                     current.previous.next = tmp;
                     current.next.previous = tmp2;
                 }

                 @Override
                 public void set(E e) {
                     current.value = e;
                 }

                 @Override
                 public void add(E e) {
                    addLast(e);
                 }
             };
    }
}

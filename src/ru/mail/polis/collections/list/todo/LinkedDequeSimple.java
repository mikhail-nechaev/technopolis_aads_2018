package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    class DequeNode<E>{
        DequeNode(E data, DequeNode<E> prev, DequeNode<E> next){
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        @Override
        public String toString(){
            return data.toString();
        }

        DequeNode<E> next, prev;
        E data;
    }

    DequeNode<E> head;
    DequeNode<E> tail;
    int size;

    public LinkedDequeSimple(){
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null){
            throw new NullPointerException();
        }
        DequeNode<E> dn = new DequeNode<>(value, head, null);
        if (head == null){
            head = dn;
        } else {
            head.next = dn;
            head = dn;
        }
        if (tail == null){
            tail = dn;
        }
        size ++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        size --;
        if (head != null){
            E data = head.data;
            if (size == 0){
                head = null;
                tail = null;
                return data;
            }
            head = head.prev;
            return data;
        } else {
            return null;
        }
    }


    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        size --;
        if (tail != null){
            E data = tail.data;
            if (size == 0){
                head = null;
                tail = null;
                return data;
            }
            tail = tail.next;
            return data;
        } else {
            return null;
        }
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        if (head !=null){
            return head.data;
        } else if (tail != null){
            return tail.data;
        }
        return null;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null){
            throw new NullPointerException();
        }
        size ++;
        DequeNode<E> dn = new DequeNode<>(value, null,tail);
        if (tail == null){
            tail = dn;
        } else {
            tail.prev = dn;
            tail = dn;
        }
        if (head == null){
            head = dn;
        }
    }


    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        if (tail != null){
            return tail.data;
        } else if (head != null) {
            return head.data;
        }
        return null;
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
    public boolean contains(E value) {
        if (value == null){
            throw new NullPointerException();
        }
        if (head == null){
            if (tail != null){
                return Objects.equals(tail.data, value);
            } else {
                return false;
            }

        }
        DequeNode<E> node = head;
        while (node != null) {
            if ( Objects.equals(node.data,value)){
                return true;
            }
            node = node.prev;
        };
        return false;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            DequeNode<E> lastReturned;
            DequeNode<E> current = head;
            @Override
            public boolean hasNext() {
                if (current == null){
                    return false;
                }
                if (current != lastReturned || current.prev != null){
                    return true;
                }
                return false;
            }

            @Override
            public E next() {
                if (current == null){
                    throw new NoSuchElementException();
                }
                DequeNode<E> temp = current;
                current = current.prev;
                lastReturned = temp;
                return temp.data;
            }
            @Override
            public void remove(){
                if (lastReturned == null){
                    throw new IllegalStateException();
                }
                DequeNode<E> prev = lastReturned.prev;
                DequeNode<E> next = lastReturned.next;
                if (prev != null){
                    prev.next = next;
                }
                if (next != null){
                    next.prev = prev;
                }
                lastReturned = null;
                size --;
            }
        };
    }
}

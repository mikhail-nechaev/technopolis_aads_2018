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

    int n;
    Node first;
    Node last;

    class Node {
        E e = null;

        public Node(E e) {
            this.e = e;
        }

        Node next;
        Node prev;
    }
    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        Objects.requireNonNull(value);
        if (isEmpty()) {
            first = new Node(value);
            last=first;
        } else {
            first.next = new Node(value);
            first.next.prev=first;
            first=first.next;
        }
        n++;

    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        E item = first.e;

        if (n == 1) {
            first = null;
            last = null;
        } else {
            first = first.prev;
            first.next.prev = null;
            first.next = null;
        }
        n--;
        return item;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return first.e;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        Objects.requireNonNull(value);
        if (isEmpty()) {
            first = new Node(value);
            last=first;
        } else {
            last.prev = new Node(value);
            last.prev.next = last;
            last = last.prev;
        }
        n++;

    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        E item = last.e;
        if (n == 1) {
            first = null;
            last = null;
        } else {
            last = last.next;
            last.prev.next = null;
            last.prev = null;
        }
        n--;


        return item;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return last.e;
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
        Objects.requireNonNull(value);
        if (isEmpty()) {
            return false;
        }
        Node place = last;
        while (place!= null){

            if (place.e.equals(value)) {
                return true;
            }
            place = place.next;
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
        return n;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear()  {
        n = 0;
        first = null;
        last = null;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        Iterator<E> iterator=new Iterator<E>() {
            boolean check = false;
            Node pointer = first;
            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                check = false;
                return pointer != null;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E out = pointer.e;
                pointer = pointer.prev;
                check = true;
                return out;
            }

            /**
             * Removes from the underlying collection the last element returned
             * by this iterator (optional operation).  This method can be called
             * only once per call to {@link #next}.
             * <p>
             * The behavior of an iterator is unspecified if the underlying collection
             * is modified while the iteration is in progress in any way other than by
             * calling this method, unless an overriding class has specified a
             * concurrent modification policy.
             * <p>
             * The behavior of an iterator is unspecified if this method is called
             * after a call to the {@link #forEachRemaining forEachRemaining} method.
             *
             * @throws UnsupportedOperationException if the {@code remove}
             *                                       operation is not supported by this iterator
             * @throws IllegalStateException         if the {@code next} method has not
             *                                       yet been called, or the {@code remove} method has already
             *                                       been called after the last call to the {@code next}
             *                                       method
             * @implSpec The default implementation throws an instance of
             * {@link UnsupportedOperationException} and performs no other action.
             */
            @Override
            public void remove() {
                if (!check) throw new IllegalStateException();
                if (pointer==null) {
                    first = null;
                    last = null;
                    n--;
                }else {
                    if (pointer.next.next != null) {
                        pointer.next.next.prev = pointer;
                    }
                    if (pointer != null) {
                        pointer.next = pointer.next.next;

                    }
                    n--;
                    if (n==0){
                        clear();
                    }
                }
                check=false;


            }
        };
        return iterator;
    }
}

package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    class Node {
        Node prev;
        Node next;
        final E value;

        public Node(E value) {
            this.value = value;
        }
    }
    Node head=null;
    Node tail=null;
    int count=0;

    public LinkedDequeSimple() {
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        checkNullPointer(value);
        if (count==0) {
            tail=new Node(value);
            head=tail;
            count++;
        }else{
            head.next=new Node(value);
            head.next.prev=head;
            head=head.next;
            count++;
        }
    }

    protected void checkNullPointer(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }


    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        E result=head.value;
        if (count==1){
            head=null;
            tail=null;
        }else{
            head=head.prev;
            head.next.prev=null;
            head.next=null;
        }
        count--;
        return result;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
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
    public void addLast(E value) {
        checkNullPointer((E) value);
        if (count==0){
            tail=new Node(value);
            head=tail;
            count++;
        }else {
            tail.prev=new Node(value);
            tail.prev.next=tail;
            tail=tail.prev;
            count++;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E result=tail.value;
        if (count==1){
            tail=null;
            head=null;
        }else{
            tail=tail.next;
            tail.prev.next=null;
            tail.prev=null;
        }

        count--;
        return result;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
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
    public boolean contains(E value) {
        checkNullPointer(value);
        Node iterator=tail;
        while(iterator!=null){
            if (value.equals(iterator.value)) {
                return true;
            }
            iterator=iterator.next;
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
        return count;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return count==0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        tail=null;
        head=null;
        count=0;
    }
    protected void removeNode(Node currentNode){
        if (currentNode.next!=null){
            currentNode.next.prev=currentNode.prev;
        }
        if (currentNode.prev!=null){
            currentNode.prev.next=currentNode.next;
        }
        count--;
        if (count==0) {
            clear();
        }
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        Iterator<E> iterator = new Iterator<E>() {
            Node currentNode =head;
            boolean canRemove=false;
            @Override
            public boolean hasNext() {
                canRemove=false;
                return currentNode !=null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E result= currentNode.value;
                currentNode = currentNode.prev;
                canRemove=true;
                return result;
            }

            @Override
            public void remove() {
                if (!canRemove){
                    throw new IllegalStateException();
                }
                if (currentNode==null){
                    count--;
                }else {
                    removeNode(currentNode.next);
                }
                canRemove=false;

            }
        };
        return iterator;
    }
}

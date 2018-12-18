package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {
    private E[] deque;
    private int head;
    private int tail;
    private int dequeSize;
    private int modCount = 0;

    public ArrayDequeSimple(){
        dequeSize = 10;
        deque = (E[])new Object[dequeSize];
        head = 0;
        tail = 1;
    }

    private void resize(){
        int newMaxSize = dequeSize * 2;
        E[] newDeque = (E[]) new Object[newMaxSize];
        int i;
        if(head < tail) {
            for(i = 1; head + i < tail; i++){
                newDeque[i] = deque[head + i];
            }
        }
        else{
            for(i = 1; head + i < dequeSize; i++){
                newDeque[i] = deque[head + i];
            }
            for(int j = 0; j < tail; j++){
                newDeque[i++] = deque[j];
            }
        }
        head = 0;
        tail = i;
        dequeSize = newMaxSize;
        deque = newDeque;
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if(value == null) {
            throw new NullPointerException();
        }
        if(head == 0){
            if(dequeSize - 1 == tail){
                resize();
            }
            deque[head] = value;
            head = dequeSize - 1;
        }
        else{
            if(head - 1 == tail){
                resize();
                deque[head] = value;
                head = dequeSize - 1;
            }
            else{
                deque[head--] = value;
            }
        }
        modCount++;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        head = (head + 1) % dequeSize;
        modCount++;
        return deque[head];
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return deque[(head + 1)% dequeSize];
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if(value == null) {
            throw new NullPointerException();
        }
        if(((tail + 1)% dequeSize) == head){
            resize();
        }
        deque[tail] = value;
        tail = (tail + 1)% dequeSize;
        modCount++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        if(tail == 0){
            tail = dequeSize - 1;
        }
        else{
            tail--;
        }
        modCount++;
        return deque[tail];
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        if (tail == 0)
            return deque[dequeSize -1];
        return deque[tail - 1];
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
        if(value == null){
            throw new NullPointerException();
        }
        if(isEmpty()){
            return false;
        }
        if(head < tail) {
            for(int i = 1; head + i < tail; i++){
                if(deque[head + i].equals(value)){
                    return true;
                }
            }
        }
        else{
            for(int i = 1; head + i < dequeSize; i++){
                if(deque[head + i].equals(value)){
                    return true;
                }
            }
            for(int j = 0; j < tail; j++){
                if(deque[j].equals(value)){
                    return true;
                }
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
        if(head < tail){
            return tail - head - 1;
        }
        else{
            return tail + dequeSize - 1 - head;
        }
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        head = 0;
        tail = 1;
        modCount++;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeSimpleIterator();
    }
    protected class ArrayDequeSimpleIterator implements Iterator<E> {
        private E lastReturned;
        private E next;
        private int nextIndex;
        private int expectedModCount = modCount;

        ArrayDequeSimpleIterator(){
            nextIndex = (head + 1) % dequeSize;
            if (size() == 0)
                next = null;
            else
                next = deque[nextIndex];
        }
        @Override
        public boolean hasNext() {
            return nextIndex != tail;
        }

        @Override
        public E next() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            nextIndex = (nextIndex + 1) % dequeSize;
            next = deque[nextIndex];
            return lastReturned;
        }

        public boolean hasPrevious() {
            return nextIndex != (head + 1)% dequeSize;
        }

        public E previous() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (!hasPrevious())
                throw new NoSuchElementException();
            if(nextIndex == 0)
                nextIndex = dequeSize - 1;
            else
                nextIndex = nextIndex - 1;
            lastReturned = next = deque[nextIndex];
            return lastReturned;
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (lastReturned == null)
                throw new IllegalStateException();
            E [] newDeque = (E[])new Object[dequeSize];
            int removedIndex;
            if (next == lastReturned)
                removedIndex = nextIndex;
            else if(nextIndex == 0)
                removedIndex = dequeSize - 1;
            else
                removedIndex = (nextIndex - 1);
            if(head < tail){
                int j = 0;
                for(int i = 0; i < dequeSize; i++){
                    if(i != removedIndex)
                        newDeque[j++] = deque[i];
                }
            }
            else{
                int j = head;
                for(int i = head; i < dequeSize; i++){
                    if(i != removedIndex) {
                        newDeque[j % dequeSize] = deque[i];
                        j++;
                    }
                }
                for(int i = 0; i < tail; i++){
                    if(i != removedIndex){
                        newDeque[j% dequeSize] = deque[i];
                        j++;
                    }
                }
            }
            if(tail == 0)
                tail = dequeSize - 1;
            else
                tail--;
            deque = newDeque;
            nextIndex = removedIndex;
            next = deque[nextIndex];
            lastReturned = null;
            modCount++;
            expectedModCount++;
        }
    }
}
package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */

@SuppressWarnings("unchecked")
public class ArrayDequeSimple<E> implements IDeque<E> {
    protected static final int DEFAULT_CAPACITY = 100;
    private final int startCapacity;
    /**
     * Isn`t fixed, expandable with widen method, reducable with
     * reduce method
     */
    protected E[] dequeue; // Array for the deque
    /**
     * next two are indexes as pointers
     */
    protected int head = 0;
    protected int tail = -1;
    protected int size;

    /**
     * Constructor of an empty Dequeue
     */
    public ArrayDequeSimple() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor by amount of elements
     */
    public ArrayDequeSimple(int amount) {
        dequeue = (E[]) new Object[amount];
        this.startCapacity = amount;
        size = 0;
    }

    /**
     * Constructor by collection
     */
    public ArrayDequeSimple(Collection<E> collection) {
        this(collection.size());
        for (E item : collection) {
            addFirst(item);
        }
    }

    /**
     * Resize method is called when head == tail
     */
    private void widen() {
        E[] newDequeue = (E[]) new Object[dequeue.length * 2 + 1];
        int l,r; //pointers on left and right parts of newDequeue in oldDequeue
        if (head < tail) {
            r = dequeue.length - head;
            l = head;
        }
        else {
            l = dequeue.length - head;
            r = dequeue.length - tail;
        }
        System.arraycopy(dequeue, l, newDequeue, 0, head);
        System.arraycopy(dequeue, r, newDequeue, head + 1, r - 1);
        head = 0;
        tail = size - 1;
    }

    private void reduce() {
        if (dequeue.length > startCapacity) {
            E[] newDequeue = (E[]) new Object[dequeue.length / 2];
            int r, l;
            if (head < tail) {
                r = dequeue.length - head;
                l = head;
            }
            else {
                l = dequeue.length - head;
                r = dequeue.length - tail;
            }
            System.arraycopy(dequeue, l, newDequeue, 0, head);
            System.arraycopy(dequeue, r, newDequeue, head + 1, r - 1);
            head = 0;
            tail = size - 1;
        }
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }
        if (isEmpty()) {
            head = tail = 0;
            dequeue[head] = value;
            size++;
            return;
        }

        head--;
        if (head == -1) {
            head = dequeue.length - 1;
        }
        dequeue[head] = value;
        size++;

        if (size == dequeue.length) {
            widen();
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
            throw new NoSuchElementException("No elements in dequeue");
        }
        E value = dequeue[head];
        dequeue[head] = null;
        head = ++head % dequeue.length;
        size--;
        if (size < dequeue.length / 2) {
            reduce();
        }
        return value;
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
            throw new NoSuchElementException("No elements in dequeue");
        }
        return dequeue[head];
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null) {
            throw new NullPointerException("Value can not be null");
        }
        if (isEmpty()) {
            head = tail = 0;
            dequeue[tail] = value;
            size++;
            return;
        }
        tail = ++tail % dequeue.length;
        dequeue[tail] = value;
        size++;
        if (size == dequeue.length) {
            widen();
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
            throw new NoSuchElementException("No elements in dequeue");
        }
        E value = dequeue[tail];
        dequeue[tail] = null;
        tail--;
        if (tail == -1) {
            tail = dequeue.length - 1;
        }
        size--;
        if (size < dequeue.length / 2) {
            reduce();
        }
        return value;
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
            throw new NoSuchElementException("No elements in dequeue");
        }
        return dequeue[tail];
    }

    //Removes elem specified by index and connects splitted parts of dequeue in one
    protected boolean removeByIndex(int index) {
        E[] dequeue = this.dequeue;
        int h = head;
        int t = tail;
        int present = dequeue.length - 1;
        int offset = (index - h) & present;
        int finish = (tail - index) & present;

        if (offset < finish) {
            if (h <= index) {
                System.arraycopy(dequeue, h, dequeue, h + 1, offset);
            }
            else {
                System.arraycopy(dequeue, 0, dequeue, 1, index);
                dequeue[0] = dequeue[present];
                System.arraycopy(dequeue, h, dequeue, h + 1, present - h);
            }
            dequeue[h] = null;
            head = (h + 1) & present;
            return false;
        }
        else {
            if (index < t) {
                System.arraycopy(dequeue, index + 1, dequeue, index, finish);
                tail = t - 1;
            }
            else {
                System.arraycopy(dequeue, index + 1, dequeue, index, present - index);
                dequeue[present] = dequeue[0];
                System.arraycopy(dequeue, 1, dequeue, 0, t);
                tail = (t - 1) & present;
            }
            return true;
        }
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
        if (value == null) {
            throw new NullPointerException("Search for a null element");
        }
        for (int i = 0, index = head; i < size; i++, index = ++index % dequeue.length) {
            if (dequeue[index].equals(value)) {
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
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public ListIterator<E> iterator() {
        return new DequeueIterator();
    }

    private class DequeueIterator implements ListIterator<E> {
        //iterator is located between current and future and points on current in the moment which is located behind iterator
        int current, future, size;

        private int next = 0;

        public DequeueIterator() {
            current = head;
            future = head + 1;
            size = size();
        }

        @Override
        public boolean hasNext() {
            return (next < size);
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E res = (E) dequeue[current];
            current = ++current % dequeue.length;
            next++;
            return res;
        }

        @Override
        public boolean hasPrevious() {
            return (current > head);
        }

        @Override
        public E previous() {
            if (hasPrevious()) {
                E res = (E) dequeue[current];
                current++;
                return res;
            } else {
                throw new UnsupportedOperationException("No element");
            }
        }

        @Override
        public int nextIndex() {
            if (hasNext()) {
                current++;
                return current;
            } else {
                throw new UnsupportedOperationException("No element");
            }
        }

        @Override
        public int previousIndex() {
            if (hasPrevious()) {
                current--;
                return current;
            } else {
                throw new UnsupportedOperationException("No element");
            }
        }

        @Override
        public void remove() {
            if (!hasNext()) throw new IllegalStateException("No elements in dequeue");
            if (isEmpty()) throw new NoSuchElementException();
            removeByIndex(current);
        }

        @Override
        public void set(E e) {
            //TODO
        }

        @Override
        public void add(E e) {
            if (isEmpty()) {
                throw new UnsupportedOperationException("No elements in dequeue");
            }
            //TODO
        }
    }
}

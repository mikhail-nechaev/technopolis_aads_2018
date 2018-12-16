package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.awt.Container;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {
    protected Object[] array;
    protected int size;
    protected int capacity = 1;
    protected int head, tail;

    public ArrayDequeSimple() {
        this.array = new Object[capacity];
        this.size = 0;
        this.head = this.tail = -1;
    }

    private void resize() {
        Object[] newArray = new Object[capacity << 1];
        if (head <= tail) {
            System.arraycopy(array, head, newArray, 0, size);
        } else {
            System.arraycopy(array, head, newArray, 0, size - head);
            System.arraycopy(array, 0, newArray, size - head, tail + 1);
        }
        array = newArray;
        capacity <<= 1;
        head = 0;
        tail = size - 1;
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
            throw new NullPointerException();
        }
        if (size == capacity) {
            resize();
        }
        if (isEmpty()) {
            head = tail = 0;
        } else {
            head = (head + capacity - 1) % capacity;
        }
        array[head] = value;
        size++;
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
            throw new NoSuchElementException();
        }
        Object value = array[head];
        if (head == tail) {
            head = tail = -1;
        } else {
            head = (head + 1) % capacity;
        }
        size--;
        return (E) value;
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
            throw new NoSuchElementException();
        }
        return (E) array[head];
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
            throw new NullPointerException();
        }
        if (capacity == size) {
            resize();
        }
        if (isEmpty()) {
            head = tail = 0;
        } else {
            tail = (tail + 1) % capacity;
        }
        array[tail] = value;
        size++;
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
        Object value = array[tail];
        if (head == tail) {
            head = tail = -1;
        } else {
            tail = (tail + capacity - 1) % capacity;
        }
        size--;
        return (E) value;
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
        return (E) array[tail];
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
            throw new NullPointerException();
        }
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            if (value.equals(iter.next())) {
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
        head = tail = -1;
        size = 0;
    }

    protected void removeElement(int index) {
        if (index == head) {
            removeFirst();
            return;
        }
        if (index == tail) {
            removeLast();
            return;
        }
        if (head <= tail) {
            if (index < head || index > tail) {
                throw new IllegalArgumentException();
            }
            System.arraycopy(array, index + 1, array, index, tail - index);
            tail--;
        } else {
            if (index > head) {
                System.arraycopy(array, head, array, head + 1, index - head);
                head = (head + 1) % capacity;
            } else if (index < tail) {
                System.arraycopy(array, index + 1, array, index, tail - index);
                tail--;
            } else {
                throw new NoSuchElementException();
            }
        }
        size--;
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
            private int head = ArrayDequeSimple.this.head;
            private int nextIndex = 0;
            private int lastNextIndex = -1;
            private int size = size();

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastNextIndex = nextIndex++;
                return (E) array[(lastNextIndex + head) % capacity];
            }

            @Override
            public void remove() {
                if (lastNextIndex == -1) {
                    throw new IllegalStateException();
                }
                removeElement((lastNextIndex + head) % capacity);
                lastNextIndex = -1;
            }
        };
    }
}

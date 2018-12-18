package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.*;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    Object[] elements;
    int start, end;
    int size;
    int capasity = INITIAL_CAPASITY;

    final static int INITIAL_CAPASITY = 8;

    public ArrayDequeSimple() {
        this.elements = new Object[INITIAL_CAPASITY];
        this.size = 0;
        this.start = this.end = -1;
    }

    private void extension() {
        Object[] newElements = new Object[capasity << 1];
        if (start <= end) {
            System.arraycopy(elements, start, newElements, 0, size);
        } else {
            System.arraycopy(elements, start, newElements, 0, size - start);
            System.arraycopy(elements, 0, newElements, size - start, end + 1);
        }
        elements = newElements;
        capasity <<= 1;
        start = 0;
        end = size - 1;
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) throw new NullPointerException();
        if (size == capasity) extension();
        if (isEmpty()) {
            start = end = 0;
        } else start = (start + capasity - 1) % capasity;
        elements[start] = value;
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
        if (isEmpty()) throw new NoSuchElementException();
        E first = (E) elements[start];
        if (start == end) {
            start = end = -1;
        } else start = (start + 1) % capasity;
        size--;
        return first;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        return (E) elements[start];
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null) throw new NullPointerException();
        if (size == capasity) extension();
        if (isEmpty()) {
            start = end = 0;
        } else end = (end + 1) % capasity;
        elements[end] = value;
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
        if (isEmpty()) throw new NoSuchElementException();
        E last = (E) elements[end];
        if (start == end) {
            start = end = -1;
        } else end = (end + capasity - 1) % capasity;
        size--;
        return last;
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
        return (E) elements[end];
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
        if (value == null) throw new NullPointerException();
        if (this.isEmpty()) return false;
        for (Object o : elements) {
            if (Objects.equals(o, value)) return true;
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
        start = end = -1;
        size = 0;
    }

    void removeElement(int index) {
        if (index == start) {
            removeFirst();
            return;
        }
        if (index == end) {
            removeLast();
            return;
        }
        if (start <= end) {
            if (index < start || index > end) {
                throw new IllegalArgumentException();
            }
            System.arraycopy(elements, index + 1, elements, index, end - index);
            end--;
        } else {
            if (index > start) {
                System.arraycopy(elements, start, elements, start + 1, index - start);
                start = (start + 1) % capasity;
            } else if (index < end) {
                System.arraycopy(elements, index + 1, elements, index, end - index);
                end--;
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
            private int head = ArrayDequeSimple.this.start;
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
                return (E) elements[(lastNextIndex + head) % capasity];
            }

            @Override
            public void remove() {
                if (lastNextIndex == -1) {
                    throw new IllegalStateException();
                }
                removeElement((lastNextIndex + head) % capasity);
                lastNextIndex = -1;
            }
        };
    }
}

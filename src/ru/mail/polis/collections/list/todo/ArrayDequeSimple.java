package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    private static final int DEFAULT_LENGTH = 10;

    protected Object[] data;
    protected int first;
    protected int last;
    private int count;

    public ArrayDequeSimple() {
        this.data = new Object[DEFAULT_LENGTH];
        this.first = 0;
        this.last = data.length;
        this.count = 0;
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
        if (size() == 0) {
            first = 0;
            last = data.length - 1;
        }
        if (size() + 1 == data.length) {
            resize();
        }
        data[first] = value;
        if (first + 1 == data.length) {
            first = 0;
        } else {
            first++;
        }
        count++;
    }

    private void resize() {
        Object[] newArray = new Object[data.length * 2];
        int last = this.last;
        int i = 0;
        while (i < count) {
            if (last + 1 == data.length) {
                last = 0;
            } else {
                last++;
            }
            newArray[i++] = data[last];
        }
        this.first = i - 1;
        this.last = newArray.length - 1;
        this.data = newArray;
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
        if (first - 1 < 0) {
            first = data.length - 1;
        } else {
            first--;
        }
        count--;
        return (E) data[first];
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
        int index = first;
        if (index - 1 < 0) {
            index = data.length - 1;
        } else {
            index--;
        }
        return (E) data[index];
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
        if (count == 0) {
            first = 0;
            last = data.length - 1;
        }
        if (count + 1 == data.length) {
            resize();
        }
        data[last] = value;
        if (last - 1 < 0) {
            last = data.length - 1;
        } else {
            last--;
        }
        count++;
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
            throw new java.util.NoSuchElementException();
        }
        if (last + 1 == data.length) {
            last = 0;
        } else {
            last++;
        }
        count--;
        return (E) data[last];
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
            throw new java.util.NoSuchElementException();
        }
        int index = last;
        if (index + 1 == data.length) {
            index = 0;
        } else {
            index++;
        }
        return (E) data[index];
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
        if (isEmpty()) {
            return false;
        }
        for (Object o : this) {
            if (value.equals(o)) {
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
        return count;
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
        count = 0;
        first = 0;
        last = data.length - 1;
        data = new Object[data.length];
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int head = first;
            int size = count;
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                canRemove = false;
                return size != 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                head = head - 1 < 0 ? data.length - 1 : head - 1;
                size--;
                canRemove = true;
                return (E) data[head];
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                ArrayDequeSimple.this.remove(size);
                if (head > last && head < data.length) {
                    if (head + 1 == data.length) {
                        head = 0;
                    } else {
                        head++;
                    }
                }
                canRemove = false;
            }
        };
    }

    protected void remove(int index) {
        if (data.length - (last + 1) - index > 0) {
            shiftRight(last + 1 + index);
        } else {
            shiftLeft(-(data.length - (last + 1) - index));
        }
        count--;
    }

    private void shiftRight(int index) {
        for (int i = index; i > last + 1; i--) {
            data[i] = data[i - 1];
        }
        last = last + 1;

    }

    private void shiftLeft(int index) {
        for (int i = index; i < first - 1; i++) {
            data[i] = data[i + 1];
        }
        first = first - 1;
    }
}

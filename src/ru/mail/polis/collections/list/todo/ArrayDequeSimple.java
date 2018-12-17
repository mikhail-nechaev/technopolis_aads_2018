package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    Object[] data;
    int head = 0;
    int tail = 0;

    public ArrayDequeSimple() {
        data = new Object[16];
    }

    public ArrayDequeSimple(int size) {
        data = new Object[size];
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) throw new NullPointerException("Element can't be null.");

        if (--head < 0) {
            head = data.length - 1;
        }
        data[head] = value;
        if (head == tail) doubleSize();
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty.");

        E result = getFirst();
        data[head++] = null;
        if (head >= data.length) {
            head = 0;
        }
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
        if (isEmpty()) throw new NoSuchElementException("Deque is empty.");
        //noinspection unchecked
        return (E) data[head];
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null) throw new NullPointerException("Element can't be null.");

        data[tail++] = value;
        if (tail == data.length) tail = 0;
        if (head == tail) doubleSize();
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty.");

        int t = tail - 1;
        if (t == -1) t = data.length - 1;
        //noinspection unchecked
        E result = (E) data[t];
        data[t] = null;
        tail = t;
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
        if (isEmpty()) throw new NoSuchElementException("Deque is empty.");
        int t = tail - 1;
        if (t == -1) t = data.length - 1;
        //noinspection unchecked
        return (E) data[t];
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

        int i = head;
        Object element;
        while ((element = data[i]) != null) {
            if (value.equals(element)) return true;
            if (++i == data.length) i = 0;
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
        if (tail >= head) {
            return tail - head;
        } else {
            return data.length - head + tail;
        }
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        int i = head;
        while (data[i] != null) {
            data[i] = null;
            if (++i == data.length) i = 0;
        }
        head = tail = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public ListIterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements ListIterator<E> {

        private int returnedPointer = -1;
        private boolean deleted = false;

        @Override
        public boolean hasNext() {
            int currentPointer = returnedPointer < 0 ? head - 1 : returnedPointer;
            return currentPointer + 1 < data.length ? data[currentPointer + 1] != null : data[0] != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();

            int currentPointer = returnedPointer < 0 ? head - 1 : returnedPointer;
            returnedPointer = currentPointer + 1 < data.length ? currentPointer + 1 : 0;
            @SuppressWarnings("unchecked")
            E next = (E) data[returnedPointer];
            deleted = false;
            return next;
        }

        @Override
        public boolean hasPrevious() {
            if (returnedPointer < 0) return false;
            if (deleted) return data[returnedPointer] != null;
            return returnedPointer > 0 ? data[returnedPointer - 1] != null : data[data.length - 1] != null;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();

            if (!deleted) {
                returnedPointer = returnedPointer > 0 ? returnedPointer - 1 : data.length - 1;
            }

            @SuppressWarnings("unchecked")
            E prev = (E) data[returnedPointer];
            return prev;
        }

        @Override
        public int nextIndex() {
            return returnedPointer + 1 < data.length ? returnedPointer + 1 : 0;
        }

        @Override
        public int previousIndex() {
            if (deleted) return returnedPointer;
            return returnedPointer > 0 ? returnedPointer - 1 : data.length - 1;
        }

        @Override
        public void remove() {
            if (returnedPointer < 0 || deleted) throw new IllegalStateException();

            boolean shifted = delete(returnedPointer);
            if (shifted) {
                returnedPointer = returnedPointer > 0 ? returnedPointer - 1 : data.length - 1;
            }
            deleted = true;
        }

        @Override
        public void set(E e) {
            data[returnedPointer] = e;
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    private void doubleSize() {
        int currentSize = data.length;
        int newSize = currentSize << 1;
        if (newSize < 0) throw new IllegalStateException("Max deque size is reached.");

        Object[] newData = new Object[newSize];

        int i = head;
        int j = 0;
        while (i != tail) {
            newData[j++] = data[i];
            if (++i == currentSize) {
                i = 0;
            }
        }

        data = newData;
        head = 0;
        tail = currentSize;
    }

    /**
     * Removes the element at the specified position in the elements array,
     * adjusting head and tail as necessary.  This can result in motion of
     * elements backwards or forwards in the array.
     *
     * @return true if elements moved backwards
     */
    boolean delete(int i) {
        int h = head;
        int t = tail;
        int elementsBefore = i - h >= 0 ? i - h : data.length - i + h;
        int elementsAfter = t - i > 0 ? t - i - 1 : data.length - t + i;
        if (i == t) {
            elementsAfter = 0;
        }

        if (elementsBefore < elementsAfter) {
            if (h <= i) {
                System.arraycopy(data, h, data, h + 1, elementsBefore);
                data[h] = null;
                head = h + 1 == data.length ? 0 : h + 1;
                return false;
            } else {
                System.arraycopy(data, i, data, i + 1, elementsAfter);
                data[i] = null;
                tail = t == 0 ? data.length - 1 : t - 1;
                return true;
            }
        } else {
            if (i < t) {
                System.arraycopy(data, i, data, i + 1, elementsAfter);
                data[i] = null;
                tail = t - 1;
                return true;
            } else {
                System.arraycopy(data, h, data, h + 1, elementsBefore);
                data[h] = null;
                head = h + 1 == data.length ? 0 : h + 1;
                return false;
            }
        }
    }
}

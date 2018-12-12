package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

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

    /**
     * Elements of deque
     */
    private Object[] deque = new Object[0];

    /**
     * Numbers of elements
     */

    private int N = 0;

    /**
     * The index of start deque
     */

    private int head = 0;

    /**
     * The index of end deque
     */

    private int tail = -1;

    public static void main(String[] args) {
        new ArrayDequeSimple<Integer>().clear();
    }

    /**
     * @param startIndex The index of start deque's elements in array
     */

    private void extendArray(int startIndex) {
        int newLength = (N == 0) ? 4 : (N + N / 2);

        Object[] newArray = new Object[newLength];

        if (N > 0) {
            int targetInd = startIndex;

            if (tail < head) {
                for (int i = head; head < deque.length; i++) {
                    newArray[targetInd] = deque[i];
                    targetInd++;
                }
                for (int i = 0; i <= tail; i++) {
                    newArray[targetInd] = deque[i];
                    targetInd++;
                }
            } else {
                for (int i = head; head <= tail; i++) {
                    newArray[targetInd] = deque[i];
                    targetInd++;
                }
            }
            head = startIndex;
            tail = targetInd - 1;
        } else {
            head = 0;
            tail = -1;
        }

        deque = newArray;
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
            throw new NullPointerException("Specified element is null");
        }

        if (N == deque.length) {
            extendArray(1);
        }

        if (head > 0) {
            head--;
        } else {
            head = deque.length - 1;
        }

        deque[head] = value;
        N++;

        if (N == 1) {
            tail = head;
        }
    }

    /**
     * Retrieves and removes the head element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (N == 0) {
            throw new NoSuchElementException("Deque is empty");
        }

        Object value = deque[head];

        if (head == deque.length - 1) {
            head = 0;
        } else {
            head++;
        }

        N--;

        return (E) value;
    }

    /**
     * Retrieves, but does not remove, the head element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (N == 0) {
            throw new NoSuchElementException("queue is empty");
        }

        Object value = deque[head];

        return (E) value;
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
            throw new NullPointerException("Specfied element is null");
        }

        if (N == deque.length) {
            extendArray(0);
        }

        if (tail == deque.length - 1) {
            tail = 0;
        } else {
            tail++;
        }

        deque[tail] = value;
        ++N;

        if (N == 1) {
            head = tail;
        }
    }

    /**
     * Retrieves and removes the tail element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (N == 0) {
            throw new NoSuchElementException("Deque is empty");
        }

        Object value = deque[tail];

        if (tail == 0) {
            tail = deque.length - 1;
        } else {
            tail--;
        }

        --N;

        return (E) value;
    }

    /**
     * Retrieves, but does not remove, the tail element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (N == 0) {
            throw new NoSuchElementException("Deque is empty");
        }

        Object value = deque[tail];

        return (E) value;
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
            throw new NullPointerException("Specified element is null");
        }

        if (N == 0) {
            return false;
        }

        if (tail < head) {
            for (int i = head; i < deque.length; i++) {
                if (value.equals(deque[i])) {
                    return true;
                }
            }
            for (int i = 0; i <= tail; i++) {
                if (value.equals(deque[i])) {
                    return true;
                }
            }
        } else {
            for (int i = head; i <= tail; i++) {
                if (value.equals(deque[i])) {
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
        return N;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        N = 0;
        head = 0;
        tail = -1;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from head (head) to tail (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<E> {

        private int i = N;

        private int target = head;

        @Override
        public void remove() {

            if (N == 0) {
                throw new IllegalStateException();
            }

            int targetInd = target == 0 ? deque.length - 1 : target - 1;

            if (head > tail && targetInd < tail) {
                int j;
                for (j = targetInd; j < tail; j++) {
                    deque[j] = deque[j + 1];
                }
                tail = j - 1;
            } else {
                int j;
                for (j = targetInd; j > head; j--) {
                    deque[j] = deque[j - 1];
                }
                head = j + 1;
            }
            N--;

        }

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public E next() {
            if (hasNext()) {
                Object value = deque[target++];

                i--;

                if (target == deque.length) {
                    target = 0;
                }

                return (E) value;
            } else {
                throw new NoSuchElementException("Deque is empty");
            }
        }
    }
}

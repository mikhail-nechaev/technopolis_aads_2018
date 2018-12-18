package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.junit.Assert;

import static java.lang.Float.max;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    Object[] deque;
    int first = 0;
    int end = 0;
    int n;

    public ArrayDequeSimple() {
        n = 10;
        deque = new Object[n];
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
            deque[first] = value;
            end++;
        } else {
            for (int i = end; i > 0; i--) {
                deque[i] = deque[i-1];
            }
            deque[0] = value;
            end++;
            if (end == n ) {
                increaseSize();
            }
        }
    }

    public void increaseSize(){
        Object[] array = deque.clone();
        deque = new Object[n * 2];
        for (int i = 0; i < n; i++) {
            deque[i] = array[i];
        }
        n = n * 2;
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
        E output = (E) deque[first];
        if ((end == 1)) {
            clear();
        } else {
            for (int i = 0; i < end - 1; i++) {
                deque[i] = deque[i + 1];
            }
            end--;
        }
        return output;
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
        return (E) deque[first];
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
        if (end == n ) {
            increaseSize();
        }
        deque[end++] = value;
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
        return (E) deque[--end];
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
        return (E) deque[end - 1];
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
        boolean b = false;
        if (isEmpty()) {
            return b;
        }
        for (int i = 0; i < end; i++) {
            if (deque[i] == value) {
                b = true;
            }
        }
        return b;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return (end);
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return (end == 0);

    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        deque = new Object[n];
        first = 0;
        end = 0;
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
            int size = size();
            int pointer = 0;
            boolean state = false;

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                state = false;
                return size > 0;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public E next() {
                if (hasNext()) {
                    size--;
                    state = true;
                    return (E) deque[pointer++];

                } else {
                    throw new NoSuchElementException();
                }

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
                if (!state) throw new IllegalStateException();
                pointer--;
                for (int i = pointer; i < end+1 ; i++) {
                    deque[i] = deque[i + 1];
                }
                end--;
                state=false;
            }
        };
        return iterator;
    }

    public static void main(String[] args) {

    }
}

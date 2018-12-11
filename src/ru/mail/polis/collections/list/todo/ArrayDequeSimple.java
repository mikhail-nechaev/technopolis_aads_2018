package ru.mail.polis.collections.list.todo;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import ru.mail.polis.collections.list.IDeque;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {
    private static final int DEFAULT_SIZE = 16;
    private E[] array = (E[]) new Object[DEFAULT_SIZE];
    private int startPointer = 0;
    private int endPointer = 0;
    private int size = 0;

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException("Element is null.");
        }
        if (size == array.length) {
            increaseArray();
        }
        if (size == 0) {
            array[startPointer] = value;
        } else if (startPointer == 0) {
            startPointer = array.length - 1;
            array[startPointer] = value;
        } else {
            startPointer--;
            array[startPointer] = value;
        }
        size++;
    }

    private void increaseArray() {
        E[] newArray = (E[]) new Object[size * 2];
        int i = 0;
        while (startPointer != endPointer) {
            newArray[i] = array[startPointer];
            if (startPointer == array.length - 1) {
                startPointer = 0;
            } else {
                startPointer++;
            }
            i++;
        }
        newArray[i] = array[startPointer];
        array = newArray;
        startPointer = 0;
        endPointer = size - 1;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        E element = array[startPointer];
        array[startPointer] = null;
        if (startPointer != endPointer) {
            if (startPointer == array.length - 1) {
                startPointer = 0;
            } else {
                startPointer++;
            }
        }
        size--;
        return element;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        return array[startPointer];
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
            throw new NullPointerException("Element is null.");
        }
        if (size == array.length) {
            increaseArray();
        }
        if (size == 0) {
            array[endPointer] = value;
        } else if (endPointer == array.length - 1) {
            endPointer = 0;
            array[endPointer] = value;
        } else {
            endPointer++;
            array[endPointer] = value;
        }
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
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        E element = array[endPointer];
        array[endPointer] = null;
        if (startPointer != endPointer) {
            if (endPointer == 0) {
                endPointer = array.length - 1;
            } else {
                endPointer--;
            }
        }
        size--;
        return element;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        return array[endPointer];
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
        for (E i : this) {
            if (i.equals(value)) {
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
        array = (E[]) new Object[DEFAULT_SIZE];
        startPointer = 0;
        endPointer = 0;
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
        return new ListIterator<>() {
            int pointer = -1;

            @Override
            public boolean hasNext() {
                if (size == 0) {
                    return false;
                }
                return pointer != endPointer;
            }

            @Override
            public E next() {
                pointer = nextIndex();
                return array[pointer];
            }

            @Override
            public boolean hasPrevious() {
                return pointer != -1 && pointer != startPointer;
            }

            @Override
            public E previous() {
                pointer = previousIndex();
                return array[pointer];
            }

            @Override
            public int nextIndex() {
                if (pointer == -1) {
                    return startPointer;
                }
                if (!hasNext()) {
                    return size;
                }
                int nextPointer = pointer;
                if (pointer == array.length - 1) {
                    nextPointer = 0;
                } else {
                    nextPointer++;
                }
                return nextPointer;
            }

            @Override
            public int previousIndex() {
                if (!hasPrevious()) {
                    return -1;
                }
                int previousPointer = pointer;
                if (pointer == 0) {
                    previousPointer = array.length - 1;
                } else {
                    previousPointer--;
                }
                return previousPointer;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(E e) {
                if (pointer == -1) {
                    throw new IllegalStateException();
                }
                array[pointer] = e;
            }

            @Override
            public void add(E e) {
                throw new UnsupportedOperationException();
            }
        };
    }
}

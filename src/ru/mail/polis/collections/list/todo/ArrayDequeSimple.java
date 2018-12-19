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
    protected E[] array = (E[]) new Object[DEFAULT_SIZE];
    protected int startPointer = 0;
    protected int endPointer = 0;
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
            int nextPointer = startPointer;
            int previousPointer = -1;
            boolean hasCurrent = false;

            @Override
            public boolean hasNext() {
                if (size == 0) {
                    return false;
                }
                return nextPointer != -1;
            }

            private int shiftNext(int index) {
                return (index == array.length - 1) ? 0 : index + 1;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E elem = array[nextPointer];
                if (elem == null) {
                    throw new NoSuchElementException();
                }
                if (nextPointer != startPointer) {
                    previousPointer = (previousPointer == -1) ? startPointer : shiftNext(previousPointer);
                }
                nextPointer = (nextPointer == endPointer) ? -1 : shiftNext(nextPointer);
                hasCurrent = true;
                return elem;
            }

            @Override
            public boolean hasPrevious() {
                if (size == 0) {
                    return false;
                }
                return previousPointer != -1;
            }

            private int shiftPrevious(int index) {
                return (index == 0) ? array.length - 1 : index - 1;
            }

            @Override
            public E previous() {
                E elem = array[previousPointer];
                if (elem == null) {
                    throw new NoSuchElementException();
                }
                nextPointer = (nextPointer == -1) ? endPointer : shiftPrevious(nextPointer);
                previousPointer = (previousPointer == startPointer) ? -1 : shiftPrevious(previousPointer);
                hasCurrent = true;
                return elem;
            }

            private int findIndexByPointer(int pointer) {
                if (pointer < startPointer) {
                    return array.length - startPointer + pointer;
                } else {
                    return pointer - startPointer;
                }
            }

            @Override
            public int nextIndex() {
                return (nextPointer == -1) ? size : findIndexByPointer(nextPointer);
            }

            @Override
            public int previousIndex() {
                return (previousPointer == -1) ? -1 : findIndexByPointer(previousPointer);
            }

            @Override
            public void remove() {
                if (!hasCurrent) {
                    throw new IllegalStateException();
                }
                int oldNextPointer = nextPointer;
                int oldPreviousPointer = previousPointer;
                if (nextPointer != -1) {
                    while (nextPointer != endPointer) {
                        array[shiftPrevious(nextPointer)] = array[nextPointer];
                        next();
                    }
                    array[shiftPrevious(nextPointer)] = array[nextPointer];
                    array[nextPointer] = null;
                    previousPointer = oldPreviousPointer;
                    nextPointer = shiftPrevious(oldNextPointer);
                    endPointer = shiftPrevious(endPointer);
                } else if (previousPointer != -1) {
                    array[shiftNext(previousPointer)] = null;
                    endPointer = shiftPrevious(endPointer);
                } else if (size == 1) {
                    array[startPointer] = null;
                } else {
                    throw new IllegalStateException();
                }
                hasCurrent = false;
                size--;
            }

            @Override
            public void set(E e) {
                if (nextPointer != -1) {
                    array[shiftPrevious(nextPointer)] = e;
                } else if (previousPointer != -1) {
                    array[shiftNext(previousPointer)] = e;
                } else if (size == 1) {
                    array[startPointer] = e;
                } else {
                    throw new IllegalStateException();
                }
            }

            @Override
            public void add(E e) {
                throw new UnsupportedOperationException();
            }
        };
    }
}

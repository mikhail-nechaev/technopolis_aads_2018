package ru.mail.polis.collections.list.todo;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import ru.mail.polis.collections.list.IDeque;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    Object elements[]; //array that contains elements

    int headIndex; //index of head

    int tailIndex; //index of tail

    int modCount;

    private static final int MIN_CAPACITY = 8; //must be a power of two

    public ArrayDequeSimple() {
        this.elements = new Object[MIN_CAPACITY];
        this.headIndex = 0;
        this.tailIndex = 0;
        modCount = 0;
    }

    protected void increaseCapacity() {
        int oldHeadIndex = headIndex;
        int numRight = elements.length - oldHeadIndex;
        int newCap = elements.length * 2;
        if (newCap < 0) {
            throw new IllegalStateException("New capacity is too big");
        }
        Object[] newElements = new Object[newCap];
        System.arraycopy(this.elements, oldHeadIndex, newElements, 0, numRight);
        System.arraycopy(this.elements, 0, newElements, numRight, oldHeadIndex);
        this.elements = newElements;
        headIndex = 0;
        tailIndex = numRight + oldHeadIndex - 1;
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
            throw new NullPointerException("Value must be not null");
        }
        if (elements[headIndex] == null) {
            elements[headIndex] = value;
        } else {
            headIndex = (headIndex - 1) & (elements.length - 1);
            elements[headIndex] = value;
        }
        if (((headIndex - 1) & (elements.length - 1)) == tailIndex) {
            increaseCapacity();
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
        E value = (E) elements[headIndex];
        if (value == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        elements[headIndex] = null;
        if (headIndex != tailIndex) {
            headIndex = (headIndex + 1) & (elements.length - 1);
        }
        modCount++;
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
        E value = (E) elements[headIndex];
        if (value == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return value;
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
            throw new NullPointerException("Value must be not null");
        }
        if (elements[tailIndex] == null) {
            elements[tailIndex] = value;
        } else {
            tailIndex = (tailIndex + 1) & (elements.length - 1);
            elements[tailIndex] = value;
        }
        if (((headIndex - 1) & (elements.length - 1)) == tailIndex) {
            increaseCapacity();
        }
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
        E value = (E) elements[tailIndex];
        if (value == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        elements[tailIndex] = null;
        if (headIndex != tailIndex) {
            tailIndex = (tailIndex - 1) & (elements.length - 1);
        }
        modCount++;
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
        E value = (E) elements[tailIndex];
        if (value == null) {
            throw new NoSuchElementException("Deque is empty");
        } else {
            return value;
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
            throw new NullPointerException("Value must be not null");
        } else {
            for (int i = headIndex; elements[i] != null; i = (i + 1) & (elements.length - 1)) {
                if (Objects.equals(value, elements[i])) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        if (tailIndex == headIndex) {
            if (elements[headIndex] == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            int size = (tailIndex - headIndex + 1) & (elements.length - 1);
            return size;
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
        for (int i = headIndex; elements[i] != null; i = (i + 1) & (elements.length - 1)) {
            elements[i] = null;
        }
        headIndex = 0;
        tailIndex = 0;
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

        ListIterator<E> listIterator = new ListIterator<E>() {

            private int nextIndex = headIndex;
            private int expModCount = modCount;
            private int lastReturnIndex = -1;

            final void checkComod() {
                if (modCount != expModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                if (elements[nextIndex] != null) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public E next() {
                checkComod();
                if (hasNext()) {
                    lastReturnIndex = nextIndex;
                    nextIndex = (nextIndex + 1) & (elements.length - 1);
                    return (E) elements[lastReturnIndex];
                } else {
                    throw new NoSuchElementException("There is no more elements");
                }
            }

            @Override
            public boolean hasPrevious() {
                if (elements[(nextIndex - 1) & (elements.length - 1)] != null) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public E previous() {
                checkComod();
                if (hasPrevious()) {
                    lastReturnIndex = (nextIndex - 1) & (elements.length - 1);
                    nextIndex = lastReturnIndex;
                    return (E) elements[lastReturnIndex];
                } else {
                    throw new NoSuchElementException("There is no more elements");
                }
            }

            @Override
            public int nextIndex() {
                if (hasNext()) {
                    return (nextIndex);
                } else {
                    return size();
                }
            }

            @Override
            public int previousIndex() {
                if (hasPrevious()) {
                    return (nextIndex - 1) & (elements.length - 1);
                } else {
                    return -1;
                }
            }

            @Override
            public void remove() {
                checkComod();
                if (lastReturnIndex == -1) {
                    throw new IllegalStateException();
                }
                for (int i = lastReturnIndex; i != tailIndex; i = (i + 1) & (elements.length - 1)) {
                    elements[i] = elements[(i + 1) & (elements.length - 1)];
                }
                elements[tailIndex] = null;
                lastReturnIndex = -1;
                if (headIndex != tailIndex) {
                    tailIndex = (tailIndex - 1) & (elements.length - 1);
                }
                nextIndex = (nextIndex - 1) & (elements.length - 1);
            }

            @Override
            public void set(E e) {
                checkComod();
                if (lastReturnIndex == -1) {
                    throw new IllegalStateException();
                }
                if (e == null) {
                    throw new NullPointerException("Value must be not null");
                }
                elements[lastReturnIndex] = e;
            }

            @Override
            public void add(E e) {
                checkComod();
                lastReturnIndex = -1;
                for (int i = tailIndex; i != (nextIndex - 1); i = (i - 1) & (elements.length - 1)) {
                    elements[(i + 1) & (elements.length - 1)] = elements[i];
                }
                elements[nextIndex] = e;
                tailIndex = (tailIndex + 1) & (elements.length - 1);
                nextIndex = (nextIndex + 1) & (elements.length - 1);
                if (((headIndex - 1) & (elements.length - 1)) == tailIndex) {
                    int oldHeadIndex = headIndex;
                    int oldLength = elements.length;
                    increaseCapacity();
                    nextIndex = (nextIndex - oldHeadIndex) & (oldLength - 1);
                }
            }
        };

        return listIterator;

    }
}

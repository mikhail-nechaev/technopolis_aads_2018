package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.*;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private Object[] queue;
    private int size = 0;

    private final Comparator<E> comparator;

    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection.
     *
     * You may consider that all elements in collection is not a null.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @throws NullPointerException if the specified collection is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection) {
        this(collection, Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        queue = new Object[13];
        this.comparator = Objects.requireNonNull(comparator, "comparator");
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection
     *  that orders its elements according to the specified comparator.
     *
     * You may consider that all elements in collection is not a null.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        queue = Objects.requireNonNull(collection).toArray();
        size = collection.size();
        heapify();
    }

    /**
     * Inserts the specified element into this priority queue.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E value) {
        if (value == null) throw new NullPointerException();

        if (size >= queue.length) {
            doubleSize();
        }
        if (size == 0) {
            queue[0] = value;
            size++;
        } else {
            size++;
            siftUp(size - 1, value);
        }
    }

    /**
     * Retrieves and removes the head of this queue.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        E elem = (E) queue[0];
        removeAt(0);
        return elem;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     * <p>
     * Complexity = O(1)
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) queue[0];
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     * <p>
     * Complexity = O(n)
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(E value) {
        if (value == null) throw new NullPointerException();

        for (int i = 0; i < size; i++) {
            if (value.equals(queue[i])) {
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
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayPriorityQueueIterator();
    }

    private class ArrayPriorityQueueIterator implements Iterator<E> {
        int cursor = 0;
        ArrayDeque<E> movedElements = new ArrayDeque<>();
        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return size > 0 && cursor < size || !movedElements.isEmpty();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            if (size > 0 && cursor < size) {
                return (E) queue[cursor++];
            } else {
                return movedElements.poll();
            }
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
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
            if (cursor == 0) throw new IllegalStateException();
            E moved = removeAt(cursor - 1);
            if (moved == null) {
                cursor--;
            } else {
                movedElements.add(moved);
            }
        }
    }

    private void doubleSize() {
        int currentSize = queue.length;
        int newSize = currentSize << 1;
        if (newSize < 0) throw new IllegalStateException("Max queue size is reached.");
        queue = Arrays.copyOf(queue, newSize);
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int elemIndex, E elem) {
        while (elemIndex > 0) {
            int parentIndex = (elemIndex - 1) >>> 1;
            Object parent = queue[parentIndex];
            if (comparator.compare(elem, (E) parent) >= 0) break;

            queue[elemIndex] = parent;
            elemIndex = parentIndex;
        }
        queue[elemIndex] = elem;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int elemIndex, E elem) {
        int halfIndex = size >>> 1; // element at that index is the first who has no children
        while (elemIndex < halfIndex) {
            int childIndex = (elemIndex << 1) + 1;
            int rightChildIndex = childIndex + 1;

            Object child = queue[childIndex];
            if (rightChildIndex < size
                    && comparator.compare((E) child, (E) queue[rightChildIndex]) > 0) {
                child = queue[rightChildIndex];
                childIndex = rightChildIndex;
            }
            if (comparator.compare(elem, (E) child) <= 0) break;
            queue[elemIndex] = child;
            elemIndex = childIndex;
        }
        queue[elemIndex] = elem;
    }

    @SuppressWarnings("unchecked")
    private E removeAt(int toRemove) {
        size--;
        if (size == toRemove) {
            queue[toRemove] = null;
        } else {
            E moved = (E) queue[size];
            queue[size] = null;

            siftDown(toRemove, moved);
            if (queue[toRemove] == moved) {
                siftUp(toRemove, moved);
                if (queue[toRemove] != moved) {
                    return moved;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        final Object[] elements = queue;
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) elements[i]);
        }
    }
}

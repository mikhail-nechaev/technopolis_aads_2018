package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.*;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - increase as necessary to support
 *
 * @param <E> the type of elements maintained by this priority elements
 */
@SuppressWarnings("ALL")
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;

    Object[] elements; // non-private to simplify nested class access
    int size;


    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection.
     * <p>
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority elements
     * @throws NullPointerException if the specified collection is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection) {
        this(collection, Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority elements.
     * @throws NullPointerException if the specified comparator is null
     */
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        if (comparator == null)
            throw new NullPointerException();
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        this.elements = new Object[11];
        this.size = 0;
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection
     * that orders its elements according to the specified comparator.
     * <p>
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority elements
     * @param comparator comparator the comparator that will be used to order this priority elements.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator) {
        if (collection == null || comparator == null)
            throw new NullPointerException();
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        this.elements = new Object[11];
        this.size = 0;
        int s = size();
        collection.forEach(this::add);
    }

    /**
     * Inserts the specified element into this priority elements.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E value) {
        if (value == null)
            throw new NullPointerException();
        if (size == elements.length)
            increase(size);
        elements[size] = value;
        siftUp(size);
        size++;
    }

    private void siftUp(int pos) {
        if (pos == 0) {
            return;
        }
        while (comparator.compare((E) elements[pos], (E) elements[(pos - 1) / 2]) < 0) {
            Object tmp = elements[pos];
            elements[pos] = elements[(pos - 1) / 2];
            elements[(pos - 1) / 2] = tmp;
            pos = (pos - 1) / 2;
            if (pos == 0) {
                break;
            }
        }
    }

    private void siftDown(int i) {
        while (2 * i + 1 < size()) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (right >= size()) {
                if (comparator.compare((E) elements[i], (E) elements[left]) < 0) {
                    break;
                }
                Object tmp = elements[i];
                elements[i] = elements[left];
                elements[left] = tmp;
                break;
            }
            int j = right;
            if (comparator.compare((E) elements[right], (E) elements[left]) > 0) {
                j = left;
            }

            if (comparator.compare((E) elements[i], (E) elements[j]) < 0) {
                break;
            }
            Object tmp = elements[i];
            elements[i] = elements[j];
            elements[j] = tmp;
            i = j;

        }
    }


    private void increase(int value) {
        int growValue = elements.length + value;
        Object[] tmp = new Object[growValue];
        System.arraycopy(elements, 0, tmp, 0, elements.length);
        elements = tmp;
    }

    /**
     * Retrieves and removes the head of this elements.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the head of this elements
     * @throws java.util.NoSuchElementException if this elements is empty
     */
    @Override
    public E remove() {
        if (isEmpty())
            throw new NoSuchElementException();
        Object tmp = elements[0];
        elements[0] = elements[size() - 1];
        size--;
        siftDown(0);
        return (E) tmp;
    }


    /**
     * Retrieves, but does not remove, the head of this elements.
     * <p>
     * Complexity = O(1)
     *
     * @return the head of this elements
     * @throws java.util.NoSuchElementException if this elements is empty
     */
    @Override
    public E element() {
        if (isEmpty())
            throw new NoSuchElementException();
        return (E) elements[0];
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
        if (value == null)
            throw new NullPointerException();
        for (int i = 0; i < size; i++)
            if (elements[i].equals(value))
                return true;
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
        return size() == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    void delete(int i) {
        if (isEmpty())
            throw new NoSuchElementException();
        Object tmp = elements[i];
        elements[i] = elements[size() - 1];
        size--;
        siftDown(0);
    }

    /**
     * Returns an iterator over the elements in this elements. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this elements
     */
    @Override
    public Iterator<E> iterator() {
        return new QueueIterator(elements);
    }

    public class QueueIterator implements Iterator {

        ArrayPriorityQueueSimple<E> queue = new ArrayPriorityQueueSimple<>();
        int cursor = -1;

        QueueIterator(Object[] queue) {
            for (int i = 0; i < queue.length; i++) {
                if (queue[i] != null)
                    this.queue.add((E) queue[i]);
                else break;
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Object next() {
            if (!hasNext())
                throw new NoSuchElementException();
            cursor++;
            return queue.remove();
        }

        @Override
        public void remove() {
            if (cursor == -1)
                throw new IllegalStateException();
            delete(cursor + 1);
        }
    }
}
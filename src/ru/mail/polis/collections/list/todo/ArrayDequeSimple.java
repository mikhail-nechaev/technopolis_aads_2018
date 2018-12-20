package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Resizable cyclic values implementation of the {@link IDeque} interface.
 * - no length restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    protected static final int DEFAULT_CAPACITY = 5;
    protected final int startCapacity;
    protected Object[] values;
    protected int size;
    protected int length;
    protected int modCount;
    protected int head;
    protected int tail;

    public ArrayDequeSimple() {
        this(DEFAULT_CAPACITY);
    }

    public <E> ArrayDequeSimple(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Initial Capacity must be higher 0");
        }
        capacity = capacity < DEFAULT_CAPACITY ? DEFAULT_CAPACITY : capacity;
        startCapacity = capacity;
        this.values = new Object[capacity];
        this.size = 0;
        this.length = capacity;
        this.modCount = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(values) + " cap = " + length + " length = " + size + " a[h] = " + values[head] + " h = " + head +
                " a[t] = " + values[tail] + " last = " + tail + " length =  " + values.length;
    }

    private void remakeDeque() {
        if (size == length) {
            length <<= 1;
            Object[] newArray = new Object[length];
            System.out.println("Before = " + toString());
            values = increase(newArray);
            System.out.println("After = " + toString());
        } else if (size <= length / 2 && length > startCapacity) {


            //System.out.println("Before = " + toString());
            length = size;
            Object[] newArray = new Object[length];
            values = reduce(newArray);
            //   System.out.println("After = " + toString());
        }
    }

    private Object[] increase(Object[] newArray) {
        if (head < tail) {
            head = 0;
            tail = newArray.length - 1;
            return Arrays.copyOf(values, newArray.length);
        }
        int head1 = newArray.length / 4, tail1 = head1 + values.length - head + tail;
        System.arraycopy(values, head, newArray, head1, values.length - head);
        System.arraycopy(values, 0, newArray, head1 + values.length - head, tail + 1);
        head = head1;
        tail = tail1;


        return newArray;
    }

    private Object[] reduce(Object[] newArray) {
        int head1 = 0, tail1 = newArray.length - 1;
        if (head < tail) {
            System.arraycopy(values, head, newArray, 0, newArray.length);
        } else {
            System.arraycopy(values, head, newArray, head1, values.length - head);
            System.arraycopy(values, 0, newArray, head1 + values.length - head, tail + 1);
        }
        head = head1;
        tail = tail1;
        return newArray;
    }

    private int indexOf(Object o) {
        for (int i = head; i <= tail; i++) {
            if (o.equals(values[i])) {
                return i;
            }
        }
        return -1;
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
            throw new NullPointerException("Value is null");
        }
        if (isEmpty()) {
            head = tail = length / 2;
        } else {
            head = head == 0 ? length - 1 : head - 1;
        }
        values[head] = value;

        modCount++;
        size++;
        remakeDeque();
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the first of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("You can`t remove object in empty deque");
        }


        E oldValue = getFirst();
        size--;
        modCount++;

        values[head] = null;
        head = (head + 1) % length;
        remakeDeque();
        return oldValue;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the first of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        return (E) values[head];
    }

    /**
     * Inserts the specified element at the last of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null) {
            throw new NullPointerException("Value is null");
        }
        if (isEmpty()) {
            head = tail = length / 2;
        } else {
            tail = tail == length - 1 ? 0 : tail + 1;
        }
        values[tail] = value;
        modCount++;
        size++;
        remakeDeque();
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the last of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("You can`t remove object in empty deque");
        }

        E oldValue = getLast();
        size--;
        modCount++;
        values[tail] = null;
        tail = tail == 0 ? length - 1 : tail - 1;
        remakeDeque();
        modCount++;

        return oldValue;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the last of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return (E) values[tail];
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

        return indexOf(value) > -1;
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
        values = new Object[DEFAULT_CAPACITY];
        size = 0;
        length = DEFAULT_CAPACITY;
    }


    protected void delete(int index) {
        if (head == index) {
            removeFirst();
        } else if (tail == index) {
            removeLast();
        } else if (head <= tail) {
            if (index > tail || index < head) {
                throw new IllegalArgumentException();
            }
            System.arraycopy(values, index + 1, values, index, tail - index);
            tail--;
            tail = tail == 0 ? length - 1 : tail - 1;

            size--;
            modCount++;
        } else {
            if (index > head) {
                System.arraycopy(values, head, values, head + 1, index - head);
                head = (head + 1) % length;
            } else if (index < tail) {
                System.arraycopy(values, index + 1, values, index, tail - index);
                tail--;
                tail = tail == 0 ? length - 1 : tail - 1;
            } else {
                throw new IllegalArgumentException();
            }

            size--;
            modCount++;
        }
        remakeDeque();
    }


    private int addIndex(int index, E value) {


        if (head <= tail) {
            //сдвигаем хвост
            if (tail < values.length - 1) {
                System.arraycopy(values, index, values, index + 1, tail - index + 1);
                tail++;
            } else { //сдвигаем голову
                System.arraycopy(values, head, values, head - 1, index - head + 1);
                head--;
            }
        } else {
            if (index >= head) {
                System.arraycopy(values, head, values, head - 1, index - head + 1);
                tail++;
            } else {
                head--;
                System.arraycopy(values, index, values, index + 1, tail - index + 1);
            }
        }
        remakeDeque();
        size++;
        modCount++;
        values[index] = value;
        return index;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (first) to last (last).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @SuppressWarnings("unchecked")
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int lastIndex = -1, lastRealIndex = head - 1;
            int nextIndex = 0, nextRealIndex = head;
            int size = ArrayDequeSimple.this.size;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                canRemove = true;
                lastIndex = nextIndex;
                lastRealIndex = nextRealIndex;
                nextIndex++;
                nextRealIndex = ++nextRealIndex % length;
                return (E) ArrayDequeSimple.this.values[lastRealIndex];
            }


            @Override
            public void remove() {

                if (!canRemove) {
                    throw new IllegalStateException();
                }
                canRemove = false;
                ArrayDequeSimple.this.delete(lastRealIndex);
            }
        };
    }
}
package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Arrays;
import java.util.Iterator;
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
    protected static final int DEFAULT_CAPACITY = 5;
    protected final int BEGIN_CAPACITY;
    protected Object[] array;
    protected int size, capacity, modCount, head, tail;

    public ArrayDequeSimple() {
        this(DEFAULT_CAPACITY);
    }

    public <E> ArrayDequeSimple(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Initial Capacity must be higher 0");
        }
        capacity = capacity < DEFAULT_CAPACITY ? DEFAULT_CAPACITY : capacity;
        BEGIN_CAPACITY = capacity;
        this.array = new Object[capacity];
        this.size = 0;
        this.capacity = capacity;
        this.modCount = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(array) + " cap = " + capacity + " size = " + size + " a[h] = " + array[head] + " h = " + head +
                " a[t] = " + array[tail] + " tail = " + tail + " length =  " + array.length;
    }

    private void remakeDeque() {
        if (size == capacity) {
            capacity <<= 1;
            Object[] newArray = new Object[capacity];
            System.out.println("Before = " + toString());
            array = increase(newArray);
            System.out.println("After = " + toString());
        } else if (size <= capacity / 2 && capacity > BEGIN_CAPACITY) {


            //System.out.println("Before = " + toString());
            capacity = size;
            Object[] newArray = new Object[capacity];
            array = reduce(newArray);
            //   System.out.println("After = " + toString());
        }
    }

    private Object[] increase(Object[] newArray) {
        if (head < tail) {
            head = 0;
            tail = newArray.length - 1;
            return Arrays.copyOf(array, newArray.length);
        }
        int head1 = newArray.length / 4, tail1 = head1 + array.length - head + tail;
        System.arraycopy(array, head, newArray, head1, array.length - head);
        System.arraycopy(array, 0, newArray, head1 + array.length - head, tail + 1);
        head = head1;
        tail = tail1;


        return newArray;
    }

    private Object[] reduce(Object[] newArray) {
        int head1 = 0, tail1 = newArray.length - 1;
        if (head < tail) {
            System.arraycopy(array, head, newArray, 0, newArray.length);
        } else {
            System.arraycopy(array, head, newArray, head1, array.length - head);
            System.arraycopy(array, 0, newArray, head1 + array.length - head, tail + 1);
        }
        head = head1;
        tail = tail1;
        return newArray;
    }

    private int indexOf(Object o) {
        for (int i = head; i <= tail; i++) {
            if (o.equals(array[i])) {
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
            head = tail = capacity / 2;
        } else {
            head = head == 0 ? capacity - 1 : head - 1;
        }
        array[head] = value;

        modCount++;
        size++;
        remakeDeque();
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("You can`t remove object in empty deque");
        }


        E oldValue = getFirst();
        size--;
        modCount++;

        array[head] = null;
        head = (head + 1) % capacity;
        remakeDeque();
        return oldValue;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        return (E) array[head];
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
            throw new NullPointerException("Value is null");
        }
        if (isEmpty()) {
            head = tail = capacity / 2;
        } else {
            tail = tail == capacity - 1 ? 0 : tail + 1;
        }
        array[tail] = value;
        modCount++;
        size++;
        remakeDeque();
    }

    /**
     * Retrieves and removes the size element of this deque.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("You can`t remove object in empty deque");
        }

        E oldValue = getLast();
        size--;
        modCount++;
        array[tail] = null;
        tail = tail == 0 ? capacity - 1 : tail - 1;
        remakeDeque();
        modCount++;

        return oldValue;
    }

    /**
     * Retrieves, but does not remove, the size element of this deque.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return (E) array[tail];
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
        array = new Object[DEFAULT_CAPACITY];
        size = 0;
        capacity = DEFAULT_CAPACITY;
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
            System.arraycopy(array, index + 1, array, index, tail - index);
            tail--;
            tail = tail == 0 ? capacity - 1 : tail - 1;

            size--;
            modCount++;
        } else {
            if (index > head) {
                System.arraycopy(array, head, array, head + 1, index - head);
                head = (head + 1) % capacity;
            } else if (index < tail) {
                System.arraycopy(array, index + 1, array, index, tail - index);
                tail--;
                tail = tail == 0 ? capacity - 1 : tail - 1;
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
            if (tail < array.length - 1) {
                System.arraycopy(array, index, array, index + 1, tail - index + 1);
                tail++;
            } else { //сдвигаем голову
                System.arraycopy(array, head, array, head - 1, index - head + 1);
                head--;
            }
        } else {
            if (index >= head) {
                System.arraycopy(array, head, array, head - 1, index - head + 1);
                tail++;
            } else {
                head--;
                System.arraycopy(array, index, array, index + 1, tail - index + 1);
            }
        }
        remakeDeque();
        size++;
        modCount++;
        array[index] = value;
        return index;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to size (tail).
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
                nextRealIndex = ++nextRealIndex % capacity;
                return (E) ArrayDequeSimple.this.array[lastRealIndex];
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
    /*@Override
    public ListIterator<E> iterator() {
        return new ListIterator<E>() {
            private int nowIndex = -1, nextIndex = 0;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                nowIndex = nextIndex;
                nextIndex++;
                return (E) array[nowIndex];
            }

            @Override
            public boolean hasPrevious() {
                return nextIndex > 0;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                nowIndex = nextIndex = nextIndex - 1;
                return (E) array[nowIndex];
            }

            @Override
            public int nextIndex() {
                return nextIndex;
            }

            @Override
            public int previousIndex() {
                return nextIndex - 1;
            }

            @Override
            public void remove() {
                delete(nowIndex);
                nowIndex = -1;
            }

            @Override
            public void set(E e) {
                array[nowIndex] = e;
                modCount++;
            }

            @Override
            public void add(E e) {
                addIndex(nowIndex, e);
                nextIndex++;
            }
        };
    }

*/
}

package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

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

    protected static final int DEFAULT_INITIAL_SIZE = 8;

    protected Object[] values;
    protected int lenght = 0;
    protected int tail = 1;
    protected int head = 0;
    protected final int minimalSize;


    public ArrayDequeSimple(){
        this(DEFAULT_INITIAL_SIZE);
    }

    public ArrayDequeSimple(int initialSize){
        this.values = new Object[initialSize];
        minimalSize = initialSize;
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null)
            throw new NullPointerException("Element is null");
        if (values == null)
            throw new IllegalStateException("Array is null");
        values[head] = value;
        lenght++;
        moveHeadBack();
        checkLeftCapacity();
    }

    private void checkLeftCapacity() {
        if (lenght >= values.length - 1){
            expand();
        } else if (lenght == 0 || values.length / lenght >= 3){
            reduce();
        }
    }

    private void reduce() {
        changeCapacity(values.length / 2);
    }

    private void expand() {
        changeCapacity(values.length * 2);
    }

    private void moveHeadFurther() {
        head++;
        if (head == values.length)
            head = 0;
    }

    private void moveHeadBack() {
        head--;
        if (head == -1)
            head = values.length - 1;
    }

    private void moveTailFurther() {
        tail++;
        if (tail == values.length)
            tail = 0;
    }

    private void moveTailBack() {
        tail--;
        if (tail == -1)
            tail = values.length - 1;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (values == null)
            throw new NullPointerException("Array is null");
        if (lenght == 0)
            throw new NoSuchElementException("Deque is empty");
        moveHeadFurther();
        E result = (E) values[head];
        lenght--;
        checkLeftCapacity();
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
        if (values == null)
            throw new NullPointerException("Array is null");
        if (lenght == 0)
            throw new NoSuchElementException("Deque is empty");
        moveHeadFurther();
        E result = (E) values[head];
        moveHeadBack();
        return result;
    }

    /**
     * Inserts the specified element at the tail of this queue
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null)
            throw new NullPointerException("Element is null");
        if (values == null)
            throw new IllegalStateException("Array is null");
        values[tail] = value;
        lenght++;
        moveTailFurther();
        checkLeftCapacity();
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (values == null)
            throw new NullPointerException("Array is null");
        if (lenght == 0)
            throw new NoSuchElementException("Deque is empty");
        moveTailBack();
        E result = (E) values[tail];
        lenght--;
        checkLeftCapacity();
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
        if (values == null)
            throw new NullPointerException("Array is null");
        if (lenght == 0)
            throw new NoSuchElementException("Deque is empty");
        moveTailBack();
        E result = (E) values[tail];
        moveTailFurther();
        return result;
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
        if (value == null)
            throw new NullPointerException("Element is null");
        if (values == null)
            throw new IllegalStateException("Array is null");
        if (lenght == 0)
            return false;

        boolean contains = false;
        int i = head + 1;
        if (i == values.length)
            i = 0;
        while (i != tail){
            if (values[i] == value){
                contains = true;
                break;
            }
            i++;
            if (i == values.length)
                i = 0;
        }
        return contains;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return lenght;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return lenght == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        lenght = 0;
        tail = 0;
        head = 1;
        changeCapacity(minimalSize);
    }

    private void changeCapacity(int newSize) {

        if (newSize < minimalSize)
            return;

        Object[] newArray = new Object[newSize];
        int newHead = newSize - 1;
        int newTail = lenght;

        if (head == values.length - 1){
            System.arraycopy(values, 0, newArray, 0, lenght);
        } else if (head < tail){
            System.arraycopy(values, head + 1, newArray, 0, lenght);
        } else {
            int offset = values.length - head - 1;
            System.arraycopy(values, head + 1, newArray, 0, offset);
            System.arraycopy(values, 0, newArray, offset, tail);
        }

        values = newArray;
        head = newHead;
        tail = newTail;

    }

    private void addByIndex(E e, int index) {
        if (head <= tail) {
            if (tail < values.length - 1) {
                System.arraycopy(values, index, values, index + 1, tail - index + 1);
                moveTailFurther();
            } else { //сдвигаем голову
                System.arraycopy(values, head, values, head - 1, index - head + 1);
                moveHeadBack();
            }
        } else {
            if (index >= head) {
                System.arraycopy(values, head, values, head - 1, index - head + 1);
                moveTailFurther();
            } else {
                moveHeadBack();
                System.arraycopy(values, index, values, index + 1, tail - index + 1);
            }
        }
        checkLeftCapacity();
        lenght++;
        values[index] = e;
    }

    protected boolean deleteByIndex(int index){
        boolean shouldShift = true;
        if (head == index) {
            removeFirst();
        } else if (tail == index) {
            removeLast();
        } else if (head <= tail) {
            if (index > tail || index < head) {
                throw new IllegalArgumentException();
            }
            System.arraycopy(values, index + 1, values, index, tail - index);
            moveTailBack();
            lenght--;
        } else {
            if (index > head) {
                System.arraycopy(values, head, values, head + 1, index - head);
                head = (head + 1) % values.length;
                shouldShift = false;
            } else if (index < tail) {
                System.arraycopy(values, index + 1, values, index, tail - index);
                tail--;
                tail = tail == 0 ? values.length - 1 : tail - 1;
            } else {
                throw new IllegalArgumentException();
            }

            lenght--;
        }
        checkLeftCapacity();
        return shouldShift;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {

        return new Iterator<E>() {

            private int currentIndex = head;
            boolean removeCheck = false;

            @Override
            public boolean hasNext() {
                boolean hasNext = true;
                if (currentIndex == tail)
                    hasNext = false;
                if (nextIndex() == tail)
                    hasNext = false;
                return hasNext;
            }

            @Override
            public E next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                removeCheck = true;
                currentIndex = nextIndex();
                return (E) values[currentIndex];
            }

            private int nextIndex() {
                int index = currentIndex + 1;
                if (index == values.length)
                    index = 0;
                return index;
            }

            @Override
            public void remove() {
                if (!removeCheck) {
                    throw new IllegalStateException();
                }
                removeCheck = false;
                if (deleteByIndex(currentIndex))
                    currentIndex = (currentIndex - 1 + values.length) % values.length;
            }
        };

    }

//        return new ListIterator<E>() {
//
//            private int currentIndex = head;
//
//            @Override
//            public boolean hasNext() {
//                return !(nextIndex() == tail);
//            }
//
//            @Override
//            public E next() {
//                if (!hasNext())
//                    throw new NoSuchElementException();
//                currentIndex = nextIndex();
//                return (E) values[currentIndex];
//            }
//
//            @Override
//            public boolean hasPrevious() {
//                return !(previousIndex() == head);
//            }
//
//            @Override
//            public E previous() {
//                if (!hasPrevious())
//                    throw new NoSuchElementException();
//                currentIndex = previousIndex();
//                return (E) values[currentIndex];
//            }
//
//            @Override
//            public int nextIndex() {
//                int index = currentIndex + 1;
//                if (index == values.length)
//                    index = 0;
//                return index;
//            }
//
//            @Override
//            public int previousIndex() {
//                int index = currentIndex - 1;
//                if (index == -1)
//                    index = values.length - 1;
//                return index;
//            }
//
//            @Override
//            public void remove() {
//
//            }
//
//            @Override
//            public void set(E e) {
//                if (e == null)
//                    throw new NullPointerException();
//                values[currentIndex] = e;
//            }
//
//            @Override
//            public void add(E e) {
//                if (e == null)
//                    throw new NullPointerException();
//                addByIndex(e, currentIndex);
//            }
//        };


}

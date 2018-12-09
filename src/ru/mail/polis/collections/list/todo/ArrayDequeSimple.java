package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

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

    private E[] deque;
    private int arraySize; // Размер массива
    private int size; // Количество элементов а очереди


    //[last, ... , first]
    private int firstCursor;
    private int lastCursor;

    public E[] getDeque() {
        return deque;
    }

    public int getFirstCursor() {
        return firstCursor;
    }

    public int getLastCursor() {
        return lastCursor;
    }

    public ArrayDequeSimple() {
        arraySize = 10;
        deque = (E[]) new Object[arraySize];
        size = 0;
        firstCursor = arraySize - 1;
        lastCursor = 0;
    }

    public ArrayDequeSimple(int arraySize) {
        this.arraySize = arraySize;
        deque = (E[]) new Object[arraySize];
        size = 0;
        firstCursor = arraySize - 1;
        lastCursor = 0;

    }

    /**
     * Увеличение номера элемента, с переходом в начало массива при достижении его конца:
     * [max + 1] => [0]
     *
     * @param n
     * @return
     */
    private int cyclicInc(int n) {
        return ++n >= arraySize ? n - arraySize : n;
    }

    /**
     * Уменьшение номера элемента, с переходом в конец массива при достижении его начала:
     * [-1] => [max]
     *
     * @param n
     * @return
     */
    private int cyclicDec(int n) {
        return --n < 0 ? n + arraySize : n;
    }

    /**
     * Увеличение размеров массива в 2 раза с копирыванием элементов
     */
    private void resize() {
        arraySize *= 2;
        E[] deque = (E[]) new Object[arraySize];
        for (int i = lastCursor, j = 0; j < size; i = cyclicInc(i), j++) {
            deque[j] = this.deque[i];
        }
        this.deque = deque;
        lastCursor = 0;
        firstCursor = size - 1;
    }

    /**
     * Проверка на заполненность очереди.
     * Применять в начале методов с добавлением элементов.
     */
    private void checkFullDeque() {
        if (size == arraySize) {
            resize();
        }
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
            throw new NullPointerException();
        }
        checkFullDeque();
        firstCursor = cyclicInc(firstCursor);
        deque[firstCursor] = value;
        size++;
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
        size--;
        E value = deque[firstCursor];
        firstCursor = cyclicDec(firstCursor);
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return deque[firstCursor];
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
            throw new NullPointerException();
        }
        checkFullDeque();
        lastCursor = cyclicDec(lastCursor);
        deque[lastCursor] = value;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        size--;
        E value = deque[lastCursor];
        lastCursor = cyclicInc(lastCursor);
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return deque[lastCursor];
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
            throw new NullPointerException();
        }
        for (int i = lastCursor; i != firstCursor + 1; i = cyclicInc(i)) {
            if (deque[i].equals(value)) {
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
        deque = (E[]) new Object[arraySize];
        size = 0;
        firstCursor = arraySize;
        lastCursor = 0;
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
            //private int last = ArrayDequeSimple.this.lastCursor;
            private int first = ArrayDequeSimple.this.firstCursor;
            private int size = ArrayDequeSimple.this.size;
            private int position = -1;
            // set(), add(), remove() => true
            // next(), previous() => false
            private boolean doOperation = true;

            // last < 8 9 0 1 2 3 > first
            // next < 5 4 3 2 1 0 > previous
            private int cyclicConvert(int n) {
                if (position <= first) {
                    return first - position;
                } else {
                    return ArrayDequeSimple.this.arraySize - position + first;
                }
            }

            @Override
            public boolean hasNext() {
                return position < size - 1;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                doOperation = false;
                return ArrayDequeSimple.this.deque[cyclicConvert(++position)];
            }

            @Override
            public boolean hasPrevious() {
                return position > 0;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                doOperation = false;
                return ArrayDequeSimple.this.deque[cyclicConvert(--position)];
            }

            @Override
            public int nextIndex() {
                return hasNext() ? position + 1 : size;
            }

            @Override
            public int previousIndex() {
                return hasPrevious() ? position - 1 : -1;
            }

            @Override
            public void remove() {
                if (doOperation) {
                    throw new IllegalStateException();
                }
                doOperation = true;

                for (int i = position; i > 0; i--) {
                    ArrayDequeSimple.this.deque[cyclicConvert(i)] =
                            ArrayDequeSimple.this.deque[cyclicConvert(i - 1)];
                }
                size = --ArrayDequeSimple.this.size;
                first = ArrayDequeSimple.this.cyclicDec(ArrayDequeSimple.this.firstCursor);
            }

            @Override
            public void set(E e) {
                if (doOperation) {
                    throw new IllegalStateException();
                }
                doOperation = true;
                ArrayDequeSimple.this.deque[cyclicConvert(position)] = e;
            }

            @Override
            public void add(E e) {
                doOperation = true;
                ArrayDequeSimple.this.checkFullDeque();
                E k = e;
                size = ++ArrayDequeSimple.this.size;
                for (int i = position; i < size - 1; i++) {
                    swap(k, ArrayDequeSimple.this.deque[cyclicConvert(i)]);
                }
                ArrayDequeSimple.this.deque[cyclicConvert(size - 1)] = k;
            }

            private void swap(E t1, E t2) {
                E t = t1;
                t1 = t2;
                t2 = t;
            }
        };

    }
}

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
    Object[] elements;
    int head = 0;
    int tail = 0;
    private static final int MIN_INITIAL_CAPACITY = 8;

    public ArrayDequeSimple() {
        elements = new Object[16];
    }

    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p; // number of elements to the right of p
        int newCapacity = n << 1;
        if (newCapacity < 0)
            throw new IllegalStateException();
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }
    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if(value == null) throw new NullPointerException();
        elements[head = (head - 1) & (elements.length - 1)] = value;
        if (head == tail) doubleCapacity();
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        E x = pollFirst();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }

    public E pollFirst() {
        int h = head;
        E result = (E) elements[h];
        if (result == null)
            return null;
        elements[h] = null;     // Must null out slot
        head = (h + 1) & (elements.length - 1);
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
        E result = (E) elements[head];
        if (result == null)
            throw new NoSuchElementException();
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
            throw new NullPointerException();
        elements[tail] = value;
        if ( (tail = (tail + 1) & (elements.length - 1)) == head)
            doubleCapacity();
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {

        E x = pollLast();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }

    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1);
        @SuppressWarnings("unchecked")
        E result = (E) elements[t];
        if (result == null)
            return null;
        elements[t] = null;
        tail = t;
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
        E result = (E) elements[(tail - 1) & (elements.length - 1)];
        if (result == null)
            throw new NoSuchElementException();
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
            return false;
        int mask = elements.length - 1;
        int i = head;
        Object x;
        while ( (x = elements[i]) != null) {
            if (value.equals(x))
                return true;
            i = (i + 1) & mask;
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
        return (tail - head) & (elements.length - 1);
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        int h = head;
        int t = tail;
        if (h != t) {
            head = tail = 0;
            int i = h;
            int mask = elements.length - 1;
            do {
                elements[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public ListIterator<E> iterator() {
        return new DeqListIterator();
    }

    public class DeqListIterator implements ListIterator {

        int index = -1;

        int cursor;

        int remaining = size();

        int done = 0;

        public DeqListIterator() {
            cursor = head - 1;
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            E element;
            if (remaining <= 0) {
                throw new NoSuchElementException();
            }
            if (cursor + 1 == elements.length) {
                cursor = -1;
            }

            element = (E) elements[++cursor];
            index++;
            remaining--;
            done++;
            return element;

        }

        @Override
        public boolean hasPrevious() {
            return done > 0;
        }

        @Override
        public Object previous() {
            E element;
            if (done <= 0) {
                throw new NoSuchElementException();
            }
            if (cursor - 1 == -1) {
                cursor = elements.length - 1;
            }
            element = (E) elements[cursor--];
            index--;
            remaining++;
            done--;
            return element;
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index == -1 ? -1 : (index - 1);
        }

        @Override
        public void remove() {
            if (index == -1) {
                throw new IllegalStateException();
            }
            int tmpCur = cursor - 1;
            if (head > tail) {
                if (cursor > head)
                    tmpCur = cursor - head - 1;
                else
                    tmpCur += elements.length - head;
            }
            delete(cursor);
            cursor = tmpCur;
            done--;
            if (index == 0)
                index = -1;
        }

        @Override
        public void set(Object o) {
            if (index == -1) {
                throw new IllegalStateException();
            }
            elements[cursor] = o;
        }

        @Override
        public void add(Object o) {
            if (index == -1) {
                addFirst((E) o);
                cursor--;
            } else {
                insert(cursor, o);
            }
            remaining++;
        }
    }

    private void insert(int i, Object o) {
        Object[] tmp = new Object[elements.length];
        System.arraycopy(elements, 0, tmp, 0, i);
        System.arraycopy(elements, i + 1, tmp, i + 2, elements.length - 1 - i);
        tail++;
        if (tail == elements.length) {
            tail = 0;
        }
        elements[i + 1] = o;
        elements = tmp;
    }

    public void delete(int i) {
        Object[] tmp = new Object[elements.length];
        if (head > tail) {
            int s = size();
            if (i < head) {
                System.arraycopy(elements, head, tmp, 0, elements.length - head);
                System.arraycopy(elements, 0, tmp, elements.length - head, i);
                System.arraycopy(elements, i + 1, tmp, elements.length - head + i, s - (elements.length - head) - i - 1);
                tail = s - 2;
            } else {
                System.arraycopy(elements, head, tmp, 0, i - head);
                System.arraycopy(elements, i + 1, tmp, i - head, elements.length - i - 1);
                System.arraycopy(elements, 0, tmp, elements.length - head - 1, tail + 1);
                tail = s - 2;
            }
            head = 0;

        } else {
            System.arraycopy(elements, head, tmp, 0, i - head);
            System.arraycopy(elements, i + 1, tmp, i - head, elements.length - 1 - i - head);

            if (tail != 0) {
                tail--;
            }
            tail -= head;
            head = 0;
        }
        elements = tmp;
    }
}
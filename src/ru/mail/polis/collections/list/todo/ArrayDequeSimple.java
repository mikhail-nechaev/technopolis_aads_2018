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
@SuppressWarnings("ALL")
public class ArrayDequeSimple<E> implements IDeque<E> {

     Object[] items;
     int head = 0, tail = 0;


    public ArrayDequeSimple() {
        items = new Object[16];
    }

    public ArrayDequeSimple(int count) throws Exception {
        if (count < 1) {
            throw new Exception("");
        }
        if (count == Integer.MAX_VALUE) {
            items = new Object[Integer.MAX_VALUE];
        } else {
            items = new Object[count + 1];
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
        if ((head - tail == 1 && tail < head) ||
                (head == 0 && tail == items.length - 1)) {
            increase(items.length);
        }
        if (head <= 0 && items[0] != null) {
            head = items.length - 1;
        }
        items[items[head] == null ? head : --head] = value;
    }



    /**
     * Retrieves and removes the firstE element of this elements.
     *
     * @return the head of this elements
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        E item = (E) items[head++];
        items[head - 1] = null;
        if (head - 1 == tail)
            head = tail;
        if (head == items.length) {
            head = 0;
        }
        return item;
    }

    /**
     * Retrieves, but does not remove, the firstE element of this elements.
     *
     * @return the head of this elements
     * @throws java.util.NoSuchElementException if this elements is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        return (E) items[head];
    }

    /**
     * Inserts the specified element at the tail of this elements
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if ((head - tail == 1 && tail < head) ||
                (head == 0 && tail == items.length - 1)) {
            increase(items.length);
        }
        items[items[tail] == null ? tail : (tail == items.length - 1 ? tail = 0 : ++tail)] = value;
    }



    public void increase(int value) {
        int growValue = items.length + value;
        if (items.length == Integer.MAX_VALUE) {
            System.out.println("Can't grow any more!");
            return;
        }
        if (((long) growValue) > Integer.MAX_VALUE) {
            growValue = Integer.MAX_VALUE;
        }

        Object[] tmp = new Object[growValue];
        if (tail < head) {
            System.arraycopy(items, head, tmp, 0, items.length - head);
            System.arraycopy(items, 0, tmp, items.length - head, tail + 1);
        } else {
            System.arraycopy(items, 0, tmp, 0, items.length);
        }
        items = tmp;
        head = 0;
        tail = items.length / 2 - 1;
    }


    /**
     * Retrieves and removes the lastE element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        E item = (E) items[tail--];
        items[tail + 1] = null;
        if (items[head] == null)
            tail = head;
        else if (tail == -1) {
            tail = items.length - 1;
        }
        return item;
    }

    /**
     * Retrieves, but does not remove, the lastE element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        return (E) items[tail];
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
        if (isEmpty())
            return false;
        if (value == null) {
            throw new NullPointerException();
        }
        for (int i = head; i != tail + 1; i++) {
            if (items[i].equals(value)) {
                return true;
            }
            if (i == tail) {
                return false;
            }
            if (i == items.length - 1) {
                i = -1;
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
        if (items.length == 0 || head == tail && items[head] == null) {
            return 0;
        }
        if (head > tail) {
            return items.length - head + tail + 1;
        }
        return tail + 1;
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
        items = new Object[0];
        head = tail = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from firstE (head) to lastE (tail).
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
            if (cursor + 1 == items.length) {
                cursor = -1;
            }

            element = (E) items[++cursor];
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
                cursor = items.length - 1;
            }
            element = (E) items[cursor--];
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
                    tmpCur += items.length - head;
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
            items[cursor] = o;
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
        Object[] tmp = new Object[items.length];
        System.arraycopy(items, 0, tmp, 0, i);
        System.arraycopy(items, i + 1, tmp, i + 2, items.length - 1 - i);
        tail++;
        if (tail == items.length) {
            tail = 0;
        }
        items[i + 1] = o;
        items = tmp;
    }

    public void delete(int i) {
        Object[] tmp = new Object[items.length];
        if (head > tail) {
            int s = size();
            if (i < head) {
                System.arraycopy(items, head, tmp, 0, items.length - head);
                System.arraycopy(items, 0, tmp, items.length - head, i);
                System.arraycopy(items, i + 1, tmp, items.length - head + i, s - (items.length - head) - i - 1);
                tail = s - 2;
            } else {
                System.arraycopy(items, head, tmp, 0, i - head);
                System.arraycopy(items, i + 1, tmp, i - head, items.length - i - 1);
                System.arraycopy(items, 0, tmp, items.length - head - 1, tail + 1);
                tail = s - 2;
            }
            head = 0;

        } else {
            System.arraycopy(items, head, tmp, 0, i - head);
            System.arraycopy(items, i + 1, tmp, i - head, items.length - 1 - i - head);

            if (tail != 0) {
                tail--;
            }
            tail -= head;
            head = 0;
        }
        items = tmp;
    }
}

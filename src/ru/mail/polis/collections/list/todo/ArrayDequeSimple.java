package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


@SuppressWarnings("unchecked")
public class ArrayDequeSimple<E> implements IDeque<E> {


    protected E[] deque;
    int head;
    int tail;
    private int size;

   

    public ArrayDequeSimple() {
        deque = (E[]) new Object[8];
    }

    public ArrayDequeSimple(int numElements) {
        deque = (E []) new Object[numElements];
    }




    protected boolean delete(int i) {
        Object[] copyDeque = deque;
        int lastIndex = copyDeque.length - 1;
        int head1 = head;
        int tail1 = tail;
        int front = (i - head1) & lastIndex;
        int back = (tail1 - i) & lastIndex;

        if (front >= ((tail1 - head1) & lastIndex))
            throw new ConcurrentModificationException();
        if (front < back) {
            if (head1 <= i) {
                System.arraycopy(copyDeque, head1, copyDeque, head1 + 1, front);
            } else {
                System.arraycopy(copyDeque, 0, copyDeque, 1, i);
                copyDeque[0] = copyDeque[lastIndex];
                System.arraycopy(copyDeque, head1, copyDeque, head1 + 1, lastIndex - head1);
            }
            copyDeque[head1] = null;
            head = (head1 + 1) & lastIndex;
            return false;
        } else {
            if (i < tail1) {
                System.arraycopy(copyDeque, i + 1, copyDeque, i, back);
                tail = tail1 - 1;
            } else {
                System.arraycopy(copyDeque, i + 1, copyDeque, i, lastIndex - i);
                copyDeque[lastIndex] = copyDeque[0];
                System.arraycopy(copyDeque, 1, copyDeque, 0, tail1);
                tail = (tail1 - 1) & lastIndex;
            }
            size--;
            return true;
        }
    }

    private void exp() {
        int firstIndex = head;
        int fullLength = deque.length;
        int r = fullLength - firstIndex;
        int newCapacity = fullLength << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException();
        }
        E[] copyArray = (E[]) new Object[newCapacity];
        System.arraycopy(deque, firstIndex, copyArray, 0, r);
        System.arraycopy(deque, 0, copyArray, r, firstIndex);
        deque = copyArray;
        head = 0;
        tail = fullLength;
    }
    
    private void press() {
        int firstIndex = head;
        int fullLength = deque.length;
        int r = fullLength - firstIndex;
        int newLength = fullLength >> 1;
        if(newLength < 16){
            return;
        }
        E[] copyArray = (E[]) new Object[newLength];
        System.arraycopy(deque, firstIndex, copyArray, 0, r);
        System.arraycopy(deque, 0, copyArray, r, firstIndex);
        deque = copyArray;
        head = 0;
        tail = fullLength;
    }

    @Override
    public void addFirst(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        deque[head = (head - 1) & (deque.length - 1)] = value;
        if (head == tail) {
            exp();
        }
        size ++;
    }

    @Override
    public E removeFirst() throws NoSuchElementException {
        int h = head;
        E res = deque[h];
        if (res == null) {
            throw new NoSuchElementException();
        }
        deque[h] = null;
        head = (h + 1) & (deque.length - 1);
        size--;
        if (size << 4 == deque.length) {
            // press();
        }
        return res;
    }


    @Override
    public E getFirst() throws NoSuchElementException {
        if (deque[head] == null) {
            throw new NoSuchElementException();
        }
        return deque[head];
    }


    @Override
    public void addLast(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        deque[tail] = value;
        if ((tail = (tail + 1) & (deque.length - 1)) == head)
            exp();
        size++;
    }


    @Override
    public E removeLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int t = (tail - 1) & (deque.length - 1);
        E result = deque[t];
        if (result == null)
            return null;
        deque[t] = null;
        tail = t;
        size--;
        if(size << 4 ==deque.length) {
            //press();
        }
        return result;
    }


    @Override
    public E getLast() throws NoSuchElementException {
        E result = deque[(tail - 1) & (deque.length - 1)];
        if (result == null)
            throw new NoSuchElementException();
        return result;
    }


    @Override
    public boolean contains(E value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        int mask = deque.length - 1;
        int i = head;
        Object tmp;
        while ((tmp = deque[i]) != null) {
            if (value.equals(tmp))
                return true;
            i = (i + 1) & mask;
        }
        return false;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        int h = head;
        int t = tail;
        if (h != t) {
            head = tail = 0;
            int i = h;
            int mask = deque.length - 1;
            do {
                deque[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
        size=0;
        //press();
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = head;
            private int cursor = -1;
            private int tail1 = tail;

            @Override
            public boolean hasNext() {
                return index != tail1;
            }

            @Override
            public E next() throws ConcurrentModificationException, NoSuchElementException {
                if (index == tail1) {
                    throw new NoSuchElementException();
                }
                E result = deque[index];
                if (tail1 != tail || result == null) {
                    throw new ConcurrentModificationException();
                }
                cursor = index;
                index = (index + 1) & (deque.length - 1);
                return result;
            }

            @Override
            public void remove() {
                if (cursor < 0)
                    throw new IllegalStateException();
                if (delete(cursor)) {
                    cursor = (cursor - 1) & (deque.length - 1);
                    tail1 = tail;
                }
                size--;
                cursor = -1;
            }
        };
    }

}
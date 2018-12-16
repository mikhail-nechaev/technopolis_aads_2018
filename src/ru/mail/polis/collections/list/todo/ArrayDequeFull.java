package ru.mail.polis.collections.list.todo;

import java.util.*;

@SuppressWarnings("ALL")
public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {

    Object[] elements;
    int head = 0, tail = 0;

    public ArrayDequeFull() {
        elements = new Object[16];
    }

    public ArrayDequeFull(int count) throws Exception {
        if (count < 1) {
            throw new Exception("");
        }
        if (count == Integer.MAX_VALUE) {
            elements = new Object[Integer.MAX_VALUE];
        } else {
            elements = new Object[count + 1];
        }
    }

    public ArrayDequeFull(Collection<? extends E> c) throws Exception {
        this(c.size());
        addAll(c);
    }


    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if ((head - tail == 1 && tail < head) ||
                (head == 0 && tail == elements.length - 1) || elements.length == 0) {
            increase(elements.length == 0 ? 16 : elements.length);
        }
        if (head <= 0 && elements[0] != null) {
            head = elements.length - 1;
        }
        elements[elements[head] == null ? head : --head] = value;
    }

    @Override
    public E removeFirst() {
        E item = pollFirst();
        if (item == null) {
            throw new NoSuchElementException();
        }
        return item;
    }


    @Override
    public void addLast(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if ((head - tail == 1 && tail < head) ||
                (head == 0 && tail == elements.length - 1) || elements.length == 0) {
            increase(elements.length == 0 ? 16 : elements.length);
        }
        elements[elements[tail] == null ? tail : (tail == elements.length - 1 ? tail = 0 : ++tail)] = value;
    }

    public void increase(int value) {
        int growValue = elements.length + value;
        if (elements.length == Integer.MAX_VALUE) {
            System.out.println("Can't grow any more!");
            return;
        }
        if (((long) growValue) > Integer.MAX_VALUE) {
            growValue = Integer.MAX_VALUE;
        }

        Object[] tmp = new Object[growValue];
        if (tail < head) {
            System.arraycopy(elements, head, tmp, 0, elements.length - head);
            System.arraycopy(elements, 0, tmp, elements.length - head, tail + 1);
        } else {
            System.arraycopy(elements, 0, tmp, 0, elements.length);
        }
        if (elements.length != 0)
            tail = tmp.length / 2 - 1;
        elements = tmp;
        head = 0;

    }

    @Override
    public E removeLast() {
        E item = pollLast();
        if (item == null) {
            throw new NoSuchElementException();
        }
        return item;
    }


    @Override
    public int size() {
        if (elements.length == 0 || head == tail && elements[head] == null) {
            return 0;
        }
        if (head > tail) {
            return elements.length - head + tail + 1;
        }
        return tail + 1;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        elements = new Object[0];
        head = tail = 0;
    }

    @Override
    public ListIterator<E> iterator() {
        return new DeqListIterator();
    }

    @Override
    public E pollFirst() {
        if (isEmpty())
            return null;
        E item = (E) elements[head++];
        elements[head - 1] = null;
        if (head - 1 == tail)
            head = tail;
        if (head == elements.length) {
            head = 0;
        }
        return item;
    }

    @Override
    public E pollLast() {
        if (isEmpty())
            return null;
        E item = (E) elements[tail--];
        elements[tail + 1] = null;
        if (elements[head] == null)
            tail = head;
        else if (tail == -1) {
            tail = elements.length - 1;
        }
        return item;
    }

    @Override
    public E getFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        return (E) elements[head];
    }

    @Override
    public E getLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        return (E) elements[tail];
    }

    @Override
    public E peekFirst() {
        if (isEmpty())
            return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (isEmpty())
            return null;
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (isEmpty())
            return false;
        for (int i = head; i != tail + 1; i++) {
            if (elements[i].equals(o)) {
                delete(i);
                return true;
            }
            if (i == tail) {
                return false;
            }
            if (i == elements.length - 1) {
                i = -1;
            }
        }
        return false;
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

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (isEmpty())
            return false;
        for (int i = tail; i != head - 1; i--) {
            if (elements[i].equals(o)) {
                delete(i);
                return true;
            }
            if (i == head) {
                return false;
            }
            if (i == 0) {
                i = elements.length;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        if (isEmpty())
            return null;
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c);
        int s = size();
        c.forEach(this::addLast);
        return size() > s;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (Object element :
                c) {
            while (contains(element)) {
                removeFirstOccurrence(element);
            }

        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != null) {
                if (!c.contains(elements[i])) {
                    delete(i);
                    i--;
                }
            }
        }
        return true;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (Object element :
                c) {
            if (!contains(element)) {
                return false;
            }

        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (isEmpty())
            return false;
        E e = (E) o;
        if (e != null) {
            for (int i = head; i != tail + 1; i++) {
                if (elements[i].equals(e)) {
                    return true;
                }
                if (i == tail) {
                    return false;
                }
                if (i == elements.length - 1) {
                    i = -1;
                }
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        return elements;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) elements;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DeqIterator();
    }

    private class DeqIterator implements Iterator {

        int cursor;

        int remaining = size();

        public DeqIterator() {
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
            remaining--;
            return element;
        }
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
    //todo: remove <abstract> modifier and implement
}

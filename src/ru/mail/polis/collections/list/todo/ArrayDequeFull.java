package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;


public class ArrayDequeFull<E> extends ArrayDequeSimple<E> implements Deque<E> {
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
    public E pollFirst() {
        E value = (E) elements[headIndex];
        if (value == null) {
            return null;
        } else {
            elements[headIndex] = null;
            if (headIndex != tailIndex) {
                headIndex = (headIndex + 1) & (elements.length - 1);
            }
            modCount++;
            return value;
        }
    }

    @Override
    public E pollLast() {
        E value = (E) elements[tailIndex];
        if (value == null) {
            return null;
        } else {
            elements[tailIndex] = null;
            if (headIndex != tailIndex) {
                tailIndex = (tailIndex - 1) & (elements.length - 1);
            }
            modCount++;
            return value;
        }
    }

    @Override
    public E peekFirst() {
        E value = (E) elements[headIndex];
        if (value == null) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public E peekLast() {
        E value = (E) elements[tailIndex];
        if (value == null) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException("Value must be not null");
        }
        for (int i = headIndex; elements[i] != null; i = (i + 1) & (elements.length - 1)) {
            if (Objects.equals((E) o, elements[i])) {
                for (int j = i; j != tailIndex; j = (j + 1) & (elements.length - 1)) {
                    elements[j] = elements[(j + 1) & (elements.length - 1)];
                }
                elements[tailIndex] = null;
                if (headIndex != tailIndex) {
                    tailIndex = (tailIndex - 1) & (elements.length - 1);
                }
                modCount++;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException("Value must be not null");
        }
        for (int i = tailIndex; elements[i] != null; i = (i - 1) & (elements.length - 1)) {
            if (Objects.equals((E) o, elements[i])) {
                for (int j = i; j != tailIndex; j = (j + 1) & (elements.length - 1)) {
                    elements[j] = elements[(j + 1) & (elements.length - 1)];
                }
                elements[tailIndex] = null;
                if (headIndex != tailIndex) {
                    tailIndex = (tailIndex - 1) & (elements.length - 1);
                }
                modCount++;
                return true;
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
        return peekFirst();
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
        return removeFirstOccurrence((E) o);
    }

    @Override
    public boolean contains(Object o) {
        return super.contains((E) o);
    }

    @Override
    public Iterator<E> descendingIterator() {

        ListIterator<E> listIterator = new ListIterator<E>() {

            private int nextIndex = tailIndex;
            private int expModCount = modCount;
            private int lastReturnIndex = -1;

            final void checkComod() {
                if (modCount != expModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                if (elements[nextIndex] != null) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public E next() {
                checkComod();
                if (hasNext()) {
                    lastReturnIndex = nextIndex;
                    nextIndex = (nextIndex - 1) & (elements.length - 1);
                    return (E) elements[lastReturnIndex];
                } else {
                    throw new NoSuchElementException("There is no more elements");
                }
            }

            @Override
            public boolean hasPrevious() {
                if (elements[(nextIndex + 1) & (elements.length - 1)] != null) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public E previous() {
                checkComod();
                if (hasPrevious()) {
                    lastReturnIndex = (nextIndex + 1) & (elements.length - 1);
                    nextIndex = lastReturnIndex;
                    return (E) elements[lastReturnIndex];
                } else {
                    throw new NoSuchElementException("There is no more elements");
                }
            }

            @Override
            public int nextIndex() {
                if (hasNext()) {
                    return (nextIndex);
                } else {
                    return size();
                }
            }

            @Override
            public int previousIndex() {
                if (hasPrevious()) {
                    return (nextIndex + 1) & (elements.length - 1);
                } else {
                    return -1;
                }
            }

            @Override
            public void remove() {
                checkComod();
                if (lastReturnIndex == -1) {
                    throw new IllegalStateException();
                }
                for (int i = lastReturnIndex; i != tailIndex; i = (i + 1) & (elements.length - 1)) {
                    elements[i] = elements[(i + 1) & (elements.length - 1)];
                }
                elements[tailIndex] = null;
                lastReturnIndex = -1;
                if (headIndex != tailIndex) {
                    tailIndex = (tailIndex - 1) & (elements.length - 1);
                }
                nextIndex = (nextIndex + 1) & (elements.length - 1);
            }

            @Override
            public void set(E e) {
                checkComod();
                if (lastReturnIndex == -1) {
                    throw new IllegalStateException();
                }
                if (e == null) {
                    throw new NullPointerException("Value must be not null");
                }
                elements[lastReturnIndex] = e;
            }

            @Override
            public void add(E e) {
                checkComod();
                lastReturnIndex = -1;
                for (int i = tailIndex; i != (nextIndex + 1); i = (i - 1) & (elements.length - 1)) {
                    elements[(i + 1) & (elements.length - 1)] = elements[i];
                }
                elements[nextIndex] = e;
                tailIndex = (tailIndex + 1) & (elements.length - 1);
                nextIndex = (nextIndex - 1) & (elements.length - 1);
                if (((headIndex - 1) & (elements.length - 1)) == tailIndex) {
                    int oldHeadIndex = headIndex;
                    int oldLength = elements.length;
                    increaseCapacity();
                    nextIndex = (nextIndex - oldHeadIndex) & (oldLength - 1);
                }
            }
        };

        return listIterator;

    }

    @Override
    public Object[] toArray() {
        Object[] newElements = new Object[size()];
        if (headIndex + size() > elements.length) {
            int numRight = elements.length - headIndex;
            System.arraycopy(this.elements, headIndex, newElements, 0, numRight);
            System.arraycopy(this.elements, 0, newElements, numRight, headIndex);
        } else {
            System.arraycopy(this.elements, headIndex, newElements, 0, size());
        }
        return newElements;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int numRight = elements.length - headIndex;
        if (a.length > size()) {
            System.arraycopy(this.elements, headIndex, a, 0, numRight);
            System.arraycopy(this.elements, 0, a, numRight, headIndex);
            a[size()] = null;
        } else {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size());
            System.arraycopy(this.elements, headIndex, a, 0, numRight);
            System.arraycopy(this.elements, 0, a, numRight, headIndex);
            a[size()] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E o : c) {
            if (add(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] obj = c.toArray();
        for (int i = 0; i < obj.length; i++) {
            if (removeFirstOccurrence((E) obj[i])) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] obj = c.toArray();
        for (int i = headIndex; i != (tailIndex + 1); i = (i + 1) & (elements.length - 1)) {
            if (!c.contains(elements[i])) {
                removeFirstOccurrence(elements[i]);
                i--;
                modified = true;
            }
        }
        return modified;
    }
}

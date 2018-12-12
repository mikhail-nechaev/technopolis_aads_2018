package ru.mail.polis.collections.list.todo;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E> {
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
        if (size == 0) {
            return null;
        } else {
            Node temp = head;
            head = temp.next;
            if (head != null) {
                head.prev = null;
            }
            size--;
            modCount++;
            return (E) temp.value;
        }
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        } else {
            Node<E> temp = tail;
            tail = temp.prev;
            if (tail != null) {
                tail.next = null;
            }
            size--;
            modCount++;
            return (E) temp.value;
        }
    }

    @Override
    public E peekFirst() {
        if (size == 0) {
            return null;
        } else {
            return head.value;
        }
    }

    @Override
    public E peekLast() {
        if (size == 0) {
            return null;
        } else {
            return tail.value;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            throw new NullPointerException("Value must be not null");
        }
        for (Node<E> i = head; i != null; i = i.next) {
            if (Objects.equals((E) i.value, (E) o)) {
                if (i != head) {
                    i.prev.next = i.next;
                }
                if (i != tail) {
                    i.next.prev = i.prev;
                }
                size--;
                modCount++;
                i = null;
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
        for (Node<E> i = tail; i != null; i = i.prev) {
            if (Objects.equals((E) i.value, (E) o)) {
                if (i != head) {
                    i.prev.next = i.next;
                }
                if (i != tail) {
                    i.next.prev = i.prev;
                }
                size--;
                modCount++;
                i = null;
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

            Node<E> currentNode = new Node<E>(null, null, tail);
            Node<E> lastReturnNode = null;
            int nextIdx = size - 1;
            int expModCount = modCount;


            final void checkComod() {
                if (modCount != expModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                return (nextIdx > -1);
            }

            @Override
            public E next() {
                checkComod();
                if (hasNext()) {
                    currentNode = currentNode.prev;
                    nextIdx--;
                    lastReturnNode = currentNode;
                    return currentNode.value;
                } else {
                    throw new NoSuchElementException("There is no more elements");
                }
            }

            @Override
            public boolean hasPrevious() {
                return (nextIdx + 1 < size);
            }

            @Override
            public E previous() {
                checkComod();
                if (hasPrevious()) {
                    currentNode = currentNode.next;
                    nextIdx++;
                    lastReturnNode = currentNode.prev;
                    return (E) currentNode.prev.value;
                } else {
                    throw new NoSuchElementException("There is no more elements");
                }
            }

            @Override
            public int nextIndex() {
                return nextIdx;
            }

            @Override
            public int previousIndex() {
                return nextIdx + 1;
            }

            @Override
            public void remove() {
                checkComod();
                if (lastReturnNode == null) {
                    throw new IllegalStateException();
                }
                Node<E> next = lastReturnNode.next;
                Node<E> prev = lastReturnNode.prev;
                if (prev == null) {
                    head = next;
                } else {
                    prev.next = next;
                }

                if (next == null) {
                    tail = prev;
                } else {
                    next.prev = prev;
                }
                if (currentNode.next == null) {
                    currentNode = new Node<E>(null, null, tail);
                } else {
                    currentNode = currentNode.next;
                }
                lastReturnNode = null;
                nextIdx--;
                size--;
            }

            @Override
            public void set(E e) {
                checkComod();
                if (lastReturnNode == null) {
                    throw new IllegalStateException();
                }
                lastReturnNode.value = e;
            }

            @Override
            public void add(E e) {
                checkComod();
                lastReturnNode = null;
                Node<E> temp = new Node<E>(e, currentNode.next, currentNode);
                currentNode.next.prev = temp;
                currentNode.next = temp;
                nextIdx++;
                size++;
            }
        };

        return listIterator;
    }

    @Override
    public Object[] toArray() {
        Object[] newElements = new Object[size];
        int count = 0;
        for (Node<E> i = head; i != null; i = i.next) {
            newElements[count++] = i.value;
        }
        return newElements;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int count = 0;
        Object[] newElements = a;
        for (Node<E> i = head; i != null; i = i.next) {
            newElements[count++] = i.value;
        }
        if (a.length > size) {
            a[size] = null;
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
        for (Node<E> i = head; i != null; i = i.next) {
            if (!c.contains(i.value)) {
                i = i.prev;
                removeFirstOccurrence(i.value);
                modified = true;
            }
        }
        return modified;
    }
}

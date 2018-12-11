package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    protected int size, modCount;
    protected Node head, tail;

    public LinkedDequeSimple() {
        size = modCount = 0;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Node x = head; x != null; x = x.right) {
            s.append(x.data.toString());
        }

        return s.toString();
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

        if (isEmpty()) {
            head = tail = new Node();
            head.data = value;
        } else {
            head.left = new Node();
            Node oldHead = head;
            head = head.left;
            head.right = oldHead;
            head.data = value;
        }
        size++;
        modCount++;
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
            throw new NoSuchElementException("Deque is empty");
        }

        E answer = head.data;
        size--;
        modCount++;
        if (isEmpty()) {
            head = tail = null;
        } else {
            head = head.right;
            head.left = null;
        }

        return answer;
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
            throw new NoSuchElementException("Deque is empty");
        }
        return head.data;
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

        if (isEmpty()) {
            head = tail = new Node();
            head.data = value;
        } else {
            tail.right = new Node();
            Node oldTail = tail;
            tail = tail.right;
            tail.left = oldTail;
            tail.data = value;
        }
        size++;
        modCount++;
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
            throw new NoSuchElementException("Deque is empty");
        }
        E answer = tail.data;
        size--;
        modCount++;
        if (isEmpty()) {
            tail = head = null;
        } else {
            tail = tail.left;
            tail.right = null;
        }

        return answer;
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
            throw new NoSuchElementException("Deque is empty");
        }
        return tail.data;
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

        return indexOf(value) > -1;
    }


    protected int indexOf(Object o) {


//        Iterator<E> iterator = iterator();
//        int i = 0;
//        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next(), i++) {
//            if (elem.equals(o)) {
//                return i;
//            }
//            if (!iterator.hasNext()) {
//                break;
//            }
//        }

        int i = 0;
        Node elem = head;
        while (elem != null) {
            if (elem.data.equals(o)) {
                return i;
            }
            i++;
            elem = elem.right;
        }
        return -1;
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
        head = tail = null;
        size = 0;
    }

    protected boolean removeNode(Node elem) {
        if (elem == null) {
            throw new NullPointerException();
        }
        if (elem == tail) {
            removeLast();
        } else if (elem == head) {
            removeFirst();
        } else {
            elem.left.right = elem.right;
            elem.right.left = elem.left;
            size--;
        }
        return true;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node lastReturned, nextElem = head;

            @Override
            public boolean hasNext() {
                return nextElem != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                lastReturned = nextElem;
                nextElem = nextElem.right;
                return lastReturned.data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }

                LinkedDequeSimple.this.removeNode(lastReturned);
                lastReturned = null;
            }
        };
    }


   /* @SuppressWarnings("unchecked")
    @Override
    public ListIterator<E> iterator() {
        return new ListIterator<E>() {
            private int iteratorSize = size, nextIndex = 0;
            private Node nextElem = head;
            private Node nowElem;
            private boolean isBegin = true;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                nowElem = nextElem;
                nextIndex++;
                nextElem = nextElem.right;
                return nowElem.data;
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
                nowElem = nextElem = (nextElem == null) ? tail : nextElem.left;
                nextIndex--;
                return nowElem.data;
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
                if (nowElem == null) {
                    throw new IllegalStateException();
                }
                Node left = nowElem.left;
                Node right = nextElem.right;
                if (left != null) {
                    left.right = right;
                }
                if (right != null) {
                    right.left = left;
                }
                nowElem = null;

                modCount++;
            }

            @Override
            public void set(Object o) {
                nowElem.data = (E) o;
                modCount++;
            }

            @Override
            public void add(Object o) {
                Node right = nowElem.right;
                Node mid = new Node();
                mid.data = (E) o;
                mid.left = nowElem;
                mid.right = right;
                right.left = mid;
                nowElem.right = mid;

                nextIndex++;
                modCount++;
            }
        };
    }*/


    class Node {
        Node left, right;
        E data;
    }

}

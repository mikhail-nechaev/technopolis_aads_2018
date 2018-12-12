package ru.mail.polis.collections.list.todo;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import ru.mail.polis.collections.list.IDeque;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    protected class Node<E> {
        E value;
        Node next;
        Node prev;

        public Node(E value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    protected Node<E> head;
    protected Node<E> tail;
    protected int size;
    protected int modCount;


    public LinkedDequeSimple() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    private int indexOf(E value) {
        int index = 0;
        for (Node<E> i = head; i != null; i = i.next) {
            if (Objects.equals(value, i.value)) {
                return index;
            }
            index++;
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
            throw new NullPointerException("Value must be not null");
        } else {
            if (size == 0) {
                head = new Node<E>(value, null, null);
                tail = head;
            } else {
                Node<E> node = new Node<E>(value, this.head, null);
                head.prev = node;
                node.next = head;
                head = node;
            }
            size++;
            modCount++;
        }
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
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

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            return (E) head.value;
        }
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
            throw new NullPointerException("Value must be not null");
        } else {
            if (size == 0) {
                tail = new Node<E>(value, null, null);
                head = tail;
            } else {
                Node<E> node = new Node<E>(value, null, tail);
                tail.next = node;
                node.prev = tail;
                tail = node;
            }
            size++;
            modCount++;
        }
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
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

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            return (E) tail.value;
        }
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
        } else {
            return (indexOf(value) != -1);
        }
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
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public Iterator<E> iterator() {

        ListIterator<E> listIterator = new ListIterator<E>() {

            Node<E> currentNode = new Node<E>(null, head, null);
            Node<E> lastReturnNode = null;
            int nextIdx = 0;
            int expModCount = modCount;


            final void checkComod() {
                if (modCount != expModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                return (nextIdx < size);
            }

            @Override
            public E next() {
                checkComod();
                if (hasNext()) {
                    currentNode = currentNode.next;
                    nextIdx++;
                    lastReturnNode = currentNode;
                    return currentNode.value;
                } else {
                    throw new NoSuchElementException("There is no more elements");
                }
            }

            @Override
            public boolean hasPrevious() {
                return (nextIdx - 1 > -1);
            }

            @Override
            public E previous() {
                checkComod();
                if (hasPrevious()) {
                    currentNode = currentNode.prev;
                    nextIdx--;
                    lastReturnNode = currentNode.next;
                    return (E) currentNode.next.value;
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
                return nextIdx - 1;
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
                if (currentNode.prev == null) {
                    currentNode = new Node<E>(null, head, null);
                } else {
                    currentNode = currentNode.prev;
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
                Node<E> temp = new Node<E>(e, currentNode.next, currentNode);
                currentNode.next.prev = temp;
                currentNode.next = temp;
                nextIdx++;
                size++;
            }
        };

        return listIterator;
    }
}

package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E> {

    class Node<E> {
        private E value;
        private Node next = null;
        private Node previous = null;

        public Node(E e) {
            value = e;
        }

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrevious() {
            return previous;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }
    }
    //    last ... first
    //previous ... next
    private Node<E> first = null;
    private Node<E> last = null;
    private int size = 0;

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
        if (size == 0) {
            first = last = new Node<>(value);
            size++;
        } else {
            first.setNext(new Node<>(value));
            first = first.getNext();
            size++;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E e = first.getValue();
        first = first.getPrevious();
        first.setNext(null);
        size--;
        return e;
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
        return first.getValue();
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
        if (size == 0) {
            first = last = new Node<>(value);
            size++;
        } else {
            last.setPrevious(new Node<>(value));
            last = last.getPrevious();
            size++;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E e = first.getValue();
        first = first.getNext();
        first.setPrevious(null);
        size--;
        return e;    }

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
        return last.getValue();
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
        ListIterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == value) {
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
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public ListIterator<E> iterator() {
        return new ListIterator<E>() {
            Node<E> position = first;
            boolean isStart = true;
            int size = size();
            int cursor = 0;

            // set(), add(), remove() => true
            // next(), previous() => false
            private boolean doOperation = true;

            //first ... last
            //prev  ... next
            @Override
            public boolean hasNext() {
                if (isStart) {
                    return position != null;
                }
                return position.getPrevious() != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (isStart) {
                    isStart = false;
                } else {
                    position = position.getPrevious();
                }
                doOperation = false;
                cursor++;
                return position.getValue();
            }

            @Override
            public boolean hasPrevious() {
                return position.getNext() != null;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                doOperation = false;
                cursor--;
                position = position.getNext();
                return position.getValue();
            }

            @Override
            public int nextIndex() {
                return cursor + 1 < size ? cursor + 1 : size;
            }

            @Override
            public int previousIndex() {
                return cursor - 1 >= 0 ? cursor - 1 : -1;
            }

            @Override
            public void remove() {
                position.previous.setNext(position.next);
                position.next.setPrevious(position.previous);
                size--;
                doOperation = true;
                Node<E> node = position.next;
                position.next = null;
                position.previous = null;
                position = node;
            }

            @Override
            public void set(E e) {
                doOperation = true;
                position.setValue(e);
            }

            @Override
            public void add(E e) {
                Node<E> node = new Node<>(e);
                if (hasPrevious()) {
                    node.setNext(position.next);
                    position.next.setPrevious(node);
                }
                node.setPrevious(position);
                position.setNext(node);
                size++;
                doOperation = true;
                position = node;
            }
        };
    }
}

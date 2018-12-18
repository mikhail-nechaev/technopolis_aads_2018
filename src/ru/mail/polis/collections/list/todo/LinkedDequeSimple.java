package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IDeque;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Linked list implementation of the {@link IDeque} interface with no capacity restrictions.
 *
 * @param <E> the type of elements held in this deque
 */
public class LinkedDequeSimple<E> implements IDeque<E>
{

    class Node
    {
        Node()
        {
            left = null;
            right = null;
        }

        E data;
        Node left, right;
    }

    protected int size, modCount;
    protected Node head, tail;


    public LinkedDequeSimple()
    {
        modCount = 0;
        size = 0;
    }

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value)
    {
        if (isEmpty())
        {
            head = tail = new Node();
            head.data = Objects.requireNonNull(value);
        } else
        {
            head.left = new Node();
            Node prevNode = head;
            head = head.left;
            head.data = Objects.requireNonNull(value);
            head.right = prevNode;
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
    public E removeFirst()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }

        E value = head.data;
        size--;
        modCount++;
        if (isEmpty())
        {
            head = tail = null;
        } else
        {
            head = head.right;
            head.left = null;
        }


        return value;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
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
    public void addLast(E value)
    {
        if (isEmpty())
        {
            head = tail = new Node();
            tail.data = Objects.requireNonNull(value);
        } else
        {
            tail.right = new Node();
            Node prevNode = tail;
            tail = tail.right;
            tail.data = Objects.requireNonNull(value);
            tail.left = prevNode;
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
    public E removeLast()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }

        E value = tail.data;
        size--;
        modCount++;
        if (isEmpty())
        {
            head = tail = null;
        } else
        {
            tail = tail.left;
            tail.right = null;
        }

        return value;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
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
    public boolean contains(Object value)
    {
        Objects.requireNonNull(value);
        if (isEmpty())
        {
            return false;
        } else
        {
            Node current = head;
            while (current != null)
            {
                if (current.data.equals(value))
                {
                    return true;
                }
                current = current.right;
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
    public int size()
    {
        return size;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear()
    {
        head = tail = null;
        size = 0;
        modCount++;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */

    /** Не получается вернуть ListIterator */
    @Override
    public Iterator<E> iterator()
    {

        return new Iterator<E>()
        {
            private Node prev, nextNode = head;

            @Override
            public boolean hasNext()
            {
                return nextNode != null;
            }

            @Override
            public E next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                prev = nextNode;
                nextNode = nextNode.right;
                return prev.data;
            }

            @Override
            public void remove() {
                if (prev == null || (head == tail && head == null))
                {
                    throw new IllegalStateException();
                }

                if (prev.equals(head))
                {
                    removeFirst();
                }
                else
                {
                    if (prev.equals(tail))
                    {
                        removeLast();
                    }
                    else
                    {
                        prev.right.left = prev.left;
                        prev.left.right = prev.right;
                        size--;
                        modCount++;
                    }
                }
            }
        };
    }
}

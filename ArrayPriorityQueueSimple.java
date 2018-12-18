package ru.mail.polis.collections.list.todo;

import ru.mail.polis.collections.list.IPriorityQueue;

import java.util.*;

/**
 * Resizable array implementation of the {@link IPriorityQueue} interface based on a priority heap.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements maintained by this priority queue
 */
public class ArrayPriorityQueueSimple<E extends Comparable<E>> implements IPriorityQueue<E> {

    private final Comparator<E> comparator;
    private final int CAPACITY = 10;
    private Object[] data;
    int size;

    public ArrayPriorityQueueSimple() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @throws NullPointerException if the specified collection is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection) {
        this(collection, Comparator.naturalOrder());
    }

    /**
     * Creates a {@code IPriorityQueue} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public ArrayPriorityQueueSimple(Comparator<E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        data = new Object[CAPACITY];
        size = 0;
    }

    /**
     * Creates a {@code IPriorityQueue} containing the elements in the specified collection
     *  that orders its elements according to the specified comparator.
     *
     * Complexity = O(n)
     *
     * @param collection the collection whose elements are to be placed into this priority queue
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified collection or comparator is null
     */
    public ArrayPriorityQueueSimple(Collection<E> collection, Comparator<E> comparator)
    {
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        Objects.requireNonNull(collection, "collection");
        data = new Object[CAPACITY];
        size = 0;
        collection.forEach(this::add);
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * Complexity = O(log(n))
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E value) {
        data[size] = Objects.requireNonNull(value);
        siftUp(size);
        ++size;
        if (size == data.length)
        {
            data = Arrays.copyOf(data, data.length + 10);
        }
    }

    /**
     * Retrieves and removes the head of this queue.
     *
     * Complexity = O(log(n))
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E remove() {
        E head;
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }
        else
        {
            head = element();
            --size;
            data[0] = data[size];
            data[size] = null;
            siftDown(0);
            if (size < data.length - 10)
            {
                data = Arrays.copyOf(data, data.length - 10);
            }
        }
        return head;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * Complexity = O(1)
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public E element() {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }
        else
        {
            return (E)data[0];
        }
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * Complexity = O(n)
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(E value) {
        if (value == null)
        {
            throw new NullPointerException();
        }
        else
        {
            for(Object o: data)
            {
                if(o.equals(value))
                {
                    return true;
                }
            }
            return false;
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
        return size == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        data = Arrays.copyOf(data, 0);
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this queue. The iterator
     * does not return the elements in any particular order.
     *
     * @return an iterator over the elements in this queue
     */
    @Override
    public Iterator<E> iterator() {
      return new Iterator<E>()
      {
          int current = 0;
          boolean rm = false;

          @Override
          public boolean hasNext()
          {
              if (current < size - 1)
              {
                  return true;
              }
              else
              {
                  return false;
              }
          }

          @Override
          public E next()
          {
              if (hasCurrent())
              {
                  current++;
                  rm = true;
                  return (E) data[current - 1];
              }
              else
              {
                  throw new NoSuchElementException();
              }
          }

          @Override
          public void remove()
          {
              if (!rm)
              {
                  throw new IllegalStateException();
              }
              --size;
              data[current - 1] = data[size];
              data[size] = null;
              siftDown(current - 1);
              rm = false;
          }

          private boolean hasCurrent()
          {
              if (current < size)
              {
                  return true;
              }
              else
              {
                  return false;
              }
          }
      };

    }

    /**
     * Code from "Хипуй"
     * @param k
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int k)
    {
        int child_1, child_2;
        if (k * 2 + 1 < data.length && data[k * 2 + 1] != null)
        {
            child_1 = k * 2 + 1;
        }
        else
        {
            child_1 = -1;
        }

        if (k * 2 + 2 < data.length && data[k * 2 + 2] != null)
        {
            child_2 = k * 2 + 2;
        }
        else
        {
            child_2 = -1;
        }

        if (child_1 == -1 && child_2 == -1)
        {
            return;
        }

        if (child_1 == -1)
        {
            if (compare(data[k], data[child_2]) > 0)
            {
                E tmp = (E)data[k];
                data[k] = data[child_2];
                data[child_2] = tmp;
                siftDown(child_2);
            }
            return;
        }

        if (child_2 == -1)
        {
            if (compare(data[k], data[child_1]) > 0)
            {
                E tmp = (E)data[k];
                data[k] = data[child_1];
                data[child_1] = tmp;
                siftDown(child_1);
            }
            return;
        }

        int child_min = compare(data[child_1], data[child_2]) > 0 ? child_2 : child_1;
        if (compare(data[k], data[child_min]) > 0)
        {
            E tmp = (E)data[k];
            data[k] = data[child_min];
            data[child_min] = tmp;
            siftDown(child_min);
        }
    }

    @SuppressWarnings("unckecked")
    private void siftUp(int k)
    {
        if (k == 0)
        {
            return;
        }
        int parent = k % 2 == 0 ? (k - 2) / 2 : (k - 1) / 2;
        if (compare(data[k], data[parent]) < 0)
        {
            E tmp = (E)data[k];
            data[k] = data[parent];
            data[parent] = tmp;
            siftUp(parent);
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(Object o1, Object o2)
    {
        return comparator.compare((E) o1, (E) o2);
    }
}

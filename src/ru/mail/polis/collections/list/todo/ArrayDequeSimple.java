package ru.mail.polis.collections.list.todo;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import ru.mail.polis.collections.list.IDeque;

/**
 * Resizable cyclic array implementation of the {@link IDeque} interface.
 * - no capacity restrictions
 * - grow as necessary to support
 *
 * @param <E> the type of elements held in this deque
 */
public class ArrayDequeSimple<E> implements IDeque<E> {

    E[] arr = (E[]) new Object[10];
    int head = 0;
    int tail = 0;
    private int elementsCount = 0;

    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param value the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addFirst(E value) {
        if (value == null) {
            throw new NullPointerException("Element is null.");
        }
        if (elementsCount == arr.length) {
            increasearr();
        }
        if (elementsCount == 0) {
            arr[head] = value;
        } else if (head == 0) {
            head = arr.length - 1;
            arr[head] = value;
        } else {
            head--;
            arr[head] = value;
        }
        elementsCount++;
    }

    private void increasearr() {
        E[] newarr = (E[]) new Object[elementsCount * 2];
        int i = 0;
        while (head != tail) {
            newarr[i] = arr[head];
            if (head == arr.length - 1) {
                head = 0;
            } else {
                head++;
            }
            i++;
        }
        newarr[i] = arr[head];
        arr = newarr;
        head = 0;
        tail = elementsCount - 1;
    }

    /**
     * Retrieves and removes the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        if (elementsCount == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        E element = arr[head];
        arr[head] = null;
        if (head != tail) {
            if (head == arr.length - 1) {
                head = 0;
            } else {
                head++;
            }
        }
        elementsCount--;
        return element;
    }

    /**
     * Retrieves, but does not remove, the first element of this queue.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E getFirst() {
        if (elementsCount == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        return arr[head];
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
            throw new NullPointerException("Element is null.");
        }
        if (elementsCount == arr.length) {
            increasearr();
        }
        if (elementsCount == 0) {
            arr[tail] = value;
        } else if (tail == arr.length - 1) {
            tail = 0;
            arr[tail] = value;
        } else {
            tail++;
            arr[tail] = value;
        }
        elementsCount++;
    }

    /**
     * Retrieves and removes the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        if (elementsCount == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        E element = arr[tail];
        arr[tail] = null;
        if (head != tail) {
            if (tail == 0) {
                tail = arr.length - 1;
            } else {
                tail--;
            }
        }
        elementsCount--;
        return element;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        if (elementsCount == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        return arr[tail];
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
        for (E i : this) {
            if (i.equals(value)) {
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
        return elementsCount;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return elementsCount == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        arr = (E[]) new Object[10];
        head = 0;
        tail = 0;
        elementsCount = 0;
    }

    int prevIndexInner(int index) {
        return (index == 0) ? arr.length - 1 : index - 1;
    }

    int nextIndexInner(int index) {
        return index == arr.length - 1 ? 0 : index + 1;
    }

    /**
     * Returns an iterator over the elements in this collection in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this collection in proper sequence
     */
    @Override
    public ListIterator<E> iterator() {
        return new ListIterator<>() {
            int currentIndex = head;
            int deleteIndex = -1;
            boolean hasElement = false;

            @Override
            public boolean hasNext() {
                if (elementsCount == 0) {
                    return false;
                }
                return currentIndex != -1;
            }

            private int nextIndexInner(int index) {
                return index == arr.length - 1 ? 0 : index + 1;
            }

            private int prevIndexInner(int index) {
                return (index == 0) ? arr.length - 1 : index - 1;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E elem = arr[currentIndex];
                if (elem == null) {
                    throw new NoSuchElementException();
                }
                if (currentIndex != head) {
                    deleteIndex = (deleteIndex == -1) ? head : nextIndexInner(deleteIndex);
                }
                currentIndex = (currentIndex == tail) ? -1 : nextIndexInner(currentIndex);
                hasElement = true;
                return elem;
            }

            @Override
            public boolean hasPrevious() {
                if (elementsCount == 0) {
                    return false;
                }
                return deleteIndex != -1;
            }

            @Override
            public E previous() {
                E elem = arr[deleteIndex];
                if (elem == null) {
                    throw new NoSuchElementException();
                }
                currentIndex = (currentIndex == -1) ? tail : prevIndexInner(currentIndex);
                deleteIndex = (deleteIndex == head) ? -1 : prevIndexInner(deleteIndex);
                hasElement = true;
                return elem;
            }

            @Override
            public int nextIndex() {
                return (currentIndex == -1) ? elementsCount : nextIndexInner(currentIndex);
            }

            @Override
            public int previousIndex() {
                return (deleteIndex == -1) ? -1 : nextIndexInner(deleteIndex);
            }

            @Override
            public void remove() {
                if (!hasElement) {
                    throw new IllegalStateException();
                }
                int currentIndexOld = currentIndex;
                int deleteIndexOld = deleteIndex;
                if (currentIndex != -1) {
                    while (currentIndex != tail) {
                        arr[prevIndexInner(currentIndex)] = arr[currentIndex];
                        next();
                    }
                    arr[prevIndexInner(currentIndex)] = arr[currentIndex];
                    arr[currentIndex] = null;
                    deleteIndex = deleteIndexOld;
                    currentIndex = prevIndexInner(currentIndexOld);
                    tail = prevIndexInner(tail);
                } else if (deleteIndex != -1) {
                    arr[nextIndexInner(deleteIndex)] = null;
                    tail = prevIndexInner(tail);
                } else if (elementsCount == 1) {
                    arr[head] = null;
                } else {
                    throw new IllegalStateException();
                }
                hasElement = false;
                elementsCount--;
            }

            @Override
            public void set(E e) {
                if (currentIndex != -1) {
                    arr[prevIndexInner(currentIndex)] = e;
                } else if (deleteIndex != -1) {
                    arr[nextIndexInner(deleteIndex)] = e;
                } else if (elementsCount == 1) {
                    arr[head] = e;
                } else {
                    throw new IllegalStateException();
                }
            }

            @Override
            public void add(E e) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        ArrayDequeSimple<Integer> arrayDequeSimple = new ArrayDequeSimple<>();
        arrayDequeSimple.addFirst(1);
        arrayDequeSimple.addLast(2);
        arrayDequeSimple.addFirst(0);
        arrayDequeSimple.addLast(3);
        arrayDequeSimple.addLast(4);
        System.out.println(arrayDequeSimple.removeFirst());
        System.out.println(arrayDequeSimple.removeLast());
        for (Integer i: arrayDequeSimple) {
            System.out.println(i);
        }
    }
}
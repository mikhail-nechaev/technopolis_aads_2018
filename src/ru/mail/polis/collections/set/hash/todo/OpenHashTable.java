package ru.mail.polis.collections.set.hash.todo;

import ru.mail.polis.collections.set.hash.IOpenHashTable;
import ru.mail.polis.collections.set.hash.IOpenHashTableEntity;

import java.util.Iterator;

/**
 * Implementation open addressed hash table with double hashing
 *
 * <a href="https://en.wikipedia.org/wiki/Double_hashing">Double_hashing</a>
 * <p>
 * Use {@link IOpenHashTableEntity#hashCode(int, int)} for hash code calculating
 * <p>
 * Use loadFactor = from 0.5f to 0.75f included
 *
 * @param <E> the type of elements maintained by this hash table
 */
public class OpenHashTable<E extends IOpenHashTableEntity> implements IOpenHashTable<E> {
    private Student[] hashArray;
    private static final int DEFAULT_SIZE = 1000000;
    private int size, modCount;

    public OpenHashTable() {
        this(DEFAULT_SIZE);
    }

    public OpenHashTable(int size) {
        hashArray = new Student[size];
        this.size = modCount = 0;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *                                  In other words if {@link IOpenHashTableEntity#hashCode(int, int)} specified element is incorrect.
     */
    @Override
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (!(value instanceof Student)) {
            throw new IllegalArgumentException();
        }

        Student student = (Student) value;
        int count = 0;
        int hashVal = student.hashCode(tableSize(), count);
        while (hashArray[hashVal] != null && !hashArray[hashVal].isDeleted()) {
            count++;
            hashVal = student.hashCode(tableSize(), count);
            if (count >= size) {
                return false;
            }
        }
        hashArray[hashVal] = student;
        size++;
        modCount++;
        return true;
    }

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *                                  In other words if {@link IOpenHashTableEntity#hashCode(int, int)} specified element is incorrect.
     */
    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (!(value instanceof Student)) {
            throw new IllegalArgumentException();
        }

        Student student = (Student) value;
        int count = 0;
        int hashVal = student.hashCode(tableSize(), count);
        while (hashArray[hashVal] != null && !hashArray[hashVal].isDeleted()) {
            if (hashArray[hashVal].equals(student)) {
                hashArray[hashVal].setDeleted(true);
                break;
            }
            hashVal = student.hashCode(tableSize(), count);
            count++;
            if (count >= size) {
                return false;
            }
        }
        hashArray[hashVal] = student;
        size--;
        modCount++;
        return true;

    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *                                  In other words if {@link IOpenHashTableEntity#hashCode(int, int)} specified element is incorrect.
     */
    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (!(value instanceof Student)) {
            throw new IllegalArgumentException();
        }

        Student student = (Student) value;

        int count = 0;
        int hashVal = student.hashCode(tableSize(), count);
        while (hashArray[hashVal] != null && !hashArray[hashVal].isDeleted() && !hashArray[hashVal].equals(student)) {
            count++;
            hashVal = student.hashCode(tableSize(), count);
            if (count >= size) {
                return false;
            }
        }

        return true;
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
        hashArray = new Student[DEFAULT_SIZE];
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @SuppressWarnings("unchecked")
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int count = size;
            int index = 0, realIndex = -1;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public E next() {

                for (int i = realIndex + 1; i < tableSize(); i++) {
                    if (hashArray[i] != null && !hashArray[i].isDeleted()) {
                        realIndex = i;
                        index++;
                        return (E) hashArray[realIndex];
                    }
                }

                return null;
            }
        };

    }

    @Override
    public int tableSize() {
        return hashArray.length;
    }
}

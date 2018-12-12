package ru.mail.polis.collections.set.hash.todo;

import ru.mail.polis.collections.set.hash.IOpenHashTable;
import ru.mail.polis.collections.set.hash.IOpenHashTableEntity;

import java.util.Iterator;

/**
 *
 * Implementation open addressed hash table with double hashing
 *
 * <a href="https://en.wikipedia.org/wiki/Double_hashing">Double_hashing</a>
 *
 * Use {@link IOpenHashTableEntity#hashCode(int,int)} for hash code calculating
 *
 * Use loadFactor = from 0.5f to 0.75f included
 *
 * @param <E> the type of elements maintained by this hash table
 */
public class OpenHashTable<E extends IOpenHashTableEntity> implements IOpenHashTable<E> {


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
        throw new UnsupportedOperationException("todo: implement this");
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
        throw new UnsupportedOperationException("todo: implement this");
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
    public boolean contains(Object value) {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("todo: implement this");
    }

    @Override
    public int tableSize() {
        throw new UnsupportedOperationException("todo: return dataArray.length");
    }
}

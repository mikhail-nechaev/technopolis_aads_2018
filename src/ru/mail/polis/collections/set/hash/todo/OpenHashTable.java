package ru.mail.polis.collections.set.hash.todo;

import ru.mail.polis.collections.set.hash.IOpenHashTable;
import ru.mail.polis.collections.set.hash.IOpenHashTableEntity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    private Object[] table;
    private boolean[] deleted;
    private final float loadFactor = 0.5f;
    private int size = 0;

    public OpenHashTable() {
        table = new Object[8];
        deleted = new boolean[8];
    }

    private void rehash() {
        Object[] oldTable = Arrays.copyOf(table, tableSize());
        table = new Object[tableSize() << 1];
        deleted = new boolean[tableSize() << 1];
        int oldSize = size;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && !deleted[i]) {
                add((E) oldTable[i]);
            }
        }
        size = oldSize;
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
        if (size >= loadFactor * tableSize()) {
            rehash();
        }

        for (int probeId = 0; probeId < tableSize(); probeId++) {
            int hash = value.hashCode(tableSize(), probeId);
            if (table[hash] == null || deleted[hash]) {
                size++;
                table[hash] = value;
                return true;
            }
            if (value.equals(table[hash])) {
                break;
            }
        }
        return false;
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

        for (int probeId = 0; probeId < tableSize(); probeId++) {
            int hash = value.hashCode(tableSize(), probeId);
            if (table[hash] == null) {
                break;
            }
            if (value.equals(table[hash]) && !deleted[hash]) {
                size--;
                deleted[hash] = true;
                return true;
            }
        }
        return false;
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
        for (int probeId = 0; probeId < tableSize(); probeId++) {
            int hash = value.hashCode(tableSize(), probeId);
            if (table[hash] == null) {
                break;
            }
            if (value.equals(table[hash]) && !deleted[hash]) {
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
        size = 0;
        table = new Object[8];
        deleted = new boolean[8];
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int nextIndex=0;
            private int lastNextIndex = -1;
            private int size = size();

            @Override
            public boolean hasNext() {
                return nextIndex<size;
            }

            @Override
            public E next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                while(table[nextIndex] == null || deleted[nextIndex]){
                    nextIndex++;
                }
                lastNextIndex = nextIndex;
                return (E)table[lastNextIndex];
            }

            @Override
            public void remove() {
                if (lastNextIndex < 0) {
                    throw new IllegalStateException();
                }
                size--;
                deleted[lastNextIndex] = true;
                lastNextIndex = -1;
            }
        };

    }

    @Override
    public int tableSize() {
        return table.length;
    }
}

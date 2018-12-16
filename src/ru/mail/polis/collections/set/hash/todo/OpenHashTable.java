package ru.mail.polis.collections.set.hash.todo;

import ru.mail.polis.collections.set.hash.IOpenHashTable;
import ru.mail.polis.collections.set.hash.IOpenHashTableEntity;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    private E [] table;
    private int size = 0;
    private boolean [] deleted;
    private int modCount = 0;

    public OpenHashTable() {
        table = (E [])new IOpenHashTableEntity[8];
        deleted = new boolean[8];
    }


    private void resize() {
        if(size() * 2 < tableSize()) {
            return;
        }
        E[] old = this.table;
        size = 0;
        int newLen = tableSize() << 1;
        table =(E[]) new IOpenHashTableEntity[newLen];
        deleted = new boolean[newLen];
        for(int i = 0; i < old.length; i++) {
            E node =  old[i];
            if(node != null) {
                old[i] = null;
                add(node);
            }
        }
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
        Objects.requireNonNull(value);
        int probId = 0;
        int hash = value.hashCode(tableSize(), probId++);
        if(table[hash] == null) {
            table[hash] = value;
        } else {
            while(table[hash] != null && !value.equals(table[hash]) && !deleted[hash]) {
                hash = value.hashCode(tableSize(), probId++);
            }
            if (value.equals(table[hash])) {
                return false;
            }
            deleted[hash] = false;
            table[hash] = value;
        }
        size++;
        resize();
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
        Objects.requireNonNull(value);
        int probId = 0;
        int hash = value.hashCode(tableSize(), probId++);
        if(value.equals(table[hash])) {
            deleted[hash] = true;
            table[hash] = null;
            size--;
            modCount++;
            return true;
        }
        while(table[hash] != null && !value.equals(table[hash]) || deleted[hash]) {
            hash = value.hashCode(tableSize(), probId++);
        }
        if(value.equals(table[hash])) {
            deleted[hash] = true;
            table[hash] = null;
            size--;
            modCount++;
            return true;
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
        Objects.requireNonNull(value);
        int probId = 0;
        int hash = value.hashCode(tableSize(), probId++);
        if(value.equals(table[hash]))
            return true;
        while(table[hash] != null && !value.equals(table[hash]) || deleted[hash]) {
            hash = value.hashCode(tableSize(), probId++);
        }
        if(value.equals(table[hash]))
            return true;
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
        table = (E []) new IOpenHashTableEntity[8];
        deleted = new boolean[8];
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<E> iterator() {
       return new OpenHashTableIterator();
    }
    private class OpenHashTableIterator implements Iterator<E> {
        private int prevIndex;
        private int nextIndex;
        private int restSize;
        private E next;
        private E lastReturned;
        private int expectedModCount = modCount;

        public OpenHashTableIterator() {
            if(!isEmpty()) {
                nextIndex = 0;
                restSize = size();
                while(table[nextIndex] == null || deleted[nextIndex]) {
                    nextIndex++;
                }
                next = table[nextIndex];
            }
        }

        @Override
        public boolean hasNext() {
            return restSize != 0;
        }

        @Override
        public E next() {
            checkForComodification();
            if (!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            prevIndex = nextIndex;
            do {
                nextIndex++;
            } while(nextIndex < tableSize() && (table[nextIndex] == null || deleted[nextIndex]));
            next = table[nextIndex];
            restSize--;
            return lastReturned;
        }

        @Override
        public void remove() {
            checkForComodification();
            if (lastReturned == null)
                throw new IllegalStateException();
            table[prevIndex] = null;
            deleted[prevIndex] = true;
            modCount++;
            expectedModCount++;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    @Override
    public int tableSize() {
        return table.length;
    }
}

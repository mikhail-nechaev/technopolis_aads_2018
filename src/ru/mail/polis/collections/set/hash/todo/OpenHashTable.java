package ru.mail.polis.collections.set.hash.todo;

import ru.mail.polis.collections.set.hash.IOpenHashTable;
import ru.mail.polis.collections.set.hash.IOpenHashTableEntity;

import java.math.BigInteger;
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

    private static final int CAPACITY = 17;
    private static final double LOAD_FACTOR = 0.65;

    private int capacity = CAPACITY;
    private int threshold = (int) (CAPACITY * LOAD_FACTOR);

    private Object[] table = new Object[CAPACITY];
    private int usedBuckets, elementsCount;

    private final Object DELETED = new Object();

    private void adjustCapacity() {
        while(!(new BigInteger(String.valueOf(capacity)).isProbablePrime(16))) {
            capacity++;
        }
    }

    private void increaseCapacity(){
        capacity = (capacity << 2) + 1;
    }

    @SuppressWarnings("unchecked")
    private void resizeTable(){
        int oldTableSize = tableSize();

        increaseCapacity();
        adjustCapacity();

        threshold = (int) (capacity * LOAD_FACTOR);

        Object[] oldTable = table;
        table = new Object[capacity];

        elementsCount = usedBuckets = 0;

        for(int i = 0; i < oldTableSize; i++){
            if(oldTable[i] != null && oldTable[i] != DELETED){
                add((E) oldTable[i]);
            }
            oldTable[i] = null;
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
        if(usedBuckets >= threshold){
            resizeTable();
        }

        int probId = 0;
        for(int i = value.hashCode(tableSize(), probId), j = -1; ; i = value.hashCode(tableSize(), ++probId)){
            if(table[i] == DELETED){
                j = (j == -1) ? i : j;
            } else if(table[i] != null){
                if(value.equals(table[i])){
                    return false;
                }
            } else {
                if(j == -1){
                    usedBuckets++;
                    elementsCount++;
                    table[i] = value;
                } else {
                   elementsCount++;
                   table[j] = value;
                }
                return true;
            }
        }
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
        for(int i = value.hashCode(tableSize(), probId); ; i = value.hashCode(tableSize(), ++probId)){
            if(table[i] == DELETED) continue;
            if(table[i] == null) return false;

            if(value.equals(table[i])){
                elementsCount--;
                table[i] = DELETED;
                return true;
            }
        }
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
        for(int i = value.hashCode(tableSize(), probId), j = -1; ; i = value.hashCode(tableSize(), ++probId)){
            if(table[i] == DELETED){
                j = (j == -1) ? i : j;
            } else if(table[i] != null){
                if(value.equals(table[i])){
                    if(j != -1){
                        table[j] = table[i];
                        table[i] = DELETED;
                    }
                    return true;
                }
            } else {
                return false;
            }

        }
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
        for (int i = 0; i < tableSize(); i++) {
            table[i] = null;
        }
        elementsCount = usedBuckets = 0;
    }

    @Override
    public int tableSize() {
        return capacity;
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

    private class OpenHashTableIterator implements Iterator<E>{

        int currentIndex;
        int lastReturnedIndex = -1;
        int remaining = elementsCount;

        @Override
        public boolean hasNext() {
            return remaining != 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }

            while(table[currentIndex] == null || table[currentIndex] == DELETED){
                currentIndex++;
            }

            remaining--;
            lastReturnedIndex = currentIndex;
            return (E) table[currentIndex++];
        }

        @Override
        public void remove() {
            if (lastReturnedIndex < 0) {
                throw new IllegalStateException();
            }
            elementsCount--;
            table[lastReturnedIndex] = DELETED;
            lastReturnedIndex = -1;
        }
    }

}

package ru.mail.polis.collections.set.hash.todo;

import ru.mail.polis.collections.set.hash.IOpenHashTable;
import ru.mail.polis.collections.set.hash.IOpenHashTableEntity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

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
    private int count = 0;
    private IOpenHashTableEntity[] table;
    private boolean[] deleted;
    private int defaultLength = 8;

    public OpenHashTable() {
        table = new IOpenHashTableEntity[defaultLength];
        deleted = new boolean[defaultLength];
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
        if (count*2>tableSize()){
            resizeTable();
        }
        int probId = 0;
        int hash = value.hashCode(tableSize(),probId++);
        if (table[hash] == null){
            table[hash] = value;
        }else{
            while (probId<tableSize()&&table[hash] != null && !deleted[hash] && !value.equals(table[hash])){
                hash = value.hashCode(tableSize(), probId++);
            }
            if (probId == tableSize()){
                throw new IllegalArgumentException();
            }
            if (value.equals(table[hash])){
                return false;
            }
            table[hash] = value;
            deleted[hash] = false;
        }
        count++;
        return true;
    }

    private void resizeTable() {
        IOpenHashTableEntity[] oldTable = table;
        count = 0;
        table = new IOpenHashTableEntity[tableSize() * 2];
        deleted = new boolean[tableSize()];
        for(int i = 0; i < oldTable.length; i++){
            if (oldTable[i] != null){
                add((E) oldTable[i]);
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
        int hash = value.hashCode(tableSize(), probId++);
        if (value.equals(table[hash])){
            table[hash] = null;
            deleted[hash] = true;
            count--;
            return true;
        }
        while((table[hash] != null && !table[hash].equals(value)) || deleted[hash]){
            hash = value.hashCode(tableSize(),probId++);
            if (probId == tableSize()){
                break;
            }
        }
        if (!value.equals(table[hash])){
            return false;
        }
        deleted[hash]=true;
        table[hash]=null;
        count--;
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
        Objects.requireNonNull(value);
        int probId = 0;
        int hash = value.hashCode(tableSize(), probId++);
        if (value.equals(table[hash])){
            return true;
        }
        while((table[hash] != null && !table[hash].equals(value)) || deleted[hash]){
            hash = value.hashCode(tableSize(),probId++);
            if (probId == tableSize()) break;
        }
        if (value.equals(table[hash])){
            return true;
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
        return count;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        table = new IOpenHashTableEntity[defaultLength];
        deleted = new boolean[defaultLength];
        count = 0;
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<E> iterator() {
        Iterator<E> iterator=new Iterator<E>() {
            int size = count;
            int index = 0;
            @Override
            public boolean hasNext() {
                return size != 0;
            }

            @Override
            public E next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                while (table[index] == null){
                    index++;
                }
                size--;
                return (E) table[index++];
            }
        };
        return iterator;
    }

    @Override
    public int tableSize() {
        return table.length;
    }
}
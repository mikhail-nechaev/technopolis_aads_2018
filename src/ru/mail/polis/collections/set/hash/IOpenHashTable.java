package ru.mail.polis.collections.set.hash;

import ru.mail.polis.collections.set.ISetIterable;

/*
 * Created by Nechaev Mikhail
 * Since 27/11/2018.
 */
/**
 * Open addressed hash table with double hashing.
 *
 * Double hashing based on implementations {@link IOpenHashTableEntity#hashCode(int,int)} specified elements.
 *
 * @param <E> the type of elements maintained by this hash table
 */
public interface IOpenHashTable<E extends IOpenHashTableEntity> extends ISetIterable<E> {

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *  In other words if {@link IOpenHashTableEntity#hashCode(int,int)} specified element is incorrect.
     */
    @Override
    boolean add(E value);

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *  In other words if {@link IOpenHashTableEntity#hashCode(int,int)} specified element is incorrect.
     */
    @Override
    boolean remove(E value);

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *  In other words if {@link IOpenHashTableEntity#hashCode(int,int)} specified element is incorrect.
     */
    @Override
    boolean contains(E value);

    int tableSize();
}

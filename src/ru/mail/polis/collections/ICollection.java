package ru.mail.polis.collections;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * @param <E> the type of elements maintained by this collection
 */
public interface ICollection<E> {

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    int size();

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    boolean isEmpty();

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    boolean contains(Object value);

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    void clear();
}

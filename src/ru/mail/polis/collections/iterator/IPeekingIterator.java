package ru.mail.polis.collections.iterator;

import java.util.Iterator;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * @param <E> the type of elements maintained by this iterator
 */
public interface IPeekingIterator<E extends Comparable<E>> extends Iterator<E>, Comparable<IPeekingIterator<E>> {

    /**
     * Returns {@code true} if the iteration has more elements.
     *
     * @return {@code true} if the iteration has more elements
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    E next();

    /**
     * Retrieves the next element in the iteration, but does not iterate.
     *
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    E peek();

    /**
     * Compares this object with the specified object for order.
     * Returns a negative integer, zero, or a positive integer as this peeked value is less
     * than, equal to, or greater than the peeked value specified object.
     *
     * <p> If iterator has no elements so it is less.
     *
     * @param other the {@link IPeekingIterator} to be compared.
     * @return a negative integer, zero, or a positive integer as this peeked value
     * is less than, equal to, or greater than the peeked value specified iterator.
     */
    int compareTo(IPeekingIterator<E> other);
}

package ru.mail.polis.collections.set;

import ru.mail.polis.collections.ICollection;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * A collection that contains no duplicate elements.
 *
 * @param <E> the type of elements maintained by this set
 */
public interface ISet<E> extends ICollection<E> {

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws NullPointerException if the specified element is null
     */
    boolean add(E value);

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    boolean remove(E value);
}

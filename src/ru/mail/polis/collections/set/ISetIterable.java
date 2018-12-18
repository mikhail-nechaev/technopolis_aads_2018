package ru.mail.polis.collections.set;

import ru.mail.polis.collections.ICollectionIterable;

import java.util.Iterator;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * A collection that contains no duplicate elements.
 *
 * @param <E> the type of elements maintained by this set
 */
public interface ISetIterable<E> extends ISet<E>, ICollectionIterable<E> {

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    Iterator<E> iterator();
}

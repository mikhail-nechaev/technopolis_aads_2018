package ru.mail.polis.collections;

import java.util.Iterator;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */

/**
 * @param <E> the type of elements maintained by this collection
 */
public interface ICollectionIterable<E> extends ICollection<E>, Iterable<E> {

    /**
     * Returns an iterator over the elements in this collection.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    Iterator<E> iterator();
}

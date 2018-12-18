package ru.mail.polis.collections.set.sorted;

import ru.mail.polis.collections.ICollectionIterable;
import ru.mail.polis.collections.set.ISet;

import java.util.Iterator;

/**
 * A {@link ISet} that further provides a <i>total ordering</i> on its elements.
 *
 * @param <E> the type of elements maintained by this set
 */
public interface ISortedSetIterable<E> extends ISortedSet<E>, ICollectionIterable<E> {

    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    Iterator<E> iterator();

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    Iterator<E> descendingIterator();
}

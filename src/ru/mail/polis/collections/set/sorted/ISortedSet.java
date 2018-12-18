package ru.mail.polis.collections.set.sorted;

import ru.mail.polis.collections.set.ISet;

/**
 * A {@link ISet} that further provides a <i>total ordering</i> on its elements.
 *
 * @param <E> the type of elements maintained by this set
 */
public interface ISortedSet<E> extends ISet<E> {

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws java.util.NoSuchElementException if this set is empty
     */
    E first();

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws java.util.NoSuchElementException if this set is empty
     */
    E last();
}

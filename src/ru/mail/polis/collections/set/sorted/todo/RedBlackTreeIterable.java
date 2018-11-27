package ru.mail.polis.collections.set.sorted.todo;

import java.util.Comparator;
import java.util.Iterator;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

public class RedBlackTreeIterable<E extends Comparable<E>> extends RedBlackTree<E> implements ISortedSetIterable<E> {

    public RedBlackTreeIterable() {
        super();
    }

    public RedBlackTreeIterable(Comparator<E> comparator) {
        super();
    }

    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("todo: implement this");
    }
}

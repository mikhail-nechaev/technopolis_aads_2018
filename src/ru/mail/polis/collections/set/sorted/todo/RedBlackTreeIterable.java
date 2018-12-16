package ru.mail.polis.collections.set.sorted.todo;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

/**
 * A Red-Black tree with iterator based {@link ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet} implementation.
 *
 * @param <E> the type of elements maintained by this set
 */
public class RedBlackTreeIterable<E extends Comparable<E>> extends RedBlackTree<E> implements ISortedSetIterable<E> {

    public RedBlackTreeIterable() {
        super();
    }

    /**
     * Creates a {@code ISelfBalancingSortedTreeSet} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public RedBlackTreeIterable(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int count = size;
            int index = -1;
            RBNode<E> next = treeMinimum(root);

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public E next() {
                if (hasNext()) {
                    throw new NoSuchElementException();
                }
                index++;
                RBNode<E> answer = next;
                next = next.parent;
                if (answer == next.left) {
                    if (next.right != null) {
                        next = treeMinimum(next.right);
                    }
                }
                return answer.value;
            }
        };
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

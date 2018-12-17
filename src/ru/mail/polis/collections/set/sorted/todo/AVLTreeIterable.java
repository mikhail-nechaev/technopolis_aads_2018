package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A AVL tree with iterator based {@link ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet} implementation.
 *
 * @param <E> the type of elements maintained by this set
 */
public class AVLTreeIterable<E extends Comparable<E>> extends AVLTree<E> implements ISortedSetIterable<E> {

    public AVLTreeIterable() {
        super();
    }

    /**
     * Creates a {@code ISelfBalancingSortedTreeSet} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public AVLTreeIterable(Comparator<E> comparator) {
        super(comparator);
    }

    protected void delete(E value) {
        remove(value);
    }


    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    @SuppressWarnings("unchecked")
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            AVLNode<E> next = getMin(root);
            AVLNode<E> lastReturned;
            int index = 0;
            int count = size;
            boolean toRight = false;
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = next;
                index++;
                canRemove = true;
                if (toRight || next.parent == null) {
                    next = getMin(next.right);
                    toRight = false;
                    return lastReturned.value;
                }

                if (isLeftSon(next)) {
                    next = next.parent;
                    toRight = next.right != null;
                } else {
                    while (next.parent != null && !isLeftSon(next)) {
                        next = next.parent;
                    }
                    if (next.parent == null) {
                        next = getMin(next.right);
                        toRight = false;
                    } else {
                        next = next.parent;
                        toRight = next.right != null;
                    }
                }

                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (!canRemove) {
                    throw new IllegalStateException();
                }
                canRemove = false;
                delete(lastReturned.value);
                count--;
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
        return new Iterator<>() {
            int index = 0;
            int count = size;
            boolean toLeft = false;
            boolean canRemove = false;
            AVLNode<E> next = getMax(root);
            AVLNode<E> lastReturned;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = next;
                index++;
                canRemove = true;
                if (toLeft || next.parent == null) {
                    next = getMax(next.left);
                    toLeft = false;
                    return lastReturned.value;
                }

                if (!isLeftSon(next)) {
                    next = next.parent;
                    toLeft = next.left != null;
                } else {
                    while (next.parent != null && isLeftSon(next)) {
                        next = next.parent;
                    }
                    if (next.parent == null) {
                        next = getMax(next.left);
                        toLeft = false;
                    } else {
                        next = next.parent;
                        toLeft = next.left != null;
                    }
                }

                return lastReturned.value;
            }

            @Override
            public void remove() {
                if(!canRemove){
                    throw new IllegalStateException();
                }
                canRemove = false;
                delete(lastReturned.value);
                count--;
            }
        };
    }
}

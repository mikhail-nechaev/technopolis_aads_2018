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

    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    public Iterator<E> iterator() {
        return new AVLIterator();
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new AVLDescIterator();
    }

    private class AVLIterator implements Iterator<E> {
        int cursor = size;
        AVLNode<E> lastReturned;

        private AVLNode<E> findNext(AVLNode<E> estimatedNode, AVLNode<E> previosNode) {
            if (estimatedNode == null) {
                return previosNode;
            }
            if (comparator.compare(estimatedNode.value, lastReturned.value) > 0) {
                return findNext(estimatedNode.left, estimatedNode);
            } else if (comparator.compare(estimatedNode.value, lastReturned.value) < 0) {
                return findNext(estimatedNode.right, previosNode);
            } else {
                if (estimatedNode.right != null) {
                    return findNext(estimatedNode.right, previosNode);
                } else {
                    return previosNode;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return cursor != 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (lastReturned == null) {
                lastReturned = findMinNode(root);
            } else {
                lastReturned = findNext(root, null);
            }
            cursor--;
            return lastReturned.value;
        }

        @Override
        public void remove() {
            if (lastReturned != null) {
                AVLTreeIterable.this.remove(lastReturned.value);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private class AVLDescIterator implements Iterator<E> {
        int cursor = size;
        AVLNode<E> lastReturned;

        private AVLNode<E> findNext(AVLNode<E> estimatedNode, AVLNode<E> previousNode) {
            if (estimatedNode == null) {
                return previousNode;
            }
            if (comparator.compare(estimatedNode.value, lastReturned.value) > 0) {
                return findNext(estimatedNode.left, previousNode);
            } else if (comparator.compare(estimatedNode.value, lastReturned.value) < 0) {
                return findNext(estimatedNode.right, estimatedNode);
            } else {
                if (estimatedNode.left != null) {
                    return findNext(estimatedNode.left, previousNode);
                } else {
                    return previousNode;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return cursor != 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (lastReturned == null) {
                lastReturned = findMaxNode(root);
            } else {
                lastReturned = findNext(root, null);
            }
            cursor--;
            return lastReturned.value;
        }

        @Override
        public void remove() {
            if (lastReturned != null) {
                AVLTreeIterable.this.remove(lastReturned.value);
            } else {
                throw new IllegalStateException();
            }
        }
    }
}

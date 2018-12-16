package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
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
        return new AVLTreeIterator();
    }

    private class AVLTreeIterator implements Iterator<E> {
        private E [] values;
        private int index = 0;
        private E next;
        private E lastReturned;
        private int nextIndex = 0;
        private int expectedModCount = modCount;
        AVLTreeIterator() {
            values = (E []) new Comparable[size()];
            inorderTraverse(root);
            next = size() == 0 ? null : values[nextIndex];
        }
        @Override
        public boolean hasNext() {
            return nextIndex < size();
        }

        @Override
        public E next() {
            checkForComodification();
            if(!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            nextIndex++;
            if(hasNext()) {
                next = values[nextIndex];
            }
            return lastReturned;
        }

        public void inorderTraverse(AVLNode<E> curr) {
            if (curr == null) return;
            inorderTraverse(curr.left);
            values[index++] = curr.value;
            inorderTraverse(curr.right);
        }
        private void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new AVLTreeDescendingIterator();
    }

    private class AVLTreeDescendingIterator implements Iterator<E>{
        private E [] values;
        private int index = 0;
        private E next;
        private E lastReturned;
        private int nextIndex = 0;
        private int expectedModCount = modCount;
        AVLTreeDescendingIterator() {
            values = (E []) new Comparable[size()];
            inorderReverse(root);
            next = size() == 0 ? null : values[nextIndex];
        }
        @Override
        public boolean hasNext() {
            return nextIndex < size();
        }

        @Override
        public E next() {
            checkForComodification();
            if(!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            nextIndex++;
            if(hasNext()) {
                next = values[nextIndex];
            }
            return lastReturned;
        }

        public void inorderReverse(AVLNode<E> curr) {
            if (curr == null) return;
            inorderReverse(curr.right);
            values[index++] = curr.value;
            inorderReverse(curr.left);
        }

        private void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}

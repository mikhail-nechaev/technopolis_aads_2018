package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

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
        return new Iterator<E>() {
            private AVLNode<E> lastNextNode = null;
            private AVLNode<E> nextNode;
            private int rest = size();

            private AVLNode<E> getNextNode(AVLNode<E> cur, AVLNode<E> parent) {
                while (cur != null) {
                    int cmp = comparator.compare(lastNextNode.value, cur.value);
                    if (cmp < 0) {
                        parent = cur;
                        cur = cur.left;
                    } else if (cmp > 0) {
                        cur = cur.right;
                    } else {
                        if(cur.right == null){
                            return parent;
                        }
                        cur = cur.right;
                    }
                }
                return parent;
            }

            @Override
            public boolean hasNext() {
                return rest > 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                rest--;
                if(nextNode == null){
                    nextNode=findMin(root);
                }
                lastNextNode = nextNode;
                nextNode = getNextNode(root, null);
                return lastNextNode.value;
            }

            @Override
            public void remove() {
                if (lastNextNode == null) {
                    throw new IllegalStateException();
                }
                AVLTreeIterable.this.remove(lastNextNode.value);
                lastNextNode = null;
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
        return new Iterator<E>() {
            private AVLNode<E> lastNextNode = null;
            private AVLNode<E> nextNode;
            private int rest = size();


            private AVLNode<E> getNextNode(AVLNode<E> cur, AVLNode<E> parent) {
                while (cur != null) {
                    int cmp = comparator.compare(lastNextNode.value, cur.value);
                    if (cmp < 0) {
                        cur = cur.left;
                    } else if (cmp > 0) {
                        parent = cur;
                        cur = cur.right;
                    } else {
                        if(cur.left == null){
                            return parent;
                        }
                        cur = cur.left;
                    }
                }
                return parent;
            }

            @Override
            public boolean hasNext() {
                return rest > 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                rest--;
                if(nextNode==null){
                    nextNode=findMax(root);
                }
                lastNextNode = nextNode;
                nextNode = getNextNode(root,null);
                return lastNextNode.value;
            }

            @Override
            public void remove() {
                if (lastNextNode == null) {
                    throw new IllegalStateException();
                }
                AVLTreeIterable.this.remove(lastNextNode.value);
                lastNextNode = null;
            }
        };
    }
}

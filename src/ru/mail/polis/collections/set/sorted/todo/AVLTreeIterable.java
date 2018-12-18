package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
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

        AVLNode<E> nextNode = root;
        Stack<AVLNode<E>> stack = new Stack<>();
        int expectedModCount = modCount;

        while (nextNode != null) {
            stack.push(nextNode);
            nextNode = nextNode.left;
        }

        Iterator<E> iterator = new Iterator<E>() {

            @Override
            public boolean hasNext() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (stack.empty()) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public E next() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                AVLNode<E> tempNode = stack.pop();
                E tempValue = tempNode.value;
                if (tempNode.right != null) {
                    tempNode = tempNode.right;
                    while (tempNode != null) {
                        stack.push(tempNode);
                        tempNode = tempNode.left;
                    }
                }
                return tempValue;
            }
        };

        return iterator;
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        AVLNode<E> nextNode = root;
        Stack<AVLNode<E>> stack = new Stack<>();
        int expectedModCount = modCount;

        while (nextNode != null) {
            stack.push(nextNode);
            nextNode = nextNode.right;
        }

        Iterator<E> iterator = new Iterator<E>() {

            @Override
            public boolean hasNext() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (stack.empty()) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public E next() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                AVLNode<E> tempNode = stack.pop();
                E tempValue = tempNode.value;
                if (tempNode.left != null) {
                    tempNode = tempNode.left;
                    while (tempNode != null) {
                        stack.push(tempNode);
                        tempNode = tempNode.right;
                    }
                }
                return tempValue;
            }
        };

        return iterator;
    }
}

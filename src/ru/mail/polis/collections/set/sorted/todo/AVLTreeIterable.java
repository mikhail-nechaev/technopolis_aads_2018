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

    public Stack<AVLNode<E>> makeAscStack(AVLNode<E> cur, Stack<AVLNode<E>> stack) {
        if (cur == null) {
            return stack;
        }
        makeAscStack(cur.left, stack);
        stack.push(cur);
        makeAscStack(cur.right, stack);
        return stack;
    }

    public Stack<AVLNode<E>> makeDescStack(AVLNode<E> cur, Stack<AVLNode<E>> stack) {
        if (cur == null) {
            return stack;
        }
        makeDescStack(cur.right, stack);
        stack.push(cur);
        makeDescStack(cur.left, stack);
        return stack;
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
            private Stack<AVLNode<E>> stack = makeAscStack(root, new Stack<>());

            @Override
            public boolean hasNext() {
                return stack.size() > 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastNextNode = stack.peek();
                return stack.pop().value;
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
            private Stack<AVLNode<E>> stack = makeDescStack(root, new Stack<>());

            @Override
            public boolean hasNext() {
                return stack.size() > 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastNextNode = stack.peek();
                return stack.pop().value;
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

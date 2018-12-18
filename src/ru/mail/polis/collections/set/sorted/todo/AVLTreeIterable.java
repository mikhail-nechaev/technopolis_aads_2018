package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.list.todo.ArrayDequeSimple;
import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.ArrayDeque;
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
        return new AVLTreeIterator();
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

    private class AVLTreeIterator implements Iterator<E> {
        ArrayDequeSimple<AVLNode<E>> deque;
        E last;

        public AVLTreeIterator() {
            deque = new ArrayDequeSimple<>();
            preorder(root);
        }

        private void preorder(AVLNode<E> node) {
            if (node == null)
                return;
            deque.addLast(node);
            preorder(node.left);
        }

        int cursor = -1;

        @Override
        public boolean hasNext() {
            return !deque.isEmpty();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            AVLNode<E> node = deque.removeLast();
            last = node.value;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    deque.addLast(node);
                    node = node.left;
                }
            }
            cursor++;
            return last;
        }

        @Override
        public void remove() {
            if (cursor == -1)
                throw new IllegalStateException();
            removeSuper(last);
        }
    }

    private void removeSuper(E value) {
        super.remove(value);
    }

    private class AVLTreeDescendingIterator implements Iterator<E> {

        public AVLTreeDescendingIterator() {
            elements = (E[]) new Comparable[size()];
            preorder(root);
        }

        private void preorder(AVLNode<E> node) {
            if (node == null)
                return;
            preorder(node.left);
            elements[index++] = node.value;
            preorder(node.right);
        }

        E[] elements;
        int index = 0, cursor = size();

        @Override
        public boolean hasNext() {
            return cursor > 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return elements[--cursor];
        }

        @Override
        public void remove() {
            if (cursor == size())
                throw new IllegalStateException();
            removeSuper(elements[cursor]);
        }
    }
}

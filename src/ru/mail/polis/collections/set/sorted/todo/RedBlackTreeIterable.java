package ru.mail.polis.collections.set.sorted.todo;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

/**
 *
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
       return new RBTreeIterator();
    }

    private class RBTreeIterator implements Iterator<E>{

        RBNode<E> current;
        RBNode<E> lastReturned;
        int remaining;

        private RBNode<E> findInorderSuccessor(RBNode<E> node){
            RBNode<E> successor;
            if(node.right != null){
                successor = node.right;
                while (successor.left != null){
                    successor = successor.left;
                }

                return successor;
            }

            successor = node;
            while (successor.parent != null && successor.parent.left != successor){
                successor = successor.parent;
            }

            return successor.parent;
        }

        RBTreeIterator(){
            remaining = size;

            current = root;
            while (current != null && current.left != null) {
                current = current.left;
            }
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }

            lastReturned = current;
            E value = current.value;
            current = findInorderSuccessor(current);
            remaining--;
            return value;
        }

        @Override
        public void remove() {
            if(lastReturned == null){
                throw new IllegalStateException();
            }
            RedBlackTreeIterable.this.remove(lastReturned.value);
            lastReturned = null;
        }
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new RBTreeDescendingIterator();
    }

    private class RBTreeDescendingIterator implements Iterator<E>{

        RBNode<E> current;
        RBNode<E> lastReturned;
        int remaining;

        private RBNode<E> findDescendingInorderSuccessor(RBNode<E> node){
            RBNode<E> successor;
            if(node.left != null){
                successor = node.left;
                while (successor.right != null){
                    successor = successor.right;
                }

                return successor;
            }

            successor = node;
            while (successor.parent != null && successor.parent.right != successor){
                successor = successor.parent;
            }

            return successor.parent;
        }

        RBTreeDescendingIterator(){
            remaining = size;

            current = root;
            while (current != null && current.right != null) {
                current = current.right;
            }
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }

            lastReturned = current;
            E value = current.value;
            current = findDescendingInorderSuccessor(current);
            remaining--;
            return value;
        }

        @Override
        public void remove() {
            if(lastReturned == null){
                throw new IllegalStateException();
            }
            RedBlackTreeIterable.this.remove(lastReturned.value);
            lastReturned = null;
        }
    }
}

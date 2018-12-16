package ru.mail.polis.collections.set.sorted.todo;

import java.util.Arrays;
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
    public RedBlackTreeIterable(Comparator<E> comparator) throws NullPointerException {
        super(comparator);
    }



    protected class StackIterator<E> {
        private E[] Nodes;
        private int arrayLength;

        private static final int MIN_INITIAL_CAPACITY = 25;

        private static final float LOAD_FACTOR = (float) 0.8;

        StackIterator() {
            Nodes = (E[]) new Object[MIN_INITIAL_CAPACITY];
            arrayLength = 0;
        }

        public void push(E value) throws NullPointerException {
            if (value == null) {
                throw new NullPointerException();
            }
            float occupancy = (float) arrayLength / (float) Nodes.length;
            if (occupancy >= LOAD_FACTOR) {
                doubleCapacity();
            }
            arrayLength++;
            Nodes[arrayLength - 1] = value;
        }

        private void doubleCapacity() {
            Nodes = Arrays.copyOf(Nodes, Nodes.length << 1);
        }

        private E pop() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }
            arrayLength--;
            return Nodes[arrayLength];
        }

       /* private void removeAtIndex(RBNode current){
            remove(current.value);
        }*/

        private boolean isEmpty() {
            return arrayLength == 0;
        }

    }


    private class RBNodeIterator <E extends Comparable<E>>implements Iterator<E> {



        RBNode <E> index = (RBNode<E>) root;
        StackIterator <RBNode<E>> stackIterator;
        public RBNodeIterator(){
            stackIterator = new StackIterator<>();

            while (index != null){
                stackIterator.push(index);
                index = index.left;
            }
        }
        @Override
        public boolean hasNext() {
            return !stackIterator.isEmpty();
        }


        @Override
        public E next() throws NoSuchElementException {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            RBNode<E> node = stackIterator.pop();
            E temp = node.value;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stackIterator.push(node);
                    node = node.left;
                }

            }

            return temp;
        }
        @Override
        public void remove() throws IllegalStateException{
            if(index == null){
                throw new IllegalStateException();
            }
            //stackIterator.removeAtIndex(index);
        }
    }



    private class RedBlackTreeDescendingIterator <E extends Comparable<E>> implements Iterator<E>{

        RBNode <E> index = (RBNode<E>) root;
        StackIterator <RBNode<E>> stackIterator;

        RedBlackTreeDescendingIterator (){
            stackIterator = new StackIterator<>();

            while (index != null){
                stackIterator.push(index);
                index = index.right;
            }
        }


        @Override
        public boolean hasNext() {
            return !stackIterator.isEmpty();
        }


        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            RBNode <E> node = stackIterator.pop();
            E temp = node.value;
            if (node.left != null) {
                node = node.left;
                while (node != null) {
                    stackIterator.push(node);
                    node = node.right;
                }

            }
            return temp;
        }
    }






    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    @Override
    public Iterator<E> iterator() {
        return new RBNodeIterator<>();
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new RedBlackTreeDescendingIterator<>();
    }
}

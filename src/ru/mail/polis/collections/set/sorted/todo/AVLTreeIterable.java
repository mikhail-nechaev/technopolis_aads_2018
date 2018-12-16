package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * A AVL tree with iterator based {@link ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet} implementation.
 *
 * @param <E> the type of elements maintained by this set
 */
@SuppressWarnings("unchecked")
public class AVLTreeIterable<E extends Comparable<E>> extends AVLTree<E> implements ISortedSetIterable<E> {


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

       /* private void removeAtIndex(AVLNode current){
            remove(current.value);
        }*/

        private boolean isEmpty() {
            return arrayLength == 0;
        }

    }


    public AVLTreeIterable() {
        super();
    }


    public AVLTreeIterable(Comparator<E> comparator) throws NullPointerException {
        super(comparator);
    }


    @Override
    public Iterator<E> iterator() {
        return new AVLTreeIter();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new AVLTreeDescendingIter();
    }


    private class AVLTreeDescendingIter<E extends Comparable<E>> implements Iterator<E>{

        AVLNode <E> index = (AVLNode) root;
        StackIterator <AVLNode<E>> stackIterator;

        AVLTreeDescendingIter(){
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
            AVLNode<E> node = stackIterator.pop();
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


    private class AVLTreeIter <E extends Comparable<E>>implements Iterator<E> {



        AVLNode <E> index = (AVLNode) root;
        StackIterator <AVLNode<E>> stackIterator;
        public AVLTreeIter(){
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
            AVLNode<E> node = stackIterator.pop();
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
}

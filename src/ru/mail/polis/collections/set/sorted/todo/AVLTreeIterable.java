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
        Iterator<E> iterator=new Iterator<E>() {
            int size=count;
            AVLNode<E> lastNext =null;

            private AVLNode<E> findSupremum(AVLNode<E> node,AVLNode<E> parent){
                if (node==null){
                    return parent;
                }
                if (comparator.compare(node.value, lastNext.value)>0){
                    return findSupremum(node.left,node);
                }else if (comparator.compare(node.value, lastNext.value)<0){
                    return findSupremum(node.right,parent);
                }else{
                    if (node.right!=null){
                        return findSupremum(node.right,parent);
                    }else {
                        return parent;
                    }
                }
            }

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return size!=0;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public E next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                if (lastNext ==null){
                    lastNext = findMinNode(root);
                }else{
                    lastNext=findSupremum(root,null);
                }
                size--;
                return lastNext.value;
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
        Iterator<E> descendingIterator=new Iterator<E>() {
            int size=count;
            AVLNode<E> lastNext =null;

            private AVLNode<E> findInfinum(AVLNode<E> node,AVLNode<E> parent){
                if (node==null){
                    return parent;
                }
                if (comparator.compare(node.value, lastNext.value)>0){
                    return findInfinum(node.left,parent);
                }else if (comparator.compare(node.value, lastNext.value)<0){
                    return findInfinum(node.right,node);
                }else{
                    if (node.left!=null){
                        return findInfinum(node.left,parent);
                    }else {
                        return parent;
                    }
                }
            }

            private AVLNode<E> findMaxNode(AVLNode<E> node){
                return node.right==null?node:findMaxNode(node.right);
            }

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return size!=0;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public E next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                if (lastNext ==null){
                    lastNext = findMaxNode(root);
                }else{
                    lastNext=findInfinum(root,null);
                }
                size--;
                return lastNext.value;
            }
        };
        return descendingIterator;
    }

    public static void main(String[] args) {
        AVLTreeIterable<Integer> tree = new AVLTreeIterable<>();
        for (int i=0;i<10;i++){
            tree.add(i);
        }
        Iterator<Integer> integerIterator=tree.descendingIterator();
        while (integerIterator.hasNext()){
            System.out.println(integerIterator.next());
        }
    }
}

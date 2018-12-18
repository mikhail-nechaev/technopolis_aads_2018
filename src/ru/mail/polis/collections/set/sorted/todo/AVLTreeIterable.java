package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Comparator;
import java.util.EmptyStackException;
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
        return new Iterator<>() {
            Stack<AVLNode<E>> stack = null;
            private void fill(AVLNode<E> root) {
                stack = new Stack<>();
                while (root != null) {
                    stack.push(root);
                    root = root.left;
                }
            }
            @Override
            public boolean hasNext() {
                if (stack == null){
                    fill(root);
                }
                return !stack.isEmpty();
            }
            @Override
            public void remove(){
                try{
                    stack.pop();
                } catch (NullPointerException e){
                    throw new IllegalStateException();
                }

            }
            @Override
            public E next() {
                if (stack == null){
                    fill(root);
                }
                AVLNode<E> node;
                try{
                    node = stack.pop();
                } catch (EmptyStackException e){
                    throw new NoSuchElementException();
                }
                E result = node.value;
                if (node.right != null) {
                    node = node.right;
                    while (node != null) {
                        stack.push(node);
                        node = node.left;
                    }
                }
                return result;
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
        return new Iterator<>() {
            Stack<AVLNode<E>> stack = null;
            private void fill(AVLNode<E> root) {
                stack = new Stack<>();
                while (root != null) {
                    stack.push(root);
                    root = root.right;
                }
            }
            @Override
            public boolean hasNext() {
                if (stack == null){
                    fill(root);
                }
                return !stack.isEmpty();
            }
            @Override
            public void remove(){
                try{
                    stack.pop();
                } catch (NullPointerException e){
                    throw new IllegalStateException();
                }
            }
            @Override
            public E next() {
                if (stack == null){
                    fill(root);
                }
                AVLNode<E> node;
                try{
                    node = stack.pop();
                } catch (EmptyStackException e){
                    throw new NoSuchElementException();
                }
                E result = node.value;
                if (node.left != null) {
                    node = node.left;
                    while (node != null) {
                        stack.push(node);
                        node = node.right;
                    }
                }
                return result;
            }
        };
    }

    public static void main(String[] args) {
        AVLTreeIterable<Integer> avl = new AVLTreeIterable<>();
        avl.add(228);
        avl.add(1337);
        for (Integer a: avl
        ) {
          System.out.println(a);
        }
    }
}

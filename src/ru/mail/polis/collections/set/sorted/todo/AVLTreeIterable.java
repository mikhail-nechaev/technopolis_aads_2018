package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/**
 * A AVL tree with iterator based {@link ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet} implementation.
 *
 * @param <E> the type of elements maintained by this set
 */
@SuppressWarnings("unchecked")
public class AVLTreeIterable<E extends Comparable<E>> extends AVLTree<E> implements ISortedSetIterable<E> {


    public AVLTreeIterable() {
        super();
    }
    public AVLTreeIterable(Comparator<E> comparator) {
        super(comparator);
    }

    private class AVLTreeIterator<E extends Comparable<E>> implements Iterator{

        private void inorderTraverse(AVLNode<E> currentNode) {
            if (currentNode == null) return;
            inorderTraverse(currentNode.left);
            Nodes[index++] = currentNode.value;
            inorderTraverse(currentNode.right);
        }
       /* private void removeAtIndex(int index){
            AVLNode currentNode = null;
            currentNode.value = Nodes[index];
            AVLTreeIterable.this.remove(currentNode.value);
        }*/

        private E [] Nodes;
        private int index;
        private int cursor;
        private int sizeIterable = size();
        private E current;


        AVLTreeIterator(){
            Nodes =(E[]) new Comparable[size()];
            inorderTraverse((AVLNode<E>) root);
        }

        @Override
        public boolean hasNext() {
            return cursor < sizeIterable;
        }



        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            current = Nodes[cursor];
            cursor++;
            return  current;
        }


       /* @Override
        public void remove() {
            if(cursor < 0){
                throw new IllegalStateException();
            }
            removeAtIndex(index);
            cursor--;
        }*/
    }


    private class AVLTreeDescendingIterator<E extends Comparable<E>> implements Iterator{

        private void reverseInorderTraverse(AVLNode<E> current) {
            if (current == null) return;
            reverseInorderTraverse(current.right);
            Nodes[index++] = current.value;
            reverseInorderTraverse(current.left);
        }

        private E [] Nodes;
        private int index;
        private int cursor;
        private int sizeIterable = size();
        private E current;


        AVLTreeDescendingIterator(){
            Nodes =(E[]) new Comparable[size()];
            reverseInorderTraverse((AVLNode<E>) root);
        }

        @Override
        public boolean hasNext() {
            return cursor < sizeIterable;
        }


        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            current = Nodes[cursor];
            cursor++;
            return  current;
        }
    }


    @Override
    public Iterator<E> iterator() {
        return new AVLTreeIterator();
    }


    @Override
    public Iterator<E> descendingIterator() {
        return new AVLTreeDescendingIterator();
    }
}

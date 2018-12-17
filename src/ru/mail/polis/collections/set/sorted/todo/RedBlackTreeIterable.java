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


    public RedBlackTreeIterable(Comparator<E> comparator) throws NullPointerException {
        super(comparator);
    }



    private class RBTreeIterator<E extends Comparable<E>> implements Iterator{

        private void inorderTraverse(RBNode currentNode) {
            if (currentNode == null) return;
            inorderTraverse(currentNode.left);
            Nodes[index++] = (E) currentNode.value;
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


        RBTreeIterator(){
            Nodes =(E[]) new Comparable[size()];
            inorderTraverse(root);
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


    private class RBTreeDescendingIterator<E extends Comparable<E>> implements Iterator{

        private void reverseInorderTraverse(RBNode current) {
            if (current == null) return;
            reverseInorderTraverse(current.right);
            Nodes[index++] = (E) current.value;
            reverseInorderTraverse(current.left);
        }

        private E [] Nodes;
        private int index;
        private int cursor;
        private int sizeIterable = size();
        private E current;


        RBTreeDescendingIterator(){
            Nodes =(E[]) new Comparable[size()];
            reverseInorderTraverse(root);
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
        return new RBTreeIterator<>();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new RBTreeDescendingIterator();
    }
}

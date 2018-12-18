package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.list.todo.ArrayDequeSimple;
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

    private void removeValue(E value){
        super.remove(value);
    }

    private class AVLTreeIterator implements Iterator<E> {

        private void inorderTraverse(AVLNode<E> currentNode) { // кидаем в дек все левые поддеревья от root
            if (currentNode == null) return;
            avlNodeArrayDequeSimple.addLast(currentNode);
            inorderTraverse(currentNode.left);
        }


        protected ArrayDequeSimple <AVLNode<E>> avlNodeArrayDequeSimple;
        E currentValue;
        int cursor = 0;


        AVLTreeIterator(){
            avlNodeArrayDequeSimple = new ArrayDequeSimple<>();
            inorderTraverse((AVLNode<E>) root);
        }

        @Override
        public boolean hasNext() {
            return !avlNodeArrayDequeSimple.isEmpty();
        }



        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            AVLNode current = avlNodeArrayDequeSimple.removeLast(); // достаем последний
            currentValue = (E) current.value; // запоминаем значение
            if(current.right != null){ // если последнего есть справа
                current = current.right; // идем по ним
                while (current != null){ // пока он не null
                    avlNodeArrayDequeSimple.addLast(current); //добавляем в конец
                    current = current.left; // потом идем в левые ветки от текущего
                }
            }
            cursor++;
            return currentValue;
        }


        @Override
        public void remove() {
            if(cursor == 0){
                throw new IllegalStateException();
            }
            cursor--;
            removeValue(currentValue);
        }
    }


    private class AVLTreeDescendingIterator implements Iterator<E>{

        private void reverseInorderTraverse(AVLNode<E> currentNode) {
            if (currentNode == null) return;
            avlNodeArrayDequeSimple.addLast(currentNode);
            reverseInorderTraverse(currentNode.right);
        }

        protected ArrayDequeSimple <AVLNode<E>> avlNodeArrayDequeSimple;
        E currentValue;
        int cursor = 0;


        AVLTreeDescendingIterator(){
            avlNodeArrayDequeSimple = new ArrayDequeSimple<>();
            reverseInorderTraverse((AVLNode<E>) root);
        }

        @Override
        public boolean hasNext() {
            return !avlNodeArrayDequeSimple.isEmpty();
        }


        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            AVLNode current = avlNodeArrayDequeSimple.removeLast(); // достаем последний
            currentValue = (E) current.value; // запоминаем значение
            if(current.left != null){ // если последнего есть справа
                current = current.left; // идем по ним
                while (current != null){ // пока он не null
                    avlNodeArrayDequeSimple.addLast(current); //добавляем в конец
                    current = current.right; // потом идем в левые ветки от текущего
                }
            }
            cursor++;
            return currentValue;
        }


        @Override
        public void remove() {
            if(cursor == 0){
                throw new IllegalStateException();
            }
            cursor--;
            removeValue(currentValue);
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
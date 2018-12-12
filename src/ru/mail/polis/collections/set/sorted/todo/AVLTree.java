package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
@SuppressWarnings("unchecked")
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {


    protected static class AVLNode<E extends  Comparable<E>> {
        private E value;
        AVLNode<E> left;
        AVLNode<E> right;
        int height;

        AVLNode(E value){
            this.value = value;
            this.height = 1;
        }
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    private int length = 0;

    private int getHeight(AVLNode current){
        if(current == null){
            return 0;
        }
        return current.height;
    }


    private  AVLNode rightRotate(AVLNode current){
        AVLNode b = current.left;
        AVLNode a = b.right;

        //rorate
        b.right = current;
        current.left = a;

        //upd
        current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;
        b.height = Math.max(getHeight(b.left), getHeight(b.right)) + 1;

        return b;

    }

    private AVLNode leftRotate(AVLNode current){
        AVLNode b = current.right;
        AVLNode a = b.left;

        //rotate
        b.left = current;
        current.right = a;

        //upd
        current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;
        b.height = Math.max(getHeight(b.left), getHeight(b.right)) + 1;

        return b;
    }


    private void insert(E value){
        this.root = insert(this.root, value);
    }


    private AVLNode insert(AVLNode current, E value){
        if(current == null){
           return new AVLNode(value);
        }
        if(current.value.compareTo(value) > 0){
            current.left = insert(current.left,value);
        }
        else if(current.value.compareTo(value) < 0){
            current.right = insert(current.right, value);
        }

        current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;

        int heightDiff = heightDiff(current);

        //LL Case
        if(heightDiff > 1 && value.compareTo((E) current.left.value) < 0){
            return rightRotate(current);
        }
        //RR Case
        //noinspection unchecked
        if(heightDiff < -1 && value.compareTo((E) current.right.value) > 0){
            return leftRotate(current);
        }
        // LR Case
        if (heightDiff > 1 && value.compareTo((E) current.left.value) < 0) {
            current.left = leftRotate(current.left);
            return rightRotate(current);
        }

        // RL Case
        if (heightDiff < -1 && value.compareTo((E) current.right.value) > 0) {
            current.right = rightRotate(current.right);
            return leftRotate(current);
        }

        return current;
    }

    private int heightDiff(AVLNode current){
        if(current == null){
            return  0;
        }
        return getHeight(current.left) - getHeight(current.right);
    }

    private AVLNode find(AVLNode current, E value){
        if(current == null || current.value.equals(value)) {
            return current;
        }
        if(current.value.compareTo(value) > 0){
            return find(current.left, value);
        }
        else{
            return find(current.right,value);
        }
    }


    private AVLNode treeMin(AVLNode current){
        while(current.left != null){
            current = current.left;
        }
        return current;
    }
    private AVLNode treeMax(AVLNode current){
        while(current.right != null){
            current = current.right;
        }
        return current;
    }



    private AVLNode delete(AVLNode current){
            if(current.left == null || current.right == null){
                AVLNode tmp;

                if(current.left != null)
                    tmp = current.left;

                else
                    tmp = current.right;

                if(tmp == null) {
                    tmp = current;
                    current = null;
                }
                else
                    current = tmp;

                tmp = null;
            }
            else{
               AVLNode tmp = treeMin(current.right);
               current.value = tmp.value;
               current.right = delete(current.right);
            }

            current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;
            int balance = heightDiff(current);

            if (balance > 1 && heightDiff(current.left) >= 0) {
                return rightRotate(current);
            }
            // Left Right Case
            if (balance > 1 && heightDiff(current.left) < 0) {
                 current.left =  leftRotate(current.left);
                 return rightRotate(current);
            }

             // Right Right Case
            if (balance < -1 && heightDiff(current.right) <= 0) {
                return leftRotate(current);
            }
             // Right Left Case
            if (balance < -1 && heightDiff(current.right) > 0) {
                current.right = rightRotate(current.right);
                return leftRotate(current);
            }
            return current;
    }



    public AVLTree() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code ISelfBalancingSortedTreeSet} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(E value) throws NullPointerException {
        if(value == null){
            throw  new NullPointerException();
        }
        if(find(root,value) == null){
            insert(value);
            length ++;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Removes the specified element from this set if it is present.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean remove(E value) throws NullPointerException {
        if(value == null ){
            throw new NullPointerException();
        }
        if(find(root,value) == null){
            return false;
        }
        else {
            delete(find(root,value));
            length --;
            return true;
        }
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     * <p>
     * Complexity = O(log(n))
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(Object value) {
        if(value == null){
            throw new NullPointerException();
        }
        if(find(root,(E) value) == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Returns the first (lowest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() {
        if(root == null) {
            throw new NoSuchElementException();
        }
        AVLNode current = root;
        return (E) treeMin(current).value;
    }

    /**
     * Returns the last (highest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() {
        if(root == null){
            throw new NoSuchElementException();
        }
        AVLNode current = root;
        return (E) treeMax(current).value;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return  length == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        length = 0;
        root = null;
    }

    /**
     * Обходит дерево и проверяет что высоты двух поддеревьев
     * различны по высоте не более чем на 1
     *
     * @throws UnbalancedTreeException если высоты отличаются более чем на один
     */
    @Override
    public void checkBalance() throws UnbalancedTreeException {
        traverseTreeAndCheckBalanced(root);
    }

    private int traverseTreeAndCheckBalanced(AVLNode<E> curr) throws UnbalancedTreeException {
        if (curr == null) {
            return 0;
        }
        int leftHeight = traverseTreeAndCheckBalanced(curr.left);
        int rightHeight = traverseTreeAndCheckBalanced(curr.right);
        if (Math.abs(leftHeight - rightHeight) > 1) {
            throw UnbalancedTreeException.create("The heights of the two child subtrees of any node must be differ by at most one",
                    leftHeight, rightHeight, curr.toString());
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }
}

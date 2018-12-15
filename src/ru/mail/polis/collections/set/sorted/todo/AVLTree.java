package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

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
        E value;
        AVLNode<E> left;
        AVLNode<E> right;
        int height = 1;

        AVLNode(E value){
            this.value = value;
        }
    }


    public AVLTree() {
        this(Comparator.naturalOrder());
    }

    public AVLTree(Comparator<E> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        length = 0;
    }



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
        current.left = b.right;

        //rotate
        b.right = current;

        //upd
        current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;
        b.height = Math.max(getHeight(b.left), getHeight(b.right)) + 1;

        return b;

    }

    private AVLNode leftRotate(AVLNode current){
        AVLNode b = current.right;
        current.right = b.left;

        //rotate
        b.left = current;


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
        if(comparator.compare((E) current.value, value) > 0){
            current.left = insert(current.left,value);
        }
        else if(comparator.compare((E) current.value, value) < 0){
            current.right = insert(current.right, value);
        }

        current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;

        int getBalance = getBalance(current);


        /*if balanceFactor not 0 or 1, we have to do balance our Tree*/

        //LL Case
        if(getBalance > 1 && comparator.compare(value, (E) current.left.value) < 0){
            return rightRotate(current);
        }
        //RR Case
        if(getBalance < -1 && comparator.compare(value, (E) current.right.value) > 0){
            return leftRotate(current);
        }
        // LR Case
        if (getBalance > 1 && comparator.compare(value, (E) current.left.value) > 0) {
            current.left = leftRotate(current.left);
            return rightRotate(current);
        }

        // RL Case
        if (getBalance < -1 && comparator.compare(value, (E) current.right.value) < 0) {
            current.right = rightRotate(current.right);
            return leftRotate(current);
        }


        return current;
    }

    /*This is a different between left height and right height of AVL Node */
    private int getBalance(AVLNode current){ // aka balanceFactor
        if(current == null){
            return  0;
        }
        return getHeight(current.left) - getHeight(current.right);
    }

    private AVLNode find(AVLNode current, E value){
        if(current == null || current.value == null || comparator.compare((E) current.value,value) == 0) {
            return current;
        }
        if(comparator.compare((E) current.value, value) > 0){
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



    private void delete(E value){
        this.root = delete(root, value);
    }


    private AVLNode delete(AVLNode current, E value) throws NoSuchElementException{
        if(current == null){
            throw new NoSuchElementException();
        }
        if(comparator.compare(value, (E) current.value) < 0){
            current.left = delete(current.left, value);
        }
        else if(comparator.compare(value, (E) current.value) > 0){
            current.right = delete(current.right, value);
        }
        else
        {
            AVLNode<E> currentLeft = current.left;
            AVLNode<E> currentRight = current.right;

            if (currentRight == null) return currentLeft;
            AVLNode<E> min = treeMin(currentRight);
            min.right = removeMin(currentRight);
            min.left = currentLeft;
            return fixRemoveBalance(min);
        }
        return fixRemoveBalance(current);
    }

    private AVLNode<E> removeMin(AVLNode<E>  current) {
        if(current.left == null){
            return current.right;
        }
        current.left = removeMin(current.left);
        return fixRemoveBalance(current);
    }

    
    private AVLNode<E> fixRemoveBalance(AVLNode<E> current){

        current.height = Math.max(getHeight(current.left), getHeight(current.right)) + 1;

        if(getHeight(current.right) - getHeight(current.left) == 2){
            if(getHeight(current.right.left) > getHeight(current.right.right)){
                current.right = rightRotate(current.right);
            }
            return leftRotate(current);
        }
        if(getHeight(current.left) - getHeight(current.right) == 2){
            if(getHeight(current.left.right) > getHeight(current.left.left)){
                current.left = leftRotate(current.left);
            }
            return rightRotate(current);
        }
        return current;
    }


    @Override
    public boolean add(E value) throws NullPointerException {
        if(value == null){
            throw  new NullPointerException();
        }
        AVLNode current = find(root,value);
        if(current == null){
            insert(value);
            length ++;
            return true;
        }

        return false;

    }


    @Override
    public boolean remove(E value) throws NullPointerException {
        if(value == null ){
            throw new NullPointerException();
        }
        AVLNode current = find(root, value);
        if(current == null || current.value == null){
            return false;
        }
        delete(value);
        length --;
        return true;
    }


    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean contains(Object value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        AVLNode current = find(root,(E) value);
        if(current == null || current.value == null) {
            return false;
        }
        return true;
    }


    @Override
    public E first() throws  NoSuchElementException{
        if(root == null) {
            throw new NoSuchElementException();
        }
        AVLNode current = root;
        return (E) treeMin(current).value;
    }


    @Override
    public E last() throws  NoSuchElementException{
        if(root == null){
            throw new NoSuchElementException();
        }
        AVLNode current = root;
        return (E) treeMax(current).value;
    }


    @Override
    public int size() {
        return length;
    }


    @Override
    public boolean isEmpty() {
        return  root != null;
    }


    @Override
    public void clear() {
        root = null;
        length = 0;
    }


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

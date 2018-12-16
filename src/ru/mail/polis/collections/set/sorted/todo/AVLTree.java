package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    //todo: update it if required
    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left;
        AVLNode<E> right;
        public AVLNode(E value) {
            this.value = value;
        }
        int height;
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    protected int count=0;

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
        Objects.requireNonNull(comparator);
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
    public boolean add(E value) {
        Objects.requireNonNull(value);
        int size=count;
        root=add(root,value);
        return size<count;
    }

    private AVLNode<E> add(AVLNode<E> node, E value) {
        if (node==null){
            count++;
            node= new AVLNode<>(value);
        }else {
            int cmp = comparator.compare(node.value, value);
            if (cmp > 0) {
                node.left = add(node.left, value);
            } else if (cmp < 0) {
                node.right = add(node.right, value);
            }
        }
        return balanceSubTree(node);
    }

    protected void updateHeight(AVLNode<E> node){
        int leftHeight,rightHeight;
        leftHeight=node.left!=null?node.left.height:0;
        rightHeight=node.right!=null?node.right.height:0;
        node.height=Math.max(leftHeight,rightHeight)+1;
    }

    protected int differenceHeight(AVLNode<E> node){
        if (node==null) {
            return 0;
        }
        int leftHeight;
        int rightHeight;
        leftHeight=node.left!=null?node.left.height:0;
        rightHeight=node.right!=null?node.right.height:0;
        return leftHeight-rightHeight;
    }

    protected AVLNode<E> balanceSubTree(AVLNode<E> node){
        if (node==null) {
            return node;
        }
        updateHeight(node);
        int diffNodeChildren=differenceHeight(node);
        if (diffNodeChildren==2){
            if (differenceHeight(node.left)>=0){
                return smallRightRotate(node);
            }else{
                return bigRightRotate(node);
            }

        }else if (-diffNodeChildren==2){
            if (differenceHeight(node.right)<=0){
                return smallLeftRotate(node);
            }else{
                return bigLeftRotate(node);
            }
        }
        return node;
    }


    private AVLNode<E> bigLeftRotate(AVLNode<E> node) {
        node.right=smallRightRotate(node.right);
        return smallLeftRotate(node);
    }

    private AVLNode<E> bigRightRotate(AVLNode<E> node) {
        node.left=smallLeftRotate(node.left);
        return smallRightRotate(node);
    }

    private AVLNode<E> smallRightRotate(AVLNode<E> node) {
        if (node==null) {
            return node;
        }
        AVLNode<E> left=node.left;
        node.left=left.right;
        left.right=node;
        updateHeight(node);
        updateHeight(left);

        return left;
    }

    private AVLNode<E> smallLeftRotate(AVLNode<E> node) {
        if (node==null) {
            return node;
        }
        AVLNode<E> right=node.right;
        node.right=right.left;
        right.left=node;
        updateHeight(node);
        updateHeight(right);

        return right;
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
    public boolean remove(E value){
        Objects.requireNonNull(value);
        int size=count;
        root=remove(root,value);
        return size>count;
    }

    protected AVLNode<E> remove(AVLNode<E> node,E value){
        if(node==null){
            return null;
        }
        int cmp=comparator.compare(node.value,value);
        if (cmp==0){
            count--;
            AVLNode<E> left=node.left;
            AVLNode<E> right=node.right;
            if (right==null){

                node.left=null;
                return left;
            }
            AVLNode<E> minRight=findMinNode(right);
            minRight.right=removeMinNode(right);
            minRight.left=left;
            return  balanceSubTree(minRight);
        }else if(cmp<0){
            node.right=remove(node.right,value);
        }else{
            node.left=remove(node.left,value);
        }
        return balanceSubTree(node);
    }

    protected AVLNode<E> removeMinNode(AVLNode<E> node) {
        if (node.left==null){
            return node.right;
        }
        node.left=removeMinNode(node.left);
        return balanceSubTree(node);
    }

    protected AVLNode<E> findMinNode(AVLNode<E> node) {
        return node.left==null?node:findMinNode(node.left);
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
    public boolean contains(E value) {
        Objects.requireNonNull(value);
        if (isEmpty()) {
            return false;
        }
        AVLNode<E> current=root;
        int cmp;
        while((cmp=comparator.compare(current.value,value))!=0){
            if (cmp>0){
                current=current.left;
            }else
            {
                current=current.right;
            }
            if (current==null) {
                return false;
            }
        }
        return true;
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
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        AVLNode<E> currentNode=root;
        while(currentNode.left!=null){
            currentNode=currentNode.left;
        }
        return currentNode.value;
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
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        AVLNode<E> currentNode=root;
        while(currentNode.right!=null){
            currentNode=currentNode.right;
        }
        return currentNode.value;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return count==0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        root=null;
        count=0;
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

package ru.mail.polis.collections.set.sorted.todo;

import java.util.Comparator;
import java.util.NoSuchElementException;

import javax.management.openmbean.KeyAlreadyExistsException;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left, right;
        int height;
        AVLNode(E value) {
            left = null;
            right = null;
            this.value = value;
            height = 1;
        }
        AVLNode() {
            left = null;
            right = null;
            this.value = null;
            height = 1;
        }
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    private int size = 0;

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
        if (comparator == null){
            throw new NullPointerException();
        }
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
        if (value == null){
            throw new NullPointerException();
        }
        if (root == null) {
            root = new AVLNode<>(value);
            size++;
            return true;
        }
        try{
            root = addInner(root, value);
            return true;
        } catch (KeyAlreadyExistsException e) {
            return false;
        }
    }

    private AVLNode<E> addInner(AVLNode<E> node, E value) throws KeyAlreadyExistsException{
        if (node == null) {
            node = new AVLNode<>(value);
            size++;
            return node;
        }
        int compare = comparator.compare(node.value, value);
        if (compare > 0){
            node.left = addInner(node.left, value);
        } else if (compare < 0){
            node.right = addInner(node.right, value);
        } else {
            throw new KeyAlreadyExistsException();
        }
        return balance(node);
    }

    private AVLNode<E> rightRotation(AVLNode<E> node) {
        if (node == null){
            return null;
        }
        AVLNode<E> nl = node.left;
        node.left = nl.right;
        nl.right = node;
        calcHeight(node);
        calcHeight(nl);
        return nl;
    }
    private AVLNode<E> leftRotation(AVLNode<E> node) {
        if (node == null){
            return null;
        }
        AVLNode<E> nr = node.right;
        node.right = nr.left;
        nr.left = node;
        calcHeight(node);
        calcHeight(nr);
        return nr;
    }

    private int balanceFactor(AVLNode<E> node) {
        if (node == null){
            return 0;
        }
        return height(node.right) - height(node.left);
    }

    private void calcHeight(AVLNode<E> node) {
        node.height = max(height(node.left), height(node.right)) + 1;
    }

    private int height(AVLNode<E> node )
    {
        return node == null ? 0 : node.height;
    }

    private int max(int lhs, int rhs)
    {
        return lhs > rhs ? lhs : rhs;
    }

    private AVLNode<E> balance(AVLNode<E> node) {
        calcHeight(node);
        if (balanceFactor(node) == 2) {
            if (balanceFactor(node.right) < 0)
                node.right = rightRotation(node.right);
            return leftRotation(node);
        }
        if (balanceFactor(node) == -2) {
            if (balanceFactor(node.left) > 0)
                node.left = leftRotation(node.left);
            return rightRotation(node);
        }
        return node;
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
    public boolean remove(E value) {
        if (value == null){
            throw new NullPointerException();
        }
        if (root == null) {
            return false;
        }
        try {
            root = remove(root, value);
            size--;
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private AVLNode<E> remove(AVLNode<E> node, E value){
        if (node == null)
            throw new NoSuchElementException();
        if (comparator.compare(value, node.value) < 0) {
            node.left = remove(node.left, value);
        } else if(comparator.compare(value, node.value) > 0) {
            node.right = remove(node.right, value);
        } else {
            AVLNode<E> left = node.left;
            AVLNode<E> right = node.right;
            if (right == null){
                return left;
            }
            AVLNode<E> min = first(right);
            min.right = removeMin(right);
            min.left = left;
            return balance(min);
        }
        return balance(node);
    }

    private AVLNode<E> first(AVLNode<E> node){
        if (node == null){
            throw new NoSuchElementException();
        }
        AVLNode<E> n = node;
        while (n.left != null){
            n = n.left;
        }
        return n;
    }

    private AVLNode<E> removeMin(AVLNode<E> node) {
        if (node.left == null)
            return node.right;
        node.left = removeMin(node.left);
        return balance(node);
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
        if (value == null){
            throw new NullPointerException();
        }
        AVLNode<E> node = root;
        while (node != null) {
            if (comparator.compare(node.value, value) == 0)
                return true;
            if (comparator.compare(value, node.value) < 0)
                node = node.left;
            else
                node = node.right;
        }
        return false;
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
    public E first() throws NoSuchElementException {
        return first(root).value;
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
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        AVLNode<E> node = root;
        while(node.right != null) {
            node = node.right;
        }
        return node.value;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        size = 0;
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

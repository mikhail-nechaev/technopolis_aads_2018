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
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {
    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left;
        AVLNode<E> right;
        int height;

        protected AVLNode(E value){
            this.value = value;
            this.height = 1;
        }
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    protected int size = 0;


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

    private int height(AVLNode<E> cur){
        return cur == null ? 0 : cur.height;
    }

    private void recountHeight(AVLNode<E> cur){
        int rightHeight = height(cur.right);
        int leftHeight = height(cur.left);
        cur.height = (rightHeight > leftHeight ? rightHeight : leftHeight) + 1;
    }

    private AVLNode<E> smallLeftRotation(AVLNode<E> cur){
        AVLNode<E> rightChild = cur.right;
        cur.right = rightChild.left;
        rightChild.left = cur;
        recountHeight(cur);
        recountHeight(rightChild);
        return rightChild;
    }

    private AVLNode<E> smallRightRotation(AVLNode<E> cur){
        AVLNode<E> leftChild = cur.left;
        cur.left = leftChild.right;
        leftChild.right = cur;
        recountHeight(cur);
        recountHeight(leftChild);
        return leftChild;
    }

    private AVLNode<E> balance(AVLNode<E> cur){
        recountHeight(cur);
        if(height(cur.right) - height(cur.left) == 2){
            if(height(cur.right.left) > height(cur.right.right)){
                cur.right = smallRightRotation(cur.right);
            }
            return smallLeftRotation(cur);
        }
        if(height(cur.left) - height(cur.right) == 2){
            if(height(cur.left.right) > height(cur.left.left)){
                cur.left = smallLeftRotation(cur.left);
            }
            return smallRightRotation(cur);
        }
        return cur;
    }

    private AVLNode<E> add(E value, AVLNode<E> cur){
        if(cur == null){
            size++;
            return new AVLNode<>(value);
        }
        int cmp = comparator.compare(value, cur.value);
        if (cmp < 0) {
            cur.left = add(value, cur.left);
        } else if (cmp > 0){
            cur.right = add(value, cur.right);
        }
        return balance(cur);
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
        if (value == null) {
            throw new NullPointerException();
        }
        int oldSize = size;
        root = add(value, root);
        return size - oldSize != 0;
    }

    private AVLNode<E> findMin(AVLNode<E> cur){
        AVLNode<E> min = cur;
        while(min.left != null){
            min = min.left;
        }
        return min;
    }

    private AVLNode<E> removeMin(AVLNode<E> cur){
        if(cur.left == null){
            return cur.right;
        }
        cur.left = removeMin(cur.left);
        return balance(cur);
    }

    private AVLNode<E> findMax(AVLNode<E> cur){
        AVLNode<E> max = cur;
        while(max.right != null){
            max = max.right;
        }
        return max;
    }
    private AVLNode<E> remove(E value, AVLNode<E> cur){
        if(cur == null){
            return null;
        }
        int cmp = comparator.compare(value, cur.value);
        if (cmp < 0) {
            cur.left = remove(value, cur.left);
        } else if (cmp > 0){
            cur.right = remove(value, cur.right);
        } else{
            size--;
            AVLNode<E> leftChild = cur.left;
            AVLNode<E> rightChild = cur.right;
            if(rightChild == null){
                return leftChild;
            }
            if(leftChild == null){
                return rightChild;
            }
            AVLNode<E> min = findMin(rightChild);
            min.right = removeMin(rightChild);
            min.left = leftChild;

            return balance(min);
        }
        return balance(cur);
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
        if (value == null) {
            throw new NullPointerException();
        }
        int oldSize = size;
        root = remove(value, root);
        return size - oldSize != 0;
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
        if(value == null){
            throw new NullPointerException();
        }
        AVLNode<E> cur = root;
        int cmp;
        do {
            if(cur == null){
                return false;
            }
            cmp = comparator.compare(value, cur.value);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            }
        } while(cmp != 0);
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
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return findMin(root).value;
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
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return findMax(root).value;
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
        root = null;
        size = 0;
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

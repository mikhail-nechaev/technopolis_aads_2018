package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 * <p>
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    protected static class AVLNode<E> {
        E value;
        int height = 1;
        AVLNode<E> left;
        AVLNode<E> right;
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    int size = 0;

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
        if (comparator == null)
            throw new NullPointerException();
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
        if (value == null)
            throw new NullPointerException();
        if (!contains(root, value)) {
            root = add(value, root);
            size++;
            return true;
        }
        return false;
    }

    AVLNode<E> add(E value, AVLNode<E> node) {
        if (node == null) {
            AVLNode<E> avlNode = new AVLNode<>();
            avlNode.value = value;
            return avlNode;
        }
        if (comparator.compare(node.value, value) < 0)
            node.right = add(value, node.right);
        else
            node.left = add(value, node.left);

        node.height = getTreeHeight(node);

        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor < -1 && comparator.compare(value, node.right.value) > 0)
            return rotateLeft(node);

        if (balanceFactor > 1 && comparator.compare(value, node.left.value) < 0)
            return rotateRight(node);

        if (balanceFactor < -1 && comparator.compare(value, node.right.value) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        if (balanceFactor > 1 && comparator.compare(value, node.left.value) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        return node;
    }


    private AVLNode<E> rotateRight(AVLNode<E> node) {
        AVLNode<E> newRootNode = node.left;
        node.left = newRootNode.right;
        newRootNode.right = node;
        node.height = getTreeHeight(node);
        newRootNode.height = getTreeHeight(newRootNode);
        return newRootNode;
    }

    private AVLNode<E> rotateLeft(AVLNode<E> node) {
        AVLNode<E> newRootNode = node.right;
        node.right = newRootNode.left;
        newRootNode.left = node;
        node.height = getTreeHeight(node);
        newRootNode.height = getTreeHeight(newRootNode);
        return newRootNode;
    }

    private int getTreeHeight(AVLNode node) {
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }


    private int getHeight(AVLNode<E> node) {
        return node == null ? 0 : node.height;
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
        if (value == null)
            throw new NullPointerException();
        if (!contains(value))
            return false;
        root = remove(value, root);
        size--;
        return true;
    }

    private AVLNode<E> remove(E value, AVLNode<E> node) {
        if (comparator.compare(value, node.value) == 0) {
            if (node.left == null)
                return node.right;
            else if (node.right == null)
                return node.left;
            else {
                AVLNode tmp = getMax(node.left);
                node.value = (E) tmp.value;
                node.left = remove((E) tmp.value, node.left);
            }
        } else if (comparator.compare(value, node.value) > 0)
            node.right = remove(value, node.right);
        else
            node.left = remove(value, node.left);

        node.height = getTreeHeight(node);
        return balanceTree(node);
    }

    AVLNode<E> balanceTree(AVLNode<E> node) {

        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0)
                node.left = rotateLeft(node.left);

            return rotateRight(node);
        }

        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0)
                node.right = rotateRight(node.right);

            return rotateLeft(node);
        }

        return node;
    }

    int getBalanceFactor(AVLNode node) {
        if (node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }


    private AVLNode getMax(AVLNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private AVLNode getMin(AVLNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
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
        if (value == null)
            throw new NullPointerException();
        if (!contains(root, value))
            return false;
        return true;
    }

    public boolean contains(AVLNode node, E value) {
        if (node == null)
            return false;

        if (comparator.compare((E) node.value, value) == 0)
            return true;

        if (comparator.compare((E) node.value, value) < 0)
            return contains(node.right, value);
        else
            return contains(node.left, value);

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
        if (isEmpty())
            throw new NoSuchElementException();
        return (E) getMin(root).value;
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
        if (isEmpty())
            throw new NoSuchElementException();
        return (E) getMax(root).value;
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
        return root == null;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
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

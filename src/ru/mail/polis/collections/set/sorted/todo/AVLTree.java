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

        AVLNode(E value) {
            this.value = value;
        }
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    protected int size = 0;
    protected int modCount = 0;

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
        if (comparator == null) {
            throw new NullPointerException("Comporator is null");
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
        if (value == null) {
            throw new NullPointerException();
        }
        int oldMC = modCount;
        root = insertNode(root, value);
        if (oldMC != modCount) {
            size++;
            return true;
        } else {
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
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        int oldMC = modCount;
        root = removeNode(root, value);
        if (oldMC != modCount) {
            size--;
            return true;
        } else {
            return false;
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
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        int compRes;
        AVLNode<E> i = root;
        while (i != null) {
            compRes = comparator.compare(i.value, value);
            if (compRes == 0) {
                return true;
            } else {
                i =  compRes > 0 ? i.left : i.right;
            }
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
    public E first() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return findMinInSubTree(root).value;
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
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return findMaxInSubTree(root).value;
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
        modCount++;
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

    public int height(AVLNode<E> node) {
        return node == null ? -1 : node.height;
    }

    private int balanceDiff(AVLNode<E> node) {
        return height(node.right) - height(node.left);
    }

    private void setHeight(AVLNode<E> node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private AVLNode<E> leftRotate(AVLNode<E> node) {
        AVLNode<E> temp = node.right;
        node.right = temp.left;
        temp.left = node;
        setHeight(node);
        setHeight(temp);
        return temp;
    }

    private AVLNode<E> rightRotate(AVLNode<E> node) {
        AVLNode<E> temp = node.left;
        node.left = temp.right;
        temp.right = node;
        setHeight(node);
        setHeight(temp);
        return temp;
    }

    private AVLNode<E> makeBalanced(AVLNode<E> node) {
        setHeight(node);
        if (balanceDiff(node) >= 2) {
            if (balanceDiff(node.right) < 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }
        if (balanceDiff(node) <= -2) {
            if (balanceDiff(node.left) > 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }
        return node;
    }

    private AVLNode<E> insertNode(AVLNode<E> node, E value) {
        if (node == null) {
            modCount++;
            return new AVLNode<E>(value);
        }
        if (comparator.compare(value, node.value) < 0) {
            node.left = insertNode(node.left, value);
        } else if (comparator.compare(value, node.value) > 0) {
            node.right = insertNode(node.right, value);
        }
        return makeBalanced(node);
    }

    private AVLNode<E> removeNode(AVLNode<E> node, E value) {
        if (node == null) {
            return null;
        }
        if (comparator.compare(value, node.value) == 0) {
            modCount++;
            AVLNode<E> left = node.left;
            AVLNode<E> right = node.right;
            node = null;
            if (right == null) {
                return left;
            }
            AVLNode<E> minNode = findMinInSubTree(right);
            minNode.right = removeMinInSubTree(right);
            minNode.left = left;
            return makeBalanced(minNode);
        } else {
            if (comparator.compare(value, node.value) < 0) {
                node.left = removeNode(node.left, value);
            } else {
                node.right = removeNode(node.right, value);
            }
        }
        return makeBalanced(node);
    }

    protected AVLNode<E> findMinInSubTree(AVLNode<E> node) {
        if (node.left != null) {
            return findMinInSubTree(node.left);
        } else {
            return node;
        }
    }

    private AVLNode<E> findMaxInSubTree(AVLNode<E> node) {
        if (node.right != null) {
            return findMaxInSubTree(node.right);
        } else {
            return node;
        }
    }

    private AVLNode<E> removeMinInSubTree(AVLNode<E> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMinInSubTree(node.left);
        return makeBalanced(node);
    }
}

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
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    //todo: update it if required
    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left;
        AVLNode<E> right;
        AVLNode<E> parent;
        int height = 1;

        AVLNode(E value) {
            this.value = value;
        }

    }

    private int getHeight(final AVLNode<E> node) {
        return (node == null) ? 0 : node.height;
    }

    private int balanceFactor(final AVLNode<E> node) {
        return getHeight(node.left) - getHeight(node.right);
    }

    private void fixHeight(AVLNode<E> node) {
        int heightLeft = getHeight(node.left);
        int heightRight = getHeight(node.right);

        node.height = Math.max(heightLeft, heightRight) + 1;

    }

    private AVLNode<E> rotateLeft(AVLNode<E> node) {
        AVLNode<E> rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
        fixHeight(node);
        fixHeight(rightChild);

        return rightChild;
    }

    private AVLNode<E> rotateRight(AVLNode<E> node) {
        AVLNode<E> leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.left) {
            node.parent.left = leftChild;
        } else {
            node.parent.right = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
        fixHeight(node);
        fixHeight(leftChild);


        return leftChild;
    }

    private boolean insert(AVLNode<E> node) {
        if (root == null) {
            root = node;
            size++;
            return true;
        }

        AVLNode<E> parent = null;
        AVLNode<E> n = root;

        while (n != null && comparator.compare(node.value, n.value) != 0) {
            parent = n;
            n = (comparator.compare(node.value, n.value) < 0)
                    ? n.left
                    : n.right;
        }

        if (n == null) {
            node.parent = parent;
            if (comparator.compare(node.value, parent.value) < 0) {
                parent.left = node;
            } else {
                parent.right = node;
            }

            size++;
            AVLNode<E> nodeBalanced = node.parent;
            while (nodeBalanced != null) {
                nodeBalanced = balance(nodeBalanced);
                nodeBalanced = nodeBalanced.parent;
            }

            return true;
        }

        return false;
    }

    private AVLNode<E> balance(AVLNode<E> node) {
        fixHeight(node);

        if (balanceFactor(node) == 2) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        } else if (balanceFactor(node) == -2) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }

        return node;

    }

    private AVLNode<E> findInorderSuccessor(AVLNode<E> node) {
        if (node == null) {
            return null;
        }

        AVLNode<E> n = node;
        while (n.left != null) {
            n = n.left;
        }

        return n;
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    private AVLNode<E> root;
    protected int size;

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
    public boolean add(E value) {
        return insert(new AVLNode<>(Objects.requireNonNull(value)));
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
        Objects.requireNonNull(value);
        AVLNode<E> removedNode = root;
        while (removedNode != null && comparator.compare(value, removedNode.value) != 0) {
            removedNode = (comparator.compare(value, removedNode.value) < 0)
                    ? removedNode.left
                    : removedNode.right;
        }
        if (removedNode == null) {
            return false;
        }
        AVLNode<E> nodeBalanced;
        AVLNode<E> minRight = findInorderSuccessor(removedNode.right);
        minRight = (minRight != null) ? minRight : removedNode.left;
        if (minRight != null) {
            removedNode.value = minRight.value;
            if (minRight.right != null) {
                minRight = rotateLeft(minRight).left;
                minRight.parent.left = null;
            } else {
                if (minRight.parent.left == minRight) {
                    minRight.parent.left = null;
                } else {
                    minRight.parent.right = null;
                }
            }
            nodeBalanced = minRight.parent;
        } else {
            nodeBalanced = removedNode.parent;
            if (nodeBalanced != null) {
                if (removedNode.parent.left == removedNode) {
                    removedNode.parent.left = null;
                } else {
                    removedNode.parent.right = null;
                }
            } else {
                root = null;
            }
        }
        size--;
        while (nodeBalanced != null) {
            nodeBalanced = balance(nodeBalanced);
            nodeBalanced = nodeBalanced.parent;
        }
        return true;
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
        AVLNode<E> node = root;
        while (node != null && comparator.compare(value, node.value) != 0) {
            node = (comparator.compare(value, node.value) < 0)
                    ? node.left
                    : node.right;
        }
        return node != null;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return findInorderSuccessor(root).value;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        AVLNode<E> node = root;
        while (node.right != null) {
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

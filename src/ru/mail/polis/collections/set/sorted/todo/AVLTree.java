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
        int height;

        AVLNode(AVLNode<E> left, E value, int height, AVLNode<E> right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = height;
        }
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    private int size;

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
        this.comparator = Objects.requireNonNull(comparator);
        init();
    }

    private void init() {
        root = null;
        size = 0;
    }

    //  Обертка над полем в ноде, которая не ломается на null
    private int height(AVLNode<E> avlNode) {
        return avlNode != null ? avlNode.height : 0;
    }

    private int balanceFactor(AVLNode<E> avlNode) {
        if (avlNode == null) return 0; //костыль
        return height(avlNode.right) - height(avlNode.left);
    }

    private void fixHeight(AVLNode<E> avlNode) {
        if (avlNode == null) { //костыль
            return;
        }
        int heightOfLeft = height(avlNode.left);
        int heightOfRight = height(avlNode.right);
        avlNode.height = ((heightOfLeft > heightOfRight) ? heightOfLeft : heightOfRight) + 1;
    }

    private AVLNode<E> rotateRight(AVLNode<E> avlNode) {
        if (avlNode == null) {
            return null;
        }
        AVLNode<E> tmp = avlNode.left;
        avlNode.left = tmp.right;
        tmp.right = avlNode;
        fixHeight(avlNode);
        fixHeight(tmp);
        return tmp;
    }

    private AVLNode<E> rotateLeft(AVLNode<E> avlNode) {
        if (avlNode == null) {
            return null;
        }
        AVLNode<E> tmp = avlNode.right;
        avlNode.right = tmp.left;
        tmp.left = avlNode;
        fixHeight(avlNode);
        fixHeight(tmp);
        return tmp;
    }

    private AVLNode<E> balance(AVLNode<E> avlNode) {
        fixHeight(avlNode);
        if (balanceFactor(avlNode) == 2) {
            if (balanceFactor(avlNode.right) < 0) {
                avlNode.right = rotateRight(avlNode.right);
            }
            return rotateLeft(avlNode);
        }
        if (balanceFactor(avlNode) == -2) {
            if (balanceFactor(avlNode.left) > 0) {
                avlNode.left = rotateLeft(avlNode.left);
            }
            return rotateRight(avlNode);
        }
        return avlNode;
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
        try {
            root = insert(root, value);
            size++;
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

        //return add(root, value);
    }

    /**
     * @throws IllegalArgumentException if specified value already exists in this tree
     *
     */
    private AVLNode<E> insert(AVLNode<E> node, E value) {
        if (node == null) {
            return new AVLNode<>(null, value, 1, null);
        }
        if (comparator.compare(value, node.value) == 0) {
            throw new IllegalArgumentException();
        }
        if (comparator.compare(value, node.value) < 0) {
            node.left = insert(node.left, value);
        } else {
            node.right = insert(node.right, value);
        }
        return balance(node);
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
        try {
            root = delete(value, root);
            size--;
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * @throws NoSuchElementException if specified value does not exist in this tree
     *
     */
    private AVLNode<E> delete(E value, AVLNode<E> avlNode) {
        if (avlNode == null) {
            throw new NoSuchElementException();
        }
        if (comparator.compare(value, avlNode.value) < 0) {
            avlNode.left = delete(value, avlNode.left);
        } else if (comparator.compare(value, avlNode.value) > 0) {
            avlNode.right = delete(value, avlNode.right);
        } else {
            AVLNode<E> left = avlNode.left;
            AVLNode<E> right = avlNode.right;
            avlNode.value = null;
            avlNode = null;
            if (right == null) return left;
            AVLNode<E> min = findMin(right);
            min.right = removeMin(right);
            min.left = left;
            return balance(min);
        }
        return balance(avlNode);
    }

    private AVLNode<E> findMin(AVLNode<E> avlNode) {
        AVLNode<E> tmp = avlNode;
        while (tmp.left != null) {
            tmp = tmp.left;
        }
        return tmp;
    }

    private AVLNode<E> removeMin(AVLNode<E> avlNode) {
        AVLNode<E> tmp = avlNode;
        if (tmp.left == null) {
            return tmp.right;
        }
        tmp.left = removeMin(tmp.left);
        return balance(tmp);
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
        return contains(value, root);
    }

    private boolean contains(E value, AVLNode<E> avlNode) {
        if (avlNode == null) {
            return false;
        }
        if (comparator.compare(value, avlNode.value) == 0) {
            return true;
        }
        if (comparator.compare(value, avlNode.value) < 0) {
            return contains(value, avlNode.left);
        } else {
            return contains(value, avlNode.right);
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
        throwExceptionIfEmpty();
        return first(root);
    }

    private E first(AVLNode<E> avlNode) {
        if (avlNode.left == null) {
            return avlNode.value;
        }
        return first(avlNode.left);
    }

    private void throwExceptionIfEmpty() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
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
        throwExceptionIfEmpty();
        return last(root);
    }

    private E last(AVLNode<E> avlNode) {
        if (avlNode.right == null) {
            return avlNode.value;
        }
        return last(avlNode.right);
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
        init();
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

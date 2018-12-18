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
        AVLNode<E> parent;
        int leftHeight, rightHeight;

        public AVLNode(E value) {
            this();
            this.value = value;
        }

        protected AVLNode() {
            leftHeight = rightHeight = 0;
        }
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    protected int size, modCount;

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
        if (comparator == null) throw new NullPointerException();
        this.comparator = comparator;
        root = null;
        size = 0;
        modCount = 0;
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
        if (value == null) throw new NullPointerException();
        if (!contains(value)) {
            root = insert(root, value);
            size++;
            modCount++;
            return true;
        }
        return false;
    }

    protected AVLNode<E> insert(AVLNode<E> p, E value) {
        if (p == null) {
            return new AVLNode<E>(value);
        }
        if (comparator.compare(value, p.value) < 0) {
            if (p.left == null) {
                p.left = new AVLNode<E>(value);
                p.left.parent = p;
                resize(p);
                return balance(p);
            }
            p.left = insert(p.left, value);
            resize(p);
        } else {
            if (p.right == null) {
                p.right = new AVLNode<E>(value);
                p.right.parent = p;
                resize(p);
                return balance(p);
            }
            p.right = insert(p.right, value);
            resize(p);
        }
        return balance(p);
    }

    private AVLNode<E> balance(AVLNode<E> node) {
        resize(node);
        if (diff(node) == 2) {
            if (diff(node.right) < 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }
        if (diff(node) == -2) {
            if (diff(node.left) > 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }
        return node;
    }

    private void resize(AVLNode<E> node) {
        if (node != null) {
            node.rightHeight = getHeight(node.right);
            node.leftHeight = getHeight(node.left);
        }
    }

    public int getHeight(AVLNode<E> node) {
        if (node == null) return 0;
        return Math.max(node.leftHeight, node.rightHeight) + 1;
    }

    private AVLNode<E> rightRotate(AVLNode<E> p) {
        boolean leftSon = leftSon(p);
        AVLNode<E> q = p.left;
        p.left = q.right;
        if (p.left != null) p.left.parent = p;
        q.right = p;
        q.parent = p.parent;
        p.parent = q;
        resize(p);
        resize(q);
        return q;
    }

    private AVLNode<E> leftRotate(AVLNode<E> q) {
        AVLNode<E> b = q.right;
        boolean leftSon = leftSon(q);
        q.right = b.left;
        if (q.right != null) q.right.parent = q;
        b.left = q;
        b.parent = q.parent;
        q.parent = b;
        resize(q);
        resize(b);
        return b;
    }

    protected boolean leftSon(AVLNode<E> node) {
        return node.parent != null && node.parent.left == node;
    }

    public int diff(AVLNode<E> node) {
        return node.rightHeight - node.leftHeight;
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
        if (value == null) throw new NullPointerException();
        if (!contains(value)) return false;
        root = remove(root, value);
        size--;
        modCount++;
        return true;
    }

    private AVLNode<E> remove(AVLNode<E> node, E value) {
        if (node == null) return null;
        final int resCompare = comparator.compare(value, node.value);
        if (resCompare > 0) {
            node.right = remove(node.right, value);
            if (node.right != null) node.right.parent = node;
        } else if (resCompare < 0) {
            node.left = remove(node.left, value);
            if (node.left != null) {
                //        node.left.parent = node;
            }
        } else {
            AVLNode<E> currentLeft = node.left;
            AVLNode<E> currentRight = node.right;
            if (currentRight == null) return currentLeft;
            AVLNode<E> min = getMin(currentRight);
            currentRight = deleteMin(currentRight);
            if (currentRight != null) currentRight.parent = min;
            if (currentLeft != null) currentLeft.parent = min;
            min.right = currentRight;
            min.left = currentLeft;
            return balance(min);
        }
        return balance(node);
    }

    protected AVLNode<E> getMin(AVLNode<E> x) {
        if (x == null) return null;
        return x.left == null ? x : getMin(x.left);
    }

    private AVLNode<E> deleteMin(AVLNode<E> node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        if (node.left != null) node.left.parent = node;
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
        if (value == null) {
            throw new NullPointerException();
        }
        return contains(root, value);
    }

    private boolean contains(AVLNode<E> node, E value) {
        if (node == null || node.value == null) return false;
        int resCompare = comparator.compare(value, node.value);
        if (resCompare == 0) return true;
        if (resCompare > 0) return contains(node.right, value);
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
        if (isEmpty()) throw new NoSuchElementException();
        return getMin(root).value;
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
        if (isEmpty()) throw new NoSuchElementException();
        return getMax(root).value;
    }

    protected AVLNode<E> getMax(AVLNode<E> node) {
        if (node == null) return null;
        return node.right == null ? node : getMax(node.right);
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
        size = 0;
        modCount = 0;
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

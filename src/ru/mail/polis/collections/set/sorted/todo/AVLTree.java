package ru.mail.polis.collections.set.sorted.todo;

import java.util.Comparator;
import java.util.NoSuchElementException;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
@SuppressWarnings({"ALL", "unchecked"})
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {


    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left;
        AVLNode right;
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


    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    protected int length;

    public AVLTree() {
        this(Comparator.naturalOrder());
    }

    public AVLTree(Comparator<E> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = comparator;
        root = null;
        length = 0;
    }


    @Override
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (!contains(value)) {
            root = insert(root, value);
            length++;
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
                remakeHeight(p);
                return balanceNode(p);
            }
            p.left = insert(p.left, value);
            remakeHeight(p);
        } else {
            if (p.right == null) {
                p.right = new AVLNode<E>(value);
                p.right.parent = p;
                remakeHeight(p);
                return balanceNode(p);
            }
            p.right = insert(p.right, value);
            remakeHeight(p);
        }
        return balanceNode(p);
    }


    private AVLNode<E> balanceNode(AVLNode<E> node) {
        remakeHeight(node);
        if (getDiff(node) == 2) {
            if (getDiff(node.right) < 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }
        if (getDiff(node) == -2) {
            if (getDiff(node.left) > 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }
        return node;
    }


    private AVLNode<E> rightRotate(AVLNode<E> p) {
        boolean isLeftSon = isLeftSon(p);
        AVLNode<E> q = p.left;
        p.left = q.right;
        if (p.left != null) {
            p.left.parent = p;
        }
        q.right = p;
        q.parent = p.parent;
        p.parent = q;
        remakeHeight(p);
        remakeHeight(q);
        return q;
    }


    private AVLNode<E> leftRotate(AVLNode<E> q) {
        AVLNode<E> b = q.right;
        boolean isLeftSon = isLeftSon(q);
        q.right = b.left;
        if (q.right != null) {
            q.right.parent = q;
        }
        b.left = q;
        b.parent = q.parent;
        q.parent = b;
        remakeHeight(q);
        remakeHeight(b);
        return b;
    }

    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (!contains(value)) {
            return false;
        }
        root = remove2(root, value);
        length--;
        return true;
    }


    private AVLNode<E> remove2(AVLNode<E> node, E value) {

        if (node == null) {
            return null;
        }
        final int resCompare = comparator.compare(value, node.value);
        if (resCompare > 0) {
            node.right = remove2(node.right, value);
            if (node.right != null) {
                node.right.parent = node;
            }
        } else if (resCompare < 0) {
            node.left = remove2(node.left, value);
            if (node.left != null) {
                //        node.left.parent = node;
            }
        } else {
            AVLNode<E> currentLeft = node.left;
            AVLNode<E> currentRight = node.right;
            if (currentRight == null) {
                return currentLeft;
            }
            AVLNode<E> min = getMin(currentRight);
            currentRight = deleteMin(currentRight);
            if (currentRight != null) {
                currentRight.parent = min;
            }
            if (currentLeft != null) {
                currentLeft.parent = min;
            }
            min.right = currentRight;
            min.left = currentLeft;
            return balanceNode(min);
        }
        return balanceNode(node);
    }

    private AVLNode<E> deleteMin(AVLNode<E> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        if (node.left != null) {
            node.left.parent = node;
        }
        return balanceNode(node);
    }

    protected AVLNode<E> getMin(AVLNode<E> x) {
        if (x == null) {
            return null;
        }
        return x.left == null ? x : getMin(x.left);
    }

    protected boolean isLeftSon(AVLNode<E> node) {
        return node.parent != null && node.parent.left == node;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return containsElemInTree(root, value);
    }

    private boolean containsElemInTree(AVLNode<E> node, E value) {
        if (node == null || node.value == null) {
            return false;
        }
        int resCompare = comparator.compare(value, node.value);
        if (resCompare == 0) {
            return true;
        }
        if (resCompare > 0) {
            return containsElemInTree(node.right, value);
        }
        return containsElemInTree(node.left, value);
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getMin(root).value;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getMax(root).value;
    }

    protected AVLNode<E> getMax(AVLNode<E> node) {
        if (node == null) {
            return null;
        }
        return node.right == null ? node : getMax(node.right);
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
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

    public int getHeight(AVLNode<E> node) {
        if (node == null) {
            return 0;
        }
        return Math.max(node.leftHeight, node.rightHeight) + 1;
    }

    public int getDiff(AVLNode<E> node) {
        return node.rightHeight - node.leftHeight;
    }

    private void remakeHeight(AVLNode<E> node) {
        if (node != null) {
            node.rightHeight = getHeight(node.right);
            node.leftHeight = getHeight(node.left);
        }
    }

}
package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;


@SuppressWarnings("unchecked")
public class RedBlackTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    private enum RBColor {
        RED, BLACK
    }

    static final class RBNode<E> {
        E value;
        RBNode<E> left;
        RBNode<E> right;
        RBNode<E> parent;
        RBColor color = RBColor.BLACK;

        @Override
        public String toString() {
            return "RBNode{" +
                    "value=" + value +
                    ", left=" + left +
                    ", right=" + right +
                    ", color=" + color +
                    '}';
        }
    }


    protected final Comparator<E> comparator;
    protected RBNode nil = new RBNode(), root = nil;
    int size = 0;


    public RedBlackTree() {
        this(Comparator.naturalOrder());
    }

    public RedBlackTree(Comparator comparator) {
        if (comparator == null)
            throw new NullPointerException();
        this.comparator = comparator;
    }


    private void rotateLeft(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;

        if (y.left != nil)
            y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == nil)
            root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else
            x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(RBNode x) {
        RBNode y = x.left;
        x.left = y.right;

        if (y.right != nil)
            y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == nil)
            root = y;
        else if (x == x.parent.right)
            x.parent.right = y;
        else
            x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void insert(RBNode z) {
        RBNode y = nil;
        RBNode x = root;
        while (x != nil) {
            y = x;
            if (comparator.compare((E) z.value, (E) x.value) < 0)
                x = x.left;
            else
                x = x.right;
        }
        z.parent = y;
        if (y == nil)
            root = z;
        else if (comparator.compare((E) z.value, (E) y.value) < 0)
            y.left = z;
        else
            y.right = z;
        z.left = nil;
        z.right = nil;
        z.color = RBColor.RED;
        insertFixUp(z);
    }

    private void insertFixUp(RBNode z) {
        RBNode y;
        while (z.parent.color == RBColor.RED) {
            if (z.parent == z.parent.parent.left) {
                y = z.parent.parent.right;
                if (y.color == RBColor.RED) {
                    z.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    z = z.parent.parent;
                } else if (z == z.parent.right) {
                    z = z.parent;
                    rotateLeft(z);
                } else {
                    z.parent.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                y = z.parent.parent.left;
                if (y.color == RBColor.RED) {
                    z.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    z = z.parent.parent;
                } else if (z == z.parent.left) {
                    z = z.parent;
                    rotateRight(z);
                } else {
                    z.parent.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = RBColor.BLACK;
    }

    public RBNode contains(RBNode node, E value) {
        if (node == nil)
            return null;

        if (comparator.compare((E) node.value, value) == 0)
            return node;

        if (comparator.compare((E) node.value, value) < 0)
            return contains(node.right, value);
        else
            return contains(node.left, value);

    }


    @Override
    public boolean add(E value) throws NullPointerException {
        if (value == null)
            throw new NullPointerException();
        if (contains(root, value) == null) {
            RBNode<E> newNode = new RBNode<>();
            newNode.value = value;
            insert(newNode);
            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(E value) throws NullPointerException {
        if (value == null)
            throw new NullPointerException();
        RBNode<E> node = contains(root, value);
        if (!contains(value))
            return false;
        remove(node);
        size--;
        return true;
    }

    private RBNode<E> remove(RBNode<E> z) {
        RBNode<E> y, x;
        if (z.left == nil || z.right == nil)
            y = z;
        else
            y = treeSuccessor(z);
        if (y.left != nil)
            x = y.left;
        else
            x = y.right;
        x.parent = y.parent;
        if (y.parent == nil)
            root = x;
        else if (y == y.parent.left)
            y.parent.left = x;
        else
            y.parent.right = x;
        if (y != z)
            z.value = y.value;
        if (y.color == RBColor.BLACK)
            deleteFixUp(x);
        return y;
    }

    private RBNode<E> treeSuccessor(RBNode<E> x) {
        if (x.left != null)
            return getMin(x.right);
        RBNode y = x.parent;
        while (y != null && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private void deleteFixUp(RBNode x) {

        RBNode w;
        while (x != root && x.color == RBColor.BLACK) {
            if (x == x.parent.left) {
                w = x.parent.right;
                if (w.color == RBColor.RED) {
                    w.color = RBColor.BLACK;
                    x.parent.color = RBColor.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == RBColor.BLACK &&
                        w.right.color == RBColor.BLACK) {
                    w.color = RBColor.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == RBColor.BLACK) {
                        w.left.color = RBColor.BLACK;
                        w.color = RBColor.RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBColor.BLACK;
                    w.right.color = RBColor.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                w = x.parent.left;
                if (w.color == RBColor.RED) {
                    w.color = RBColor.BLACK;
                    x.parent.color = RBColor.RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == RBColor.BLACK &&
                        w.left.color == RBColor.BLACK) {
                    w.color = RBColor.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == RBColor.BLACK) {
                        w.right.color = RBColor.BLACK;
                        w.color = RBColor.RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBColor.BLACK;
                    w.left.color = RBColor.BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = RBColor.BLACK;
    }

    @Override
    public boolean contains(E value) throws NullPointerException {
        if (value == null)
            throw new NullPointerException();
        if (contains(root, value) == null)
            return false;
        return true;
    }


    @Override
    public E first() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException();

        return getMin(root).value;
    }

    RBNode<E> getMin(RBNode node) {
        while (node.left != nil)
            node = node.left;

        return node;
    }

    @Override
    public E last() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException();

        return getMax(root).value;
    }


    RBNode<E> getMax(RBNode node) {
        while (node.right != nil)
            node = node.right;

        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == nil;
    }

    @Override
    public void clear() {
        root = nil;
        size = 0;
    }

    @Override
    public void checkBalance() throws UnbalancedTreeException {
        if (root != null) {
            if (root.color != RBColor.BLACK) {
                throw new UnbalancedTreeException("Root must be black");
            }
            traverseTreeAndCheckBalanced(root);
        }
    }

    private int traverseTreeAndCheckBalanced(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode == null) {
            return 1;
        }
        int leftBlackHeight = traverseTreeAndCheckBalanced(RBNode.left);
        int rightBlackHeight = traverseTreeAndCheckBalanced(RBNode.right);
        if (leftBlackHeight != rightBlackHeight) {
            throw UnbalancedTreeException.create("Black height must be equal.", leftBlackHeight, rightBlackHeight, RBNode.toString());
        }
        if (RBNode.color == RBColor.RED) {
            checkRedNodeRule(RBNode);
            return leftBlackHeight;
        }
        return leftBlackHeight + 1;
    }

    private void checkRedNodeRule(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode.left != null && RBNode.left.color != RBColor.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then left child must be black.\n" + RBNode.toString());
        }
        if (RBNode.right != null && RBNode.right.color != RBColor.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then right child must be black.\n" + RBNode.toString());
        }
    }

}


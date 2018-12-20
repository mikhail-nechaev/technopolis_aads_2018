package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A Red-Black tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * ! An implementation of red-black trees must be based on the description in
 * Introduction to Algorithms (Cormen, Leiserson, Rivest)
 *
 * <a href="http://staff.ustc.edu.cn/~csli/graduate/algorithms/book6/chap13.htm">CHAPTER 13: BINARY SEARCH TREES</a>
 *
 * <a href="http://staff.ustc.edu.cn/~csli/graduate/algorithms/book6/chap14.htm">CHAPTER 14: RED-BLACK TREES</a>
 *
 * @param <E> the type of elements maintained by this set
 */

public class RedBlackTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {


    enum RBColor {
        RED, BLACK
    }

    static final class RBNode<E> {

        E value; //key
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
    protected RBNode rootNode;
    private int counter = 0;
    private RBNode<E> nil = new RBNode<>();

    public RedBlackTree() {
        this(Comparator.naturalOrder());
    }


    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }


    @Override
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException();
        }

        RBNode<E> z = new RBNode<>();
        z.value = value;
        RBNode<E> y = nil;
        RBNode<E> x = rootNode;

        while (x != nil && rootNode != null) {
            y = x;
            if (comparator.compare(z.value, x.value) < 0) {
                x = x.left;
            } else if (comparator.compare(z.value, x.value) > 0) {
                x = x.right;
            } else {
                return false;
            }
        }
        z.parent = y;
        if (y == nil) {
            rootNode = z;
        } else if (comparator.compare(z.value, y.value) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }
        z.left = nil;
        z.right = nil;
        z.color = RBColor.RED;
        insertFixup(z);
        counter++;
        return true;
    }

    private void insertFixup(RBNode z) {
        RBNode<E> y;
        while (z != nil && z.parent.color == RBColor.RED) {
            if (z.parent == z.parent.parent.left) {
                y = z.parent.parent.right;
                if (RBColor.RED == y.color) {
                    z.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    z = z.parent.parent;
                } else if (z == z.parent.right) {
                    z = z.parent;
                    leftRotate(z);
                }
                z.parent.color = RBColor.BLACK;
                z.parent.parent.color = RBColor.RED;
                rightRotate(z.parent.parent);

            } else {
                y = z.parent.parent.left;
                if (RBColor.RED == y.color) {
                    z.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    z = z.parent;
                } else if (z == z.parent.left) {
                    z = z.parent;
                    rightRotate(z);
                }
                z.parent.color = RBColor.BLACK;
                z.parent.parent.color = RBColor.RED;
                leftRotate(z.parent.parent);
            }
        }
        rootNode.color = RBColor.BLACK;
    }

    private void rightRotate(RBNode<E> x) {
        RBNode<E> y = x.left;
        x.left = y.right;

        if (y.right != nil) {
            y.right.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == nil) {
            rootNode = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void leftRotate(RBNode<E> x) {
        RBNode<E> y = x.right;
        x.right = y.left;

        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            rootNode = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }


    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return true;
    }


    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        }

        return searchValue(rootNode, value);
    }

    private boolean searchValue(RBNode<E> x, E value) {
        if (x == nil) {
            return false;
        }

        if (comparator.compare(x.value, value) < 0) {
            return searchValue(x.left, value);
        } else if (comparator.compare(x.value, value) > 0) {
            return searchValue(x.right, value);
        } else {
            return true;
        }
    }


    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        RBNode<E> x = rootNode;
        while (x.left != nil) {
            x = x.left;
        }
        return x.value;
    }


    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        RBNode<E> x = rootNode;
        while (x.right != nil) {
            x = x.right;
        }
        return x.value;
    }


    @Override
    public int size() {
        return counter;
    }


    @Override
    public boolean isEmpty() {
        return counter == 0;
    }


    @Override
    public void clear() {
        counter = 0;
    }


    @Override
    public void checkBalance() throws UnbalancedTreeException {
        if (rootNode != null) {
            if (rootNode.color != RBColor.BLACK) {
                throw new UnbalancedTreeException("Root must be black");
            }
            traverseTreeAndCheckBalanced(rootNode);
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
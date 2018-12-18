package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

import static ru.mail.polis.collections.set.sorted.todo.RedBlackTree.RBColor.BLACK;
import static ru.mail.polis.collections.set.sorted.todo.RedBlackTree.RBColor.RED;

/**
 * A Red-Black tree based {@link ISelfBalancingSortedTreeSet} implementation.
 * <p>
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

    //todo: update it if required
    enum RBColor {
        RED, BLACK
    }


    //todo: update it if required
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

        RBNode() {
        }

        RBNode(E value) {
            this.value = value;
        }
    }

    /**
     * The comparator used to maintain order in this tree sett.
     */
    protected final Comparator<E> comparator;
    protected RBNode<E> root, nil;
    protected int size;

    public RedBlackTree() {
        this(Comparator.naturalOrder());
        root = null;
    }

    /**
     * Creates a {@code ISelfBalancingSortedTreeSet} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public RedBlackTree(Comparator<E> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = comparator;
        nil = new RBNode<>(null);
        root = nil;
        root.left = nil;
        root.right = nil;
    }

    private void leftRotate(RBNode<E> x) {
        if (x.right == nil) return;
        RBNode<E> y = x.right;
        x.right = y.left;
        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(RBNode<E> x) {
        if (x.left == nil) return;
        RBNode<E> y = x.left;
        x.left = y.right;
        if (y.right != nil) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private boolean treeInsert(RBNode<E> z) {
        RBNode<E> y = nil;
        RBNode<E> x = root;
        while (x != nil) {
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
            root = z;
        } else if (comparator.compare(z.value, y.value) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }
        z.left = nil;
        z.right = nil;
        return true;
    }

    /**
     * Adds the specified element to this set if it is not already present.
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
        RBNode<E> insertion = new RBNode<>(value);
        if (!treeInsert(insertion)) {
            return false;
        }
        insertion.color = RBColor.RED;
        while (insertion != root && insertion.parent.color == RBColor.RED) {
            if (insertion.parent == insertion.parent.parent.left) {
                RBNode<E> y = insertion.parent.parent.right;
                if (y!= null && y.color == RBColor.RED) {
                    insertion.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    insertion.parent.parent.color = RBColor.RED;
                    insertion = insertion.parent.parent;
                } else {
                    if (insertion == insertion.parent.right) {
                        insertion = insertion.parent;
                        leftRotate(insertion);
                    }
                    insertion.parent.color = RBColor.BLACK;
                    insertion.parent.parent.color = RBColor.RED;
                    rightRotate(insertion.parent.parent);

                }
            } else {
                RBNode<E> y = insertion.parent.parent.left;
                if (y != null && y.color == RBColor.RED) {
                    insertion.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    insertion.parent.parent.color = RBColor.RED;
                    insertion = insertion.parent.parent;
                } else {
                    if (insertion == insertion.parent.left) {
                        insertion = insertion.parent;
                        rightRotate(insertion);
                    }
                    insertion.parent.color = RBColor.BLACK;
                    insertion.parent.parent.color = RBColor.RED;
                    leftRotate(insertion.parent.parent);
                }
            }
        }
        root.color = RBColor.BLACK;
        size++;
        return true;
    }

    public RBNode<E> minimum(RBNode<E> x) {
        while (x.left != nil) {
            x = minimum(x.left);
        }
        return x;
    }

    public RBNode<E> successor(RBNode<E> x) {
        RBNode<E> y = x.parent;
        while (x.right != nil) {
            return minimum(x.right);
        }
        while ((y != nil) && (x == y.right)) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    public void deleteFixUp(RBNode<E> x) {
        RBNode<E> node = new RBNode();
        while ((x != root) && (x.color == BLACK)) {
            if (x == x.parent.left) {
                node = x.parent.right;
                if (node.color == BLACK) {
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    node = x.parent.right;
                }
                if ((node.left.color == BLACK) && (node.right.color == BLACK)) {
                    node.color = RED;
                    x = x.parent;
                } else if (node.right.color == BLACK) {
                    node.left.color = BLACK;
                    node.color = RED;
                    rightRotate(node);
                    node = x.parent.right;
                }
                node.color = x.parent.color;
                x.parent.color = BLACK;
                node.right.color = BLACK;
                leftRotate(x.parent);
                x = root;
            } else {
                node = x.parent.left;
                if (node.color == BLACK) {
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    node = x.parent.left;
                }
                if ((node.right.color == BLACK) && (node.left.color == BLACK)) {
                    node.color = RED;
                    x = x.parent;
                } else if (node.left.color == BLACK) {
                    node.right.color = BLACK;
                    node.color = RED;
                    rightRotate(node);
                    node = x.parent.left;
                }
                node.color = x.parent.color;
                x.parent.color = BLACK;
                node.left.color = BLACK;
                leftRotate(x.parent);
                x = root;
            }
        }
        size--;
    }

    /**
     * Removes the specified element from this set if it is present.
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
        RBNode z = new RBNode();
        RBNode y;
        RBNode x;
        z.value = value;
        if ((z.left == nil) || (z.right == nil)) {
            y = z;
        } else {
            y = successor(z);
        }
        if (y.left != nil) {
            x = y.left;
        } else {
            x = y.right;
        }
        x.parent = y.parent;
        if (y.parent == nil) {
            root = x;
        } else {
            if (y == y.parent.left) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
            }
        }
        if (y != z) {
            z.value = y.value;
        }
        if (y.color == BLACK) {
            deleteFixUp(x);
        }
        return true;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
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
        return containsIntro(value, root);
    }


    public boolean containsIntro(E value, RBNode<E> current) {
        if (current == nil) return false;
            int comparison = comparator.compare(value, current.value);
            if (comparison == 0) {
                return true;
            }
            if (comparison < 0) {
                return containsIntro(value, current.left);
            }
        return containsIntro(value, current.right);
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        RBNode<E> current = root;
        while (current.left != nil) {
            current = current.left;
        }
        return (E) current.value;
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        RBNode<E> current = root;
        while (current.right != nil) {
            current = current.right;
        }
        return (E) current.value;
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
        root = nil;
        root.left = nil;
        root.right = nil;
        root.parent = nil;
    }

    /**
     * Обходит дерево и проверяет выполнение свойств сбалансированного красно-чёрного дерева
     * <p>
     * 1) Корень всегда чёрный.
     * 2) Если узел красный, то его потомки должны быть чёрными (обратное не всегда верно)
     * 3) Все пути от узла до листьев содержат одинаковое количество чёрных узлов (чёрная высота)
     *
     * @throws UnbalancedTreeException если какое-либо свойство невыполнено
     */
    @Override
    public void checkBalance() throws UnbalancedTreeException {
        if (root != nil) {
            if (root.color != RBColor.BLACK) {
                throw new UnbalancedTreeException("Root must be black");
            }
            traverseTreeAndCheckBalanced(root);
        }
    }

    private int traverseTreeAndCheckBalanced(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode == nil) {
            return 1;
        }
        int leftBlackHeight = traverseTreeAndCheckBalanced(RBNode.left);
        int rightBlackHeight = traverseTreeAndCheckBalanced(RBNode.right);
        if (leftBlackHeight != rightBlackHeight) {
            throw UnbalancedTreeException.create("Black height must be equal.", leftBlackHeight, rightBlackHeight, RBNode.toString());
        }
        if (RBNode.color == RED) {
            checkRedNodeRule(RBNode);
            return leftBlackHeight;
        }
        return leftBlackHeight + 1;
    }

    private void checkRedNodeRule(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode.left != nil && RBNode.left.color != RBColor.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then left child must be black.\n" + RBNode.toString());
        }
        if (RBNode.right != nil && RBNode.right.color != RBColor.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then right child must be black.\n" + RBNode.toString());
        }
    }

}

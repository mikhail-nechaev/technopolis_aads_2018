package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

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

        RBNode(E value) {
            this.value = value;
        }

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

    /**
     * The comparator used to maintain order in this tree sett.
     */
    protected final Comparator<E> comparator;
    protected RBNode<E> root, nilT;
    protected int size, modCount;


    public RedBlackTree() {
        this(Comparator.naturalOrder());
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
        nilT = new RBNode<>(null);
        root = nilT;
        root.left = nilT;
        root.right = nilT;
        root.parent = nilT;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void leftRotate(RBNode<E> x) {
        RBNode<E> y = x.right;
        x.right = y.left;
        if (y.left != nilT) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nilT) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void rightRotate(RBNode<E> x) {
        RBNode<E> y = x.left;
        x.left = y.right;
        if (y.right != nilT) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nilT) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
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
        if (insert(new RBNode<>(value))) {
            size++;
            modCount++;
            return true;
        }
        return false;
    }

    private boolean treeInsert(RBNode<E> z) {
        RBNode<E> y = nilT;
        RBNode<E> x = root;
        while (x != nilT) {
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
        if (y == nilT) {
            root = z;
        } else if (comparator.compare(z.value, y.value) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }
        z.left = nilT;
        z.right = nilT;
        return true;
    }

    private boolean insert(RBNode<E> x) {
        if (!treeInsert(x)) {
            return false;
        }
        x.color = RBColor.RED;
        while (x != root && x.parent.color == RBColor.RED) {
            if (x.parent == x.parent.parent.left) {
                RBNode<E> y = x.parent.parent.right;
                if (y.color == RBColor.RED) {
                    x.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    x.parent.parent.color = RBColor.RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        leftRotate(x);
                    } else {//тут не было else

                    x.parent.color = RBColor.BLACK;
                    x.parent.parent.color = RBColor.RED;
                    rightRotate(x.parent.parent);
                      }
                }
            } else {
                RBNode<E> y = x.parent.parent.left;
                if (y.color == RBColor.RED) {
                    x.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    x.parent.parent.color = RBColor.RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rightRotate(x);
                    } else { //тут не было else
                    x.parent.color = RBColor.BLACK;
                    x.parent.parent.color = RBColor.RED;
                    leftRotate(x.parent.parent);
                     }
                }
            }
        }
        root.color = RBColor.BLACK;
        return true;
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

        RBNode<E> node =  search(root, value);
        if (node == nilT){
            return false;
        }
        delete(node);
        size--;
        modCount++;
        return true;
    }

    protected boolean delete(RBNode<E> z) {
        RBNode<E> y;
        RBNode<E> x;
        if (z.left == nilT || z.right == nilT) {
            y = z;
        } else {
            y = treeSuccessor(z);
        }

        if (y.left != nilT) {
            x = y.left;
        } else {
            x = y.right;
        }

        x.parent = y.parent;
        if (y.parent == nilT) {
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
        if (y.color == RBColor.BLACK) {
            deleteFixUp(x);
        }

        return true;
    }

    protected void deleteFixUp(RBNode<E> x) {
        RBNode<E> w;
        while (x != root && x.color == RBColor.BLACK) {
            if (x == x.parent.left) {
                w = x.parent.right;
                if (w.color == RBColor.RED) {
                    w.color = RBColor.BLACK;
                    x.parent.color = RBColor.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == RBColor.BLACK && w.right.color == RBColor.BLACK) {
                    w.color = RBColor.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == RBColor.BLACK) {
                        w.left.color = RBColor.BLACK;
                        w.color = RBColor.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBColor.BLACK;
                    w.right.color = RBColor.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                w = x.parent.left;
                if (w.color == RBColor.RED) {
                    w.color = RBColor.BLACK;
                    x.parent.color = RBColor.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == RBColor.BLACK && w.left.color == RBColor.BLACK) {
                    w.color = RBColor.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == RBColor.BLACK) {
                        w.right.color = RBColor.BLACK;
                        w.color = RBColor.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBColor.BLACK;
                    w.left.color = RBColor.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = RBColor.BLACK;
    }

    protected RBNode<E> treeSuccessor(RBNode<E> x) {
        if (x.right != nilT) {
            return treeMinimum(x.right);
        }
        RBNode<E> y = x.parent;
        while (y != nilT && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    protected RBNode<E> treeMinimum(RBNode<E> x) {
       return x.left == nilT ? x: treeMinimum(x.left);
    }
    protected RBNode<E> treeMaximum(RBNode<E> x) {
        return x.right == nilT ? x: treeMaximum(x.right);
    }


    public RBNode<E> search(RBNode<E> node, E value){
        if (node == nilT) {
            return node;
        }
        int resCompare = comparator.compare(value, node.value);
        if (resCompare == 0) {
            return node;
        }
        if (resCompare > 0) {
            return search(node.right, value);
        }
        return search(node.left, value);
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
        return search(root, value) != nilT;
    }


    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return treeMinimum(root).value;
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
       return treeMaximum(root).value;
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
        root = nilT;
        root.left = nilT;
        root.right = nilT;
        root.parent = nilT;
        size = 0;
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
        if (root != null) {
            if (root.color != RBColor.BLACK) {
                throw new UnbalancedTreeException("Root must be black");
            }
            traverseTreeAndCheckBalanced(root);
        }
    }

    private int traverseTreeAndCheckBalanced(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode == nilT) {
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
        if (RBNode.left != nilT && RBNode.left.color != RBColor.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then left child must be black.\n" + RBNode.toString());
        }
        if (RBNode.right != nilT && RBNode.right.color != RBColor.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then right child must be black.\n" + RBNode.toString());
        }
    }

}

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

    //todo: update it if required
    enum RBColor {
        RED, BLACK
    }

    //todo: update it if required
    static final class RBNode<E> {

        RBNode(E value) {
            this.value = value;
        }

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

    /**
     * The comparator used to maintain order in this tree sett.
     */
    protected final Comparator<E> comparator;
    protected RBNode<E> nil;
    protected RBNode<E> root;
    protected int length;

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
        nil = new RBNode<>(null);
        nil.color = RBColor.BLACK;
        root = nil;
        root.left = nil;
        root.right = nil;
        root.parent = nil;
    }


    private RBNode<E> getRoot() {
        return root;
    }

    /*
     * x - current subRoot;
     * y - new subRoot;
     */
    private void leftRotate(RBNode<E> x) {
        RBNode<E> y = x.right;
        x.right = y.left;

        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x.parent.left == x) { //if x - left child
            x.parent.left = y;
        } else {                                   //if x - right child
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(RBNode<E> x) {
        RBNode<E> y = x.left;
        x.left = y.right;

        if (y.right != nil) {
            y.right.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x.parent.left == x) { //if x - left child
            x.parent.left = y;
        } else {                                   //if x - right child
            x.parent.right = y;
        }
        y.right = x;
        x.parent = y;
    }

    private E max() {
        return maxNode().value;
    }

    private RBNode<E> maxNode() {
        RBNode<E> x = getRoot();
        while (x.right != nil) {
            x = x.right;
        }
        return x;
    }

    private E min() {
        return minNode(root).value;
    }

    private RBNode<E> minNode(RBNode<E> x) {
        while (x.left != nil) {
            x = x.left;
        }
        return x;
    }

    private void insert(E z) {
        insert(new RBNode<>(z));
    }

    /*
     * z - new node to insert
     */

    private void insert(RBNode<E> z) {
        RBNode<E> y = nil;
        RBNode<E> x = root;

        while (x != nil) {
            y = x;
            if (comparator.compare(z.value, x.value) < 0) {   //z.value.compareTo(x.value)
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = (y);
        if (y == nil) {
            root = z;
        } else if (comparator.compare(z.value, y.value) < 0) {     //z.value.compareTo(y.value)
            y.left = (z);
        } else {
            y.right = (z);
        }
        z.left = (nil);
        z.right = (nil);
        z.color = RBColor.RED;
        insertFixup(z);
    }

    private void insertFixup(RBNode<E> z) {
        while (z.parent.color == RBColor.RED) {
            if (z.parent == z.parent.parent.left) {
                RBNode<E> y = z.parent.parent.right;
                if (y.color == RBColor.RED) {
                    z.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    z = z.parent.parent;
                } else if (z == z.parent.right) {
                    z = z.parent;
                    leftRotate(z);
                } else {
                    z.parent.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                RBNode<E> y = z.parent.parent.left;
                if (y.color == RBColor.RED) {
                    z.parent.color = RBColor.BLACK;
                    y.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    z = z.parent.parent;
                } else if (z == z.parent.left) {
                    z = z.parent;
                    rightRotate(z);
                } else {
                    z.parent.color = RBColor.BLACK;
                    z.parent.parent.color = RBColor.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        getRoot().color = RBColor.BLACK;
    }

    private RBNode<E> findNode(E value) {
        RBNode<E> current = root;

        while (current != nil){
            if (comparator.compare(current.value, value) == 0)
                return current;
            else if (comparator.compare(current.value, value) < 0)
                current = current.right;
            else
                current = current.left;
        }
        return null;
    }

    private E delete(E z) {
        return delete(new RBNode<>(z)).value;
    }

    private RBNode<E> delete(RBNode<E> z) {

        z = findNode(z.value);

        RBNode<E> y = (z.left == nil || z.right == nil) ? z : treeSuccessor(z);
        RBNode<E> x = y.left != nil ? y.left : y.right;

        x.parent = y.parent;

        if (y.parent == nil) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        if (y != z) {
            z.value = y.value;
        }
        if (y.color == RBColor.BLACK) {
            deleteFixup(x);
        }
        return y;
    }

    private void deleteFixup(RBNode<E> x) {
        RBNode<E> w;
        while (x != root && x.color == RBColor.BLACK) {
            if (x == x.parent.left) {
                w = x.parent.right;
                if (w.color == RBColor.RED) {   //case 1
                    w.color = RBColor.BLACK;
                    x.parent.color = RBColor.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == RBColor.BLACK &&        //case 2
                        w.right.color == RBColor.BLACK) {
                    w.color = RBColor.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == RBColor.BLACK) {   //case 3
                        w.left.color = RBColor.BLACK;
                        w.color = RBColor.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;    //case 4
                    x.parent.color = RBColor.BLACK;
                    w.right.color = RBColor.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                w = x.parent.left;
                if (w.color == RBColor.RED) {   //case 1
                    w.color = RBColor.BLACK;
                    x.parent.color = RBColor.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == RBColor.BLACK &&        //case 2
                        w.left.color == RBColor.BLACK) {
                    w.color = RBColor.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == RBColor.BLACK) {   //case 3
                        w.right.color = RBColor.BLACK;
                        w.color = RBColor.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;      //case 4
                    x.parent.color = RBColor.BLACK;
                    w.left.color = RBColor.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = RBColor.BLACK;
    }



    private RBNode<E> treeSuccessor(RBNode<E> x) {
        if (x.left != nil) {
            return minNode(x.right);
        }

        RBNode<E> y = x.parent;

        while (y != nil && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
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
        if (!contains(value)) {
            insert(value);
            length++;
            return true;
        } else {
            return false;
        }

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
        if (contains(value)) {
            delete(value);
            length--;
            return true;
        } else {
            return false;
        }
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
        if(value == null){
            throw new NullPointerException();
        }

        RBNode current = findNode(value);
        if(current == nil || current == null){
            return false;
        }
        return true;
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
        return min();
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
        return max();
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return root == nil && root.left == nil && root.right == nil;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        root = nil;
        root.left = nil;
        root.right = nil;
        length = 0;
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
        if (root != null || root != nil) {
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
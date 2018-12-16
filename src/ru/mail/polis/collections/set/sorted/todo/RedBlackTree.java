package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

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
        E value;
        RBNode<E> left;
        RBNode<E> right;
        RBNode<E> parent;
        RBColor color = RBColor.BLACK;

        public RBNode() {}

        public RBNode(E value, RBColor color) {
            this.value = value;
            this.color = color;
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
    protected RBNode<E> root;
    protected int size = 0;
    protected RBNode<E> emptyNode = new RBNode<>();

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
        this.comparator = comparator;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    //Левый поворот
    private RBNode<E> leftRotate(RBNode<E> node) {
        RBNode<E> right = node.right;
        node.right = right.left;
        if(right.left != emptyNode)
            right.left.parent = node;
        if(right != emptyNode)
            right.parent = node.parent;
        if(node.parent != emptyNode) {
            if(node == node.parent.left)
                node.parent.left = right;
            else
                node.parent.right = right;
        } else
            root = right;
        right.left = node;
        if(node != emptyNode)
            node.parent = right;
        return root;
    }

    //Правый поворот
    private RBNode<E> rightRotate(RBNode<E> node) {
        RBNode<E> left = node.left;
        node.left = left.right;
        if(left.right != emptyNode)
            left.right.parent = node;
        if(left != emptyNode)
            left.parent = node.parent;
        if(node.parent != emptyNode) {
            if(node == node.parent.right)
                node.parent.right = left;
            else
                node.parent.left = left;

        } else
            root = left;
        left.right = node;
        if(node != emptyNode)
            node.parent = left;
        return root;
    }

    //Функция восстановления свойств красно-чёрного дерева
    private void balance(RBNode<E> node) {
        RBNode<E> uncle;
        while(node != root && node.parent.color == RBColor.RED) {
            if(node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;
                if(uncle.color == RBColor.RED) {
                    node.parent.color = RBColor.BLACK;
                    uncle.color = RBColor.BLACK;
                    node.parent.parent.color = RBColor.RED;
                    node = node.parent.parent;
                } else {
                    if(node == node.parent.right) {
                        node = node.parent;
                        root = leftRotate(node);
                    }
                    node.parent.color = RBColor.BLACK;
                    node.parent.parent.color = RBColor.RED;
                    root = rightRotate(node.parent.parent);
                }
            } else {
                uncle = node.parent.parent.left;
                if(uncle.color == RBColor.RED) {
                    node.parent.color = RBColor.BLACK;
                    uncle.color = RBColor.BLACK;
                    node.parent.parent.color = RBColor.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        root = rightRotate(node);
                    }
                    node.parent.color = RBColor.BLACK;
                    node.parent.parent.color = RBColor.RED;
                    root = leftRotate(node.parent.parent);
                }
            }
        }
        root.color = RBColor.BLACK;
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
        Objects.requireNonNull(value);
        RBNode<E> parent = emptyNode;
        RBNode<E> node = root;
        while(node != emptyNode && node != null) {
            parent = node;
            if(compare(value, node.value) < 0)
                node = node.left;
            else if(compare(value, node.value) > 0)
                node = node.right;
            else
                return false;
        }
        node = new RBNode<>(value, RBColor.RED);
        node.parent = parent;
        node.left = node.right = emptyNode;
        if(parent != emptyNode) {
            if(compare(value, parent.value) < 0)
                parent.left = node;
            else
                parent.right = node;
        } else {
            root = node;
        }
        balance(node);
        size++;
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
        Objects.requireNonNull(value);
        return false;
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
        Objects.requireNonNull(value);
        RBNode<E> curr = root;
        while(curr != emptyNode && curr != null) {
            if (compare(curr.value, value) == 0)
                return true;
            curr = (compare(value, curr.value) < 0) ? curr.left : curr.right;
        }
        return false;
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        RBNode<E> curr = root;
        while (!(curr.left == null || curr.left == emptyNode)) {
            curr = curr.left;
        }
        return curr.value;
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        RBNode<E> curr = root;
        while (!(curr.right == null || curr.right == emptyNode)) {
            curr = curr.right;
        }
        return curr.value;
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
        return size == 0 || root == null;
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

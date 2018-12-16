package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;

import javax.print.attribute.standard.NumberUp;

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
        E value;
        RBNode<E> left;
        RBNode<E> right;
        RBNode<E> parent;
        RBColor color = RBColor.BLACK;

        RBNode(E value){
            this.value = value;
        }

        RBNode(E value, RBNode<E> parent){
            this.value = value;
            this.parent = parent;
            this.color = RBColor.RED;
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
    protected RBNode root;
    protected int size = 0;

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
        if(comparator == null){
            throw new NullPointerException();
        }
        this.comparator = comparator;
    }

    private void leftRotation(RBNode<E> cur){
        RBNode<E> rightChild = cur.right;
        cur.right = rightChild.left;
        if(rightChild.left != null){
            rightChild.left.parent = cur;
        }
        rightChild.parent = cur.parent;
        if(cur.parent == null){
            root = rightChild;
        } else if(cur == cur.parent.left){
            cur.parent.left = rightChild;
        } else {
            cur.parent.right = rightChild;
        }
        rightChild.left = cur;
        cur.parent = rightChild;
    }

    private void rightRotation(RBNode<E> cur){
        RBNode<E> leftChild = cur.left;
        cur.left = leftChild.right;
        if(leftChild.right != null){
            leftChild.right.parent = cur;
        }
        leftChild.parent = cur.parent;
        if(cur.parent == null){
            root = leftChild;
        } else if(cur == cur.parent.right){
            cur.parent.right = leftChild;
        } else {
            cur.parent.left = leftChild;
        }
        leftChild.right = cur;
        cur.parent = leftChild;
    }

    private void addBalance(RBNode<E> cur){
        while(cur != root && cur.parent.color == RBColor.RED){
            if(cur.parent.parent.left == cur.parent) {
                RBNode<E> uncle = cur.parent.parent.right;
                if (uncle != null && uncle.color == RBColor.RED) {
                        cur.parent.color = RBColor.BLACK;
                        uncle.color = RBColor.BLACK;
                        cur.parent.parent.color = RBColor.RED;
                        cur = cur.parent.parent;
                } else {
                    if(cur.parent.right == cur){
                        cur = cur.parent;
                        leftRotation(cur);
                    }
                    cur.parent.color = RBColor.BLACK;
                    cur.parent.parent.color = RBColor.RED;
                    rightRotation(cur.parent.parent);
                }
            } else{
                RBNode<E> uncle = cur.parent.parent.left;
                if (uncle != null && uncle.color == RBColor.RED) {
                        cur.parent.color = RBColor.BLACK;
                        uncle.color = RBColor.BLACK;
                        cur.parent.parent.color = RBColor.RED;
                        cur = cur.parent.parent;
                } else {
                    if(cur.parent.left == cur){
                        cur = cur.parent;
                        rightRotation(cur);
                    }
                    cur.parent.color = RBColor.BLACK;
                    cur.parent.parent.color = RBColor.RED;
                    leftRotation(cur.parent.parent);
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
        if (value == null) {
            throw new NullPointerException();
        }

        if(root == null){
            root = new RBNode<>(value);
            size++;
            return true;
        }
        RBNode<E> parent = null;
        RBNode<E> cur = root;
        while(cur != null){
            int cmp = comparator.compare(value, cur.value);
            parent = cur;
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0){
                cur = cur.right;
            } else {
                return false;
            }
        }
        cur = new RBNode<>(value, parent);
        if(comparator.compare(cur.value, parent.value) < 0){
            parent.left = cur;
        } else{
            parent.right = cur;
        }
        size++;
        addBalance(cur);
        return true;
    }

    private void deleteBalance(RBNode<E> cur, RBNode<E> parent){
        while(cur != root && (cur == null || cur.color == RBColor.BLACK)){
            if(parent.left == cur) {
                RBNode<E> brother = parent.right;
                if (brother != null && brother.color == RBColor.RED) {
                    parent.color = RBColor.RED;
                    brother.color = RBColor.BLACK;
                    leftRotation(parent);
                    brother = parent.right;
                }
                if ((brother.left == null || brother.left.color == RBColor.BLACK) && (brother.right == null || brother.right.color == RBColor.BLACK)) {
                    brother.color = RBColor.RED;
                    cur = parent;
                    parent = cur.parent;
                } else {
                    if (brother.right == null || brother.right.color == RBColor.BLACK) {
                        brother.left.color = RBColor.BLACK;
                        brother.color = RBColor.RED;
                        rightRotation(brother);
                        brother = parent.right;
                    }
                    brother.color = parent.color;
                    parent.color = RBColor.BLACK;
                    brother.right.color = RBColor.BLACK;
                    leftRotation(parent);
                    cur = root;
                }
            } else{
                RBNode<E> brother = parent.left;
                if (brother != null && brother.color == RBColor.RED) {
                    parent.color = RBColor.RED;
                    brother.color = RBColor.BLACK;
                    rightRotation(parent);
                    brother = parent.left;
                }
                if ((brother.right == null || brother.right.color == RBColor.BLACK) && (brother.left == null || brother.left.color == RBColor.BLACK)) {
                    brother.color = RBColor.RED;
                    cur = parent;
                    parent = cur.parent;
                } else {
                    if (brother.left == null || brother.left.color == RBColor.BLACK) {
                        brother.right.color = RBColor.BLACK;
                        brother.color = RBColor.RED;
                        leftRotation(brother);
                        brother = parent.left;
                    }
                    brother.color = parent.color;
                    parent.color = RBColor.BLACK;
                    brother.left.color = RBColor.BLACK;
                    rightRotation(parent);
                    cur = root;
                }
            }
        }
        if(cur !=null) {
            cur.color = RBColor.BLACK;
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

        RBNode<E> cur = root;
        while(cur != null){
            int cmp = comparator.compare(value, cur.value);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0){
                cur = cur.right;
            } else {
                size--;
                RBNode<E> leftChild = cur.left;
                RBNode<E> rightChild = cur.right;
                RBNode<E> node;
                if(leftChild == null || rightChild == null){
                    node = cur;
                } else{
                    node = findMin(rightChild);
                }
                RBNode<E> child;
                if(node.left != null){
                    child = node.left;
                } else{
                    child = node.right;
                }
                if(child != null) {
                    child.parent = node.parent;
                }
                if(node.parent != null) {
                    if (node.parent.left == node) {
                        node.parent.left = child;
                    } else {
                        node.parent.right = child;
                    }
                } else{
                    root = child;
                }
                if(node != cur){
                    cur.value = node.value;
                }
                if(node.color == RBColor.BLACK){
                    deleteBalance(child, node.parent);
                }
                return true;
            }
        }
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
        if(value == null){
            throw new NullPointerException();
        }
        RBNode<E> cur = root;
        int cmp;
        do {
            if(cur == null){
                return false;
            }
            cmp = comparator.compare(value, cur.value);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            }
        } while(cmp != 0);
        return true;
    }
    private RBNode<E> findMin(RBNode<E> cur){
        RBNode<E> min = cur;
        while(min.left != null){
            min = min.left;
        }
        return min;
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return (E)findMin(root).value;
    }
    private RBNode<E> findMax(RBNode<E> cur){
        RBNode<E> max = cur;
        while(max.right != null){
            max = max.right;
        }
        return max;
    }
    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return (E)findMax(root).value;
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
        root = null;
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

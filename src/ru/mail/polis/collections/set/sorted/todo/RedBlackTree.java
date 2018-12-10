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
 * <a href="http://staff.ustc.edu.cn/~csli/graduate/algorithms/book6/chap14.htm">CHAPTER 14: RED-BLACK TREES</a>
 *
 * @param <E> the type of elements maintained by this set
 */
public class RedBlackTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    enum RBColor {
        RED, BLACK
    }

    protected int size;

    static final class RBNode<E> {
        E value;
        RBNode<E> left;
        RBNode<E> right;
        RBNode<E> parent;
        RBColor color = RBColor.BLACK;

        RBNode(E value){
            this.value = value;
        }

        void removeFromParent(){
            if (parent != null) {
                if (parent.left == this) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
                parent = null;
            }
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

    private void setColor(RBNode<E> node, RBColor color) {
        if (node != null) {
            node.color = color;
        }
    }

    private RBColor colorOf(RBNode<E> node) {
        return node == null ? RBColor.BLACK : node.color;
    }

    private boolean isRed(RBNode<E> node) {
        return node != null && colorOf(node) == RBColor.RED;
    }

    private boolean isBlack(RBNode<E> node) {
        return node == null || colorOf(node) == RBColor.BLACK;
    }

    private RBNode<E> parentOf(RBNode<E> node) {
        return node == null ? null : node.parent;
    }

    private RBNode<E> grandparentOf(RBNode<E> node) {
        return (node == null || node.parent == null) ? null : node.parent.parent;
    }

    private RBNode<E> leftOf(RBNode<E> node) {
        return node == null ? null : node.left;
    }

    private RBNode<E> rightOf(RBNode<E> node) {
        return node == null ? null : node.right;
    }

    private RBNode<E> findInorderSuccessor(RBNode<E> node){
        if(node == null){
            return null;
        }

        RBNode<E> n = node;
        while (n.left != null){
            n = n.left;
        }

        return n;
    }

    private void rotateLeft(RBNode<E> node){
        RBNode<E> rightChild = node.right;
        node.right = rightChild.left;

        if(rightChild.left != null){
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;
        if(node.parent == null){
            root = rightChild;
        } else if(node == node.parent.left){
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(RBNode<E> node){
        RBNode<E> leftChild = node.left;
        node.left = leftChild.right;

        if(leftChild.right != null){
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;
        if(node.parent == null){
            root = leftChild;
        } else if(node == node.parent.left){
            node.parent.left = leftChild;
        } else {
            node.parent.right = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    private void adjustAfterInsertion(RBNode<E> node){
        while (isRed(parentOf(node))){
            if(parentOf(node) == leftOf(grandparentOf(node))){
                RBNode<E> uncle = rightOf(grandparentOf(node));

                if(isRed(uncle)){
                    setColor(parentOf(node), RBColor.BLACK);
                    setColor(uncle, RBColor.BLACK);
                    setColor(grandparentOf(node), RBColor.RED);
                    node = grandparentOf(node);
                } else {
                    if(node == rightOf(parentOf(node))){
                        node = parentOf(node);
                        rotateLeft(node);
                    }
                    setColor(parentOf(node), RBColor.BLACK);
                    setColor(grandparentOf(node), RBColor.RED);
                    rotateRight(grandparentOf(node));
                }
            }else {
                RBNode<E> uncle = leftOf(grandparentOf(node));

                if(isRed(uncle)){
                    setColor(parentOf(node), RBColor.BLACK);
                    setColor(uncle, RBColor.BLACK);
                    setColor(grandparentOf(node), RBColor.RED);
                    node = grandparentOf(node);
                } else {
                    if(node == leftOf(parentOf(node))){
                        node = parentOf(node);
                        rotateRight(node);
                    }
                    setColor(parentOf(node), RBColor.BLACK);
                    setColor(grandparentOf(node), RBColor.RED);
                    rotateLeft(grandparentOf(node));
                }
            }
        }

        setColor(root, RBColor.BLACK);
    }

    private void adjustAfterRemoval(RBNode<E> node){
        while (node != root && isBlack(node)){
            if(leftOf(parentOf(node)) == node){
                RBNode<E> sibling = rightOf(parentOf(node));
                if(isRed(sibling)){
                    setColor(sibling, RBColor.BLACK);
                    setColor(parentOf(node), RBColor.RED);
                    rotateLeft(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }

                if(isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))){
                    setColor(sibling, RBColor.RED);
                    node = parentOf(node);
                } else {
                    if(isBlack(rightOf(sibling))){
                        setColor(leftOf(sibling), RBColor.BLACK);
                        setColor(sibling, RBColor.RED);
                        rotateRight(sibling);
                        sibling = rightOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), RBColor.BLACK);
                    setColor(rightOf(sibling), RBColor.BLACK);
                    rotateLeft(parentOf(node));
                    node = root;
                }
            } else {
                RBNode<E> sibling = leftOf(parentOf(node));
                if(isRed(sibling)){
                    setColor(sibling, RBColor.BLACK);
                    setColor(parentOf(node), RBColor.RED);
                    rotateRight(parentOf(node));
                    sibling =  leftOf(parentOf(node));
                }

                if(isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))){
                    setColor(sibling, RBColor.RED);
                    node = parentOf(node);
                } else {
                    if(isBlack(leftOf(sibling))){
                        setColor(rightOf(sibling), RBColor.BLACK);
                        setColor(sibling, RBColor.RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), RBColor.BLACK);
                    setColor(leftOf(sibling), RBColor.BLACK);
                    rotateRight(parentOf(node));
                    node = root;
                }
            }
        }
        setColor(node, RBColor.BLACK);
    }

    private boolean insert(RBNode<E> node){
        if(root == null){
            root = node;
            size++;
            return true;
        }

        RBNode<E> parent = null;
        RBNode<E> n = root;

        while (n != null && comparator.compare(node.value, n.value) != 0){
            parent = n;
            n = (comparator.compare(node.value, n.value) < 0)
                    ? n.left
                    : n.right;
        }

        if(n == null){
            node.parent = parent;
            if (comparator.compare(node.value, parent.value) < 0) {
                parent.left = node;
            } else {
                parent.right = node;
            }

            node.color = RBColor.RED;
            size++;
            adjustAfterInsertion(node);
            return true;
        }

        return false;
    }

    /**
     * The comparator used to maintain order in this tree sett.
     */
    protected final Comparator<E> comparator;
    protected RBNode<E> root;

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
        return insert(new RBNode<>(Objects.requireNonNull(value)));
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

        RBNode<E> removedNode = root;
        while (removedNode != null && comparator.compare(value, removedNode.value) != 0){
            removedNode = (comparator.compare(value, removedNode.value) < 0)
                    ? removedNode.left
                    : removedNode.right;
        }

        if(removedNode == null){
            return  false;
        }

        if(removedNode.left != null && removedNode.right != null){
            RBNode<E> successor = findInorderSuccessor(removedNode.right);
            removedNode.value = successor.value;
            removedNode = successor;
        }

        RBNode<E> pullUp = (removedNode.right == null) ? removedNode.left : removedNode.right;
        if(pullUp != null){
            if(removedNode == root){
                root = pullUp;
            } else if(removedNode.parent.left == removedNode){
                removedNode.parent.left = pullUp;
                pullUp.parent = removedNode.parent;
            } else {
                removedNode.parent.right = pullUp;
                pullUp.parent = removedNode.parent;
            }

            if(isBlack(removedNode)){
                adjustAfterRemoval(pullUp);
            }
        } else if(removedNode == root){
            root = null;
        } else {
            if(isBlack(removedNode)){
                adjustAfterRemoval(removedNode);
            }
            removedNode.removeFromParent();
        }

        size--;
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
        Objects.requireNonNull(value);

        if(isEmpty()){
            return false;
        }

        RBNode<E> node = root;
        while (node != null && comparator.compare(value, node.value) != 0){
            node = (comparator.compare(value, node.value) < 0)
                    ? node.left
                    : node.right;
        }

        return node != null;
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

        return findInorderSuccessor(root).value;
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

        RBNode<E> node = root;
        while (node.right != null){
            node = node.right;
        }
        return node.value;
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

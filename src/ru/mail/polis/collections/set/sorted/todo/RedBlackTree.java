package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

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

    enum RBColor {
        RED, BLACK
    }

    final class RBNode<E> {
        E value;
        RBNode<E> left;
        RBNode<E> right;
        RBNode<E> parent;
        RBColor color = RBColor.BLACK;

        public RBNode(E value, RBColor color) {
            this.value = value;
            this.color = color;
        }

        public RBNode() {}


        @Override
        public String toString() {
            return "RBNode{" +
                    "value=" + value +
                    ", left=" + left +
                    ", right=" + right +
                    ", color=" + color +
                    '}';
        }

        public RBNode<E> getSuccessor() {
            RBNode<E> temp;
            RBNode<E> node = this;
            if(!(node.right == null || node.right == blankNode)) {
                temp = node.right;
                while(!(temp.left == null || temp.left == blankNode))
                    temp = temp.left;
                return temp;
            }
            temp = node.parent;
            while(temp != blankNode && node == temp.right) {
                node = temp;
                temp = temp.parent;
            }
            return temp;
        }
    }

    /**
     * The comparator used to maintain order in this tree sett.
     */
    protected final Comparator<E> comparator;
    protected RBNode root;
    protected int size = 0;
    protected RBNode<E> blankNode = new RBNode<>();

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
        Objects.requireNonNull(value);
        RBNode<E> parent = blankNode;
        RBNode<E> node = root;
        while (node != blankNode && node != null) {
            parent = node;
            if (compare(value, node.value) < 0) node = node.left;
            else if (compare(value, node.value) > 0) node = node.right;
            else return false;
        }
        node = new RBNode<>(value, RBColor.RED);
        node.parent = parent;
        node.left = node.right = blankNode;
        if (parent != blankNode) {
            if (compare(value, parent.value) < 0) parent.left = node;
            else parent.right = node;
        } else root = node;
        balance(node);
        size++;
        return true;
    }

    private int compare(E a, E b) {
        return comparator == null ? a.compareTo(b) : comparator.compare(a, b);
    }

    private RBNode<E> leftRotate(RBNode<E> node) {
        RBNode<E> right = node.right;
        node.right = right.left;
        if (right.left != blankNode) right.left.parent = node;
        if (right != blankNode) right.parent = node.parent;
        if (node.parent != blankNode) {
            if (node == node.parent.left) node.parent.left = right;
            else node.parent.right = right;
        } else root = right;
        right.left = node;
        if (node != blankNode) node.parent = right;
        return root;
    }

    private RBNode<E> rightRotate(RBNode<E> node) {
        RBNode<E> left = node.left;
        node.left = left.right;
        if (left.right != blankNode) left.right.parent = node;
        if (left != blankNode) left.parent = node.parent;
        if (node.parent != blankNode) {
            if (node == node.parent.right) node.parent.right = left;
            else node.parent.left = left;
        } else  root = left;
        left.right = node;
        if (node != blankNode) node.parent = left;
        return root;
    }

    private void balance(RBNode<E> node) {
        RBNode<E> uncle;
        while (node != root && node.parent.color == RBColor.RED) {
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;
                if (uncle.color == RBColor.RED) {
                    node.parent.color = RBColor.BLACK;
                    uncle.color = RBColor.BLACK;
                    node.parent.parent.color = RBColor.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        root = leftRotate(node);
                    }
                    node.parent.color = RBColor.BLACK;
                    node.parent.parent.color = RBColor.RED;
                    root = rightRotate(node.parent.parent);
                }
            } else {
                uncle = node.parent.parent.left;
                if (uncle.color == RBColor.RED) {
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
     * Removes the specified element from this set if it is present.
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean remove(E value) {

        RBNode<E> curr = root;
        while(curr != blankNode && curr != null) {
            if(compare(value, curr.value) < 0) curr = curr.left;
            else if(compare(value, curr.value) > 0) curr = curr.right;
            else if(compare(value, curr.value) == 0) break;
        }
        if(curr != blankNode && compare(value, curr.value) == 0) {
            RBNode<E> temp, successor;
            if(curr == null || curr == blankNode) return false;
            if(curr.left == null || curr.left == blankNode || curr.right == null || curr.right == blankNode)
                successor = curr;
            else successor = curr.getSuccessor();
            if(!(successor.left == null || successor.left == blankNode)) temp = successor.left;
            else temp = successor.right;
            temp.parent = successor.parent;
            if(successor.parent == null || successor.parent == blankNode) root = temp;
            else if(successor == successor.parent.left) successor.parent.left = temp;
            else successor.parent.right = temp;
            if(successor != curr) curr.value = successor.value;
            if(successor.color == RBColor.BLACK) removeFixUp(temp);
            size--;
            return true;
        }
        return false;
    }

    private void removeFixUp(RBNode<E> node) {
        RBNode<E> temp;
        while(node != root && node.color == RBColor.BLACK) {
            if(node == node.parent.left) {
                temp = node.parent.right;
                if(temp.color == RBColor.RED) {
                    temp.color = RBColor.BLACK;
                    node.parent.color = RBColor.RED;
                    leftRotate(node.parent);
                    temp = node.parent.right;
                }
                if(temp.left.color == RBColor.BLACK && temp.right.color == RBColor.BLACK) {
                    temp.color = RBColor.RED;
                    node = node.parent;
                } else {
                    if(temp.right.color == RBColor.BLACK) {
                        temp.left.color = RBColor.BLACK;
                        temp.color = RBColor.RED;
                        rightRotate(temp);
                        temp = node.parent.right;
                    }
                    temp.color = node.parent.color;
                    node.parent.color = RBColor.BLACK;
                    temp.right.color = RBColor.BLACK;
                    leftRotate(node.parent);
                    node = root;
                }
            } else {
                temp = node.parent.left;
                if(temp.color == RBColor.RED) {
                    temp.color = RBColor.BLACK;
                    node.parent.color = RBColor.RED;
                    rightRotate(node.parent);
                    temp = node.parent.left;
                }
                if(temp.left.color == RBColor.BLACK && temp.right.color == RBColor.BLACK) {
                    temp.color = RBColor.RED;
                    node = node.parent;
                } else {
                    if(temp.left.color == RBColor.BLACK) {
                        temp.right.color = RBColor.BLACK;
                        temp.color = RBColor.RED;;
                        leftRotate(temp);
                        temp = node.parent.left;
                    }
                    temp.color = node.parent.color;
                    node.parent.color = RBColor.BLACK;
                    temp.left.color = RBColor.BLACK;

                    rightRotate(node.parent);
                    node = root;
                }
            }
        }
        node.color = RBColor.BLACK;
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
        while(curr != blankNode && curr != null) {
            if (compare(curr.value, value) == 0) return true;
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
        if(isEmpty()) throw new NoSuchElementException();
        RBNode<E> curr = root;
        while (!(curr.left == null || curr.left == curr)) {
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
        if(isEmpty()) throw new NoSuchElementException();
        RBNode<E> curr = root;
        while (!(curr.right == null || curr.right == blankNode)) {
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
        return root == null;
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

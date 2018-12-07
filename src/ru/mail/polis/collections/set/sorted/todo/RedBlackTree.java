package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A Red-Black tree based {@link ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet} implementation.
 *
 * ! An implementation of red-black trees must be based on the description in
 * Introduction to Algorithms (Cormen, Leiserson, Rivest)
 *
 * <a href="http://staff.ustc.edu.cn/~csli/graduate/algorithms/book6/chap14.htm">CHAPTER 14: RED-BLACK TREES</a>
 *
 * @param <E> the type of elements maintained by this set
 */
public class RedBlackTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    private static final class RBNode<E extends Comparable<E>> {
        E value;
        RBNode left;
        RBNode right;
        RBNode parent;
        Color color;

        private enum  Color{
            RED, BLACK
        }

        public RBNode(E value){
            this();
            this.value = value;
        }

        private RBNode() {
            color = Color.RED;
            parent = null;
            left = null;
            right = null;
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

    protected final Comparator<E> comparator;
    protected RBNode root;
    private int length;


    public RedBlackTree() {
        this(Comparator.naturalOrder());
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    private void leftRotate(RBNode x){
        RBNode y = x.right;
        x.right = y.left;
        if(y.left != null)
            y.left.parent = x;
        y.parent = x.parent;
        if(x.parent == null)
            root = y;
        if (x.parent.left == x)
            x.parent.left = y;
        else
            x.parent.right = y;
        y.left = x;
        x.parent = y;
    }
    private void rightRotate(RBNode y){
        RBNode x = y.left;
        y.left = x.right;
        if(x.right != null)
            x.right.parent = y;
        x.parent = y.parent;
        if(y.parent == null)
            root = x;
        if (y.parent.right == y)
            y.parent.right = x;
        else
            y.parent.left = x;
        x.right = y;
        y.parent = x;
    }



    private void insert(RBNode z){
        RBNode y = null;
        RBNode x = root;
        while(x != null){
            y = x;
            if(z.value.compareTo(x.value) < 0){
                x = x.left;
            }
            else{
                x = x.right;
            }
        }
        z.parent = y;
        if(y == null){
            root = z;
        }
        else if (z.value.compareTo(y.value) < 0)
            y.left = z;
        else
            y.right = z;
        z.left = null;
        z.right = null;
        z.color = RBNode.Color.RED;
        insertFixUp(z);
    }


    private void insertFixUp(RBNode z) {
        RBNode y;
        if(z.parent != null) {
            while (z.parent.color.equals(RBNode.Color.RED)) {
                if (z.parent == z.parent.parent.left) {
                    y = z.parent.parent.right;
                    if (y != null && y.color.equals(RBNode.Color.RED)) {
                        z.parent.color = RBNode.Color.BLACK;
                        y.color = RBNode.Color.BLACK;
                        z.parent.parent.color = RBNode.Color.RED;
                        z = z.parent.parent;
                        if(z.parent == null){
                            break;
                        }
                    } else if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    } else {
                        z.parent.color = RBNode.Color.BLACK;
                        z.parent.parent.color = RBNode.Color.RED;
                        rightRotate(z.parent.parent);
                    }
                }
                else {
                    y = z.parent.parent.left;
                    if (y!= null && y.color.equals(RBNode.Color.RED)) {
                        z.parent.color = RBNode.Color.BLACK;
                        y.color = RBNode.Color.BLACK;
                        z.parent.parent.color = RBNode.Color.RED;
                        z = z.parent.parent;
                        if(z.parent == null){
                            break;
                        }
                    } else if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    } else {
                        z.parent.color = RBNode.Color.BLACK;
                        z.parent.parent.color = RBNode.Color.RED;
                        leftRotate(z.parent.parent);
                    }
                }
            }
        }
        root.color = RBNode.Color.BLACK;
    }


    private RBNode find(RBNode current, E value){
        if(current == null || current.value.equals(value)) {
            return current;
        }
        if(current.value.compareTo(value) > 0){
            return find(current.left, value);
        }
        else{
            return find(current.right,value);
        }
    }


    private RBNode treeMin(RBNode current){
        while(current.left != null){
            current = current.left;
        }
        return current;
    }
    private RBNode treeMax(RBNode current){
        while(current.right != null){
            current = current.right;
        }
        return current;
    }

    private RBNode treeSuccessor(RBNode x){
        if(x.left != null)
            return treeMin(x.right);
        RBNode y = x.parent;
        while(y != null && x == y.right){
            x = y;
            y = y.parent;
        }
        return y;
    }

    private void remove(RBNode z){
        RBNode y;
        RBNode x;
        if(z.left == null || z.right == null){
            y = z;
        }
        else
            y = treeSuccessor(z);
        if(y.left != null)
            x = y.left;
        else
            x = y.right;

        if(x == null){
            x = new RBNode();
        }
        x.parent = y.parent;

        if(y.parent == null)
            root = x;
        else if(y.parent.left != null && y.parent.left == y)
            y.parent.left = x;
        else if(y.parent.right != null && y.parent.right == y)
            y.parent.right = x;
        if(y != z){
            z.value = y.value;
        }

        if(y.color.equals(RBNode.Color.BLACK))
            removeFixUp(x);
    }

    private void removeFixUp(RBNode x) {
        RBNode w;
        while(x!=root && x.color.equals(RBNode.Color.BLACK)){
            if(x == x.parent.left){
                w = x.parent.right;
                if(w.color.equals(RBNode.Color.RED)){
                    w.color = RBNode.Color.BLACK;
                    x.parent.color = RBNode.Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if(w.left != null && w.right != null && w.left.color.equals(RBNode.Color.BLACK) && w.right.color.equals(RBNode.Color.BLACK)){
                    w.color = RBNode.Color.RED;
                    x = x.parent;
                }
                else{
                    if(w.right.color.equals(RBNode.Color.BLACK) ){
                        w.left.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    w.parent.color = RBNode.Color.BLACK;
                    w.right.color = RBNode.Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }
            else{
                w = x.parent.left;
                if(w.color.equals(RBNode.Color.RED)){
                    w.color = RBNode.Color.BLACK;
                    x.parent.color = RBNode.Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right != null && w.left != null && w.right.color.equals(RBNode.Color.BLACK) && w.left.color.equals(RBNode.Color.BLACK)){
                    w.color = RBNode.Color.RED;
                    x = x.parent;
                }
                else{
                    if(w.left.color.equals(RBNode.Color.BLACK) ){
                        w.right.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    w.parent.color = RBNode.Color.BLACK;
                    w.left.color = RBNode.Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = RBNode.Color.BLACK;
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
        if(value == null){
            throw new NullPointerException();
        }
        if(find(root,value) == null) {
            insert(new RBNode(value));
            length = length + 1;
            return true;
        }
        else {
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
        if(value == null){
            throw new NullPointerException();
        }
        if(find(root,value) == null){
            return false;
        }
        else{
            remove(find(root, value));
            length --;
            return true;
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
    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean contains(Object value) {
        if(value == null){
            throw new NullPointerException();
        }
        if(find(root, (E) value) == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() {
        if(root == null) {
            throw new NoSuchElementException();
        }
        RBNode current = root;
        return (E) treeMin(current).value;
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() {
        if(root == null){
            throw new NoSuchElementException();
        }
        RBNode current = root;
        return (E) treeMax(current).value;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return this.length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        root = null;
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
        if (root != null) {
            if (root.color != RBNode.Color.BLACK) {
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
        if (RBNode.color == RedBlackTree.RBNode.Color.RED) {
            checkRedNodeRule(RBNode);
            return leftBlackHeight;
        }
        return leftBlackHeight + 1;
    }

    private void checkRedNodeRule(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode.left != null && RBNode.left.color != RedBlackTree.RBNode.Color.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then left child must be black.\n" + RBNode.toString());
        }
        if (RBNode.right != null && RBNode.right.color != RedBlackTree.RBNode.Color.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then right child must be black.\n" + RBNode.toString());
        }
    }

}

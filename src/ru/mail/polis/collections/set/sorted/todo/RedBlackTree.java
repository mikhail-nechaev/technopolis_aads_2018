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

        public RBNode(E value) {
            this.value = value;
        }

        public RBNode() {
            this.left = null;
            this.right = null;
            this.parent = null;
            this.color = RBColor.BLACK;
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
    protected RBNode<E> nil=new RBNode<>();
    protected int count=0;

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
        RBNode<E> parent=nil;
        RBNode<E> node=root;
        while (node!=nil&&node!=null){
            parent=node;
            if (comparator.compare(node.value,value)>0){
                node=node.left;
            }else if (comparator.compare(node.value,value)<0){
                node=node.right;
            }else{
                return false;
            }
        }
        node=new RBNode<>(value);
        node.parent=parent;
        node.left=nil;
        node.right=nil;
        if (parent==nil){
            root=node;
        }else if (comparator.compare(parent.value,value)>0){
            parent.left=node;
        }else{
            parent.right=node;
        }
        node.color=RBColor.RED;
        count++;
        RBInsertFiUp(node);
        return true;
    }

    private void RBInsertFiUp(RBNode<E> node) {
        while(node != root && node.parent.color == RBColor.RED) {
            if(node.parent == node.parent.parent.left) {
                RBNode<E> uncle = node.parent.parent.right;
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
                RBNode<E> uncle = node.parent.parent.left;
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

    private RBNode<E> leftRotate(RBNode<E> node) {
        RBNode<E> right=node.right;
        node.right=right.left;
        if (right.left!=nil){
            right.left.parent=node;
        }
        right.parent=node.parent;
        if (node.parent==nil){
            root=right;
        }else if (node==node.parent.left){
            node.parent.left=right;
        }else{
            node.parent.right=right;
        }
        right.left=node;
        node.parent=right;
        return root;
    }

    private RBNode<E> rightRotate(RBNode<E> node) {
        RBNode<E> left=node.left;
        node.left=left.right;
        if (left.right!=nil){
            left.right.parent=node;
        }
        left.parent=node.parent;
        if (node.parent==nil){
            root=left;
        }else if (node==node.parent.right){
            node.parent.right=left;
        }else{
            node.parent.left=left;
        }
        left.right=node;
        node.parent=left;
        return root;
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
        RBNode<E> current = root;
        while (current!=nil&&current!=null){
            if (comparator.compare(current.value,value)>0){
                current=current.left;
            }else if (comparator.compare(current.value,value)<0){
                current=current.right;
            }else{
                remove(current,value);
                count--;
                return true;
            }
        }
        return false;
    }

    protected void remove(RBNode<E> node, E value) {
        RBNode<E> y;
        RBNode<E> x;
        if (node.left==nil||node.right==nil){
            y=node;
        }else{
            y=treeSuccessor(node);
        }
        if (y.left!=nil){
            x=y.left;
        }else{
            x=y.right;
        }
        x.parent=y.parent;
        if (y.parent==nil){
            root=x;
        }else{
            if (y==y.parent.left){
                y.parent.left=x;
            }else{
                y.parent.right=x;
            }
        }
        if (y!=node){
            node.value=y.value;
        }
        if (y.color==RBColor.BLACK){
            RBDeleteFixUp(x);
        }
    }

    private void RBDeleteFixUp(RBNode<E> node) {
        RBNode<E> w;
        while (node!=root&&node.color==RBColor.BLACK){
            if(node==node.parent.left){
                w=node.parent.right;
                if (w.color==RBColor.RED){
                    w.color=RBColor.BLACK;
                    node.parent.color=RBColor.RED;
                    leftRotate(node.parent);
                    w=node.parent.right;
                }
                if (w.left.color==RBColor.BLACK&&w.right.color==RBColor.BLACK){
                    w.color=RBColor.RED;
                    node=node.parent;
                }else {
                    if (w.right.color==RBColor.BLACK){
                        w.left.color=RBColor.BLACK;
                        w.color=RBColor.RED;
                        rightRotate(w);
                        w=node.parent.right;
                    }
                    w.color=node.parent.color;
                    node.parent.color=RBColor.BLACK;
                    w.right.color=RBColor.BLACK;
                    leftRotate(node.parent);
                    node=root;
                }
            }else{
                w=node.parent.left;
                if (w.color==RBColor.RED){
                    w.color=RBColor.BLACK;
                    node.parent.color=RBColor.RED;
                    rightRotate(node.parent);
                    w=node.parent.left;
                }
                if (w.right.color==RBColor.BLACK&&w.left.color==RBColor.BLACK){
                    w.color=RBColor.RED;
                    node=node.parent;
                }else {
                    if (w.left.color==RBColor.BLACK){
                        w.right.color=RBColor.BLACK;
                        w.color=RBColor.RED;
                        leftRotate(w);
                        w=node.parent.left;
                    }
                    w.color=node.parent.color;
                    node.parent.color=RBColor.BLACK;
                    w.left.color=RBColor.BLACK;
                    rightRotate(node.parent);
                    node=root;
                }
            }
        }
        node.color=RBColor.BLACK;
    }

    private RBNode<E> treeSuccessor(RBNode<E> node) {
        if (node.right!=nil){
            RBNode<E> current=node.right;
            while (current.left!=nil){
                current=current.left;
            }
            return current;
        }
        RBNode<E> y=node.parent;
        while (y!=nil&&node==y.right){
            node=y;
            y=y.parent;
        }
        return y;
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
        RBNode<E> node=root;
        while (node!=nil&&node!=null){
            if(comparator.compare(node.value,value)>0){
                node=node.left;
            }else if (comparator.compare(node.value,value)<0){
                node=node.right;
            }else{
                return true;
            }
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
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        RBNode<E> node=root;
        while (node.left!=nil&&node.left!=null){
            node=node.left;
        }
        return node.value;
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() {
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        RBNode<E> node=root;
        while (node.right!=nil&&node.right!=null){
            node=node.right;
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
        return count;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return count==0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        count=0;
        root=nil;
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

package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;


@SuppressWarnings("unchecked")
public class RedBlackTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {


    protected static final class RBNode<E extends Comparable<E>> {
        E value;
        RBNode left;
        RBNode right;
        RBNode parent;
        int numRight ;
        int numLeft ;
        Color color;

        private enum  Color{
            RED, BLACK
        }

        public RBNode(E value){
            this();
            this.value = value;
        }

        private RBNode() {
            color = Color.BLACK;
            parent = null;
            left = null;
            right = null;
            numLeft = 0;
            numRight = 0;
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
    protected RBNode nil = new RBNode();
    protected RBNode root = nil;
    protected int length;


    public RedBlackTree() {
        this(Comparator.naturalOrder());
        root.left = nil;
        root.right = nil;
        root.parent = nil;
        length = 0;
    }

    public RedBlackTree(Comparator<E> comparator) {
        if(comparator == null){
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
    }


    @Override
    public void checkBalance() throws UnbalancedTreeException {
        if (root != null || !isNil(root)) {
            if (root.color != RBNode.Color.BLACK) {
                throw new UnbalancedTreeException("Root must be black");
            }
            traverseTreeAndCheckBalanced(root);
        }
    }

    protected int traverseTreeAndCheckBalanced(RBNode RBNode) throws UnbalancedTreeException {
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

    protected void checkRedNodeRule(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode.left != null && RBNode.left.color != RedBlackTree.RBNode.Color.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then left child must be black.\n" + RBNode.toString());
        }
        if (RBNode.right != null && RBNode.right.color != RedBlackTree.RBNode.Color.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then right child must be black.\n" + RBNode.toString());
        }
    }

    protected boolean isNil(RBNode node){
        return node == nil;
    }



    protected void leftRotateFixup(RBNode x){
        if (isNil(x.left) && isNil(x.right.left)){
            x.numLeft = 0;
            x.numRight = 0;
            x.right.numLeft = 1;
        }
        else if (isNil(x.left) && !isNil(x.right.left)){
            x.numLeft = 0;
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 2 + x.right.left.numLeft +
                    x.right.left.numRight;
        }


        else if (!isNil(x.left) && isNil(x.right.left)){
            x.numRight = 0;
            x.right.numLeft = 2 + x.left.numLeft + x.left.numRight;
        }

        else{
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 3 + x.left.numLeft + x.left.numRight +
                    x.right.left.numLeft + x.right.left.numRight;
        }
    }


    protected RBNode<E> treeSuccessor(RBNode<E> x){
        if (!isNil(x.left) )
            return treeMinimum(x.right);
        RBNode<E> y = x.parent;
        while (!isNil(y) && x == y.right){
            x = y;
            y = y.parent;
        }
        return y;
    }



    protected RBNode <E> treeMaximum(RBNode<E> node){
        while (!isNil(node.right))
            node = node.right;
        return node;
    }


    protected RBNode<E> treeMinimum(RBNode<E> node){
        while (!isNil(node.left))
            node = node.left;
        return node;
    }



    protected void leftRotate(RBNode<E> x){

        leftRotateFixup(x);
        RBNode<E> y;
        y = x.right;
        x.right = y.left;

        if (!isNil(y.left))
            y.left.parent = x;
        y.parent = x.parent;

        if (isNil(x.parent))
            root = y;

        else if (x.parent.left == x)
            x.parent.left = y;

        else
            x.parent.right = y;
        y.left = x;
        x.parent = y;
    }





    protected void rightRotate(RBNode<E> y){

        rightRotateFixup(y);

        RBNode<E> x = y.left;
        y.left = x.right;

        if (!isNil(x.right))
            x.right.parent = y;
        x.parent = y.parent;

        if (isNil(y.parent))
            root = x;

        else if (y.parent.right == y)
            y.parent.right = x;

        else
            y.parent.left = x;
        x.right = y;

        y.parent = x;

    }
    protected void rightRotateFixup(RBNode<E> y){

        if (isNil(y.right) && isNil(y.left.right)){
            y.numRight = 0;
            y.numLeft = 0;
            y.left.numRight = 1;
        }


        else if (isNil(y.right) && !isNil(y.left.right)){
            y.numRight = 0;
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 2 + y.left.right.numRight +
                    y.left.right.numLeft;
        }


        else if (!isNil(y.right) && isNil(y.left.right)){
            y.numLeft = 0;
            y.left.numRight = 2 + y.right.numRight +y.right.numLeft;

        }


        else{
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 3 + y.right.numRight +
                    y.right.numLeft +
                    y.left.right.numRight + y.left.right.numLeft;
        }

    }



    protected void insert(RBNode<E> z) {
        RBNode<E> y = nil;
        RBNode<E> x = root;
        while (!isNil(x)){
            y = x;
            if (comparator.compare(z.value, x.value) < 0){
                x.numLeft++;
                x = x.left;
            }
            else{
                x.numRight++;
                x = x.right;
            }
        }
        z.parent = y;
        if (isNil(y))
            root = z;
        else if (comparator.compare(z.value, y.value) < 0)
            y.left = z;
        else
            y.right = z;
        z.left = nil;
        z.right = nil;
        z.color = RBNode.Color.RED;


        insertFixup(z);

    }
    protected void insertFixup(RBNode z){

        RBNode<E> y = nil;

        while (z.parent.color == RBNode.Color.RED){

            if (z.parent == z.parent.parent.left){
                y = z.parent.parent.right;
                if (y.color == RBNode.Color.RED){
                    z.parent.color = RBNode.Color.BLACK;
                    y.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    z = z.parent.parent;
                }
                else if (z == z.parent.right){
                    z = z.parent;
                    leftRotate(z);
                }
                else{
                    z.parent.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    rightRotate(z.parent.parent);
                }
            }
            else{
                y = z.parent.parent.left;
                if (y.color == RBNode.Color.RED){
                    z.parent.color = RBNode.Color.BLACK;
                    y.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    z = z.parent.parent;
                }
                else if (z == z.parent.left){
                    z = z.parent;
                    rightRotate(z);
                }
                else{
                    z.parent.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }

        root.color = RBNode.Color.BLACK;

    }




    protected void remove(RBNode<E> v){
        RBNode<E> z = search(v.value);
        RBNode<E> x = nil;
        RBNode<E> y = nil;

        if (isNil(z.left) || isNil(z.right))
            y = z;

        else y = treeSuccessor(z);

        if (!isNil(y.left))
            x = y.left;
        else
            x = y.right;

        x.parent = y.parent;

        if (isNil(y.parent))
            root = x;
        else if (!isNil(y.parent.left) && y.parent.left == y)
            y.parent.left = x;

        else if (!isNil(y.parent.right) && y.parent.right == y)
            y.parent.right = x;

        if (y != z){
            z.value = y.value;
        }
        fixNodeData(x,y);
        if (y.color == RBNode.Color.BLACK)
            removeFixup(x);
    }

    private void fixNodeData(RBNode<E> x, RBNode<E> y){
        RBNode<E> current = nil;
        RBNode<E> track = nil;

        if (isNil(x)){
            current = y.parent;
            track = y;
        }

        else{
            current = x.parent;
            track = x;
        }

        while (!isNil(current)){
            if (comparator.compare(y.value, current.value) != 0) {

                if (comparator.compare(y.value, current.value) > 0)
                    current.numRight--;

                if (comparator.compare(y.value, current.value) < 0)
                    current.numLeft--;
            }

            else{

                if (isNil(current.left))
                    current.numLeft--;
                else if (isNil(current.right))
                    current.numRight--;

                else if (track == current.right)
                    current.numRight--;
                else if (track == current.left)
                    current.numLeft--;
            }

            track = current;
            current = current.parent;

        }

    }



    protected void removeFixup(RBNode<E> x){

        RBNode<E> w;
        while (x != root && x.color == RBNode.Color.BLACK){
            if (x == x.parent.left){
                w = x.parent.right;
                if (w.color == RBNode.Color.RED){
                    w.color = RBNode.Color.BLACK;
                    x.parent.color = RBNode.Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == RBNode.Color.BLACK &&
                        w.right.color == RBNode.Color.BLACK){
                    w.color = RBNode.Color.RED;
                    x = x.parent;
                }
                else{
                    if (w.right.color == RBNode.Color.BLACK){
                        w.left.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBNode.Color.BLACK;
                    w.right.color = RBNode.Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }
            else{
                w = x.parent.left;
                if (w.color == RBNode.Color.RED){
                    w.color =RBNode.Color.BLACK;
                    x.parent.color = RBNode.Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == RBNode.Color.BLACK &&
                        w.left.color == RBNode.Color.BLACK){
                    w.color = RBNode.Color.RED;
                    x = x.parent;
                }

                else{
                    if (w.left.color == RBNode.Color.BLACK){
                        w.right.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = RBNode.Color.BLACK;
                    w.left.color = RBNode.Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = RBNode.Color.BLACK;
    }

    protected RBNode<E> search(E value){


        RBNode<E> current = root;

        while (!isNil(current)){
            if (comparator.compare(current.value, value) == 0)
                return current;
            else if (comparator.compare(current.value, value) < 0)
                current = current.right;
            else
                current = current.left;
        }

        return null;
    }


    @Override
    public boolean add(E value) throws NullPointerException{
        if(value == null){
            throw new NullPointerException();
        }
        RBNode current = search(value);
        if(isNil(current) || current == null){
            insert(new RBNode<E>(value));
            length++;
            return true;
        }
        return false;
    }

    /**
     * Removes the specified element from this set if it is present.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean remove(E value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        if(root == null){
            throw new NoSuchElementException();
        }
        RBNode current = search(value);
        if(current != null){
            remove(current);
            length--;
            return true;
        }

        return false;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     * <p>
     * Complexity = O(log(n))
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(E value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }

        RBNode current = search((E)value);
        if(isNil(current) || current == null){
            return false;
        }
        return true;
    }

    /**
     * Returns the first (lowest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() throws  NoSuchElementException {

        if(isEmpty()){
            throw new NoSuchElementException();
        }

        RBNode current = treeMinimum(root);
        if(isNil(current) || current == null)
        {
            throw new IllegalStateException();
        }

        return (E) current.value;

    }

    /**
     * Returns the last (highest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException();
        }

        RBNode current = treeMaximum(root);
        if(isNil(current) || current == null)
        {
            throw new IllegalStateException();
        }

        return (E) current.value;
    }

    /**
     * Traverse self balanced tree set and check balance correctness.
     *
     * @throws UnbalancedTreeException if the self balanced tree set is unbalanced
     */

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
        return root == null || isNil(root);
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
}

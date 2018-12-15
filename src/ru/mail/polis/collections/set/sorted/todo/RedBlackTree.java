package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;


@SuppressWarnings("unchecked")
public class RedBlackTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    private static final class RBNode<E extends Comparable<E>> {
        E value;
        RBNode left;
        RBNode right;
        RBNode parent;
        int numRigh = 0;
        int numLeft = 0;
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
            numRigh = 0;
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
    }

    public RedBlackTree(Comparator<E> comparator) {
        if(comparator == null){
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
        root.left = nil;
        root.right = nil;
        root.parent = nil;
        length = 0;
    }

    private void leftRotate(RBNode current){
        RBNode child = current.right;
        current.right = child.left;
        if(child.left != null)
            child.left.parent = current;
        child.parent = current.parent;
        if(current.parent == null)
            root = child;
        if (current.parent.left == current)
            current.parent.left = child;
        else
            current.parent.right = child;
        child.left = current;
        current.parent = child;
    }
    private void rightRotate(RBNode current){
        RBNode child = current.left;
        current.left = child.right;
        if(child.right != null)
            child.right.parent = current;
        child.parent = current.parent;
        if(current.parent == null)
            root = child;
        if (current.parent.right == current)
            current.parent.right = child;
        else
            current.parent.left = child;
        child.right = current;
        current.parent = child;
    }



    private void insert(RBNode current){
        RBNode y = null;
        RBNode x = root;
        while(x != null){
            y = x;
            if(comparator.compare((E) current.value, (E)x.value) < 0)
            {
                x = x.left;
            }
            else{
                x = x.right;
            }
        }
        current.parent = y;
        if(y == null){
            root = current;
        }
        else if (comparator.compare((E) current.value, (E) y.value ) < 0)
            y.left = current;
        else
            y.right = current;
        current.left = null;
        current.right = null;
        current.color = RBNode.Color.RED;
        insertFixUp(current);
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
        if(current == null || comparator.compare((E) current.value, value) == 0)
        {
            return current;
        }
        if(comparator.compare((E) current.value, value) > 0){
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

    private RBNode treeSuccessor(RBNode current){
        if(current.left != null)
            return treeMin(current.right);
        RBNode y = current.parent;
        while(y != null && current == y.right){
            current = y;
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

    RBNode leftOf(RBNode rbNode) {
        return  rbNode == null ? null : rbNode.left;
    }

    RBNode parentOf(RBNode rbNode) {
        return  rbNode == null ? null : rbNode.parent;
    }

    private void removeFixUp(RBNode current) {
        RBNode w;
        while(current!=root && current.color.equals(RBNode.Color.BLACK)){
            if(current == current.parent.left){
                w = current.parent.right;
                if(w.color.equals(RBNode.Color.RED)){
                    w.color = RBNode.Color.BLACK;
                    current.parent.color = RBNode.Color.RED;
                    leftRotate(current.parent);
                    w = current.parent.right;
                }
                if(w.left != null && w.right != null && w.left.color.equals(RBNode.Color.BLACK) && w.right.color.equals(RBNode.Color.BLACK)){
                    w.color = RBNode.Color.RED;
                    current = current.parent;
                }
                else{
                    if(w.right.color.equals(RBNode.Color.BLACK) ){
                        w.left.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        rightRotate(w);
                        w = current.parent.right;
                    }
                    w.color = current.parent.color;
                    w.parent.color = RBNode.Color.BLACK;
                    w.right.color = RBNode.Color.BLACK;
                    leftRotate(current.parent);
                    current = root;
                }
            }
            else{
                w = current.parent.left;
                if(w.color.equals(RBNode.Color.RED)){
                    w.color = RBNode.Color.BLACK;
                    current.parent.color = RBNode.Color.RED;
                    rightRotate(current.parent);
                    w = current.parent.left;
                }
                if(w.right != null && w.left != null && w.right.color.equals(RBNode.Color.BLACK) && w.left.color.equals(RBNode.Color.BLACK)){
                    w.color = RBNode.Color.RED;
                    current = current.parent;
                }
                else{
                    if(w.left.color.equals(RBNode.Color.BLACK) ){
                        w.right.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        leftRotate(w);
                        w = current.parent.left;
                    }
                    w.color = current.parent.color;
                    w.parent.color = RBNode.Color.BLACK;
                    w.left.color = RBNode.Color.BLACK;
                    rightRotate(current.parent);
                    current = root;
                }
            }
        }
        current.color = RBNode.Color.BLACK;
    }





    @Override
    public boolean add(E value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        if(find(root,value) == null) {
            insert(new RBNode(value));
            length = length + 1;
            return true;
        }

        return false;

    }

    @Override
    public boolean remove(E value) throws NullPointerException{
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


    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean contains(Object value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        if(find(root, (E) value) != null){
            return true;
        }
        return false;

    }


    @Override
    public E first() throws NoSuchElementException{
        if(root == null) {
            throw new NoSuchElementException();
        }
        RBNode current = root;
        return (E) treeMin(current).value;
    }


    @Override
    public E last() throws NoSuchElementException {
        if(root == null){
            throw new NoSuchElementException();
        }
        RBNode current = root;
        return (E) treeMax(current).value;
    }


    @Override
    public int size() {
        return this.length;
    }


    @Override
    public boolean isEmpty() {
        return root != null;
    }


    @Override
    public void clear() {
        root = null;
        length = 0;
    }

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

package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {
    //не сделано
    //todo: update it if required
    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left;
        AVLNode<E> right;
        int leftHeight, rightHeight;
        boolean isChanged;

        protected AVLNode(E value) {
            this();
            this.value = value;
        }


        protected AVLNode() {
            leftHeight = rightHeight = 0;
            isChanged = false;
        }
    }


    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    protected int size, modCount;

    public AVLTree() {
        this(Comparator.naturalOrder());
    }

    /**
     * Creates a {@code ISelfBalancingSortedTreeSet} that orders its elements according to the specified comparator.
     *
     * @param comparator comparator the comparator that will be used to order this priority queue.
     * @throws NullPointerException if the specified comparator is null
     */
    public AVLTree(Comparator<E> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = comparator;
        root = new AVLNode<>();
        size = 0;
        modCount = 0;
    }

    public void printTree() {
        if (isEmpty()) {
            System.out.println("Tree is empty");
            return;
        }
        ArrayDeque<AVLNode<E>> queue = new ArrayDeque<>();
        queue.push(root.right);

        while (!queue.isEmpty()) {
            AVLNode<E> node = queue.pop();
            System.out.print(node.value + " left - ");
            if (node.left != null) {
                queue.push(node.left);
                System.out.print(node.left.value + "(" + node.leftHeight + ")");
            } else {
                System.out.print("null(0)");
            }
            System.out.print(" right - ");
            if (node.right != null) {
                queue.push(node.right);
                System.out.print(node.right.value + "(" + node.rightHeight + ")");
            } else {
                System.out.print("null(0)");
            }
            System.out.println();
        }
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
    }


    /**
     * Adds the specified element to this set if it is not already present.
     * <p>
     * Complexity = O(log(n))
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
        size++;
        modCount++;
        return addElemToTree(root, value);
    }


    protected boolean addElemToTree(AVLNode<E> node, E elem) {
        if (node == root || comparator.compare(elem, node.value) >= 0) {
            node.rightHeight++;
            if (node.right == null) {
                node.right = new AVLNode<>(elem);
                return true;
            }
            addElemToTree(node.right, elem);
            balanceNode(node.right, node, false);
        } else {
            node.leftHeight--;
            if (node.left == null) {
                node.left = new AVLNode<>(elem);
                return true;
            }
            addElemToTree(node.left, elem);
            balanceNode(node.left, node, true);
        }

        return true;
    }

    private boolean balanceNode(AVLNode<E> node, AVLNode<E> parent, boolean isLeftSon) {
        switch (getDiff(node)) {
        case -2:
            if (getDiff(node.right) <= 0) {
                smallLeftRotate(node, parent, isLeftSon);
            } else {
                bigLeftRotate(node, parent, isLeftSon);
            }
            break;
        case 2:
            if (getDiff(node) >= 0) {
                smallRightRotate(node, parent, isLeftSon);
            } else {
                bigRightRotate(node, parent, isLeftSon);
            }
            break;
        }

        return getDiff(node) == 0 && node.leftHeight != 0;
    }

    private void smallRightRotate(AVLNode<E> node, AVLNode<E> parent, boolean isLeftSon) {
        System.out.println("right Rotate " + node.value);
        AVLNode<E> b = node.left;
        setLeft(node, b.right);
        setRight(b, node);

        if (isLeftSon) {
            setLeft(parent, b);
        } else {
            setRight(parent, b);
        }
    }

    private void smallLeftRotate(AVLNode<E> node, AVLNode<E> parent, boolean isLeftSon) {
        AVLNode<E> b = node.right;
        System.out.println("left Rotate " + node.value);

        setRight(node, b.left);
        setLeft(b, node);

        if (isLeftSon) {
            setLeft(parent, b);
        } else {
            setRight(parent, b);
        }
    }

    private void bigRightRotate(AVLNode<E> node, AVLNode<E> parent, boolean isLeftSon) {
        smallLeftRotate(node.left, node, true);
        smallRightRotate(node, parent, isLeftSon);
    }

    private void bigLeftRotate(AVLNode<E> node, AVLNode<E> parent, boolean isLeftSon) {
        smallRightRotate(node.right, node, false);
        smallLeftRotate(node, parent, isLeftSon);
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

    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        }


        return !isEmpty() && searchForRemove(root, value);
    }

    private boolean searchForRemove(AVLNode<E> node, E value) {
        final int resCompare = comparator.compare(value, node.value);
        if (resCompare > 0) {
            if(node.rightHeight > 0){

            }
        }
        if (resCompare < 0) {

        }

        if (resCompare == 0) {

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
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return !isEmpty() && containsElemInTree(root.right, value);
    }

    private boolean containsElemInTree(AVLNode<E> node, E value) {
        int resCompare = comparator.compare(node.value, value);
        if (resCompare == 0) {
            return true;
        }
        if (resCompare > 0) {
            return node.right != null && containsElemInTree(node.right, value);
        }
        return node.left != null && containsElemInTree(node.left, value);
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
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        AVLNode<E> node = root.right;
        while (node.left != null) {
            node = node.left;
        }

        return node.value;
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
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        AVLNode<E> node = root.right;
        while (node.right != null) {
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
        root = new AVLNode<>();
        size = 0;
        modCount = 0;
    }

    /**
     * Обходит дерево и проверяет что высоты двух поддеревьев
     * различны по высоте не более чем на 1
     *
     * @throws UnbalancedTreeException если высоты отличаются более чем на один
     */
    @Override
    public void checkBalance() throws UnbalancedTreeException {
        traverseTreeAndCheckBalanced(root.right);
    }


    private int traverseTreeAndCheckBalanced(AVLNode<E> curr) throws UnbalancedTreeException {
        if (curr == null) {
            return 0;
        }
        int leftHeight = traverseTreeAndCheckBalanced(curr.left);
        int rightHeight = traverseTreeAndCheckBalanced(curr.right);
        if (Math.abs(leftHeight - rightHeight) > 1) {
            throw UnbalancedTreeException.create("The heights of the two child subtrees of any node must be differ by at most one",
                    leftHeight, rightHeight, curr.toString());
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public int getHeight(AVLNode<E> node) {
        if (node == null) {
            return 0;
        }
        return Math.max(node.leftHeight, node.rightHeight) + 1;
    }

    public int getDiff(AVLNode<E> node) {
        return node.leftHeight - node.rightHeight;
    }

    private void setLeft(AVLNode<E> mainNode, AVLNode<E> leftNode) {
        mainNode.left = leftNode;
        mainNode.leftHeight = getHeight(mainNode.left);
    }

    private void setRight(AVLNode<E> mainNode, AVLNode<E> rightNode) {
        mainNode.right = rightNode;
        mainNode.rightHeight = getHeight(mainNode.right);
    }

    private boolean isSheet(AVLNode<E> node) {
        return node.left == null && node.right == null;
    }
}
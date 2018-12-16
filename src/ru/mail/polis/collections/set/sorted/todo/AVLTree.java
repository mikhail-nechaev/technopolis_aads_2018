package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
@SuppressWarnings({"ALL", "unchecked"})
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {
    //не сделано
    //todo: update it if required
    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left;
        AVLNode right;
        AVLNode<E> parent;
        int leftHeight, rightHeight;

        protected AVLNode(AVLNode<E> parent, E value) {
            this(value);
            this.parent = parent;
        }

        public AVLNode(E value) {
            this();
            this.value = value;
        }

        protected AVLNode() {
            leftHeight = rightHeight = 0;
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
        if (isEmpty()) {
            root.right = new AVLNode<>(root, value);
            size++;
            modCount++;
            return true;
        }
        return addElemToTree(root.right, value);
    }


    protected boolean addElemToTree(AVLNode<E> node, E elem) {

//        while (true) {
//            int resCompare = comparator.compare(elem, node.value);
//            if (resCompare > 0) {
//                if (node.right == null) {
//                    size++;
//                    modCount++;
//                    node.right = new AVLNode<>(node, elem);
//                    balannceToRoot(node.right);
//                    return true;
//                }
//                node = node.right;
//            } else if (resCompare < 0) {
//                if (node.left == null) {
//                    size++;
//                    modCount++;
//                    node.left = new AVLNode<>(node, elem);
//                    balannceToRoot(node.left);
//                    return true;
//                }
//                node = node.left;
//            } else {
//                return false;
//            }
//        }


        int resCompare = comparator.compare(elem, node.value);

        if (resCompare > 0) {
            if (node.right == null) {
                size++;
                modCount++;
                node.right = new AVLNode<>(node, elem);
                balannceToRoot(node);
                return true;
            }
            return addElemToTree(node.right, elem);
        }

        if (resCompare < 0) {
            if (node.left == null) {
                size++;
                modCount++;
                node.left = new AVLNode<>(node, elem);
                balannceToRoot(node);
                return true;
            }
            return addElemToTree(node.left, elem);
        }

        return false;
    }


    private void balance2(AVLNode<E> node, boolean isLeftSon) {
        remakeHeight(node);
        if (getDiff(node) == 2) {
            if (getDiff(node.right) < 0) {
                smallRightRotate(node.right, isLeftSon);
            }
            smallLeftRotate(node, isLeftSon);
        }
        if (getDiff(node) == -2) {
            if (getDiff(node.left) > 0) {
                smallLeftRotate(node.left, isLeftSon);
            }
            smallRightRotate(node, isLeftSon);
        }
    }

    private boolean balanceNode(AVLNode<E> node, boolean isLeftSon) {
        switch (getDiff(node)) {
        case -2:
            if (getDiff(node.right) <= 0) {
                smallLeftRotate(node, isLeftSon);
            } else {
                bigLeftRotate(node, isLeftSon);
            }
            break;
        case 2:
            if (getDiff(node) >= 0) {
                smallRightRotate(node, isLeftSon);
            } else {
                bigRightRotate(node, isLeftSon);
            }
            break;
        }

        return getDiff(node) == 0 && node.leftHeight != 0;
    }

    private void smallRightRotate(AVLNode<E> node, boolean isLeftSon) {
      //  System.out.println("right Rotate " + node.value);
        AVLNode<E> parent = node.parent;
        AVLNode<E> b = node.left;

        if (b != null) {
            setLeft(node, b.right);
            setRight(b, node);
        } else {
            setLeft(node, null);
        }


        if (isLeftSon) {
            setLeft(parent, b);
        } else {
            setRight(parent, b);
        }
        remakeHeight(node);
        remakeHeight(b);
        remakeHeight(parent);
    }

    private void smallLeftRotate(AVLNode<E> node, boolean isLeftSon) {
        AVLNode<E> parent = node.parent;
        AVLNode<E> b = node.right;
       // System.out.println("left Rotate " + node.value);
        if (b != null) {
            setRight(node, b.left);
            setLeft(b, node);
        } else {
            setRight(node, null);
        }

        if (isLeftSon) {
            setLeft(parent, b);
        } else {
            setRight(parent, b);
        }
        remakeHeight(node);
        remakeHeight(b);
        remakeHeight(parent);
    }

    private void bigRightRotate(AVLNode<E> node, boolean isLeftSon) {
        smallLeftRotate(node.left, true);
        smallRightRotate(node, isLeftSon);
    }

    private void bigLeftRotate(AVLNode<E> node, boolean isLeftSon) {
        smallRightRotate(node.right, false);
        smallLeftRotate(node, isLeftSon);
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
        return !isEmpty() && remove2(root.right, value);
    }

    private boolean remove2(AVLNode<E> node, E value) {
        if (node == null) {
            return false;
        }
        final int resCompare = comparator.compare(value, node.value);
        if (resCompare > 0) {
            return remove2(node.right, value);
        } else if (resCompare < 0) {
            return remove2(node.right, value);
        } else if (resCompare == 0) {
            AVLNode<E> q = node.left;
            AVLNode<E> r = node.right;
            if( r == null ) return true;
            AVLNode<E> min = getMin(r);
            min.right = deleteMin(r);
            min.left = q;
            balance2(min, isLeftSon(min));
            return true;
        }
        balannceToRoot(node);
        return true;
    }

    private AVLNode deleteMin(AVLNode node) {
        if (node.left == null) {
            return node.right;
        }

        node.left = deleteMin(node.left);
        balance2(node, isLeftSon(node));
        return node;
    }

    private AVLNode<E> getMin(AVLNode x) {
        return x.left == null ? x : getMin(x.left);
    }

    private boolean searchForRemove(AVLNode<E> node, E value) {
        if (node == null) {
            return false;
        }

        final int resCompare = comparator.compare(value, node.value);
        if (resCompare > 0) {
            return searchForRemove(node.right, value);
        }
        if (resCompare < 0) {
            return searchForRemove(node.left, value);
        }

        if (resCompare == 0) {
            size--;
            modCount++;
            AVLNode<E> nodeToBalance;
            if (isSheet(node)) {
                nodeToBalance = node.parent;
                deleteNode(node);
            } else {

                if (node.right != null) {

                    AVLNode<E> min = getMin(node.right);
                    node.value = min.value;
                    nodeToBalance = min.parent;
                    deleteNode(min);
                } else {
                    nodeToBalance = node.left;
                    if (isLeftSon(node)) {
                        setLeft(node.parent, node.left);
//                        node.parent.left = node.left;
                    } else {
                        setRight(node.parent, node.left);
//                        node.parent.right = node.left;
                    }
                }
            }


            return balannceToRoot(nodeToBalance);
        }


        return true;
    }

    private void deleteNode(AVLNode<E> node) {
        if (isLeftSon(node)) {
            node.parent.left = null;
        } else {
            node.parent.right = null;
        }
    }

    private boolean balannceToRoot(AVLNode<E> node) {
        while (node.parent != null) {
            // System.out.println("Balance node - " + node.value);
            remakeHeight(node.left);
            remakeHeight(node.right);
            remakeHeight(node);

            AVLNode<E> next = node.parent;
            balance2(node, isLeftSon(node));
            node = next;
        }
        return true;
    }

    private boolean isLeftSon(AVLNode<E> node) {
        return node.parent.left == node;
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
        return containsElemInTree(root.right, value);
    }

    private boolean containsElemInTree(AVLNode<E> node, E value) {
        if (node == null) {
            return false;
        }
        int resCompare = comparator.compare(value, node.value);
        if (resCompare == 0) {
            return true;
        }
        if (resCompare > 0) {
            return containsElemInTree(node.right, value);
        }
        return containsElemInTree(node.left, value);
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
        return getMin(root.right).value;

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
        //  System.out.println("CHEEEEEEEECK");
        // printTree();
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
        return node.rightHeight - node.leftHeight;
    }

    private void setLeft(AVLNode<E> mainNode, AVLNode<E> leftNode) {
        mainNode.left = leftNode;
        mainNode.leftHeight = getHeight(mainNode.left);
        if (leftNode != null) {
            leftNode.parent = mainNode;
        }
    }

    @SuppressWarnings("unchecked")
    private void setRight(AVLNode<E> mainNode, AVLNode<E> rightNode) {
        mainNode.right = rightNode;
        mainNode.rightHeight = getHeight(mainNode.right);
        if (rightNode != null) {
            rightNode.parent = mainNode;
        }
    }

    private void remakeHeight(AVLNode<E> node) {
        if (node != null) {
            node.rightHeight = getHeight(node.right);
            node.leftHeight = getHeight(node.left);
        }
    }

    private boolean isSheet(AVLNode<E> node) {
        return node.left == null && node.right == null;
    }
}
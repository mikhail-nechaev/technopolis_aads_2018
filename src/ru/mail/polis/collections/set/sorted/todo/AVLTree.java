package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.NodeAlreadyExistsException;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A AVL tree based {@link ISelfBalancingSortedTreeSet} implementation.
 *
 * <a href="https://en.wikipedia.org/wiki/AVL_tree>AVL_tree</a>
 *
 * @param <E> the type of elements maintained by this set
 */
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    //todo: update it if required
    protected static class AVLNode<E> {
        AVLNode(E value) {
            this.value = value;
            height = 1;
        }
        E value;
        AVLNode<E> left;
        AVLNode<E> right;
        int height;
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    protected int size = 0;
    protected int modCount = 0;

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
        Objects.requireNonNull(comparator);
        this.comparator = comparator;
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
        Objects.requireNonNull(value);
        if (root == null) {
            root = new AVLNode<>(value);
            size++;
            modCount++;
            return true;
        }
        try{
            root = add(root, value);
            modCount++;
            return true;
        } catch (NodeAlreadyExistsException e) {
            return false;
        }
    }

    private AVLNode<E> add(AVLNode<E> curr, E value) throws NodeAlreadyExistsException{
        if (curr == null) {
            curr = new AVLNode<>(value);
            size++;
            return curr;
        }
        int cmp = compare(curr.value, value);
        if (cmp == 0) throw new NodeAlreadyExistsException();
        if (cmp > 0) curr.left = add(curr.left, value);
        else curr.right = add(curr.right, value);
        return balance(curr);
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
        Objects.requireNonNull(value);
        if (root == null) return false;
        try {
            root = remove(root, value);
            size--;
            modCount++;
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private AVLNode<E> remove(AVLNode<E> v, E value) throws NoSuchElementException {
        if (v == null)
            throw new NoSuchElementException();
        if (compare(value, v.value) < 0) {
            v.left = remove(v.left, value);
        } else if(compare(value, v.value) > 0) {
            v.right = remove(v.right, value);
        } else {
            AVLNode<E> left = v.left;
            AVLNode<E> right = v.right;
            v.value = null;
            v = null;
            if (right == null) return left;
            AVLNode<E> min = findMin(right);
            min.right = removeMin(right);
            min.left = left;
            return balance(min);
        }
        return balance(v);
    }
    //Поиск минимального потомка узла v
    private AVLNode<E> findMin(AVLNode<E> v) {
        AVLNode<E> curr = v;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }
    //Удаление минимального потомка узла v
    private AVLNode<E> removeMin(AVLNode<E> v) {
        if (v.left == null)
            return v.right;
        v.left = removeMin(v.left);
        return balance(v);
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
        Objects.requireNonNull(value);
        AVLNode<E> curr = root;
        while (curr != null) {
            if (compare(curr.value, value) == 0)
                return true;
            if (compare(value, curr.value) < 0)
                curr = curr.left;
            else
                curr = curr.right;
        }
        return false;
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
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        AVLNode<E> curr = root;
        while(curr.left != null) {
            curr = curr.left;
        }
        return curr.value;
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
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        AVLNode<E> curr = root;
        while(curr.right != null) {
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
        modCount++;
    }

    /**
     * Обходит дерево и проверяет что высоты двух поддеревьев
     * различны по высоте не более чем на 1
     *
     * @throws UnbalancedTreeException если высоты отличаются более чем на один
     */
    @Override
    public void checkBalance() throws UnbalancedTreeException {
        traverseTreeAndCheckBalanced(root);
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

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }
    //Малое правое вращение
    private AVLNode<E> rightRotate(AVLNode<E> v) {
        if (v == null) return v;
        AVLNode<E> x = v.left;
        v.left = x.right;
        x.right = v;
        fixBalanceFactor(v);
        fixBalanceFactor(x);
        return x;
    }
    //Малое левое вращение
    private AVLNode<E> leftRotate(AVLNode<E> v) {
        if (v == null) return  v;
        AVLNode<E> x = v.right;
        v.right = x.left;
        x.left = v;
        fixBalanceFactor(v);
        fixBalanceFactor(x);
        return x;
    }
    //Разница между левым и правым поддеревом
    private int balanceFactor(AVLNode<E> curr) {
        if (curr == null) return 0;
        int leftHeight = curr.left == null ? 0 : curr.left.height;
        int rightHeight = curr.right == null ? 0 : curr.right.height;
        return rightHeight - leftHeight;
    }
    //Установка высоты поддерева
    private void fixBalanceFactor(AVLNode<E> curr) {
        int leftHeight = curr.left == null ? 0 : curr.left.height;
        int rightHeight = curr.right == null ? 0 : curr.right.height;
        curr.height = (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
    }
    //Балансировка дерева
    private AVLNode<E> balance(AVLNode<E> v) {
        fixBalanceFactor(v);
        if (balanceFactor(v) == 2) {
            if (balanceFactor(v.right) < 0)
                v.right = rightRotate(v.right);
            return leftRotate(v);
        }
        if (balanceFactor(v) == -2) {
            if (balanceFactor(v.left) > 0)
                v.left = leftRotate(v.left);
            return rightRotate(v);
        }
        return v;
    }
}

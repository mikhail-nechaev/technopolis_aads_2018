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
public class AVLTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {

    protected static class AVLNode<E> {
        E value;
        AVLNode<E> left;
        AVLNode<E> right;
        AVLNode<E> parent;

        AVLNode(E value) {
            this.value = value;
        }
    }

    /**
     * The comparator used to maintain order in this tree map.
     */
    protected final Comparator<E> comparator;
    protected AVLNode<E> root;
    private boolean isFirst = true;
    private int size = 0;

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
        this.comparator = comparator;
    }

    private void smallLeftRout(AVLNode<E> a) {
        AVLNode<E> b = a.right;
        a.right = b.left;
        b.left = a;
        b.parent = a.parent;
        a.parent = b;
        if (a.right != null) {
            a.right.parent = a;
        }
        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        } else {
            root = b;
        }
    }

    private void smallRightRout(AVLNode<E> a) {
        AVLNode<E> b = a.left;
        a.left = b.right;
        b.right = a;
        b.parent = a.parent;
        a.parent = b;
        if (a.left != null) {
            a.left.parent = a;
        }
        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        } else {
            root = b;
        }
    }

    private void bigRightRout(AVLNode<E> a) {
        AVLNode<E> b = a.left;
        AVLNode<E> c = b.right;
        if (c.right == null) {
            a.left = null;
        } else {
            a.left = c.right;
            a.left.parent = a;
        }
        if (c.left == null) {
            b.right = null;
        } else {
            b.right = c.left;
            b.right.parent = b;
        }
        c.parent = a.parent;
        c.right = a;
        a.parent = c;
        c.left = b;
        b.parent = c;
        if (c.parent == null) {
            root = c;
        } else {
            if (c.parent.left == a) {
                c.parent.left = c;
            } else {
                c.parent.right = c;
            }
        }
    }

    private void bigLeftRout(AVLNode<E> a) {
        AVLNode<E> b = a.right;
        AVLNode<E> c = b.left;
        if (c.left == null) {
            a.right = null;
        } else {
            a.right = c.left;
            a.right.parent = a;
        }
        if (c.right == null) {
            b.left = null;
        } else {
            b.left = c.right;
            b.left.parent = b;
        }
        c.parent = a.parent;
        c.left = a;
        a.parent = c;
        c.right = b;
        b.parent = c;
        if (c.parent == null) {
            root = c;
        } else {
            if (c.parent.left == a) {
                c.parent.left = c;
            } else {
                c.parent.right = c;
            }
        }
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
        if (isFirst) {
            root = new AVLNode<>(value);
            size++;
            isFirst = false;
            return true;
        }
        AVLNode<E> node = add(value, root);
        if (node == null) {
            return false;
        }
        size++;
        balansed(node);
        return true;
    }

    private AVLNode<E> add(E value, AVLNode<E> node) {
        if (comparator.compare(node.value,value) > 0) {
            if (node.left == null) {
                node.left = new AVLNode<>(value);
                node.left.parent = node;
                return node.left;
            }
            return add(value, node.left);
        } else if (comparator.compare(node.value,value) < 0) {
            if (node.right == null) {
                node.right = new AVLNode<>(value);
                node.right.parent = node;
                return node.right;
            }
            return add(value, node.right);
        } else {
            return null;
        }
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
        AVLNode<E> removed = remove(value, root);
        if (removed == null) {
            return false;
        }

        // Ищим самый малый справа
        if (removed.right != null) {
            AVLNode<E> changed = removed.right;
            // Перый не самый малый
            if (changed.left != null) {
                do {
                    changed = changed.left;
                } while (changed.left != null);
                removed.value = changed.value;
                // Самый малый имеет узлы снизу
                if (changed.right != null) {
                    removed = changed.parent;
                    changed.parent.left = changed.right;
                    changed.right.parent = changed.parent;
                } else {
                    //У самого малого нет справа узлов
                    removed = changed.parent;
                    changed.parent.left = null;
                }
            } else {
                // Первый самый малый
                if (removed.left != null) {
                    changed.left = removed.left;
                    removed.left.parent = changed;
                }
                // Удаляемый - корень
                if (removed.parent == null) {
                    root = changed;
                    changed.parent = null;
                } else {
                    // Удаляемый не корень
                        changed.parent = removed.parent;
                    // Удаляемый на левой ветке родителя
                    if (removed.parent.left == removed) {
                        removed.parent.left = changed;
                    } else {
                        // Удаляемый на правой ветке родителя
                        removed.parent.right = changed;
                    }
                }
                removed = changed;
            }
        } else if (removed.left != null) {
            // Справа ничего нет, ищим самый большой слева
            AVLNode<E> changed = removed.left;
            // Перый не самый большой
            if (changed.right != null) {
                do {
                    changed = changed.right;
                } while (changed.right != null);
                removed.value = changed.value;
                // Самый большой имеет узлы снизу
                if (changed.left != null) {
                    removed = changed.parent;
                    changed.parent.right = changed.left;
                    changed.left.parent = changed.parent;
                } else {
                    //У самого большого нет слева узлов
                    removed = changed.parent;
                    changed.parent.right = null;
                }
            } else {
                // Первый самый большой
                // Удаляемый - корень
                if (removed.parent == null) {
                    root = changed;
                    changed.parent = null;
                } else {
                    // Удаляемый не корень
                    changed.parent = removed.parent;
                    // Удаляемый на левой ветке родителя
                    if (removed.parent.right == removed) {
                        removed.parent.right = changed;
                    } else {
                        // Удаляемый на правой ветке родителя
                        removed.parent.left = changed;
                    }
                }
                removed = changed;
            }
        } else {
            // Ни справа, ни слева нет веток, удаляемый - лист
            // Удаляемый - корень
            if (removed.parent == null) {
                clear();
                return true;
            } else {
                // Удаляемый не корень
                // Удаляемый на левой ветке родителя
                if (removed.parent.right == removed) {
                    removed.parent.right = null;
                } else {
                    // Удаляемый на правой ветке родителя
                    removed.parent.left = null;
                }
                    removed = removed.parent;
            }
        }

        size--;
        balansed(removed);
        return true;
    }

    private AVLNode<E> remove(E value, AVLNode<E> node) {
        if (node == null) {
            return null;
        }
        if (comparator.compare(node.value,value) > 0) {
            if (node.left == null) {
                return null;
            }
            return remove(value, node.left);
        } else if (comparator.compare(node.value,value) < 0) {
            if (node.right == null) {
                return null;
            }
            return remove(value, node.right);
        } else {
            return node;
        }
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
        if (isEmpty()) {
            return false;
        }
        AVLNode<E> node = root;
        return contains(value, node);
    }
    private boolean contains(E value, AVLNode<E> node) {
        if (comparator.compare(node.value,value) > 0) {
            if (node.left == null) {
                return false;
            }
            return contains(value, node.left);
        } else if (comparator.compare(node.value,value) < 0) {
            if (node.right == null) {
                return false;
            }
            return contains(value, node.right);
        } else {
            return true;
        }
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
        AVLNode<E> node = root;
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
        AVLNode<E> node = root;
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
        root = null;
        size = 0;
        isFirst = true;
    }

    private void balansed(AVLNode<E> node) {
        if (node == null) return;
        int diffA = getHeight(node.left) - getHeight(node.right);
        // left
        if (diffA == -2) {
            int diffB = getHeight(node.right.left) - getHeight(node.right.right);
            if (diffB <= 0) {
                System.out.println("sl");
                smallLeftRout(node);
            } else {
                System.out.println("bl");
                bigLeftRout(node);
            }
        }
        // right
        if (diffA == 2) {
            int diffB = getHeight(node.left.left) - getHeight(node.left.right);
            if (diffB >= 0) {
                System.out.println("sr");
                smallRightRout(node);
            } else {
                System.out.println("br");
                bigRightRout(node);
            }
        }
        if (node.parent == null) {
            return;
        }
        balansed(node.parent);
    }
    private int getHeight(AVLNode<E> node) {
        if (node == null) return 0;
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
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
}

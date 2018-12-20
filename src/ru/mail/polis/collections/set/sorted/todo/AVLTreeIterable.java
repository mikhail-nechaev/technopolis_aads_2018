package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISortedSetIterable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AVLTreeIterable<E extends Comparable<E>> extends AVLTree<E> implements ISortedSetIterable<E> {

    public AVLTreeIterable() {
        super();
    }

    public AVLTreeIterable(Comparator<E> comparator) {
        super(comparator);
    }

    protected void delete(E value) {
        remove(value);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            AVLNode<E> next = getMin(root);
            AVLNode<E> last;
            int index = 0;
            int count = length;
            boolean toRight = false;
            boolean removeCheck = false;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                last = next;
                index++;
                removeCheck = true;
                if (toRight || next.parent == null) {
                    next = getMin(next.right);
                    toRight = false;
                    return last.value;
                }

                if (isLeftSon(next)) {
                    next = next.parent;
                    toRight = next.right != null;
                } else {
                    while (next.parent != null && !isLeftSon(next)) {
                        next = next.parent;
                    }
                    if (next.parent == null) {
                        next = getMin(next.right);
                        toRight = false;
                    } else {
                        next = next.parent;
                        toRight = next.right != null;
                    }
                }

                return last.value;
            }

            @Override
            public void remove() {
                if (!removeCheck) {
                    throw new IllegalStateException();
                }
                removeCheck = false;
                delete(last.value);
                count--;
            }
        };
    }


    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            int index = 0;
            int count = length;
            boolean toLeft = false;
            boolean removeCheck = false;
            AVLNode<E> next = getMax(root);
            AVLNode<E> last;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                last = next;
                index++;
                removeCheck = true;
                if (toLeft || next.parent == null) {
                    next = getMax(next.left);
                    toLeft = false;
                    return last.value;
                }

                if (!isLeftSon(next)) {
                    next = next.parent;
                    toLeft = next.left != null;
                } else {
                    while (next.parent != null && isLeftSon(next)) {
                        next = next.parent;
                    }
                    if (next.parent == null) {
                        next = getMax(next.left);
                        toLeft = false;
                    } else {
                        next = next.parent;
                        toLeft = next.left != null;
                    }
                }

                return last.value;
            }

            @Override
            public void remove() {
                if (!removeCheck) {
                    throw new IllegalStateException();
                }
                removeCheck = false;
                delete(last.value);
                count--;
            }
        };
    }
}
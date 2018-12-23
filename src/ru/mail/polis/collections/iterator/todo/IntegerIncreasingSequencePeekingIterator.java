package ru.mail.polis.collections.iterator.todo;

import java.util.NoSuchElementException;

import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.IPeekingIterator;

/**
 *
 */
public class IntegerIncreasingSequencePeekingIterator implements IIncreasingSequenceIterator<Integer> {

    private int first;
    private Integer current = null;
    private int last;
    private int maxStep;

    /**
     * minStep = 1
     *
     * first must be less or equal last
     *
     * maxStep must be positive
     *
     * @param first — min value in iterator
     * @param last - max value in iterator included
     * @param maxStep — max diff between adjacent values
     * @throws IllegalArgumentException if arguments is invalid
     */
    public IntegerIncreasingSequencePeekingIterator(int first, int last, int maxStep) {
        if (first > last || maxStep < 1) throw new IllegalArgumentException();
        this.first = first;
        this.last = last;
        this.maxStep = maxStep;
    }


    /**
     * Returns {@code true} if the iteration has more elements.
     *
     * In other words, returns {@code false} if lastNextElement + minStep > last.
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return current == null || current != last;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        current = peek();
        return current;
    }

    /**
     * Retrieves the next element in the iteration, but does not iterate.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer peek() {
        if (!hasNext()) throw new NoSuchElementException();
        Integer next;
        if (current == null) next = first;
        else if (current + maxStep <= current || current + maxStep > last) next = last;
        else next = current + maxStep;
        return next;
    }

    /**
     * Compares this object with the specified object for order.
     * Returns a negative integer, zero, or a positive integer as this peeked value is less
     * than, equal to, or greater than the peeked value specified object.
     *
     * <p> If iterator has no elements so it is less.
     *
     * @param other the {@link IPeekingIterator} to be compared.
     * @return a negative integer, zero, or a positive integer as this peeked value
     * is less than, equal to, or greater than the peeked value specified iterator.
     */
    @Override
    public int compareTo(IPeekingIterator<Integer> other) {
        if (!this.hasNext() && !other.hasNext()) return 0;
        if (!this.hasNext()) return -1;
        if (!other.hasNext()) return 1;
        return this.peek().compareTo(other.peek());
    }
}

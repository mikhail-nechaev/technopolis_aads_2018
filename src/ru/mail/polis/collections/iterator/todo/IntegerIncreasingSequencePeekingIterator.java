package ru.mail.polis.collections.iterator.todo;

import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.IPeekingIterator;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 */
public class IntegerIncreasingSequencePeekingIterator implements IIncreasingSequenceIterator<Integer> {

    private int first;
    private int last;
    private int maxStep;
    private int cursor;
    private int result;
    private boolean isFirst;

    /**
     * minStep = 1
     * <p>
     * first must be less or equal last
     * <p>
     * maxStep must be positive
     *
     * @param first   — min value in iterator
     * @param last    - max value in iterator included
     * @param maxStep — max diff between adjacent values
     * @throws IllegalArgumentException if arguments is invalid
     */
    public IntegerIncreasingSequencePeekingIterator(int first, int last, int maxStep) {
        if (first > last || maxStep < 1) {
            throw new IllegalArgumentException();
        }
        this.first = first;
        this.last = last;
        this.maxStep = maxStep;
        isFirst = true;
        cursor = first;
    }


    /**
     * Returns {@code true} if the iteration has more elements.
     * <p>
     * In other words, returns {@code false} if lastNextElement + minStep > last.
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return isFirst || result < last;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        result = cursor;
        int step = -1;
        while (step < 0) {
            step = 1 + new Random().nextInt(maxStep);
        }
        long next = (long) cursor + step;

        cursor = next > Integer.MAX_VALUE || next > last ? last : cursor + step;
        isFirst = false;
        return result;
    }

    /**
     * Retrieves the next element in the iteration, but does not iterate.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer peek() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return isFirst ? first : cursor;
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
        if (hasNext() && other.hasNext()) {
            return peek() - other.peek();
        }
        if (hasNext()) {
            return 1;
        } else {
            return -1;
        }
    }
}

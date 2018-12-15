package ru.mail.polis.collections.iterator.todo;

import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.IPeekingIterator;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 */
public class IntegerIncreasingSequencePeekingIterator implements IIncreasingSequenceIterator<Integer> {

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

    protected static final int MIN_STEP = 1;
    int first;
    int last;
    int maxStep;
    int step = 0;
    private int current;
    private Random random = new Random();

    public IntegerIncreasingSequencePeekingIterator(int first, int last, int maxStep) {
        if (last < first || maxStep < 1){
            throw new IllegalArgumentException();
        }
        this.current = this.first = first;
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
        if (last - (long) current < MIN_STEP)
            return step == 0;
        else
            return true;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        if (!hasNext())
            throw new NoSuchElementException();
        if (step == 0)
            step = random.nextInt(maxStep) + MIN_STEP;
        else if (last - (long) current < step)
            current = last;
        else {
            current += step;
            step = random.nextInt(maxStep) + MIN_STEP;
        }
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

        if (step == 0)
            return current;
        else if (hasNext()) {
            if (last - (long) current < step)
                return last;
            else
                return current + step;
        } else
            throw new NoSuchElementException();

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
        if (other.hasNext() && hasNext())
            return Integer.compare(peek(), other.peek());
        if (other.hasNext())
            return -1;
        if (hasNext())
            return 1;
        return Integer.compare(peek(), other.peek());
    }
}

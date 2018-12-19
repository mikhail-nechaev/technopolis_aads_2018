package ru.mail.polis.collections.iterator.todo;

import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.IPeekingIterator;

import java.util.NoSuchElementException;

/**
 *
 */
public class IntegerIncreasingSequencePeekingIterator implements IIncreasingSequenceIterator<Integer> {

    int current;
    boolean wasLast, wasOverflow;
    int first, last, maxStep;

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

    public IntegerIncreasingSequencePeekingIterator(int first, int last, int maxStep)
    {
        if ((first <= last) && (maxStep > 0))
        {
            this.first = first;
            this.last = last;
            this.maxStep = maxStep;
            this.current = first;
            wasLast = false;
            wasOverflow = false;
        }
        else
        {
            throw new IllegalArgumentException();
        }
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
        if ((current >= last) && (wasLast))
        {
            return false;
        }
        else
        {
            if (wasOverflow && wasLast)
            {
                return false;
            }
            return true;
        }
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        if (current >= last)
        {
            if (!wasLast)
            {
                wasLast = true;
                return last;
            }
            else
            {
                throw new NoSuchElementException();
            }
        }
        else
        {
            if (wasOverflow)
            {
                if (!wasLast)
                {
                    wasLast = true;
                    return last;
                }
                else
                {
                    throw new NoSuchElementException();
                }
            }
            int prev = current;
            current = current + (int) (Math.random() * maxStep);
            if (current == prev)
            {
                ++current;
            }
            if (current < prev)
            {
                wasOverflow = true;
            }
            return prev;
        }
    }

    /**
     * Retrieves the next element in the iteration, but does not iterate.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer peek() {
        if (current >= last)
        {
            if (!wasLast)
            {
                return last;
            }
            else
            {
                throw new NoSuchElementException();
            }
        }
        else
        {
            if (wasOverflow && !wasLast)
            {
                return last;
            }
            if (wasOverflow && wasLast)
            {
                throw new NoSuchElementException();
            }
            return current;
        }
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
        if (this.hasNext() && other.hasNext())
        {
            return this.peek() - other.peek();
        }
        else
        {
            if (this.hasNext())
            {
                return 1;
            }
            if (other.hasNext())
            {
                return -1;
            }
            return 0;
        }
    }
}

package ru.mail.polis.collections.iterator.todo;

import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.IPeekingIterator;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 */
public class IntegerIncreasingSequencePeekingIterator implements IIncreasingSequenceIterator<Integer> {
    private int nextEl;
    private int prevEl;
    private final int minStep = 1;
    private final int maxStep;
    private final int last;
    private final Random random;

    /**
     * minStep = 1
     *
     * first must be less or equal last
     *
     * maxStep must be positive
     *
     * @param first — min value in iterator
     * @param last - max value in iterator included
     * @param maxStep — max diff between adjacent values
     * @throws IllegalArgumentException if arguments is invalid
     */
    public IntegerIncreasingSequencePeekingIterator(int first, int last, int maxStep) {
        if(first > last || maxStep <= 0){
            throw new IllegalArgumentException();
        }

        if(first == Integer.MIN_VALUE)
            this.prevEl = first;
        else
            this.prevEl = first-1;
        this.nextEl = first;
        this.last = last;
        this.maxStep = maxStep;
        this.random = new Random();
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     *
     * In other words, returns {@code false} if prevNextElement + minStep > last.
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        if(prevEl + minStep > last || prevEl == Integer.MAX_VALUE)
            return false;
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
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        prevEl = nextEl;
        long l = random.nextInt(maxStep) + minStep;
        long nextLong = nextEl + l;
        if(nextLong > Integer.MAX_VALUE || nextLong > last) {
            nextEl = last;
        }
        else {
            nextEl = (int) nextLong;
        }
        return prevEl;
    }

    /**
     * Retrieves the next element in the iteration, but does not iterate.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer peek() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        return nextEl;
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
        if(!hasNext()){
            return -1;
        }
        else if(!other.hasNext()) {
            return 1;
        }
        if (this.peek() < other.peek())
            return -1;
        else if (this.peek() > other.peek())
            return 1;
        return 0;
    }
}
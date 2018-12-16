package ru.mail.polis.collections.iterator.todo;

import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.IPeekingIterator;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 */
public class IntegerIncreasingSequencePeekingIterator implements IIncreasingSequenceIterator<Integer> {

//    private int first;
    private int last;
    private int maxStep;
    private Integer element;
    private Random random;
    private boolean isFirst;
    private boolean isLast;

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
        if (maxStep <= 0 | last < first) {
            throw new IllegalArgumentException();
        }
//        this.first = first;
        this.last = last;
        this.maxStep = maxStep;
        element = first;
        isFirst = true;
        random = new Random();
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
        boolean b = !((element + 1 > last) || element == Integer.MAX_VALUE);
        return isFirst || b || isLast;
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
        if (isLast) {
            isLast = false;
            return last;
        } else {
            isFirst = false;
            Integer i = element;
            maxStep = element < 0 || last - element >= maxStep ? maxStep : last - element;
            element += random.nextInt(maxStep <= 1 ? 1 : maxStep - 1) + 1;
            //System.out.println("el = " + i + " nel = " + element + " mstep = " + maxStep);
            isLast = element == last;
            return i;
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
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return element;
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
        return element.compareTo(other.peek());
    }
}

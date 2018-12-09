package ru.mail.polis.collections.iterator.todo;

import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.IPeekingIterator;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 */
public class IntegerIncreasingSequencePeekingIterator implements IIncreasingSequenceIterator<Integer> {

    private Integer current;
    private int last;
    private int maxStep;
    private Random rnd;
    private boolean hasElements = true;
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
        if(first > last || maxStep <= 0){
            throw new IllegalArgumentException();
        }
        current = first;
        this.last = last;
        this.maxStep = maxStep;
        rnd = new Random();
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
        return hasElements;
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
       Integer value = current;
       if(value == last){
           hasElements = false;
       } else {
           Integer step = rnd.nextInt(maxStep) + 1;
           current = ((long) value + step >= last) ? last : value + step;
       }
       return value;
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
        return current;
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
        if(hasNext() && other.hasNext()){
            return peek() - other.peek();
        }
        if(hasNext() || other.hasNext()){
            if(hasNext()){
                return 1;
            }
            return -1;
        }
        return 0;
    }
}

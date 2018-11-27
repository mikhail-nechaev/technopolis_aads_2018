package ru.mail.polis.collections.iterator.todo;

import java.util.Iterator;

/**
 * Итератор возвращающий последовательность последовательностей элементов возрастающих итераторов в порядке возрастания
 * <p>
 * first = 1,3,4,5,7
 * second = 0,2,4,6,8
 * result = 0,1,2,3,4,4,5,6,7,8
 * <p>
 * Суммарное время работы = O(n + k * log n),
 * n — количество итераторов
 * k — суммарное количество элементов
 */
public class MergingPeekingIncreasingIterator implements Iterator<Integer> {

    /**
     * Creates a {@code MergingPeekingIncreasingIterator} containing the inside all elements of this specified iterators.
     *
     * Complexity = O(n)
     *
     * @param iterators the iterators whose are to be placed into this merging peeking increasing iterator
     */
    public MergingPeekingIncreasingIterator(IntegerIncreasingSequencePeekingIterator... iterators) {
        //todo: do some stuff with iterators
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * Complexity = O(1)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Returns the next element in the iteration.
     *
     * Complexity = O(log(n))
     *
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        throw new UnsupportedOperationException("todo: implement this");
    }
}

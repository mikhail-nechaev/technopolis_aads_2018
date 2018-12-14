package ru.mail.polis.collections.iterator.todo;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import ru.mail.polis.collections.iterator.IPeekingIterator;
import ru.mail.polis.collections.list.todo.ArrayPriorityQueueSimple;

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

    PriorityQueue<IntegerIncreasingSequencePeekingIterator> priorityQueue;

    /**
     * Creates a {@code MergingPeekingIncreasingIterator} containing the inside all elements of this specified iterators.
     * <p>
     * Complexity = O(n)
     *
     * @param iterators the iterators whose are to be placed into this merging peeking increasing iterator
     */
    public MergingPeekingIncreasingIterator(IntegerIncreasingSequencePeekingIterator... iterators) {
        priorityQueue = new PriorityQueue<>(IntegerIncreasingSequencePeekingIterator::compareTo);
        for (IntegerIncreasingSequencePeekingIterator iterator : iterators) {
            priorityQueue.add(iterator);
        }
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     * <p>
     * Complexity = O(1)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        try {
            return priorityQueue.element().hasNext();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the next element in the iteration.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        IntegerIncreasingSequencePeekingIterator iterator = priorityQueue.remove();
        Integer i = iterator.next();
        if (iterator.hasNext()) {
            priorityQueue.add(iterator);
        }
        System.out.println(i);
        return i;
    }
}

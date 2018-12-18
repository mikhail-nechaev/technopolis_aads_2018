package ru.mail.polis.collections.iterator.todo;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
 * n — количество итераторов
 * k — суммарное количество элементов
 */
public class MergingPeekingIncreasingIterator implements Iterator<Integer> {

    private ArrayPriorityQueueSimple<IPeekingIterator<Integer>> queue;

    /**
     * Creates a {@code MergingPeekingIncreasingIterator} containing the inside all elements of this specified iterators.
     * <p>
     * Complexity = O(n)
     *
     * @param iterators the iterators whose are to be placed into this merging peeking increasing iterator
     */
    public MergingPeekingIncreasingIterator(IntegerIncreasingSequencePeekingIterator... iterators) {
        queue = new ArrayPriorityQueueSimple<>();
        for (IntegerIncreasingSequencePeekingIterator iterator : iterators) {
            queue.add(iterator);
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
        return queue.size() > 0;
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
        IPeekingIterator<Integer> integerIPeekingIterator = queue.remove();
        Integer result = integerIPeekingIterator.next();
        if (integerIPeekingIterator.hasNext()) {
            queue.add(integerIPeekingIterator);
        }
        return result;
    }
}

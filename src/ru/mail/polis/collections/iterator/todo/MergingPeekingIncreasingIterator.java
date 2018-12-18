package ru.mail.polis.collections.iterator.todo;

import java.util.Arrays;
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
 * n — количество итераторов
 * k — суммарное количество элементов
 */
public class MergingPeekingIncreasingIterator implements Iterator<Integer> {
    ArrayPriorityQueueSimple<IPeekingIterator<Integer>> arrayPriorityQueueSimple;

    public MergingPeekingIncreasingIterator(IntegerIncreasingSequencePeekingIterator... iterators) {
        arrayPriorityQueueSimple = new ArrayPriorityQueueSimple<>(Arrays.asList(iterators));
    }


    @Override
    public boolean hasNext() {
        return arrayPriorityQueueSimple.size() > 0;
    }


    @Override
    public Integer next() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        IntegerIncreasingSequencePeekingIterator iterator = (IntegerIncreasingSequencePeekingIterator) arrayPriorityQueueSimple.remove();
        Integer value = iterator.next();
        if (iterator.hasNext()) {
            arrayPriorityQueueSimple.add(iterator);
        }
        return value;
    }
}

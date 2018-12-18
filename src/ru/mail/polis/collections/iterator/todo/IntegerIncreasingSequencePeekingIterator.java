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
    private int index;
    private int lastReterned;
    private int count;



    public IntegerIncreasingSequencePeekingIterator(int first, int last, int maxStep)  {
        if(maxStep < 1 || first > last){
            throw new IllegalArgumentException();
        }
        this.first = first;
        this.last = last;
        this.maxStep = maxStep;
        this.index = this.first;
        this.count = 0;
    }


    @Override
    public boolean hasNext() {
        return count == 0 || lastReterned < last;
    }


    @Override
    public Integer next() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        lastReterned = index;
        long step =(long) new Random().nextInt(maxStep) + 1L;
        while (step > Integer.MAX_VALUE){
            step =(long) new Random().nextInt(maxStep) + 1L;
        }

        if((long)index + step > last){
            index = last;
        }
        else {
            index += step;
        }
        count ++;
        return lastReterned;
    }


    @Override
    public Integer peek() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        return index;
    }

    @Override
    public int compareTo(IPeekingIterator<Integer> other) {
        if(this.hasNext() && other.hasNext()){
            return Integer.compare(peek(), other.peek());
        }
        if(other.hasNext()){
            return -1;
        }
        return 1;
    }
}

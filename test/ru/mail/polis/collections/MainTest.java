package ru.mail.polis.collections;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.mail.polis.collections.iterator.TestIntegerIncreasingSequencePeekingIterator;
import ru.mail.polis.collections.iterator.TestMergingPeekingIncreasingIterator;
import ru.mail.polis.collections.list.TestFullDeque;
import ru.mail.polis.collections.list.TestIDeque;
import ru.mail.polis.collections.list.TestIDequeRemove;
import ru.mail.polis.collections.list.TestIPriorityQueue;
//import ru.mail.polis.collections.set.hash.TestHashTable;
import ru.mail.polis.collections.set.sorted.TestBalancedSortedSet;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestIDeque.class,
        TestIDequeRemove.class,
        TestFullDeque.class,
        TestIPriorityQueue.class,
        TestIntegerIncreasingSequencePeekingIterator.class,
        TestMergingPeekingIncreasingIterator.class,
       // TestHashTable.class,
        TestBalancedSortedSet.class
})
public class MainTest {
    //Run this from IntelliJ IDEA
}

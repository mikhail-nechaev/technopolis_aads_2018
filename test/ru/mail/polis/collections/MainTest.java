package ru.mail.polis.collections;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.mail.polis.collections.iterator.TestIntegerIncreasingSequencePeekingIterator;
import ru.mail.polis.collections.iterator.TestMergingPeekingIncreasingIterator;
import ru.mail.polis.collections.list.TestFullDeque;
import ru.mail.polis.collections.list.TestIDeque;
import ru.mail.polis.collections.list.TestIDequeRemove;
import ru.mail.polis.collections.list.TestIPriorityQueueBase;
import ru.mail.polis.collections.list.TestIPriorityQueueComp;
import ru.mail.polis.collections.list.TestIPriorityQueueRemove;
import ru.mail.polis.collections.set.hash.TestHashTable;
import ru.mail.polis.collections.set.hash.TestHastTableIteratorRemove;
import ru.mail.polis.collections.set.sorted.TestAVLTreeIterable;
import ru.mail.polis.collections.set.sorted.TestAVLTreeIterableRemove;
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
        TestIPriorityQueueBase.class,
        TestIPriorityQueueComp.class,
        TestIPriorityQueueRemove.class,
        TestIntegerIncreasingSequencePeekingIterator.class,
        TestMergingPeekingIncreasingIterator.class,
        TestHashTable.class,
        TestHastTableIteratorRemove.class,
        TestBalancedSortedSet.class,
        TestAVLTreeIterable.class,
        TestAVLTreeIterableRemove.class
})
public class MainTest {
    //Run this from IntelliJ IDEA
}

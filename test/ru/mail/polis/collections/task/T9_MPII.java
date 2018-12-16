package ru.mail.polis.collections.task;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.mail.polis.collections.iterator.TestIntegerIncreasingSequencePeekingIterator;
import ru.mail.polis.collections.iterator.TestMergingPeekingIncreasingIterator;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestIntegerIncreasingSequencePeekingIterator.class,
        TestMergingPeekingIncreasingIterator.class,
})
public class T9_MPII {
}

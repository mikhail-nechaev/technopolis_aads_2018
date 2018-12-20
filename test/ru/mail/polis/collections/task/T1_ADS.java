package ru.mail.polis.collections.task;

import org.junit.Before;
import ru.mail.polis.collections.list.AbstractIDequeTest;
import ru.mail.polis.collections.list.IDeque;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
public class T1_ADS extends AbstractIDequeTest {

    private IDeque<String> deque;

    @Before
    public void init() {
        deque = new ArrayDequeSimple<>();
    }

    public IDeque<String> get() {
        return deque;
    }
}

package ru.mail.polis.collections.task;

import org.junit.Before;
import ru.mail.polis.collections.list.AbstractIDequeRemoveTest;
import ru.mail.polis.collections.list.IDeque;
import ru.mail.polis.collections.list.todo.LinkedDequeSimple;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
public class T5_LDSIR extends AbstractIDequeRemoveTest {

    private IDeque<String> deque;

    @Before
    public void init() {
        deque = new LinkedDequeSimple<>();
    }

    public IDeque<String> get() {
        return deque;
    }
}

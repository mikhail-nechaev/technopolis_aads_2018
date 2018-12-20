package ru.mail.polis.collections.task;

import org.junit.Before;
import ru.mail.polis.collections.list.AbstractTestFullDeque;
import ru.mail.polis.collections.list.todo.LinkedDequeFull;

import java.util.Deque;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
public class T6_LDF extends AbstractTestFullDeque {

    private Deque<String> deque;

    @Before
    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        deque = (Deque<String>) LinkedDequeFull.class.getConstructor().newInstance();
    }

    @Override
    protected Deque<String> get() {
        return deque;
    }
}


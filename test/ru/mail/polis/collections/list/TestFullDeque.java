package ru.mail.polis.collections.list;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.list.todo.ArrayDequeFull;
import ru.mail.polis.collections.list.todo.LinkedDequeFull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */

/**
 * Makes sense to make sure that the TestIDeque for Full* classes passes.
 */
@SuppressWarnings("unchecked")
@RunWith(value = Parameterized.class)
public class TestFullDeque extends AbstractTestFullDeque {

    private Deque<String> deque;

    @Parameterized.Parameter()
    public Class<?> testClass;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Class<?>> data() {
        return Arrays.asList(
                ArrayDequeFull.class,
                LinkedDequeFull.class
        );
    }

    @Before
    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        deque = (Deque<String>) testClass.getConstructor().newInstance();
    }

    @Override
    protected Deque<String> get() {
        return deque;
    }
}

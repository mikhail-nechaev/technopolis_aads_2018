package ru.mail.polis.collections.list;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;
import ru.mail.polis.collections.list.todo.LinkedDequeSimple;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class TestIDequeRemove extends AbstractIDequeRemoveTest {

    @Parameterized.Parameter()
    public Class<?> testClass;
    private IDeque<String> deque;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Class<?>> data() {
        return Arrays.asList(
                ArrayDequeSimple.class,
                LinkedDequeSimple.class
        );
    }

    @Before //Run before every test
    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        deque = (IDeque<String>) testClass.getConstructor().newInstance();
    }

    public IDeque<String> get() {
        return deque;
    }

}

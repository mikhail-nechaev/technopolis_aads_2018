package ru.mail.polis.collections.list;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.list.todo.ArrayDequeFull;
import ru.mail.polis.collections.list.todo.ArrayDequeSimple;
import ru.mail.polis.collections.list.todo.LinkedDequeFull;
import ru.mail.polis.collections.list.todo.LinkedDequeSimple;

import java.util.Arrays;
import java.util.Collection;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */

/**
 * Класс тестирующий интерфейс {@link IDeque<Integer>} в четырёх реализациях:
 * 1) {@link ArrayDequeSimple<Integer>}
 * 2) {@link LinkedDequeSimple<Integer>}
 * 3) {@link ArrayDequeFull<Integer>}
 * 4) {@link LinkedDequeFull<Integer>}
 */
@RunWith(value = Parameterized.class)
public class TestIDeque extends AbstractIDequeTest {

    @Parameterized.Parameter()
    public Class<?> testClass;
    private IDeque<String> deque;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Class<?>> data() {
        return Arrays.asList(
                ArrayDequeSimple.class
                ,
                LinkedDequeSimple.class
                ,
                ArrayDequeFull.class
                ,
                LinkedDequeFull.class
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

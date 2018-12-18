package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mail.polis.collections.list.todo.ArrayDequeFull;
import ru.mail.polis.collections.list.todo.LinkedDequeFull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * Created by Nechaev Mikhail
 * Since 30/11/2018.
 */

/**
 * Makes sense to make sure that the TestIDeque for Full* classes passes.
 */
@RunWith(value = Parameterized.class)
public class TestFullDeque {

    private final static Object STUB = new Object();
    private List<Function<Deque<String>, String>> getWithoutRemove = Arrays.asList(
            Deque::getFirst,
            Deque::getLast,
            Deque::peekLast,
            Deque::peekFirst,
            Deque::element,
            Deque::peek
    );
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

    @Test
    public void testNull() {
        //deque
        assertException("1", (d) -> d.addFirst(null), NullPointerException.class);
        assertException("2", (d) -> d.addLast(null), NullPointerException.class);
        assertException("3", (d) -> d.offerFirst(null), NullPointerException.class);
        assertException("4", (d) -> d.offerLast(null), NullPointerException.class);
        assertException("5", (d) -> d.removeFirstOccurrence(null), NullPointerException.class);
        assertException("6", (d) -> d.removeLastOccurrence(null), NullPointerException.class);
        //queue
        assertException("7", (d) -> d.add(null), NullPointerException.class);
        assertException("8", (d) -> d.offer(null), NullPointerException.class);
        assertException("9", (d) -> d.addAll(null), NullPointerException.class);
        //stack
        assertException("9", (d) -> d.push(null), NullPointerException.class);
        //collection
        assertException("10", (d) -> d.remove(null), NullPointerException.class);
        assertException("11", (d) -> d.contains(null), NullPointerException.class);
    }

    @Test
    public void empty() {
        checkEmpty();
    }

    @Test
    public void emptyClear() {
        deque.clear();
        checkEmpty();
    }

    @Test
    public void emptyIterator() {
        Assert.assertFalse("1", deque.iterator().hasNext());
        Assert.assertFalse("2", deque.descendingIterator().hasNext());
    }

    private void checkEmpty() {
        //deque
        assertException("1", Deque::removeFirst, NoSuchElementException.class);
        assertException("2", Deque::removeLast, NoSuchElementException.class);
        Assert.assertNull("3", deque.pollFirst());
        Assert.assertNull("4", deque.pollLast());
        assertException("5", Deque::getFirst, NoSuchElementException.class);
        assertException("6", Deque::getLast, NoSuchElementException.class);
        Assert.assertNull("7", deque.peekFirst());
        Assert.assertNull("8", deque.peekLast());
        Assert.assertFalse("9", deque.removeFirstOccurrence(STUB));
        Assert.assertFalse("10", deque.removeLastOccurrence(STUB));
        //queue
        assertException("11", Deque::remove, NoSuchElementException.class);
        Assert.assertNull("12", deque.poll());
        assertException("13", Deque::element, NoSuchElementException.class);
        Assert.assertNull("14", deque.peek());
        //stack
        assertException("15", Deque::pop, NoSuchElementException.class);
        //collection
        Assert.assertFalse("16", deque.contains(STUB));
        Assert.assertEquals("17", 0, deque.size());
    }

    @Test
    public void add() {
        Assert.assertTrue("1", deque.add("A"));
        Assert.assertTrue("1", deque.contains("A"));
        Assert.assertTrue("1", deque.remove("A"));
        checkEmpty();
    }

    @Test
    public void addAndRemove() {
        checkAdd(this::checkAddContainsRemove);
    }

    @Test
    public void addAndClear() {
        checkAdd(this::checkAddContainsClear);
    }

    @Test
    public void some1() {
        deque.addFirst("A");
        Assert.assertEquals("A", deque.getFirst());
        Assert.assertEquals("A", deque.getLast());
        deque.addLast("B");
        Assert.assertEquals("A", deque.getFirst());
        Assert.assertEquals("B", deque.getLast());
        deque.addFirst("C");
        Assert.assertEquals("C", deque.getFirst());
        Assert.assertEquals("B", deque.getLast());
        Assert.assertEquals("B", deque.removeLast());
        Assert.assertEquals("A", deque.getLast());
    }

    @Test
    public void some2() {
        deque.addFirst("A");
        deque.addFirst("B");
        deque.addFirst("C");
        Assert.assertEquals("A", deque.removeLast());
        Assert.assertEquals("B", deque.removeLast());
        Assert.assertEquals("C", deque.removeLast());
        deque.addLast("A");
        deque.addLast("B");
        deque.addLast("C");
        Assert.assertEquals("A", deque.removeFirst());
        Assert.assertEquals("B", deque.removeFirst());
        Assert.assertEquals("C", deque.removeFirst());
        checkEmpty();
    }

    @Test
    public void some3() {
        deque.addFirst("A");
        Assert.assertTrue(deque.size() == 1);
        deque.addLast("B");
        Assert.assertTrue(deque.size() == 2);
        deque.addFirst("C");
        Assert.assertTrue(deque.size() == 3);
        deque.addLast("D");
        Assert.assertTrue(deque.size() == 4);
        Assert.assertEquals("D", deque.removeLast());
        Assert.assertTrue(deque.size() == 3);
        Assert.assertEquals("C", deque.removeFirst());
        Assert.assertTrue(deque.size() == 2);
        Assert.assertEquals("B", deque.removeLast());
        Assert.assertTrue(deque.size() == 1);
        Assert.assertEquals("A", deque.removeFirst());
        checkEmpty();
    }

    @Test
    public void grow() {
        for (int i = 0; i < 100000; i++) {
            deque.addFirst("A");
        }
        Assert.assertTrue(100000 == deque.size());
        deque.clear();
        checkEmpty();
    }

    private void checkAdd(Consumer<Consumer<Deque<String>>> check) {
        check.accept((deque) -> deque.addFirst("A"));
        check.accept((deque) -> deque.addLast("A"));
        check.accept((deque) -> Assert.assertTrue(deque.offerFirst("A")));
        check.accept((deque) -> Assert.assertTrue(deque.offerLast("A")));
        check.accept((deque) -> Assert.assertTrue(deque.add("A")));
        check.accept((deque) -> Assert.assertTrue(deque.offer("A")));
        check.accept((deque) -> Assert.assertTrue(deque.addAll(Collections.singleton("A"))));
        check.accept((deque) -> deque.push("A"));
    }

    private void checkAddContainsRemove(Consumer<Deque<String>> add) {
        checkAddRemove(add, (deque) -> Assert.assertEquals("1", "A", deque.removeFirst()));
        checkAddRemove(add, (deque) -> Assert.assertEquals("4", "A", deque.removeLast()));
        checkAddRemove(add, (deque) -> Assert.assertEquals("6", "A", deque.pollFirst()));
        checkAddRemove(add, (deque) -> Assert.assertEquals("8", "A", deque.pollLast()));
        checkAddRemove(add, (deque) -> Assert.assertTrue("10", deque.removeFirstOccurrence("A")));
        checkAddRemove(add, (deque) -> Assert.assertTrue("12", deque.removeLastOccurrence("A")));
        checkAddRemove(add, (deque) -> Assert.assertEquals("14", "A", deque.poll()));
        checkAddRemove(add, (deque) -> Assert.assertEquals("16", "A", deque.pop()));
    }

    private void checkAddRemove(Consumer<Deque<String>> add, Consumer<Deque<String>> remove) {
        add.accept(deque);
        Assert.assertTrue(deque.contains("A"));
        remove.accept(deque);
        checkEmpty();
    }

    private void checkAddContainsClear(Consumer<Deque<String>> add) {
        for (Function<Deque<String>, String> gwr : getWithoutRemove) {
            add.accept(deque);
            checkOneA(gwr);
            deque.clear();
            checkEmpty();
        }
    }

    private void checkOneA(Function<Deque<String>, String> get) {
        Assert.assertEquals("1", "A", get.apply(deque));
        Assert.assertTrue("2", deque.contains("A"));
        Assert.assertTrue("3", deque.size() == 1);
        Iterator<String> iterator = deque.iterator();
        Assert.assertTrue("4", iterator.hasNext());
        Assert.assertEquals("5", "A", iterator.next());
        Assert.assertFalse("6", iterator.hasNext());
        iterator = deque.descendingIterator();
        Assert.assertTrue("7", iterator.hasNext());
        Assert.assertEquals("8", "A", iterator.next());
        Assert.assertFalse("9", iterator.hasNext());
    }

    private void assertException(String step,
                                 Consumer<Deque<String>> consumer,
                                 Class<?> exClass) {
        try {
            consumer.accept(deque);
            Assert.fail(step);
        } catch (Exception e) {
            if (!exClass.isInstance(e)) {
                Assert.fail(step);
            }
        }
    }

}

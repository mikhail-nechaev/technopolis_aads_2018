package ru.mail.polis.collections.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
public abstract class AbstractTestFullDeque {

    private final static Object STUB = new Object();
    private List<Function<Deque<String>, String>> getWithoutRemove = Arrays.asList(
            Deque::getFirst,
            Deque::getLast,
            Deque::peekLast,
            Deque::peekFirst,
            Deque::element,
            Deque::peek
    );

    protected abstract Deque<String> get();

    @Test
    public void testNull() {
        Deque<String> deque = get();
        //deque
        assertException(deque, "1", (d) -> d.addFirst(null), NullPointerException.class);
        assertException(deque, "2", (d) -> d.addLast(null), NullPointerException.class);
        assertException(deque, "3", (d) -> d.offerFirst(null), NullPointerException.class);
        assertException(deque, "4", (d) -> d.offerLast(null), NullPointerException.class);
        assertException(deque, "5", (d) -> d.removeFirstOccurrence(null), NullPointerException.class);
        assertException(deque, "6", (d) -> d.removeLastOccurrence(null), NullPointerException.class);
        //queue
        assertException(deque, "7", (d) -> d.add(null), NullPointerException.class);
        assertException(deque, "8", (d) -> d.offer(null), NullPointerException.class);
        assertException(deque, "9", (d) -> d.addAll(null), NullPointerException.class);
        //stack
        assertException(deque, "9", (d) -> d.push(null), NullPointerException.class);
        //collection
        assertException(deque, "10", (d) -> d.remove(null), NullPointerException.class);
        assertException(deque, "11", (d) -> d.contains(null), NullPointerException.class);
    }

    @Test
    public void empty() {
        checkEmpty();
    }

    @Test
    public void emptyClear() {
        get().clear();
        checkEmpty();
    }

    @Test
    public void emptyIterator() {
        Deque<String> deque = get();
        Assert.assertFalse("1", deque.iterator().hasNext());
        Assert.assertFalse("2", deque.descendingIterator().hasNext());
    }

    private void checkEmpty() {
        Deque<String> deque = get();
        //deque
        assertException(deque, "1", Deque::removeFirst, NoSuchElementException.class);
        assertException(deque, "2", Deque::removeLast, NoSuchElementException.class);
        Assert.assertNull("3", deque.pollFirst());
        Assert.assertNull("4", deque.pollLast());
        assertException(deque, "5", Deque::getFirst, NoSuchElementException.class);
        assertException(deque, "6", Deque::getLast, NoSuchElementException.class);
        Assert.assertNull("7", deque.peekFirst());
        Assert.assertNull("8", deque.peekLast());
        Assert.assertFalse("9", deque.removeFirstOccurrence(STUB));
        Assert.assertFalse("10", deque.removeLastOccurrence(STUB));
        //queue
        assertException(deque, "11", Deque::remove, NoSuchElementException.class);
        Assert.assertNull("12", deque.poll());
        assertException(deque, "13", Deque::element, NoSuchElementException.class);
        Assert.assertNull("14", deque.peek());
        //stack
        assertException(deque, "15", Deque::pop, NoSuchElementException.class);
        //collection
        Assert.assertFalse("16", deque.contains(STUB));
        Assert.assertEquals("17", 0, deque.size());
    }

    @Test
    public void add() {
        Deque<String> deque = get();
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
        Deque<String> deque = get();
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
        Deque<String> deque = get();
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
        Deque<String> deque = get();
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
        Deque<String> deque = get();
        for (int i = 0; i < 100000; i++) {
            deque.addFirst("A");
        }
        Assert.assertTrue(100000 == deque.size());
        deque.clear();
        checkEmpty();
    }

    private void checkAdd(Consumer<Consumer<Deque<String>>> check) {
        Deque<String> deque = get();
        check.accept((dq) -> deque.addFirst("A"));
        check.accept((dq) -> deque.addLast("A"));
        check.accept((dq) -> Assert.assertTrue(deque.offerFirst("A")));
        check.accept((dq) -> Assert.assertTrue(deque.offerLast("A")));
        check.accept((dq) -> Assert.assertTrue(deque.add("A")));
        check.accept((dq) -> Assert.assertTrue(deque.offer("A")));
        check.accept((dq) -> Assert.assertTrue(deque.addAll(Collections.singleton("A"))));
        check.accept((dq) -> deque.push("A"));
    }

    private void checkAddContainsRemove(Consumer<Deque<String>> add) {
        Deque<String> deque = get();
        checkAddRemove(add, (dq) -> Assert.assertEquals("1", "A", deque.removeFirst()));
        checkAddRemove(add, (dq) -> Assert.assertEquals("4", "A", deque.removeLast()));
        checkAddRemove(add, (dq) -> Assert.assertEquals("6", "A", deque.pollFirst()));
        checkAddRemove(add, (dq) -> Assert.assertEquals("8", "A", deque.pollLast()));
        checkAddRemove(add, (dq) -> Assert.assertTrue("10", deque.removeFirstOccurrence("A")));
        checkAddRemove(add, (dq) -> Assert.assertTrue("12", deque.removeLastOccurrence("A")));
        checkAddRemove(add, (dq) -> Assert.assertEquals("14", "A", deque.poll()));
        checkAddRemove(add, (dq) -> Assert.assertEquals("16", "A", deque.pop()));
    }

    private void checkAddRemove(Consumer<Deque<String>> add, Consumer<Deque<String>> remove) {
        Deque<String> deque = get();
        add.accept(deque);
        Assert.assertTrue(deque.contains("A"));
        remove.accept(deque);
        checkEmpty();
    }

    private void checkAddContainsClear(Consumer<Deque<String>> add) {
        Deque<String> deque = get();
        for (Function<Deque<String>, String> gwr : getWithoutRemove) {
            add.accept(deque);
            checkOneA(deque, gwr);
            deque.clear();
            checkEmpty();
        }
    }

    private void checkOneA(Deque<String> deque, Function<Deque<String>, String> get) {
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

    private void assertException(Deque<String> deque, String step,
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

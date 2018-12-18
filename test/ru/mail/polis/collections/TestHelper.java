package ru.mail.polis.collections;

import ru.mail.polis.collections.list.IDeque;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;

import static com.sun.tools.javac.util.List.of;

/*
 * Created by Nechaev Mikhail
 * Since 07/12/2018.
 */
public class TestHelper {

    public static final Collection<Object[]> COMPARATORS = of(new Object[][]{
            {Comparator.naturalOrder(), "NATURAL"},
            {Comparator.reverseOrder(), "REVERSE"},
            {Comparator.comparingInt((Integer v) -> v % 2).thenComparingInt(v -> v), "EVEN_FIRST"},
            {(Comparator) (v1, v2) -> 0, "ALL_EQUALS"},
    });

    public static class CorrectIDeque<E> extends ArrayDeque<E> implements IDeque<E> {

    }
}

package ru.mail.polis.collections;

import ru.mail.polis.collections.list.IDeque;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/*
 * Created by Nechaev Mikhail
 * Since 07/12/2018.
 */
public class TestHelper {

    public static final Object[][] kek = new Object[][]{
            {Comparator.naturalOrder(), "NATURAL"},
            {Comparator.reverseOrder(), "REVERSE"},
            {Comparator.comparingInt((Integer v) -> v % 2).thenComparingInt(v -> v), "EVEN_FIRST"},
            {(Comparator) (v1, v2) -> 0, "ALL_EQUALS"},
    };
    public static final Collection<Object[]> COMPARATORS = Arrays.asList(kek);


    public static class CorrectIDeque<E> extends ArrayDeque<E> implements IDeque<E> {

    }
}

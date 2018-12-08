package ru.mail.polis.collections.list.todo;

import java.util.Deque;

public abstract class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E> {
    //todo: remove <abstract> modifier and implement

    @Override
    public boolean contains(Object o) {
        return false;
    }
}

package ru.mail.polis.collections.set.hash;

public class BrokenStudent extends AbstractOpenHashTableEntity {

    private int hashCode;

    public BrokenStudent(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    protected int hashCode2() {
        return hashCode;
    }

    @Override
    public int hashCode(int tableSize, int probId) throws IllegalArgumentException {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}

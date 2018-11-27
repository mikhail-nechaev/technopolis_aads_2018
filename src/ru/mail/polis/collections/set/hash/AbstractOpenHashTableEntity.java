package ru.mail.polis.collections.set.hash;

/*
 * Created by Nechaev Mikhail
 * Since 27/11/2018.
 */
public abstract class AbstractOpenHashTableEntity extends CheckedOpenHashTableEntity implements IOpenHashTableEntity {

    /**
     * Second hash-function in double hashing.
     * Must be independent from first hashCode {@link Object#hashCode()}.
     *
     * @return a second hash code value for this object.
     */
    protected abstract int hashCode2();
}

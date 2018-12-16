package ru.mail.polis.collections.set.hash.todo;

import ru.mail.polis.collections.set.hash.IOpenHashTable;
import ru.mail.polis.collections.set.hash.IOpenHashTableEntity;

import java.util.Iterator;

/**
 *
 * Implementation open addressed hash table with double hashing
 *
 * <a href="https://en.wikipedia.org/wiki/Double_hashing">Double_hashing</a>
 *
 * Use {@link IOpenHashTableEntity#hashCode(int,int)} for hash code calculating
 *
 * Use loadFactor = from 0.5f to 0.75f included
 *
 * @param <E> the type of elements maintained by this hash table
 */
@SuppressWarnings("unchecked")
public class OpenHashTable<E extends IOpenHashTableEntity>  implements IOpenHashTable<E> {

    private static final double LOAD_FACTOR = 0.5;

    private E [] arrayHash;
    private int length;

    public OpenHashTable(Class<IOpenHashTableEntity> iOpenHashTableEntityClass) {

    }


    private boolean isNil(E value){
        return value  == null;// || value == nil;
    }

    private Integer hashFunctionInteger(Integer key){
        return key % arrayHash.length;
    }

    private Integer hashFunctionInteger2(Integer key){
        return 1 - key % 7;
    }





    public OpenHashTable(){

    }

    private void doubleCapacity(){
        int newCapacity = arrayHash.length << 1;
        if(newCapacity < 0){
            throw new IllegalStateException("Sorry, size of Table too big");
        }
        E [] a = (E[]) new Object[newCapacity];
        System.arraycopy(arrayHash, 0, a, 0,arrayHash.length);
        arrayHash = a;
    }


    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *                                  In other words if {@link IOpenHashTableEntity#hashCode(int, int)} specified element is incorrect.
     */
    @Override
    public boolean add(E value){
        float occupancy = (float) length / (float) arrayHash.length;
        boolean isComplete = false;
        /*if(typeParameterClass.equals(Integer) || typeParameterClass.equals(Double) || typeParameterClass.equals(Float)) {
            for(int i = 0; i < arrayHash.length; i++) {
                int j = (hashFunctionInteger(value) + i * hashFunctionInteger2(value)) % arrayHash.length;
                if(isNil(arrayHash[j])){
                    if(occupancy >= LOAD_FACTOR){
                        doubleCapacity();
                    }
                    arrayHash[j] = value;
                    isComplete = true;
                    length ++;
                    break;
                }
            }
        }*/


        return isComplete;
    }

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *                                  In other words if {@link IOpenHashTableEntity#hashCode(int, int)} specified element is incorrect.
     */
    @Override
    public boolean remove(E value) {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents it from being added to this collection
     *                                  In other words if {@link IOpenHashTableEntity#hashCode(int, int)} specified element is incorrect.
     */
    @Override
    public boolean contains(Object value) {
       /* for(int i=0; i < arrayHash.length; i++){
            //выполняя i - пробу, мы считаем индекс в массиве
            int j = hashFunction(i);
            if(isNil(arrayHash[j])){ // если там null или специальное значение
                continue; // то продолжим
            }
            if(arrayHash[j] == value){
                return true;
            }
        }*/
        return false;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("todo: implement this");
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                return null;
            }
        };
    }

    @Override
    public int tableSize() {
        return arrayHash.length;
    }
}

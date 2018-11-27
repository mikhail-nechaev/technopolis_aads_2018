package ru.mail.polis.collections.set.hash;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */
public interface IOpenHashTableEntity {

    /**
     * Вычисляет значение хеша объекта (другими словами значение индекса ячейки в хеш-таблице)
     * на основании её размера и номера пробы,
     * где номер пробы равен попытке найти свободную ячейку.
     * <p>
     * Если методу поочередно дать на вход все значения probIdx, от 0 до tableSize - 1, при фиксированном tableSize,
     * то на выходе должна получиться последовательности из всех возможных индексов ячеек хеш-таблицы
     *
     * Хеш-функция должна
     *  - вычисляться на основе двух независимых хеш-функций
     *  - за tableSize раз выдавать все индексы
     *
     * <a href="https://neerc.ifmo.ru/wiki/index.php?title=Разрешение_коллизий#.D0.94.D0.B2.D0.BE.D0.B9.D0.BD.D0.BE.D0.B5_.D1.85.D0.B5.D1.88.D0.B8.D1.80.D0.BE.D0.B2.D0.B0.D0.BD.D0.B8.D0.B5">
     *     Разрешение коллизий — Двойное хеширование
     * </a>
     *
     * Например: hashCode = (hashCode1() + probId * hashCode2()) % tableSize
     * Также следует не забыть учесть целочисленное переполнение и отрицательные значения
     *
     * @param tableSize — текущий размер хеш-таблицы
     * @param probId    — номер пробы. Значение от 0 до tableSize - 1
     * @return значение вычисленного хеша в диапазоне [0..tableSize-1)
     *
     * @throws IllegalArgumentException если probIdx < 0 или probIdx >= tableSize
     */
    int hashCode(int tableSize, int probId) throws IllegalArgumentException;

}

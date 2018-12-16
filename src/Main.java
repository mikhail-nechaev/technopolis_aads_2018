import ru.mail.polis.collections.iterator.IIncreasingSequenceIterator;
import ru.mail.polis.collections.iterator.todo.IntegerIncreasingSequencePeekingIterator;
import ru.mail.polis.collections.iterator.todo.MergingPeekingIncreasingIterator;
import ru.mail.polis.collections.list.IPriorityQueue;
import ru.mail.polis.collections.list.todo.*;

import java.util.*;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        ArrayPriorityQueueSimple<Integer> mas1 = new ArrayPriorityQueueSimple<>();
        mas1.add(1);
        Iterator iter = mas1.iterator();
        iter.next();
        iter.remove();

    }
}

package ru.mail.polis.collections.list.todo;

import java.util.*;

public class LinkedDequeFull<E> extends LinkedDequeSimple<E> implements Deque<E>
{

    public LinkedDequeFull()
    {
        super();
    }

    @Override
    public boolean offerFirst(E e)
    {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e)
    {
        addLast(e);
        return true;
    }

    @Override
    public E pollFirst()
    {
        if (isEmpty())
        {
            return null;
        } else
        {
            return removeFirst();
        }
    }

    @Override
    public E pollLast()
    {
        if (isEmpty())
        {
            return null;
        } else
        {
            return removeLast();
        }
    }

    @Override
    public E peekFirst()
    {
        if (isEmpty())
        {
            return null;
        } else
        {
            return head.data;
        }
    }

    @Override
    public E peekLast()
    {
        if (isEmpty())
        {
            return null;
        } else
        {
            return tail.data;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o)
    {
        Objects.requireNonNull(o);

        if (o.equals(head.data))
        {
            removeFirst();
            return true;
        }

        Node current = head;

        while (current != null)
        {
            if (current.equals(o))
            {
                if (current == tail)
                {
                    removeLast();
                    return true;
                }
                current.left.right = current.right;
                current.right.left = current.left;
                size--;
                modCount++;
                return true;
            }
            current = current.right;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o)
    {
        Objects.requireNonNull(o);

        if (o.equals(tail.data))
        {
            removeLast();
            return true;
        }

        Node current = tail;

        while (current != null)
        {
            if (current.equals(o))
            {
                if (current == head)
                {
                    removeFirst();
                    return true;
                }
                current.left.right = current.right;
                current.right.left = current.left;
                size--;
                modCount++;
                return true;
            }
            current = current.left;
        }
        return false;
    }


    @Override
    public boolean add(E e)
    {
        return offerLast(e);
    }

    @Override
    public boolean offer(E e)
    {
        return offerLast(e);
    }

    @Override
    public E remove()
    {
        return removeFirst();
    }

    @Override
    public E poll()
    {
        if (isEmpty())
        {
            return null;
        }

        return removeFirst();
    }

    @Override
    public E element()
    {
        return getFirst();
    }

    @Override
    public E peek()
    {
        if (isEmpty())
        {
            return null;
        }
        return getFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        for (E elem : c)
        {
            addLast(elem);
        }
        return true;
    }

    @Override
    public void push(E e)
    {
        addFirst(e);
    }

    @Override
    public E pop()
    {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o)
    {
        return removeFirstOccurrence(o);
    }


    @Override
    public Iterator<E> descendingIterator()
    {
        return new Iterator<E>()
        {
            private Node prev, nextNode = tail;

            @Override
            public boolean hasNext()
            {
                return prev != null;
            }

            @Override
            public E next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }

                prev = nextNode;
                nextNode = nextNode.left;
                return prev.data;
            }
        };
    }

    @Override
    public Object[] toArray()
    {
        Object[] array = new Object[size];
        return toArray(array);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a)
    {
        Iterator<E> iterator = iterator();
        int i = 0;
        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next(), i++)
        {
            a[i] = (T) elem;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object o : c)
        {
            if (!contains(o))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        Iterator<E> iterator = iterator();
        int count = 0;
        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next())
        {
            if (c.contains(elem))
            {
                iterator.remove();
                count++;
            }
        }

        return count >= c.size();
    }


    @Override
    public boolean retainAll(Collection<?> c)
    {
        Iterator<E> iterator = iterator();
        for (E elem = iterator.next(); iterator.hasNext(); elem = iterator.next())
        {
            if (!c.contains(elem))
            {
                iterator.remove();
            }
        }
        return true;
    }

}



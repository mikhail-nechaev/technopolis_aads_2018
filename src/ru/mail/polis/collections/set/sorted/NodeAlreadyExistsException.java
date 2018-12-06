package ru.mail.polis.collections.set.sorted;

public class NodeAlreadyExistsException extends Exception{
    public NodeAlreadyExistsException(String message) {
        super(message);
    }
    public NodeAlreadyExistsException()
    {
        super("Node already exists");
    }
}

package ru.mail.polis.collections.set.sorted;

/*
 * Created by Nechaev Mikhail
 * Since 25/11/2018.
 */
public class UnbalancedTreeException extends Exception {

    public UnbalancedTreeException(String message) {
        super(message);
    }

    public static UnbalancedTreeException create(String message, int leftHeight, int rightHeight, String nodeInfo) {
        return new UnbalancedTreeException(
                message + "\n"
                        + "leftHeight = " + leftHeight + ","
                        + "rightHeight = " + rightHeight + "\n"
                        + "nodeInfo = " + nodeInfo
        );
    }
}

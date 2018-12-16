package ru.mail.polis.collections.set.sorted.todo;

import ru.mail.polis.collections.set.sorted.ISelfBalancingSortedTreeSet;
import ru.mail.polis.collections.set.sorted.UnbalancedTreeException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;


@SuppressWarnings("unchecked")
public class RedBlackTree<E extends Comparable<E>> implements ISelfBalancingSortedTreeSet<E> {


    protected static final class RBNode<E extends Comparable<E>> {
        E value;
        RBNode left;
        RBNode right;
        RBNode parent;
        int numRight ;
        int numLeft ;
        Color color;

        private enum  Color{
            RED, BLACK
        }

        public RBNode(E value){
            this();
            this.value = value;
        }

        private RBNode() {
            color = Color.BLACK;
            parent = null;
            left = null;
            right = null;
            numLeft = 0;
            numRight = 0;
        }

        @Override
        public String toString() {
            return "RBNode{" +
                    "value=" + value +
                    ", left=" + left +
                    ", right=" + right +
                    ", color=" + color +
                    '}';
        }
    }


    protected final Comparator<E> comparator;
    protected RBNode nil = new RBNode();
    protected RBNode root = nil;
    protected int length;


    public RedBlackTree() {
        this(Comparator.naturalOrder());
        root.left = nil;
        root.right = nil;
        root.parent = nil;
        length = 0;
    }

    public RedBlackTree(Comparator<E> comparator) {
        if(comparator == null){
            throw new NullPointerException();
        }
        this.comparator = Objects.requireNonNull(comparator, "comparator");
    }


    @Override
    public void checkBalance() throws UnbalancedTreeException {
        if (root != null) {
            if (root.color != RBNode.Color.BLACK) {
                throw new UnbalancedTreeException("Root must be black");
            }
            traverseTreeAndCheckBalanced(root);
        }
    }

    protected int traverseTreeAndCheckBalanced(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode == null) {
            return 1;
        }
        int leftBlackHeight = traverseTreeAndCheckBalanced(RBNode.left);
        int rightBlackHeight = traverseTreeAndCheckBalanced(RBNode.right);
        if (leftBlackHeight != rightBlackHeight) {
            throw UnbalancedTreeException.create("Black height must be equal.", leftBlackHeight, rightBlackHeight, RBNode.toString());
        }
        if (RBNode.color == RedBlackTree.RBNode.Color.RED) {
            checkRedNodeRule(RBNode);
            return leftBlackHeight;
        }
        return leftBlackHeight + 1;
    }

    protected void checkRedNodeRule(RBNode RBNode) throws UnbalancedTreeException {
        if (RBNode.left != null && RBNode.left.color != RedBlackTree.RBNode.Color.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then left child must be black.\n" + RBNode.toString());
        }
        if (RBNode.right != null && RBNode.right.color != RedBlackTree.RBNode.Color.BLACK) {
            throw new UnbalancedTreeException("If a RBNode is red, then right child must be black.\n" + RBNode.toString());
        }
    }

    protected boolean isNil(RBNode node){

        // return appropriate value
        return node == nil;

    }// end isNil(RedBlackNode node)



    protected void leftRotateFixup(RBNode x){

        // Case 1: Only x, x.right and x.right.right always are not nil.
        if (isNil(x.left) && isNil(x.right.left)){
            x.numLeft = 0;
            x.numRight = 0;
            x.right.numLeft = 1;
        }

        // Case 2: x.right.left also exists in addition to Case 1
        else if (isNil(x.left) && !isNil(x.right.left)){
            x.numLeft = 0;
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 2 + x.right.left.numLeft +
                    x.right.left.numRight;
        }

        // Case 3: x.left also exists in addition to Case 1
        else if (!isNil(x.left) && isNil(x.right.left)){
            x.numRight = 0;
            x.right.numLeft = 2 + x.left.numLeft + x.left.numRight;

        }
        // Case 4: x.left and x.right.left both exist in addtion to Case 1
        else{
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 3 + x.left.numLeft + x.left.numRight +
                    x.right.left.numLeft + x.right.left.numRight;
        }

    }// end leftRotateFixup(RedBlackNode x)


    protected RBNode<E> treeSuccessor(RBNode<E> x){

        // if x.left is not nil, call treeMinimum(x.right) and
        // return it's value
        if (!isNil(x.left) )
            return treeMinimum(x.right);

        RBNode<E> y = x.parent;

        // while x is it's parent's right child...
        while (!isNil(y) && x == y.right){
            // Keep moving up in the tree
            x = y;
            y = y.parent;
        }
        // Return successor
        return y;
    }// end treeMinimum(RedBlackNode x)



    protected RBNode <E> treeMaximum(RBNode<E> node){
        // while there is a smaller key, keep going left
        while (!isNil(node.right))
            node = node.right;
        return node;
    }// end treeMinimum(RedBlackNode node)


    protected RBNode<E> treeMinimum(RBNode<E> node){

        // while there is a smaller key, keep going left
        while (!isNil(node.left))
            node = node.left;
        return node;
    }// end treeMinimum(RedBlackNode node)


    // @param: x, The node which the lefRotate is to be performed on.
    // Performs a leftRotate around x.
    protected void leftRotate(RBNode<E> x){

        // Call leftRotateFixup() which updates the numLeft
        // and numRight values.
        leftRotateFixup(x);

        // Perform the left rotate as described in the algorithm
        // in the course text.
        RBNode<E> y;
        y = x.right;
        x.right = y.left;

        // Check for existence of y.left and make pointer changes
        if (!isNil(y.left))
            y.left.parent = x;
        y.parent = x.parent;

        // x's parent is nul
        if (isNil(x.parent))
            root = y;

            // x is the left child of it's parent
        else if (x.parent.left == x)
            x.parent.left = y;

            // x is the right child of it's parent.
        else
            x.parent.right = y;

        // Finish of the leftRotate
        y.left = x;
        x.parent = y;
    }// end leftRotate(RedBlackNode x)





    protected void rightRotate(RBNode<E> y){

        // Call rightRotateFixup to adjust numRight and numLeft values
        rightRotateFixup(y);

        // Perform the rotate as described in the course text.
        RBNode<E> x = y.left;
        y.left = x.right;

        // Check for existence of x.right
        if (!isNil(x.right))
            x.right.parent = y;
        x.parent = y.parent;

        // y.parent is nil
        if (isNil(y.parent))
            root = x;

            // y is a right child of it's parent.
        else if (y.parent.right == y)
            y.parent.right = x;

            // y is a left child of it's parent.
        else
            y.parent.left = x;
        x.right = y;

        y.parent = x;

    }// end rightRotate(RedBlackNode y)


    // @param: y, the node around which the righRotate is to be performed.
    // Updates the numLeft and numRight values affected by the rotate
    protected void rightRotateFixup(RBNode<E> y){

        // Case 1: Only y, y.left and y.left.left exists.
        if (isNil(y.right) && isNil(y.left.right)){
            y.numRight = 0;
            y.numLeft = 0;
            y.left.numRight = 1;
        }

        // Case 2: y.left.right also exists in addition to Case 1
        else if (isNil(y.right) && !isNil(y.left.right)){
            y.numRight = 0;
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 2 + y.left.right.numRight +
                    y.left.right.numLeft;
        }

        // Case 3: y.right also exists in addition to Case 1
        else if (!isNil(y.right) && isNil(y.left.right)){
            y.numLeft = 0;
            y.left.numRight = 2 + y.right.numRight +y.right.numLeft;

        }

        // Case 4: y.right & y.left.right exist in addition to Case 1
        else{
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 3 + y.right.numRight +
                    y.right.numLeft +
                    y.left.right.numRight + y.left.right.numLeft;
        }

    }// end rightRotateFixup(RedBlackNode y)



    protected void insert(RBNode<E> z) {

        // Create a reference to root & initialize a node to nil
        RBNode<E> y = nil;
        RBNode<E> x = root;

        // While we haven't reached a the end of the tree keep
        // tryint to figure out where z should go
        while (!isNil(x)){
            y = x;

            // if z.key is < than the current key, go left
            if (comparator.compare(z.value, x.value) < 0){

                // Update x.numLeft as z is < than x
                x.numLeft++;
                x = x.left;
            }

            // else z.key >= x.key so go right.
            else{

                // Update x.numGreater as z is => x
                x.numRight++;
                x = x.right;
            }
        }
        // y will hold z's parent
        z.parent = y;

        // Depending on the value of y.key, put z as the left or
        // right child of y
        if (isNil(y))
            root = z;
        else if (comparator.compare(z.value, y.value) < 0)
            y.left = z;
        else
            y.right = z;

        // Initialize z's children to nil and z's color to red
        z.left = nil;
        z.right = nil;
        z.color = RBNode.Color.RED;

        // Call insertFixup(z)
        insertFixup(z);

    }// end insert(RedBlackNode z)


    // @param: z, the node which was inserted and may have caused a violation
    // of the RedBlackTree properties
    // Fixes up the violation of the RedBlackTree properties that may have
    // been caused during insert(z)
    protected void insertFixup(RBNode z){

        RBNode<E> y = nil;
        // While there is a violation of the RedBlackTree properties..
        while (z.parent.color == RBNode.Color.RED){

            // If z's parent is the the left child of it's parent.
            if (z.parent == z.parent.parent.left){

                // Initialize y to z 's cousin
                y = z.parent.parent.right;

                // Case 1: if y is red...recolor
                if (y.color == RBNode.Color.RED){
                    z.parent.color = RBNode.Color.BLACK;
                    y.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    z = z.parent.parent;
                }
                // Case 2: if y is black & z is a right child
                else if (z == z.parent.right){

                    // leftRotaet around z's parent
                    z = z.parent;
                    leftRotate(z);
                }

                // Case 3: else y is black & z is a left child
                else{
                    // recolor and rotate round z's grandpa
                    z.parent.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    rightRotate(z.parent.parent);
                }
            }

            // If z's parent is the right child of it's parent.
            else{

                // Initialize y to z's cousin
                y = z.parent.parent.left;

                // Case 1: if y is red...recolor
                if (y.color == RBNode.Color.RED){
                    z.parent.color = RBNode.Color.BLACK;
                    y.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    z = z.parent.parent;
                }

                // Case 2: if y is black and z is a left child
                else if (z == z.parent.left){
                    // rightRotate around z's parent
                    z = z.parent;
                    rightRotate(z);
                }
                // Case 3: if y  is black and z is a right child
                else{
                    // recolor and rotate around z's grandpa
                    z.parent.color = RBNode.Color.BLACK;
                    z.parent.parent.color = RBNode.Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        // Color root black at all times
        root.color = RBNode.Color.BLACK;

    }// end insertFixup(RedBlackNode z)




    protected void remove(RBNode<E> v){

        RBNode<E> z = search(v.value);

        // Declare variables
        RBNode<E> x = nil;
        RBNode<E> y = nil;

        // if either one of z's children is nil, then we must remove z
        if (isNil(z.left) || isNil(z.right))
            y = z;

            // else we must remove the successor of z
        else y = treeSuccessor(z);

        // Let x be the left or right child of y (y can only have one child)
        if (!isNil(y.left))
            x = y.left;
        else
            x = y.right;

        // link x's parent to y's parent
        x.parent = y.parent;

        // If y's parent is nil, then x is the root
        if (isNil(y.parent))
            root = x;

            // else if y is a left child, set x to be y's left sibling
        else if (!isNil(y.parent.left) && y.parent.left == y)
            y.parent.left = x;

            // else if y is a right child, set x to be y's right sibling
        else if (!isNil(y.parent.right) && y.parent.right == y)
            y.parent.right = x;

        // if y != z, trasfer y's satellite data into z.
        if (y != z){
            z.value = y.value;
        }

        // Update the numLeft and numRight numbers which might need
        // updating due to the deletion of z.key.
        fixNodeData(x,y);

        // If y's color is black, it is a violation of the
        // RedBlackTree properties so call removeFixup()
        if (y.color == RBNode.Color.BLACK)
            removeFixup(x);
    }// end remove(RedBlackNode z)


    // @param: y, the RedBlackNode which was actually deleted from the tree
    // @param: key, the value of the key that used to be in y
    private void fixNodeData(RBNode<E> x, RBNode<E> y){

        // Initialize two variables which will help us traverse the tree
        RBNode<E> current = nil;
        RBNode<E> track = nil;


        // if x is nil, then we will start updating at y.parent
        // Set track to y, y.parent's child
        if (isNil(x)){
            current = y.parent;
            track = y;
        }

        // if x is not nil, then we start updating at x.parent
        // Set track to x, x.parent's child
        else{
            current = x.parent;
            track = x;
        }

        // while we haven't reached the root
        while (!isNil(current)){
            // if the node we deleted has a different key than
            // the current node
            if (comparator.compare(y.value, current.value) != 0) {

                // if the node we deleted is greater than
                // current.key then decrement current.numRight
                if (comparator.compare(y.value, current.value) > 0)
                    current.numRight--;

                // if the node we deleted is less than
                // current.key thendecrement current.numLeft
                if (comparator.compare(y.value, current.value) < 0)
                    current.numLeft--;
            }

            // if the node we deleted has the same key as the
            // current node we are checking
            else{
                // the cases where the current node has any nil
                // children and update appropriately
                if (isNil(current.left))
                    current.numLeft--;
                else if (isNil(current.right))
                    current.numRight--;

                    // the cases where current has two children and
                    // we must determine whether track is it's left
                    // or right child and update appropriately
                else if (track == current.right)
                    current.numRight--;
                else if (track == current.left)
                    current.numLeft--;
            }

            // update track and current
            track = current;
            current = current.parent;

        }

    }//end fixNodeData()



    protected void removeFixup(RBNode<E> x){

        RBNode<E> w;

        // While we haven't fixed the tree completely...
        while (x != root && x.color == RBNode.Color.BLACK){

            // if x is it's parent's left child
            if (x == x.parent.left){

                // set w = x's sibling
                w = x.parent.right;

                // Case 1, w's color is red.
                if (w.color == RBNode.Color.RED){
                    w.color = RBNode.Color.BLACK;
                    x.parent.color = RBNode.Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                // Case 2, both of w's children are black
                if (w.left.color == RBNode.Color.BLACK &&
                        w.right.color == RBNode.Color.BLACK){
                    w.color = RBNode.Color.RED;
                    x = x.parent;
                }
                // Case 3 / Case 4
                else{
                    // Case 3, w's right child is black
                    if (w.right.color == RBNode.Color.BLACK){
                        w.left.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    // Case 4, w = black, w.right = red
                    w.color = x.parent.color;
                    x.parent.color = RBNode.Color.BLACK;
                    w.right.color = RBNode.Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }
            // if x is it's parent's right child
            else{

                // set w to x's sibling
                w = x.parent.left;

                // Case 1, w's color is red
                if (w.color == RBNode.Color.RED){
                    w.color =RBNode.Color.BLACK;
                    x.parent.color = RBNode.Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                // Case 2, both of w's children are black
                if (w.right.color == RBNode.Color.BLACK &&
                        w.left.color == RBNode.Color.BLACK){
                    w.color = RBNode.Color.RED;
                    x = x.parent;
                }

                // Case 3 / Case 4
                else{
                    // Case 3, w's left child is black
                    if (w.left.color == RBNode.Color.BLACK){
                        w.right.color = RBNode.Color.BLACK;
                        w.color = RBNode.Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }

                    // Case 4, w = black, and w.left = red
                    w.color = x.parent.color;
                    x.parent.color = RBNode.Color.BLACK;
                    w.left.color = RBNode.Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }// end while

        // set x to black to ensure there is no violation of
        // RedBlack tree Properties
        x.color = RBNode.Color.BLACK;
    }// end removeFixup(RedBlackNode x)


    protected RBNode<E> search(E value){

        // Initialize a pointer to the root to traverse the tree
        RBNode<E> current = root;

        // While we haven't reached the end of the tree
        while (!isNil(current)){

            // If we have found a node with a value equal to value
            if (comparator.compare(current.value, value) == 0)

                // return that node and exit search(int)
                return current;

                // go left or right based on value of current and value
            else if (comparator.compare(current.value, value) < 0)
                current = current.right;

                // go left or right based on value of current and value
            else
                current = current.left;
        }

        // we have not found a node whose value is "value"
        return null;


    }// end search(int value)


    @Override
    public boolean add(E value) throws NullPointerException{
        if(value == null){
            throw new NullPointerException();
        }
        RBNode current = search(value);
        if(isNil(current) || current == null){
            insert(new RBNode<E>(value));
            length++;
            return true;
        }
        return false;
    }

    /**
     * Removes the specified element from this set if it is present.
     * <p>
     * Complexity = O(log(n))
     *
     * @param value object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean remove(E value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }
        if(root == null){
            throw new NoSuchElementException();
        }
        RBNode current = search(value);
        if(current != null){
            remove(current);
            length--;
            return true;
        }

        return false;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * aka collection contains element el such that {@code Objects.equals(el, value) == true}
     * <p>
     * Complexity = O(log(n))
     *
     * @param value element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(Object value) throws NullPointerException {
        if(value == null){
            throw new NullPointerException();
        }

        RBNode current = search((E)value);
        if(isNil(current) || current == null){
            return false;
        }
        return true;
    }

    /**
     * Returns the first (lowest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E first() throws  NoSuchElementException {

        if(isEmpty()){
            throw new NoSuchElementException();
        }

        RBNode current = treeMinimum(root);
        if(isNil(current) || current == null)
        {
            throw new IllegalStateException();
        }

        return (E) current.value;

    }

    /**
     * Returns the last (highest) element currently in this set.
     * <p>
     * Complexity = O(log(n))
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public E last() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException();
        }

        RBNode current = treeMaximum(root);
        if(isNil(current) || current == null)
        {
            throw new IllegalStateException();
        }

        return (E) current.value;
    }

    /**
     * Traverse self balanced tree set and check balance correctness.
     *
     * @throws UnbalancedTreeException if the self balanced tree set is unbalanced
     */

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
        return root == null || isNil(root);
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this method returns.
     */
    @Override
    public void clear() {
        root = null;
        length = 0;
    }
}

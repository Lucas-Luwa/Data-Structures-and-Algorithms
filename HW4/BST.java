import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of a BST.
 *
 * @author Lucas Luwa
 * @version 1.2823
 * @userid lluwa3 (i.e. gburdell3)
 * @GTID 903593176 (i.e. 900000000)
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The Collection of data provided is null. "
                    + "Please enter a valid Collection.");
        }
        for (T myData : data) {
            //An Illegal Argument Exception will be thrown if myData happens to be null.
            add(myData);
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data provided is null. Can't add at index. "
                    + "Please enter valid data");
        }
        root = helperAdd(root, data);
    }

    /**
     * Recursively assists the add method.
     * @param curr Node that operations will be performed on.
     * @param data Data to insert into the BST.
     * @return returns a BSTNode for the pointer reinforcement process.
     */
    private BSTNode<T> helperAdd(BSTNode<T> curr, T data) {
        if (curr == null) {
            size++;
            return new BSTNode<T>(data);
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(helperAdd(curr.getLeft(), data));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(helperAdd(curr.getRight(), data));
        }
        return curr;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data provided is null. Can't remove at index. "
                    + "Please enter valid data");
        }
        //Can data be negative in a BST?
        BSTNode<T> dummyNode = new BSTNode<T>(null);
        //Is it okay to just initialize to the above with data. Lecture modules say -1 but I'm not too sure.
        root = helpRemove(root, data, dummyNode);
        return dummyNode.getData();
    }

    /**
     *
     * @param curr Node being used to identify node to remove.
     * @param data Data being checked to identify node to remove.
     * @param dummyNode Node used to store return value.
     * @return Returns the value that is removed from the BST.
     */
    private BSTNode<T> helpRemove(BSTNode<T> curr, T data, BSTNode<T> dummyNode) {
        if (curr == null) {
            throw new NoSuchElementException("The data you are trying to remove is not in the BST. "
                    + "Can't remove element as a result. Sorry.");
        }
        if ((data.compareTo(curr.getData()) < 0)) {
            curr.setLeft(helpRemove(curr.getLeft(), data, dummyNode));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(helpRemove(curr.getRight(), data, dummyNode));
        } else {
            dummyNode.setData(curr.getData());
            size--;
            if (curr.getLeft() == null && curr.getRight() == null) {
                return null;
            } else if (curr.getLeft() != null && curr.getRight() == null) {
                return curr.getLeft();
            } else if (curr.getRight() != null && curr.getLeft() == null) {
                return curr.getRight();
            } else {
                BSTNode<T> newDummy = new BSTNode<T>(null);
                //Same issue with data here.
                curr.setRight(eliminateSuccessor(curr.getRight(), newDummy));
                curr.setData(newDummy.getData());
            }
        }
        return curr;
    }

    /**
     * Helps seek and replace Successor
     * @param curr Node where we will start to seek the successor.
     * @param newDummy Dummy node to hold our return value.
     * @return Return the BSTNode of the new right node of curr, which is set in the previous method.
     */
    private BSTNode<T> eliminateSuccessor(BSTNode<T> curr, BSTNode<T> newDummy) {
        if (curr.getLeft() == null) {
            newDummy.setData(curr.getData());
            return curr.getRight();
        }
        curr.setLeft(eliminateSuccessor(curr.getLeft(), newDummy));
        return curr;
    }
    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data provided is null. The program can't retrieve null data "
                    + "from the BST. Please enter valid data.");
        }
        return helperGet(root, data); //replace
    }

    /**
     *
     * @param curr The node being passed in. We will perform left and right checks for this method.
     * @param data Data that we are trying to find within the BST.
     * @return Returns value of the data we are trying to find.
     */
    private T helperGet(BSTNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("The data you are seeking is not in the BST. Sorry.");
        }
        if (data.compareTo(curr.getData()) == 0) {
            return curr.getData();
        } else if (data.compareTo(curr.getData()) < 0) {
            return helperGet(curr.getLeft(), data);
        } else {
            return helperGet(curr.getRight(), data);
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data provided is null. Can't perform contains operation."
                    + "Please enter valid data.");
        }
        return containsHelper(root, data);
    }

    /**
     * Helper method for contains.
     * @param curr Starting node to perform contains operation.
     * @param data Data being searched in the BST.
     * @return Returns whether the data exists or not.
     */
    private boolean containsHelper(BSTNode<T> curr, T data) {
        if (curr == null) {
            return false;
        } else if (data.compareTo(curr.getData()) < 0) {
            return containsHelper(curr.getLeft(), data);
        } else if (data.compareTo(curr.getData()) > 0) {
            return containsHelper(curr.getRight(), data);
        } else {
            return true;
        }
    }
    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the pre-order traversal of the tree
     */
    public List<T> preorder() {
        List<T> myList = new ArrayList<>(); // Should I be doing this.
        return preorderHelper(root, myList);
    }
    /**
     * LEFT TICK
     * @param curr Node that will be used to traverse to the next part of the BST.
     * @param myList This collects values and returns it back to the preorder method.
     * @return Returns List of elements "in order" to the caller.
     */
    private List<T> preorderHelper(BSTNode<T> curr, List<T> myList) {
        if (curr != null) {
            myList.add(curr.getData());
            preorderHelper(curr.getLeft(), myList);
            preorderHelper(curr.getRight(), myList);
        }
        return myList;
    }
    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the in-order traversal of the tree
     */
    public List<T> inorder() {
        List<T> myList = new ArrayList<>(); // Should I be doing this.
        return inorderHelper(root, myList); //replace
    }

    /**
     * CENTER TICK
     * @param curr Node that will be used to traverse to the next part of the BST.
     * @param myList This collects values and returns it back to the inorder method.
     * @return Returns List of elements "in order" to the caller.
     */
    private List<T> inorderHelper(BSTNode<T> curr, List<T> myList) {
        if (curr != null) {
            inorderHelper(curr.getLeft(), myList);
            myList.add(curr.getData());
            inorderHelper(curr.getRight(), myList);
        }
        return myList;
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the post-order traversal of the tree
     */
    public List<T> postorder() {
        List<T> myList = new ArrayList<>();
        return postorderHelper(root, myList);
    }
    /**
     *
     * @param curr Node that will be used to traverse to the next part of the BST.
     * @param myList This collects values and returns it back to the postorder method.
     * @return Returns List of elements "in order" to the caller.
     */
    private List<T> postorderHelper(BSTNode<T> curr, List<T> myList) {
        if (curr != null) {
            postorderHelper(curr.getLeft(), myList);
            postorderHelper(curr.getRight(), myList);
            myList.add(curr.getData());
        }
        return myList;
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level-order traversal of the tree
     */
    public List<T> levelorder() {
        Queue<BSTNode<T>> myQueue = new LinkedList<>();
        List<T> myList = new ArrayList<>();
        if (size == 0) {
            return myList;
        }
        myQueue.add(root);
        while (!myQueue.isEmpty()) {
            BSTNode<T> curr = myQueue.remove();
            myList.add(curr.getData());
            if (curr.getLeft() != null) {
                myQueue.add(curr.getLeft());
            }
            if (curr.getRight() != null) {
                myQueue.add(curr.getRight());
            }
        }
        return myList;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelper(root);
    }

    /**
     *
     * @param curr Starting node to perform height calculations.
     * @return Return height of BST.
     */
    private int heightHelper(BSTNode<T> curr) {
        if (curr == null) {
            return -1;
        } else {
            int leftHeight = heightHelper(curr.getLeft());
            int rightHeight = heightHelper(curr.getRight());
            if (leftHeight > rightHeight) {
                return leftHeight + 1;
            } else {
                return rightHeight + 1;
            }
        }

    }
    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Generates a list of the max data per level from the top to the bottom
     * of the tree. (Another way to think about this is to get the right most
     * data per level from top to bottom.)
     * 
     * This must be done recursively.
     *
     * This list should contain the last node of each level.
     *
     * If the tree is empty, an empty list should be returned.
     *
     * Ex:
     * Given the following BST composed of Integers
     *      2
     *    /   \
     *   1     4
     *  /     / \
     * 0     3   5
     * getMaxDataPerLevel() should return the list [2, 4, 5] - 2 is the max
     * data of level 0, 4 is the max data of level 1, and 5 is the max data of
     * level 2
     *
     * Ex:
     * Given the following BST composed of Integers
     *               50
     *           /        \
     *         25         75
     *       /    \
     *      12    37
     *     /  \    \
     *   11   15   40
     *  /
     * 10
     * getMaxDataPerLevel() should return the list [50, 75, 37, 40, 10] - 50 is
     * the max data of level 0, 75 is the max data of level 1, 37 is the
     * max data of level 2, etc.
     *
     * Must be O(n).
     *
     * @return the list containing the max data of each level
     */
    public List<T> getMaxDataPerLevel() {
        List<T> myList = new ArrayList<>();
        gmdplHelper(root, myList, 0);
        return myList;
    }

    /**
     *
     * @param curr Starting node to perform the get max per level operation.
     * @param myList Trackers the running maxes in each level of the BST>
     * @param currlevel Tracks current level within the BST.
     */
    private void gmdplHelper(BSTNode<T> curr, List<T> myList, int currlevel) {
        if (curr == null) {
            return;
        }
        if (myList.size() == currlevel) {
            myList.add(curr.getData());
        }
        gmdplHelper(curr.getRight(), myList, currlevel + 1);
        gmdplHelper(curr.getLeft(), myList, currlevel + 1);
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}

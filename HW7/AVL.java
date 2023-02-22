import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
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
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data provided is null and, therefore, invalid.");
        }
        for (T loc: data) {
            if (loc == null) {
                throw new IllegalArgumentException("There exists a null data in the collection. Please try again.");
            }
            add(loc);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     * 
     * Hint: Should you use value equality or reference equality?
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
     * @param data Data to insert into the AVL.
     * @return returns a AVLNode for the pointer reinforcement process.
     */
    private AVLNode<T> helperAdd(AVLNode<T> curr, T data) {
        if (curr == null) {
            curr = new AVLNode<>(data);
            curr.setHeight(0);
            curr.setBalanceFactor(0);
            size++;
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(helperAdd(curr.getLeft(), data));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(helperAdd(curr.getRight(), data));
        } else {
            return curr;
        }
        curr.setBalanceFactor(balanceFactor(curr));
        curr = updateRotate(curr);
        curr.setHeight(heightCalculator(curr));
        curr.setBalanceFactor(balanceFactor(curr));
        return curr;
    }

    /**
     * Right rotation
     * @param myNode Node to rotate right.
     * @return parent node after operation.
     */
    private AVLNode<T> rightTurn(AVLNode<T> myNode) {
        AVLNode<T> myNodeLT = myNode.getLeft();
        myNode.setLeft(myNodeLT.getRight());
        myNodeLT.setRight(myNode);
        myNodeLT.setHeight(heightCalculator(myNodeLT));
        myNodeLT.setBalanceFactor(balanceFactor(myNodeLT));
        myNode.setHeight(heightCalculator(myNode));
        myNode.setBalanceFactor(balanceFactor(myNode));
        return myNodeLT;
    }
    /**
     * left rotation
     * @param myNode Node to rotate.
     * @return parent node after operation.
     */
    private AVLNode<T> leftTurn(AVLNode<T> myNode) {
        AVLNode<T> myNodeRT = myNode.getRight();
        myNode.setRight(myNodeRT.getLeft());
        myNodeRT.setLeft(myNode);
        myNode.setHeight(heightCalculator(myNode));
        myNode.setBalanceFactor(balanceFactor(myNode));
        myNodeRT.setHeight(heightCalculator(myNodeRT));
        myNodeRT.setBalanceFactor(balanceFactor(myNodeRT));
        return myNodeRT;
    }

    /**
     * Finds balance factor for you!
     * @param myNode Node you want balance factor of.
     * @return Balance Factor of node.
     */
    private int balanceFactor(AVLNode<T> myNode) {
        return heightCalculator(myNode.getLeft()) - heightCalculator(myNode.getRight());
    }

    /**
     * Finds height of node
     * @param myNode Node you want height of.
     * @return height of node.
     */
    private int heightCalculator(AVLNode<T> myNode) {
        if (myNode == null) {
            return -1;
        }
        int lt = heightCalculator(myNode.getLeft());
        int rt = heightCalculator(myNode.getRight());
        if (lt > rt) {
            return 1 + lt;
        } else {
            return 1 + rt;
        }
    }


    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data provided is null. Can't remove at index. "
                    + "Please enter valid data");
        }
        AVLNode<T> dummyNode = new AVLNode<>(null);
        root = helpRemove(root, data, dummyNode);
        return dummyNode.getData();
    }
    /**
     *
     * @param curr Node being used to identify node to remove.
     * @param data Data being checked to identify node to remove.
     * @param dummyNode Node used to store return value.
     * @return Returns the value that is removed from the AVL.
     */
    private AVLNode<T> helpRemove(AVLNode<T> curr, T data, AVLNode<T> dummyNode) {
        if (curr == null) {
            throw new NoSuchElementException("The data you are trying to remove is not in the AVL. "
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
                AVLNode<T> newDummy = new AVLNode<T>(null);
                //curr.setRight(eliminateSuccessor(curr.getRight(), newDummy));
                curr.setLeft(axePredecessor(curr.getLeft(), newDummy));
                curr.setData(newDummy.getData());
            }
        }
        curr.setHeight(heightCalculator(curr));
        curr.setBalanceFactor(balanceFactor(curr));
        curr = updateRotate(curr);
        return curr;
    }

    /**
     * Helps seek and replace Predecessor
     * @param curr Node where we will start to seek the Predecessor.
     * @param newDummy Dummy node to hold our return value.
     * @return Return the AVLNode of the new right node of curr, which is set in the previous method.
     */
    private AVLNode<T> axePredecessor(AVLNode<T> curr, AVLNode<T> newDummy) {
        if (curr.getRight() == null) {
            newDummy.setData(curr.getData());
            return curr.getLeft();
        }
        curr.setRight(axePredecessor(curr.getRight(), newDummy));
        //Performing more adjustments here:
        curr.setHeight(heightCalculator(curr));
        curr.setBalanceFactor(balanceFactor(curr));
        curr = updateRotate(curr);
        return curr;
    }

    /**
     * Determine if rotate is needed.
     * @param curr rotated node.
     * @return resulting new parent node.
     */
    private AVLNode<T> updateRotate(AVLNode<T> curr) {
        if (curr == null) {
            return null;
        }
        if (curr.getBalanceFactor() > 1) {
            if (curr.getLeft().getBalanceFactor() == -1) {
                curr.setLeft(leftTurn(curr.getLeft()));
            }
            return rightTurn(curr);
        } else if (curr.getBalanceFactor() < -1) {
            if (curr.getRight().getBalanceFactor() == 1) {
                curr.setRight(rightTurn(curr.getRight()));
            }
            return leftTurn(curr);
        }
        return curr;
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data provided is null. The program can't retrieve null data "
                    + "from the AVL. Please enter valid data.");
        }
        return helperGet(root, data); //replace
    }
    /**
     *
     * @param curr The node being passed in. We will perform left and right checks for this method.
     * @param data Data that we are trying to find within the AVL.
     * @return Returns value of the data we are trying to find.
     */
    private T helperGet(AVLNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("The data you are seeking is not in the AVL. Sorry.");
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
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
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
     * @param data Data being searched in the AVL.
     * @return Returns whether the data exists or not.
     */
    private boolean containsHelper(AVLNode<T> curr, T data) {
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
     * Returns the height of the root of the tree.
     * 
     * Should be O(1). 
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the data in the deepest node. If there is more than one node
     * with the same deepest depth, return the rightmost (i.e. largest) node with 
     * the deepest depth. 
     * 
     * Should be recursive. 
     *
     * Must run in O(log n) for all cases.
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      3
     *        \
     *         1
     * Max Deepest Node:
     * 1 because it is the deepest node
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      4
     *        \    /
     *         1  3
     * Max Deepest Node:
     * 3 because it is the maximum deepest node (1 has the same depth but 3 > 1)
     *
     * @return the data in the maximum deepest node or null if the tree is empty
     */
    public T maxDeepestNode() {
        if (size == 0) {
            return null;
        }
        return mdnHelper(root);
    }

    /**
     *
     * @param curr location to start finding deepest node.
     * @return T data.
     */
    private T mdnHelper(AVLNode<T> curr) {
        if (curr.getHeight() == 0) {
            return curr.getData();
        }
        if (curr.getBalanceFactor() > 0) {
            return mdnHelper(curr.getLeft());
        }
        return mdnHelper(curr.getRight());
    }


    /**
     * In BSTs, you learned about the concept of the successor: the
     * smallest data that is larger than the current data. However, you only
     * saw it in the context of the 2-child remove case.
     *
     * This method should retrieve (but not remove) the successor of the data
     * passed in. There are 2 cases to consider:
     * 1: The right subtree is non-empty. In this case, the successor is the
     * leftmost node of the right subtree.
     * 2: The right subtree is empty. In this case, the successor is the lowest
     * ancestor of the node whose left subtree contains the data. 
     * 
     * The second case means the successor node will be one of the node(s) we 
     * traversed left from to find data. Since the successor is the SMALLEST element 
     * greater than data, the successor node is the lowest/last node 
     * we traversed left from on the path to the data node.
     *
     * This should NOT be used in the remove method.
     * 
     * Should be recursive. 
     *
     * Ex:
     * Given the following AVL composed of Integers
     *                    76
     *                  /    \
     *                34      90
     *                  \    /
     *                  40  81
     * successor(76) should return 81
     * successor(81) should return 90
     * successor(40) should return 76
     *
     * @param data the data to find the successor of
     * @return the successor of data. If there is no larger data than the
     * one given, return null.
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T successor(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Your data is invalid, since it's null");
        }
        AVLNode<T> target = nodeSeeker(root, data);
        AVLNode<T> retVal = nextInLine(root, target);
        if (retVal == null) {
            return null;
        }
        return retVal.getData();
    }

    /**
     *
     * @param root root of avl.
     * @param dataLine location of og data.
     * @return return desired node.
     */
    private AVLNode<T> nextInLine(AVLNode<T> root, AVLNode<T> dataLine) {
        if (dataLine.getRight() != null) {
            AVLNode<T> holder = dataLine.getRight();
            while (holder.getLeft() != null) {
                holder = holder.getLeft();
            }
            return holder;
        }
        AVLNode<T> retVal = null;
        while (root != null) {
            if (root.getData().compareTo(dataLine.getData()) > 0) {
                retVal = root;
                root = root.getLeft();
            } else if (root.getData().compareTo(dataLine.getData()) < 0) {
                root = root.getRight();
            } else {
                break;
            }
        }
        return retVal;
    }

    /**
     *
     * @param curr node to start at.
     * @param data data being sought after.
     * @return node of sought after data.
     */
    private AVLNode<T> nodeSeeker(AVLNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("The data you are seeking is not in the AVL. Sorry.");
        }
        if (data.compareTo(curr.getData()) == 0) {
            return curr;
        } else if (data.compareTo(curr.getData()) < 0) {
            return nodeSeeker(curr.getLeft(), data);
        } else {
            return nodeSeeker(curr.getRight(), data);
        }
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
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

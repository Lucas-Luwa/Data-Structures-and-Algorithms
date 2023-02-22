import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MinHeap.
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
public class MinHeap<T extends Comparable<? super T>> {

    /**
     * The initial capacity of the MinHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MinHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     * To initialize the backing array, create a Comparable array and then cast
     * it to a T array.
     */
    public MinHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the
     * size of the passed in ArrayList (not INITIAL_CAPACITY). Index 0 should
     * remain empty, indices 1 to n should contain the data in proper order, and
     * the rest of the indices should be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MinHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The provided ArrayList is null. Please try again.");
        }
        backingArray = (T[]) new Comparable[data.size() * 2 + 1];
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException("Initialize MinHeap Failed. "
                        + "A part of the data within ArrayList is null.");
            }
            backingArray[i + 1] = data.get(i);
        }
        size = data.size();
        for (int i = size / 2; i > 0; i--) {
            downHeap(i);
        }
    }

    /**
     * downHeap is used to help to downheap items within our heap.
     * @param parentNodeIndexORG Index of the parent to be dealt with.
     */
    private void downHeap(int parentNodeIndexORG) {
        int parentNodeIndex = parentNodeIndexORG;
        while (parentNodeIndex * 2 <= size) {
            if ((parentNodeIndex * 2 + 1) > size) {
                if (backingArray[parentNodeIndex].compareTo(backingArray[parentNodeIndex * 2]) > 0) {
                    swapSpots(parentNodeIndex * 2, parentNodeIndex);
                }
                break; //END CONDITION!
            } else if (backingArray[parentNodeIndex].compareTo(backingArray[parentNodeIndex * 2]) > 0
                    && backingArray[parentNodeIndex * 2].compareTo(backingArray[parentNodeIndex * 2 + 1]) < 0) {
                //Generic non-edge case. Let's check if lt < rt && lt < parentNodeIndex.
                swapSpots(parentNodeIndex * 2, parentNodeIndex);
                parentNodeIndex *= 2;
            } else if (backingArray[parentNodeIndex].compareTo(backingArray[parentNodeIndex * 2 + 1]) > 0) {
                //Generic non-edge case. Let's check if lt > rt && rt < parentNodeIndex.
                swapSpots(parentNodeIndex * 2 + 1, parentNodeIndex);
                parentNodeIndex = parentNodeIndex * 2 + 1;
            } else {
                break;
            }
        }
    }

    /**
     *
     * @param one first swap val.
     * @param two second swap val.
     */
    private void swapSpots(int one, int two) {
        T tempVal = backingArray[one];
        backingArray[one] = backingArray[two];
        backingArray[two] = tempVal;
    }
    /**
     * Adds an item to the heap. If the backing array is full (except for
     * index 0) and you're trying to add a new item, then double its capacity.
     * The order property of the heap must be maintained after adding. You can
     * assume that no duplicate data will be passed in.
     * 
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data provided was null. Please enter valid data");
        }
        T tempVal;
        //RESIZE ANTICS
        if (size + 1 == backingArray.length) {
            T[] tempArr = (T[]) new Comparable[backingArray.length * 2];
            for (int i = 1; i < backingArray.length; i++) {
                tempArr[i] = backingArray[i];
            }
            backingArray = tempArr;
        }
        backingArray[size + 1] = data;
        int localPosition = size + 1;
        while (localPosition > 0) {
            if (localPosition / 2 > 0 && backingArray[localPosition / 2].compareTo(backingArray[localPosition]) > 0) {
                tempVal = backingArray[localPosition / 2];
                backingArray[localPosition / 2] = backingArray[localPosition];
                backingArray[localPosition] = tempVal;
            }
            localPosition = localPosition / 2;
        }
        size++;
    }

    /**
     * Removes and returns the min item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array.
     * The order property of the heap must be maintained after removing.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("You can't remove a value from an empty heap.");
        }
        //Edge case if there's only one element
        T retVal = backingArray[1];
        if (size == 1) {
            backingArray[1] = null;
            size = 0;
            return retVal;
        }
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size--; //MUST BE BEFORE DOWNHEAP OR SIZE IS MESSED UP.
        downHeap(1);
        return retVal;
    }

    /**
     * Returns the minimum element in the heap.
     *
     * @return the minimum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Can't find minimum value of an empty heap.");
        }
        return backingArray[1];
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}

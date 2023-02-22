import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Random;
import java.util.LinkedList;

/**
 * Your implementation of various sorting algorithms.
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
public class Sorting {

    /**
     * Implement insertion sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Your array or comparator is null. "
                    + "Please enter a valid array or comparator.");
        }
        int j = 0;
        for (int i = 1; i <= arr.length - 1; i++) {
            j = i;
            while (j > 0 && comparator.compare(arr[j - 1], arr[j]) > 0) {
                switchSpots(arr, j - 1, j);
                j--;
            }
        }
    }

    /**
     * Implement cocktail sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * NOTE: See pdf for last swapped optimization for cocktail sort. You
     * MUST implement cocktail sort with this optimization
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Your array or comparator is null. "
                    + "Please enter a valid array or comparator.");
        }
        int startInd = 0;
        int endInd = arr.length - 1;
        int swapped = startInd;
        while (endInd > startInd) {
            swapped = startInd;
            for (int i = startInd; i < endInd; i++) {
                if (comparator.compare(arr[i], arr[i + 1]) > 0) {
                    switchSpots(arr, i, i + 1);
                    swapped = i;
                }
            }
            endInd = swapped;
            for (int i = endInd; i > startInd; i--) {
                if (comparator.compare(arr[i - 1], arr[i]) > 0) {
                    switchSpots(arr, i - 1, i);
                    swapped = i;
                }
            }
            startInd = swapped;
        }
    }

    /**
     *
     * @param arr Array fed into method.
     * @param index1 First index to switch.
     * @param index2 Second index to switch.
     * @param <T> Data type to swap
     */
    private static <T> void switchSpots(T[] arr, int index1, int index2) {
        T holder = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = holder;
    }

    /**
     * Implement merge sort.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * You can create more arrays to run merge sort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * Hint: If two data are equal when merging, think about which subarray
     * you should pull from first
     *
     * @param <T>        data type to sort
     * @param arr        the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Your array or comparator is null. "
                    + "Please enter a valid array or comparator.");
        }
        int length = arr.length;
        int middle = arr.length / 2;
        if (length <= 1) {
            return;
        }
        T[] leftArr = (T[]) new Object[length / 2];
        T[] rightArr = (T[]) new Object[length - length / 2];
        for (int i = 0; i < arr.length; i++) {
            if (i < length / 2) {
                leftArr[i] = arr[i];
            } else {
                rightArr[i - length / 2] = arr[i];
            }
        }
        mergeSort(leftArr, comparator);
        mergeSort(rightArr, comparator);
        int leftIndex = 0;
        int rightIndex = 0;
        int currIndex = 0;
        while (leftIndex < middle && rightIndex < length - middle) {
            if (comparator.compare(leftArr[leftIndex], rightArr[rightIndex]) <= 0) {
                arr[currIndex] = leftArr[leftIndex];
                leftIndex++;
            } else {
                arr[currIndex] = rightArr[rightIndex];
                rightIndex++;
            }
            currIndex++;
        }
        while (leftIndex < middle) {
            arr[currIndex] = leftArr[leftIndex];
            currIndex++;
            leftIndex++;
        }
        while (rightIndex < length - middle) {
            arr[currIndex] = rightArr[rightIndex];
            currIndex++;
            rightIndex++;
        }
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     * in-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not receive
     * credit if you do not use the one we have taught you!
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     * @throws java.lang.IllegalArgumentException if the array or comparator or
     *                                            rand is null
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator, Random rand) {
        if (arr == null || comparator == null || rand == null) {
            throw new IllegalArgumentException("Your array or comparator or rand is null. "
                    + "Please enter a valid array or comparator or rand");
        }
        quikSortHelper(arr, comparator, rand, 0, arr.length - 1);
    }

    /**
     *
     * @param arr Arr being fed into method.
     * @param comparator Comparator parameter for usage in method.
     * @param rand Rand to be used in method if needed.
     * @param left Starting point.
     * @param right Right starting point.
     * @param <T> Data type to sort
     */
    private static <T> void quikSortHelper(T[] arr, Comparator<T> comparator, Random rand, int left, int right) {
        if (right - left < 1) {
            return;
        }
        int pivotIndex = rand.nextInt(right - left + 1) + left;
        T pivot = arr[pivotIndex];
        switchSpots(arr, left, pivotIndex);
        int i = left + 1;
        int j = right;
        while (i <= j) {
            while (i <= j && comparator.compare(pivot, arr[i]) >= 0) {
                i++;
            }
            while (i <= j && comparator.compare(pivot, arr[j]) <= 0) {
                j--;
            }
            if (i <= j) {
                switchSpots(arr, i, j);
                i++;
                j--;
            }
        }
        switchSpots(arr, left, j);
        quikSortHelper(arr, comparator, rand, left, j - 1);
        quikSortHelper(arr, comparator, rand, j + 1, right);
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(kn)
     *
     * And a best case running time of:
     * O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need. The number of iterations
     * can be determined using the number with the largest magnitude.
     *
     * At no point should you find yourself needing a way to exponentiate a
     * number; any such method would be non-O(1). Think about how how you can
     * get each power of BASE naturally and efficiently as the algorithm
     * progresses through each digit.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use ArrayList or LinkedList if you wish, but it may only be
     * used inside radix sort and any radix sort helpers. Do NOT use these
     * classes with other sorts. However, be sure the List implementation you
     * choose allows for stability while being as efficient as possible.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @param arr the array to be sorted
     * @throws java.lang.IllegalArgumentException if the array is null
     */
    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Your array is null. Please enter a valid array.");
        }
        if (arr.length == 0) {
            return;
        }
        LinkedList<Integer>[] buckets = new LinkedList[19];
        int currMax = Math.abs(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            if (Math.abs(arr[i]) > currMax) {
                currMax = Math.abs(arr[i]);
            } else if (arr[i] == Integer.MIN_VALUE) {
                currMax = Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < 19; i++) {
            buckets[i] = new LinkedList<>();
        }
        //int numDigits = Integer.toString(currMax).length();
        int temp = currMax;
        int numDigits = 0;
        while (temp > 0) {
            temp = temp / 10;
            numDigits++;
        }
        int multVal = 1;
        for (int i = 0; i < numDigits; i++) {
            for (int j = 0; j < arr.length; j++) {
                //buckets[(arr[j] / (Math.pow(10,i))) % 10) + 9].add; doesn't work - I've messed up brackets somehow.
                buckets[(arr[j] / (int) multVal % 10) + 9].addLast(arr[j]);
            }
            int index = 0;
            for (int j = 0; j < buckets.length; j++) {
                while (buckets[j] != null && !buckets[j].isEmpty()) {
                    arr[index] = buckets[j].remove();
                    index++;
                }
            }
            multVal *= 10;
        }


    }

    /**
     * Implement heap sort.
     *
     * It should be:
     * out-of-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Use java.util.PriorityQueue as the heap. Note that in this
     * PriorityQueue implementation, elements are removed from smallest
     * element to largest element.
     *
     * Initialize the PriorityQueue using its build heap constructor (look at
     * the different constructors of java.util.PriorityQueue).
     *
     * Return an int array with a capacity equal to the size of the list. The
     * returned array should have the elements in the list in sorted order.
     *
     * @param data the data to sort
     * @return the array with length equal to the size of the input list that
     * holds the elements from the list is sorted order
     * @throws java.lang.IllegalArgumentException if the data is null
     */
    public static int[] heapSort(List<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data provided is null. Provide valid data please.");
        }
        PriorityQueue<Integer> newHeap = new PriorityQueue<>(data);
        int[] arr = new int[data.size()];
        for (int i = 0;  i < arr.length; i++) {
            arr[i] = newHeap.remove();
        }
        return arr;
    }
}

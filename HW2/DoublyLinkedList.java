import java.util.NoSuchElementException;

/**
 * Your implementation of a non-circular DoublyLinkedList with a tail pointer.
 *
 * @author Lucas Luwa
 * @version 1.0
 * @userid lluwa3 (i.e. gburdell3)
 * @GTID 903593176 (i.e. 900000000)
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class DoublyLinkedList<T> {

    // Do not add new instance variables or modify existing ones.
    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    // Do not add a constructor.

    /**
     * Adds the element to the specified index. Don't forget to consider whether
     * traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Your data is invalid. "
                    + "Index entered is less than 0 or greater than size.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data provided is null. Can't add to specified index.");
        }
        if (isEmpty()) {
            firstNode(data);
        } else if (index == 0) {
            //            DoublyLinkedListNode<T> curr = head;
            DoublyLinkedListNode<T> insertVal = new DoublyLinkedListNode<>(data, null, head);
            head.setPrevious(insertVal); //head = head.getPrevious();
            head = insertVal;
            size++;
        } else if (index == size) {
            //ASK ABOUT SCRATCH PAPER.
            //Don't forget about the tail lol doubly not singly!!
            DoublyLinkedListNode<T> insertVal = new DoublyLinkedListNode<>(data, tail, null);
            tail.setNext(insertVal);
            tail = insertVal; //tail = tail.getNext();
            size++;
        } else if (index < (size / 2)) {
            DoublyLinkedListNode<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.getNext();
            }
            //Consider stopping on node itself - practice later on.
            DoublyLinkedListNode<T> insertVal = new DoublyLinkedListNode<>(data, curr, curr.getNext());
            curr.setNext(insertVal);
            curr.getNext().getNext().setPrevious(insertVal);
            size++;
        } else {
            DoublyLinkedListNode<T> curr = tail;
            for (int i = size; i > index + 1; i--) {
                curr = curr.getPrevious();
            }
            DoublyLinkedListNode<T> insertVal = new DoublyLinkedListNode<>(data, curr.getPrevious(), curr);
            curr.setPrevious(insertVal);
            curr.getPrevious().getPrevious().setNext(insertVal);
            size++;
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        //Will change head later so need to create a holder var.
        addAtIndex(0, data);
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        addAtIndex(size, data);
    }

    /**
     * Removes and returns the element at the specified index. Don't forget to
     * consider whether traversing the list from the head or tail is more
     * efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("The index you have entered is out of bounds. "
                    + "It is either less than 0 or greater than or equal to size. Please try again.");
        }
        if (index == 0) {
            return removeFromFront();
        } else if (index == size - 1) {
            return removeFromBack();
        } else if (index > size / 2) {
            DoublyLinkedListNode<T> curr = tail;
            for (int i = size - 1; i > index + 1; i--) {
                curr = curr.getPrevious();
            }
            T holder = curr.getPrevious().getData();
            curr.setPrevious(curr.getPrevious().getPrevious());
            curr = curr.getPrevious();
            curr.setNext(curr.getNext().getNext());
            size--;
            return holder;
        } else {
            DoublyLinkedListNode<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.getNext();
            }
            T holder = curr.getNext().getData();
            curr.setNext(curr.getNext().getNext());
            curr = curr.getNext();
            curr.setPrevious(curr.getPrevious().getPrevious());
            size--;
            return holder;
        }
    }

    /**
     * Removes and returns the first element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        T holder;
        if (isEmpty()) {
            throw new NoSuchElementException("The list is currently empty. Can not complete removing data from front.");
        }
        holder = head.getData();
        if (size == 1) {
            tail = null;
            head = null;
        } else {
            head = head.getNext();
            head.setPrevious(null);
        }
        size--;
        return holder;
    }

    /**
     * Removes and returns the last element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromBack() {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is currently empty. Can not complete removing data from back.");
        }
        T holder = tail.getData();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        size--;
        return holder;
    }

    /**
     * Returns the element at the specified index. Don't forget to consider
     * whether traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        T retData;
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("The index provided is out of bounds. It is either less than 0 or "
                    + "greater than or equal to size. Please enter a valid index.");
        }
        if (index > index / 2) {
            int endNum = size - 1;
            DoublyLinkedListNode<T> curr = tail;
            while (endNum > index) {
                curr = curr.getPrevious();
                endNum--;
            }
            retData = curr.getData();
        } else {
            int startNum = 0;
            DoublyLinkedListNode<T> curr = head;
            while (startNum < index) {
                curr = curr.getNext();
                startNum++;
            }
            retData = curr.getData();
        }
        return retData;
    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the list.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        tail = null;
        head = null;
        size = 0;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the list.
     *
     * Must be O(1) if data is in the tail and O(n) for all other cases.
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        T retData = null;
        if (data == null) {
            throw new IllegalArgumentException("The provided data was null. Please enter a valid piece of data.");
        }
        DoublyLinkedListNode<T> curr = tail;
        while (curr != null) {
            if (curr.getData().equals(data)) {
                retData = curr.getData();
                if (size == 1) {
                    size = 0;
                    head = null;
                    tail = null;
                    break;
                }
                if (curr == head) {
                    head = curr.getNext();
                    curr.getNext().setPrevious(null);
                } else if (curr == tail) {
                    tail = curr.getPrevious();
                    curr.getPrevious().setNext(null);
                } else {
                    curr.getPrevious().setNext(curr.getNext());
                    curr.getNext().setPrevious(curr.getPrevious());
                }
                size--;
                break;
            }
            curr = curr.getPrevious();
        }
        if (retData == null) {
            throw new NoSuchElementException("The data was not found in the doubly linked list.");
        }
        return retData;
    }

    /**
     * Returns an array representation of the linked list. If the list is
     * size 0, return an empty array.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length size holding all of the objects in the
     * list in the same order
     */
    public Object[] toArray() {
        T[] retArr = (T[]) new Object[size];
        int myIndex = 0;
        DoublyLinkedListNode<T> curr = head;
        while (curr != null) {
            retArr[myIndex] = curr.getData();
            curr = curr.getNext();
            myIndex++;
        }
        return retArr;
    }

    /**
     * Returns the head node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the list
     */
    public DoublyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the tail of the list
     */
    public DoublyLinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }

    /**
     * Returns the size of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }
    /**
     * Used to set setup first value when the Doubly Linked List is Empty
     *
     * @param data provided by user.
     */
    private void firstNode(T data) {
        DoublyLinkedListNode<T> insertVal = new DoublyLinkedListNode<>(data, null, null);
        head = insertVal;
        tail = insertVal;
        size++;
    }
}


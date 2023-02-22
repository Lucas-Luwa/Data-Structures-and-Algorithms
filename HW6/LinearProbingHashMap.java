import java.util.NoSuchElementException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Your implementation of a LinearProbingHashMap.
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
public class LinearProbingHashMap<K, V> {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private LinearProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public LinearProbingHashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public LinearProbingHashMap(int initialCapacity) {
        size = 0;
        table = (LinearProbingMapEntry<K, V>[]) new LinearProbingMapEntry[initialCapacity];
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the array would violate the max load factor if the data was
     * added. For example, let's say the array is of length 5 and the current
     * size is 3 (LF = 0.6). For this example, assume that no elements are
     * removed in between steps. If another entry is attempted to be added,
     * before doing anything else, you should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so you would trigger a resize before
     * you even attempt to add the data or figure out if it's a duplicate. Be
     * careful to consider the differences between integer and double
     * division when calculating load factor.
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("You've provided an invalid key or value. PLease try again.");
        }
        if ((size + 1.0) / table.length >= MAX_LOAD_FACTOR) {
            resizeBackingTable(2 * table.length + 1);
        }
        int outputVal = linearIndexFinder(key);
        if (table[outputVal] == null || table[outputVal].isRemoved()) {
            table[outputVal] = new LinearProbingMapEntry<>(key, value);
            table[outputVal].setRemoved(false);
            size++;
            return null;
        }
        V savedVal = table[outputVal].getValue();
        table[outputVal].setValue(value);
        return savedVal;
    }

    /**
     * Used to find index of put method.
     * @param k Key being searched for.
     * @return Return found index value.
     */
    private int linearIndexFinder(K k) {
        boolean delMarkerFound = false;
        int numVal = 0;
        int retIndex = -1;
        int curr = Math.abs(k.hashCode() % table.length);
        for (int i = 0; i < table.length; i++) {
            if (table[curr] != null && k.equals(table[curr].getKey()) && !table[curr].isRemoved()) {
                retIndex = curr;
                break;
            }
            if (numVal > (size)) {
                break;
            }
            if (table[curr] == null || k.equals(table[curr].getKey()) && table[curr].isRemoved()) {
                if (!delMarkerFound) {
                    retIndex = curr;
                }
                break;
            }
            if (table[curr].isRemoved() && !delMarkerFound) {
                delMarkerFound = true;
                retIndex = curr;
            }
            if (!table[curr].isRemoved()) {
                numVal++;
            }
            curr = Math.abs((curr + 1) % table.length);
        }
        return retIndex;
    }

    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param key the key to remove
     * @return the value previously associated with the key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("The key you provided is null. Please provide a valid key.");
        }
        int removeVal = findKey(key);
        if (removeVal == -1) {
            throw new NoSuchElementException("The element you are seeking could not be found.");
        }
        V retVal = table[removeVal].getValue();
        //System.out.println(retVal);
        table[removeVal].setRemoved(true);
        size--;
        return retVal;
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("The key you provided is null. Please provide a valid key.");
        }
        int results = findKey(key);
        if (results == -1) {
            throw new NoSuchElementException("The key you are searching for does not exist in the map");
        }
        return table[results].getValue();
    }

    /**
     * Helper function to find keys for me.
     * @param key key being sought after.
     * @return int of index of key.
     */
    private int findKey(K key) {
        int numVal = 0;
        int curr = Math.abs(key.hashCode() % table.length);
        for (int i = 0; i < table.length; i++) {
            if (numVal > size || table[curr] == null) {
                break;
            }
            if (!table[curr].isRemoved() && table[curr].getKey().equals(key)) {
                return curr;
            }
            if (!table[curr].isRemoved()) {
                numVal++;
            }
            curr = Math.abs((curr + 1) % table.length);
        }
        return -1;
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("The key provided is null. Please provide a valid key.");
        }
        int truthVal = findKey(key);
        return truthVal == -1 ? false : true;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        Set<K> retSet = new HashSet<>();
        int numVal = 0;
        for (int i = 0; i < table.length; i++) {
            if (numVal > size) {
                break;
            }
            if (table[i] != null && !table[i].isRemoved()) {
                numVal++;
                retSet.add(table[i].getKey());
            }
        }
        return retSet;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        List<V> myList = new ArrayList<>();
        int numVal = 0;
        for (int i = 0; i < table.length; i++) {
            if (numVal > size) {
                break;
            }
            if (table[i] != null && !table[i].isRemoved()) {
                numVal++;
                myList.add(table[i].getValue());
            }
        }
        return myList;
    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed. 
     * You should NOT copy over removed elements to the resized backing table.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("Can't resize to a value smaller than the size.");
        }
        LinearProbingMapEntry<K, V>[] newTab =
                (LinearProbingMapEntry<K, V>[]) new LinearProbingMapEntry[length];
        int numVal = 0;
        for (int i = 0; i < table.length; i++) {
            if (numVal > size) {
                break;
            }
            if (table[i] != null && !table[i].isRemoved()) {
                int newInd = Math.abs(table[i].getKey().hashCode() % newTab.length);
                //Taken from put
                while (newTab[newInd] != null) {
                    newInd = Math.abs((newInd + 1) % newTab.length);
                }
                newTab[newInd] = table[i];
                numVal++;
            }
        }
        table = newTab;
    }

    /**
     * Clears the map.
     *
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     *
     * Must be O(1).
     */
    public void clear() {
        table = (LinearProbingMapEntry<K, V>[]) new LinearProbingMapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public LinearProbingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}

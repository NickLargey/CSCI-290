/**
 * RaggedArrayList.java
 * ****************************************************************************
 * revision history
 * ****************************************************************************
 * 2.22.2021 - added Add Method
 * 2.15.2021 - added findFront and findEnd Methods
 * 9.25.2020 -- AA cleaning up documentation again
 * 8/2015 - Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 * ****************************************************************************
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator. Duplicates
 * are allowed. New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them
 *
 * @param <E> A generic data type so that this structure can be built with any
 * data type (object)
 */
package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces
    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * ***********************************************************
     * nested class for 2nd level arrays (DONE - do not change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

    // ***********************************************************
    /**
     * total size (number of entries) in the entire data structure (DONE - do
     * not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts: level 1
     * index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int level1Index;

        /**
         * Level 2 index
         */
        public int level2Index;

        /**
         * Parameterized constructor DONE (Do Not Change)
         *
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        /**
         * test if two ListLoc's are to the same location (done -- do not
         * change)
         *
         * @param otherObj the other listLoc
         * @return true if they are the same location and false otherwise
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index
                    && level2Index == other.level2Index;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be one index past the last value in the used level 2 array. Can
         * be used internally to scan through the array for sublist also can be
         * used to implement the iterator.
         */
        public void moveToNext() {
            level2Index++;
            if (level2Index >= ((L2Array) l1Array[level1Index]).numUsed) {
                if (level1Index + 1 < l1NumUsed) {
                    level1Index++;
                    level2Index = 0;
                }
            }
        }
    }

    /**
     * find 1st matching entry
     *
     * @param item the thing we are searching for a place to put.
     * @return a ListLoc object - ListLoc of 1st matching item or of 1st item
     * greater than the item if no match this might be an unused slot at the end
     * of a level 2 array
     */
    // Nick Largey and Caleb Tracey
//    public ListLoc findFront(E item) {
//        if (size == 0) {
//            return new ListLoc(0, 0);
//        }
//        int i = 0;
//        L2Array l2Array = (L2Array) l1Array[i];
//
//        while (i < l1NumUsed - 1 && comp.compare(item, l2Array.items[l2Array.numUsed - 1]) > 0) {
//            i++;
//            l2Array = (L2Array) l1Array[i];
//        }
//
//        int l2index = 0;
//        while (l2index <= l2Array.numUsed - 1
//                && comp.compare(item, l2Array.items[l2index]) > 0) {
//            l2index++;
//        }
//        
//        return new ListLoc(i, l2index);
//
//    }
    public ListLoc findFront(E item) {
        ListLoc frontLL = new ListLoc(0, 0);
        //  empty list ?
        if (size > 0) {
            int i1 = 0;
            L2Array l2Array = (L2Array) l1Array[i1];
            // linear search of l1 until we find the right l2 array
            while (i1 < l1NumUsed - 1
                    && comp.compare(item, l2Array.items[l2Array.numUsed - 1]) > 0) {
                i1++; // this could get us to NumUsed-1 but not above
                l2Array = (L2Array) l1Array[i1];
            }
            // find the position within the l2Array
            int i2 = 0;
            while (i2 < l2Array.numUsed - 1
                    && comp.compare(item, l2Array.items[i2]) > 0) {
                // stop at first match or larger value
                i2++;
            }
            // can it go at the end of the previous row?
            boolean can = i1 > 0;
            // is it the same as the first element?
            if (i2 == 0 && can
                    && comp.compare(item, ((L2Array) l1Array[i1]).items[i2]) != 0) {
                frontLL = new ListLoc(i1 - 1, ((L2Array) l1Array[i1 - 1]).numUsed);
            } else if (comp.compare(item, l2Array.items[i2]) <= 0) {
                frontLL = new ListLoc(i1, i2);
            } else {
                frontLL = new ListLoc(l1NumUsed - 1,
                        ((L2Array) l1Array[l1NumUsed - 1]).numUsed);
            }
        }
        return frontLL;
    }

    /**
     * find location after the last matching entry or if no match, it finds the
     * index of the next larger item this is the position to add a new entry
     * this might be an unused slot at the end of a level 2 array
     *
     * @param item the thing we are searching for a place to put.
     * @return a ListLoc object - the location where this item should go
     */
    // Nick Largey and Caleb Tracey
//    public ListLoc findEnd(E item) {
//        if (size == 0) {
//            return new ListLoc(0, 0);
//        }
//
//        int i = l1NumUsed - 1;
//        L2Array l2Array = (L2Array) l1Array[i];
//        while (i > 0 && comp.compare(item, l2Array.items[0]) < 0) {
//            i--;
//            l2Array = (L2Array) l1Array[i];
//        }
//
//        int l2index = l2Array.numUsed - 1;
//        while (l2index >= 0
//                && comp.compare(item, l2Array.items[l2index]) < 0) {
//            l2index--;
//        }
//
//        return new ListLoc(i, l2index + 1);
//
//    }
    public ListLoc findEnd(E item) {
        ListLoc endLL = new ListLoc(0, 0);
        if (size > 0) {
            // linear search working backwards
            int i1 = l1NumUsed - 1;
            L2Array l2Array = (L2Array) l1Array[i1];
            while (i1 > 0
                    && comp.compare(item, l2Array.items[0]) < 0) {
                i1--;
                l2Array = (L2Array) l1Array[i1];
            }
            int i2 = l2Array.numUsed - 1;
            while (i2 > 0
                    && comp.compare(item, l2Array.items[i2]) < 0) {
                i2--;
            }
            // if this item is the same as the current one and I'm looking 
            // at the last 
            if (comp.compare(item, l2Array.items[i2]) >= 0
                    && i2 == l2Array.numUsed - 1 // at the end of this l2
                    && i1 < l1NumUsed - 1) {  // there is another l2
                endLL = new ListLoc(i1 + 1, 0);
            } else if (comp.compare(item, l2Array.items[i2]) >= 0) {
                endLL = new ListLoc(i1, i2 + 1);
            } else {
                endLL = new ListLoc(i1, i2);
            }
        }
        return endLL;
    }

    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     *
     * @param item the thing we are searching for a place to put.
     * @return true once item is added and arrays are scaled if necessary
     */
    public boolean add(E item) {
        // Create variable containing where item should be iserted
        ListLoc insertionPos = findEnd(item);

        // Create instance of L2 array from L1 index
        L2Array l2Array = (L2Array) l1Array[insertionPos.level1Index];
        // Create variable of item's L2 index
        int pos = insertionPos.level2Index;

        // Create space for insertion
        System.arraycopy(l2Array.items, pos,
                l2Array.items, pos + 1,
                l2Array.numUsed - pos);

        // Insert item
        l2Array.items[pos] = item;
        // Inc L2 numUsed
        l2Array.numUsed++;
        // Inc size
        size++;

        // If there's still room in the L2 array, we can return true
        if (l2Array.numUsed < l2Array.items.length) {
            return true;
        }

        // If the L2 length is less than the L1 length, double L2's length 
        if (l2Array.items.length < l1Array.length) {
            l2Array.items = Arrays.copyOf(l2Array.items, l2Array.numUsed * 2);

            // Else split L2 array
        } else {
            // Inc L1 num used
            l1NumUsed++;
            // Create space to insert split L2 array in L1
            System.arraycopy(l1Array, insertionPos.level1Index,
                    l1Array, insertionPos.level1Index + 1,
                    (l1Array.length - 1) - (insertionPos.level1Index));

            // Create new array for first half of L2 array to be split 
            L2Array newSplitL1 = new L2Array(l1Array.length);
            // Set numUsed to half of L1 array
            newSplitL1.numUsed = l1Array.length / 2;

            // Create new array for second half of L2 array to be split
            L2Array newSplitL2 = new L2Array(l1Array.length);
            // Set numUsed to half of L1 array
            newSplitL2.numUsed = l1Array.length / 2;

            // Copy first half of L2 array into first new array 
            System.arraycopy(l2Array.items, 0, newSplitL1.items,
                    0, newSplitL1.numUsed);
            // Copy second half of L2 array into second new array
            System.arraycopy(l2Array.items, newSplitL2.numUsed, newSplitL2.items,
                    0, newSplitL2.numUsed);

            // insert first new array into L1 
            l1Array[insertionPos.level1Index] = newSplitL1;
            // Insert second new array into L1
            l1Array[insertionPos.level1Index + 1] = newSplitL2;
        }

        // If L1 array is full, double L1 length
        if (l1NumUsed == l1Array.length) {
            l1Array = Arrays.copyOf(l1Array, l1Array.length * 2);
        }
        return true;
    }

    /**
     * check if list contains a match
     *
     * @param item the thing we are looking for.
     * @return true if the item is already in the data structure
     */
    public boolean contains(E item) {

        ListLoc findPos = findFront(item);
        L2Array l2Array = (L2Array) l1Array[findPos.level1Index];
        int pos = findPos.level2Index;

        if (l2Array.items[pos].equals(item)) {
            return true;
        }

        return false;
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {

        if (a.length != size) {
            System.out.println("New array must be a larger size");
        } else {
            Iterator<E> it = iterator();
            int i = 0;
            do {
                a[i] = (E) it.next();
                i++;
            }
            while (it.hasNext());            
                       
        }
        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive. The original list is
     * unaffected findStart and findEnd will be useful here
     *
     * @param fromElement the starting element
     * @param toElement the element after the last element we actually want
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        // Create ListLoc object for fromElement
        ListLoc from = findFront(fromElement);
        // Create ListLoc object for toElement
        ListLoc to = findFront(toElement);

        // Create empty RaggedArrayList to store sublist and return
        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);

        // Loop over RaggedArrayList until to element is reached
        while (!from.equals(to)) {
            // Set index of current element to be stored
            L2Array fromL2 = (L2Array) l1Array[from.level1Index];
            // Add element to result
            result.add(fromL2.items[from.level2Index]);
            // Move ahead to the next element
            from.moveToNext();
        }
        // return completed list
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     *
     * @return an iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iterator is just a list loc. It starts at (0,0) and finishes with index2
     * 1 past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check if more items
         */
        @Override
        public boolean hasNext() {
            return loc.level2Index < ((L2Array) l1Array[loc.level1Index]).numUsed
                    || loc.level1Index < l1NumUsed - 1;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list
         */
        public E next() {
            L2Array l2Array = (L2Array) l1Array[loc.level1Index];
            
            
            if (loc.level2Index >= l2Array.numUsed) {
                throw new IndexOutOfBoundsException();
            }
            E ret = l2Array.items[loc.level2Index]; 
            
            
            loc.moveToNext();
            
            return ret;
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

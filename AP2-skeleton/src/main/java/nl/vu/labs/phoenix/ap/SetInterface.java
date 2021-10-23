package nl.vu.labs.phoenix.ap;

/* [1] Set Specification
 * 	   -- Complete the specification for a set interface.
 * 		  See the List interface for inspiration
 */

// value in hashmap<K,V> ie the value of the variable

/** @elements
 *    objects of type T extends Comparable<T>
 *  @structure
 *    no structure
 *  @domain
 *    all possible elements that are unique
 *  @constructor
 *    There is a default constructor that creates an empty set
 *  @precondition
 *    --
 *  @postcondition
 *    The new Set-object is an empty set has been created
 *
 **/

public interface SetInterface<T extends Comparable<T>> {
	
	/* 
	 * [2] Mandatory methods. Make sure you do not modify these!
	 * 	   -- Complete the specifications of these methods
	 */
	
	/**
	 * Hint:
	 * 
	 * @precondition
	 *  
	 * @postcondition
	 * 
	 * @return
	 * 	true  - element was inserted
	 * 	false - element was already present 
	 */
	boolean add(T t); 
	
    /** @precondition
     *    Set is not empty
     *  @postcondition
     *    A random element in Set has been returned
     **/
	
	T get();
	
    /** @precondition
     *    Set is not empty
     *  @postcondition
     *    TRUE - element was removed
	 * 	  FALSE - element was already not presented
     **/
	
	boolean remove(T t);
	
    /** @precondition
     *      --
     *  @postcondition
     *    number of elements in set has been returned
     **/
	
	int size();
	
    /** @precondition
     *      --
     *  @postcondition
     *    A copy of the set has been returned
     **/
	
	SetInterface<T> copy();
	
	/*
	 * [3] Methods for set operations 
	 * 	   -- Add methods to perform the 4 basic set operations 
	 * 		  (union, intersection, difference, symmetric difference)
	 */

    /** @precondition
     *      --
     *  @postcondition
     *    union of both sets has been returned
     **/

	SetInterface<T> union(SetInterface<T> set); // use in class Set 
	
    /** @precondition
     *      --
     *  @postcondition
     *    all elements contained in both sets have been returned
     **/
	
	SetInterface<T> intersection(SetInterface<T> set);	
	
    /** @precondition
     *      --
     *  @postcondition
     *    all elements contained in the 1st but not the 2nd set have been returned
     **/
	
	SetInterface<T> difference(SetInterface<T> set);
	
    /** @precondition
     *      --
     *  @postcondition
     *    symdiff of both sets has been returned
     **/
	
	SetInterface<T> symdiff(SetInterface<T> set);
	
	/* 
	 * [4] Add anything else you think belongs to this interface 
	 */
	
	// your code here
	
	/** @precondition
     *      --
     *  @postcondition
     *    TRUE - an element in the set has the same content as t
     *    FALSE - no element in the set has the same content as t
     **/
	
	boolean containsElement (T t);
	
	/** @precondition
     *      --
     *  @postcondition
     *    set is empty
     **/
	
	void init();
	
	/** @precondition
     *      --
     *  @postcondition
     *    TRUE - size of set is 0
     *    FALSE - size of set is not 0
     **/
	
	boolean isEmpty();
	
}

package nl.vu.labs.phoenix.ap;

/* [1] Identifier Specification
 * 	   -- Complete the specification for an Identifier interface.
 * 		  See the List interface for inspiration
 */

//An identifier starts with a letter and only consists of letters and numbers.

// this is key in hashmap<K,V> ie variable from  input

/** @elements
 *    characters of the type char
 *  @structure
 *    linear
 *  @domain
 *    all possible rows of alphanumeric characters starting with a letter
 *  @constructor
 *    There is a default constructor that creates an identifier starts with an unknown letter and contains only letters and numbers
 *  @precondition
 *    --
 *  @postcondition
 *    The new Identifier-object is an identifier starts with an unknown letter
 *
 **/

public interface IdentifierInterface {
	/* 
	 * [2] Mandatory methods. Make sure you do not modify these!
	 * 	   -- Complete the specifications of these methods
	 */
	
    /** @precondition
     *      --
     *  @postcondition
     *    String-obj of the content of identifier has been returned
     **/
	
	String value(); 
	
	/* 
	 * [3] Add anything else you think belongs to this interface 
	 */
	
    /** @precondition
     *    char c is alphanumeric letter
     *  @postcondition
     *    char c has been added to index 0 and length is 1
     **/
	
	IdentifierInterface init(char c);
	
	/** @precondition
     *     --
     *  @postcondition
     *    The length of the identifier has been returned
     **/
		
	int size();
	
	/** @precondition
     *    char c is alphanumeric character
     *  @postcondition
     *    char c has been added to the identifier 
     *    size of the identifier grows by 1
     **/	
	
	IdentifierInterface add(char c);
	
	/** @precondition
     *    --
     *  @postcondition
     *    TRUE - this and obj have the same content
     *    FALSE - this and obj do not have the same content or
     *    		obj is not of the type identifier or
     *    		obj is null
     **/	
	
	boolean equals (Object obj); // compare the values of identifiers, override built in equals()
	
	/** @precondition
     *    --
     *  @postcondition
     *    hashCode of the Identifier has been return
     **/	

	int hashCode();	// override
}

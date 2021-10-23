package assignment1;

interface SetInterface {

	/*
	 *
	 * Elements: objects of the type Identifiers
	 * Structure: none 
	 * Domain: all possible sets of minimum 0 and maximum MAX_NUMBER_OF_ELEMENTS
	 *
	 * constructors
	 *
	 * Set();
	 *   PRE  - 
	 *   POST -A new Set-object has been made and contains the empty set.
	 *
	 * Set (Identifiers src);
	 *   PRE  - 
	 *   POST - A new Set-object has been made and contains a copy of the value of src.
	 *
	 */	
	
	int MAX_NUMBER_OF_ELEMENTS = 20;

	void init();
	/* PRE - 
	 * POST - Set is empty
	 */

	int numberOfElements (); // number of element in a set
	/* PRE - 
	 * POST - Number of elements in the set has been returned
	 */

	boolean  isEmpty();
	/* PRE - 
	 * POST - true: number of elements is 0
	 		  false: number of elements is larger than 0
	 */

	boolean containIdentifier (Identifier identifier); 
	/* PRE - 
	 * POST - true: an identifier in the set has the same content as identifier
	 *		  false: no identifiers in the set has the same content as identifier
	 */

	void addIdentifier (Identifier identifier) throws Exception ; // duplicate
	/* PRE - 
	 * POST - true: a copy of identifier is present in the set
	 *		  false: set was full and identifier was not a member of the set
	 */
	
	void removeIdentifier (Identifier identifier); 
	/* PRE - 
	 * POST - identifier was not in the set
	 */
	
	void removeFirst(); // implies set has structure should be private in set implementation 
	/* PRE - Set is not empty
	 * POST - The 1st identifier is removed from the set
	 */
	
	void removeLast(); // implies set has structure should be private in set implementation 
	/* PRE - Set is not empty
	 * POST - The latest identifier is removed from the set
	 */

	Identifier getElement();
	/* PRE - set is not empty
	 * POST - a copy of a random identifier has been returned 
	 */
	
	Set calculateDifference (Set set) throws Exception; // don't need to throw exception
	/* PRE - 
	 * POST - all elements contained in the 1st but not the 2nd set have been returned
	 */
	
	Set calculateIntersection (Set set) throws Exception; // don't need to throw exception
	/* PRE -
	 * POST - all elements contained in both sets have been returned
	 */
	
	Set calculateUnion (Set set) throws Exception; 
	/* PRE - 
	 * POST - true: union of both sets has been returned
	 * 		false: union of both sets exceed MAX_NUMBER_OF_ELEMENTS
	 */
	
	Set calculateSymDiff (Set set) throws Exception;
	/* PRE - 
	 * POST -  true: symdiff of both sets has been returned
	 * 		false: symdiff of both sets exceed MAX_NUMBER_OF_ELEMENTS
	 */
}

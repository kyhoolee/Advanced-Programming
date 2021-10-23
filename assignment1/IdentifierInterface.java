package assignment1;

interface IdentifierInterface {

	/*
	 *
	 * Elements: characters of the type char
	 * Structure: linear
	 * Domain: all possible rows of alphanumeric characters // starting w a letter
	 *
	 * constructors
	 *
	 * Identifier();
	 *   PRE  - 
	 *   POST -A new Identifier-object has been made and contains at least 1 char.
	 *
	 * Identifiers (Identifier src);
	 *   PRE  - 
	 *   POST - A new Identifier-object has been made and it contains a copy of the value of src.
	 *
	 */

	Identifier init (char c); 
	/* PRE - char is alphanumeric letter
	 * POST - char c has been added to index 0 and length is 1
	 */

	void add (char c);
	/* PRE - char is alphanumeric
	 * POST - char c has been added to index length
	 */

	char getCharAt​(int index);
	/* PRE - 0 <= index <= length() -1 
	 * POST - the character at index has been returned
	 */

	int length();
	/* PRE - 
	 * POST - The length of the identifier has been returned
	 */

	boolean isIdentical(Identifier identifier); // compare the content of 2 identifiers
	/* PRE - 
	 * POST - true:  identifier has the same sequence of char
		      false: identifier does not have the same sequence of char
	 */

}


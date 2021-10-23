package assignment1;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.PrintStream;

public class Program {

	static final char START_SET = '{';//
	static final char END_SET = '}';//
	static final String WRONG_IDENTIFIER_EXCEPTION = "Wrong input: identifier must start with letter and contain alphanumeric characters";
	static final String END_OF_INPUT_EXCEPTION = "No input allowed after '}'";
	static final String EMPTY_STRING = "";	//
	static final String SPACE = " "; //
	static final String EXCEEDED_MAX_NUMBER_OF_IDENTIFIERS = "Input contains too many identifiers. Maximum number of identifiers is 10";
	static final int MAX_IDENTIFIERS = 10; //
	static final String QUESTION_1 = "Give first set : ";
	static final String QUESTION_2 = "Give second set : ";
	PrintStream out;

	Program() {
		out = new PrintStream (System.out);
	}                                

	void checkStart (Scanner input,char c) throws Exception { // one general method for check start and end 
		if (nextChar(input) != c) {
			throw new Exception (String.format("Missing %c", c));
		}
	}

	void checkEnd (Scanner input,char c) throws Exception {
		if (!nextCharIs(input,c)) {
			throw new Exception (String.format("Missing %c", c));
		}
	}

	void checkEndOfSet (Scanner input) throws Exception { // remane checkEndOFLine
		if (input.hasNext()) {
			throw new Exception (END_OF_INPUT_EXCEPTION);
		}
	}

	char nextChar (Scanner in) { // read next character from input
		return in.next().charAt(0);
	}

	boolean nextCharIs(Scanner in, char c) { // Method to check if the next character to be read when calling nextChar() is equal to the provided character
		return in.hasNext(Pattern.quote(c+""));
	}

	boolean nextCharIsDigit (Scanner in) { // Method to check if the next character to be read when calling nextChar() is a digit.
		return in.hasNext("[0-9]");
	}

	boolean nextCharIsLetter (Scanner in) { // Method to check if the next character to be read when calling nextChar() is a letter
		return in.hasNext("[a-zA-Z]");
	}

	String printSet (Set set) { // split to print set  + print identifier
		StringBuffer result = new StringBuffer();
		Identifier id = new Identifier();
		Set generated = new Set();

		try {
			if (set.isEmpty() == true) { //get element and remove everytime
				return EMPTY_STRING;
			} else {
				while (generated.numberOfElements() != set.numberOfElements()) {
					id = set.getElement();

					while (!generated.containIdentifier(id)) {
						generated.addIdentifier(id);

						for (int j = 0; j < id.length(); j++) {
							result.append(id.getCharAt​(j));
						}	

						if (generated.numberOfElements() == set.numberOfElements()) {
							break;
						}
						result.append(SPACE);
					}
				}
			}
		}
		catch (Exception e) {
			out.println(e);
		}
		return result.toString();
	}

	Set checkSet (Scanner input, Set set) throws Exception{ 
		Identifier identifier;

		while (!nextCharIs(input,END_SET)) {
			while (input.hasNext(SPACE)) {
				nextChar(input);
			}

			if (!input.hasNext()) {
				throw new Exception (String.format("Missing %c", END_SET));
			}

			if (!nextCharIsLetter(input)) {
				throw new Exception (WRONG_IDENTIFIER_EXCEPTION);
			} 

			identifier = new Identifier();
			identifier.init(nextChar(input));

			while (nextCharIsLetter(input) || nextCharIsDigit(input))	{ // new method readIdentifier
				identifier.add(nextChar(input));
			}
			set.addIdentifier(identifier);

			if (set.numberOfElements() > MAX_IDENTIFIERS) {
				throw new Exception (EXCEEDED_MAX_NUMBER_OF_IDENTIFIERS); 
			}

			while (input.hasNext(SPACE)) { // new method 
				nextChar(input);
			}
		}
		return set;
	}

	boolean inputContainsCorrectSet (Scanner input, Set set){ 
		String line = input.nextLine();
		Scanner lineScanner = new Scanner(line).useDelimiter(EMPTY_STRING);
		boolean result = true;

		try {			
			set.init();

			if(!lineScanner.hasNext()) {
				return result = false;
			}

			checkStart(lineScanner,START_SET);

			checkSet(lineScanner,set);

			checkEnd(lineScanner,END_SET);
			nextChar(lineScanner);

			checkEndOfSet(lineScanner); 

		} catch (Exception e) {
			out.println(e);
			result = false;
		}
		return result;
	}

	boolean askSet (Scanner input, String question, Set set) {
		do {
			out.printf("%s", question); 
			if (! input.hasNextLine()) {
				out.printf("\n"); // otherwise line with ^D will be overwritten
				return false;
			}
		} while (! inputContainsCorrectSet(input, set)) ;
		return true;
	}

	boolean askBothSets (Scanner input, Set set1, Set set2) {
		return askSet(input, QUESTION_1, set1) && 
				askSet(input, QUESTION_2, set2) ;
	}

	void calculateAndGiveOutput (Set set1, Set set2) {
		try {
			out.printf("Difference: {%s}\n", printSet(set1.calculateDifference(set2))); 
			out.printf("Intersection: {%s}\n", printSet(set1.calculateIntersection(set2)));
			out.printf("Union: {%s}\n", printSet(set1.calculateUnion(set2)));
			out.printf("Symmetric Difference: {%s}\n", printSet(set1.calculateSymDiff(set2)));
		}
		catch (Exception exception) {
			out.println(exception);
		}
	}

	void start () {
		Scanner in = new Scanner(System.in).useDelimiter(EMPTY_STRING);
		Set set1 = new Set(),
				set2 = new Set();

		while (askBothSets(in, set1, set2)) {
			calculateAndGiveOutput(set1, set2);
		}
	}

	public static void main (String[] args) {
		new Program().start();
	}
}
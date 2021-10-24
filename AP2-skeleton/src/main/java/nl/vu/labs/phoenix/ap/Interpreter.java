package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {
	static int LOG_LEVEL = 0;
	static void log(String msg) {
		String tab = "";
		for(int i = 0; i < LOG_LEVEL ; i ++) {
			tab += "  ";
		}
		System.out.println(tab + msg);
	}

	private HashMap<Identifier, T> map;
	private static final String SPACE = " ";

	public Interpreter () {
		map = new HashMap<Identifier, T>();
	}

	/**
	 * Retrieve the value of an identifier from the memory
	 * @param var 		value of an Identifier
	 * @return 
	 * 	the set corresponding to var or null
	 */

	@Override
	public T getMemory(String v) {
		/*Scanner keyScannet = new Scanner (v);
		Identifier key = null;
		try {
		 key = identifier(new Scanner (v));
		} catch(APException e) {
			System.out.println(e);
		}*/
		return map.get(v);
		//return eval(v);
	}	

	/**
	 * Evaluate a line of input
	 * @param s 		an expression
	 * @return
	 * 	if the statement is a print return the corresponding set
	 * 	otherwise return null. also return null when an exception occurs (after printing it out!)
	 */

	@Override
	public T eval(String s) {
		Scanner stringScanner = new Scanner (s).useDelimiter("");
		T result = null;

		ignoreInput(stringScanner, SPACE);

		try {
			result = statement(stringScanner); 
		} catch (APException e){
			result = null;
			System.out.println(e);
		}
		return result;
	}

	private void ignoreInput (Scanner in, String s) {
		while (in.hasNext(s)) {
			nextChar(in);
		}
	}

	private T statement(Scanner statementScanner) throws APException {
		log("statement");
		LOG_LEVEL ++;

		ignoreInput(statementScanner, SPACE);

		if (nextCharIsLetter(statementScanner)) { 
			return assignment(statementScanner);
		} else if (nextCharIs(statementScanner, '?')) {
			nextChar(statementScanner);
			return printStatement(statementScanner);
		} else if (nextCharIs(statementScanner, '/')) {
			nextChar(statementScanner);
			return comment(statementScanner);
		} else {
			throw new APException ("Invalid input\n");
		}
	}

	private T assignment (Scanner assignmentScanner) throws APException { // char by char
		// assign an expression to an identifier
		// expression should be evaluated and return a set

		log("assignment");
		LOG_LEVEL ++;

		T set = null;
		Identifier identifier = null;
		StringBuffer help = new StringBuffer();

		ignoreInput(assignmentScanner, SPACE);

		while (assignmentScanner.hasNext()) {
			// 1. Get identifier 
			identifier = identifier(assignmentScanner);
			log("Identifier: " + identifier.value());
			// 2. Pass = character
			nextChar(assignmentScanner);
			// 3. Get expression value
			set = expression(assignmentScanner);
			//log("expression: " + setValue(set));
		}	
		map.put(identifier, set);

		LOG_LEVEL --;
		log("done assign");
		return null;
	}

	private T printStatement(Scanner printStatementScanner) throws APException {
		// print the value of expression
		// expression can be either an identifier or expression
		T set;

		ignoreInput(printStatementScanner, SPACE);

		if (printStatementScanner.hasNext()) {
			//1. Evaluate set need to print
			set = expression(printStatementScanner);
		} else {
			throw new APException("Invalid statement\n");
		}
		return set;
	}

	private String setValue (T set) {
		StringBuffer result = new StringBuffer();

		if (set.isEmpty()) {
			result.append('{');
			result.append('}');
		}else {
			result.append('{');
			result.append(set.get());
			set.remove(set.get());
			while(!set.isEmpty()){
				result.append(", ");
				result.append(set.get());
				set.remove(set.get());
			}
			result.append('}');
		}
		return result.toString();
	}

	private T comment(Scanner commentScanner) {
		return null;
	}

	private Identifier identifier (Scanner identifierScanner) throws APException {

		log("identifier");
		LOG_LEVEL ++;

		Identifier result = new Identifier();

		// 1. Init with first letter from input
		result.init(nextChar(identifierScanner));

		while (identifierScanner.hasNext()) {
			// 2. Check more char in Identifier
			if (nextCharIsLetter(identifierScanner) || nextCharIsDigit(identifierScanner)) {
				result.add(nextChar(identifierScanner));
				continue;
			} else  {
				// 3. Check spaces in identifier and = char
				while (nextCharIs(identifierScanner,' ')) {
					nextChar(identifierScanner);
				}
				if (nextCharIs(identifierScanner,'=')) {
					break;
				} else throw new APException ("Invalid Identifier. Identifier must start with a letter and contain only letters or natural numbers.");
			}
		}

		log("Identifier: " + result.value());
		LOG_LEVEL --;
		log("done identifier");
		return result;
	}

	private T expression (Scanner expressionScanner) throws APException {
		/* read terms separated by + - or |
		 * calculate expression from left to right
		 * there has to be at least 1 term
		 * +: union
		 * - difference
		 * | symdiff
		 */

		log("expression");
		LOG_LEVEL ++;

		ignoreInput(expressionScanner, SPACE);

		// 1. read first term 
		T result = term(expressionScanner);

		ignoreInput(expressionScanner, SPACE);

		// 2. Check other terms if available
		while (expressionScanner.hasNext()) {
			// 3. Check operators
			if (nextCharIs(expressionScanner, '+') || nextCharIs(expressionScanner, '-') || nextCharIs(expressionScanner, '|')) {
				char operator = nextChar(expressionScanner);
				// 4. Calculate and update result
				result = calculate(result, expression (expressionScanner), operator);
			} else {
				throw new APException ("No operator detected\n");
			}
		}

		log("Set: " + setValue(result));
		LOG_LEVEL --;
		log("done expression");

		return result;
	}

	private T calculate (T term1, T term2, char c) {
		T result = null;

		if (c == '+') {
			result = (T) term1.union(term2);
		} else if (c == '-') {
			result = (T) term1.difference(term2);
		} else if (c =='|') {
			result = (T) term1.symdiff(term2);
		}
		return result;
	}

	private T term (Scanner termScanner) throws APException {
		/* read factor separated by *
		 * calculate term from left to right
		 * should contain at lease 1 factor
		 * *: intersection
		 */

		log("term");
		LOG_LEVEL ++;

		ignoreInput(termScanner, SPACE);

		// 1. read first factor
		T result = factor(termScanner);

		ignoreInput(termScanner, SPACE);

		// 2. Check other factors if available
		while (termScanner.hasNext()) {
			if (nextCharIs(termScanner, '*')) { // factors separated by *
				// 3. calculate and update result
				result = (T) result.intersection(term(termScanner));
			} else {
				throw new APException ("No operator detected");
			}
		}

		//log("Term:" + setValue(result));
		LOG_LEVEL --;
		log("done term");

		return result;
	}

	private T factor (Scanner factorScanner) throws APException {
		/* factor () reads , if possible , a correct factor of the input .
		If this succeeds this factor is evaluated and the resulting
		set is returned .
		If this fails , then an error - message is given and a
		APException is thrown .
		 */

		log("factor");
		LOG_LEVEL ++;
		T result;

		ignoreInput(factorScanner,SPACE);

		if (nextCharIsLetter (factorScanner)) { // Check if factor is identifier
			// 1. read an identifier
			Identifier id = identifier(factorScanner); 

			// 2. retrieve the set that belongs with that identifier
			if (map.containsKey(id)) { 
				result = getMemory(id.value());
			} else {
				throw new APException(String.format("Identifier %s does not correspond to a set\n", id.value()));
			}
		}
		else if (nextCharIs(factorScanner, '{')) { // Check if factor is set
			// 3. read a set
			nextChar(factorScanner);
			result = set(factorScanner);
		}
		else if (nextCharIs(factorScanner, '(')) { // Check if factor is complex factor
			// 4. determine the set that is the result of the complex factor
			nextChar(factorScanner);
			result = complexFactor(factorScanner);
		}
		else {
			throw new APException ("Invalid input. Factor syntax is wrong.\n");
		}

		//log("Factor:" + setValue(result));
		LOG_LEVEL --;
		log("done factor");

		return result;
	}

	private T complexFactor (Scanner complexFactorScanner) throws APException {
		/* complexFactor () reads , if possible , a correct expression from the input between '(' and ')'.
		If this succeeds this expression is evaluated and the resulting
		set is returned .
		If this fails , then an error - message is given and a
		APException is thrown .

		this method will employ recursive descent it will then call the method expression() 
		and inside expression,term() factor() and possibly complexFactor ís called again.
		 */

		/* Corner case: complex factor looks like this () 
		 * Exception or not?
		 */

		log("complex factor");
		LOG_LEVEL ++;

		T result = null;
		// 1. Check if there is factor to read
		while (!nextCharIs(complexFactorScanner, ')')) { 
			ignoreInput(complexFactorScanner, SPACE);
			if (!complexFactorScanner.hasNext()) {
				throw new APException("Missing ')' in one or more complex factor\n");
			}
			// 2. Evaluate complex factor
			result = expression(complexFactorScanner);

			ignoreInput(complexFactorScanner, SPACE);
		}

		// 3. Check end of factor
		if (!nextCharIs(complexFactorScanner, ')')) {
			throw new APException("Missing ')' in one or more complex factor\n");
		} 
		nextChar(complexFactorScanner);

		log("Complex factor:" + setValue(result));
		LOG_LEVEL --;
		log("done complex factor");

		return result;
	}

	private T set (Scanner setScanner) throws APException {
		log("set");
		LOG_LEVEL ++;

		T result;
		// 1. Read open set

		ignoreInput(setScanner, SPACE);

		// 2. Read set contents
		result = naturalNumberRow(setScanner);

		// 3. check & read closing set }		
		if (!nextCharIs(setScanner,'}')) { 
			throw new APException (String.format("Missing %c\n", '}'));
		}
		nextChar(setScanner);

		// 3. check end of set, no other elements after closing
		if (setScanner.hasNext()) { 
			throw new APException ("No elements allowed after '}'\n");
		}

		//log("Set:" + setValue(result));
		LOG_LEVEL --;
		log("done set");

		return result;
	}	

	private T naturalNumberRow(Scanner rowScanner) throws APException {
		SetInterface<BigInteger> result = new Set<BigInteger>();
		result.init();
		StringBuffer bigInt;
		BigInteger num;
		log("row");
		LOG_LEVEL ++;

		// 1. Check if there is factor to read
		// empty sets are allowed
		while (!nextCharIs(rowScanner,'}')) {
			ignoreInput(rowScanner, SPACE);

			if (!rowScanner.hasNext()) {
				throw new APException (String.format("Missing %c\n", '}'));
			}

			/* corner case: {1,2,3,,5}
			 * need to check this and give exception
			 */

			// negative number must have a '-' -> check this 
			ignoreInput(rowScanner, SPACE);

			// 2. Read contents of the set
			while(rowScanner.hasNext() && !nextCharIs(rowScanner,'}')) {
				ignoreInput(rowScanner, SPACE);
				// 2.1 Read Big Integer
				if (nextCharIs(rowScanner, ',')) {
					nextChar(rowScanner);
				}
				while (rowScanner.hasNext(" ")) {
					nextChar(rowScanner);
				}
				
				result.add(new BigInteger(naturalNumber(rowScanner)));

				ignoreInput(rowScanner, SPACE);
			}

			ignoreInput(rowScanner, SPACE);

		}
		System.out.println(setValue((T)result));

		//log("row" + setValue((T)result));
		LOG_LEVEL --;
		log("done row");

		return (T) result;
	}

	private String naturalNumber (Scanner numberScanner) throws APException {
		log("number");
		LOG_LEVEL ++;

		StringBuffer num = new StringBuffer();
		while (numberScanner.hasNext()) {
			ignoreInput(numberScanner, SPACE);
			
			if (nextCharIsDigit(numberScanner) && !nextCharIs(numberScanner, '0')) {
				num.append(nextChar(numberScanner));
			} else {
				throw new APException ("number cannot start with '0'");
			}
			
			while (nextCharIsDigit(numberScanner)) {
				num.append(nextChar(numberScanner));
			} 
			
			ignoreInput(numberScanner, SPACE);
			
			if (nextCharIs(numberScanner, ',') || nextCharIs(numberScanner, '}') ) {
				break;
			} else throw new APException("Invalid number in Set. Set can only consist of natural numbers");
		}

		log("Number:" + num.toString());
		LOG_LEVEL --;
		log("done number");

		return num.toString();
	}

	private char nextChar (Scanner in) { // read next character from input
		return in.next().charAt(0);
	}

	boolean nextCharIs(Scanner in, char c) { // Method to check if the next character to be read when calling nextChar() is equal to the provided character
		return in.hasNext(Pattern.quote(c+""));
	}

	boolean nextCharIsDigit (Scanner in) { // Method to check if the next character to be read when calling nextChar() is a digit.
		return in.hasNext("[0-9]");
	}

	private boolean nextCharIsLetter (Scanner in) { // Method to check if the next character to be read when calling nextChar() is a letter
		return in.hasNext("[a-zA-Z]");
	}
}

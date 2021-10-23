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
	private static final char SPACE = ' ';

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
		return map.get(v);
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
			System.out.print(e);
		}
		return result;
	}

	private void ignoreInput (Scanner in, char c) {
		while (nextCharIs(in,c)) {
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
		// parse id
		// check =
		// parse expression

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
		}	
		map.put(identifier, set);

		LOG_LEVEL --;
		log("done assing");
		return null;
	}

	private T printStatement(Scanner printStatementScanner) throws APException {
		// print the value of expression
		// expression can be either an identifier or expression
		T set;

		ignoreInput(printStatementScanner, SPACE);

		if (printStatementScanner.hasNext()) {
			set = expression(printStatementScanner);
		} else {
			throw new APException("Invalid statement\n");
		}
		return set;
	}

	private T comment(Scanner commentScanner) {
		return null;
	}

	private Identifier identifier (Scanner identifierScanner) throws APException {
		Identifier result = new Identifier();

		result.init(nextChar(identifierScanner));

		while (identifierScanner.hasNext()) {
			if (nextCharIsLetter(identifierScanner) || nextCharIsDigit(identifierScanner)) {
				result.add(nextChar(identifierScanner));
				continue;
			} else  {
				while (nextCharIs(identifierScanner,' ')) {
					nextChar(identifierScanner);
				}
				if (nextCharIs(identifierScanner,'=')) {
					break;
				} else throw new APException ("Invalid Identifier. Identifier must start with a letter and contain only letters or natural numbers.");
			}
		}
		System.out.println(result.value());

		return result;
	}

	private T expression (Scanner expressionScanner) throws APException {
		/* read terms separated by + - or |
		 * calculate expression from left to right
		 * +: union
		 * - difference
		 * | symdiff
		 */

		System.out.println("expression");

		ignoreInput(expressionScanner, SPACE);
		
		T result = term(expressionScanner);

		ignoreInput(expressionScanner, SPACE);

		while (expressionScanner.hasNext()) {
			if (nextCharIs(expressionScanner, '+') || nextCharIs(expressionScanner, '-') || nextCharIs(expressionScanner, '|')) {
				char operator = nextChar(expressionScanner);
				result = calculate(result, expression (expressionScanner), operator);
			} else {
				throw new APException ("No operator detected\n");
			}
		}

		System.out.println("done expression");

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
		 * *: intersection
		 */

		System.out.println("term");

		ignoreInput(termScanner, SPACE);
		
		T result = factor(termScanner);

		ignoreInput(termScanner, SPACE);
		
		while (termScanner.hasNext()) {
			if (nextCharIs(termScanner, '*')) {
				result = (T) result.intersection(term(termScanner));
			} else {
				throw new APException ("No operator detected");
			}
		}

		System.out.println("done term");

		return result;
	}

	private T factor (Scanner factorScanner) throws APException {
		/* factor () reads , if possible , a correct factor of the input .
		If this succeeds this factor is evaluated and the resulting
		set is returned .
		If this fails , then an error - message is given and a
		APException is thrown .
		 */
		T result;
		
		System.out.println("factor");

		ignoreInput(factorScanner,SPACE);
		
		if (nextCharIsLetter (factorScanner)) {
			Identifier id = identifier(factorScanner); //read an identifier

			if (map.containsKey(id.value())) { //retrieve the set that belongs with that identifier
				result = getMemory(id.value());
			} else {
				throw new APException(String.format("Identifier %s does not correspond to a set\n", id.value()));
			}
		}
		else if (nextCharIs(factorScanner, '{')) {
			result = set(factorScanner);
		}
		else if (nextCharIs(factorScanner, '(')) { 
			//determine the set that is the result of the complex factor
			nextChar(factorScanner);
			result = complexFactor(factorScanner);
		}
		else {
			throw new APException ("Invalid input. Factor syntax is wrong.\n");
		}

		System.out.println("done factor");

		return result;
	}

	private T complexFactor (Scanner complexFactorScanner) throws APException {
		/* complexFactor () reads , if possible , a correct expression from the input between '(' and ')'.
		If this succeeds this expression is evaluated and the resulting
		set is returned .
		If this fails , then an error - message is given and a
		APException is thrown .

		this method will employ recursive descent it will then call the method expression() 
		and inside expression,term() factor() and possibly complexFactor �s called again.
		 */

		T result = null;

		while (!nextCharIs(complexFactorScanner, ')')) { // check end factor
			ignoreInput(complexFactorScanner, SPACE);
			if (!complexFactorScanner.hasNext()) {
				throw new APException("Missing ')' in one or more complex factor\n");
			}
			result = expression(complexFactorScanner);
		}

		if (!nextCharIs(complexFactorScanner, ')')) {
			throw new APException("Missing ')' in one or more complex factor\n");
		} 
		nextChar(complexFactorScanner);

		return result;
	}

	private T set (Scanner setScanner) throws APException { // setinterface<BigInt> obj when read set, cast to T when return 
		
		System.out.println("set");
		
		T result;
		
		nextChar(setScanner);

		ignoreInput(setScanner, SPACE);

		// read set
		result = naturalNumberRow(setScanner);

		// check }
		if (!nextCharIs(setScanner,'}')) {
			throw new APException (String.format("Missing %c\n", '}'));
		}
		nextChar(setScanner);

		// check end of set
		if (setScanner.hasNext()) {
			throw new APException ("No elements allowed after '}'\n");
		}
		
		System.out.println("done set");
		
		return result;
	}	

	private T naturalNumberRow(Scanner rowScanner) throws APException {
		SetInterface<BigInteger> result = new Set<BigInteger>();
		result.init();
		StringBuffer bigInt = new StringBuffer();
		
		System.out.println("number row");
		
		while (!nextCharIs(rowScanner,'}')) {
			ignoreInput(rowScanner, SPACE);

			if (!rowScanner.hasNext()) {
				throw new APException (String.format("Missing %c\n", '}'));
			}

			/* corner case: {1,2,3,,5}
			 * need to check this and give exception
			 */
			
			if (!nextCharIsDigit(rowScanner) && nextCharIs(rowScanner,',')) {
				 throw new APException ("Missing number in set");
			}

			// negative number must have a '-' -> check this 

			if (nextCharIsDigit(rowScanner)) {
				bigInt.append(nextChar(rowScanner));
			} else throw new APException ("Invalid number in Set. Set can only consist of natural numbers \n");

			while(rowScanner.hasNext()) {
				ignoreInput(rowScanner, SPACE);
				if (!nextCharIs(rowScanner,',')) {
					if (nextCharIsDigit(rowScanner)) {
						bigInt.append(nextChar(rowScanner));
						continue;
					} else throw new APException ("Invalid number in Set. Set can only consist of natural numbers \n");
				} else if (nextCharIs(rowScanner,',')) {
					BigInteger num = new BigInteger(bigInt.toString());
					result.add(num);
					bigInt.setLength(0);
				}
			}
			
			ignoreInput(rowScanner, SPACE);
		}

		System.out.println("done number row");
		
		return (T) result;
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

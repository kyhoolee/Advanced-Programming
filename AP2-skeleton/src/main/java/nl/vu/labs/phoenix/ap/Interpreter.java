package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A set interpreter for sets of elements of type T
 */
public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {

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
		
		System.out.println("statement");
		
		ignoreInput(statementScanner, SPACE);
		
		if (nextCharIsLetter(statementScanner)) { // identifier starts with letter
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
		//check =
		//parse expression
		
		System.out.println("assign");
		T set = null;
		Identifier identifier = new Identifier();
		StringBuffer help = new StringBuffer();
		
		ignoreInput(assignmentScanner, SPACE);
		
		while (assignmentScanner.hasNext()) {
			if (nextCharIsLetter(assignmentScanner) || nextCharIsDigit(assignmentScanner)) {
				identifier.add(nextChar(assignmentScanner));
				continue;
			} else if (nextCharIs(assignmentScanner,'=') || nextCharIs(assignmentScanner,' ')) {
				break;
			} else throw new APException ("lol wrong id");
		
		}
		nextChar(assignmentScanner);
		set = expression(assignmentScanner);

		map.put(identifier, set);

		System.out.println("done assing");
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
			if (nextCharIsLetter(identifierScanner) || nextCharIsDigit(identifierScanner)) { // if there is space return false
				result.add(nextChar(identifierScanner));
			}
			ignoreInput(identifierScanner,SPACE);

			if (nextCharIsLetter(identifierScanner) || nextCharIsDigit(identifierScanner)) {
				if (nextCharIs(identifierScanner,'=')) {
					return result;
				} else throw new APException("Invalid Identifier. Identifier must start with a letter and contain only letters or natural numbers.\n");

			} else break;
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

		System.out.println("1");

		T result = term(expressionScanner);

		//ignoreInput(expressionScanner, SPACE);

		while (expressionScanner.hasNext()) {
			ignoreInput(expressionScanner, SPACE);
			if (nextCharIs(expressionScanner, '+') || nextCharIs(expressionScanner, '-') || nextCharIs(expressionScanner, '|')) {
				char operator = nextChar(expressionScanner);
				result = calculate(result, expression (new Scanner (expressionScanner.nextLine())), operator);
			} else {
				throw new APException ("No operator detected\n");
			}
		}

		System.out.println("2");

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
		T result = factor(termScanner);

		while (termScanner.hasNext()) {
			ignoreInput(termScanner, SPACE);
			if (nextCharIs(termScanner, '*')) {
				result = (T) result.intersection(term(termScanner));
			} else {
				throw new APException ("No operator detected\n");
			}
		}

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

		ignoreInput(factorScanner,SPACE);

		System.out.println("this is factor");

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
		T result;

		nextChar(setScanner);

		if (nextCharIs(setScanner, ',')) {
			throw new APException("Number missing in set");
		}

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
		return result;
	}	

	private T naturalNumberRow(Scanner rowScanner) throws APException {
		SetInterface<BigInteger> result = new Set<BigInteger>();
		result.init();

		StringBuffer bigInt = new StringBuffer();

		while (!nextCharIs(rowScanner,'}')) {
			ignoreInput(rowScanner, SPACE);

			if (!rowScanner.hasNext()) {
				throw new APException (String.format("Missing %c\n", '}'));
			}

			/* corner case: {1,2,3,,5}
			 * need to check this and give exception
			 */

			// negative number must have a '-' -> check this 

			if (nextCharIsDigit(rowScanner)) {
				bigInt.append(nextChar(rowScanner));
			} else throw new APException ("Invalid number in Set. Set can only consist of natural numbers \n");

			while(rowScanner.hasNext()) {
				ignoreInput(rowScanner, SPACE);
				if (!nextCharIs(rowScanner,',')) {
					if (nextCharIsDigit(rowScanner)) {
						bigInt.append(nextChar(rowScanner));
					} else throw new APException ("Invalid number in Set. Set can only consist of natural numbers \n");
				} else if (nextCharIs(rowScanner,',')) {
					BigInteger num = new BigInteger(bigInt.toString());
					result.add(num);
					bigInt.setLength(0);
					continue;
				}
			}

			BigInteger num = new BigInteger(bigInt.toString());
			result.add(num);

			ignoreInput(rowScanner, SPACE);
		}

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

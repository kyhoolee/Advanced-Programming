package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class PosixEvaluation {

	static int LOG_LEVEL = 0;
	static void log(String msg) {
		String tab = "";
		for(int i = 0; i < LOG_LEVEL ; i ++) {
			tab += "  ";
		}
		System.out.println(tab + msg);
	}

	/**
	 * Creates the postfix representation as string from the given term.
	 * @param s the term string in infix notation
	 * @return the term in postfix notation
	 * @throws ParserException
	 */
	public static String createPostfix(String s) throws Exception
	{
		Stack<Character> stack = new Stack<Character>();
		StringBuffer resStr = new StringBuffer();

		char c;
		int strpos = 0;
		while (strpos < s.length())
		{
			// get the current character
			c = s.charAt(strpos);
			if (c == ')')
			{
				while (!stack.empty() && !stack.peek().equals('('))
				{
					resStr.append(stack.pop());
				}
				if (!stack.empty())
					stack.pop();
			}
			else if (c == '+')
			{
				if (!stack.empty() && (stack.peek().equals('+') || stack.peek().equals('-') ||
						stack.peek().equals('*') || stack.peek().equals('/')))
				{
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '-')
			{
				if (!stack.empty() && (stack.peek().equals('+') || stack.peek().equals('-') ||
						stack.peek().equals('*') || stack.peek().equals('/')))
				{
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '*')
			{
				if (!stack.empty() && (stack.peek().equals('*') || stack.peek().equals('/')))
				{
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '/')
			{
				if (!stack.empty() && (stack.peek().equals('*') || stack.peek().equals('/')))
				{
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '(')
			{
				// just skip open bracket
				stack.push(c);
			}
			else if (c >= '0' && c <= '9')
			{
				// process numericals
				while ( (c >= '0' && c <= '9') || c == '.')
				{
					resStr.append(c);
					if (strpos+1 < s.length())
						c = s.charAt(++strpos);
					else
					{
						// abort while loop if we reach end of string
						c = 0;
						strpos = s.length();
					}
				}

				// inside while loop strpos is incremented one time too often
				strpos--;
			}
			else
			{
				throw new Exception("Invalid symbol: " + c + '!');
			}
			// make a right step inside the string
			strpos++;
			// insert a space to differ between consecutive numbers
			resStr.append(" ");
		}

		while (!stack.empty())
		{
			resStr.append(stack.pop());
			resStr.append(" ");
		}
		// delete the space character at the end of the string wrongly added in above while-loop
		resStr.deleteCharAt(resStr.length()-1);

		return resStr.toString();
	} 

	public static double evaluatePostfix(String s) throws Exception 
	{
		Stack stack = new Stack();
		int strpos = 0;
		char c;
		double x = 0;
		while (strpos < s.length())
		{
			// get the current character
			c = s.charAt(strpos);
			x = 0;
			if (c == '+')
			{
				double x1 = Double.valueOf(stack.pop().toString());
				double x2 = Double.valueOf(stack.pop().toString());
				x = x2 + x1;
				stack.push(x);
			}
			else if (c == '-')
			{
				double x1 = Double.valueOf(stack.pop().toString());
				double x2 = Double.valueOf(stack.pop().toString());
				x = x2 - x1;
				stack.push(x);
			}
			else if (c == '*')
			{
				double x1 = Double.valueOf(stack.pop().toString());
				double x2 = Double.valueOf(stack.pop().toString());
				x = x2 * x1;
				stack.push(x);
			}
			else if (c == '/')
			{
				double x1 = Double.valueOf(stack.pop().toString());
				double x2 = Double.valueOf(stack.pop().toString());
				x = x2 / x1;
				stack.push(x);
			}
			else if (c >= '0' && c <= '9')
			{
				// process numericals
				// substring with the number at the beginning of the string
				String sub = s.substring(strpos);
				int i;
				// find end of current number in the string
				for (i = 0; i < sub.length(); i++)
					if (sub.charAt(i) == ' ')
						sub = sub.substring(0, i);

				// 'sub' contains now just the number
				try {
					x = Double.parseDouble(sub);
				} catch (NumberFormatException ex) {
					throw new Exception("String to number parsing exception: " + s);
				}
				stack.push(x);
				// go on with next token
				strpos += i-1;
			}
			// ignore other symbols and proceed
			strpos++;
		}
		return x; // equal to "return stack.pop()";
	}  



	public static String createPostfixSet(String s) throws Exception {
		Stack<Character> stack = new Stack<Character>();
		StringBuffer resStr = new StringBuffer();

		char c;
		int strpos = 0;
		while (strpos < s.length()) {
			// get the current character
			c = s.charAt(strpos);
			if (c == ')') {
				while (!stack.empty() && !stack.peek().equals('(')) {
					resStr.append(stack.pop());
				}
				if (!stack.empty())
					stack.pop();
			}
			else if (c == '+') {
				if (!stack.empty() && (stack.peek().equals('+') || stack.peek().equals('-') ||
						stack.peek().equals('*') || stack.peek().equals('/'))) {
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '-') {
				if (!stack.empty() && (stack.peek().equals('+') || stack.peek().equals('-') ||
						stack.peek().equals('*') || stack.peek().equals('/'))) {
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '*') {
				if (!stack.empty() && (stack.peek().equals('*') || stack.peek().equals('/'))) {
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '/') {
				if (!stack.empty() && (stack.peek().equals('*') || stack.peek().equals('/'))) {
					resStr.append(stack.pop());
				}
				stack.push(c);
			}
			else if (c == '(') {
				// just skip open bracket
				stack.push(c);
			}
			else if (c == '{') {
				// process numericals
				while ( c != '}' ) {
					resStr.append(c);
					if (strpos+1 < s.length())
						c = s.charAt(++strpos);
					else {
						// abort while loop if we reach end of string
						c = 0;
						strpos = s.length();
					}
				}

				resStr.append(c);
				if (strpos+1 < s.length())
					c = s.charAt(++strpos);
				else {
					// abort while loop if we reach end of string
					c = 0;
					strpos = s.length();
				}

				// inside while loop strpos is incremented one time too often
				strpos--;
			}
			else {
				throw new Exception("Invalid symbol: " + c + '!');
			}
			// make a right step inside the string
			strpos++;
			// insert a space to differ between consecutive numbers
			resStr.append(" ");
		}

		while (!stack.empty()) {
			resStr.append(stack.pop());
			resStr.append(" ");
		}
		// delete the space character at the end of the string wrongly added in above while-loop
		resStr.deleteCharAt(resStr.length()-1);

		return resStr.toString();
	} 

	public static String evaluatePostfixSet(String s) throws Exception {
		Stack<SetInterface<BigInteger>> stack = new Stack<SetInterface<BigInteger>>();
		int strpos = 0;
		char c;
		SetInterface<BigInteger> x = null;
		while (strpos < s.length()) {
			// get the current character
			c = s.charAt(strpos);
			//x = 0;
			if (c == '+') {
				SetInterface<BigInteger> x1 = stack.pop();
				SetInterface<BigInteger> x2 = stack.pop();
				x = x2.union(x1);
				stack.push(x);
				System.out.println(setValue(x1));
			}
			else if (c == '-') {
				SetInterface<BigInteger> x1 = stack.pop();
				SetInterface<BigInteger> x2 = stack.pop();
				x = x2.difference(x1);
				stack.push(x);
			}
			else if (c == '*') {
				SetInterface<BigInteger> x1 = stack.pop();
				SetInterface<BigInteger> x2 = stack.pop();
				x = x2.intersection(x1);
				stack.push(x);
			}
			else if (c == '|') {
				SetInterface<BigInteger> x1 = stack.pop();
				SetInterface<BigInteger> x2 = stack.pop();
				x = x2.symdiff(x1);
				stack.push(x);
			}
			else if (c == '{') {
				// process numericals
				// substring with the number at the beginning of the string
				String sub = s.substring(strpos);
				int i;
				// find end of current number in the string
				for (i = 0; i < sub.length(); i++)
					if (sub.charAt(i) == ' ')
						sub = sub.substring(0, i);

				// 'sub' contains now just the number

				try {
					x = stringToSet(sub);
					//System.out.println(setValue(x));
				} catch (NumberFormatException ex) {
					throw new Exception("String to number parsing exception: " + s);
				}
				stack.push(x);
				System.out.println(setValue(stack.pop()));
				stack.push(x);
				// go on with next token
				strpos += i-1;

			}
			// ignore other symbols and proceed
			strpos++;
		}
		return setValue(x); // equal to "return stack.pop()";
	}  

	public static SetInterface<BigInteger> stringToSet (String s) throws Exception {
		SetInterface<BigInteger> result = new Set<BigInteger>();
		Scanner in = new Scanner (s).useDelimiter("");


		if (nextCharIs(in,'{')) {
			nextChar(in);
		}
		
		while (!nextCharIs(in,'}')) {
		
			while (in.hasNext(" ")) {
				nextChar(in);
			}
			
			if (nextCharIs(in, ',')) {
				nextChar(in);
				if(nextCharIs(in,' ')) {
					throw new APException ("Missing number");
				}
			}
			
			while (in.hasNext(" ")) {
				nextChar(in);
			}
			
			BigInteger num  = new BigInteger(naturalNumber(in));
			
			result.add(num);

			while (in.hasNext(" ")) {
				nextChar(in);
			}
		}

		if (nextCharIs(in,'}')) {
			nextChar(in);
		}
		
	//	System.out.println(setValue(result));
		
		return result;
	}

	public static String naturalNumber (Scanner numberScanner) throws APException {
		log("number");
		LOG_LEVEL ++;

		StringBuffer num = new StringBuffer();

		while (numberScanner.hasNext()) {
			while (numberScanner.hasNext(" ")) {
				nextChar(numberScanner);
			}

			// 2. Check next digits
			if (nextCharIsDigit(numberScanner)) {
				num.append(nextChar(numberScanner));
				if (num.charAt(0) == '0' && num.length() >= 2) {
					throw new APException ("number cannot start with '0'");
				}
				continue;
			} 
			
			while (numberScanner.hasNext(" ")) {
				nextChar(numberScanner);
			}

			if (nextCharIs(numberScanner, ',') || nextCharIs(numberScanner, '}') ) {
				break;
			} else throw new APException("Invalid number in Set. Set can only consist of natural numbers");
		}

		log("Number:" + num.toString());
		LOG_LEVEL --;
		log("done number");
		return num.toString();
	}

	public static String setValue (SetInterface<BigInteger> set) {
		StringBuffer result = new StringBuffer();

		if (set.isEmpty()) {
			result.append('{');
			result.append('}');
		} else {
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

	public static char nextChar (Scanner in) { // read next character from input
		return in.next().charAt(0);
	}

	public static boolean nextCharIs(Scanner in, char c) { // Method to check if the next character to be read when calling nextChar() is equal to the provided character
		return in.hasNext(Pattern.quote(c+""));
	}

	public static boolean nextCharIsDigit (Scanner in) { // Method to check if the next character to be read when calling nextChar() is a digit.
		return in.hasNext("[0-9]");
	}

	public static boolean nextCharIsLetter (Scanner in) { // Method to check if the next character to be read when calling nextChar() is a letter
		return in.hasNext("[a-zA-Z]");
	}

	public static void main(String[] args) {
		String ex = "{3,5}+{4}";
		try {
			String postfix = createPostfixSet(ex);
			System.out.println(postfix);
			String result = evaluatePostfixSet(postfix);
			//System.out.println(result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

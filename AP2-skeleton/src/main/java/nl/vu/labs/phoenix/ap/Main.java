package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.Scanner;
import java.io.PrintStream;

public class Main {
	
	PrintStream out;
	
	Main () {
		out = new PrintStream(System.out);
	}
	
	private String setValue (Set<BigInteger> set) {
		StringBuffer result = new StringBuffer();

		if (set.isEmpty()) {
			result.append('{');
			result.append('}');
		}else {
			result.append('{');
			result.append(set.get());
			set.remove(set.get());
			while(!set.isEmpty()){
				result.append(" ");
				result.append(set.get());
				set.remove(set.get());
			}
			
			result.append('}');
		}
		return result.toString();
	}
	
	private void printSet (String set) {
		System.out.println(set);
	}
	
	private void start() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		Scanner in = new Scanner(System.in);

		Set<BigInteger> set;
		
		while(in.hasNextLine()) {
			set = interpreter.eval(in.nextLine());
			if (set != null) {
				printSet(setValue(set));
			} else continue;
		}
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}

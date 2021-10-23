package assignment1;

import java.util.Random;

class Set implements SetInterface {
	
	private Identifier[] identifierRow;
	private int numberOfElements;
	private static final int MAX_NUMBER_OF_ELEMENTS = 20; // call from interface
	private static final String EXCEEDED_MAX_NUMBER_OF_ELEMENTS_EXCEPTION = "Maximum number of identifiers in set has been reached.";

	Set() {
		identifierRow = new Identifier[MAX_NUMBER_OF_ELEMENTS];
		numberOfElements = 0;
	}

	private void copyElements (Identifier[] dest, Identifier[] src, int amount) {
		for (int i = 0; i < amount; i++) {
			dest[i] = new Identifier(src[i]);
		}
	}

	Set (Set src) {
		this.identifierRow = new Identifier [MAX_NUMBER_OF_ELEMENTS];
		this.numberOfElements = 0;
		copyElements (this.identifierRow, src.identifierRow, src.numberOfElements);
		this.numberOfElements = src.numberOfElements;
	}

	@Override
	public void init() {
		numberOfElements = 0;
	}

	@Override
	public int numberOfElements() {
		return numberOfElements;
	}

	@Override
	public boolean isEmpty() {
		if (numberOfElements == 0) {
			return true;
		} else return false;
	}

	@Override
	public boolean containIdentifier(Identifier identifier) {
		for (int i = 0; i < numberOfElements ; i++) {
			if (identifierRow[i].isIdentical(identifier) == true) {
				return true;
			} 
		} 
		return false; 
	}

	@Override
	public void addIdentifier(Identifier identifier) throws Exception {// exception identifier has already been in set + exceed max number of elements
		if (this.containIdentifier(identifier) == true) {
			return;
		} else if (numberOfElements < MAX_NUMBER_OF_ELEMENTS ) {
			identifierRow[numberOfElements] = new Identifier(identifier);
			numberOfElements += 1;
		} else throw new Exception (EXCEEDED_MAX_NUMBER_OF_ELEMENTS_EXCEPTION);

	}

	@Override
	public void removeIdentifier(Identifier identifier) { // easier: take ref from one place it to the other and deduct 1 number of elements
		if (this.containIdentifier(identifier) == true) {
			for (int i = 0; i < this.numberOfElements; i++) {
				if(this.identifierRow[i].isIdentical(identifier) == true) {
					identifierRow[i] = null;
					identifierRow[i] = new Identifier(identifierRow[i+1]);
					numberOfElements -= 1;
				}
			}
		}
	}

	@Override
	public void removeFirst() {
		if(!this.isEmpty()) {
			for (int i = 0; i <= numberOfElements; i++) {
				identifierRow[i] = identifierRow[i+1];
				numberOfElements -=1;
			}
		}
	}
	
	@Override // be private
	public void removeLast() {
		if(!this.isEmpty()) {
			identifierRow[numberOfElements - 1] = null;
			numberOfElements -= 1;
		}
	}

	@Override
	public Identifier getElement() {
		int index = new Random().nextInt(this.numberOfElements);
		Identifier result = new Identifier (this.getElementAt(index));
		return result;
	}

	private Identifier getElementAt (int index) {
		return new Identifier (this.identifierRow[index]);
	}

	@Override
	public Set calculateDifference(Set input) { 
		Set result = new Set();
		try {
			for(int i = 0; i < this.numberOfElements; i++) {
				if (input.containIdentifier(this.identifierRow[i]) == false) { 
					result.addIdentifier(this.identifierRow[i]);
				} 
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	} 

	@Override
	public Set calculateIntersection(Set input) {
		Set result = new Set();
		try {
			for(int i = 0; i < input.numberOfElements; i++) {
				if (this.containIdentifier(input.identifierRow[i]) == true) {
					result.addIdentifier(input.identifierRow[i]);
				}
			}
		}catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public Set calculateUnion(Set input) throws Exception {
		Set result = new Set (this);
		if (this.numberOfElements + input.numberOfElements <= MAX_NUMBER_OF_ELEMENTS) {
			for (int i = 0; i < input.numberOfElements; i++) {
				if (result.containIdentifier(input.identifierRow[i]) == false) {
					result.addIdentifier(input.identifierRow[i]);
				}
			} return result;
		} else {
			throw new Exception (EXCEEDED_MAX_NUMBER_OF_ELEMENTS_EXCEPTION);
		}
	}

	@Override
	public Set calculateSymDiff(Set input) throws Exception { 
		Set result = new Set (this.calculateUnion(input).calculateDifference(this.calculateIntersection(input)));
		if (result.numberOfElements <= MAX_NUMBER_OF_ELEMENTS) {
			return result;
		} else {
			throw new Exception (EXCEEDED_MAX_NUMBER_OF_ELEMENTS_EXCEPTION);
		}
	}
}
package assignment1;

class Identifier implements IdentifierInterface {

	private StringBuffer sb;

	public Identifier() {
		sb = new StringBuffer();
	}

	public Identifier(Identifier src) {
		sb = new StringBuffer(src.sb.toString());
	}

	@Override
	public Identifier init (char c) {
		sb.append(c); // call add();
		return this;
	}

	@Override
	public void add (char c) {
		sb.append(c);
	}

	@Override
	public char getCharAt​(int index) {
		return sb.charAt(index);
	}

	@Override
	public int length() {
		return sb.length();
	}

	@Override
	public boolean isIdentical(Identifier identifier) {  // convert to string and use equals();
		boolean result = true;
		if (this.length() == identifier.length()) {
			for (int i = 0; i < this.length(); i++) {	
				if (this.sb.charAt(i) != identifier.sb.charAt(i)) {	
					result = false;
				}
			}
		} else result = false;
		return result;
	}

}

package nl.vu.labs.phoenix.ap;

public class Identifier implements IdentifierInterface {

	private StringBuffer sb;

	public Identifier() {
		sb = new StringBuffer();
	}

	public Identifier(Identifier src) {
		sb = new StringBuffer(src.sb.toString());
	}

	@Override
	public String value() {
		return sb.toString();
	}

	@Override
	public int size() {
		return sb.length();
	}

	@Override
	public IdentifierInterface init(char c) {
		sb.append(c);
		return this;
	}

	@Override
	public IdentifierInterface add(char c) {
		sb.append(c); 
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Identifier) || obj == null) {
			return false;
		} else if (this.value().equals(obj.toString())) {
			return true;
		} else return false;
	}

	@Override
	public int hashCode() {
		return sb.toString().hashCode();
	}
}

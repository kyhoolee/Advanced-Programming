package nl.vu.labs.phoenix.ap;

public class LinkedList<E extends Comparable<E>> implements ListInterface<E> {

	private class Node {

		E data;
		Node prior, next;

		public Node(E data) {
			this(data, null, null);
		}

		public Node(E data, Node prior, Node next) {
			this.data = data == null ? null : data;
			this.prior = prior;
			this.next = next;
		}
	}

	private int numberOfElements;
	private Node first,
	current,
	last;

	public LinkedList() {
		numberOfElements = 0;
		first = null;
		current = null;
		last = null;
	}

	@Override
	public boolean isEmpty() {
		if (first == null || numberOfElements == 0 ){
			return true;
		} else return false;
	}

	@Override
	public ListInterface<E> init() {
		first = null;
		current = null;
		last = null;
		numberOfElements = 0;
		return this;
	}

	@Override
	public int size() {
		return numberOfElements;
	}

	@Override
	public ListInterface<E> insert(E d) { // ordered list: from small to large ?????what if newNode.data already exist (Check in set)
		Node newNode = new Node(d);

		if (this.isEmpty()) {
			insertEmpty(newNode);
		} else if (newNode.data.compareTo(first.data) < 0) {
			insertFirst(newNode);
		} else if (newNode.data.compareTo(last.data) > 0) {
			insertLast(newNode);
		} else {
			insertMiddle (newNode);
		}
		
		numberOfElements ++;
		
		//System.out.println(numberOfElements);
		return this;
	}

	private void insertEmpty (Node newNode) {
		first = last = current = newNode;
		first.prior = null;
		last.next = null;
	}

	private void insertFirst(Node newNode) {
		newNode.prior = null;
		newNode.next = first;
		current = first = first.prior = newNode;
	}

	private void insertLast(Node newNode) {
		newNode.prior = last;
		newNode.next = null;
		current = last = last.next = newNode;
	}	

	private void insertMiddle (Node newNode) {
		/*goToFirst();
		int x = 0;
		while ((newNode.data.compareTo(current.data) > 0 && newNode.data.compareTo(current.next.data) < 0)) { //  try to use find
			goToNext();
			System.out.printf("%d\n", x);
			x++;
		}*/

		find(newNode.data);
		newNode.next = current.next;
		newNode.prior = current;
		current.next = newNode;
		current.next.prior = newNode;
		current = newNode;
	}

	@Override
	public E retrieve() {
		if (this.isEmpty()) {
			throw new NullPointerException();
		}
		else return current.data;
	}

	@Override
	public ListInterface<E> remove() {
		if (this.isEmpty()) {
			throw new NullPointerException();
		} else if (numberOfElements == 1) {
			current = first = last = null;
			numberOfElements -- ;
		} else if (current.next == null && current.prior != null) {
			current.prior.next = null;
			current = current.prior;
			last = current;
			numberOfElements -- ;
		} else if (current.prior == null && current.next != null) {
			current.next.prior = null;
			current = current.next;
			first = current;
			numberOfElements -- ;
		} else if (current.prior != null && current.next != null) {
			current.prior.next = current.next;
			current.next.prior = current.prior;
			current = current.next;
			numberOfElements -- ;
		}
		
		return this;
	}

	@Override
	public boolean find(E d) {
		if (this.isEmpty()) {
			return false;
		}

		goToFirst();
		if (current.data.compareTo(d) == 0) {
			return true;
		} /*else if (current.data.compareTo(d) > 0) {
			return false;
		}*/
		while (goToNext()) {
			if (current.data.compareTo(d) == 0) {
				return true;
			}
			if (current.data.compareTo(d) < 0) {
				continue;
			} else {
				current = current.prior;
				break;
			}
		} 

		/*goToFirst();	
		while(current.data.compareTo(d) < 0) {
			goToNext();
		}
		current  = current.prior;*/
		return false;
	}

	@Override
	public boolean goToFirst() {
		if (this.isEmpty()) {
			return false;
		} else {
			current = first;
			return true;
		}
	}

	@Override
	public boolean goToLast() {
		if (this.isEmpty()) {
			return false;
		} else {
			current = last;
			return true;   
		} 
	}

	@Override
	public boolean goToNext() {
		if (this.isEmpty() || current.next == null) {
			return false;
		} else {
			current = current.next;
			return true;
		}
	}

	@Override
	public boolean goToPrevious() {
		if (this.isEmpty() || current.prior == null) {
			return false;
		} else {
			current = current.prior;
			return true;	
		}
	}

	@Override
	public ListInterface<E> copy() {
        LinkedList<E> copy = new LinkedList<>();
        this.goToFirst();
        if (this.isEmpty()){
            return copy.init();
        }
        copy.insert(current.data);
        copy.first = copy.current;

        while (goToNext()){
            copy.insert(current.data);
        }
        copy.last = copy.current;
        return copy;
    }
	
	
}
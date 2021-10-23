package nl.vu.labs.phoenix.ap;

import java.util.NoSuchElementException;

public class Set<T extends Comparable<T>> implements SetInterface<T> {

	private ListInterface<T> set;

	public Set() {
		set = new LinkedList<T>();
	}

	@Override
	public boolean add(T t) {
		if (this.containsElement(t) == true) {
			return false;
		}
		set.insert(t);
		return true;
	}

	@Override
	public T get() {
		if (set.isEmpty()) {
			throw new NoSuchElementException(); 
		} else {
			set.goToFirst();
			return set.retrieve();
		}
	}

	@Override
	public boolean remove(T t) {
		if (set.isEmpty()) {
			throw new NoSuchElementException(); 
		} else {
			if (set.find(t)) {
				set.remove();
				return true;
			} else return false;
		}
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public SetInterface<T> copy() {
		Set<T> result = new Set<T>();
		if (this.isEmpty()) {
			result.init();
		} else {
			while (!this.isEmpty()) {
				T t = this.get();
				if(!result.containsElement(t)) {
					result.add(t);
				}
				this.remove(t);
			}
		}
		return result;
	}

	@Override
	public SetInterface<T> union(SetInterface<T> set) {
		SetInterface<T> result = set.copy();

		this.set.goToFirst();
		if (!result.containsElement(this.set.retrieve())){
			result.add(this.set.retrieve());
		}

		while (this.set.goToNext()) {
			if (!result.containsElement(this.set.retrieve())){
				result.add(this.set.retrieve());
			}
		}
		return result;
	}

	@Override
	public SetInterface<T> intersection(SetInterface<T> set) {
		SetInterface<T> result = new Set<T>();

		this.set.goToFirst();
		if (set.containsElement(this.set.retrieve())){
			result.add(this.set.retrieve());
		}

		while (this.set.goToNext()) {
			if (set.containsElement(this.set.retrieve())){
				result.add(this.set.retrieve());
			}
		}
		return result;
	}

	@Override
	public SetInterface<T> difference(SetInterface<T> set) {
		SetInterface<T> result = new Set<T>();

		this.set.goToFirst();
		if (!set.containsElement(this.set.retrieve())){
			result.add(this.set.retrieve());
		}

		while (this.set.goToNext()) {
			if (!result.containsElement(this.set.retrieve())){
				result.add(this.set.retrieve());
			}
		} 
		return result;
	}

	@Override
	public SetInterface<T> symdiff(SetInterface<T> set) {
		SetInterface<T> result = this.union(set).difference(this.intersection(set));
		return result;
	}

	@Override
	public boolean containsElement(T t) {
		return set.find(t);
	}

	@Override
	public void init() {
		set.init();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}
}

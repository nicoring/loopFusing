package collection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Collection {

	private List<Integer> list;

	public Collection() {
		this.list = new ArrayList<Integer>();
	}

	public Collection(List<Integer> list) {
		this.list = list;
	}

	public Integer get(Integer index) {
		return this.list.get(index);
	}

	public void add(Integer value) {
		this.list.add(value);
	}

	public void set(Integer index, Integer value) {
		this.list.set(index, value);
	}

	public void remove(int index) {
		this.list.remove(index);
	}

	public Integer size() {
		return this.list.size();
	}
	
	public Boolean isEmpty() {
		return this.list.isEmpty();
	}

	public Collection map(Function<Integer, Integer> func) {
//		System.out.println("call to map");
		for (int i = 0; i < this.size(); i++) {
			Integer newElem = func.apply(this.get(i));
			this.set(i, newElem);
		}
		return this;
	}

	public Collection filter(Function<Integer, Boolean> func) {
//		System.out.println("call to filter");
		for (int i = 0; i < this.size(); i++) {
			if(!func.apply(this.get(i))) {
				this.remove(i);
				i--;
			}
		}
		return this;
	}
	

	// forces the computation of the stored maps and filters
	public void collectValues() {};
	
	public void forEach(Consumer<Integer> consumer) {
		for (int i = 0; i < this.size(); i++) {
			consumer.accept(this.get(i));
		}
	}

	public <T> T fold(T acc, BiFunction<T, Integer, T> func) {
		for (int i = 0; i < this.size(); i++) {
			acc = func.apply(acc, this.get(i));
		}
		return acc;
	}

	public <T> T reduce(BiFunction<T, Integer, T> func) {

		if (this.isEmpty()) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		T acc = (T) this.get(0);
		for (int i = 1; i < this.size(); i++) {
			acc = func.apply(acc, this.get(i));
		}
		return acc;
	}
}

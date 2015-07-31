package collection;

public class ExtendedCollection extends Collection {
	public ExtendedCollection multiply(int factor) {
		this.map((i) -> i * factor);
		return this;
	}
	
	public ExtendedCollection square() {
		return (ExtendedCollection) this.map((i) -> i * i);
	}
	
	public ExtendedCollection doubleAndSquare() {
		this.multiply(2)
			.square();
		return this;
	}
	
	public ExtendedCollection getGreaterThan(int value) {
		this.filter((i) -> i > value);
		return this;
	}
	
	public ExtendedCollection getLowerThan(int value) {
		this.filter((i) -> i < value);
		return this;
	}
	
	public ExtendedCollection getGreaterThanOrEqual(int value) {
		this.filter((i) -> i >= value);
		return this;
	}
	
	public ExtendedCollection getLowerThanOrEqual(int value) {
		this.filter((i) -> i <= value);
		return this;
	}
	
	public ExtendedCollection getInterval(int start, int end) {
		this
			.getGreaterThanOrEqual(start)
			.getLowerThanOrEqual(end);
		return this;
	}


	public ExtendedCollection getEvens() {
		this.filter(i -> i % 2 == 0);
		return this;
	}
	
	public ExtendedCollection getOdds() {
		this.filter(i -> i % 2 == 1);
		return this;
	}
	
	public ExtendedCollection printAll() {
		this.forEach((i) -> { System.out.print(i + " "); });
		System.out.print("\n");
		return this;
	}
	
	public String toString() {
		String tmpString = this.reduce((acc, next) -> acc + ", " + next); 
		return "[" + tmpString + "]";
	}
	
	public int sum() {
		return fold(0, (acc, next) -> acc + next);
	}
	
	public int sumSquares() {
		return this
			.square()
			.sum();
	}
	
}

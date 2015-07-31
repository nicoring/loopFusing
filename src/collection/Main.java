package collection;

public class Main {

	public static void main(String[] args) {
		ExtendedCollection collection = new ExtendedCollection();
		collection.add(1);
		collection.add(2);
		collection.add(3);
		
		collection
			.map(x -> x * 2)
			.map(x -> x * x)
			.filter(x -> x > 4)
			.filter(x -> x > 3)
			.filter(x -> x < 5);

		
		int sum = collection.sum();
		System.out.println(sum);
	}

}

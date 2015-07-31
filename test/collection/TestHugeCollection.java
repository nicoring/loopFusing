package collection;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class TestHugeCollection {
	
	final int collectionSize = 5000000;
	int[] testNumbers = new int[collectionSize];
	Random rn = new Random();
	ExtendedCollection collection;
	


	
	@Before
	public void setUp() {
		for (int i = 0; i < collectionSize; i++) {
			testNumbers[i] = i;//rn.nextInt();
		}
		collection = new ExtendedCollection();
		for (int i : testNumbers) {
			collection.add(i);
		}
	}
	
	@Test
	public void test() {
		collection
			.map(x -> x / 1000)
			.map(x -> x * 1000)
			.map(x -> x / 1000)
			.map(x -> x / 1000)
			.map(x -> x / 1000)
			.filter(x -> x < (int) (collectionSize * 0.8))
			.filter(x -> x < (int) (collectionSize * 0.7))
			.filter(x -> x < (int) (collectionSize * 0.6))
			.filter(x -> x < (int) (collectionSize * 0.5))
			.filter(x -> x < (int) (collectionSize * 0.4));
		
		// force computing
		collection.size();
	}

}

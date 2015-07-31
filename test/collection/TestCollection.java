package collection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class TestCollection {

	int[] testNumbers = { 1,2,3,4,5,6,7,8,9 };
	ExtendedCollection collection;

	@Before
	public void setUp() {
		collection = new ExtendedCollection();
		for (int i : testNumbers) {
			collection.add(i);
		}
	}


	private void compareCollections(Collection collection, Vector<Integer> v) {
		assertThat(collection.size(), is(v.size()));
		for (int i = 0; i < v.size(); i++) {
			assertThat(collection.get(i), is(v.get(i)));
		}
	}

	@Test
	public void testFilter() {
		collection
			.filter(x -> x > 3)
			.filter(x -> x < 5)
			.collectValues();

		assertThat(collection.size(), is(1));
		assertThat(collection.get(0), is(4));
	}

	@Test
	public void testMap1() {
		collection
			.map(x -> x * 2)
			.map(x -> x / 2)
			.collectValues();

		assertThat(collection.size(), is(testNumbers.length));
		for (int i = 0; i < testNumbers.length; i++) {
			assertThat(collection.get(i), is(testNumbers[i]));
		}
	}

	@Test
	public void testMap2() {
		collection
			.map(x -> x + 2)
			.map(x -> x * x)
			.collectValues();

		assertThat(collection.size(), is(testNumbers.length));
		for (int i = 0; i < testNumbers.length; i++) {
			assertThat(collection.get(i), is((testNumbers[i] + 2) * (testNumbers[i] + 2)));
		}
	}

	@Test
	public void testMapAndFilter1() {
		collection
			.map(x -> x * 2)
			.map(x -> x * x)
			.filter(x -> x > 4)
			.filter(x -> x > 3)
			.filter(x -> x < 5)
			.collectValues();

		assertThat(collection.size(), is(0));
	}

	@Test
	public void testMapAndFilter2() {
		collection
			.map(x -> x * 2)
			.map(x -> x * x)
			.filter(x -> x > 4)
			.filter(x -> x > 3)
			.filter(x -> x < 10)
			.collectValues();

		Vector<Integer> testVector = new Vector<Integer>();
		for (int i = 0; i < testNumbers.length; i++) {
			int newValue = (testNumbers[i] * 2) * (testNumbers[i] * 2);
			if (newValue > 4 && newValue < 10) {
				testVector.add(newValue);
			}
		}

		compareCollections(collection, testVector);
	}

	@Test
	public void testMapAndFilter3() {
		collection = new ExtendedCollection();
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
		assertThat(sum, is(0));
	}

	@Test
	public void testMapAndFilterAndMap1() {
		collection
			.map(x -> x + 5)
			.filter(x -> x >= 10)
			.map(x -> x - 5)
			.collectValues();


		Vector<Integer> testVector = new Vector<Integer>();
		for (int i = 0; i < testNumbers.length; i++) {
			if (testNumbers[i] >= 5 ) {
				testVector.add(testNumbers[i]);
			}
		}

		compareCollections(collection, testVector);
	}

	@Test
	public void testMapAndFilterAndMap2() {
		collection
			.map(x -> x + 2)
			.map(x -> x + 3)
			.filter(x -> x >= 10)
			.filter(x -> x >= 5)
			.map(x -> x - 3)
			.map(x -> x - 2)
			.collectValues();


		Vector<Integer> testVector = new Vector<Integer>();
		for (int i = 0; i < testNumbers.length; i++) {
			if (testNumbers[i] >= 5 ) {
				testVector.add(testNumbers[i]);
			}
		}

		compareCollections(collection, testVector);
	}

	@Test
	public void testFold() {
		int result = collection.fold(0, (acc, next) -> acc + next);
		int reference = 0;
		for (int each : testNumbers) {
			reference += each;
		}
		assertThat(result, is(reference));
	}

	@Test
	public void testEmptyFold() {
		collection = new ExtendedCollection();
		int result = collection.fold(0, (acc, next) -> acc + next);
		assertThat(result, is(0));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testEmptyReduce() {
		collection = new ExtendedCollection();
		int result = collection.reduce((acc, next) -> acc + next);
	}

	@Test
	public void testReduce() {
		int result = collection.reduce((acc, next) -> acc + next);
		int reference = 0;
		for (int each : testNumbers) {
			reference += each;
		}
		assertThat(result, is(reference)); 
	}

}

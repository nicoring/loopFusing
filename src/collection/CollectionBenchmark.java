package collection;

import java.util.function.Function;

public class CollectionBenchmark {
	
	final int RUN_COUNT = 10;
	final int COLLECTION_SIZE_STEPS = 8;
	final int COLLECTION_SIZE_STEP_SIZE = 10;
	final int MAP_COUNT = 10;
	
	long [][][] allResults = new long[RUN_COUNT][COLLECTION_SIZE_STEPS][MAP_COUNT];
	long [][] result = new long[COLLECTION_SIZE_STEPS][MAP_COUNT];
	
	final Function<Integer, Integer> ID = x -> x + 1000;

	public static void main(String[] args) {
		CollectionBenchmark b = new CollectionBenchmark();
		b.runBenchmarks();
		b.printResults();
	}
	
	public void printResults() {
		
		for (int i = 0, n = 1; i < COLLECTION_SIZE_STEPS; i++, n *=  COLLECTION_SIZE_STEP_SIZE) {
			System.out.print(n + ", ");
		}
		System.out.println();
		
		for (int j = 0; j < MAP_COUNT; j++) {
			for (int i = 0; i < COLLECTION_SIZE_STEPS; i++) {
				long tmp = result[i][j];
				System.out.print(tmp + ", ");
			}
			System.out.println();
		}
	}
	
	public void runBenchmarks() {
		for (int k = 0; k < RUN_COUNT; k++) {
			long [][] results = new long[COLLECTION_SIZE_STEPS][MAP_COUNT];
			for (int i = 0, n = 1; i < COLLECTION_SIZE_STEPS; i++, n *=  COLLECTION_SIZE_STEP_SIZE) {
				for (int j = 0; j < MAP_COUNT; j++) {
					results[i][j] = benchmark(j + 1, n);
				}
			}
			allResults[k] = results;
		}
		aggregateResults();
	}
	
	public void aggregateResults() {
		for (int i = 0; i < COLLECTION_SIZE_STEPS; i++) {
			for (int j = 0; j < MAP_COUNT; j++) {
				long sum = 0;
				for (int k = 0; k < RUN_COUNT; k++) {
					sum += allResults[k][i][j];
				}
				result[i][j] = sum / RUN_COUNT;
			}
		}
	}
	
	public long nanoToMillis(long nanos) {
		return nanos / 1000000l; 
	}
	
	public long benchmark(int mapCount, int collectionSize) {
		Collection collection = setUpCollection(collectionSize);
		
		long startTime = System.nanoTime();
		for (int i = 0; i < mapCount; i++) {
			collection.map(ID);
		}
		collection.collectValues();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		return nanoToMillis(duration);
	}
	
	public Collection setUpCollection(int collectionSize) {
		Collection collection = new Collection();
		for (int i = 0; i < collectionSize; i++) {
			collection.add(i);
		}
		return collection;
	}
}

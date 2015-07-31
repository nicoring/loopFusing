package collection;


import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public aspect Fuse pertarget(collection()){

	pointcut collection(): target(Collection);

	abstract class Operation {
		public abstract Boolean isMapOp();
		public abstract Boolean isFilterOp();
		public abstract <T> Function<Integer, T> getOperation();
		public abstract <T> T apply(Integer i);
	}

	class MapOp extends Operation {
		private Function<Integer, Integer> operation;

		public MapOp(Function<Integer, Integer> func) {
			this.operation = func;
		}

		@SuppressWarnings("unchecked")
		public Integer apply(Integer i) {
			return this.operation.apply(i);
		}

		@SuppressWarnings("unchecked")
		public Function<Integer, Integer> getOperation() {
			return this.operation;
		}

		public Boolean isMapOp() { return true; }
		public Boolean isFilterOp() { return false; }
	}

	class FilterOp extends Operation {
		private Function<Integer, Boolean> operation;

		public FilterOp(Function<Integer, Boolean> predicate) {
			this.operation = predicate;
		}

		@SuppressWarnings("unchecked")
		public Boolean apply(Integer i) {
			return this.operation.apply(i);
		}

		@SuppressWarnings("unchecked")
		public Function<Integer, Boolean> getOperation() {
			return this.operation;
		}

		public Boolean isMapOp() { return false; }
		public Boolean isFilterOp() { return true; }
	}

	private Stack<Operation> operationStack = new Stack<Operation>();

	private void addOp(MapOp mapOp) {
		if (!operationStack.isEmpty() && operationStack.peek().isMapOp()) {
			MapOp lastMapOp = (MapOp) operationStack.pop();
			operationStack.push(fuseOps(lastMapOp, mapOp));
		} else {
			operationStack.push(mapOp);
		}
	}

	private void addOp(FilterOp filterOp) {
		if (!operationStack.isEmpty() && operationStack.peek().isFilterOp()) {
			FilterOp lastFilterOp = (FilterOp) operationStack.pop();
			operationStack.push(fuseOps(lastFilterOp, filterOp));
		} else {
			operationStack.push(filterOp);
		}
	}

	private MapOp fuseOps(MapOp op1, MapOp op2) {
		Function<Integer, Integer> f = (x) -> {
			return op2.apply(op1.apply(x));
		};
		return new MapOp(f);
	}

	private FilterOp fuseOps(FilterOp op1, FilterOp op2) {	
		Function<Integer, Boolean> f = (x) -> {
			return op1.apply(x) && op2.apply(x);
		};
		return new FilterOp(f);
	}


	pointcut map(Function f):
		call(* Collection.map(Function)) &&
		args(f);

	pointcut filter(Function<Integer, Boolean> f):
		call(* Collection.filter(Function<Integer, Boolean>)) &&
		args(f);

	
	Boolean isFusing = true;

	@SuppressWarnings("unchecked")
	Collection around(Function f, Collection collection) :
		map(f) && target(collection) {
			if (isFusing) {
//				System.out.println("around map");
				addOp(new MapOp(f));
			} else {
				proceed(f, collection);
			}
			return collection;
	}


	@SuppressWarnings("unchecked")
	Collection around(Function f, Collection collection) :
		filter(f) && target(collection) {
			if (isFusing) {
//				System.out.println("around filter");
				addOp(new FilterOp(f));
			} else {
				proceed(f, collection);
			}
			return collection;		
	}
	
	pointcut mapOrFilter():
		map(*) || filter(*);

	pointcut forceCompute():
		(get(List<Integer> Collection.list) ||
		set(List<Integer> Collection.list) ||
		execution(* Collection.collectValues())) &&
		!cflow(mapOrFilter());

	before(Collection collection): forceCompute() && target(collection) {
		isFusing = false;
		for (Operation op : operationStack) {
			if (op.isFilterOp()) {
				collection.filter(op.getOperation());
			} else if (op.isMapOp()) {
				collection.map(op.getOperation());
			} else {
				throw new IllegalArgumentException();
			}
		}
		operationStack.clear();
		isFusing = true;
	}
}

package main;

import java.util.concurrent.TimeoutException;

public class EliStack<T> extends LockFreeStack<T> {

	private static ThreadLocal<RangePolicy> policy;
	private EliminationArray<T> eliminationArray;
	private int capacity, waitDuration;

	EliStack(final int capacity, int waitDuration) {
		super(0, 0);

		this.capacity = capacity;
		this.waitDuration = waitDuration;

		eliminationArray = new EliminationArray<T>(capacity, waitDuration);

		policy = new ThreadLocal<RangePolicy>() {
			protected synchronized RangePolicy initialValue() {
				return new RangePolicy(capacity);
			}
		};
	}

	public void push(T value) {
		RangePolicy rangePolicy = policy.get();
		Node<T> node = new Node<T>(value);

		while (true) {
			if (this.tryPush(node))
				return;
			else {
				try {
					T otherValue = eliminationArray.visit(value,
							rangePolicy.getRange());
					if (otherValue == null) {
						rangePolicy.recordEliminationSuccess();
						return;
					}
				} catch (TimeoutException e) {
					rangePolicy.recordEliminationTimeout();
				}
			}
		}
	}

	public T pop() throws Exception {
		RangePolicy rangePolicy = policy.get();

		while (true) {
			Node<T> returnNode = tryPop();
			if (returnNode != null)
				return returnNode.value;
			else {
				try {
					T otherValue = eliminationArray.visit(null,
							rangePolicy.getRange());
					if (otherValue != null) {
						rangePolicy.recordEliminationSuccess();
						return otherValue;
					}
				} catch (TimeoutException e) {
					rangePolicy.recordEliminationTimeout();
				}
			}
		}
	}

	public int removeRemaining() {
		int count = 0;
		while (true) {
			try {
				pop();
				count++;
			} catch (Exception e) {
				return count;
			}
		}
	}

	public String toString() {
		return capacity + "," + waitDuration;
	}
}
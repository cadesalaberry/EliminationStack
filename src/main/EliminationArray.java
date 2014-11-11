package main;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class EliminationArray<T> {

	private LockFreeExchanger<T>[] exchanger;
	private int duration = 10;
	private Random random;

	@SuppressWarnings("unchecked")
	EliminationArray(int capacity, int duration) {

		this.duration = duration;

		exchanger = new LockFreeExchanger[capacity];

		for (int i = 0; i < capacity; i++) {
			exchanger[i] = new LockFreeExchanger<T>();
		}

		random = new Random();
	}

	public T visit(T value, int range) throws TimeoutException {

		int slot = random.nextInt(range);
		
		T t = exchanger[slot].exchange(value, duration, TimeUnit.MILLISECONDS);

		return t;
	}
}
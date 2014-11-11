package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

class StackThread extends Thread {

	private static final int QUEUE_CAPACITY = 10;

	private Queue<Integer> last = new ArrayDeque<Integer>(QUEUE_CAPACITY);
	private Random random = new Random();

	public int pushCount = 0;
	public int popCount = 0;

	private EliStack<Integer> stack;

	private int operationCount, maxDelay;

	StackThread(EliStack<Integer> stack, int operationCount, int maxDelay) {
		this.operationCount = operationCount;
		this.maxDelay = maxDelay;
		this.stack = stack;
	}

	@Override
	public void run() {

		for (int i = 0; i < operationCount; i++) {

			if (random.nextFloat() >= 0.5) {

				if (last.size() == 0 || random.nextFloat() >= 0.5) {
					stack.push(random.nextInt());
				} else {
					stack.push(randomFromQueue());
				}

				pushCount++;

			} else {
				try {
					addToQueue(stack.pop());
					popCount++;
				} catch (Exception ignore) {
					i--;
				}
			}

			try {
				Thread.sleep(random.nextInt(maxDelay));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private int randomFromQueue() {
		List<Integer> array = new ArrayList<Integer>(last);
		return array.get(random.nextInt(array.size()));
	}

	private void addToQueue(int value) {
		if (last.size() > QUEUE_CAPACITY)
			last.remove();
		last.add(value);
	}

	public int getPushCount() {
		return pushCount;
	}

	public int getPopCount() {
		return popCount;
	}
}
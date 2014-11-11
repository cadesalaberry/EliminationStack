package main;

import main.StackThread;

public class EliminationStack {

	final static int[] threads = { 2, 4, 8, 16 };
	final static int[] questions = { 1, 2, 3, 4 };

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 5) {
			// test();
			System.out.println("Running default config...");
			solveQuestion(4, 200, 200, 10, 100);
			return;
		}

		int p, d, n, t, e;

		p = Integer.parseInt(args[0]);
		d = Integer.parseInt(args[1]);
		n = Integer.parseInt(args[2]);
		t = Integer.parseInt(args[3]);
		e = Integer.parseInt(args[4]);

		if (!validateArgs(p, d, n, t, e)) {
			return;
		}
		solveQuestion(p, d, n, t, e);
	}

	public static void solveQuestion(int p, int d, int n, int t, int e) {

		int threadCount = p;
		int maxDelay = d;
		int operationCount = n;
		int timeoutFactor = t;
		int arrayCapacity = e;

		EliStack<Integer> stack;

		stack = new EliStack<Integer>(arrayCapacity, timeoutFactor);
		StackThread[] threads = new StackThread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			threads[i] = new StackThread(stack, operationCount, maxDelay);
		}

		long start = System.currentTimeMillis();

		for (StackThread thread : threads)
			thread.start();

		for (StackThread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException ignore) {
			}
		}

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		int totalPush = 0, totalPop = 0;
		for (StackThread thread : threads) {
			totalPush += thread.getPushCount();
			totalPop += thread.getPopCount();
		}

		System.out.printf("%d %d %d\n", totalPush, totalPop,
				stack.removeRemaining());
	}

	public static void test() {

		String report = "nbOfThreads, time";

		for (int i : threads) {

			report += "" + i + ",";
			System.out.println(report);
		}

	}

	public static boolean validateArgs(int p, int d, int n, int t, int e) {

		boolean valid = true;

		// Checks that the number of threads is greater than 1
		if (!(1 < p)) {

			System.err.println("Error: p should be greater than 1.");
			valid = false;
		}

		// Checks that we have a valid random delay between each thread
		// operations
		if (!(0 < d)) {

			System.err.println("Error: d should be greater than 0.");
			valid = false;
		}

		// Checks that the number of operations
		// each thread attempts to do is valid

		if (!(0 < n)) {

			System.err.println("Error: n should be greater than 0.");
			valid = false;
		}

		// Checks that the timeout factor used in the elimination stack is valid
		if (!(0 <= t)) {

			System.err.println("Error: p should be greater than 0.");
			valid = false;
		}

		// Checks that the timeout factor used in the elimination stack is valid
		if (!(0 < e)) {

			System.err.println("Error: e should be greater than 0.");
			valid = false;
		}

		if (!valid) {
			System.err.println("Usage: java EliminationStack p d n t e");
		}

		return valid;
	}
}
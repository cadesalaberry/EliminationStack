EliminationStack
================

We will need to evaluate and tune the performance of a lock-free stack.

First, let's implement a lock-free stack. The stack needs to support two operations, PUSH and POP, capable of reusing nodes/data (re-PUSH-ing after POP-ing), and it is not possible to lose data or otherwise corrupt the stack.

Next, we will implement an elimination stack based on an elimination array, as described in the textbook. It resorts to the lock-free stack implementation if an exchange fails. The size of the elimination array and the timeout used to wait for an elimination partner should be parameters.

Stacks are tested by starting p threads that then repeatedly perform PUSH or POP operations on the stack, randomly choosing one operation or the other with equal probability. A thread should retain the last 10 items it popped, and when performing a PUSH, 50% of the time it should randomly select a previously popped node to re-push (if it has any). After each push/pop operation, a thread sleeps for a random time between `0`ms and `d`ms. Each thread should also keep track of how many pushes it does and how many pops successfully returned actual data.


The program can be invoked with 5 integer arguments as:
```bash
$ java EliminationStack p d n t e
```

Where `p > 1` represents the number of threads to use, `d > 0` represents the upper bound for the random delay between each thread operation, n the total number of operations each thread attempts to do, `t ≥ 0` represents the timeout factor used in the elimination stack, and `e > 0` is the size of the elimination array. All times are in milliseconds. Choose an `n > 1000` and a relatively brief `d`, such that execution takes at least several seconds for `t = 0`.

As output, The program (single-threadedly) emits a time in milliseconds measuring the entire concurrent simulation on one line. A second line of output contains three numbers separated by spaces: the first value is the total number of pushes done by all threads, the second the total number of successful pops done by all threads, and the third the total number of number of nodes remaining in the stack.

For each of `p ∈ 2, 4, 8, 16` what values/combination(s) of `e` and `t` usually works best? Note that we do not need to test all combinations(!), but we do need to provide a clear, numerical justification for answers, including performance graph(s) as appropriate, and your textual argument/explanation (as a separate file).

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

# Sample Output

```bash
$ java EliminationStack 2 10 1000 0 100
4765
1008 992 16
```

# Remarks

For each of `p ∈ 2, 4, 8, 16` what values/combination(s) of `e` and `t` usually works best? Note that we do not need to test all combinations(!), but we do need to provide a clear, numerical justification for answers, including performance graph(s) as appropriate.

> Let's use `t` and `e` as key variables. When the timeout factor (`t`) is increased, the total processing time increases **a lot**. Reducing its value leads to way more acceptable time. It makes sense for if a thread is blocked, it will wait a random amount of time, capped by `t`. On average, it will add `t/2` to the total processing time per times a thread is blocked.

> We can see in the table, that the size of the elimination array does not really matter; It is also surprising to see that the number of thread does not severly impact the timing. It shows that the implementation is pretty well done, not adding too much overhead in computation when adding more thread.


| p  | d | n    | t  | e   | Total Time (ms) | 
|----|---|------|----|-----|-----------------| 
| 2  | 5 | 1500 | 0  | 20  | 3218            | 
| 2  | 5 | 1500 | 0  | 40  | 3199            | 
| 2  | 5 | 1500 | 0  | 80  | 3239            | 
| 2  | 5 | 1500 | 0  | 160 | 3184            | 
| 2  | 5 | 1500 | 0  | 320 | 3414            | 
| 2  | 5 | 1500 | 0  | 640 | 3281            | 
|    |   |      |    |     |                 | 
| 2  | 5 | 1500 | 2  | 160 | 3465            | 
| 2  | 5 | 1500 | 4  | 160 | 3435            | 
| 2  | 5 | 1500 | 8  | 160 | 3776            | 
| 2  | 5 | 1500 | 16 | 160 | 4008            | 
| 2  | 5 | 1500 | 32 | 160 | 4031            | 
| 2  | 5 | 1500 | 64 | 160 | 4338            | 
|    |   |      |    |     |                 | 
| 4  | 5 | 1500 | 0  | 20  | 3318            | 
| 4  | 5 | 1500 | 0  | 40  | 3307            | 
| 4  | 5 | 1500 | 0  | 80  | 3237            | 
| 4  | 5 | 1500 | 0  | 160 | 3238            | 
| 4  | 5 | 1500 | 0  | 320 | 3195            | 
| 4  | 5 | 1500 | 0  | 640 | 3274            | 
|    |   |      |    |     |                 | 
| 4  | 5 | 1500 | 2  | 320 | 3670            | 
| 4  | 5 | 1500 | 4  | 320 | 3929            | 
| 4  | 5 | 1500 | 8  | 320 | 4075            | 
| 4  | 5 | 1500 | 16 | 320 | 4318            | 
| 4  | 5 | 1500 | 32 | 320 | 4329            | 
| 4  | 5 | 1500 | 64 | 320 | 4464            | 
|    |   |      |    |     |                 | 
| 8  | 5 | 1500 | 0  | 20  | 3308            | 
| 8  | 5 | 1500 | 0  | 40  | 3240            | 
| 8  | 5 | 1500 | 0  | 80  | 3207            | 
| 8  | 5 | 1500 | 0  | 160 | 3217            | 
| 8  | 5 | 1500 | 0  | 320 | 3251            | 
| 8  | 5 | 1500 | 0  | 640 | 3213            | 
|    |   |      |    |     |                 | 
| 8  | 5 | 1500 | 2  | 160 | 3858            | 
| 8  | 5 | 1500 | 4  | 160 | 4017            | 
| 8  | 5 | 1500 | 8  | 160 | 4206            | 
| 8  | 5 | 1500 | 16 | 160 | 4482            | 
| 8  | 5 | 1500 | 32 | 160 | 4817            | 
| 8  | 5 | 1500 | 64 | 160 | 6302            | 
|    |   |      |    |     |                 | 
| 16 | 5 | 1500 | 0  | 20  | 3258            | 
| 16 | 5 | 1500 | 0  | 40  | 3203            | 
| 16 | 5 | 1500 | 0  | 80  | 3234            | 
| 16 | 5 | 1500 | 0  | 160 | 3232            | 
| 16 | 5 | 1500 | 0  | 320 | 3251            | 
| 16 | 5 | 1500 | 0  | 640 | 3277            | 
|    |   |      |    |     |                 | 
| 16 | 5 | 1500 | 2  | 160 | 3876            | 
| 16 | 5 | 1500 | 4  | 160 | 4025            | 
| 16 | 5 | 1500 | 8  | 160 | 4290            | 
| 16 | 5 | 1500 | 16 | 160 | 4296            | 
| 16 | 5 | 1500 | 32 | 160 | 4590            | 
| 16 | 5 | 1500 | 64 | 160 | 5091            | 
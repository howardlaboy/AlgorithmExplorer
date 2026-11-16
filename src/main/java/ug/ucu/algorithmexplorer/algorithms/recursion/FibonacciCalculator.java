package ug.ucu.algorithmexplorer.algorithms.recursion;

import java.math.BigInteger;

import ug.ucu.algorithmexplorer.models.FibonacciMethod;
import ug.ucu.algorithmexplorer.models.FibonacciResult;

/**
 * Calculates Fibonacci numbers using recursive and dynamic-programming
 * procedures.
 *
 * Naive recursion has exponential running time.
 * Memoization and bottom-up calculation have O(n) running time.
 */
public class FibonacciCalculator {

    public static final int MAX_NAIVE_INPUT = 30;

    /**
     * Calculates Fibonacci using plain recursive calls.
     *
     * @param n Fibonacci position
     * @return result, recursive-call count and execution time
     */
    public FibonacciResult calculateNaive(int n) {
        validateNonNegative(n);

        if (n > MAX_NAIVE_INPUT) {
            throw new IllegalArgumentException(
                    "Naive Fibonacci is limited to n <= "
                            + MAX_NAIVE_INPUT
                            + " because its number of recursive calls "
                            + "grows very quickly."
            );
        }

        OperationCounter counter = new OperationCounter();

        long startTime = System.nanoTime();
        BigInteger value = naiveFibonacci(n, counter);
        long executionTime = System.nanoTime() - startTime;

        return new FibonacciResult(
                n,
                value,
                FibonacciMethod.NAIVE_RECURSIVE,
                counter.getCount(),
                executionTime
        );
    }

    /**
     * Calculates Fibonacci recursively while storing previously
     * calculated values.
     *
     * Time complexity: O(n)
     * Extra space: O(n)
     *
     * @param n Fibonacci position
     * @return result, recursive-call count and execution time
     */
    public FibonacciResult calculateMemoized(int n) {
        validateNonNegative(n);

        BigInteger[] memo = new BigInteger[n + 1];
        memo[0] = BigInteger.ZERO;

        if (n >= 1) {
            memo[1] = BigInteger.ONE;
        }

        OperationCounter counter = new OperationCounter();

        long startTime = System.nanoTime();
        BigInteger value = memoizedFibonacci(n, memo, counter);
        long executionTime = System.nanoTime() - startTime;

        return new FibonacciResult(
                n,
                value,
                FibonacciMethod.MEMOIZED_RECURSIVE,
                counter.getCount(),
                executionTime
        );
    }

    /**
     * Calculates Fibonacci by building results from F(0) upward.
     *
     * Time complexity: O(n)
     * Extra space: O(1)
     *
     * @param n Fibonacci position
     * @return result, iteration count and execution time
     */
    public FibonacciResult calculateBottomUp(int n) {
        validateNonNegative(n);

        long startTime = System.nanoTime();

        BigInteger value;
        long iterations = 0;

        if (n == 0) {
            value = BigInteger.ZERO;
        } else if (n == 1) {
            value = BigInteger.ONE;
        } else {
            BigInteger previous = BigInteger.ZERO;
            BigInteger current = BigInteger.ONE;

            for (int position = 2; position <= n; position++) {
                BigInteger next = previous.add(current);
                previous = current;
                current = next;
                iterations++;
            }

            value = current;
        }

        long executionTime = System.nanoTime() - startTime;

        return new FibonacciResult(
                n,
                value,
                FibonacciMethod.BOTTOM_UP,
                iterations,
                executionTime
        );
    }

    private BigInteger naiveFibonacci(
            int n,
            OperationCounter counter
    ) {
        counter.increment();

        if (n == 0) {
            return BigInteger.ZERO;
        }

        if (n == 1) {
            return BigInteger.ONE;
        }

        return naiveFibonacci(n - 1, counter)
                .add(naiveFibonacci(n - 2, counter));
    }

    private BigInteger memoizedFibonacci(
            int n,
            BigInteger[] memo,
            OperationCounter counter
    ) {
        counter.increment();

        if (memo[n] != null) {
            return memo[n];
        }

        memo[n] = memoizedFibonacci(n - 1, memo, counter)
                .add(memoizedFibonacci(n - 2, memo, counter));

        return memo[n];
    }

    private void validateNonNegative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "Fibonacci input must be zero or greater."
            );
        }
    }

    /**
     * Small internal helper used to count recursive calls.
     */
    private static final class OperationCounter {

        private long count;

        void increment() {
            count++;
        }

        long getCount() {
            return count;
        }
    }
}
package ug.ucu.algorithmexplorer.algorithms.recursion;

import java.math.BigInteger;

import ug.ucu.algorithmexplorer.models.FactorialMethod;
import ug.ucu.algorithmexplorer.models.FactorialResult;

/**
 * Calculates factorial values recursively and iteratively.
 *
 * Both procedures have O(n) time complexity.
 * The recursive procedure uses O(n) call-stack space.
 * The iterative procedure uses O(1) extra working space.
 */
public class FactorialCalculator {

    /**
     * Calculates n! recursively.
     *
     * The recursive-call count includes the base-case call.
     *
     * @param n non-negative input
     * @return factorial result and measurements
     */
    public FactorialResult calculateRecursive(int n) {
        validateInput(n);

        OperationCounter counter = new OperationCounter();

        long startTime = System.nanoTime();
        BigInteger value = recursiveFactorial(n, counter);
        long executionTime = System.nanoTime() - startTime;

        return new FactorialResult(
                n,
                value,
                FactorialMethod.RECURSIVE,
                counter.getCount(),
                executionTime
        );
    }

    /**
     * Calculates n! using a loop.
     *
     * The iteration count records the number of multiplications.
     *
     * @param n non-negative input
     * @return factorial result and measurements
     */
    public FactorialResult calculateIterative(int n) {
        validateInput(n);

        long startTime = System.nanoTime();

        BigInteger value = BigInteger.ONE;
        long iterations = 0;

        for (int number = 2; number <= n; number++) {
            value = value.multiply(BigInteger.valueOf(number));
            iterations++;
        }

        long executionTime = System.nanoTime() - startTime;

        return new FactorialResult(
                n,
                value,
                FactorialMethod.ITERATIVE,
                iterations,
                executionTime
        );
    }

    private BigInteger recursiveFactorial(
            int n,
            OperationCounter counter
    ) {
        counter.increment();

        if (n == 0 || n == 1) {
            return BigInteger.ONE;
        }

        return BigInteger.valueOf(n)
                .multiply(recursiveFactorial(n - 1, counter));
    }

    private void validateInput(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "Factorial input must be zero or greater."
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
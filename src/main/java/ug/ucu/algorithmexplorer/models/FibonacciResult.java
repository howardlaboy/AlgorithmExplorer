package ug.ucu.algorithmexplorer.models;

import java.math.BigInteger;

/**
 * Stores the output and measurements from one Fibonacci calculation.
 *
 * @param input the requested Fibonacci position
 * @param value the calculated Fibonacci number
 * @param method the procedure used
 * @param operationCount recursive calls or loop iterations
 * @param executionTimeNanos execution time in nanoseconds
 */
public record FibonacciResult(
        int input,
        BigInteger value,
        FibonacciMethod method,
        long operationCount,
        long executionTimeNanos
) {

    public FibonacciResult {
        if (input < 0) {
            throw new IllegalArgumentException(
                    "Fibonacci input cannot be negative."
            );
        }

        if (value == null) {
            throw new IllegalArgumentException(
                    "Fibonacci value cannot be null."
            );
        }

        if (method == null) {
            throw new IllegalArgumentException(
                    "Fibonacci method cannot be null."
            );
        }

        if (operationCount < 0) {
            throw new IllegalArgumentException(
                    "Operation count cannot be negative."
            );
        }

        if (executionTimeNanos < 0) {
            throw new IllegalArgumentException(
                    "Execution time cannot be negative."
            );
        }
    }

    /**
     * Converts the measured time from nanoseconds to milliseconds.
     *
     * @return execution time in milliseconds
     */
    public double executionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }
}
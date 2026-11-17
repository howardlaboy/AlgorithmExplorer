package ug.ucu.algorithmexplorer.models;

import java.math.BigInteger;

/**
 * Stores the output and measurements from one factorial calculation.
 *
 * @param input the number whose factorial was calculated
 * @param value the calculated factorial
 * @param method the procedure used
 * @param operationCount recursive calls or loop iterations
 * @param executionTimeNanos execution time in nanoseconds
 */
public record FactorialResult(
        int input,
        BigInteger value,
        FactorialMethod method,
        long operationCount,
        long executionTimeNanos
) {

    public FactorialResult {
        if (input < 0) {
            throw new IllegalArgumentException(
                    "Factorial input cannot be negative."
            );
        }

        if (value == null) {
            throw new IllegalArgumentException(
                    "Factorial value cannot be null."
            );
        }

        if (method == null) {
            throw new IllegalArgumentException(
                    "Factorial method cannot be null."
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
     * Converts the measured execution time to milliseconds.
     *
     * @return execution time in milliseconds
     */
    public double executionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }
}
package ug.ucu.algorithmexplorer.models;

import java.util.List;

/**
 * Stores the complete outcome of a balanced-bracket check.
 *
 * @param expression original expression
 * @param balanced whether all brackets are correctly balanced
 * @param message readable outcome
 * @param steps state recorded after each processed symbol
 * @param stackOperationCount number of push, peek and pop operations
 * @param executionTimeNanos execution time in nanoseconds
 */
public record BracketCheckResult(
        String expression,
        boolean balanced,
        String message,
        List<BracketCheckStep> steps,
        long stackOperationCount,
        long executionTimeNanos
) {

    public BracketCheckResult {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException(
                    "Expression cannot be empty."
            );
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Result message cannot be empty."
            );
        }

        if (steps == null) {
            throw new IllegalArgumentException(
                    "Step list cannot be null."
            );
        }

        if (stackOperationCount < 0) {
            throw new IllegalArgumentException(
                    "Stack-operation count cannot be negative."
            );
        }

        if (executionTimeNanos < 0) {
            throw new IllegalArgumentException(
                    "Execution time cannot be negative."
            );
        }

        steps = List.copyOf(steps);
    }

    /**
     * Converts execution time from nanoseconds to milliseconds.
     *
     * @return execution time in milliseconds
     */
    public double executionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }
}
package ug.ucu.algorithmexplorer.models.sorting;

import java.util.List;

/**
 * Stores a complete sorting run and its measurements.
 *
 * @param algorithmName algorithm used
 * @param originalArray original unsorted input
 * @param sortedArray final sorted output
 * @param steps visual sorting steps
 * @param comparisonCount number of element comparisons
 * @param swapCount number of swaps
 * @param moveCount number of individual value assignments or moves
 * @param executionTimeNanos measured execution time
 */
public record SortResult(
        String algorithmName,
        int[] originalArray,
        int[] sortedArray,
        List<SortStep> steps,
        long comparisonCount,
        long swapCount,
        long moveCount,
        long executionTimeNanos
) {

    public SortResult {
        if (algorithmName == null || algorithmName.isBlank()) {
            throw new IllegalArgumentException(
                    "Algorithm name cannot be empty."
            );
        }

        if (originalArray == null || sortedArray == null) {
            throw new IllegalArgumentException(
                    "Sorting arrays cannot be null."
            );
        }

        if (steps == null) {
            throw new IllegalArgumentException(
                    "Sorting steps cannot be null."
            );
        }

        if (comparisonCount < 0
                || swapCount < 0
                || moveCount < 0
                || executionTimeNanos < 0) {

            throw new IllegalArgumentException(
                    "Sorting measurements cannot be negative."
            );
        }

        originalArray = copyArray(originalArray);
        sortedArray = copyArray(sortedArray);
        steps = List.copyOf(steps);
    }

    @Override
    public int[] originalArray() {
        return copyArray(originalArray);
    }

    @Override
    public int[] sortedArray() {
        return copyArray(sortedArray);
    }

    /**
     * Returns execution time in milliseconds.
     *
     * @return execution time in milliseconds
     */
    public double executionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }

    /**
     * Returns the number of recorded visual steps.
     *
     * @return step count
     */
    public int stepCount() {
        return steps.size();
    }

    private static int[] copyArray(int[] source) {
        int[] copy = new int[source.length];

        for (int index = 0; index < source.length; index++) {
            copy[index] = source[index];
        }

        return copy;
    }
}
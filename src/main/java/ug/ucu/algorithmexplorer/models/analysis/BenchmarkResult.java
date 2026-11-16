package ug.ucu.algorithmexplorer.models.analysis;

/**
 * Stores the averaged measurements for one experimental configuration.
 *
 * @param algorithmName sorting algorithm
 * @param inputType input arrangement
 * @param inputSize number of values
 * @param repetitions number of measured runs
 * @param averageTimeNanos average execution time in nanoseconds
 * @param minimumTimeNanos shortest measured execution time
 * @param maximumTimeNanos longest measured execution time
 * @param averageComparisons average element-comparison count
 * @param averageSwaps average swap count
 * @param averageMoves average move count
 */
public record BenchmarkResult(
        String algorithmName,
        BenchmarkInputType inputType,
        int inputSize,
        int repetitions,
        double averageTimeNanos,
        long minimumTimeNanos,
        long maximumTimeNanos,
        double averageComparisons,
        double averageSwaps,
        double averageMoves
) {

    public BenchmarkResult {
        if (algorithmName == null || algorithmName.isBlank()) {
            throw new IllegalArgumentException(
                    "Algorithm name cannot be empty."
            );
        }

        if (inputType == null) {
            throw new IllegalArgumentException(
                    "Input type cannot be null."
            );
        }

        if (inputSize < 1) {
            throw new IllegalArgumentException(
                    "Input size must be at least 1."
            );
        }

        if (repetitions < 1) {
            throw new IllegalArgumentException(
                    "Repetitions must be at least 1."
            );
        }

        if (averageTimeNanos < 0
                || minimumTimeNanos < 0
                || maximumTimeNanos < 0
                || averageComparisons < 0
                || averageSwaps < 0
                || averageMoves < 0) {

            throw new IllegalArgumentException(
                    "Benchmark measurements cannot be negative."
            );
        }

        if (minimumTimeNanos > maximumTimeNanos) {
            throw new IllegalArgumentException(
                    "Minimum time cannot exceed maximum time."
            );
        }
    }

    /**
     * Returns average execution time in milliseconds.
     *
     * @return average milliseconds
     */
    public double averageTimeMillis() {
        return averageTimeNanos / 1_000_000.0;
    }

    /**
     * Returns minimum execution time in milliseconds.
     *
     * @return minimum milliseconds
     */
    public double minimumTimeMillis() {
        return minimumTimeNanos / 1_000_000.0;
    }

    /**
     * Returns maximum execution time in milliseconds.
     *
     * @return maximum milliseconds
     */
    public double maximumTimeMillis() {
        return maximumTimeNanos / 1_000_000.0;
    }
}
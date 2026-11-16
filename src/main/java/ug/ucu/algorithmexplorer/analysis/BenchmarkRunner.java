package ug.ucu.algorithmexplorer.analysis;

import ug.ucu.algorithmexplorer.models.analysis.BenchmarkInputType;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkResult;

/**
 * Runs repeatable sorting experiments without visualization snapshots.
 */
public class BenchmarkRunner {

    /**
     * Executes one benchmark configuration repeatedly.
     *
     * A fresh copy of the original input is supplied to every run.
     *
     * @param algorithm benchmark-only sorting implementation
     * @param inputType input arrangement
     * @param inputSize number of values
     * @param repetitions measured runs
     * @param generator input generator
     * @return averaged benchmark result
     */
    public BenchmarkResult run(
            BenchmarkSortingAlgorithm algorithm,
            BenchmarkInputType inputType,
            int inputSize,
            int repetitions,
            BenchmarkInputGenerator generator
    ) {
        validate(
                algorithm,
                inputType,
                inputSize,
                repetitions,
                generator
        );

        int[] originalInput =
                generator.generate(
                        inputSize,
                        inputType
                );

        long totalTime = 0;
        long minimumTime = Long.MAX_VALUE;
        long maximumTime = 0;

        long totalComparisons = 0;
        long totalSwaps = 0;
        long totalMoves = 0;

        for (int repetition = 0;
                repetition < repetitions;
                repetition++) {

            int[] workingCopy =
                    copyArray(originalInput);

            BenchmarkMeasurement measurement =
                    algorithm.sortAndMeasure(
                            workingCopy
                    );

            verifySorted(workingCopy);

            totalTime +=
                    measurement.executionTimeNanos();

            totalComparisons +=
                    measurement.comparisonCount();

            totalSwaps +=
                    measurement.swapCount();

            totalMoves +=
                    measurement.moveCount();

            if (measurement.executionTimeNanos()
                    < minimumTime) {

                minimumTime =
                        measurement.executionTimeNanos();
            }

            if (measurement.executionTimeNanos()
                    > maximumTime) {

                maximumTime =
                        measurement.executionTimeNanos();
            }
        }

        return new BenchmarkResult(
                algorithm.getName(),
                inputType,
                inputSize,
                repetitions,
                totalTime / (double) repetitions,
                minimumTime,
                maximumTime,
                totalComparisons / (double) repetitions,
                totalSwaps / (double) repetitions,
                totalMoves / (double) repetitions
        );
    }

    private void validate(
            BenchmarkSortingAlgorithm algorithm,
            BenchmarkInputType inputType,
            int inputSize,
            int repetitions,
            BenchmarkInputGenerator generator
    ) {
        if (algorithm == null) {
            throw new IllegalArgumentException(
                    "Benchmark algorithm cannot be null."
            );
        }

        if (inputType == null) {
            throw new IllegalArgumentException(
                    "Benchmark input type cannot be null."
            );
        }

        if (inputSize < 1) {
            throw new IllegalArgumentException(
                    "Benchmark input size must be at least 1."
            );
        }

        if (repetitions < 3) {
            throw new IllegalArgumentException(
                    "Run each benchmark at least 3 times."
            );
        }

        if (generator == null) {
            throw new IllegalArgumentException(
                    "Benchmark input generator cannot be null."
            );
        }
    }

    private void verifySorted(int[] values) {
        for (int index = 1;
                index < values.length;
                index++) {

            if (values[index - 1] > values[index]) {
                throw new IllegalStateException(
                        "Benchmark algorithm produced "
                                + "an unsorted result."
                );
            }
        }
    }

    private int[] copyArray(int[] source) {
        int[] copy = new int[source.length];

        for (int index = 0;
                index < source.length;
                index++) {

            copy[index] = source[index];
        }

        return copy;
    }

    /**
     * Contract for sorting implementations used during benchmarking.
     */
    public interface BenchmarkSortingAlgorithm {

        /**
         * Sorts the supplied array in place and returns measurements.
         *
         * @param values modifiable working array
         * @return operation counts and execution time
         */
        BenchmarkMeasurement sortAndMeasure(int[] values);

        /**
         * Returns the readable algorithm name.
         *
         * @return algorithm name
         */
        String getName();
    }

    /**
     * Stores one measured sorting run.
     *
     * @param comparisonCount comparisons performed
     * @param swapCount swaps performed
     * @param moveCount moves performed
     * @param executionTimeNanos elapsed nanoseconds
     */
    public record BenchmarkMeasurement(
            long comparisonCount,
            long swapCount,
            long moveCount,
            long executionTimeNanos
    ) {

        public BenchmarkMeasurement {
            if (comparisonCount < 0
                    || swapCount < 0
                    || moveCount < 0
                    || executionTimeNanos < 0) {

                throw new IllegalArgumentException(
                        "Benchmark measurements "
                                + "cannot be negative."
                );
            }
        }
    }
}
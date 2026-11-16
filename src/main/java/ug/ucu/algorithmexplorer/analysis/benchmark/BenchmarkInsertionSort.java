package ug.ucu.algorithmexplorer.analysis.benchmark;

import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkMeasurement;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkSortingAlgorithm;

/**
 * Benchmark-only Insertion Sort.
 *
 * This implementation sorts in place and does not create
 * visualization snapshots.
 *
 * Best case: O(n)
 * Average case: O(n^2)
 * Worst case: O(n^2)
 * Extra space: O(1)
 */
public class BenchmarkInsertionSort
        implements BenchmarkSortingAlgorithm {

    @Override
    public BenchmarkMeasurement sortAndMeasure(
            int[] values
    ) {
        validateValues(values);

        long comparisons = 0;
        long moves = 0;

        long startTime = System.nanoTime();

        for (int currentIndex = 1;
                currentIndex < values.length;
                currentIndex++) {

            int valueToInsert =
                    values[currentIndex];

            int previousIndex =
                    currentIndex - 1;

            while (previousIndex >= 0) {
                comparisons++;

                if (values[previousIndex]
                        > valueToInsert) {

                    values[previousIndex + 1] =
                            values[previousIndex];

                    moves++;
                    previousIndex--;
                } else {
                    break;
                }
            }

            values[previousIndex + 1] =
                    valueToInsert;

            moves++;
        }

        long executionTime =
                System.nanoTime() - startTime;

        return new BenchmarkMeasurement(
                comparisons,
                0,
                moves,
                executionTime
        );
    }

    @Override
    public String getName() {
        return "Insertion Sort";
    }

    private void validateValues(int[] values) {
        if (values == null) {
            throw new IllegalArgumentException(
                    "Benchmark values cannot be null."
            );
        }

        if (values.length == 0) {
            throw new IllegalArgumentException(
                    "Benchmark values cannot be empty."
            );
        }
    }
}
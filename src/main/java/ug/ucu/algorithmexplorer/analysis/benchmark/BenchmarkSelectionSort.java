package ug.ucu.algorithmexplorer.analysis.benchmark;

import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkMeasurement;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkSortingAlgorithm;

/**
 * Benchmark-only Selection Sort.
 *
 * This implementation sorts in place and does not create
 * visualization snapshots.
 *
 * Best case: O(n^2)
 * Average case: O(n^2)
 * Worst case: O(n^2)
 * Extra space: O(1)
 */
public class BenchmarkSelectionSort
        implements BenchmarkSortingAlgorithm {

    @Override
    public BenchmarkMeasurement sortAndMeasure(
            int[] values
    ) {
        validateValues(values);

        long comparisons = 0;
        long swaps = 0;
        long moves = 0;

        long startTime = System.nanoTime();

        for (int currentPosition = 0;
                currentPosition < values.length - 1;
                currentPosition++) {

            int minimumIndex = currentPosition;

            for (int candidateIndex =
                    currentPosition + 1;
                    candidateIndex < values.length;
                    candidateIndex++) {

                comparisons++;

                if (values[candidateIndex]
                        < values[minimumIndex]) {

                    minimumIndex = candidateIndex;
                }
            }

            if (minimumIndex != currentPosition) {
                int temporary =
                        values[currentPosition];

                values[currentPosition] =
                        values[minimumIndex];

                values[minimumIndex] =
                        temporary;

                swaps++;
                moves += 3;
            }
        }

        long executionTime =
                System.nanoTime() - startTime;

        return new BenchmarkMeasurement(
                comparisons,
                swaps,
                moves,
                executionTime
        );
    }

    @Override
    public String getName() {
        return "Selection Sort";
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
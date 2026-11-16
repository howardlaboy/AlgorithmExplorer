package ug.ucu.algorithmexplorer.analysis.benchmark;

import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkMeasurement;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkSortingAlgorithm;

/**
 * Benchmark-only Bubble Sort.
 *
 * This implementation sorts in place and does not create
 * visualization snapshots.
 *
 * Best case: O(n)
 * Average case: O(n^2)
 * Worst case: O(n^2)
 * Extra space: O(1)
 */
public class BenchmarkBubbleSort
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

        for (int pass = 0;
                pass < values.length - 1;
                pass++) {

            boolean swappedDuringPass = false;

            int unsortedEnd =
                    values.length - 1 - pass;

            for (int index = 0;
                    index < unsortedEnd;
                    index++) {

                comparisons++;

                if (values[index]
                        > values[index + 1]) {

                    int temporary = values[index];
                    values[index] = values[index + 1];
                    values[index + 1] = temporary;

                    swaps++;
                    moves += 3;
                    swappedDuringPass = true;
                }
            }

            if (!swappedDuringPass) {
                break;
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
        return "Bubble Sort";
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
package ug.ucu.algorithmexplorer.analysis.benchmark;

import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkMeasurement;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkSortingAlgorithm;

/**
 * Benchmark-only Merge Sort.
 *
 * The implementation sorts the supplied array in place and does not
 * create visualization snapshots.
 *
 * Best case: O(n log n)
 * Average case: O(n log n)
 * Worst case: O(n log n)
 * Extra space: O(n)
 */
public class BenchmarkMergeSort
        implements BenchmarkSortingAlgorithm {

    private long comparisonCount;
    private long moveCount;

    @Override
    public BenchmarkMeasurement sortAndMeasure(
            int[] values
    ) {
        validateValues(values);

        comparisonCount = 0;
        moveCount = 0;

        int[] temporary =
                new int[values.length];

        long startTime = System.nanoTime();

        if (values.length > 1) {
            mergeSort(
                    values,
                    temporary,
                    0,
                    values.length - 1
            );
        }

        long executionTime =
                System.nanoTime() - startTime;

        return new BenchmarkMeasurement(
                comparisonCount,
                0,
                moveCount,
                executionTime
        );
    }

    private void mergeSort(
            int[] values,
            int[] temporary,
            int left,
            int right
    ) {
        if (left >= right) {
            return;
        }

        int middle =
                left + (right - left) / 2;

        mergeSort(
                values,
                temporary,
                left,
                middle
        );

        mergeSort(
                values,
                temporary,
                middle + 1,
                right
        );

        merge(
                values,
                temporary,
                left,
                middle,
                right
        );
    }

    private void merge(
            int[] values,
            int[] temporary,
            int left,
            int middle,
            int right
    ) {
        int leftIndex = left;
        int rightIndex = middle + 1;
        int temporaryIndex = left;

        while (leftIndex <= middle
                && rightIndex <= right) {

            comparisonCount++;

            if (values[leftIndex]
                    <= values[rightIndex]) {

                temporary[temporaryIndex] =
                        values[leftIndex];

                leftIndex++;
            } else {
                temporary[temporaryIndex] =
                        values[rightIndex];

                rightIndex++;
            }

            temporaryIndex++;
            moveCount++;
        }

        while (leftIndex <= middle) {
            temporary[temporaryIndex] =
                    values[leftIndex];

            leftIndex++;
            temporaryIndex++;
            moveCount++;
        }

        while (rightIndex <= right) {
            temporary[temporaryIndex] =
                    values[rightIndex];

            rightIndex++;
            temporaryIndex++;
            moveCount++;
        }

        for (int index = left;
                index <= right;
                index++) {

            values[index] = temporary[index];
            moveCount++;
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
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
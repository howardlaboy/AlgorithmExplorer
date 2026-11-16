package ug.ucu.algorithmexplorer.analysis.benchmark;

import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkMeasurement;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkSortingAlgorithm;

/**
 * Benchmark-only first-pivot Quick Sort.
 *
 * The first element in each active range is used as the pivot. The
 * partition follows the yellow/green procedure used in Module C.
 *
 * An explicit stack is used instead of recursive method calls. This
 * protects the Java call stack when ordered inputs create extremely
 * unbalanced partitions.
 *
 * Best case: O(n log n)
 * Average case: O(n log n)
 * Worst case: O(n^2)
 * Extra space: O(n) for the explicit range stack
 */
public class BenchmarkFirstPivotQuickSort
        implements BenchmarkSortingAlgorithm {

    private long comparisonCount;
    private long swapCount;
    private long moveCount;

    @Override
    public BenchmarkMeasurement sortAndMeasure(
            int[] values
    ) {
        validateValues(values);

        comparisonCount = 0;
        swapCount = 0;
        moveCount = 0;

        long startTime = System.nanoTime();

        if (values.length > 1) {
            quickSortIteratively(values);
        }

        long executionTime =
                System.nanoTime() - startTime;

        return new BenchmarkMeasurement(
                comparisonCount,
                swapCount,
                moveCount,
                executionTime
        );
    }

    /**
     * Sorts using an explicit stack of inclusive index ranges.
     *
     * Each range uses two stack positions:
     *
     * low, high
     *
     * @param values working array
     */
    private void quickSortIteratively(int[] values) {
        int[] rangeStack =
                new int[values.length * 2];

        int top = -1;

        top = pushRange(
                rangeStack,
                top,
                0,
                values.length - 1
        );

        while (top >= 1) {
            int high = rangeStack[top--];
            int low = rangeStack[top--];

            if (low >= high) {
                continue;
            }

            int pivotIndex =
                    partition(
                            values,
                            low,
                            high
                    );

            /*
             * Push the larger range first and the smaller range
             * second. The smaller range is therefore processed next.
             */
            int leftLow = low;
            int leftHigh = pivotIndex - 1;

            int rightLow = pivotIndex + 1;
            int rightHigh = high;

            int leftSize =
                    leftHigh - leftLow + 1;

            int rightSize =
                    rightHigh - rightLow + 1;

            if (leftSize > rightSize) {
                if (leftLow < leftHigh) {
                    top = pushRange(
                            rangeStack,
                            top,
                            leftLow,
                            leftHigh
                    );
                }

                if (rightLow < rightHigh) {
                    top = pushRange(
                            rangeStack,
                            top,
                            rightLow,
                            rightHigh
                    );
                }
            } else {
                if (rightLow < rightHigh) {
                    top = pushRange(
                            rangeStack,
                            top,
                            rightLow,
                            rightHigh
                    );
                }

                if (leftLow < leftHigh) {
                    top = pushRange(
                            rangeStack,
                            top,
                            leftLow,
                            leftHigh
                    );
                }
            }
        }
    }

    /**
     * Applies the first-pivot yellow/green partition.
     *
     * @param values working array
     * @param low first active index
     * @param high final active index
     * @return final pivot index
     */
    private int partition(
            int[] values,
            int low,
            int high
    ) {
        int pivotValue = values[low];
        int greenIndex = low;

        for (int yellowIndex = low + 1;
                yellowIndex <= high;
                yellowIndex++) {

            comparisonCount++;

            if (values[yellowIndex]
                    <= pivotValue) {

                greenIndex++;

                if (greenIndex != yellowIndex) {
                    swap(
                            values,
                            greenIndex,
                            yellowIndex
                    );
                }
            }
        }

        if (greenIndex != low) {
            swap(
                    values,
                    low,
                    greenIndex
            );
        }

        return greenIndex;
    }

    private int pushRange(
            int[] rangeStack,
            int top,
            int low,
            int high
    ) {
        rangeStack[++top] = low;
        rangeStack[++top] = high;

        return top;
    }

    private void swap(
            int[] values,
            int firstIndex,
            int secondIndex
    ) {
        int temporary = values[firstIndex];
        values[firstIndex] = values[secondIndex];
        values[secondIndex] = temporary;

        swapCount++;
        moveCount += 3;
    }

    @Override
    public String getName() {
        return "Quick Sort - First Pivot";
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
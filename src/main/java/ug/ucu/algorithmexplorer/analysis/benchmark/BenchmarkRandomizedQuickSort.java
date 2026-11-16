package ug.ucu.algorithmexplorer.analysis.benchmark;

import java.util.Random;

import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkMeasurement;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkSortingAlgorithm;

/**
 * Benchmark-only randomized-pivot Quick Sort.
 *
 * A random pivot is selected from each active range and moved to its
 * first position. The standard yellow/green partition is then used.
 *
 * An explicit stack replaces recursive method calls.
 *
 * Best case: O(n log n)
 * Expected case: O(n log n)
 * Worst case: O(n^2)
 * Extra space: O(n) for the explicit range stack
 */
public class BenchmarkRandomizedQuickSort
        implements BenchmarkSortingAlgorithm {

    private final Random random;

    private long comparisonCount;
    private long swapCount;
    private long moveCount;

    /**
     * Creates a sorter using normal runtime randomness.
     */
    public BenchmarkRandomizedQuickSort() {
        this(new Random());
    }

    /**
     * Creates a repeatable sorter for tests and experiments.
     *
     * @param seed random-number seed
     */
    public BenchmarkRandomizedQuickSort(long seed) {
        this(new Random(seed));
    }

    private BenchmarkRandomizedQuickSort(
            Random random
    ) {
        if (random == null) {
            throw new IllegalArgumentException(
                    "Random-number generator cannot be null."
            );
        }

        this.random = random;
    }

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

    private void quickSortIteratively(
            int[] values
    ) {
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
                    randomizedPartition(
                            values,
                            low,
                            high
                    );

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

    private int randomizedPartition(
            int[] values,
            int low,
            int high
    ) {
        int selectedPivotIndex =
                low + random.nextInt(
                        high - low + 1
                );

        if (selectedPivotIndex != low) {
            swap(
                    values,
                    low,
                    selectedPivotIndex
            );
        }

        return partition(
                values,
                low,
                high
        );
    }

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
        return "Quick Sort - Randomized Pivot";
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
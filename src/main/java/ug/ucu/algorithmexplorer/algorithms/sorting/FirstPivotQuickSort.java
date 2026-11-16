package ug.ucu.algorithmexplorer.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

/**
 * Sorts integer arrays using recursive Quick Sort with the first
 * element as the pivot.
 *
 * The partition uses a yellow scanning index and a green boundary:
 *
 * - the first element is the pivot;
 * - yellow scans from left to right;
 * - green marks the end of values less than or equal to the pivot;
 * - qualifying values are moved into the green section;
 * - the pivot is finally placed between the two partitions.
 *
 * Best-case time: O(n log n)
 * Average-case time: O(n log n)
 * Worst-case time: O(n^2)
 * Extra space: O(log n) average recursion stack,
 *              O(n) worst-case recursion stack
 * Stable: no
 */
public class FirstPivotQuickSort
        implements SortingAlgorithm {

    private long comparisonCount;
    private long swapCount;
    private long moveCount;
    private int stepNumber;

    private List<SortStep> steps;

    @Override
    public SortResult sort(int[] input) {
        validateInput(input);

        int[] values = copyArray(input);
        int[] original = copyArray(input);

        comparisonCount = 0;
        swapCount = 0;
        moveCount = 0;
        stepNumber = 0;

        steps = new ArrayList<>();

        long startTime = System.nanoTime();

        if (values.length > 1) {
            quickSort(
                    values,
                    0,
                    values.length - 1
            );
        }

        long executionTime =
                System.nanoTime() - startTime;

        return new SortResult(
                getName(),
                original,
                values,
                steps,
                comparisonCount,
                swapCount,
                moveCount,
                executionTime
        );
    }

    /**
     * Recursively sorts the inclusive range [low, high].
     *
     * @param values working array
     * @param low first index in the current range
     * @param high final index in the current range
     */
    private void quickSort(
            int[] values,
            int low,
            int high
    ) {
        if (low >= high) {
            return;
        }

        int pivotIndex = partition(
                values,
                low,
                high
        );

        quickSort(
                values,
                low,
                pivotIndex - 1
        );

        quickSort(
                values,
                pivotIndex + 1,
                high
        );
    }

    /**
     * Partitions the range using its first element as the pivot.
     *
     * Yellow scans the unsorted values. Green marks the last element
     * known to be less than or equal to the pivot.
     *
     * @param values working array
     * @param low first index in the current range
     * @param high final index in the current range
     * @return final pivot index
     */
    private int partition(
            int[] values,
            int low,
            int high
    ) {
        int pivotValue = values[low];

        /*
         * Green initially points at the pivot. Values in the range
         * [low + 1, green] will become the <= pivot section.
         */
        int greenIndex = low;

        /*
         * Yellow scans every value after the pivot.
         */
        for (int yellowIndex = low + 1;
                yellowIndex <= high;
                yellowIndex++) {

            comparisonCount++;

            if (values[yellowIndex] <= pivotValue) {
                greenIndex++;

                /*
                 * Avoid counting a self-swap when yellow and green
                 * already refer to the same position.
                 */
                if (greenIndex != yellowIndex) {
                    swap(
                            values,
                            greenIndex,
                            yellowIndex
                    );
                }
            }
        }

        /*
         * Move the pivot from the first position to the boundary
         * between the <= pivot and > pivot sections.
         */
        if (greenIndex != low) {
            swap(
                    values,
                    low,
                    greenIndex
            );
        }

        stepNumber++;

        steps.add(
                new SortStep(
                        stepNumber,
                        createStepDescription(
                                stepNumber,
                                pivotValue,
                                low,
                                high,
                                greenIndex
                        ),
                        values,
                        new int[]{
                                greenIndex
                        }
                )
        );

        return greenIndex;
    }

    /**
     * Exchanges two distinct array positions.
     *
     * One exchange counts as one swap and three moves.
     *
     * @param values working array
     * @param firstIndex first position
     * @param secondIndex second position
     */
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

    private String createStepDescription(
            int currentStep,
            int pivotValue,
            int low,
            int high,
            int pivotIndex
    ) {
        return "Partition "
                + currentStep
                + ": used first-element pivot "
                + pivotValue
                + " for range ["
                + low
                + ", "
                + high
                + "]. Yellow scanned the range, green marked "
                + "the <= pivot section, and the pivot finished "
                + "at index "
                + pivotIndex
                + ".";
    }

    @Override
    public String getName() {
        return "Quick Sort - First Pivot";
    }

    @Override
    public String getBestCase() {
        return "O(n log n)";
    }

    @Override
    public String getAverageCase() {
        return "O(n log n)";
    }

    @Override
    public String getWorstCase() {
        return "O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(log n) average, O(n) worst";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    private void validateInput(int[] input) {
        if (input == null) {
            throw new IllegalArgumentException(
                    "Sorting input cannot be null."
            );
        }

        if (input.length == 0) {
            throw new IllegalArgumentException(
                    "Enter at least one number to sort."
            );
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
}
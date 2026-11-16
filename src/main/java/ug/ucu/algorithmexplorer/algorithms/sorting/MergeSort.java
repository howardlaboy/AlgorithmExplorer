package ug.ucu.algorithmexplorer.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

/**
 * Sorts integer arrays using Merge Sort.
 *
 * Merge Sort recursively divides the array into smaller sections,
 * sorts those sections, and merges them back together.
 *
 * Best-case time: O(n log n)
 * Average-case time: O(n log n)
 * Worst-case time: O(n log n)
 * Extra space: O(n), excluding result snapshots for visualisation
 * Stable: yes
 */
public class MergeSort implements SortingAlgorithm {

    private long comparisonCount;
    private long moveCount;
    private int stepNumber;

    private List<SortStep> steps;

    @Override
    public SortResult sort(int[] input) {
        validateInput(input);

        int[] values = copyArray(input);
        int[] original = copyArray(input);
        int[] temporary = new int[values.length];

        comparisonCount = 0;
        moveCount = 0;
        stepNumber = 0;
        steps = new ArrayList<>();

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

        return new SortResult(
                getName(),
                original,
                values,
                steps,
                comparisonCount,
                0,
                moveCount,
                executionTime
        );
    }

    /**
     * Recursively sorts the specified inclusive array range.
     *
     * @param values array being sorted
     * @param temporary auxiliary merge array
     * @param left first index in the range
     * @param right final index in the range
     */
    private void mergeSort(
            int[] values,
            int[] temporary,
            int left,
            int right
    ) {
        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;

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

    /**
     * Merges two adjacent sorted sections:
     *
     * left section:  [left, middle]
     * right section: [middle + 1, right]
     *
     * @param values array being sorted
     * @param temporary auxiliary merge array
     * @param left first index of the left section
     * @param middle final index of the left section
     * @param right final index of the right section
     */
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

            /*
             * The <= comparison preserves stability. When two values
             * are equal, the value from the left section is selected
             * first.
             */
            if (values[leftIndex] <= values[rightIndex]) {
                temporary[temporaryIndex] =
                        values[leftIndex];

                leftIndex++;
            } else {
                temporary[temporaryIndex] =
                        values[rightIndex];

                rightIndex++;
            }

            moveCount++;
            temporaryIndex++;
        }

        while (leftIndex <= middle) {
            temporary[temporaryIndex] =
                    values[leftIndex];

            moveCount++;
            leftIndex++;
            temporaryIndex++;
        }

        while (rightIndex <= right) {
            temporary[temporaryIndex] =
                    values[rightIndex];

            moveCount++;
            rightIndex++;
            temporaryIndex++;
        }

        for (int index = left;
                index <= right;
                index++) {

            values[index] = temporary[index];
            moveCount++;
        }

        stepNumber++;

        steps.add(
                new SortStep(
                        stepNumber,
                        "Merge "
                                + stepNumber
                                + ": combined sorted ranges ["
                                + left
                                + ", "
                                + middle
                                + "] and ["
                                + (middle + 1)
                                + ", "
                                + right
                                + "] into range ["
                                + left
                                + ", "
                                + right
                                + "].",
                        values,
                        createRangeIndices(left, right)
                )
        );
    }

    /**
     * Creates all indices inside a merged section for highlighting.
     *
     * @param left first highlighted index
     * @param right final highlighted index
     * @return indices from left through right
     */
    private int[] createRangeIndices(
            int left,
            int right
    ) {
        int[] indices = new int[right - left + 1];

        for (int offset = 0;
                offset < indices.length;
                offset++) {

            indices[offset] = left + offset;
        }

        return indices;
    }

    @Override
    public String getName() {
        return "Merge Sort";
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
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }

    @Override
    public boolean isStable() {
        return true;
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

        for (int index = 0; index < source.length; index++) {
            copy[index] = source[index];
        }

        return copy;
    }
}
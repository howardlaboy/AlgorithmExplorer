package ug.ucu.algorithmexplorer.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

/**
 * Sorts integer arrays using Insertion Sort.
 *
 * Insertion Sort maintains a sorted section on the left. During each
 * pass, the next value is inserted into its correct position within
 * that sorted section.
 *
 * Best-case time: O(n)
 * Average-case time: O(n^2)
 * Worst-case time: O(n^2)
 * Extra space: O(1), excluding result snapshots for visualisation
 * Stable: yes
 */
public class InsertionSort implements SortingAlgorithm {

    @Override
    public SortResult sort(int[] input) {
        validateInput(input);

        int[] values = copyArray(input);
        int[] original = copyArray(input);

        List<SortStep> steps = new ArrayList<>();

        long comparisons = 0;
        long swaps = 0;
        long moves = 0;

        long startTime = System.nanoTime();

        for (int currentIndex = 1;
                currentIndex < values.length;
                currentIndex++) {

            int valueToInsert = values[currentIndex];
            int previousIndex = currentIndex - 1;
            int shiftCount = 0;

            /*
             * Compare the value to insert with values in the sorted
             * left section. Larger values move one position right.
             */
            while (previousIndex >= 0) {
                comparisons++;

                if (values[previousIndex] > valueToInsert) {
                    values[previousIndex + 1] =
                            values[previousIndex];

                    moves++;
                    shiftCount++;
                    previousIndex--;
                } else {
                    break;
                }
            }

            int insertionIndex = previousIndex + 1;

            values[insertionIndex] = valueToInsert;
            moves++;

            String description;

            if (shiftCount == 0) {
                description =
                        "Pass "
                                + currentIndex
                                + ": value "
                                + valueToInsert
                                + " was already in the correct "
                                + "position at index "
                                + insertionIndex
                                + ".";

            } else {
                description =
                        "Pass "
                                + currentIndex
                                + ": inserted value "
                                + valueToInsert
                                + " at index "
                                + insertionIndex
                                + " after shifting "
                                + shiftCount
                                + " value(s) to the right.";
            }

            steps.add(
                    new SortStep(
                            currentIndex,
                            description,
                            values,
                            new int[]{
                                    insertionIndex,
                                    currentIndex
                            }
                    )
            );
        }

        long executionTime =
                System.nanoTime() - startTime;

        return new SortResult(
                getName(),
                original,
                values,
                steps,
                comparisons,
                swaps,
                moves,
                executionTime
        );
    }

    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public String getBestCase() {
        return "O(n)";
    }

    @Override
    public String getAverageCase() {
        return "O(n^2)";
    }

    @Override
    public String getWorstCase() {
        return "O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
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
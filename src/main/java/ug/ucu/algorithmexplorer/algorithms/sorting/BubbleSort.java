package ug.ucu.algorithmexplorer.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

/**
 * Sorts integer arrays using an optimized Bubble Sort.
 *
 * Bubble Sort repeatedly compares adjacent values and swaps them when
 * they are in the wrong order. After each complete pass, the largest
 * remaining unsorted value has moved to its final position.
 *
 * Best-case time: O(n), when the array is already sorted
 * Average-case time: O(n^2)
 * Worst-case time: O(n^2)
 * Extra space: O(1), excluding result snapshots for visualisation
 * Stable: yes
 */
public class BubbleSort implements SortingAlgorithm {

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

        for (int pass = 0;
                pass < values.length - 1;
                pass++) {

            boolean swappedDuringPass = false;
            int lastComparedLeft = -1;
            int lastComparedRight = -1;

            int unsortedEnd =
                    values.length - 1 - pass;

            for (int index = 0;
                    index < unsortedEnd;
                    index++) {

                comparisons++;

                lastComparedLeft = index;
                lastComparedRight = index + 1;

                if (values[index] > values[index + 1]) {
                    int temporary = values[index];
                    values[index] = values[index + 1];
                    values[index + 1] = temporary;

                    swaps++;
                    moves += 3;
                    swappedDuringPass = true;
                }
            }

            String description =
                    "Pass "
                            + (pass + 1)
                            + " completed. Position "
                            + unsortedEnd
                            + " now contains its final value.";

            if (!swappedDuringPass) {
                description +=
                        " No swaps occurred, so the array is sorted.";
            }

            steps.add(
                    new SortStep(
                            pass + 1,
                            description,
                            values,
                            new int[]{
                                    lastComparedLeft,
                                    lastComparedRight
                            }
                    )
            );

            if (!swappedDuringPass) {
                break;
            }
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
        return "Bubble Sort";
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
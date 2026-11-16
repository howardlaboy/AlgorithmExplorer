package ug.ucu.algorithmexplorer.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

/**
 * Sorts integer arrays using Selection Sort.
 *
 * During each pass, Selection Sort searches the unsorted section for
 * its smallest value and places that value at the current position.
 *
 * Best-case time: O(n^2)
 * Average-case time: O(n^2)
 * Worst-case time: O(n^2)
 * Extra space: O(1), excluding result snapshots for visualisation
 * Stable: no
 */
public class SelectionSort implements SortingAlgorithm {

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

        for (int currentPosition = 0;
                currentPosition < values.length - 1;
                currentPosition++) {

            int minimumIndex = currentPosition;

            for (int candidateIndex = currentPosition + 1;
                    candidateIndex < values.length;
                    candidateIndex++) {

                comparisons++;

                if (values[candidateIndex]
                        < values[minimumIndex]) {

                    minimumIndex = candidateIndex;
                }
            }

            String description;

            if (minimumIndex != currentPosition) {
                int minimumValue = values[minimumIndex];
                int replacedValue = values[currentPosition];

                int temporary = values[currentPosition];
                values[currentPosition] = values[minimumIndex];
                values[minimumIndex] = temporary;

                swaps++;
                moves += 3;

                description =
                        "Pass "
                                + (currentPosition + 1)
                                + ": selected minimum value "
                                + minimumValue
                                + " from index "
                                + minimumIndex
                                + " and swapped it with "
                                + replacedValue
                                + " at index "
                                + currentPosition
                                + ".";

            } else {
                description =
                        "Pass "
                                + (currentPosition + 1)
                                + ": value "
                                + values[currentPosition]
                                + " at index "
                                + currentPosition
                                + " was already the minimum "
                                + "of the unsorted section.";
            }

            steps.add(
                    new SortStep(
                            currentPosition + 1,
                            description,
                            values,
                            new int[]{
                                    currentPosition,
                                    minimumIndex
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
        return "Selection Sort";
    }

    @Override
    public String getBestCase() {
        return "O(n^2)";
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

        for (int index = 0; index < source.length; index++) {
            copy[index] = source[index];
        }

        return copy;
    }
}
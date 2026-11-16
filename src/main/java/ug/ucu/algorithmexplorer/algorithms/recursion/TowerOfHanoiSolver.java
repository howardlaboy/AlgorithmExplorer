package ug.ucu.algorithmexplorer.algorithms.recursion;

import java.util.ArrayList;
import java.util.List;

import ug.ucu.algorithmexplorer.models.HanoiMove;
import ug.ucu.algorithmexplorer.models.HanoiResult;

/**
 * Generates the moves required to solve a Tower of Hanoi puzzle.
 *
 * Time complexity: O(2^n)
 * Extra recursive space: O(n)
 */
public class TowerOfHanoiSolver {

    public static final int MIN_DISKS = 1;
    public static final int MAX_DISKS = 8;

    /**
     * Solves Tower of Hanoi recursively.
     *
     * @param diskCount number of disks from 1 to 8
     * @param sourcePeg starting peg
     * @param auxiliaryPeg temporary peg
     * @param destinationPeg target peg
     * @return complete ordered solution
     */
    public HanoiResult solve(
            int diskCount,
            char sourcePeg,
            char auxiliaryPeg,
            char destinationPeg
    ) {
        validateInput(
                diskCount,
                sourcePeg,
                auxiliaryPeg,
                destinationPeg
        );

        List<HanoiMove> moves = new ArrayList<>();
        OperationCounter counter = new OperationCounter();

        long startTime = System.nanoTime();

        generateMoves(
                diskCount,
                sourcePeg,
                auxiliaryPeg,
                destinationPeg,
                moves,
                counter
        );

        long executionTime = System.nanoTime() - startTime;

        return new HanoiResult(
                diskCount,
                sourcePeg,
                auxiliaryPeg,
                destinationPeg,
                moves,
                counter.getCount(),
                executionTime
        );
    }

    private void generateMoves(
            int diskCount,
            char sourcePeg,
            char auxiliaryPeg,
            char destinationPeg,
            List<HanoiMove> moves,
            OperationCounter counter
    ) {
        counter.increment();

        if (diskCount == 1) {
            moves.add(
                    new HanoiMove(
                            1,
                            sourcePeg,
                            destinationPeg
                    )
            );
            return;
        }

        generateMoves(
                diskCount - 1,
                sourcePeg,
                destinationPeg,
                auxiliaryPeg,
                moves,
                counter
        );

        moves.add(
                new HanoiMove(
                        diskCount,
                        sourcePeg,
                        destinationPeg
                )
        );

        generateMoves(
                diskCount - 1,
                auxiliaryPeg,
                sourcePeg,
                destinationPeg,
                moves,
                counter
        );
    }

    private void validateInput(
            int diskCount,
            char sourcePeg,
            char auxiliaryPeg,
            char destinationPeg
    ) {
        if (diskCount < MIN_DISKS || diskCount > MAX_DISKS) {
            throw new IllegalArgumentException(
                    "Tower of Hanoi requires between "
                            + MIN_DISKS
                            + " and "
                            + MAX_DISKS
                            + " disks."
            );
        }

        if (sourcePeg == auxiliaryPeg
                || sourcePeg == destinationPeg
                || auxiliaryPeg == destinationPeg) {
            throw new IllegalArgumentException(
                    "Source, auxiliary and destination pegs "
                            + "must all be different."
            );
        }
    }

    /**
     * Small internal helper used to count recursive calls.
     */
    private static final class OperationCounter {

        private long count;

        void increment() {
            count++;
        }

        long getCount() {
            return count;
        }
    }
}
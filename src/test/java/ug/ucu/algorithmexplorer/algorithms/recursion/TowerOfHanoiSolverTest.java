package ug.ucu.algorithmexplorer.algorithms.recursion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.HanoiMove;
import ug.ucu.algorithmexplorer.models.HanoiResult;

class TowerOfHanoiSolverTest {

    private TowerOfHanoiSolver solver;

    @BeforeEach
    void setUp() {
        solver = new TowerOfHanoiSolver();
    }

    @Test
    void oneDiskProducesOneMove() {
        HanoiResult result = solver.solve(1, 'A', 'B', 'C');

        assertEquals(1, result.moveCount());
        assertEquals(1, result.expectedMoveCount());
        assertTrue(result.hasExpectedMoveCount());

        HanoiMove move = result.moves().get(0);

        assertEquals(1, move.disk());
        assertEquals('A', move.sourcePeg());
        assertEquals('C', move.destinationPeg());
        assertEquals(
                "Move disk 1 from A to C",
                move.description()
        );
    }

    @Test
    void threeDisksProduceCorrectMoveSequence() {
        HanoiResult result = solver.solve(3, 'A', 'B', 'C');

        assertEquals(7, result.moveCount());

        assertEquals(
                new HanoiMove(1, 'A', 'C'),
                result.moves().get(0)
        );
        assertEquals(
                new HanoiMove(2, 'A', 'B'),
                result.moves().get(1)
        );
        assertEquals(
                new HanoiMove(1, 'C', 'B'),
                result.moves().get(2)
        );
        assertEquals(
                new HanoiMove(3, 'A', 'C'),
                result.moves().get(3)
        );
        assertEquals(
                new HanoiMove(1, 'B', 'A'),
                result.moves().get(4)
        );
        assertEquals(
                new HanoiMove(2, 'B', 'C'),
                result.moves().get(5)
        );
        assertEquals(
                new HanoiMove(1, 'A', 'C'),
                result.moves().get(6)
        );
    }

    @Test
    void moveCountMatchesFormulaForEveryAllowedSize() {
        for (int disks = 1; disks <= 8; disks++) {
            HanoiResult result =
                    solver.solve(disks, 'A', 'B', 'C');

            long expected = (1L << disks) - 1;

            assertEquals(expected, result.moveCount());
            assertEquals(expected, result.expectedMoveCount());
            assertTrue(result.hasExpectedMoveCount());
        }
    }

    @Test
    void supportsDifferentPegAssignments() {
        HanoiResult result = solver.solve(2, 'C', 'A', 'B');

        assertEquals(
                new HanoiMove(1, 'C', 'A'),
                result.moves().get(0)
        );
        assertEquals(
                new HanoiMove(2, 'C', 'B'),
                result.moves().get(1)
        );
        assertEquals(
                new HanoiMove(1, 'A', 'B'),
                result.moves().get(2)
        );
    }

    @Test
    void generatedMovesAreAllLegal() {
        for (int disks = 1; disks <= 8; disks++) {
            HanoiResult result =
                    solver.solve(disks, 'A', 'B', 'C');

            assertSolutionIsLegal(result);
        }
    }

    @Test
    void maximumInputProducesExpectedNumberOfMoves() {
        HanoiResult result = solver.solve(8, 'A', 'B', 'C');

        assertEquals(255, result.moveCount());
        assertEquals(255, result.expectedMoveCount());
        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void resultMoveListCannotBeModified() {
        HanoiResult result = solver.solve(2, 'A', 'B', 'C');

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.moves().add(
                        new HanoiMove(1, 'A', 'C')
                )
        );
    }

    @Test
    void rejectsDiskCountBelowMinimum() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> solver.solve(0, 'A', 'B', 'C')
        );

        assertTrue(exception.getMessage().contains("between 1 and 8"));
    }

    @Test
    void rejectsDiskCountAboveMaximum() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> solver.solve(9, 'A', 'B', 'C')
        );

        assertTrue(exception.getMessage().contains("between 1 and 8"));
    }

    @Test
    void rejectsRepeatedPegNames() {
        assertThrows(
                IllegalArgumentException.class,
                () -> solver.solve(3, 'A', 'A', 'C')
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> solver.solve(3, 'A', 'B', 'A')
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> solver.solve(3, 'C', 'B', 'B')
        );
    }

    private void assertSolutionIsLegal(HanoiResult result) {
        Map<Character, Deque<Integer>> pegs = new HashMap<>();

        pegs.put(result.sourcePeg(), new ArrayDeque<>());
        pegs.put(result.auxiliaryPeg(), new ArrayDeque<>());
        pegs.put(result.destinationPeg(), new ArrayDeque<>());

        Deque<Integer> source = pegs.get(result.sourcePeg());

        for (int disk = result.diskCount(); disk >= 1; disk--) {
            source.push(disk);
        }

        for (HanoiMove move : result.moves()) {
            Deque<Integer> from = pegs.get(move.sourcePeg());
            Deque<Integer> to = pegs.get(move.destinationPeg());

            assertFalse(
                    from.isEmpty(),
                    "A move attempted to use an empty source peg."
            );

            int movedDisk = from.pop();

            assertEquals(
                    move.disk(),
                    movedDisk,
                    "The recorded disk was not on top of its source peg."
            );

            assertTrue(
                    to.isEmpty() || to.peek() > movedDisk,
                    "A larger disk was placed on a smaller disk."
            );

            to.push(movedDisk);
        }

        assertTrue(pegs.get(result.sourcePeg()).isEmpty());
        assertTrue(pegs.get(result.auxiliaryPeg()).isEmpty());
        assertEquals(
                result.diskCount(),
                pegs.get(result.destinationPeg()).size()
        );
    }
}
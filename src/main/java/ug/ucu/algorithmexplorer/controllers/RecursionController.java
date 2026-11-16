package ug.ucu.algorithmexplorer.controllers;

import ug.ucu.algorithmexplorer.algorithms.recursion.FactorialCalculator;
import ug.ucu.algorithmexplorer.algorithms.recursion.FibonacciCalculator;
import ug.ucu.algorithmexplorer.algorithms.recursion.TowerOfHanoiSolver;
import ug.ucu.algorithmexplorer.models.FactorialMethod;
import ug.ucu.algorithmexplorer.models.FactorialResult;
import ug.ucu.algorithmexplorer.models.FibonacciMethod;
import ug.ucu.algorithmexplorer.models.FibonacciResult;
import ug.ucu.algorithmexplorer.models.HanoiResult;

/**
 * Connects Recursion Lab requests to the appropriate backend classes.
 *
 * This controller contains no Swing code and no algorithm implementation.
 */
public class RecursionController {

    private final FibonacciCalculator fibonacciCalculator;
    private final FactorialCalculator factorialCalculator;
    private final TowerOfHanoiSolver hanoiSolver;

    /**
     * Creates a controller with the standard recursion backend classes.
     */
    public RecursionController() {
        this(
                new FibonacciCalculator(),
                new FactorialCalculator(),
                new TowerOfHanoiSolver()
        );
    }

    /**
     * Creates a controller using supplied backend components.
     *
     * This constructor makes the controller easier to test and maintain.
     *
     * @param fibonacciCalculator Fibonacci backend
     * @param factorialCalculator factorial backend
     * @param hanoiSolver Tower of Hanoi backend
     */
    public RecursionController(
            FibonacciCalculator fibonacciCalculator,
            FactorialCalculator factorialCalculator,
            TowerOfHanoiSolver hanoiSolver
    ) {
        if (fibonacciCalculator == null) {
            throw new IllegalArgumentException(
                    "Fibonacci calculator cannot be null."
            );
        }

        if (factorialCalculator == null) {
            throw new IllegalArgumentException(
                    "Factorial calculator cannot be null."
            );
        }

        if (hanoiSolver == null) {
            throw new IllegalArgumentException(
                    "Tower of Hanoi solver cannot be null."
            );
        }

        this.fibonacciCalculator = fibonacciCalculator;
        this.factorialCalculator = factorialCalculator;
        this.hanoiSolver = hanoiSolver;
    }

    /**
     * Calculates a Fibonacci number using the selected procedure.
     *
     * @param n Fibonacci position
     * @param method selected Fibonacci procedure
     * @return Fibonacci result
     */
    public FibonacciResult calculateFibonacci(
            int n,
            FibonacciMethod method
    ) {
        if (method == null) {
            throw new IllegalArgumentException(
                    "Select a Fibonacci method."
            );
        }

        return switch (method) {
            case NAIVE_RECURSIVE ->
                    fibonacciCalculator.calculateNaive(n);

            case MEMOIZED_RECURSIVE ->
                    fibonacciCalculator.calculateMemoized(n);

            case BOTTOM_UP ->
                    fibonacciCalculator.calculateBottomUp(n);
        };
    }

    /**
     * Calculates a factorial using the selected procedure.
     *
     * @param n non-negative factorial input
     * @param method selected factorial procedure
     * @return factorial result
     */
    public FactorialResult calculateFactorial(
            int n,
            FactorialMethod method
    ) {
        if (method == null) {
            throw new IllegalArgumentException(
                    "Select a factorial method."
            );
        }

        return switch (method) {
            case RECURSIVE ->
                    factorialCalculator.calculateRecursive(n);

            case ITERATIVE ->
                    factorialCalculator.calculateIterative(n);
        };
    }

    /**
     * Generates an ordered Tower of Hanoi solution.
     *
     * @param diskCount number of disks
     * @param sourcePeg starting peg
     * @param auxiliaryPeg temporary peg
     * @param destinationPeg target peg
     * @return complete Tower of Hanoi result
     */
    public HanoiResult solveHanoi(
            int diskCount,
            char sourcePeg,
            char auxiliaryPeg,
            char destinationPeg
    ) {
        return hanoiSolver.solve(
                diskCount,
                sourcePeg,
                auxiliaryPeg,
                destinationPeg
        );
    }
}
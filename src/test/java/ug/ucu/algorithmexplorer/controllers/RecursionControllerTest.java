package ug.ucu.algorithmexplorer.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.FactorialMethod;
import ug.ucu.algorithmexplorer.models.FactorialResult;
import ug.ucu.algorithmexplorer.models.FibonacciMethod;
import ug.ucu.algorithmexplorer.models.FibonacciResult;
import ug.ucu.algorithmexplorer.models.HanoiResult;

class RecursionControllerTest {

    private RecursionController controller;

    @BeforeEach
    void setUp() {
        controller = new RecursionController();
    }

    @Test
    void delegatesNaiveFibonacciRequest() {
        FibonacciResult result = controller.calculateFibonacci(
                10,
                FibonacciMethod.NAIVE_RECURSIVE
        );

        assertEquals(BigInteger.valueOf(55), result.value());
        assertEquals(
                FibonacciMethod.NAIVE_RECURSIVE,
                result.method()
        );
    }

    @Test
    void delegatesMemoizedFibonacciRequest() {
        FibonacciResult result = controller.calculateFibonacci(
                100,
                FibonacciMethod.MEMOIZED_RECURSIVE
        );

        assertEquals(
                new BigInteger("354224848179261915075"),
                result.value()
        );
        assertEquals(
                FibonacciMethod.MEMOIZED_RECURSIVE,
                result.method()
        );
    }

    @Test
    void delegatesBottomUpFibonacciRequest() {
        FibonacciResult result = controller.calculateFibonacci(
                20,
                FibonacciMethod.BOTTOM_UP
        );

        assertEquals(BigInteger.valueOf(6765), result.value());
        assertEquals(
                FibonacciMethod.BOTTOM_UP,
                result.method()
        );
    }

    @Test
    void rejectsMissingFibonacciMethod() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.calculateFibonacci(10, null)
        );

        assertTrue(
                exception.getMessage().contains(
                        "Select a Fibonacci method"
                )
        );
    }

    @Test
    void delegatesRecursiveFactorialRequest() {
        FactorialResult result = controller.calculateFactorial(
                10,
                FactorialMethod.RECURSIVE
        );

        assertEquals(
                BigInteger.valueOf(3_628_800),
                result.value()
        );
        assertEquals(
                FactorialMethod.RECURSIVE,
                result.method()
        );
    }

    @Test
    void delegatesIterativeFactorialRequest() {
        FactorialResult result = controller.calculateFactorial(
                10,
                FactorialMethod.ITERATIVE
        );

        assertEquals(
                BigInteger.valueOf(3_628_800),
                result.value()
        );
        assertEquals(
                FactorialMethod.ITERATIVE,
                result.method()
        );
    }

    @Test
    void rejectsMissingFactorialMethod() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.calculateFactorial(5, null)
        );

        assertTrue(
                exception.getMessage().contains(
                        "Select a factorial method"
                )
        );
    }

    @Test
    void delegatesTowerOfHanoiRequest() {
        HanoiResult result = controller.solveHanoi(
                4,
                'A',
                'B',
                'C'
        );

        assertEquals(15, result.moveCount());
        assertTrue(result.hasExpectedMoveCount());
        assertEquals('A', result.sourcePeg());
        assertEquals('B', result.auxiliaryPeg());
        assertEquals('C', result.destinationPeg());
    }

    @Test
    void passesBackendValidationErrorsToCaller() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.calculateFibonacci(
                        -1,
                        FibonacciMethod.BOTTOM_UP
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.calculateFactorial(
                        -1,
                        FactorialMethod.ITERATIVE
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.solveHanoi(
                        9,
                        'A',
                        'B',
                        'C'
                )
        );
    }
}
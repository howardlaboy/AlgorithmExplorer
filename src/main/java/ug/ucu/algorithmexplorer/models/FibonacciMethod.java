package ug.ucu.algorithmexplorer.models;

/**
 * Identifies the Fibonacci procedure used for a calculation.
 */
public enum FibonacciMethod {

    NAIVE_RECURSIVE("Naive Recursion"),
    MEMOIZED_RECURSIVE("Memoized Recursion"),
    BOTTOM_UP("Bottom-Up Dynamic Programming");

    private final String displayName;

    FibonacciMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
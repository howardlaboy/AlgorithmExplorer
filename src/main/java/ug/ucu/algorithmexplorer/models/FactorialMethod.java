package ug.ucu.algorithmexplorer.models;

/**
 * Identifies the procedure used to calculate a factorial.
 */
public enum FactorialMethod {

    RECURSIVE("Recursive"),
    ITERATIVE("Iterative");

    private final String displayName;

    FactorialMethod(String displayName) {
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
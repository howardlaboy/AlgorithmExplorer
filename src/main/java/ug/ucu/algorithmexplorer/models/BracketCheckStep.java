package ug.ucu.algorithmexplorer.models;

/**
 * Describes the bracket checker's state after processing one symbol.
 *
 * @param position zero-based input position
 * @param symbol symbol processed
 * @param action operation or decision made
 * @param stackState stack contents after processing the symbol
 * @param validSoFar whether the expression remains valid at this step
 */
public record BracketCheckStep(
        int position,
        char symbol,
        String action,
        String stackState,
        boolean validSoFar
) {

    public BracketCheckStep {
        if (position < 0) {
            throw new IllegalArgumentException(
                    "Step position cannot be negative."
            );
        }

        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException(
                    "Step action cannot be empty."
            );
        }

        if (stackState == null) {
            throw new IllegalArgumentException(
                    "Stack state cannot be null."
            );
        }
    }

    /**
     * Returns a readable representation for the later GUI table.
     *
     * @return formatted step description
     */
    public String description() {
        return "Position "
                + position
                + ": "
                + printableSymbol()
                + " - "
                + action
                + " - Stack "
                + stackState;
    }

    private String printableSymbol() {
        if (symbol == ' ') {
            return "[space]";
        }

        if (symbol == '\t') {
            return "[tab]";
        }

        if (symbol == '\n') {
            return "[new line]";
        }

        return String.valueOf(symbol);
    }
}
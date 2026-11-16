package ug.ucu.algorithmexplorer.models.sorting;

/**
 * Stores one visual step produced during sorting.
 *
 * @param stepNumber one-based step number
 * @param description readable explanation of the step
 * @param arrayState array state after the step
 * @param highlightedIndices indices involved in the important action
 */
public record SortStep(
        int stepNumber,
        String description,
        int[] arrayState,
        int[] highlightedIndices
) {

    public SortStep {
        if (stepNumber < 1) {
            throw new IllegalArgumentException(
                    "Step number must be at least 1."
            );
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Step description cannot be empty."
            );
        }

        if (arrayState == null) {
            throw new IllegalArgumentException(
                    "Array state cannot be null."
            );
        }

        if (highlightedIndices == null) {
            throw new IllegalArgumentException(
                    "Highlighted indices cannot be null."
            );
        }

        arrayState = copyArray(arrayState);
        highlightedIndices = copyArray(highlightedIndices);
    }

    /**
     * Returns an independent copy of the array snapshot.
     *
     * @return copied array state
     */
    @Override
    public int[] arrayState() {
        return copyArray(arrayState);
    }

    /**
     * Returns an independent copy of the highlighted indices.
     *
     * @return copied highlighted indices
     */
    @Override
    public int[] highlightedIndices() {
        return copyArray(highlightedIndices);
    }

    private static int[] copyArray(int[] source) {
        int[] copy = new int[source.length];

        for (int index = 0; index < source.length; index++) {
            copy[index] = source[index];
        }

        return copy;
    }
}
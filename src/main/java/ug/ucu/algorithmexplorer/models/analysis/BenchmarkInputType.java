package ug.ucu.algorithmexplorer.models.analysis;

/**
 * Input arrangements required by the experimental complexity analysis.
 */
public enum BenchmarkInputType {

    RANDOM("Random"),
    SORTED("Already Sorted"),
    REVERSE_SORTED("Reverse Sorted");

    private final String displayName;

    BenchmarkInputType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the readable input-type name.
     *
     * @return readable input-type name
     */
    public String getDisplayName() {
        return displayName;
    }
}
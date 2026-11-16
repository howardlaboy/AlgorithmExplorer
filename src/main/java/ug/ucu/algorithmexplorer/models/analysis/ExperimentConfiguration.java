package ug.ucu.algorithmexplorer.models.analysis;

/**
 * Describes one benchmark configuration in an experiment plan.
 *
 * @param algorithmName algorithm to execute
 * @param inputType input arrangement
 * @param inputSize number of values
 * @param repetitions number of measured runs
 */
public record ExperimentConfiguration(
        String algorithmName,
        BenchmarkInputType inputType,
        int inputSize,
        int repetitions
) {

    public ExperimentConfiguration {
        if (algorithmName == null
                || algorithmName.isBlank()) {

            throw new IllegalArgumentException(
                    "Algorithm name cannot be empty."
            );
        }

        if (inputType == null) {
            throw new IllegalArgumentException(
                    "Input type cannot be null."
            );
        }

        if (inputSize < 1) {
            throw new IllegalArgumentException(
                    "Input size must be at least 1."
            );
        }

        if (repetitions < 3) {
            throw new IllegalArgumentException(
                    "Each experiment must use at least 3 repetitions."
            );
        }
    }

    /**
     * Returns a readable configuration description.
     *
     * @return configuration description
     */
    public String description() {
        return algorithmName
                + " | "
                + inputType.getDisplayName()
                + " | n = "
                + inputSize
                + " | repetitions = "
                + repetitions;
    }
}
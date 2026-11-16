package ug.ucu.algorithmexplorer.models.analysis;

/**
 * Reports progress after an experiment configuration is completed or
 * skipped.
 *
 * @param completedConfigurations completed or skipped count
 * @param totalConfigurations total planned count
 * @param percentage percentage from 0 through 100
 * @param configuration current configuration
 * @param message progress message
 * @param skipped whether the configuration was skipped
 */
public record ExperimentProgress(
        int completedConfigurations,
        int totalConfigurations,
        int percentage,
        ExperimentConfiguration configuration,
        String message,
        boolean skipped
) {

    public ExperimentProgress {
        if (totalConfigurations < 1) {
            throw new IllegalArgumentException(
                    "Total configurations must be at least 1."
            );
        }

        if (completedConfigurations < 0
                || completedConfigurations
                > totalConfigurations) {

            throw new IllegalArgumentException(
                    "Completed configuration count is invalid."
            );
        }

        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                    "Progress percentage must be from 0 to 100."
            );
        }

        if (configuration == null) {
            throw new IllegalArgumentException(
                    "Progress configuration cannot be null."
            );
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Progress message cannot be empty."
            );
        }
    }
}
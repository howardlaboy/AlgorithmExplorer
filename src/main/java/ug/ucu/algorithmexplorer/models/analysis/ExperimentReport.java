package ug.ucu.algorithmexplorer.models.analysis;

import java.util.List;

/**
 * Stores all completed, skipped and cancellation information from one
 * experiment session.
 *
 * @param results completed benchmark results
 * @param skippedConfigurations configurations omitted by safety rules
 * @param cancelled whether the experiment was cancelled
 * @param requestedConfigurations original number of configurations
 */
public record ExperimentReport(
        List<BenchmarkResult> results,
        List<ExperimentConfiguration> skippedConfigurations,
        boolean cancelled,
        int requestedConfigurations
) {

    public ExperimentReport {
        if (results == null) {
            throw new IllegalArgumentException(
                    "Experiment results cannot be null."
            );
        }

        if (skippedConfigurations == null) {
            throw new IllegalArgumentException(
                    "Skipped configurations cannot be null."
            );
        }

        if (requestedConfigurations < 0) {
            throw new IllegalArgumentException(
                    "Requested configuration count cannot be negative."
            );
        }

        results = List.copyOf(results);

        skippedConfigurations =
                List.copyOf(skippedConfigurations);

        if (results.size()
                + skippedConfigurations.size()
                > requestedConfigurations) {

            throw new IllegalArgumentException(
                    "Report contains more configurations "
                            + "than were requested."
            );
        }
    }

    /**
     * Returns the number of completed benchmark configurations.
     *
     * @return completed result count
     */
    public int completedCount() {
        return results.size();
    }

    /**
     * Returns the number of skipped configurations.
     *
     * @return skipped count
     */
    public int skippedCount() {
        return skippedConfigurations.size();
    }
}
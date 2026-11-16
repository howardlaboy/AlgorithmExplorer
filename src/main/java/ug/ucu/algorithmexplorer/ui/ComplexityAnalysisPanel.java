package ug.ucu.algorithmexplorer.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import ug.ucu.algorithmexplorer.controllers.ComplexityAnalysisController;
import ug.ucu.algorithmexplorer.controllers.ComplexityAnalysisController.CancellationToken;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkInputType;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkResult;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentConfiguration;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentProgress;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentReport;

/**
 * Interface for Module D: Time Complexity Analysis.
 *
 * Experiments are executed in a SwingWorker so that lengthy benchmark
 * work does not block Swing's event-dispatch thread.
 */
public class ComplexityAnalysisPanel extends JPanel {

    private static final Color TITLE_COLOR =
            new Color(30, 74, 120);

    private static final Color SUCCESS_COLOR =
            new Color(25, 115, 60);

    private static final Color ERROR_COLOR =
            new Color(180, 35, 35);

    private static final Color WARNING_COLOR =
            new Color(160, 90, 10);

    private static final String STANDARD_PLAN =
            "Standard Plan";

    private static final String LARGE_PLAN =
            "Large Efficient-Algorithm Plan";

    private final ComplexityAnalysisController controller;

    private JTabbedPane mainTabs;

    private JComboBox<String> planComboBox;
    private JSpinner repetitionsSpinner;
    private JCheckBox extendedWorstCaseCheckBox;

    private JButton runButton;
    private JButton cancelButton;
    private JButton clearButton;

    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel planSummaryLabel;
    private JTextArea progressLogArea;

    private DefaultTableModel resultsTableModel;
    private JTable resultsTable;

    private DefaultTableModel skippedTableModel;
    private JTable skippedTable;

    private JComboBox<String> chartInputTypeComboBox;

    private RuntimeChartPanel quadraticChartPanel;
    private RuntimeChartPanel efficientChartPanel;

    private JTextArea discussionArea;

    private ExperimentWorker currentWorker;
    private CancellationToken cancellationToken;
    private ExperimentReport currentReport;

    public ComplexityAnalysisPanel() {
        controller =
                new ComplexityAnalysisController();

        setLayout(new BorderLayout());

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        buildInterface();
        refreshPlanSummary();
        updateControlState(false);
    }

    private void buildInterface() {
        mainTabs = new JTabbedPane();

        mainTabs.setFont(
                mainTabs.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        mainTabs.addTab(
                "Experiment Setup",
                createSetupTab()
        );

        mainTabs.addTab(
                "Results Table",
                createResultsTab()
        );

        mainTabs.addTab(
                "Runtime Charts",
                createChartsTab()
        );

        mainTabs.addTab(
                "Discussion Guide",
                createDiscussionTab()
        );

        add(mainTabs, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------
    // Experiment setup
    // ------------------------------------------------------------

    private JPanel createSetupTab() {
        JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        panel.add(
                createExperimentControls(),
                BorderLayout.NORTH
        );

        progressLogArea = new JTextArea(
                18,
                80
        );

        progressLogArea.setEditable(false);
        progressLogArea.setLineWrap(true);
        progressLogArea.setWrapStyleWord(true);
        progressLogArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        12
                )
        );

        progressLogArea.setText(
                "Select an experiment plan and click Run Experiment."
        );

        JScrollPane logScrollPane =
                new JScrollPane(progressLogArea);

        logScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Experiment Progress Log"
                )
        );

        logScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        logScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        logScrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        logScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                logScrollPane,
                BorderLayout.CENTER
        );

        panel.add(
                createProgressPanel(),
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel createExperimentControls() {
        JPanel panel = new JPanel(
                new GridBagLayout()
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Experiment Plan"
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets =
                new Insets(6, 8, 6, 8);

        constraints.fill =
                GridBagConstraints.HORIZONTAL;

        constraints.anchor =
                GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;

        panel.add(
                new JLabel("Plan:"),
                constraints
        );

        planComboBox = new JComboBox<>(
                new String[]{
                        STANDARD_PLAN,
                        LARGE_PLAN
                }
        );

        planComboBox.addActionListener(
                event -> refreshPlanSummary()
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(
                planComboBox,
                constraints
        );

        constraints.gridx = 2;
        constraints.weightx = 0.0;

        panel.add(
                new JLabel("Repetitions:"),
                constraints
        );

        repetitionsSpinner = new JSpinner(
                new SpinnerNumberModel(
                        3,
                        3,
                        20,
                        1
                )
        );

        repetitionsSpinner.addChangeListener(
                event -> refreshPlanSummary()
        );

        constraints.gridx = 3;

        panel.add(
                repetitionsSpinner,
                constraints
        );

        extendedWorstCaseCheckBox =
                new JCheckBox(
                        "Allow extended ordered first-pivot runs"
                );

        extendedWorstCaseCheckBox.setToolTipText(
                "Allows large sorted and reverse-sorted "
                        + "first-pivot Quick Sort experiments."
        );

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        constraints.weightx = 1.0;

        panel.add(
                extendedWorstCaseCheckBox,
                constraints
        );

        planSummaryLabel = new JLabel(
                " ",
                SwingConstants.CENTER
        );

        planSummaryLabel.setForeground(TITLE_COLOR);

        constraints.gridy = 2;

        panel.add(
                planSummaryLabel,
                constraints
        );

        JTextArea explanationArea = new JTextArea(
                "The Standard Plan runs all six available algorithms "
                        + "on the six required sizes and three input "
                        + "arrangements. The Large Plan runs Merge Sort "
                        + "and both Quick Sort versions at 50,000 and "
                        + "100,000. Large ordered first-pivot runs are "
                        + "skipped unless the extended option is enabled.",
                4,
                70
        );

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setOpaque(false);

        explanationArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Plan Information"
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );

        constraints.gridy = 3;

        panel.add(
                explanationArea,
                constraints
        );

        JPanel buttonsPanel = new JPanel(
                new GridLayout(1, 0, 8, 8)
        );

        runButton = new JButton(
                "Run Experiment"
        );

        runButton.addActionListener(
                event -> startExperiment()
        );

        cancelButton = new JButton(
                "Cancel"
        );

        cancelButton.addActionListener(
                event -> cancelExperiment()
        );

        clearButton = new JButton(
                "Clear Results"
        );

        clearButton.addActionListener(
                event -> clearResults()
        );

        buttonsPanel.add(runButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(clearButton);

        constraints.gridy = 4;

        panel.add(
                buttonsPanel,
                constraints
        );

        return panel;
    }

    private JPanel createProgressPanel() {
        JPanel panel = new JPanel(
                new BorderLayout(6, 6)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Overall Progress"
                )
        );

        progressBar = new JProgressBar(
                0,
                100
        );

        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        statusLabel = new JLabel(
                "Ready.",
                SwingConstants.CENTER
        );

        statusLabel.setForeground(TITLE_COLOR);

        statusLabel.setFont(
                statusLabel.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        panel.add(
                progressBar,
                BorderLayout.CENTER
        );

        panel.add(
                statusLabel,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ------------------------------------------------------------
    // Results tables
    // ------------------------------------------------------------

    private JPanel createResultsTab() {
        JPanel panel = new JPanel(
                new BorderLayout(8, 8)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JTabbedPane tablesTabs =
                new JTabbedPane();

        tablesTabs.addTab(
                "Completed Results",
                createCompletedResultsPanel()
        );

        tablesTabs.addTab(
                "Skipped Configurations",
                createSkippedResultsPanel()
        );

        panel.add(
                tablesTabs,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createCompletedResultsPanel() {
        JPanel panel = new JPanel(
                new BorderLayout()
        );

        resultsTableModel = new DefaultTableModel(
                new Object[]{
                        "Algorithm",
                        "Input Type",
                        "n",
                        "Runs",
                        "Average Time (ms)",
                        "Minimum (ms)",
                        "Maximum (ms)",
                        "Avg Comparisons",
                        "Avg Swaps",
                        "Avg Moves"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        resultsTable = new JTable(
                resultsTableModel
        );

        resultsTable.setRowHeight(27);
        resultsTable.setAutoCreateRowSorter(true);
        resultsTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        int[] widths = {
                220,
                140,
                90,
                70,
                150,
                130,
                130,
                150,
                110,
                120
        };

        applyColumnWidths(
                resultsTable,
                widths
        );

        JScrollPane scrollPane =
                new JScrollPane(resultsTable);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createSkippedResultsPanel() {
        JPanel panel = new JPanel(
                new BorderLayout()
        );

        skippedTableModel = new DefaultTableModel(
                new Object[]{
                        "Algorithm",
                        "Input Type",
                        "n",
                        "Runs",
                        "Reason"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        skippedTable = new JTable(
                skippedTableModel
        );

        skippedTable.setRowHeight(27);
        skippedTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        applyColumnWidths(
                skippedTable,
                new int[]{
                        220,
                        150,
                        100,
                        80,
                        620
                }
        );

        JScrollPane scrollPane =
                new JScrollPane(skippedTable);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ------------------------------------------------------------
    // Charts
    // ------------------------------------------------------------

    private JPanel createChartsTab() {
        JPanel panel = new JPanel(
                new BorderLayout(8, 8)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JPanel filterPanel = new JPanel(
                new BorderLayout(8, 8)
        );

        filterPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Chart Input Filter"
                )
        );

        filterPanel.add(
                new JLabel("Input Type:"),
                BorderLayout.WEST
        );

        chartInputTypeComboBox =
                new JComboBox<>(
                        new String[]{
                                BenchmarkInputType.RANDOM
                                        .getDisplayName(),
                                BenchmarkInputType.SORTED
                                        .getDisplayName(),
                                BenchmarkInputType.REVERSE_SORTED
                                        .getDisplayName()
                        }
                );

        chartInputTypeComboBox.addActionListener(
                event -> refreshCharts()
        );

        filterPanel.add(
                chartInputTypeComboBox,
                BorderLayout.CENTER
        );

        panel.add(
                filterPanel,
                BorderLayout.NORTH
        );

        JTabbedPane chartsTabs =
                new JTabbedPane();

        quadraticChartPanel =
                new RuntimeChartPanel(
                        "O(n²) Sorting Algorithms",
                        new String[]{
                                ComplexityAnalysisController.BUBBLE_SORT,
                                ComplexityAnalysisController.SELECTION_SORT,
                                ComplexityAnalysisController.INSERTION_SORT
                        }
                );

        efficientChartPanel =
                new RuntimeChartPanel(
                        "O(n log n) and Quick Sort Algorithms",
                        new String[]{
                                ComplexityAnalysisController.MERGE_SORT,
                                ComplexityAnalysisController.FIRST_PIVOT_QUICK_SORT,
                                ComplexityAnalysisController.RANDOMIZED_QUICK_SORT
                        }
                );

        JScrollPane quadraticScrollPane =
                new JScrollPane(
                        quadraticChartPanel
                );

        quadraticScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        quadraticScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        JScrollPane efficientScrollPane =
                new JScrollPane(
                        efficientChartPanel
                );

        efficientScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        efficientScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        chartsTabs.addTab(
                "Quadratic Sorts",
                quadraticScrollPane
        );

        chartsTabs.addTab(
                "Efficient Sorts",
                efficientScrollPane
        );

        panel.add(
                chartsTabs,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ------------------------------------------------------------
    // Discussion tab
    // ------------------------------------------------------------

    private JPanel createDiscussionTab() {
        JPanel panel = new JPanel(
                new BorderLayout(8, 8)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        discussionArea = new JTextArea(
                30,
                80
        );

        discussionArea.setEditable(false);
        discussionArea.setLineWrap(true);
        discussionArea.setWrapStyleWord(true);

        discussionArea.setText(
                createDefaultDiscussion()
        );

        discussionArea.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(discussionArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Report Discussion Guide"
                )
        );

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ------------------------------------------------------------
    // Experiment execution
    // ------------------------------------------------------------

    private void startExperiment() {
        if (currentWorker != null
                && !currentWorker.isDone()) {

            showMessage(
                    "An experiment is already running.",
                    WARNING_COLOR,
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int repetitions =
                (Integer) repetitionsSpinner
                        .getValue();

        List<ExperimentConfiguration> plan;

        if (LARGE_PLAN.equals(
                planComboBox.getSelectedItem()
        )) {
            plan = controller.createLargeInputPlan(
                    repetitions
            );
        } else {
            plan = controller.createStandardPlan(
                    repetitions
            );
        }

        boolean allowExtendedWorstCase =
                extendedWorstCaseCheckBox
                        .isSelected();

        if (allowExtendedWorstCase
                && LARGE_PLAN.equals(
                        planComboBox.getSelectedItem()
                )) {

            int decision =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Large ordered first-pivot Quick Sort "
                                    + "experiments may take a very "
                                    + "long time because they perform "
                                    + "quadratic work.\n\nContinue?",
                            "Extended Worst-Case Experiments",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (decision
                    != JOptionPane.YES_OPTION) {

                return;
            }
        }

        clearResults();

        progressLogArea.setText(
                "Starting experiment with "
                        + plan.size()
                        + " configuration(s)..."
                        + System.lineSeparator()
        );

        progressBar.setValue(0);
        statusLabel.setText(
                "Experiment running..."
        );

        statusLabel.setForeground(TITLE_COLOR);

        cancellationToken =
                new CancellationToken();

        currentWorker = new ExperimentWorker(
                plan,
                allowExtendedWorstCase,
                cancellationToken
        );

        updateControlState(true);
        currentWorker.execute();
    }

    private void cancelExperiment() {
        if (currentWorker == null
                || currentWorker.isDone()) {

            return;
        }

        cancellationToken.cancel();

        statusLabel.setText(
                "Cancellation requested. "
                        + "The current configuration will finish first."
        );

        statusLabel.setForeground(WARNING_COLOR);

        progressLogArea.append(
                System.lineSeparator()
                        + "Cancellation requested..."
                        + System.lineSeparator()
        );

        cancelButton.setEnabled(false);
    }

    private void handleProgress(
            ExperimentProgress progress
    ) {
        progressBar.setValue(
                progress.percentage()
        );

        statusLabel.setText(
                progress.message()
        );

        statusLabel.setForeground(
                progress.skipped()
                        ? WARNING_COLOR
                        : TITLE_COLOR
        );

        progressLogArea.append(
                progress.completedConfigurations()
                        + "/"
                        + progress.totalConfigurations()
                        + " ["
                        + progress.percentage()
                        + "%] "
                        + progress.message()
                        + System.lineSeparator()
        );

        progressLogArea.setCaretPosition(
                progressLogArea
                        .getDocument()
                        .getLength()
        );
    }

    private void handleCompletedReport(
            ExperimentReport report
    ) {
        currentReport = report;

        populateResultsTable(report.results());

        populateSkippedTable(
                report.skippedConfigurations()
        );

        refreshCharts();
        refreshDiscussion(report);

        if (report.cancelled()) {
            statusLabel.setText(
                    "Experiment cancelled after "
                            + report.completedCount()
                            + " completed configuration(s)."
            );

            statusLabel.setForeground(WARNING_COLOR);
        } else {
            progressBar.setValue(100);

            statusLabel.setText(
                    "Experiment complete: "
                            + report.completedCount()
                            + " completed, "
                            + report.skippedCount()
                            + " skipped."
            );

            statusLabel.setForeground(SUCCESS_COLOR);
        }

        progressLogArea.append(
                System.lineSeparator()
                        + "Finished. Completed: "
                        + report.completedCount()
                        + ", skipped: "
                        + report.skippedCount()
                        + ", cancelled: "
                        + report.cancelled()
                        + "."
                        + System.lineSeparator()
        );

        updateControlState(false);
    }

    private void handleWorkerError(
            Throwable throwable
    ) {
        String message =
                throwable == null
                        || throwable.getMessage() == null
                        ? "The experiment could not be completed."
                        : throwable.getMessage();

        statusLabel.setText(message);
        statusLabel.setForeground(ERROR_COLOR);

        progressLogArea.append(
                System.lineSeparator()
                        + "ERROR: "
                        + message
                        + System.lineSeparator()
        );

        updateControlState(false);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Time Complexity Analysis",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void updateControlState(
            boolean running
    ) {
        runButton.setEnabled(!running);
        cancelButton.setEnabled(running);
        clearButton.setEnabled(!running);

        planComboBox.setEnabled(!running);
        repetitionsSpinner.setEnabled(!running);

        extendedWorstCaseCheckBox.setEnabled(
                !running
        );
    }

    // ------------------------------------------------------------
    // Results and charts
    // ------------------------------------------------------------

    private void populateResultsTable(
            List<BenchmarkResult> results
    ) {
        resultsTableModel.setRowCount(0);

        for (BenchmarkResult result : results) {
            resultsTableModel.addRow(
                    new Object[]{
                            result.algorithmName(),
                            result.inputType()
                                    .getDisplayName(),
                            result.inputSize(),
                            result.repetitions(),
                            formatDecimal(
                                    result.averageTimeMillis()
                            ),
                            formatDecimal(
                                    result.minimumTimeMillis()
                            ),
                            formatDecimal(
                                    result.maximumTimeMillis()
                            ),
                            formatDecimal(
                                    result.averageComparisons()
                            ),
                            formatDecimal(
                                    result.averageSwaps()
                            ),
                            formatDecimal(
                                    result.averageMoves()
                            )
                    }
            );
        }
    }

    private void populateSkippedTable(
            List<ExperimentConfiguration> configurations
    ) {
        skippedTableModel.setRowCount(0);

        for (ExperimentConfiguration configuration
                : configurations) {

            skippedTableModel.addRow(
                    new Object[]{
                            configuration.algorithmName(),
                            configuration.inputType()
                                    .getDisplayName(),
                            configuration.inputSize(),
                            configuration.repetitions(),
                            controller.createSkipMessage(
                                    configuration
                            )
                    }
            );
        }
    }

    private void refreshCharts() {
        if (quadraticChartPanel == null
                || efficientChartPanel == null) {

            return;
        }

        BenchmarkInputType selectedType =
                getSelectedChartInputType();

        List<BenchmarkResult> results =
                currentReport == null
                        ? List.of()
                        : currentReport.results();

        quadraticChartPanel.updateResults(
                results,
                selectedType
        );

        efficientChartPanel.updateResults(
                results,
                selectedType
        );
    }

    private BenchmarkInputType
            getSelectedChartInputType() {

        String selected =
                (String) chartInputTypeComboBox
                        .getSelectedItem();

        for (BenchmarkInputType inputType
                : BenchmarkInputType.values()) {

            if (inputType.getDisplayName()
                    .equals(selected)) {

                return inputType;
            }
        }

        return BenchmarkInputType.RANDOM;
    }

    private void refreshDiscussion(
            ExperimentReport report
    ) {
        StringBuilder discussion =
                new StringBuilder();

        discussion.append(
                "MODULE D EXPERIMENT SUMMARY"
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                "==========================="
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                "Completed configurations: "
        );

        discussion.append(
                report.completedCount()
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                "Skipped configurations: "
        );

        discussion.append(
                report.skippedCount()
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                "Cancelled: "
        );

        discussion.append(
                report.cancelled()
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                createDefaultDiscussion()
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                "MEASUREMENT CAUTION"
        );

        discussion.append(
                System.lineSeparator()
        );

        discussion.append(
                "Execution times can vary because of JVM warm-up, "
                        + "operating-system scheduling and other "
                        + "processes. Discuss broad growth patterns "
                        + "rather than treating one nanosecond value "
                        + "as an exact universal result."
        );

        discussionArea.setText(
                discussion.toString()
        );

        discussionArea.setCaretPosition(0);
    }

    private String createDefaultDiscussion() {
        return "THEORETICAL EXPECTATIONS"
                + System.lineSeparator()
                + System.lineSeparator()
                + "1. Bubble Sort"
                + System.lineSeparator()
                + "Bubble Sort is O(n) on already sorted input "
                + "because the optimized implementation stops after "
                + "a pass with no swaps. Its average and reverse-input "
                + "behavior are O(n²)."
                + System.lineSeparator()
                + System.lineSeparator()
                + "2. Selection Sort"
                + System.lineSeparator()
                + "Selection Sort performs the same number of element "
                + "comparisons for random, sorted and reverse-sorted "
                + "input of the same size. Its running time is O(n²) "
                + "in all three cases."
                + System.lineSeparator()
                + System.lineSeparator()
                + "3. Insertion Sort"
                + System.lineSeparator()
                + "Insertion Sort is fast on already sorted input "
                + "because each value is compared once with the value "
                + "before it and no shifting is required. Reverse "
                + "input requires the maximum number of shifts."
                + System.lineSeparator()
                + System.lineSeparator()
                + "4. Merge Sort"
                + System.lineSeparator()
                + "Merge Sort repeatedly divides and merges the input. "
                + "Its expected growth remains O(n log n) for random, "
                + "sorted and reverse-sorted input, although exact "
                + "comparison counts can differ."
                + System.lineSeparator()
                + System.lineSeparator()
                + "5. First-Pivot Quick Sort"
                + System.lineSeparator()
                + "First-element pivot selection creates highly "
                + "unbalanced partitions on already sorted input. "
                + "The measured comparison count should approach "
                + "n(n - 1) / 2, demonstrating O(n²) behavior."
                + System.lineSeparator()
                + System.lineSeparator()
                + "6. Randomized Quick Sort"
                + System.lineSeparator()
                + "Random pivot selection reduces dependence on the "
                + "input's original arrangement. Ordered input is less "
                + "likely to produce the same consistently unbalanced "
                + "partition pattern as first-pivot Quick Sort."
                + System.lineSeparator()
                + System.lineSeparator()
                + "REPORT QUESTIONS"
                + System.lineSeparator()
                + "- Do the quadratic-sort chart lines curve upward?"
                + System.lineSeparator()
                + "- Does Insertion Sort remain lower on sorted input?"
                + System.lineSeparator()
                + "- Does Selection Sort perform similar comparisons "
                + "for all input arrangements?"
                + System.lineSeparator()
                + "- Do Merge Sort and randomized Quick Sort scale "
                + "better than the O(n²) algorithms?"
                + System.lineSeparator()
                + "- How severe is first-pivot Quick Sort's slowdown "
                + "on ordered input?"
                + System.lineSeparator()
                + "- Which results disagree with theory, and can JVM "
                + "overhead or measurement noise explain small-input "
                + "differences?";
    }

    private void clearResults() {
        if (currentWorker != null
                && !currentWorker.isDone()) {

            return;
        }

        currentReport = null;

        if (resultsTableModel != null) {
            resultsTableModel.setRowCount(0);
        }

        if (skippedTableModel != null) {
            skippedTableModel.setRowCount(0);
        }

        if (progressBar != null) {
            progressBar.setValue(0);
        }

        if (progressLogArea != null) {
            progressLogArea.setText(
                    "No experiment results."
            );
        }

        if (statusLabel != null) {
            statusLabel.setText("Ready.");
            statusLabel.setForeground(TITLE_COLOR);
        }

        if (discussionArea != null) {
            discussionArea.setText(
                    createDefaultDiscussion()
            );
        }

        refreshCharts();
    }

    private void refreshPlanSummary() {
        if (planComboBox == null
                || repetitionsSpinner == null
                || planSummaryLabel == null) {

            return;
        }

        int repetitions =
                (Integer) repetitionsSpinner
                        .getValue();

        int configurationCount;

        if (LARGE_PLAN.equals(
                planComboBox.getSelectedItem()
        )) {
            configurationCount =
                    controller.createLargeInputPlan(
                            repetitions
                    ).size();
        } else {
            configurationCount =
                    controller.createStandardPlan(
                            repetitions
                    ).size();
        }

        planSummaryLabel.setText(
                "Configurations: "
                        + configurationCount
                        + " | Measured runs per configuration: "
                        + repetitions
        );
    }

    private void applyColumnWidths(
            JTable table,
            int[] widths
    ) {
        for (int index = 0;
                index < widths.length;
                index++) {

            table.getColumnModel()
                    .getColumn(index)
                    .setPreferredWidth(widths[index]);
        }
    }

    private String formatDecimal(
            double value
    ) {
        return String.format("%.6f", value);
    }

    private void showMessage(
            String message,
            Color color,
            int messageType
    ) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Time Complexity Analysis",
                messageType
        );
    }

    // ------------------------------------------------------------
    // SwingWorker
    // ------------------------------------------------------------

    private final class ExperimentWorker
            extends SwingWorker<
                    ExperimentReport,
                    ExperimentProgress> {

        private final List<ExperimentConfiguration> plan;
        private final boolean allowExtendedWorstCase;
        private final CancellationToken token;

        ExperimentWorker(
                List<ExperimentConfiguration> plan,
                boolean allowExtendedWorstCase,
                CancellationToken token
        ) {
            this.plan = plan;
            this.allowExtendedWorstCase =
                    allowExtendedWorstCase;
            this.token = token;
        }

        @Override
        protected ExperimentReport doInBackground() {
            return controller.runPlan(
                    plan,
                    allowExtendedWorstCase,
                    progress -> {
                        if (!isCancelled()) {
                            publish(progress);
                            setProgress(
                                    progress.percentage()
                            );
                        }
                    },
                    token
            );
        }

        @Override
        protected void process(
                List<ExperimentProgress> chunks
        ) {
            for (ExperimentProgress progress
                    : chunks) {

                handleProgress(progress);
            }
        }

        @Override
        protected void done() {
            try {
                ExperimentReport report = get();
                handleCompletedReport(report);

            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();

                handleWorkerError(
                        new IllegalStateException(
                                "The experiment thread "
                                        + "was interrupted.",
                                exception
                        )
                );

            } catch (ExecutionException exception) {
                handleWorkerError(
                        exception.getCause()
                );

            } catch (java.util.concurrent.CancellationException
                    exception) {

                statusLabel.setText(
                        "Experiment cancelled."
                );

                statusLabel.setForeground(
                        WARNING_COLOR
                );

                updateControlState(false);
            }
        }
    }

    // ------------------------------------------------------------
    // Runtime chart
    // ------------------------------------------------------------

    private static final class RuntimeChartPanel
            extends JPanel {

        private static final Color[] LINE_COLORS = {
                new Color(40, 105, 190),
                new Color(200, 75, 65),
                new Color(35, 145, 85),
                new Color(155, 90, 185),
                new Color(215, 135, 30),
                new Color(60, 155, 165)
        };

        private final String chartTitle;
        private final String[] algorithmNames;

        private List<BenchmarkResult> results =
                List.of();

        private BenchmarkInputType inputType =
                BenchmarkInputType.RANDOM;

        RuntimeChartPanel(
                String chartTitle,
                String[] algorithmNames
        ) {
            this.chartTitle = chartTitle;
            this.algorithmNames =
                    copyStrings(algorithmNames);

            setBackground(Color.WHITE);

            setPreferredSize(
                    new Dimension(1050, 560)
            );

            setMinimumSize(
                    new Dimension(700, 420)
            );
        }

        void updateResults(
                List<BenchmarkResult> results,
                BenchmarkInputType inputType
        ) {
            this.results = List.copyOf(results);
            this.inputType = inputType;

            repaint();
        }

        @Override
        protected void paintComponent(
                Graphics graphics
        ) {
            super.paintComponent(graphics);

            Graphics2D graphics2D =
                    (Graphics2D) graphics.create();

            try {
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                drawChart(graphics2D);

            } finally {
                graphics2D.dispose();
            }
        }

        private void drawChart(
                Graphics2D graphics2D
        ) {
            int left = 95;
            int top = 70;
            int right = getWidth() - 210;
            int bottom = getHeight() - 85;

            Font originalFont =
                    graphics2D.getFont();

            graphics2D.setFont(
                    originalFont.deriveFont(
                            Font.BOLD,
                            17f
                    )
            );

            graphics2D.setColor(
                    new Color(35, 55, 75)
            );

            graphics2D.drawString(
                    chartTitle
                            + " - "
                            + inputType.getDisplayName(),
                    left,
                    35
            );

            graphics2D.setFont(originalFont);

            if (!hasMatchingResults()) {
                String message =
                        "Run an experiment containing these "
                                + "algorithms and input type.";

                int textWidth = graphics2D
                        .getFontMetrics()
                        .stringWidth(message);

                graphics2D.setColor(
                        new Color(100, 100, 100)
                );

                graphics2D.drawString(
                        message,
                        Math.max(
                                20,
                                (getWidth() - textWidth) / 2
                        ),
                        getHeight() / 2
                );

                return;
            }

            graphics2D.setColor(
                    new Color(65, 65, 65)
            );

            graphics2D.setStroke(
                    new BasicStroke(1.5f)
            );

            graphics2D.drawLine(
                    left,
                    bottom,
                    right,
                    bottom
            );

            graphics2D.drawLine(
                    left,
                    top,
                    left,
                    bottom
            );

            int maximumSize =
                    findMaximumInputSize();

            double maximumTime =
                    findMaximumTimeMillis();

            if (maximumTime <= 0.0) {
                maximumTime = 1.0;
            }

            drawGridAndLabels(
                    graphics2D,
                    left,
                    top,
                    right,
                    bottom,
                    maximumSize,
                    maximumTime
            );

            for (int algorithmIndex = 0;
                    algorithmIndex
                            < algorithmNames.length;
                    algorithmIndex++) {

                drawAlgorithmLine(
                        graphics2D,
                        algorithmNames[algorithmIndex],
                        LINE_COLORS[
                                algorithmIndex
                                        % LINE_COLORS.length
                        ],
                        left,
                        top,
                        right,
                        bottom,
                        maximumSize,
                        maximumTime
                );
            }

            drawLegend(
                    graphics2D,
                    right + 20,
                    top
            );

            graphics2D.setFont(originalFont);
        }

        private void drawGridAndLabels(
                Graphics2D graphics2D,
                int left,
                int top,
                int right,
                int bottom,
                int maximumSize,
                double maximumTime
        ) {
            graphics2D.setColor(
                    new Color(220, 225, 230)
            );

            for (int division = 1;
                    division <= 5;
                    division++) {

                int y = bottom
                        - (bottom - top)
                        * division
                        / 5;

                graphics2D.drawLine(
                        left,
                        y,
                        right,
                        y
                );

                double timeValue =
                        maximumTime
                                * division
                                / 5.0;

                graphics2D.setColor(
                        new Color(70, 70, 70)
                );

                graphics2D.drawString(
                        String.format(
                                "%.3f",
                                timeValue
                        ),
                        20,
                        y + 5
                );

                graphics2D.setColor(
                        new Color(220, 225, 230)
                );
            }

            for (int division = 0;
                    division <= 5;
                    division++) {

                int x = left
                        + (right - left)
                        * division
                        / 5;

                int sizeValue =
                        maximumSize
                                * division
                                / 5;

                graphics2D.setColor(
                        new Color(70, 70, 70)
                );

                graphics2D.drawString(
                        formatInteger(sizeValue),
                        x - 18,
                        bottom + 25
                );
            }

            graphics2D.drawString(
                    "Input size n",
                    (left + right) / 2 - 35,
                    bottom + 55
            );

            graphics2D.drawString(
                    "Average time (ms)",
                    8,
                    top - 18
            );
        }

        private void drawAlgorithmLine(
                Graphics2D graphics2D,
                String algorithmName,
                Color color,
                int left,
                int top,
                int right,
                int bottom,
                int maximumSize,
                double maximumTime
        ) {
            List<BenchmarkResult> matching =
                    getMatchingResults(
                            algorithmName
                    );

            if (matching.isEmpty()) {
                return;
            }

            sortByInputSize(matching);

            graphics2D.setColor(color);
            graphics2D.setStroke(
                    new BasicStroke(2.4f)
            );

            int previousX = -1;
            int previousY = -1;

            for (BenchmarkResult result
                    : matching) {

                int x = left
                        + (int) Math.round(
                                result.inputSize()
                                        * (right - left)
                                        / (double) maximumSize
                        );

                int y = bottom
                        - (int) Math.round(
                                result.averageTimeMillis()
                                        * (bottom - top)
                                        / maximumTime
                        );

                graphics2D.fillOval(
                        x - 4,
                        y - 4,
                        8,
                        8
                );

                if (previousX >= 0) {
                    graphics2D.drawLine(
                            previousX,
                            previousY,
                            x,
                            y
                    );
                }

                previousX = x;
                previousY = y;
            }
        }

        private void drawLegend(
                Graphics2D graphics2D,
                int x,
                int y
        ) {
            Font originalFont =
                    graphics2D.getFont();

            graphics2D.setFont(
                    originalFont.deriveFont(
                            Font.BOLD
                    )
            );

            graphics2D.setColor(
                    new Color(45, 45, 45)
            );

            graphics2D.drawString(
                    "Legend",
                    x,
                    y
            );

            graphics2D.setFont(originalFont);

            for (int index = 0;
                    index < algorithmNames.length;
                    index++) {

                int rowY =
                        y + 28 + index * 28;

                graphics2D.setColor(
                        LINE_COLORS[
                                index
                                        % LINE_COLORS.length
                        ]
                );

                graphics2D.fillRect(
                        x,
                        rowY - 10,
                        18,
                        5
                );

                graphics2D.setColor(
                        new Color(50, 50, 50)
                );

                graphics2D.drawString(
                        algorithmNames[index],
                        x + 26,
                        rowY
                );
            }
        }

        private boolean hasMatchingResults() {
            for (BenchmarkResult result : results) {
                if (result.inputType() == inputType
                        && containsAlgorithm(
                                result.algorithmName()
                        )) {

                    return true;
                }
            }

            return false;
        }

        private int findMaximumInputSize() {
            int maximum = 1;

            for (BenchmarkResult result : results) {
                if (result.inputType() == inputType
                        && containsAlgorithm(
                                result.algorithmName()
                        )
                        && result.inputSize() > maximum) {

                    maximum = result.inputSize();
                }
            }

            return maximum;
        }

        private double findMaximumTimeMillis() {
            double maximum = 0.0;

            for (BenchmarkResult result : results) {
                if (result.inputType() == inputType
                        && containsAlgorithm(
                                result.algorithmName()
                        )
                        && result.averageTimeMillis()
                        > maximum) {

                    maximum =
                            result.averageTimeMillis();
                }
            }

            return maximum;
        }

        private boolean containsAlgorithm(
                String algorithmName
        ) {
            for (String supported
                    : algorithmNames) {

                if (supported.equals(
                        algorithmName
                )) {
                    return true;
                }
            }

            return false;
        }

        private List<BenchmarkResult>
                getMatchingResults(
                        String algorithmName
                ) {

            List<BenchmarkResult> matching =
                    new ArrayList<>();

            for (BenchmarkResult result : results) {
                if (result.inputType() == inputType
                        && result.algorithmName()
                        .equals(algorithmName)) {

                    matching.add(result);
                }
            }

            return matching;
        }

        private void sortByInputSize(
                List<BenchmarkResult> values
        ) {
            for (int current = 1;
                    current < values.size();
                    current++) {

                BenchmarkResult valueToInsert =
                        values.get(current);

                int previous = current - 1;

                while (previous >= 0
                        && values.get(previous)
                        .inputSize()
                        > valueToInsert
                        .inputSize()) {

                    values.set(
                            previous + 1,
                            values.get(previous)
                    );

                    previous--;
                }

                values.set(
                        previous + 1,
                        valueToInsert
                );
            }
        }

        private static String[] copyStrings(
                String[] source
        ) {
            String[] copy =
                    new String[source.length];

            for (int index = 0;
                    index < source.length;
                    index++) {

                copy[index] = source[index];
            }

            return copy;
        }

        private static String formatInteger(
                int value
        ) {
            return String.format("%,d", value);
        }
    }
}
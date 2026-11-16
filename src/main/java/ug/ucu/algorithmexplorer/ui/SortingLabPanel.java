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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import ug.ucu.algorithmexplorer.controllers.SortingController;
import ug.ucu.algorithmexplorer.controllers.SortingController.AlgorithmInformation;
import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

/**
 * Main interface for Module C: Sorting Lab.
 *
 * The interface provides:
 *
 * - user-entered and randomly generated input;
 * - six sorting-algorithm choices;
 * - step-by-step visualization;
 * - automatic playback;
 * - comparisons, swaps, moves and execution time;
 * - comparison of all five required algorithms;
 * - complexity, space and stability information.
 */
public class SortingLabPanel extends JPanel {

    private static final Color TITLE_COLOR =
            new Color(30, 74, 120);

    private static final Color SUCCESS_COLOR =
            new Color(25, 115, 60);

    private static final Color ERROR_COLOR =
            new Color(180, 35, 35);

    private static final Color HIGHLIGHT_COLOR =
            new Color(255, 210, 95);

    private static final Color NORMAL_CELL_COLOR =
            new Color(175, 215, 250);

    private final SortingController controller;

    private JTextField inputField;
    private JSpinner randomSizeSpinner;
    private JComboBox<String> algorithmComboBox;

    private JButton runButton;
    private JButton stepButton;
    private JButton autoPlayButton;
    private JButton resetButton;
    private JButton compareAllButton;

    private JSlider speedSlider;

    private JLabel statusLabel;
    private JLabel stepLabel;
    private JLabel metricsLabel;
    private JLabel playbackLabel;

    private JTextArea descriptionArea;
    private JTextArea complexityArea;

    private SortingArrayDrawingPanel drawingPanel;

    private DefaultTableModel compareTableModel;
    private JTable compareTable;

    private DefaultTableModel complexityTableModel;
    private JTable complexityTable;

    private Timer autoPlayTimer;

    private SortResult currentResult;
    private int currentStepIndex;

    public SortingLabPanel() {
        controller = new SortingController();

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
        configureAutoPlayTimer();
        resetVisualization();
    }

    private void buildInterface() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.setFont(
                tabs.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        tabs.addTab(
                "Sort Visualizer",
                createVisualizerTab()
        );

        tabs.addTab(
                "Compare All",
                createCompareTab()
        );

        tabs.addTab(
                "Complexity Guide",
                createComplexityTab()
        );

        add(tabs, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------
    // Visualizer tab
    // ------------------------------------------------------------

    private JPanel createVisualizerTab() {
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
                createInputSection(),
                BorderLayout.NORTH
        );

        drawingPanel =
                new SortingArrayDrawingPanel();

        JScrollPane drawingScrollPane =
                new JScrollPane(drawingPanel);

        drawingScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Array Visualization"
                )
        );

        drawingScrollPane.setPreferredSize(
                new Dimension(900, 360)
        );

        drawingScrollPane.setMinimumSize(
                new Dimension(500, 250)
        );

        drawingScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        drawingScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        drawingScrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        drawingScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                drawingScrollPane,
                BorderLayout.CENTER
        );

        JPanel lowerPanel = createVisualizerInformationPanel();

        JScrollPane lowerScrollPane =
                new JScrollPane(lowerPanel);

        lowerScrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        lowerScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        lowerScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        lowerScrollPane.setPreferredSize(
                new Dimension(900, 220)
        );

        lowerScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                lowerScrollPane,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel createInputSection() {
        JPanel panel = new JPanel(
                new GridBagLayout()
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Input and Sorting Controls"
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets =
                new Insets(5, 6, 5, 6);

        constraints.fill =
                GridBagConstraints.HORIZONTAL;

        constraints.anchor =
                GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;

        panel.add(
                new JLabel("Numbers:"),
                constraints
        );

        inputField = new JTextField(
                "5, 2, 8, 1, 4, 3",
                35
        );

        inputField.setToolTipText(
                "Enter integers separated by commas, spaces or semicolons."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.gridwidth = 3;

        panel.add(inputField, constraints);

        constraints.gridx = 4;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;

        panel.add(
                new JLabel("Random size:"),
                constraints
        );

        randomSizeSpinner = new JSpinner(
                new SpinnerNumberModel(
                        10,
                        1,
                        100,
                        1
                )
        );

        constraints.gridx = 5;

        panel.add(
                randomSizeSpinner,
                constraints
        );

        JButton generateButton =
                new JButton("Generate Random");

        generateButton.addActionListener(
                event -> generateRandomInput()
        );

        constraints.gridx = 6;

        panel.add(
                generateButton,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 1;

        panel.add(
                new JLabel("Algorithm:"),
                constraints
        );

        algorithmComboBox =
                new JComboBox<>(
                        controller.getAlgorithmNames()
                );

        algorithmComboBox.addActionListener(
                event -> refreshSelectedAlgorithmInformation()
        );

        constraints.gridx = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;

        panel.add(
                algorithmComboBox,
                constraints
        );

        JPanel actionPanel = new JPanel(
                new GridLayout(1, 0, 6, 6)
        );

        runButton = new JButton("Run");
        runButton.addActionListener(
                event -> runSelectedAlgorithm()
        );

        stepButton = new JButton("Step");
        stepButton.addActionListener(
                event -> showNextStep()
        );

        autoPlayButton =
                new JButton("Auto-play");

        autoPlayButton.addActionListener(
                event -> toggleAutoPlay()
        );

        resetButton = new JButton("Reset");
        resetButton.addActionListener(
                event -> resetVisualization()
        );

        compareAllButton =
                new JButton("Compare All");

        compareAllButton.addActionListener(
                event -> compareAllAlgorithms()
        );

        actionPanel.add(runButton);
        actionPanel.add(stepButton);
        actionPanel.add(autoPlayButton);
        actionPanel.add(resetButton);
        actionPanel.add(compareAllButton);

        constraints.gridx = 3;
        constraints.gridwidth = 4;
        constraints.weightx = 1.0;

        panel.add(
                actionPanel,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;

        panel.add(
                new JLabel("Auto-play speed:"),
                constraints
        );

        speedSlider = new JSlider(
                150,
                2000,
                800
        );

        speedSlider.setMajorTickSpacing(500);
        speedSlider.setMinorTickSpacing(100);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        speedSlider.addChangeListener(
                event -> updateTimerSpeed()
        );

        constraints.gridx = 1;
        constraints.gridwidth = 6;
        constraints.weightx = 1.0;

        panel.add(
                speedSlider,
                constraints
        );

        return panel;
    }

    private JPanel createVisualizerInformationPanel() {
        JPanel panel = new JPanel(
                new BorderLayout(6, 6)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Current Step and Measurements"
                )
        );

        JPanel labelsPanel = new JPanel(
                new GridLayout(4, 1, 3, 3)
        );

        statusLabel = new JLabel(
                "Enter numbers and select an algorithm.",
                SwingConstants.CENTER
        );

        statusLabel.setForeground(TITLE_COLOR);
        statusLabel.setFont(
                statusLabel.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        stepLabel = new JLabel(
                "Step: -",
                SwingConstants.CENTER
        );

        metricsLabel = new JLabel(
                "Comparisons: - | Swaps: - | Moves: - "
                        + "| Execution time: -",
                SwingConstants.CENTER
        );

        playbackLabel = new JLabel(
                "Playback: stopped",
                SwingConstants.CENTER
        );

        labelsPanel.add(statusLabel);
        labelsPanel.add(stepLabel);
        labelsPanel.add(metricsLabel);
        labelsPanel.add(playbackLabel);

        descriptionArea = new JTextArea(
                "Run an algorithm to display its recorded steps.",
                3,
                60
        );

        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(
                new Color(246, 248, 250)
        );

        descriptionArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Step Description"
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );

        complexityArea = new JTextArea(
                3,
                60
        );

        complexityArea.setEditable(false);
        complexityArea.setLineWrap(true);
        complexityArea.setWrapStyleWord(true);
        complexityArea.setBackground(
                new Color(246, 248, 250)
        );

        complexityArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Selected Algorithm"
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );

        JPanel textPanel = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        textPanel.add(descriptionArea);
        textPanel.add(complexityArea);

        panel.add(
                labelsPanel,
                BorderLayout.NORTH
        );

        panel.add(
                textPanel,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ------------------------------------------------------------
    // Compare All tab
    // ------------------------------------------------------------

    private JPanel createCompareTab() {
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

        JPanel inputPanel = new JPanel(
                new BorderLayout(6, 6)
        );

        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Compare the Five Required Algorithms"
                )
        );

        JTextArea explanationArea = new JTextArea(
                "Compare All runs Bubble Sort, Selection Sort, "
                        + "Insertion Sort, Merge Sort and the required "
                        + "first-pivot Quick Sort on independent copies "
                        + "of the same input. Randomized Quick Sort is "
                        + "available as an additional individual option.",
                3,
                60
        );

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setOpaque(false);

        JButton compareButton =
                new JButton("Run Compare All");

        compareButton.addActionListener(
                event -> compareAllAlgorithms()
        );

        inputPanel.add(
                explanationArea,
                BorderLayout.CENTER
        );

        inputPanel.add(
                compareButton,
                BorderLayout.EAST
        );

        panel.add(
                inputPanel,
                BorderLayout.NORTH
        );

        compareTableModel = new DefaultTableModel(
                new Object[]{
                        "Algorithm",
                        "Sorted Result",
                        "Comparisons",
                        "Swaps",
                        "Moves",
                        "Time (ns)",
                        "Time (ms)",
                        "Steps"
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

        compareTable = new JTable(
                compareTableModel
        );

        compareTable.setRowHeight(28);
        compareTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        compareTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(190);

        compareTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(380);

        compareTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(110);

        compareTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(90);

        compareTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(90);

        compareTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(120);

        compareTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);

        compareTable.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(80);

        JScrollPane tableScrollPane =
                new JScrollPane(compareTable);

        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Comparison Results"
                )
        );

        tableScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        tableScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        tableScrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        tableScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ------------------------------------------------------------
    // Complexity tab
    // ------------------------------------------------------------

    private JPanel createComplexityTab() {
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

        JTextArea introduction = new JTextArea(
                "This table summarizes the theoretical time "
                        + "complexity, extra space and stability "
                        + "published by each sorting implementation.",
                2,
                60
        );

        introduction.setEditable(false);
        introduction.setLineWrap(true);
        introduction.setWrapStyleWord(true);
        introduction.setOpaque(false);

        introduction.setBorder(
                BorderFactory.createTitledBorder(
                        "Algorithm Complexity Guide"
                )
        );

        panel.add(
                introduction,
                BorderLayout.NORTH
        );

        complexityTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Algorithm",
                                "Best Case",
                                "Average Case",
                                "Worst Case",
                                "Extra Space",
                                "Stable"
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

        complexityTable =
                new JTable(complexityTableModel);

        complexityTable.setRowHeight(28);
        complexityTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        complexityTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(230);

        complexityTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(130);

        complexityTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(160);

        complexityTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(130);

        complexityTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(230);

        complexityTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(90);

        JScrollPane tableScrollPane =
                new JScrollPane(complexityTable);

        tableScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        tableScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        tableScrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        tableScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        populateComplexityTable();

        return panel;
    }

    // ------------------------------------------------------------
    // Sorting actions
    // ------------------------------------------------------------

    private void generateRandomInput() {
        try {
            int size =
                    (Integer) randomSizeSpinner.getValue();

            int[] values =
                    controller.generateRandomInput(size);

            inputField.setText(
                    formatInputForField(values)
            );

            stopAutoPlay();

            currentResult = null;
            currentStepIndex = -1;

            drawingPanel.updateArray(
                    values,
                    new int[0]
            );

            statusLabel.setText(
                    "Generated "
                            + size
                            + " random value(s)."
            );

            statusLabel.setForeground(SUCCESS_COLOR);

            stepLabel.setText("Step: input");
            metricsLabel.setText(
                    "Comparisons: - | Swaps: - | Moves: - "
                            + "| Execution time: -"
            );

            descriptionArea.setText(
                    "Random input generated. Select an algorithm "
                            + "and choose Run."
            );

        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void runSelectedAlgorithm() {
        stopAutoPlay();

        try {
            int[] input =
                    controller.parseInput(
                            inputField.getText()
                    );

            String algorithmName =
                    (String) algorithmComboBox
                            .getSelectedItem();

            currentResult =
                    controller.runAlgorithm(
                            algorithmName,
                            input
                    );

            currentStepIndex = -1;

            drawingPanel.updateArray(
                    currentResult.originalArray(),
                    new int[0]
            );

            statusLabel.setText(
                    algorithmName
                            + " completed. Use Step or Auto-play "
                            + "to inspect the recorded work."
            );

            statusLabel.setForeground(SUCCESS_COLOR);

            stepLabel.setText(
                    "Step: input / "
                            + currentResult.stepCount()
                            + " recorded step(s)"
            );

            metricsLabel.setText(
                    createMetricsText(currentResult)
            );

            descriptionArea.setText(
                    "Initial input: "
                            + controller.formatArray(
                                    currentResult.originalArray()
                            )
            );

            refreshSelectedAlgorithmInformation();
            updatePlaybackControls();

            if (currentResult.stepCount() == 0) {
                drawingPanel.updateArray(
                        currentResult.sortedArray(),
                        new int[0]
                );

                descriptionArea.setText(
                        "The input contains one value, so no "
                                + "sorting steps were required."
                );

                stepLabel.setText(
                        "Step: complete"
                );
            }

        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showNextStep() {
        if (currentResult == null) {
            showError(
                    "Run a sorting algorithm before using Step."
            );
            return;
        }

        if (currentStepIndex
                >= currentResult.stepCount() - 1) {

            stopAutoPlay();

            statusLabel.setText(
                    "The final recorded sorting step is displayed."
            );

            statusLabel.setForeground(SUCCESS_COLOR);
            updatePlaybackControls();
            return;
        }

        currentStepIndex++;

        SortStep step =
                currentResult.steps()
                        .get(currentStepIndex);

        drawingPanel.updateArray(
                step.arrayState(),
                step.highlightedIndices()
        );

        stepLabel.setText(
                "Step "
                        + step.stepNumber()
                        + " of "
                        + currentResult.stepCount()
        );

        descriptionArea.setText(
                step.description()
                        + System.lineSeparator()
                        + "Array: "
                        + controller.formatArray(
                                step.arrayState()
                        )
        );

        if (currentStepIndex
                == currentResult.stepCount() - 1) {

            statusLabel.setText(
                    "Sorting visualization complete."
            );

            statusLabel.setForeground(SUCCESS_COLOR);
            stopAutoPlay();
        } else {
            statusLabel.setText(
                    "Displayed step "
                            + step.stepNumber()
                            + "."
            );

            statusLabel.setForeground(TITLE_COLOR);
        }

        updatePlaybackControls();
    }

    private void toggleAutoPlay() {
        if (currentResult == null) {
            showError(
                    "Run a sorting algorithm before starting Auto-play."
            );
            return;
        }

        if (currentResult.stepCount() == 0) {
            showError(
                    "This result has no recorded sorting steps."
            );
            return;
        }

        if (autoPlayTimer.isRunning()) {
            stopAutoPlay();
            return;
        }

        if (currentStepIndex
                >= currentResult.stepCount() - 1) {

            currentStepIndex = -1;

            drawingPanel.updateArray(
                    currentResult.originalArray(),
                    new int[0]
            );
        }

        updateTimerSpeed();
        autoPlayTimer.start();

        autoPlayButton.setText("Pause");
        playbackLabel.setText(
                "Playback: running at "
                        + speedSlider.getValue()
                        + " ms per step"
        );

        statusLabel.setText(
                "Auto-play started."
        );

        statusLabel.setForeground(TITLE_COLOR);
    }

    private void configureAutoPlayTimer() {
        autoPlayTimer = new Timer(
                speedSlider.getValue(),
                event -> showNextStep()
        );

        autoPlayTimer.setRepeats(true);
    }

    private void updateTimerSpeed() {
        if (autoPlayTimer == null) {
            return;
        }

        int delay = speedSlider.getValue();

        autoPlayTimer.setDelay(delay);
        autoPlayTimer.setInitialDelay(delay);

        if (autoPlayTimer.isRunning()) {
            playbackLabel.setText(
                    "Playback: running at "
                            + delay
                            + " ms per step"
            );
        }
    }

    private void stopAutoPlay() {
        if (autoPlayTimer != null
                && autoPlayTimer.isRunning()) {

            autoPlayTimer.stop();
        }

        if (autoPlayButton != null) {
            autoPlayButton.setText("Auto-play");
        }

        if (playbackLabel != null) {
            playbackLabel.setText(
                    "Playback: stopped"
            );
        }
    }

    private void resetVisualization() {
        stopAutoPlay();

        currentResult = null;
        currentStepIndex = -1;

        if (drawingPanel != null) {
            drawingPanel.updateArray(
                    new int[0],
                    new int[0]
            );
        }

        if (statusLabel != null) {
            statusLabel.setText(
                    "Enter numbers and select an algorithm."
            );

            statusLabel.setForeground(TITLE_COLOR);
        }

        if (stepLabel != null) {
            stepLabel.setText("Step: -");
        }

        if (metricsLabel != null) {
            metricsLabel.setText(
                    "Comparisons: - | Swaps: - | Moves: - "
                            + "| Execution time: -"
            );
        }

        if (descriptionArea != null) {
            descriptionArea.setText(
                    "Run an algorithm to display its recorded steps."
            );
        }

        refreshSelectedAlgorithmInformation();
        updatePlaybackControls();
    }

    private void updatePlaybackControls() {
        boolean hasResult =
                currentResult != null;

        boolean hasSteps =
                hasResult
                        && currentResult.stepCount() > 0;

        boolean hasNextStep =
                hasSteps
                        && currentStepIndex
                        < currentResult.stepCount() - 1;

        stepButton.setEnabled(hasNextStep);
        autoPlayButton.setEnabled(hasSteps);
        resetButton.setEnabled(true);
    }

    // ------------------------------------------------------------
    // Compare All
    // ------------------------------------------------------------

    private void compareAllAlgorithms() {
        try {
            int[] input =
                    controller.parseInput(
                            inputField.getText()
                    );

            List<SortResult> results =
                    controller.compareAll(input);

            compareTableModel.setRowCount(0);

            for (SortResult result : results) {
                compareTableModel.addRow(
                        new Object[]{
                                result.algorithmName(),
                                controller.formatArray(
                                        result.sortedArray()
                                ),
                                result.comparisonCount(),
                                result.swapCount(),
                                result.moveCount(),
                                result.executionTimeNanos(),
                                String.format(
                                        "%.6f",
                                        result.executionTimeMillis()
                                ),
                                result.stepCount()
                        }
                );
            }

            statusLabel.setText(
                    "Compare All completed for "
                            + input.length
                            + " input value(s)."
            );

            statusLabel.setForeground(SUCCESS_COLOR);

            if (compareTableModel.getRowCount() > 0) {
                compareTable.setRowSelectionInterval(
                        0,
                        0
                );
            }

        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Complexity information
    // ------------------------------------------------------------

    private void populateComplexityTable() {
        complexityTableModel.setRowCount(0);

        for (String algorithmName
                : controller.getAlgorithmNames()) {

            AlgorithmInformation information =
                    controller.getAlgorithmInformation(
                            algorithmName
                    );

            complexityTableModel.addRow(
                    new Object[]{
                            information.name(),
                            information.bestCase(),
                            information.averageCase(),
                            information.worstCase(),
                            information.spaceComplexity(),
                            information.stable()
                                    ? "Yes"
                                    : "No"
                    }
            );
        }
    }

    private void refreshSelectedAlgorithmInformation() {
        if (algorithmComboBox == null
                || complexityArea == null) {
            return;
        }

        String algorithmName =
                (String) algorithmComboBox
                        .getSelectedItem();

        if (algorithmName == null) {
            complexityArea.setText(
                    "Select an algorithm."
            );
            return;
        }

        AlgorithmInformation information =
                controller.getAlgorithmInformation(
                        algorithmName
                );

        complexityArea.setText(
                "Algorithm: "
                        + information.name()
                        + System.lineSeparator()
                        + "Best: "
                        + information.bestCase()
                        + " | Average: "
                        + information.averageCase()
                        + " | Worst: "
                        + information.worstCase()
                        + System.lineSeparator()
                        + "Extra space: "
                        + information.spaceComplexity()
                        + " | Stable: "
                        + (information.stable()
                                ? "Yes"
                                : "No")
        );
    }

    // ------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------

    private String createMetricsText(
            SortResult result
    ) {
        return String.format(
                "Comparisons: %,d | Swaps: %,d | Moves: %,d "
                        + "| Execution time: %,d ns (%.6f ms)",
                result.comparisonCount(),
                result.swapCount(),
                result.moveCount(),
                result.executionTimeNanos(),
                result.executionTimeMillis()
        );
    }

    private String formatInputForField(int[] values) {
        StringBuilder text = new StringBuilder();

        for (int index = 0;
                index < values.length;
                index++) {

            text.append(values[index]);

            if (index < values.length - 1) {
                text.append(", ");
            }
        }

        return text.toString();
    }

    private void showError(String message) {
        stopAutoPlay();

        statusLabel.setText(message);
        statusLabel.setForeground(ERROR_COLOR);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Sorting Lab",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ------------------------------------------------------------
    // Array visualization
    // ------------------------------------------------------------

    private static final class SortingArrayDrawingPanel
            extends JPanel {

        private int[] values = new int[0];
        private int[] highlightedIndices =
                new int[0];

        SortingArrayDrawingPanel() {
            setBackground(Color.WHITE);

            setPreferredSize(
                    new Dimension(900, 330)
            );

            setMinimumSize(
                    new Dimension(500, 250)
            );
        }

        void updateArray(
                int[] values,
                int[] highlightedIndices
        ) {
            this.values = copyArray(values);

            this.highlightedIndices =
                    copyArray(highlightedIndices);

            updatePreferredWidth();
            revalidate();
            repaint();
        }

        private void updatePreferredWidth() {
            int requiredWidth = Math.max(
                    900,
                    100 + values.length * 100
            );

            setPreferredSize(
                    new Dimension(
                            requiredWidth,
                            330
                    )
            );
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

                if (values.length == 0) {
                    drawEmptyMessage(graphics2D);
                    return;
                }

                drawArray(graphics2D);

            } finally {
                graphics2D.dispose();
            }
        }

        private void drawArray(
                Graphics2D graphics2D
        ) {
            int cellWidth = 76;
            int gap = 16;
            int cellY = 115;
            int cellHeight = 85;

            int totalWidth =
                    values.length * cellWidth
                            + (values.length - 1) * gap;

            int startX = Math.max(
                    45,
                    (getWidth() - totalWidth) / 2
            );

            for (int index = 0;
                    index < values.length;
                    index++) {

                int x =
                        startX + index * (cellWidth + gap);

                boolean highlighted =
                        containsIndex(index);

                graphics2D.setColor(
                        highlighted
                                ? HIGHLIGHT_COLOR
                                : NORMAL_CELL_COLOR
                );

                graphics2D.fillRoundRect(
                        x,
                        cellY,
                        cellWidth,
                        cellHeight,
                        12,
                        12
                );

                graphics2D.setColor(
                        new Color(50, 65, 80)
                );

                graphics2D.setStroke(
                        new BasicStroke(1.7f)
                );

                graphics2D.drawRoundRect(
                        x,
                        cellY,
                        cellWidth,
                        cellHeight,
                        12,
                        12
                );

                String valueText =
                        String.valueOf(values[index]);

                int valueWidth = graphics2D
                        .getFontMetrics()
                        .stringWidth(valueText);

                graphics2D.drawString(
                        valueText,
                        x + (cellWidth - valueWidth) / 2,
                        cellY + 48
                );

                String indexText =
                        "index " + index;

                int indexWidth = graphics2D
                        .getFontMetrics()
                        .stringWidth(indexText);

                graphics2D.drawString(
                        indexText,
                        x + (cellWidth - indexWidth) / 2,
                        cellY + cellHeight + 24
                );

                if (highlighted) {
                    Font originalFont =
                            graphics2D.getFont();

                    graphics2D.setFont(
                            originalFont.deriveFont(
                                    Font.BOLD
                            )
                    );

                    String label = "ACTIVE";

                    int labelWidth = graphics2D
                            .getFontMetrics()
                            .stringWidth(label);

                    graphics2D.setColor(
                            new Color(155, 85, 15)
                    );

                    graphics2D.drawString(
                            label,
                            x + (cellWidth - labelWidth) / 2,
                            cellY - 15
                    );

                    graphics2D.setFont(originalFont);
                }
            }
        }

        private boolean containsIndex(int index) {
            for (int highlighted
                    : highlightedIndices) {

                if (highlighted == index) {
                    return true;
                }
            }

            return false;
        }

        private void drawEmptyMessage(
                Graphics2D graphics2D
        ) {
            String message =
                    "Run a sorting algorithm to display the array.";

            int textWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(message);

            graphics2D.setColor(
                    new Color(95, 95, 95)
            );

            graphics2D.drawString(
                    message,
                    Math.max(
                            10,
                            (getWidth() - textWidth) / 2
                    ),
                    getHeight() / 2
            );
        }

        private static int[] copyArray(
                int[] source
        ) {
            int[] copy = new int[source.length];

            for (int index = 0;
                    index < source.length;
                    index++) {

                copy[index] = source[index];
            }

            return copy;
        }
    }
}
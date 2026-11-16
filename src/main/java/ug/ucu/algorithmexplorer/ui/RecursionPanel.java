package ug.ucu.algorithmexplorer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import ug.ucu.algorithmexplorer.controllers.RecursionController;
import ug.ucu.algorithmexplorer.models.FactorialMethod;
import ug.ucu.algorithmexplorer.models.FactorialResult;
import ug.ucu.algorithmexplorer.models.FibonacciMethod;
import ug.ucu.algorithmexplorer.models.FibonacciResult;

/**
 * Swing interface for the Recursion Lab.
 *
 * The panel handles input and display only. Calculations are delegated
 * to RecursionController and the recursion backend classes.
 */
public class RecursionPanel extends JPanel {

    private static final Color TITLE_COLOR = new Color(30, 74, 120);
    private static final Color ERROR_COLOR = new Color(180, 35, 35);
    private static final Color SUCCESS_COLOR = new Color(25, 115, 60);

    private final RecursionController controller;

    private JTextField fibonacciInputField;
    private JComboBox<FibonacciMethod> fibonacciMethodBox;
    private JButton fibonacciCalculateButton;
    private JTextArea fibonacciValueArea;
    private JLabel fibonacciOperationsLabel;
    private JLabel fibonacciTimeLabel;
    private JLabel fibonacciStatusLabel;

    private JTextField factorialInputField;
    private JComboBox<FactorialMethod> factorialMethodBox;
    private JButton factorialCalculateButton;
    private JTextArea factorialValueArea;
    private JLabel factorialOperationsLabel;
    private JLabel factorialTimeLabel;
    private JLabel factorialStatusLabel;

    public RecursionPanel() {
        this(new RecursionController());
    }

    public RecursionPanel(RecursionController controller) {
        if (controller == null) {
            throw new IllegalArgumentException(
                    "Recursion controller cannot be null."
            );
        }

        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        buildInterface();
    }

    private void buildInterface() {
        add(createHeadingPanel(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(
                tabbedPane.getFont().deriveFont(Font.BOLD, 14f)
        );

        tabbedPane.addTab(
                "Fibonacci",
                createFibonacciPanel()
        );

        tabbedPane.addTab(
                "Factorial",
                createFactorialPanel()
        );

        tabbedPane.addTab(
                "Tower of Hanoi",
                new HanoiPanel(controller)
        );

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeadingPanel() {
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 15, 0)
        );

        JLabel headingLabel = new JLabel("Recursion Lab");
        headingLabel.setForeground(TITLE_COLOR);
        headingLabel.setFont(
                headingLabel.getFont().deriveFont(Font.BOLD, 25f)
        );

        JLabel descriptionLabel = new JLabel(
                "Explore recursive and iterative procedures, "
                        + "their operation counts and execution times."
        );
        descriptionLabel.setFont(
                descriptionLabel.getFont().deriveFont(Font.PLAIN, 14f)
        );

        headingPanel.add(headingLabel, BorderLayout.NORTH);
        headingPanel.add(descriptionLabel, BorderLayout.SOUTH);

        return headingPanel;
    }

    private JPanel createFibonacciPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout(0, 15));
        outerPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Fibonacci Input"
                )
        );

        GridBagConstraints constraints = createConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        inputPanel.add(new JLabel("Enter n:"), constraints);

        fibonacciInputField = new JTextField(15);
        fibonacciInputField.setToolTipText(
                "Enter a non-negative whole number."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(fibonacciInputField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Method:"), constraints);

        fibonacciMethodBox = new JComboBox<>(
                FibonacciMethod.values()
        );

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(fibonacciMethodBox, constraints);

        fibonacciCalculateButton = new JButton(
                "Calculate Fibonacci"
        );
        fibonacciCalculateButton.addActionListener(
                event -> calculateFibonacci()
        );

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        inputPanel.add(fibonacciCalculateButton, constraints);

        JLabel warningLabel = new JLabel(
                "<html><b>Safety note:</b> Naive recursion is limited "
                        + "to n ≤ 30 because repeated recursive calls "
                        + "grow very quickly.</html>"
        );
        warningLabel.setForeground(new Color(145, 85, 10));

        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(warningLabel, constraints);

        fibonacciStatusLabel = new JLabel(" ");
        fibonacciStatusLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        constraints.gridy = 4;
        inputPanel.add(fibonacciStatusLabel, constraints);

        outerPanel.add(inputPanel, BorderLayout.NORTH);
        outerPanel.add(createFibonacciResultPanel(), BorderLayout.CENTER);

        return outerPanel;
    }

    private JPanel createFibonacciResultPanel() {
        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Fibonacci Result"
                )
        );

        GridBagConstraints constraints = createConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        resultPanel.add(new JLabel("Value:"), constraints);

        fibonacciValueArea = createResultArea();

        JScrollPane scrollPane = new JScrollPane(
                fibonacciValueArea
        );
        scrollPane.setPreferredSize(new Dimension(500, 130));

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        resultPanel.add(scrollPane, constraints);

        fibonacciOperationsLabel = new JLabel("Not calculated");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        resultPanel.add(fibonacciOperationsLabel, constraints);

        fibonacciTimeLabel = new JLabel("Execution time: -");
        constraints.gridy = 2;
        resultPanel.add(fibonacciTimeLabel, constraints);

        return resultPanel;
    }

    private JPanel createFactorialPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout(0, 15));
        outerPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Factorial Input"
                )
        );

        GridBagConstraints constraints = createConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        inputPanel.add(new JLabel("Enter n:"), constraints);

        factorialInputField = new JTextField(15);
        factorialInputField.setToolTipText(
                "Enter a non-negative whole number."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(factorialInputField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Method:"), constraints);

        factorialMethodBox = new JComboBox<>(
                FactorialMethod.values()
        );

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(factorialMethodBox, constraints);

        factorialCalculateButton = new JButton(
                "Calculate Factorial"
        );
        factorialCalculateButton.addActionListener(
                event -> calculateFactorial()
        );

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        inputPanel.add(factorialCalculateButton, constraints);

        JLabel informationLabel = new JLabel(
                "<html>Both methods accept n = 0, where 0! = 1. "
                        + "Negative input is not permitted.</html>"
        );
        informationLabel.setForeground(new Color(50, 80, 120));

        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(informationLabel, constraints);

        factorialStatusLabel = new JLabel(" ");
        factorialStatusLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        constraints.gridy = 4;
        inputPanel.add(factorialStatusLabel, constraints);

        outerPanel.add(inputPanel, BorderLayout.NORTH);
        outerPanel.add(createFactorialResultPanel(), BorderLayout.CENTER);

        return outerPanel;
    }

    private JPanel createFactorialResultPanel() {
        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Factorial Result"
                )
        );

        GridBagConstraints constraints = createConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        resultPanel.add(new JLabel("Value:"), constraints);

        factorialValueArea = createResultArea();

        JScrollPane scrollPane = new JScrollPane(
                factorialValueArea
        );
        scrollPane.setPreferredSize(new Dimension(500, 130));

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        resultPanel.add(scrollPane, constraints);

        factorialOperationsLabel = new JLabel("Not calculated");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        resultPanel.add(factorialOperationsLabel, constraints);

        factorialTimeLabel = new JLabel("Execution time: -");
        constraints.gridy = 2;
        resultPanel.add(factorialTimeLabel, constraints);

        return resultPanel;
    }

    

    private JTextArea createResultArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(
                area.getFont().deriveFont(Font.PLAIN, 15f)
        );
        area.setBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        return area;
    }

    private GridBagConstraints createConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(7, 7, 7, 7);
        constraints.anchor = GridBagConstraints.WEST;

        return constraints;
    }

    private void calculateFibonacci() {
        Integer input = readNonNegativeInteger(
                fibonacciInputField,
                fibonacciStatusLabel
        );

        if (input == null) {
            return;
        }

        FibonacciMethod method =
                (FibonacciMethod) fibonacciMethodBox.getSelectedItem();

        setFibonacciBusy(true);
        showStatus(
                fibonacciStatusLabel,
                "Calculating...",
                TITLE_COLOR
        );

        SwingWorker<FibonacciResult, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected FibonacciResult doInBackground() {
                        return controller.calculateFibonacci(
                                input,
                                method
                        );
                    }

                    @Override
                    protected void done() {
                        try {
                            FibonacciResult result = get();
                            displayFibonacciResult(result);

                            showStatus(
                                    fibonacciStatusLabel,
                                    "Calculation completed successfully.",
                                    SUCCESS_COLOR
                            );
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();

                            showStatus(
                                    fibonacciStatusLabel,
                                    "The calculation was interrupted.",
                                    ERROR_COLOR
                            );
                        } catch (ExecutionException exception) {
                            showCalculationError(
                                    fibonacciStatusLabel,
                                    exception.getCause()
                            );
                        } finally {
                            setFibonacciBusy(false);
                        }
                    }
                };

        worker.execute();
    }

    private void calculateFactorial() {
        Integer input = readNonNegativeInteger(
                factorialInputField,
                factorialStatusLabel
        );

        if (input == null) {
            return;
        }

        FactorialMethod method =
                (FactorialMethod) factorialMethodBox.getSelectedItem();

        setFactorialBusy(true);
        showStatus(
                factorialStatusLabel,
                "Calculating...",
                TITLE_COLOR
        );

        SwingWorker<FactorialResult, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected FactorialResult doInBackground() {
                        return controller.calculateFactorial(
                                input,
                                method
                        );
                    }

                    @Override
                    protected void done() {
                        try {
                            FactorialResult result = get();
                            displayFactorialResult(result);

                            showStatus(
                                    factorialStatusLabel,
                                    "Calculation completed successfully.",
                                    SUCCESS_COLOR
                            );
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();

                            showStatus(
                                    factorialStatusLabel,
                                    "The calculation was interrupted.",
                                    ERROR_COLOR
                            );
                        } catch (ExecutionException exception) {
                            showCalculationError(
                                    factorialStatusLabel,
                                    exception.getCause()
                            );
                        } finally {
                            setFactorialBusy(false);
                        }
                    }
                };

        worker.execute();
    }

    private Integer readNonNegativeInteger(
            JTextField inputField,
            JLabel statusLabel
    ) {
        String text = inputField.getText().trim();

        if (text.isEmpty()) {
            showStatus(
                    statusLabel,
                    "Enter a value before calculating.",
                    ERROR_COLOR
            );
            inputField.requestFocusInWindow();
            return null;
        }

        try {
            int value = Integer.parseInt(text);

            if (value < 0) {
                showStatus(
                        statusLabel,
                        "The input must be zero or greater.",
                        ERROR_COLOR
                );
                inputField.requestFocusInWindow();
                return null;
            }

            return value;
        } catch (NumberFormatException exception) {
            showStatus(
                    statusLabel,
                    "Enter a valid whole number.",
                    ERROR_COLOR
            );
            inputField.requestFocusInWindow();
            return null;
        }
    }

    private void displayFibonacciResult(
            FibonacciResult result
    ) {
        fibonacciValueArea.setText(result.value().toString());

        fibonacciOperationsLabel.setText(
                result.method().getDisplayName()
                        + " operations: "
                        + result.operationCount()
                        + operationWord(result.method())
        );

        fibonacciTimeLabel.setText(
                String.format(
                        "Execution time: %,d ns (%.6f ms)",
                        result.executionTimeNanos(),
                        result.executionTimeMillis()
                )
        );
    }

    private void displayFactorialResult(
            FactorialResult result
    ) {
        factorialValueArea.setText(result.value().toString());

        String operationType =
                result.method() == FactorialMethod.RECURSIVE
                        ? " recursive calls"
                        : " loop iterations";

        factorialOperationsLabel.setText(
                result.method().getDisplayName()
                        + " operations: "
                        + result.operationCount()
                        + operationType
        );

        factorialTimeLabel.setText(
                String.format(
                        "Execution time: %,d ns (%.6f ms)",
                        result.executionTimeNanos(),
                        result.executionTimeMillis()
                )
        );
    }

    private String operationWord(FibonacciMethod method) {
        if (method == FibonacciMethod.BOTTOM_UP) {
            return " loop iterations";
        }

        return " recursive calls";
    }

    private void setFibonacciBusy(boolean busy) {
        fibonacciCalculateButton.setEnabled(!busy);
        fibonacciInputField.setEnabled(!busy);
        fibonacciMethodBox.setEnabled(!busy);
    }

    private void setFactorialBusy(boolean busy) {
        factorialCalculateButton.setEnabled(!busy);
        factorialInputField.setEnabled(!busy);
        factorialMethodBox.setEnabled(!busy);
    }

    private void showCalculationError(
            JLabel statusLabel,
            Throwable cause
    ) {
        String message = cause == null
                ? "The calculation could not be completed."
                : cause.getMessage();

        if (message == null || message.isBlank()) {
            message = "The calculation could not be completed.";
        }

        showStatus(statusLabel, message, ERROR_COLOR);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Calculation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showStatus(
            JLabel statusLabel,
            String message,
            Color color
    ) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}
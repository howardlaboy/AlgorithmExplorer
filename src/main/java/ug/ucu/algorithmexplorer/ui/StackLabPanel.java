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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import ug.ucu.algorithmexplorer.controllers.DataStructuresController;
import ug.ucu.algorithmexplorer.models.BracketCheckResult;
import ug.ucu.algorithmexplorer.models.BracketCheckStep;

/**
 * Interactive interface for the fixed-capacity array stack and the
 * balanced-bracket application.
 *
 * Stack operations and the bracket checker use separate tabs so that
 * both activities remain visible and accessible at different window
 * sizes.
 */
public class StackLabPanel extends JPanel {

    private static final Color TITLE_COLOR =
            new Color(30, 74, 120);

    private static final Color SUCCESS_COLOR =
            new Color(25, 115, 60);

    private static final Color ERROR_COLOR =
            new Color(180, 35, 35);

    private final DataStructuresController controller;

    private JSpinner capacitySpinner;
    private JButton createButton;

    private JTextField valueField;
    private JButton pushButton;
    private JButton popButton;
    private JButton peekButton;
    private JButton sizeButton;
    private JButton emptyButton;
    private JButton fullButton;
    private JButton displayButton;
    private JButton clearButton;

    private JLabel stackStatusLabel;
    private JLabel stackInformationLabel;

    private StackDrawingPanel drawingPanel;

    private JTextField expressionField;
    private JLabel bracketResultLabel;
    private JLabel bracketMetricsLabel;

    private DefaultTableModel bracketTableModel;
    private JTable bracketTable;

    public StackLabPanel(
            DataStructuresController controller
    ) {
        if (controller == null) {
            throw new IllegalArgumentException(
                    "Data-structures controller cannot be null."
            );
        }

        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        buildInterface();
        setStackOperationsEnabled(false);
    }

    private void buildInterface() {
        JTabbedPane activityTabs = new JTabbedPane();

        activityTabs.setFont(
                activityTabs.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        activityTabs.addTab(
                "Stack Operations",
                createStackOperationsTab()
        );

        activityTabs.addTab(
                "Balanced-Bracket Checker",
                createBracketCheckerTab()
        );

        add(activityTabs, BorderLayout.CENTER);
    }

    private JPanel createStackOperationsTab() {
        JPanel tabPanel = new JPanel(
                new BorderLayout(12, 12)
        );

        tabPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        JPanel controlsPanel = createStackControls();

        JScrollPane controlsScrollPane =
                new JScrollPane(controlsPanel);

        controlsScrollPane.setPreferredSize(
                new Dimension(350, 430)
        );

        controlsScrollPane.setMinimumSize(
                new Dimension(290, 300)
        );

        controlsScrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        controlsScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        controlsScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        controlsScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        drawingPanel = new StackDrawingPanel();

        JScrollPane drawingScrollPane =
                new JScrollPane(drawingPanel);

        drawingScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Stack Visualisation"
                )
        );

        drawingScrollPane.setPreferredSize(
                new Dimension(700, 430)
        );

        drawingScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        drawingScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        drawingScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        tabPanel.add(
                controlsScrollPane,
                BorderLayout.WEST
        );

        tabPanel.add(
                drawingScrollPane,
                BorderLayout.CENTER
        );

        tabPanel.add(
                createStackInformationPanel(),
                BorderLayout.SOUTH
        );

        return tabPanel;
    }

    private JPanel createStackControls() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setPreferredSize(
                new Dimension(325, 390)
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Array-Based Fixed-Capacity Stack"
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                8,
                                12,
                                8
                        )
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;

        panel.add(new JLabel("Capacity:"), constraints);

        capacitySpinner = new JSpinner(
                new SpinnerNumberModel(
                        5,
                        1,
                        20,
                        1
                )
        );

        capacitySpinner.setToolTipText(
                "Choose a capacity from 1 to 20."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(capacitySpinner, constraints);

        createButton = new JButton("Create Stack");

        createButton.addActionListener(
                event -> createStack()
        );

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;

        panel.add(createButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;

        panel.add(new JLabel("Value:"), constraints);

        valueField = new JTextField(12);

        valueField.setToolTipText(
                "Enter an integer to push onto the stack."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(valueField, constraints);

        JPanel operationPanel = new JPanel(
                new GridLayout(0, 2, 7, 7)
        );

        operationPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Stack Operations"
                )
        );

        pushButton = createOperationButton(
                "Push",
                this::pushValue
        );

        popButton = createOperationButton(
                "Pop",
                this::popValue
        );

        peekButton = createOperationButton(
                "Peek",
                this::peekValue
        );

        sizeButton = createOperationButton(
                "Size",
                this::showSize
        );

        emptyButton = createOperationButton(
                "Is Empty",
                this::showEmpty
        );

        fullButton = createOperationButton(
                "Is Full",
                this::showFull
        );

        displayButton = createOperationButton(
                "Display",
                this::showDisplay
        );

        clearButton = createOperationButton(
                "Clear",
                this::clearStack
        );

        operationPanel.add(pushButton);
        operationPanel.add(popButton);
        operationPanel.add(peekButton);
        operationPanel.add(sizeButton);
        operationPanel.add(emptyButton);
        operationPanel.add(fullButton);
        operationPanel.add(displayButton);
        operationPanel.add(clearButton);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;

        panel.add(operationPanel, constraints);

        JTextArea explanationArea = new JTextArea(
                "Changing the capacity and selecting Recreate Stack "
                        + "creates a new empty stack. Existing stack "
                        + "values are discarded when the stack is "
                        + "recreated.",
                4,
                23
        );

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setOpaque(false);

        explanationArea.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        4,
                        4,
                        4
                )
        );

        constraints.gridy = 4;
        constraints.weighty = 0.0;
        constraints.anchor = GridBagConstraints.NORTH;

        panel.add(explanationArea, constraints);

        GridBagConstraints filler =
                new GridBagConstraints();

        filler.gridx = 0;
        filler.gridy = 5;
        filler.gridwidth = 2;
        filler.weightx = 1.0;
        filler.weighty = 1.0;
        filler.fill = GridBagConstraints.VERTICAL;

        panel.add(new JPanel(), filler);

        return panel;
    }

    private JPanel createStackInformationPanel() {
        JPanel panel = new JPanel(
                new GridLayout(2, 1, 3, 3)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Stack State and Messages"
                )
        );

        stackInformationLabel = new JLabel(
                "Create a stack to begin.",
                SwingConstants.CENTER
        );

        stackStatusLabel = new JLabel(
                "Choose a capacity and select Create Stack.",
                SwingConstants.CENTER
        );

        stackStatusLabel.setForeground(TITLE_COLOR);

        stackStatusLabel.setFont(
                stackStatusLabel.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        panel.add(stackInformationLabel);
        panel.add(stackStatusLabel);

        return panel;
    }

    private JPanel createBracketCheckerTab() {
        JPanel tabPanel = new JPanel(
                new BorderLayout(8, 8)
        );

        tabPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        tabPanel.add(
                createBracketInputPanel(),
                BorderLayout.NORTH
        );

        bracketTableModel = new DefaultTableModel(
                new Object[]{
                        "Position",
                        "Symbol",
                        "Action",
                        "Stack After Symbol",
                        "Valid So Far"
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

        bracketTable = new JTable(bracketTableModel);

        bracketTable.setRowHeight(26);
        bracketTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_LAST_COLUMN
        );

        bracketTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(65);

        bracketTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(75);

        bracketTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(420);

        bracketTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(220);

        bracketTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(100);

        JScrollPane tableScrollPane =
                new JScrollPane(bracketTable);

        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Processing Steps"
                )
        );

        tableScrollPane.setPreferredSize(
                new Dimension(900, 360)
        );

        tableScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        tableScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        tableScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        tableScrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        tabPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        JTextArea explanationArea = new JTextArea(
                "Opening brackets are pushed onto the project's own "
                        + "array stack. A closing bracket must match "
                        + "the current top, after which the matching "
                        + "opening bracket is popped. Ordinary symbols "
                        + "are ignored, but a row is still recorded "
                        + "for each processed symbol.",
                3,
                60
        );

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setOpaque(false);

        explanationArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "How the Checker Works"
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );

        tabPanel.add(
                explanationArea,
                BorderLayout.SOUTH
        );

        return tabPanel;
    }

    private JPanel createBracketInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Expression and Result"
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets = new Insets(5, 6, 5, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;

        panel.add(new JLabel("Expression:"), constraints);

        expressionField =
                new JTextField("{ ( [ ] ) }", 35);

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(expressionField, constraints);

        JButton checkButton =
                new JButton("Check Expression");

        checkButton.addActionListener(
                event -> checkExpression()
        );

        constraints.gridx = 2;
        constraints.weightx = 0.0;

        panel.add(checkButton, constraints);

        bracketResultLabel = new JLabel(
                "Enter an expression and select Check Expression.",
                SwingConstants.CENTER
        );

        bracketResultLabel.setForeground(TITLE_COLOR);
        bracketResultLabel.setFont(
                bracketResultLabel.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;

        panel.add(bracketResultLabel, constraints);

        bracketMetricsLabel = new JLabel(
                "Stack operations: - | Execution time: -",
                SwingConstants.CENTER
        );

        constraints.gridy = 2;

        panel.add(bracketMetricsLabel, constraints);

        return panel;
    }

    private JButton createOperationButton(
            String text,
            Runnable action
    ) {
        JButton button = new JButton(text);
        button.addActionListener(event -> action.run());

        return button;
    }

    private void createStack() {
        int capacity =
                (Integer) capacitySpinner.getValue();

        controller.createStack(capacity);

        drawingPanel.updateStack(
                controller.stackSnapshot(),
                capacity,
                controller.stackTopIndex()
        );

        setStackOperationsEnabled(true);

        /*
         * The capacity spinner deliberately remains enabled so the
         * user can choose a new capacity and recreate the stack.
         */
        capacitySpinner.setEnabled(true);
        createButton.setText("Recreate Stack");

        showStackStatus(
                "Created an empty stack with capacity "
                        + capacity
                        + ".",
                SUCCESS_COLOR
        );

        refreshStackInformation();
        valueField.requestFocusInWindow();
    }

    private void pushValue() {
        Integer value = readStackValue();

        if (value == null) {
            return;
        }

        try {
            controller.pushStack(value);

            showStackStatus(
                    "Pushed " + value + " onto the stack.",
                    SUCCESS_COLOR
            );

            valueField.setText("");
            refreshStackView();
            valueField.requestFocusInWindow();

        } catch (IllegalStateException exception) {
            showStackError(exception);
        }
    }

    private void popValue() {
        try {
            int removed = controller.popStack();

            showStackStatus(
                    "Popped " + removed + " from the stack.",
                    SUCCESS_COLOR
            );

            refreshStackView();

        } catch (IllegalStateException exception) {
            showStackError(exception);
        }
    }

    private void peekValue() {
        try {
            int topValue = controller.peekStack();

            showStackStatus(
                    "Top element: "
                            + topValue
                            + ". The element was not removed.",
                    TITLE_COLOR
            );

            refreshStackView();

        } catch (IllegalStateException exception) {
            showStackError(exception);
        }
    }

    private void showSize() {
        showStackStatus(
                "Stack size: " + controller.stackSize() + ".",
                TITLE_COLOR
        );

        refreshStackInformation();
    }

    private void showEmpty() {
        showStackStatus(
                "is_empty = " + controller.isStackEmpty(),
                TITLE_COLOR
        );

        refreshStackInformation();
    }

    private void showFull() {
        showStackStatus(
                "is_full = " + controller.isStackFull(),
                TITLE_COLOR
        );

        refreshStackInformation();
    }

    private void showDisplay() {
        showStackStatus(
                "Bottom to top: "
                        + controller.displayStack(),
                TITLE_COLOR
        );

        refreshStackInformation();
    }

    private void clearStack() {
        controller.clearStack();

        showStackStatus(
                "The stack was cleared. Capacity remains "
                        + controller.stackCapacity()
                        + ".",
                SUCCESS_COLOR
        );

        refreshStackView();
    }

    private Integer readStackValue() {
        String text = valueField.getText().trim();

        if (text.isEmpty()) {
            showStackStatus(
                    "Enter an integer value.",
                    ERROR_COLOR
            );

            valueField.requestFocusInWindow();
            return null;
        }

        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException exception) {
            showStackStatus(
                    "Enter a valid integer value.",
                    ERROR_COLOR
            );

            valueField.requestFocusInWindow();
            return null;
        }
    }

    private void refreshStackView() {
        drawingPanel.updateStack(
                controller.stackSnapshot(),
                controller.stackCapacity(),
                controller.stackTopIndex()
        );

        refreshStackInformation();
    }

    private void refreshStackInformation() {
        stackInformationLabel.setText(
                "Size: "
                        + controller.stackSize()
                        + " / "
                        + controller.stackCapacity()
                        + " | Top index: "
                        + controller.stackTopIndex()
                        + " | Empty: "
                        + controller.isStackEmpty()
                        + " | Full: "
                        + controller.isStackFull()
        );
    }

    private void setStackOperationsEnabled(
            boolean enabled
    ) {
        valueField.setEnabled(enabled);
        pushButton.setEnabled(enabled);
        popButton.setEnabled(enabled);
        peekButton.setEnabled(enabled);
        sizeButton.setEnabled(enabled);
        emptyButton.setEnabled(enabled);
        fullButton.setEnabled(enabled);
        displayButton.setEnabled(enabled);
        clearButton.setEnabled(enabled);
    }

    private void showStackError(Exception exception) {
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            message = "The stack operation could not be completed.";
        }

        showStackStatus(message, ERROR_COLOR);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Stack Operation",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showStackStatus(
            String message,
            Color color
    ) {
        stackStatusLabel.setText(message);
        stackStatusLabel.setForeground(color);
    }

    private void checkExpression() {
        String expression = expressionField.getText();

        try {
            BracketCheckResult result =
                    controller.checkBrackets(expression);

            displayBracketResult(result);

        } catch (IllegalArgumentException exception) {
            bracketTableModel.setRowCount(0);

            bracketResultLabel.setText(
                    exception.getMessage()
            );

            bracketResultLabel.setForeground(ERROR_COLOR);

            bracketMetricsLabel.setText(
                    "Stack operations: - | Execution time: -"
            );
        }
    }

    private void displayBracketResult(
            BracketCheckResult result
    ) {
        bracketTableModel.setRowCount(0);

        for (BracketCheckStep step : result.steps()) {
            bracketTableModel.addRow(
                    new Object[]{
                            step.position(),
                            printableSymbol(step.symbol()),
                            step.action(),
                            step.stackState(),
                            step.validSoFar()
                    }
            );
        }

        bracketResultLabel.setText(result.message());

        bracketResultLabel.setForeground(
                result.balanced()
                        ? SUCCESS_COLOR
                        : ERROR_COLOR
        );

        bracketMetricsLabel.setText(
                String.format(
                        "Stack operations: %,d | "
                                + "Execution time: %,d ns "
                                + "(%.6f ms)",
                        result.stackOperationCount(),
                        result.executionTimeNanos(),
                        result.executionTimeMillis()
                )
        );

        if (bracketTableModel.getRowCount() > 0) {
            bracketTable.setRowSelectionInterval(0, 0);
            bracketTable.scrollRectToVisible(
                    bracketTable.getCellRect(
                            0,
                            0,
                            true
                    )
            );
        }
    }

    private String printableSymbol(char symbol) {
        return switch (symbol) {
            case ' ' -> "[space]";
            case '\t' -> "[tab]";
            case '\n' -> "[new line]";
            default -> String.valueOf(symbol);
        };
    }

    /**
     * Draws the physical fixed-capacity stack array vertically.
     */
    private static final class StackDrawingPanel
            extends JPanel {

        private Object[] values = new Object[0];
        private int capacity;
        private int topIndex = -1;

        StackDrawingPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(680, 420));
            setMinimumSize(new Dimension(400, 300));
        }

        void updateStack(
                Object[] values,
                int capacity,
                int topIndex
        ) {
            this.values = values.clone();
            this.capacity = capacity;
            this.topIndex = topIndex;

            updatePreferredHeight();
            revalidate();
            repaint();
        }

        private void updatePreferredHeight() {
            int requiredHeight = Math.max(
                    420,
                    90 + capacity * 55
            );

            setPreferredSize(
                    new Dimension(680, requiredHeight)
            );
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D graphics2D =
                    (Graphics2D) graphics.create();

            try {
                graphics2D.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                if (capacity == 0) {
                    drawInitialMessage(graphics2D);
                    return;
                }

                drawStack(graphics2D);

            } finally {
                graphics2D.dispose();
            }
        }

        private void drawStack(Graphics2D graphics2D) {
            int cellWidth = Math.min(
                    260,
                    Math.max(170, getWidth() - 260)
            );

            int cellHeight = 50;
            int stackHeight = cellHeight * capacity;

            int startX =
                    Math.max(100, (getWidth() - cellWidth) / 2);

            int startY =
                    Math.max(45, (getHeight() - stackHeight) / 2);

            for (int visualRow = 0;
                    visualRow < capacity;
                    visualRow++) {

                int arrayIndex =
                        capacity - 1 - visualRow;

                int y =
                        startY + visualRow * cellHeight;

                boolean occupied =
                        arrayIndex < values.length;

                boolean topCell =
                        arrayIndex == topIndex;

                if (topCell) {
                    graphics2D.setColor(
                            new Color(255, 214, 102)
                    );
                } else if (occupied) {
                    graphics2D.setColor(
                            new Color(166, 210, 255)
                    );
                } else {
                    graphics2D.setColor(
                            new Color(240, 243, 247)
                    );
                }

                graphics2D.fillRect(
                        startX,
                        y,
                        cellWidth,
                        cellHeight
                );

                graphics2D.setColor(
                        new Color(55, 70, 85)
                );

                graphics2D.setStroke(
                        new BasicStroke(1.5f)
                );

                graphics2D.drawRect(
                        startX,
                        y,
                        cellWidth,
                        cellHeight
                );

                String indexText =
                        "index " + arrayIndex;

                graphics2D.drawString(
                        indexText,
                        startX - 68,
                        y + cellHeight / 2 + 5
                );

                String valueText = occupied
                        ? String.valueOf(values[arrayIndex])
                        : "empty";

                int valueWidth = graphics2D
                        .getFontMetrics()
                        .stringWidth(valueText);

                graphics2D.drawString(
                        valueText,
                        startX
                                + (cellWidth - valueWidth) / 2,
                        y + cellHeight / 2 + 5
                );

                if (topCell) {
                    Font originalFont =
                            graphics2D.getFont();

                    graphics2D.setColor(
                            new Color(180, 90, 20)
                    );

                    graphics2D.setFont(
                            originalFont.deriveFont(
                                    Font.BOLD
                            )
                    );

                    graphics2D.drawString(
                            "<- TOP",
                            startX + cellWidth + 14,
                            y + cellHeight / 2 + 5
                    );

                    graphics2D.setFont(originalFont);
                }
            }
        }

        private void drawInitialMessage(
                Graphics2D graphics2D
        ) {
            String message =
                    "Choose a capacity and create the stack.";

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
    }
}
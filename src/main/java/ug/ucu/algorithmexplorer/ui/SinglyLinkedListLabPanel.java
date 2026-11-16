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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ug.ucu.algorithmexplorer.controllers.DataStructuresController;

/**
 * Interactive interface for the custom singly linked list.
 *
 * The panel delegates operations to DataStructuresController and
 * redraws the linked structure after each change.
 */
public class SinglyLinkedListLabPanel extends JPanel {

    private static final Color TITLE_COLOR =
            new Color(30, 74, 120);

    private static final Color SUCCESS_COLOR =
            new Color(25, 115, 60);

    private static final Color ERROR_COLOR =
            new Color(180, 35, 35);

    private static final Color SEARCH_COLOR =
            new Color(155, 90, 20);

    private final DataStructuresController controller;

    private JTextField valueField;
    private JTextField targetField;

    private JLabel informationLabel;
    private JLabel statusLabel;
    private JTextArea forwardDisplayArea;

    private SinglyListDrawingPanel drawingPanel;

    public SinglyLinkedListLabPanel(
            DataStructuresController controller
    ) {
        if (controller == null) {
            throw new IllegalArgumentException(
                    "Data-structures controller cannot be null."
            );
        }

        this.controller = controller;

        setLayout(new BorderLayout(12, 12));
        setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        buildInterface();
        refreshListView();
    }

    private void buildInterface() {
        add(createControlsPanel(), BorderLayout.WEST);

        drawingPanel = new SinglyListDrawingPanel();

        JScrollPane drawingScrollPane =
                new JScrollPane(drawingPanel);

        drawingScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Singly Linked List Visualisation"
                )
        );

        drawingScrollPane.setPreferredSize(
                new Dimension(800, 430)
        );

        drawingScrollPane.getHorizontalScrollBar()
                .setUnitIncrement(20);

        add(drawingScrollPane, BorderLayout.CENTER);
        add(createInformationPanel(), BorderLayout.SOUTH);
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setPreferredSize(new Dimension(360, 470));
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Singly Linked List Operations"
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets = new Insets(5, 6, 5, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Value:"), constraints);

        valueField = new JTextField(10);

        constraints.gridx = 1;
        panel.add(valueField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Target:"), constraints);

        targetField = new JTextField(10);

        constraints.gridx = 1;
        panel.add(targetField, constraints);

        JPanel insertionPanel = new JPanel(
                new GridLayout(0, 2, 6, 6)
        );

        insertionPanel.setBorder(
                BorderFactory.createTitledBorder("Insertion")
        );

        insertionPanel.add(
                createOperationButton(
                        "Insert Beginning",
                        this::insertAtBeginning
                )
        );

        insertionPanel.add(
                createOperationButton(
                        "Insert End",
                        this::insertAtEnd
                )
        );

        insertionPanel.add(
                createOperationButton(
                        "Insert Before Target",
                        this::insertBefore
                )
        );

        insertionPanel.add(
                createOperationButton(
                        "Insert After Target",
                        this::insertAfter
                )
        );

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(insertionPanel, constraints);

        JPanel deletionPanel = new JPanel(
                new GridLayout(0, 2, 6, 6)
        );

        deletionPanel.setBorder(
                BorderFactory.createTitledBorder("Deletion")
        );

        deletionPanel.add(
                createOperationButton(
                        "Delete Beginning",
                        this::deleteAtBeginning
                )
        );

        deletionPanel.add(
                createOperationButton(
                        "Delete End",
                        this::deleteAtEnd
                )
        );

        deletionPanel.add(
                createOperationButton(
                        "Delete Before Target",
                        this::deleteBefore
                )
        );

        deletionPanel.add(
                createOperationButton(
                        "Delete After Target",
                        this::deleteAfter
                )
        );

        deletionPanel.add(
                createOperationButton(
                        "Delete Value",
                        this::deleteValue
                )
        );

        constraints.gridy = 3;
        panel.add(deletionPanel, constraints);

        JPanel queryPanel = new JPanel(
                new GridLayout(0, 2, 6, 6)
        );

        queryPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Search and Display"
                )
        );

        queryPanel.add(
                createOperationButton(
                        "Search Value",
                        this::searchValue
                )
        );

        queryPanel.add(
                createOperationButton(
                        "Size",
                        this::showSize
                )
        );

        queryPanel.add(
                createOperationButton(
                        "Display Forward",
                        this::showForwardDisplay
                )
        );

        queryPanel.add(
                createOperationButton(
                        "Clear",
                        this::clearList
                )
        );

        constraints.gridy = 4;
        panel.add(queryPanel, constraints);

        JTextArea explanationArea = new JTextArea(
                "For before/after operations, enter the new or "
                        + "deletion value in Value and the existing "
                        + "node to locate in Target. Operations use "
                        + "the first occurrence of a duplicate target.",
                4,
                25
        );

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setOpaque(false);
        explanationArea.setBorder(
                BorderFactory.createEmptyBorder(8, 4, 4, 4)
        );

        constraints.gridy = 5;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        panel.add(explanationArea, constraints);

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

    private JPanel createInformationPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "List State and Messages"
                )
        );

        informationLabel = new JLabel(
                "Size: 0 | Empty: true",
                SwingConstants.CENTER
        );

        statusLabel = new JLabel(
                "The singly linked list is empty.",
                SwingConstants.CENTER
        );

        statusLabel.setForeground(TITLE_COLOR);
        statusLabel.setFont(
                statusLabel.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        forwardDisplayArea = new JTextArea(
                "HEAD -> null",
                2,
                50
        );

        forwardDisplayArea.setEditable(false);
        forwardDisplayArea.setLineWrap(true);
        forwardDisplayArea.setWrapStyleWord(true);
        forwardDisplayArea.setBackground(
                new Color(246, 248, 250)
        );

        forwardDisplayArea.setBorder(
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        );

        panel.add(informationLabel, BorderLayout.NORTH);
        panel.add(forwardDisplayArea, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        return panel;
    }

    private void insertAtBeginning() {
        Integer value = readValue();

        if (value == null) {
            return;
        }

        runOperation(
                () -> controller.insertSinglyAtBeginning(value),
                "Inserted " + value + " at the beginning."
        );
    }

    private void insertAtEnd() {
        Integer value = readValue();

        if (value == null) {
            return;
        }

        runOperation(
                () -> controller.insertSinglyAtEnd(value),
                "Inserted " + value + " at the end."
        );
    }

    private void insertBefore() {
        Integer value = readValue();
        Integer target = readTarget();

        if (value == null || target == null) {
            return;
        }

        runOperation(
                () -> controller.insertSinglyBefore(
                        target,
                        value
                ),
                "Inserted "
                        + value
                        + " before the first occurrence of "
                        + target
                        + "."
        );
    }

    private void insertAfter() {
        Integer value = readValue();
        Integer target = readTarget();

        if (value == null || target == null) {
            return;
        }

        runOperation(
                () -> controller.insertSinglyAfter(
                        target,
                        value
                ),
                "Inserted "
                        + value
                        + " after the first occurrence of "
                        + target
                        + "."
        );
    }

    private void deleteAtBeginning() {
        try {
            int removed =
                    controller.deleteSinglyAtBeginning();

            operationSucceeded(
                    "Deleted "
                            + removed
                            + " from the beginning."
            );
        } catch (RuntimeException exception) {
            showOperationError(exception);
        }
    }

    private void deleteAtEnd() {
        try {
            int removed = controller.deleteSinglyAtEnd();

            operationSucceeded(
                    "Deleted "
                            + removed
                            + " from the end."
            );
        } catch (RuntimeException exception) {
            showOperationError(exception);
        }
    }

    private void deleteBefore() {
        Integer target = readTarget();

        if (target == null) {
            return;
        }

        try {
            int removed =
                    controller.deleteSinglyBefore(target);

            operationSucceeded(
                    "Deleted "
                            + removed
                            + " before the first occurrence of "
                            + target
                            + "."
            );
        } catch (RuntimeException exception) {
            showOperationError(exception);
        }
    }

    private void deleteAfter() {
        Integer target = readTarget();

        if (target == null) {
            return;
        }

        try {
            int removed =
                    controller.deleteSinglyAfter(target);

            operationSucceeded(
                    "Deleted "
                            + removed
                            + " after the first occurrence of "
                            + target
                            + "."
            );
        } catch (RuntimeException exception) {
            showOperationError(exception);
        }
    }

    private void deleteValue() {
        Integer value = readValue();

        if (value == null) {
            return;
        }

        try {
            int removed =
                    controller.deleteSinglyValue(value);

            operationSucceeded(
                    "Deleted the first occurrence of "
                            + removed
                            + "."
            );
        } catch (RuntimeException exception) {
            showOperationError(exception);
        }
    }

    private void searchValue() {
        Integer value = readValue();

        if (value == null) {
            return;
        }

        int index = controller.searchSingly(value);

        drawingPanel.setHighlightedIndex(index);

        if (index >= 0) {
            showStatus(
                    "Found "
                            + value
                            + " at zero-based index "
                            + index
                            + ".",
                    SEARCH_COLOR
            );
        } else {
            showStatus(
                    "Value " + value + " was not found.",
                    ERROR_COLOR
            );
        }

        refreshListInformation();
    }

    private void showSize() {
        showStatus(
                "List size: "
                        + controller.singlyListSize()
                        + ".",
                TITLE_COLOR
        );

        refreshListInformation();
    }

    private void showForwardDisplay() {
        showStatus(
                "Displayed the list from head to tail.",
                TITLE_COLOR
        );

        refreshListInformation();
    }

    private void clearList() {
        controller.clearSinglyList();

        showStatus(
                "The singly linked list was cleared.",
                SUCCESS_COLOR
        );

        clearInputFields();
        refreshListView();
    }

    private void runOperation(
            Runnable operation,
            String successMessage
    ) {
        try {
            operation.run();
            operationSucceeded(successMessage);
        } catch (RuntimeException exception) {
            showOperationError(exception);
        }
    }

    private void operationSucceeded(String message) {
        showStatus(message, SUCCESS_COLOR);
        clearInputFields();
        refreshListView();
        valueField.requestFocusInWindow();
    }

    private Integer readValue() {
        return readInteger(
                valueField,
                "Enter an integer in the Value field."
        );
    }

    private Integer readTarget() {
        return readInteger(
                targetField,
                "Enter an integer in the Target field."
        );
    }

    private Integer readInteger(
            JTextField field,
            String emptyMessage
    ) {
        String text = field.getText().trim();

        if (text.isEmpty()) {
            showStatus(emptyMessage, ERROR_COLOR);
            field.requestFocusInWindow();
            return null;
        }

        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException exception) {
            showStatus(
                    "Enter a valid integer value.",
                    ERROR_COLOR
            );

            field.requestFocusInWindow();
            return null;
        }
    }

    private void clearInputFields() {
        valueField.setText("");
        targetField.setText("");
    }

    private void refreshListView() {
        drawingPanel.updateList(
                controller.singlyListSnapshot()
        );

        refreshListInformation();
    }

    private void refreshListInformation() {
        informationLabel.setText(
                "Size: "
                        + controller.singlyListSize()
                        + " | Empty: "
                        + controller.isSinglyListEmpty()
        );

        forwardDisplayArea.setText(
                controller.displaySinglyForward()
        );
    }

    private void showOperationError(
            RuntimeException exception
    ) {
        String message = exception.getMessage();

        showStatus(message, ERROR_COLOR);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Singly Linked List Operation",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showStatus(
            String message,
            Color color
    ) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    /**
     * Draws nodes and one-directional next-reference arrows.
     */
    private static final class SinglyListDrawingPanel
            extends JPanel {

        private Object[] values = new Object[0];
        private int highlightedIndex = -1;

        SinglyListDrawingPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(760, 320));
        }

        void updateList(Object[] values) {
            this.values = values.clone();
            highlightedIndex = -1;

            updatePreferredWidth();
            revalidate();
            repaint();
        }

        void setHighlightedIndex(int highlightedIndex) {
            this.highlightedIndex = highlightedIndex;
            repaint();
        }

        private void updatePreferredWidth() {
            int requiredWidth = Math.max(
                    760,
                    100 + values.length * 155
            );

            setPreferredSize(
                    new Dimension(requiredWidth, 320)
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

                if (values.length == 0) {
                    drawEmptyList(graphics2D);
                    return;
                }

                drawList(graphics2D);

            } finally {
                graphics2D.dispose();
            }
        }

        private void drawList(Graphics2D graphics2D) {
            int nodeWidth = 100;
            int nodeHeight = 64;
            int gap = 55;
            int startX = 60;
            int nodeY = 125;

            drawLabel(
                    graphics2D,
                    "HEAD",
                    startX,
                    nodeY - 35,
                    nodeWidth,
                    TITLE_COLOR
            );

            for (int index = 0;
                    index < values.length;
                    index++) {

                int x = startX
                        + index * (nodeWidth + gap);

                drawNode(
                        graphics2D,
                        x,
                        nodeY,
                        nodeWidth,
                        nodeHeight,
                        values[index],
                        index
                );

                if (index < values.length - 1) {
                    drawNextArrow(
                            graphics2D,
                            x + nodeWidth,
                            nodeY + nodeHeight / 2,
                            x + nodeWidth + gap
                    );
                }
            }

            int nullX = startX
                    + values.length * (nodeWidth + gap)
                    - gap
                    + 20;

            graphics2D.setColor(new Color(70, 70, 70));
            graphics2D.drawString(
                    "null",
                    nullX,
                    nodeY + nodeHeight / 2 + 5
            );

            int finalNodeX = startX
                    + (values.length - 1)
                    * (nodeWidth + gap);

            drawNextArrow(
                    graphics2D,
                    finalNodeX + nodeWidth,
                    nodeY + nodeHeight / 2,
                    nullX - 8
            );

            drawLabel(
                    graphics2D,
                    "TAIL",
                    finalNodeX,
                    nodeY + nodeHeight + 48,
                    nodeWidth,
                    new Color(150, 70, 70)
            );
        }

        private void drawNode(
                Graphics2D graphics2D,
                int x,
                int y,
                int width,
                int height,
                Object value,
                int index
        ) {
            boolean highlighted =
                    index == highlightedIndex;

            graphics2D.setColor(
                    highlighted
                            ? new Color(255, 220, 145)
                            : new Color(175, 215, 250)
            );

            graphics2D.fillRoundRect(
                    x,
                    y,
                    width,
                    height,
                    12,
                    12
            );

            graphics2D.setColor(new Color(50, 65, 80));
            graphics2D.setStroke(new BasicStroke(1.7f));

            graphics2D.drawRoundRect(
                    x,
                    y,
                    width,
                    height,
                    12,
                    12
            );

            int dividerX = x + width - 28;

            graphics2D.drawLine(
                    dividerX,
                    y,
                    dividerX,
                    y + height
            );

            String valueText = String.valueOf(value);
            int valueAreaWidth = width - 28;

            int valueTextWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(valueText);

            graphics2D.drawString(
                    valueText,
                    x + (valueAreaWidth - valueTextWidth) / 2,
                    y + height / 2 + 5
            );

            graphics2D.fillOval(
                    dividerX + 10,
                    y + height / 2 - 3,
                    6,
                    6
            );

            String indexText = "index " + index;

            graphics2D.drawString(
                    indexText,
                    x + 24,
                    y + height + 20
            );

            if (highlighted) {
                drawLabel(
                        graphics2D,
                        "FOUND",
                        x,
                        y - 16,
                        width,
                        SEARCH_COLOR
                );
            }
        }

        private void drawNextArrow(
                Graphics2D graphics2D,
                int startX,
                int y,
                int endX
        ) {
            graphics2D.setColor(TITLE_COLOR);
            graphics2D.setStroke(new BasicStroke(2f));

            graphics2D.drawLine(
                    startX,
                    y,
                    endX,
                    y
            );

            graphics2D.drawLine(
                    endX,
                    y,
                    endX - 8,
                    y - 6
            );

            graphics2D.drawLine(
                    endX,
                    y,
                    endX - 8,
                    y + 6
            );
        }

        private void drawLabel(
                Graphics2D graphics2D,
                String text,
                int x,
                int y,
                int width,
                Color color
        ) {
            graphics2D.setColor(color);
            graphics2D.setFont(
                    graphics2D.getFont().deriveFont(Font.BOLD)
            );

            int textWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(text);

            graphics2D.drawString(
                    text,
                    x + (width - textWidth) / 2,
                    y
            );

            graphics2D.setFont(
                    graphics2D.getFont().deriveFont(Font.PLAIN)
            );
        }

        private void drawEmptyList(
                Graphics2D graphics2D
        ) {
            String message =
                    "HEAD -> null   (the list is empty)";

            int textWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(message);

            graphics2D.setColor(new Color(95, 95, 95));

            graphics2D.drawString(
                    message,
                    Math.max(10, (getWidth() - textWidth) / 2),
                    getHeight() / 2
            );
        }
    }
}
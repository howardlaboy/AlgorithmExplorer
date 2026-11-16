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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import ug.ucu.algorithmexplorer.controllers.DataStructuresController;

/**
 * Interactive interface for the custom doubly linked list.
 *
 * The panel displays both previous and next references and supports
 * forward and backward traversal.
 */
public class DoublyLinkedListLabPanel extends JPanel {

    private static final Color TITLE_COLOR =
            new Color(30, 74, 120);

    private static final Color SUCCESS_COLOR =
            new Color(25, 115, 60);

    private static final Color ERROR_COLOR =
            new Color(180, 35, 35);

    private static final Color SEARCH_COLOR =
            new Color(155, 90, 20);

    private static final Color NEXT_COLOR =
            new Color(30, 100, 175);

    private static final Color PREVIOUS_COLOR =
            new Color(165, 70, 125);

    private final DataStructuresController controller;

    private JTextField valueField;
    private JTextField targetField;

    private JLabel informationLabel;
    private JLabel consistencyLabel;
    private JLabel statusLabel;

    private JTextArea forwardDisplayArea;
    private JTextArea backwardDisplayArea;

    private DoublyListDrawingPanel drawingPanel;

    public DoublyLinkedListLabPanel(
            DataStructuresController controller
    ) {
        if (controller == null) {
            throw new IllegalArgumentException(
                    "Data-structures controller cannot be null."
            );
        }

        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        buildInterface();
        refreshListView();
    }

    private void buildInterface() {
        JPanel controlsPanel = createControlsPanel();

        JScrollPane controlsScrollPane =
                new JScrollPane(controlsPanel);

        controlsScrollPane.setPreferredSize(
                new Dimension(390, 410)
        );

        controlsScrollPane.setMinimumSize(
                new Dimension(330, 280)
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

        drawingPanel = new DoublyListDrawingPanel();

        JScrollPane drawingScrollPane =
                new JScrollPane(drawingPanel);

        drawingScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Doubly Linked List Visualisation"
                )
        );

        drawingScrollPane.setPreferredSize(
                new Dimension(740, 390)
        );

        drawingScrollPane.setMinimumSize(
                new Dimension(400, 280)
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

        add(controlsScrollPane, BorderLayout.WEST);
        add(drawingScrollPane, BorderLayout.CENTER);
        add(createInformationPanel(), BorderLayout.SOUTH);
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setPreferredSize(new Dimension(360, 570));

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Doubly Linked List Operations"
                        ),
                        BorderFactory.createEmptyBorder(
                                14,
                                8,
                                12,
                                8
                        )
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets = new Insets(5, 6, 5, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;

        panel.add(new JLabel("Value:"), constraints);

        valueField = new JTextField(12);
        valueField.setToolTipText(
                "Enter the value to insert, delete or search for."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(valueField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;

        panel.add(new JLabel("Target:"), constraints);

        targetField = new JTextField(12);
        targetField.setToolTipText(
                "Enter the existing node used by before/after operations."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(targetField, constraints);

        panel.add(
                createInsertionPanel(),
                createSectionConstraints(2)
        );

        panel.add(
                createDeletionPanel(),
                createSectionConstraints(3)
        );

        panel.add(
                createQueryPanel(),
                createSectionConstraints(4)
        );

        JTextArea explanationArea = new JTextArea(
                "Each node stores explicit previous and next "
                        + "references. For before or after operations, "
                        + "enter the new value in Value and the existing "
                        + "node in Target. Duplicate-value operations "
                        + "use the first matching target.",
                4,
                24
        );

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setOpaque(false);

        explanationArea.setBorder(
                BorderFactory.createEmptyBorder(6, 4, 4, 4)
        );

        constraints = createSectionConstraints(5);
        constraints.weighty = 0.0;
        constraints.anchor = GridBagConstraints.NORTH;

        panel.add(explanationArea, constraints);

        GridBagConstraints filler =
                new GridBagConstraints();

        filler.gridx = 0;
        filler.gridy = 6;
        filler.gridwidth = 2;
        filler.weightx = 1.0;
        filler.weighty = 1.0;
        filler.fill = GridBagConstraints.VERTICAL;

        panel.add(new JPanel(), filler);

        return panel;
    }

    private GridBagConstraints createSectionConstraints(
            int row
    ) {
        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 6, 5, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;

        return constraints;
    }

    private JPanel createInsertionPanel() {
        JPanel panel = new JPanel(
                new GridLayout(0, 2, 6, 6)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder("Insertion")
        );

        panel.add(
                createOperationButton(
                        "Insert Beginning",
                        this::insertAtBeginning
                )
        );

        panel.add(
                createOperationButton(
                        "Insert End",
                        this::insertAtEnd
                )
        );

        panel.add(
                createOperationButton(
                        "Insert Before Target",
                        this::insertBefore
                )
        );

        panel.add(
                createOperationButton(
                        "Insert After Target",
                        this::insertAfter
                )
        );

        return panel;
    }

    private JPanel createDeletionPanel() {
        JPanel panel = new JPanel(
                new GridLayout(0, 2, 6, 6)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder("Deletion")
        );

        panel.add(
                createOperationButton(
                        "Delete Beginning",
                        this::deleteAtBeginning
                )
        );

        panel.add(
                createOperationButton(
                        "Delete End",
                        this::deleteAtEnd
                )
        );

        panel.add(
                createOperationButton(
                        "Delete Before Target",
                        this::deleteBefore
                )
        );

        panel.add(
                createOperationButton(
                        "Delete After Target",
                        this::deleteAfter
                )
        );

        panel.add(
                createOperationButton(
                        "Delete Value",
                        this::deleteValue
                )
        );

        return panel;
    }

    private JPanel createQueryPanel() {
        JPanel panel = new JPanel(
                new GridLayout(0, 2, 6, 6)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Search and Display"
                )
        );

        panel.add(
                createOperationButton(
                        "Search Value",
                        this::searchValue
                )
        );

        panel.add(
                createOperationButton(
                        "Size",
                        this::showSize
                )
        );

        panel.add(
                createOperationButton(
                        "Display Forward",
                        this::showForwardDisplay
                )
        );

        panel.add(
                createOperationButton(
                        "Display Backward",
                        this::showBackwardDisplay
                )
        );

        panel.add(
                createOperationButton(
                        "Clear",
                        this::clearList
                )
        );

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
        JPanel panel = new JPanel(new BorderLayout(4, 4));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "List State and Messages"
                )
        );

        JPanel statePanel = new JPanel(
                new GridLayout(1, 2, 10, 2)
        );

        informationLabel = new JLabel(
                "Size: 0 | Empty: true",
                SwingConstants.CENTER
        );

        consistencyLabel = new JLabel(
                "Previous/next links consistent: true",
                SwingConstants.CENTER
        );

        consistencyLabel.setForeground(SUCCESS_COLOR);

        statePanel.add(informationLabel);
        statePanel.add(consistencyLabel);

        JPanel displaysPanel = new JPanel(
                new GridLayout(2, 1, 3, 3)
        );

        forwardDisplayArea = createDisplayArea(
                "HEAD -> null"
        );

        backwardDisplayArea = createDisplayArea(
                "TAIL -> null"
        );

        displaysPanel.add(forwardDisplayArea);
        displaysPanel.add(backwardDisplayArea);

        statusLabel = new JLabel(
                "The doubly linked list is empty.",
                SwingConstants.CENTER
        );

        statusLabel.setForeground(TITLE_COLOR);

        statusLabel.setFont(
                statusLabel.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        panel.add(statePanel, BorderLayout.NORTH);
        panel.add(displaysPanel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JTextArea createDisplayArea(String initialText) {
        JTextArea area = new JTextArea(
                initialText,
                1,
                50
        );

        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(246, 248, 250));

        area.setBorder(
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        );

        return area;
    }

    private void insertAtBeginning() {
        Integer value = readValue();

        if (value == null) {
            return;
        }

        runOperation(
                () -> controller.insertDoublyAtBeginning(value),
                "Inserted " + value + " at the beginning."
        );
    }

    private void insertAtEnd() {
        Integer value = readValue();

        if (value == null) {
            return;
        }

        runOperation(
                () -> controller.insertDoublyAtEnd(value),
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
                () -> controller.insertDoublyBefore(
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
                () -> controller.insertDoublyAfter(
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
                    controller.deleteDoublyAtBeginning();

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
            int removed =
                    controller.deleteDoublyAtEnd();

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
                    controller.deleteDoublyBefore(target);

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
                    controller.deleteDoublyAfter(target);

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
                    controller.deleteDoublyValue(value);

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

        int index = controller.searchDoubly(value);

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
                        + controller.doublyListSize()
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

    private void showBackwardDisplay() {
        showStatus(
                "Displayed the list from tail to head.",
                TITLE_COLOR
        );

        refreshListInformation();
    }

    private void clearList() {
        controller.clearDoublyList();

        showStatus(
                "The doubly linked list was cleared.",
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
                controller.doublyListForwardSnapshot()
        );

        refreshListInformation();
    }

    private void refreshListInformation() {
        informationLabel.setText(
                "Size: "
                        + controller.doublyListSize()
                        + " | Empty: "
                        + controller.isDoublyListEmpty()
        );

        boolean consistent =
                controller.hasConsistentDoublyLinks();

        consistencyLabel.setText(
                "Previous/next links consistent: " + consistent
        );

        consistencyLabel.setForeground(
                consistent ? SUCCESS_COLOR : ERROR_COLOR
        );

        forwardDisplayArea.setText(
                controller.displayDoublyForward()
        );

        backwardDisplayArea.setText(
                controller.displayDoublyBackward()
        );
    }

    private void showOperationError(
            RuntimeException exception
    ) {
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            message = "The linked-list operation could not be completed.";
        }

        showStatus(message, ERROR_COLOR);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Doubly Linked List Operation",
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
     * Draws nodes with separate previous and next references.
     */
    private static final class DoublyListDrawingPanel
            extends JPanel {

        private Object[] values = new Object[0];
        private int highlightedIndex = -1;

        DoublyListDrawingPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(740, 300));
            setMinimumSize(new Dimension(400, 260));
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
                    740,
                    150 + values.length * 190
            );

            setPreferredSize(
                    new Dimension(requiredWidth, 300)
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
            int nodeWidth = 130;
            int nodeHeight = 72;
            int gap = 60;
            int startX = 65;
            int nodeY = 105;

            drawLabel(
                    graphics2D,
                    "HEAD",
                    startX,
                    nodeY - 40,
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
                    drawTwoWayArrows(
                            graphics2D,
                            x + nodeWidth,
                            x + nodeWidth + gap,
                            nodeY,
                            nodeHeight
                    );
                }
            }

            int finalNodeX = startX
                    + (values.length - 1)
                    * (nodeWidth + gap);

            drawLabel(
                    graphics2D,
                    "TAIL",
                    finalNodeX,
                    nodeY + nodeHeight + 55,
                    nodeWidth,
                    new Color(150, 70, 70)
            );

            drawNullLabels(
                    graphics2D,
                    startX,
                    finalNodeX,
                    nodeY,
                    nodeWidth,
                    nodeHeight
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
                            : new Color(185, 220, 250)
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

            int previousDivider = x + 28;
            int nextDivider = x + width - 28;

            graphics2D.drawLine(
                    previousDivider,
                    y,
                    previousDivider,
                    y + height
            );

            graphics2D.drawLine(
                    nextDivider,
                    y,
                    nextDivider,
                    y + height
            );

            graphics2D.fillOval(
                    x + 10,
                    y + height / 2 - 3,
                    6,
                    6
            );

            graphics2D.fillOval(
                    nextDivider + 10,
                    y + height / 2 - 3,
                    6,
                    6
            );

            String valueText = String.valueOf(value);

            int valueAreaWidth =
                    nextDivider - previousDivider;

            int textWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(valueText);

            graphics2D.drawString(
                    valueText,
                    previousDivider
                            + (valueAreaWidth - textWidth) / 2,
                    y + height / 2 + 5
            );

            String indexText = "index " + index;

            int indexWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(indexText);

            graphics2D.drawString(
                    indexText,
                    x + (width - indexWidth) / 2,
                    y + height + 22
            );

            Font originalFont = graphics2D.getFont();

            graphics2D.setFont(
                    originalFont.deriveFont(Font.PLAIN, 10f)
            );

            graphics2D.drawString(
                    "prev",
                    x + 4,
                    y + 15
            );

            graphics2D.drawString(
                    "next",
                    nextDivider + 2,
                    y + 15
            );

            graphics2D.setFont(originalFont);

            if (highlighted) {
                drawLabel(
                        graphics2D,
                        "FOUND",
                        x,
                        y - 17,
                        width,
                        SEARCH_COLOR
                );
            }
        }

        private void drawTwoWayArrows(
                Graphics2D graphics2D,
                int startX,
                int endX,
                int nodeY,
                int nodeHeight
        ) {
            int nextY = nodeY + nodeHeight / 2 - 9;
            int previousY = nodeY + nodeHeight / 2 + 9;

            graphics2D.setStroke(new BasicStroke(2f));

            graphics2D.setColor(NEXT_COLOR);

            graphics2D.drawLine(
                    startX,
                    nextY,
                    endX,
                    nextY
            );

            drawRightArrowHead(
                    graphics2D,
                    endX,
                    nextY
            );

            graphics2D.setColor(PREVIOUS_COLOR);

            graphics2D.drawLine(
                    endX,
                    previousY,
                    startX,
                    previousY
            );

            drawLeftArrowHead(
                    graphics2D,
                    startX,
                    previousY
            );
        }

        private void drawRightArrowHead(
                Graphics2D graphics2D,
                int x,
                int y
        ) {
            graphics2D.drawLine(
                    x,
                    y,
                    x - 8,
                    y - 5
            );

            graphics2D.drawLine(
                    x,
                    y,
                    x - 8,
                    y + 5
            );
        }

        private void drawLeftArrowHead(
                Graphics2D graphics2D,
                int x,
                int y
        ) {
            graphics2D.drawLine(
                    x,
                    y,
                    x + 8,
                    y - 5
            );

            graphics2D.drawLine(
                    x,
                    y,
                    x + 8,
                    y + 5
            );
        }

        private void drawNullLabels(
                Graphics2D graphics2D,
                int firstNodeX,
                int finalNodeX,
                int nodeY,
                int nodeWidth,
                int nodeHeight
        ) {
            graphics2D.setColor(PREVIOUS_COLOR);

            graphics2D.drawString(
                    "null <-",
                    firstNodeX - 48,
                    nodeY + nodeHeight / 2 + 14
            );

            graphics2D.setColor(NEXT_COLOR);

            graphics2D.drawString(
                    "-> null",
                    finalNodeX + nodeWidth + 8,
                    nodeY + nodeHeight / 2 - 5
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
            Font originalFont = graphics2D.getFont();

            graphics2D.setColor(color);
            graphics2D.setFont(
                    originalFont.deriveFont(Font.BOLD)
            );

            int textWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(text);

            graphics2D.drawString(
                    text,
                    x + (width - textWidth) / 2,
                    y
            );

            graphics2D.setFont(originalFont);
        }

        private void drawEmptyList(
                Graphics2D graphics2D
        ) {
            String message =
                    "null <- HEAD / TAIL -> null"
                            + "   (the list is empty)";

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
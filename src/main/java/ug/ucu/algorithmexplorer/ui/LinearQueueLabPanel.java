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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import ug.ucu.algorithmexplorer.controllers.DataStructuresController;

/**
 * Interactive interface for the fixed-capacity linear queue.
 *
 * The physical array is displayed so that unused positions and the
 * false-full condition remain visible.
 *
 * Recreating the queue creates a new empty queue with the currently
 * selected capacity.
 */
public class LinearQueueLabPanel extends JPanel {

    private static final Color TITLE_COLOR =
            new Color(30, 74, 120);

    private static final Color SUCCESS_COLOR =
            new Color(25, 115, 60);

    private static final Color ERROR_COLOR =
            new Color(180, 35, 35);

    private static final Color WARNING_COLOR =
            new Color(160, 90, 10);

    private static final Color FRONT_COLOR =
            new Color(25, 125, 65);

    private static final Color REAR_COLOR =
            new Color(175, 60, 60);

    private final DataStructuresController controller;

    private JSpinner capacitySpinner;
    private JButton createButton;

    private JTextField valueField;
    private JButton enqueueButton;
    private JButton dequeueButton;
    private JButton frontButton;
    private JButton sizeButton;
    private JButton emptyButton;
    private JButton fullButton;
    private JButton displayButton;
    private JButton clearButton;

    private LinearQueueDrawingPanel drawingPanel;

    private JLabel informationLabel;
    private JLabel falseFullLabel;
    private JLabel statusLabel;

    public LinearQueueLabPanel(
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
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        buildInterface();
        setOperationsEnabled(false);
    }

    private void buildInterface() {
        add(
                createLeftControlColumn(),
                BorderLayout.WEST
        );

        drawingPanel = new LinearQueueDrawingPanel();

        JScrollPane drawingScrollPane =
                new JScrollPane(drawingPanel);

        drawingScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Linear Queue Physical Array"
                )
        );

        drawingScrollPane.setPreferredSize(
                new Dimension(760, 430)
        );

        drawingScrollPane.setMinimumSize(
                new Dimension(420, 290)
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

        add(drawingScrollPane, BorderLayout.CENTER);
        add(createInformationPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the complete left section.
     *
     * The Capacity section remains fixed at the top. Only the queue
     * operations and explanatory text below it are scrollable.
     */
    private JPanel createLeftControlColumn() {
        JPanel leftColumn = new JPanel(
                new BorderLayout(0, 8)
        );

        leftColumn.setPreferredSize(
                new Dimension(390, 430)
        );

        leftColumn.setMinimumSize(
                new Dimension(320, 290)
        );

        leftColumn.setBorder(
                BorderFactory.createTitledBorder(
                        "Linear Queue Operations"
                )
        );

        leftColumn.add(
                createFixedCapacityPanel(),
                BorderLayout.NORTH
        );

        JPanel scrollableContent =
                createScrollableControlsPanel();

        JScrollPane controlsScrollPane =
                new JScrollPane(scrollableContent);

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

        leftColumn.add(
                controlsScrollPane,
                BorderLayout.CENTER
        );

        return leftColumn;
    }

    /**
     * Creates the fixed Capacity section.
     *
     * This panel is outside the scroll pane, so it cannot disappear
     * when the operation section is scrolled.
     */
    private JPanel createFixedCapacityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Queue Capacity"
                        ),
                        BorderFactory.createEmptyBorder(
                                6,
                                8,
                                8,
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
                "Choose a queue capacity from 1 to 20."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(capacitySpinner, constraints);

        createButton = new JButton("Create Queue");

        createButton.addActionListener(
                event -> createQueue()
        );

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;

        panel.add(createButton, constraints);

        return panel;
    }

    /**
     * Creates the scrollable part of the left control section.
     */
    private JPanel createScrollableControlsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setPreferredSize(
                new Dimension(350, 500)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        4,
                        8,
                        12,
                        8
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;

        panel.add(new JLabel("Value:"), constraints);

        valueField = new JTextField(12);

        valueField.setToolTipText(
                "Enter an integer to enqueue."
        );

        constraints.gridx = 1;
        constraints.weightx = 1.0;

        panel.add(valueField, constraints);

        JPanel operationPanel =
                createQueueOperationPanel();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;

        panel.add(operationPanel, constraints);

        JTextArea explanationArea = new JTextArea(
                "A linear queue moves the front and rear only "
                        + "toward the end of the array. Positions "
                        + "freed before the front cannot be reused. "
                        + "When the rear reaches the final index, "
                        + "the queue reports full even when earlier "
                        + "positions are empty. This is the "
                        + "false-full problem.",
                6,
                23
        );

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setOpaque(false);

        explanationArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "How the Linear Queue Works"
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                6,
                                5,
                                6
                        )
                )
        );

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.anchor = GridBagConstraints.NORTH;

        panel.add(explanationArea, constraints);

        JTextArea recreationArea = new JTextArea(
                "The capacity remains editable after creation. "
                        + "Choose a different capacity and select "
                        + "Recreate Queue to create a new empty "
                        + "linear queue.",
                3,
                23
        );

        recreationArea.setEditable(false);
        recreationArea.setLineWrap(true);
        recreationArea.setWrapStyleWord(true);
        recreationArea.setOpaque(false);

        recreationArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Changing Capacity"
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                6,
                                5,
                                6
                        )
                )
        );

        constraints.gridy = 3;

        panel.add(recreationArea, constraints);

        GridBagConstraints filler =
                new GridBagConstraints();

        filler.gridx = 0;
        filler.gridy = 4;
        filler.gridwidth = 2;
        filler.weightx = 1.0;
        filler.weighty = 1.0;
        filler.fill = GridBagConstraints.VERTICAL;

        panel.add(new JPanel(), filler);

        return panel;
    }

    private JPanel createQueueOperationPanel() {
        JPanel panel = new JPanel(
                new GridLayout(0, 2, 7, 7)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Queue Operations"
                )
        );

        enqueueButton = createOperationButton(
                "Enqueue",
                this::enqueueValue
        );

        dequeueButton = createOperationButton(
                "Dequeue",
                this::dequeueValue
        );

        frontButton = createOperationButton(
                "Front",
                this::showFront
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
                this::clearQueue
        );

        panel.add(enqueueButton);
        panel.add(dequeueButton);
        panel.add(frontButton);
        panel.add(sizeButton);
        panel.add(emptyButton);
        panel.add(fullButton);
        panel.add(displayButton);
        panel.add(clearButton);

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
        JPanel panel = new JPanel(
                new GridLayout(3, 1, 3, 3)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Queue State and Messages"
                )
        );

        informationLabel = new JLabel(
                "Create a linear queue to begin.",
                SwingConstants.CENTER
        );

        falseFullLabel = new JLabel(
                "False-full condition: -",
                SwingConstants.CENTER
        );

        falseFullLabel.setForeground(TITLE_COLOR);

        statusLabel = new JLabel(
                "Choose a capacity and select Create Queue.",
                SwingConstants.CENTER
        );

        statusLabel.setForeground(TITLE_COLOR);

        statusLabel.setFont(
                statusLabel.getFont().deriveFont(
                        Font.BOLD,
                        13f
                )
        );

        panel.add(informationLabel);
        panel.add(falseFullLabel);
        panel.add(statusLabel);

        return panel;
    }

    private void createQueue() {
        int capacity =
                (Integer) capacitySpinner.getValue();

        controller.createLinearQueue(capacity);

        /*
         * Capacity remains enabled so the user can select another
         * capacity and recreate the queue.
         */
        capacitySpinner.setEnabled(true);

        createButton.setText("Recreate Queue");
        setOperationsEnabled(true);

        showStatus(
                "Created an empty linear queue with capacity "
                        + capacity
                        + ".",
                SUCCESS_COLOR
        );

        refreshQueueView();
        valueField.requestFocusInWindow();
    }

    private void enqueueValue() {
        Integer value = readValue();

        if (value == null) {
            return;
        }

        try {
            int insertionIndex =
                    controller.linearRearIndex() + 1;

            controller.enqueueLinear(value);

            showStatus(
                    "Enqueued "
                            + value
                            + " at rear index "
                            + insertionIndex
                            + ".",
                    SUCCESS_COLOR
            );

            valueField.setText("");
            refreshQueueView();
            valueField.requestFocusInWindow();

        } catch (IllegalStateException exception) {
            showOperationError(exception);
            refreshQueueView();
        }
    }

    private void dequeueValue() {
        try {
            int removalIndex =
                    controller.linearFrontIndex();

            int removed =
                    controller.dequeueLinear();

            showStatus(
                    "Dequeued "
                            + removed
                            + " from front index "
                            + removalIndex
                            + ".",
                    SUCCESS_COLOR
            );

            refreshQueueView();

        } catch (IllegalStateException exception) {
            showOperationError(exception);
        }
    }

    private void showFront() {
        try {
            int frontValue =
                    controller.frontLinear();

            showStatus(
                    "Front element: "
                            + frontValue
                            + " at index "
                            + controller.linearFrontIndex()
                            + ". The element was not removed.",
                    TITLE_COLOR
            );

        } catch (IllegalStateException exception) {
            showOperationError(exception);
        }
    }

    private void showSize() {
        showStatus(
                "Queue size: "
                        + controller.linearQueueSize()
                        + ".",
                TITLE_COLOR
        );

        refreshQueueInformation();
    }

    private void showEmpty() {
        showStatus(
                "is_empty = "
                        + controller.isLinearQueueEmpty(),
                TITLE_COLOR
        );

        refreshQueueInformation();
    }

    private void showFull() {
        showStatus(
                "is_full = "
                        + controller.isLinearQueueFull(),
                TITLE_COLOR
        );

        refreshQueueInformation();
    }

    private void showDisplay() {
        showStatus(
                "Front to rear: "
                        + controller.displayLinearQueue(),
                TITLE_COLOR
        );

        refreshQueueInformation();
    }

    private void clearQueue() {
        controller.clearLinearQueue();

        showStatus(
                "The linear queue was cleared. Capacity remains "
                        + controller.linearQueueCapacity()
                        + ", and the indices were reset.",
                SUCCESS_COLOR
        );

        refreshQueueView();
    }

    private Integer readValue() {
        String text = valueField.getText().trim();

        if (text.isEmpty()) {
            showStatus(
                    "Enter an integer value.",
                    ERROR_COLOR
            );

            valueField.requestFocusInWindow();
            return null;
        }

        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException exception) {
            showStatus(
                    "Enter a valid integer value.",
                    ERROR_COLOR
            );

            valueField.requestFocusInWindow();
            return null;
        }
    }

    private void refreshQueueView() {
        drawingPanel.updateQueue(
                controller.linearQueueStorageSnapshot(),
                controller.linearQueueSize(),
                controller.linearFrontIndex(),
                controller.linearRearIndex(),
                controller.hasLinearFalseFullCondition()
        );

        refreshQueueInformation();
    }

    private void refreshQueueInformation() {
        informationLabel.setText(
                "Size: "
                        + controller.linearQueueSize()
                        + " / "
                        + controller.linearQueueCapacity()
                        + " | Front index: "
                        + controller.linearFrontIndex()
                        + " | Rear index: "
                        + controller.linearRearIndex()
                        + " | Empty: "
                        + controller.isLinearQueueEmpty()
                        + " | Full: "
                        + controller.isLinearQueueFull()
        );

        if (controller.hasLinearFalseFullCondition()) {
            falseFullLabel.setText(
                    "FALSE-FULL: "
                            + controller.linearWastedPositions()
                            + " earlier position(s) are empty, "
                            + "but the rear cannot move farther right."
            );

            falseFullLabel.setForeground(WARNING_COLOR);
        } else {
            falseFullLabel.setText(
                    "False-full condition: false | "
                            + "Wasted earlier positions: "
                            + controller.linearWastedPositions()
            );

            falseFullLabel.setForeground(TITLE_COLOR);
        }
    }

    private void setOperationsEnabled(boolean enabled) {
        valueField.setEnabled(enabled);
        enqueueButton.setEnabled(enabled);
        dequeueButton.setEnabled(enabled);
        frontButton.setEnabled(enabled);
        sizeButton.setEnabled(enabled);
        emptyButton.setEnabled(enabled);
        fullButton.setEnabled(enabled);
        displayButton.setEnabled(enabled);
        clearButton.setEnabled(enabled);
    }

    private void showOperationError(Exception exception) {
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            message =
                    "The linear queue operation could not be completed.";
        }

        showStatus(message, ERROR_COLOR);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Linear Queue Operation",
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
     * Draws every physical position in the fixed linear array.
     */
    private static final class LinearQueueDrawingPanel
            extends JPanel {

        private Object[] storage = new Object[0];

        private int elementCount;
        private int frontIndex;
        private int rearIndex = -1;

        private boolean falseFull;

        LinearQueueDrawingPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(800, 390));
            setMinimumSize(new Dimension(420, 290));
        }

        void updateQueue(
                Object[] storage,
                int elementCount,
                int frontIndex,
                int rearIndex,
                boolean falseFull
        ) {
            this.storage = storage.clone();
            this.elementCount = elementCount;
            this.frontIndex = frontIndex;
            this.rearIndex = rearIndex;
            this.falseFull = falseFull;

            updatePreferredWidth();
            revalidate();
            repaint();
        }

        private void updatePreferredWidth() {
            int requiredWidth = Math.max(
                    800,
                    100 + storage.length * 145
            );

            setPreferredSize(
                    new Dimension(requiredWidth, 390)
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

                if (storage.length == 0) {
                    drawInitialMessage(graphics2D);
                    return;
                }

                drawPhysicalArray(graphics2D);

            } finally {
                graphics2D.dispose();
            }
        }

        private void drawPhysicalArray(
                Graphics2D graphics2D
        ) {
            int cellWidth = 120;
            int cellHeight = 88;
            int gap = 12;

            int totalWidth =
                    cellWidth * storage.length
                            + gap * (storage.length - 1);

            int startX = Math.max(
                    50,
                    (getWidth() - totalWidth) / 2
            );

            int cellY = Math.max(
                    125,
                    getHeight() / 2 - cellHeight / 2
            );

            drawDirectionLabel(
                    graphics2D,
                    startX,
                    totalWidth,
                    cellY
            );

            for (int index = 0;
                    index < storage.length;
                    index++) {

                int x =
                        startX + index * (cellWidth + gap);

                drawCell(
                        graphics2D,
                        x,
                        cellY,
                        cellWidth,
                        cellHeight,
                        index
                );
            }
        }

        private void drawCell(
                Graphics2D graphics2D,
                int x,
                int y,
                int width,
                int height,
                int index
        ) {
            boolean occupied =
                    storage[index] != null;

            boolean wasted =
                    index < frontIndex
                            && storage[index] == null;

            if (occupied) {
                graphics2D.setColor(
                        new Color(166, 210, 255)
                );
            } else if (wasted) {
                graphics2D.setColor(
                        new Color(255, 220, 170)
                );
            } else {
                graphics2D.setColor(
                        new Color(240, 243, 247)
                );
            }

            graphics2D.fillRoundRect(
                    x,
                    y,
                    width,
                    height,
                    10,
                    10
            );

            graphics2D.setColor(
                    new Color(55, 70, 85)
            );

            graphics2D.setStroke(
                    new BasicStroke(1.6f)
            );

            graphics2D.drawRoundRect(
                    x,
                    y,
                    width,
                    height,
                    10,
                    10
            );

            String valueText = occupied
                    ? String.valueOf(storage[index])
                    : "empty";

            int valueWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(valueText);

            graphics2D.drawString(
                    valueText,
                    x + (width - valueWidth) / 2,
                    y + 40
            );

            String indexText =
                    "index " + index;

            int indexWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(indexText);

            graphics2D.drawString(
                    indexText,
                    x + (width - indexWidth) / 2,
                    y + height + 24
            );

            if (wasted) {
                String wastedText = "wasted";

                int wastedWidth = graphics2D
                        .getFontMetrics()
                        .stringWidth(wastedText);

                graphics2D.setColor(WARNING_COLOR);

                graphics2D.drawString(
                        wastedText,
                        x + (width - wastedWidth) / 2,
                        y + 65
                );
            }

            if (elementCount > 0
                    && index == frontIndex) {

                drawMarker(
                        graphics2D,
                        "FRONT",
                        x,
                        y - 25,
                        width,
                        FRONT_COLOR
                );
            }

            if (index == rearIndex) {
                drawMarker(
                        graphics2D,
                        "REAR",
                        x,
                        y + height + 49,
                        width,
                        REAR_COLOR
                );
            }
        }

        private void drawMarker(
                Graphics2D graphics2D,
                String label,
                int x,
                int y,
                int width,
                Color color
        ) {
            Font originalFont =
                    graphics2D.getFont();

            graphics2D.setColor(color);

            graphics2D.setFont(
                    originalFont.deriveFont(Font.BOLD)
            );

            int labelWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(label);

            graphics2D.drawString(
                    label,
                    x + (width - labelWidth) / 2,
                    y
            );

            graphics2D.setFont(originalFont);
        }

        private void drawDirectionLabel(
                Graphics2D graphics2D,
                int startX,
                int totalWidth,
                int cellY
        ) {
            Font originalFont =
                    graphics2D.getFont();

            graphics2D.setColor(
                    falseFull
                            ? WARNING_COLOR
                            : TITLE_COLOR
            );

            graphics2D.setFont(
                    originalFont.deriveFont(
                            Font.BOLD,
                            14f
                    )
            );

            String label = falseFull
                    ? "FALSE-FULL: rear reached the final index"
                    : "Enqueue direction: left to right only";

            int textWidth = graphics2D
                    .getFontMetrics()
                    .stringWidth(label);

            graphics2D.drawString(
                    label,
                    startX
                            + (totalWidth - textWidth) / 2,
                    cellY - 62
            );

            graphics2D.setFont(originalFont);

            graphics2D.setStroke(
                    new BasicStroke(2f)
            );

            int arrowY = cellY - 38;

            graphics2D.drawLine(
                    startX,
                    arrowY,
                    startX + totalWidth,
                    arrowY
            );

            graphics2D.drawLine(
                    startX + totalWidth,
                    arrowY,
                    startX + totalWidth - 10,
                    arrowY - 6
            );

            graphics2D.drawLine(
                    startX + totalWidth,
                    arrowY,
                    startX + totalWidth - 10,
                    arrowY + 6
            );
        }

        private void drawInitialMessage(
                Graphics2D graphics2D
        ) {
            String message =
                    "Choose a capacity and create the linear queue.";

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
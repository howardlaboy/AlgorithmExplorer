package ug.ucu.algorithmexplorer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ug.ucu.algorithmexplorer.controllers.DataStructuresController;

/**
 * Main interface for the Data Structures Lab.
 *
 * Each required structure appears in its own tab while sharing one
 * controller instance.
 */
public class DataStructuresPanel extends JPanel {

    private static final Color TITLE_COLOR = new Color(30, 74, 120);

    private final DataStructuresController controller;

    public DataStructuresPanel() {
        controller = new DataStructuresController();

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
                "Stack",
                new StackLabPanel(controller)
        );

        tabbedPane.addTab(
                "Linear Queue",
                new LinearQueueLabPanel(controller)
        );

        tabbedPane.addTab(
                "Circular Queue",
                new CircularQueueLabPanel(controller)
        );

        tabbedPane.addTab(
                "Singly Linked List",
                new SinglyLinkedListLabPanel(controller)
        );

        tabbedPane.addTab(
                "Doubly Linked List",
                new DoublyLinkedListLabPanel(controller)
        );

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeadingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 15, 0)
        );

        JLabel heading = new JLabel("Data Structures Lab");
        heading.setForeground(TITLE_COLOR);
        heading.setFont(
                heading.getFont().deriveFont(Font.BOLD, 25f)
        );

        JLabel description = new JLabel(
                "Create, operate and visualise custom data structures."
        );
        description.setFont(
                description.getFont().deriveFont(Font.PLAIN, 14f)
        );

        panel.add(heading, BorderLayout.NORTH);
        panel.add(description, BorderLayout.SOUTH);

        return panel;
    }

    
}
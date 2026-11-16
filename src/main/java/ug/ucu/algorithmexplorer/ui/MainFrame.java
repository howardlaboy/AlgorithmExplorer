package ug.ucu.algorithmexplorer.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Main application window for Algorithm Explorer.
 *
 * This frame provides navigation to the four required project modules.
 * Algorithm implementations will remain in the backend packages.
 */
public class MainFrame extends JFrame {

    private static final String HOME_CARD = "HOME";
    private static final String RECURSION_CARD = "RECURSION";
    private static final String DATA_STRUCTURES_CARD = "DATA_STRUCTURES";
    private static final String SORTING_CARD = "SORTING";
    private static final String COMPLEXITY_CARD = "COMPLEXITY";

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public MainFrame() {
        super("Algorithm Explorer");

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        configureWindow();
        buildInterface();
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createNavigationPanel(), BorderLayout.WEST);

        contentPanel.add(createHomePanel(), HOME_CARD);
        contentPanel.add(
                new RecursionPanel(),
                RECURSION_CARD
        );
        contentPanel.add(
                new DataStructuresPanel(),
                DATA_STRUCTURES_CARD
        );
        contentPanel.add(
                new SortingLabPanel(),
                SORTING_CARD
        );
        contentPanel.add(
                new ComplexityAnalysisPanel(),
                COMPLEXITY_CARD
        );

        add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, HOME_CARD);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 74, 120));
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(16, 22, 16, 22)
        );

        JLabel titleLabel = new JLabel("Algorithm Explorer");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(
                titleLabel.getFont().deriveFont(Font.BOLD, 26f)
        );

        JLabel courseLabel = new JLabel(
                "CSC2107 - Design and Analysis of Algorithms"
        );
        courseLabel.setForeground(new Color(225, 235, 245));
        courseLabel.setFont(
                courseLabel.getFont().deriveFont(Font.PLAIN, 14f)
        );

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(courseLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        navigationPanel.setPreferredSize(new Dimension(220, 0));
        navigationPanel.setBorder(
                BorderFactory.createEmptyBorder(18, 12, 18, 12)
        );
        navigationPanel.setBackground(new Color(238, 242, 246));

        navigationPanel.add(
                createNavigationButton("Home", HOME_CARD)
        );
        navigationPanel.add(
                createNavigationButton("Recursion Lab", RECURSION_CARD)
        );
        navigationPanel.add(
                createNavigationButton(
                        "Data Structures Lab",
                        DATA_STRUCTURES_CARD
                )
        );
        navigationPanel.add(
                createNavigationButton("Sorting Lab", SORTING_CARD)
        );
        navigationPanel.add(
                createNavigationButton(
                        "Complexity Analysis",
                        COMPLEXITY_CARD
                )
        );

        return navigationPanel;
    }

    private JButton createNavigationButton(
            String buttonText,
            String cardName
    ) {
        JButton button = new JButton(buttonText);
        button.setFocusPainted(false);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));
        button.addActionListener(
                event -> cardLayout.show(contentPanel, cardName)
        );

        return button;
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBorder(
                BorderFactory.createEmptyBorder(50, 50, 50, 50)
        );

        JLabel welcomeLabel = new JLabel(
                "<html><div style='text-align: center;'>"
                        + "<h1>Welcome to Algorithm Explorer</h1>"
                        + "<p>Select a module from the navigation panel.</p>"
                        + "<p>Algorithms will run in the backend while "
                        + "their results and steps are displayed here.</p>"
                        + "</div></html>",
                SwingConstants.CENTER
        );

        welcomeLabel.setFont(
                welcomeLabel.getFont().deriveFont(Font.PLAIN, 17f)
        );

        homePanel.add(welcomeLabel, BorderLayout.CENTER);

        return homePanel;
    }


}
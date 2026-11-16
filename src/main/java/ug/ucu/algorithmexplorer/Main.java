package ug.ucu.algorithmexplorer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ug.ucu.algorithmexplorer.ui.MainFrame;

/**
 * Entry point for the Algorithm Explorer application.
 *
 * The Swing interface is created on the Event Dispatch Thread
 * to keep Swing component creation thread-safe.
 */
public final class Main {

    private Main() {
        // Prevent creation of Main objects.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            useSystemLookAndFeel();

            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    private static void useSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception exception) {
            System.err.println(
                    "System look and feel could not be loaded. "
                    + "Using the default Swing appearance."
            );
        }
    }
}

package main.java.fr.ynov.ortalab.gui.frames;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;

/**
 * A frame displaying the game tutorial with basic gameplay instructions.
 * Provides new players with information about game mechanics and objectives.
 */
public class TutorialFrame extends JFrame {

    /**
     * Factory method to create and display the tutorial frame.
     */
    public static void showTutorial() {
        SwingUtilities.invokeLater(() -> {
            TutorialFrame tutorialFrame = new TutorialFrame();
            tutorialFrame.setVisible(true);
        });
    }

    /**
     * Constructs a new tutorial frame with game instructions.
     */
    public TutorialFrame() {
        // Set up the frame
        setTitle("Ortalab - Tutorial");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the main panel with tutorial content
        JPanel tutorialPanel = new JPanel();
        tutorialPanel.setLayout(new BoxLayout(tutorialPanel, BoxLayout.Y_AXIS));
        tutorialPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tutorial title
        JTextArea titleArea = new JTextArea("Ortalab");
        titleArea.setFont(new Font("Arial", Font.BOLD, 20));
        titleArea.setEditable(false);
        titleArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titleArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tutorial content
        final JTextArea tutorialTextArea = createTutorialTextArea();

        // Add components to panel
        tutorialPanel.add(titleArea);
        tutorialPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tutorialPanel.add(new JScrollPane(tutorialTextArea));

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());

        tutorialPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tutorialPanel.add(closeButton);

        // Add panel to frame
        add(tutorialPanel);
    }

    /**
     * Creates the text area containing the tutorial content.
     *
     * @return A configured JTextArea with tutorial text
     */
    private static JTextArea createTutorialTextArea() {
        JTextArea tutorialTextArea = new JTextArea(
                """
                         Welcome to Ortalab, a card-based combat game!
                        \s
                         Game objective:
                         - Defeat enemies by forming the best poker hands
                         - Progress through different levels
                        \s
                         Game mechanics :
                         1. Starting the fight
                            - You start with 8 cards
                            - Select 5 cards to form your hand
                        \s
                         2. Discard
                            - You have a maximum of 3 discards (per enemy).
                            - Use them to optimize your hand
                            - Strategically choose which cards to discard
                        \s
                         3. Hand evaluation
                            - Poker combinations influence damage
                            - The stronger your hand, the higher the damage
                            - If you're playing a hand that doesn't require 5 cards, you can add garbage cards. They won't count towards damage, but will be discarded for free.
                        \s
                         4. Combat
                            - Inflict damage on the enemy with your hand
                            - Enemy strikes back at regular intervals
                        \s
                         5. Progress
                            - Earn gold in battle
                            - Buy items in the stores
                            - Reach the final boss!
                        
                        """
        );
        tutorialTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        tutorialTextArea.setEditable(false);
        tutorialTextArea.setLineWrap(true);
        tutorialTextArea.setWrapStyleWord(true);
        return tutorialTextArea;
    }
}
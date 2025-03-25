package main.java.fr.ynov.ortalab.gui.panels;

import javax.swing.*;
import java.awt.*;

public class SortButtonPanel extends JPanel {
    private final HandPanel handPanel;
    private final JButton sortByValueButton;
    private final JButton sortBySuitButton;

    public SortButtonPanel(HandPanel handPanel) {
        this.handPanel = handPanel;

        // Set layout to vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create buttons
        sortByValueButton = createSortByValueButton();
        sortBySuitButton = createSortBySuitButton();

        // Add some vertical spacing between buttons
        add(Box.createVerticalStrut(20));
        add(sortByValueButton);
        add(Box.createVerticalStrut(10));
        add(sortBySuitButton);

        // Adjust panel width
        setPreferredSize(new Dimension(120, getPreferredSize().height));
    }

    private JButton createSortByValueButton() {
        JButton button = new JButton("Valeur");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            handPanel.sortHand(HandPanel.SortType.VALUE);
            updateSortButtonAppearance(true);
        });
        return button;
    }

    private JButton createSortBySuitButton() {
        JButton button = new JButton("Couleur");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            handPanel.sortHand(HandPanel.SortType.SUIT);
            updateSortButtonAppearance(false);
        });
        return button;
    }

    private void updateSortButtonAppearance(boolean isValueSort) {
        sortByValueButton.setBackground(isValueSort ? Color.LIGHT_GRAY : null);
        sortBySuitButton.setBackground(isValueSort ? null : Color.LIGHT_GRAY);
    }
}
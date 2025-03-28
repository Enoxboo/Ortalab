package main.java.fr.ynov.ortalab.gui.frames;

import javax.swing.*;
import java.awt.*;

public class TutorialFrame extends JFrame {
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

        // Tutorial text areas
        JTextArea titleArea = new JTextArea("Ortalab - Jeu de Cartes et de Combat");
        titleArea.setFont(new Font("Arial", Font.BOLD, 20));
        titleArea.setEditable(false);
        titleArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titleArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea tutorialTextArea = new JTextArea(
                "Bienvenue dans Ortalab, un jeu de combat par cartes !\n\n" +
                        "Objectif du Jeu :\n" +
                        "- Vaincre des ennemis en formant les meilleures mains de poker\n" +
                        "- Progresser à travers différents niveaux\n\n" +
                        "Mécaniques de Jeu :\n" +
                        "1. Début du Combat\n" +
                        "   - Vous commencez avec 8 cartes\n" +
                        "   - Sélectionnez 5 cartes pour former votre main\n\n" +
                        "2. Défausse\n" +
                        "   - Vous avez 3 défausses maximum (par ennemis)\n" +
                        "   - Utilisez-les pour optimiser votre main\n" +
                        "   - Choisissez stratégiquement quelles cartes défausser\n\n" +
                        "3. Évaluation de la Main\n" +
                        "   - Les combinaisons de poker influencent les dégâts\n" +
                        "   - Plus votre main est forte, plus les dégâts sont élevés\n" +
                        "   - Si vous jouez une main qui ne nécessite pas 5 cartes, vous pouvez rajouter des cartes poubelles, elles ne conteront pas dans le calcul de dégâts mais elle seront défaussées gratuitement\n\n" +
                        "4. Combat\n" +
                        "   - Infligez des dégâts à l'ennemi avec votre main\n" +
                        "   - L'ennemi riposte à intervalles réguliers\n\n" +
                        "5. Progression\n" +
                        "   - Gagnez de l'or en combattant\n" +
                        "   - Achetez des objets dans les boutiques\n" +
                        "   - Atteignez le boss final !\n\n"
        );
        tutorialTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        tutorialTextArea.setEditable(false);
        tutorialTextArea.setLineWrap(true);
        tutorialTextArea.setWrapStyleWord(true);

        // Add components to panel
        tutorialPanel.add(titleArea);
        tutorialPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tutorialPanel.add(new JScrollPane(tutorialTextArea));

        // Add close button
        JButton closeButton = new JButton("Fermer");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());

        tutorialPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tutorialPanel.add(closeButton);

        // Add panel to frame
        add(tutorialPanel);
    }

    public static void showTutorial() {
        SwingUtilities.invokeLater(() -> {
            TutorialFrame tutorialFrame = new TutorialFrame();
            tutorialFrame.setVisible(true);
        });
    }
}
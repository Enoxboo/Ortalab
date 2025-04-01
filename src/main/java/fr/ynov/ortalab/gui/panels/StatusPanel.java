package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;

import javax.swing.JTextArea;
import java.awt.Font;


public class StatusPanel extends JTextArea {

    private final boolean isPlayerPanel;

    public StatusPanel(Player player, Enemy enemy, int currentLevel, boolean isPlayerPanel) {
        this.isPlayerPanel = isPlayerPanel;

        // Configure text area
        setEditable(false);
        setRows(5);
        setColumns(40);
        setFont(new Font("Monospaced", Font.PLAIN, 26));

        // Initial update
        updateStatus(player, enemy, currentLevel);
    }

    public void updateStatus(Player player, Enemy enemy, int currentLevel) {
        StringBuilder statusTextBuilder = new StringBuilder();

        if (isPlayerPanel) {
            // Player status details
            statusTextBuilder.append(
                    String.format(
                            """
                                    --- Player Status ---
                                    HP: %d/%d
                                    Discards Left: %d
                                    Gold: %d
                                    """,
                            player.getHealthPoints(), player.getMaxHealthPoints(),
                            player.getRemainingDiscards(),
                            player.getGold()
                    )
            );
        } else {
            // Enemy status details
            statusTextBuilder.append(
                    String.format(
                            """
                                    --- Enemy Status ---
                                    Level: %d
                                    HP: %d
                                    Attack Damage: %d
                                    Attack Cooldown: %d
                                    """,
                            currentLevel,
                            enemy.getHealthPoints(),
                            enemy.getAttackDamage(),
                            enemy.getCurrentCooldown()
                    )
            );
        }

        setText(statusTextBuilder.toString());
    }
}
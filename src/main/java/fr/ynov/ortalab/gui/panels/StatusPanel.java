package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JTextArea {
    private Player player;
    private Enemy enemy;
    private int currentLevel;
    private boolean isPlayerPanel;

    public StatusPanel(Player player, Enemy enemy, int currentLevel, boolean isPlayerPanel) {
        this.player = player;
        this.enemy = enemy;
        this.currentLevel = currentLevel;
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
        this.player = player;
        this.enemy = enemy;
        this.currentLevel = currentLevel;

        // Prepare the base status text
        StringBuilder statusTextBuilder = new StringBuilder();

        if (isPlayerPanel) {
            // Player status details
            statusTextBuilder.append(
                    String.format(
                            "--- Player Status ---\n" +
                                    "HP: %d/%d\n" +
                                    "Discards Left: %d\n" +
                                    "Gold: %d\n",
                            player.getHealthPoints(), player.getMaxHealthPoints(),
                            player.getRemainingDiscards(),
                            player.getGold()
                    )
            );
        } else {
            // Enemy status details
            statusTextBuilder.append(
                    String.format(
                            "--- Enemy Status ---\n" +
                                    "Level: %d\n" +
                                    "HP: %d\n" +
                                    "Attack Damage: %d\n" +
                                    "Attack Cooldown: %d\n",
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
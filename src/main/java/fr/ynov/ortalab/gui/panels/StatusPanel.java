package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JTextArea {
    private Player player;
    private Enemy enemy;
    private int currentLevel;

    public StatusPanel(Player player, Enemy enemy, int currentLevel) {
        this.player = player;
        this.enemy = enemy;
        this.currentLevel = currentLevel;

        // Configure text area
        setEditable(false);
        setRows(5);
        setColumns(40);
        setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Initial update
        updateStatus(player, enemy, currentLevel);
    }

    public void updateStatus(Player player, Enemy enemy, int currentLevel) {
        this.player = player;
        this.enemy = enemy;
        this.currentLevel = currentLevel;

        // Format status text
        String statusText = String.format(
                "--- Player Status ---\n" +
                        "HP: %d/%d\n" +
                        "Discards Left: %d\n" +
                        "Gold: %d\n\n" +
                        "--- Enemy Status ---\n" +
                        "Level: %d\n" +
                        "HP: %d\n" +
                        "Attack Damage: %d\n" +
                        "Attack Cooldown: %d",
                player.getHealthPoints(), player.getMaxHealthPoints(),
                player.getRemainingDiscards(),
                player.getGold(),
                currentLevel,
                enemy.getHealthPoints(),
                enemy.getAttackDamage(),
                enemy.getCurrentCooldown()
        );

        setText(statusText);
    }
}
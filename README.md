# Ortalab demo - Poker Roguelike

## Description
Ortalab is a roguelike card-based game developed in Java with a graphical interface. The gameplay is inspired by poker mechanics, where players use their hands to deal damage to enemies while progressing through different encounters.
### Requirements
- Java JDK 17 ou supérieur
- Gradle 7.x ou supérieur

## Installation
1. Clone repository
```bash
  git clone https://github.com/Enoxboo/Ortalab.git
```    

2. Open project

- run ApplicationRunner class in the main directory

## Game loop
- The player starts a battle.
- They draw 8 cards.
- They select 5 cards to form a hand.
- They can discard and redraw up to 3 times to optimize their hand.
- The game evaluates the poker hand and applies damage.
- Unused cards are automatically discarded.
- The enemy attacks if possible.
- The battle continues until the player or the enemy is defeated.
- If the player wins, they receive gold and can advance on the map.
- They may encounter a shop (to purchase items) or a healing zone.
- Progression continues up to the final boss.

## Game Components

### Cards

- Value (2 to Ace).
- Suit (Clubs, Hearts, Diamonds, Spades).
- Display function for the graphical interface.

### Poker Hands

- Determines the best combination among the 5 chosen cards.
- The stronger the hand, the higher the damage.
- Higher card values increase damage output.

### Discard & Draw

- Maximum of 3 discards.
- Manually select cards to discard.
- Draw new cards after discarding.

### Enemies

- Health Points (HP).
- Attack power (damage dealt).
- Attack cooldown (every X turns).

### Items & Shop

- Buyable items with permanent effects (e.g., +1 discard, +10% damage for Heart cards).
- Gold-based economy.
- Maximum of 6 items, with the option to sell.

### Game Map

- Alternates between battles and shops.
- Final boss after 5 normal battles.

### Tutorial

- Can press the tutorial button to have a more detailed tutorial
- Click on cards, up to 5, chose to discard them or play them
- You can switch between the value sorting, and the color one

### Contributors
- Matteo Marquant
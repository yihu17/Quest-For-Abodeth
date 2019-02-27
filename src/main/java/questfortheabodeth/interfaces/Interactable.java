package main.java.questfortheabodeth.interfaces;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;

/**
 * Provides methods for objects to interact with players and enemies
 */
public interface Interactable extends Collidable
{
    /**
     * Apply an effect to the player
     *
     * @param p (Player) The player object
     */
    void interact(Player p);

    /**
     * Remove the aforementioned effect from the player
     * @param p (Player) The player object
     */
    void remove(Player p);

    void buffEnemy(Enemy e);

    void removeEnemyBuff(Enemy e);
}

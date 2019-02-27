package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

/**
 * Creates a new Water environment object that slows down the player
 * when they pass over it
 */
public class Water extends InteractableEnvironment
{
    /**
     * Creates a new Water object
     *
     * @param xPos          (int) X position of the water
     * @param yPos          (int) Y position of the water
     * @param imageFilePath (String) Image file to load
     */
    public Water(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
    }

    /**
     * When the player passes over the water, reduce their movement
     * speed to half
     * @param p (Player) The player object
     */
    @Override
    public void interact(Player p)
    {
        // Ensure the interact hasn't already been applied
        if (p.applyInteract(this)) {
            p.setMovementSpeed(p.getMovementSpeed() / 2);
        }

        // Player the water sound effect if they are enabled
        if (Settings.SOUND_EFFECTS_ON && System.currentTimeMillis() - super.getLastAudioTrigger() >= Helper.getLengthOfAudioFile("water")) {
            Helper.playAudio("water");
            super.setLastAudioTrigger(System.currentTimeMillis());
        }
    }

    /**
     * Reset the players movement speed when they leave the water tile
     * @param p (Player) The player object
     */
    @Override
    public void remove(Player p)
    {
        p.setMovementSpeed(p.getMovementSpeed() * 2);
    }

    /**
     * Double the speed of the enemy this interact effects
     * @param e (Enemy) The enemy
     */
    @Override
    public void buffEnemy(Enemy e)
    {
        if (e.applyInteract(this)) {
            System.out.println(e.getName());
            e.setMovementSpeed(e.getMovementSpeed() * 2);
        }
    }

    /**
     * Sets the enemy speed back to its original value once they
     * leave the tile
     * @param e (Enemy) The enemy
     */
    @Override
    public void removeEnemyBuff(Enemy e)
    {
        if (e.getEnemyName().equals("crocodile")) {
            System.out.println(e.getName());
            e.setMovementSpeed(e.getMovementSpeed() / 2);
        }
    }
}

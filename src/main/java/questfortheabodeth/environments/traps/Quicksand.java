package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

/**
 * Quicksand class. This environment object slows down the player
 * when they pass over it.
 */
public class Quicksand extends InteractableEnvironment
{
    /**
     * Creates a new quicksand environment object
     *
     * @param xPos          (int) X position of the quicksand
     * @param yPos          (int) Y position of the quicksand
     * @param imageFilePath (String) Image file to load
     */
    public Quicksand(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
    }

    /**
     * When the player passes over the quicksand, half the players current
     * movement speed
     * @param p (Player) The player object
     */
    @Override
    public void interact(Player p)
    {
        // If the interact isn't already applied, apply it
        if (p.applyInteract(this)) {
            p.setMovementSpeed(p.getMovementSpeed() / 2);
        }

        // If sound effects are enabled play the quicksand sound
        if (Settings.SOUND_EFFECTS_ON && System.currentTimeMillis() - super.getLastAudioTrigger() >= Helper.getLengthOfAudioFile("quicksand")) {
            Helper.playAudio("quicksand");
            super.setLastAudioTrigger(System.currentTimeMillis());
        }
    }

    /**
     * When the player exits the quicksand tile, remove the speed debuff
     * @param p (Player) The player object
     */
    @Override
    public void remove(Player p)
    {
        p.setMovementSpeed(p.getMovementSpeed() * 2);
    }

    /**
     * Quicksand has no effect on enemies
     * @param e (Enemy) The enemy object
     */
    @Override
    public void buffEnemy(Enemy e)
    {

    }

    /**
     * Quicksand has no effect on enemies
     * @param e (Enemy) The enemy object
     */
    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }
}
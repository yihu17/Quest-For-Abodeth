package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

/**
 * THe lava class damages the player as they pass over it
 */
public class Lava extends InteractableEnvironment{
    private int damageSpeed = 1000;

    /**
     * Creates a new Lava object in the environment
     *
     * @param xPos          (int) X position of the lava
     * @param yPos          (int) Y position of the lava
     * @param imageFilePath (String) Image file to load
     */
    public Lava (int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
    }

    /**
     * When the player is over the lava, damage the player for 20 health
     * @param p (Player) The player object
     */
    @Override
    public void interact(Player p)
    {
        // Check for the last time the player was hit so they don't instantly die
        if (System.currentTimeMillis() - p.getLastTimeHit() >= damageSpeed) {
            p.decreaseHealth(20);
            p.setLastTimeHit(System.currentTimeMillis());
        }

        // Play a burning sound if sounds are enabled
        if (Settings.SOUND_EFFECTS_ON && System.currentTimeMillis() - super.getLastAudioTrigger() >= Helper.getLengthOfAudioFile("lava")) {
            Helper.playAudio("lava");
            super.setLastAudioTrigger(System.currentTimeMillis());
        }
    }

    /**
     * Does nothing as the lava applies no lasting effects
     * @param p (Player) The player object
     */
    @Override
    public void remove(Player p)
    {

    }

    /**
     * By default the lava does no damage to enemies as they cannot avoid it
     * @param e (Enemy) The enemy
     */
    @Override
    public void buffEnemy(Enemy e)
    {

    }

    /**
     * By default the lava does no damage to enemies as they cannot avoid it
     * @param e (Enemy) The enemy
     */
    @Override
    public void removeEnemyBuff(Enemy e) {

    }
}

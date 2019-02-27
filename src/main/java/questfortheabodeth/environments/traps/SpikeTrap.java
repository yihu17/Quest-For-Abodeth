package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

/**
 * The spike trap class damage the player for a large amount of health
 * if they pass over it.
 */
public class SpikeTrap extends InteractableEnvironment
{
    private boolean activated = false;

    /**
     * Creates a new SpikeTrap envionment object
     *
     * @param xPos          (int) X position of the spiketrap
     * @param yPos          (int) Y position of the spiketrap
     * @param imageFilePath (String) Image file to load
     */
    public SpikeTrap(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
    }

    /**
     * Deals a large amount of damage to the player when they pass over it.
     * Changes the traps image so that it is shown its been sprung
     * @param p (Player) The player object
     */
    @Override
    public void interact(Player p)
    {
        if (p.applyInteract(this) && !activated) {
            p.decreaseHealth(50);
            loadImageFromFile("res/assets/environment/spikeTrap2.png");
            activated = true;
        }
    }

    /**
     * Resets te spike trap to its original state
     */
    public void reset()
    {
        activated = false;
        loadImageFromFile("res/assets/environment/spikeTrap.png");
    }

    /**
     * Returns a boolean values as to whether or not the trap has already activated
     * @return (boolean) True if the trap is activated or false otherwise
     */
    public boolean isActivated()
    {
        return activated;
    }

    /**
     * The spike trap is a one time effect and so has no remove method
     * @param p (Player) The player object
     */
    @Override
    public void remove(Player p)
    {

    }

    /**
     * SpikeTraps have no effect on enemies
     * @param e (Enemy) The enemy object
     */
    @Override
    public void buffEnemy(Enemy e)
    {

    }

    /**
     * SpikeTraps have no effect on enemies
     * @param e (Enemy) The enemy object
     */
    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }
}

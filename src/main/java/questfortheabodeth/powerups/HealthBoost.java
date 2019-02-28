package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

/**
 * Health pickups increase the players current health
 * up to a maximum of 100 points
 */
public class HealthBoost extends Pickup
{
    private int healthBoostage = 50;

    /**
     * Creates a new health booster
     *
     * @param x (int) X position of the health boost
     * @param y (int) Y position of the health boost
     */
    public HealthBoost(int x, int y)
    {
        super(x, y, "res/assets/pickups/healthPickup.png");
    }

    /**
     * Adds an extra {@code healthBoostage} to the players current health
     * @param p (Player) The player object
     */
    @Override
    public void applyBuff(Player p)
    {
        p.increaseHealth(healthBoostage);
    }


    /**
     * The health boost provides no lasting effects and so by default this
     * function does nothing
     * @param p (Player) The player object
     */
    @Override
    public void removeBuff(Player p)
    {
    }
}
package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

/**
 * Adds a shield to the player. A shield is fully depleted
 * before any of the players health is actually depleted
 */
public class ShieldPickup extends Pickup
{
    private int shieldHealth = 50;

    /**
     * Creates a new shield pickup
     *
     * @param x (int) X position of this shield pickup
     * @param y (int) Y position of this shield pickup
     */
    public ShieldPickup(int x, int y)
    {
        super(x, y, "res/assets/pickups/shieldPickup.png");
    }

    /**
     * Adds the shield amount to the player
     * @param p (Player) The player object
     */
    @Override
    public void applyBuff(Player p)
    {
        System.out.println("Getting buffed by shield");
        p.addShield(shieldHealth);
        System.out.println("Shield is now " + p.getShield());
    }

    /**
     * Shields last until they are depleted and so this function does nothing
     * @param p (Player) The player object
     */
    @Override
    public void removeBuff(Player p)
    {

    }

}
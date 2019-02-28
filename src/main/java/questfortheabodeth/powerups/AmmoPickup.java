package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

/**
 * Ammo pickups give the player extra ammo for all the ammo
 * pools that they currently hold. Ammo is increased for each gun
 * by the amount set in this class
 */
public class AmmoPickup extends Pickup
{
    private int ammo = 50;

    /**
     * Creates a new AmmoPickup
     *
     * @param x (int) X position of the ammo box
     * @param y (int) Y position of the ammo box
     */
    public AmmoPickup(int x, int y)
    {
        super(x, y, "res/assets/pickups/ammoPickup.png");
    }


    /**
     * When the buff is applied the players ammo is increased
     * by the amount set in this class
     * @param p (Player) The player object
     */
    @Override
    public void applyBuff(Player p)
    {
        p.increaseAmmo(ammo);
    }

    /**
     * Does nothing by default as the ammo pickup has no
     * lasting effects that would need to be removed
     * @param p (Player) The player object
     */
    @Override
    public void removeBuff(Player p)
    {

    }
}
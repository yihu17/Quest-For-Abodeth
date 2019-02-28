package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

/**
 * Adds a shield to the player. A shield is fully depleted
 * before any of the players health is actually depleted
 */
public class ShieldPickup extends Pickup implements Runnable
{
    private int timeout;
    private Player p;

    /**
     * Creates a new shield pickup
     *
     * @param x (int) X position of this shield pickup
     * @param y (int) Y position of this shield pickup
     */
    public ShieldPickup(int x, int y, int timeout)
    {
        super(x, y, "res/assets/pickups/shieldPickup.png");
        this.timeout = timeout;
    }

    /**
     * Adds the shield amount to the player
     * @param p (Player) The player object
     */
    @Override
    public void applyBuff(Player p)
    {
        this.p = p;
        p.activateShield();
        new Thread(this).start();
    }

    /**
     * Shields last until they are depleted and so this function does nothing
     * @param p (Player) The player object
     */
    @Override
    public void removeBuff(Player p)
    {
        p.deactivateShield();
    }

    /**
     * Waits for a set amount of time when the thread is
     * run and then removes the shield boost
     */
    @Override
    public void run()
    {
        p.addCurrentPowerup("shieldPickup");
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            p.removeCurrentPowerup("shieldPickup");
            removeBuff(p);
        }
    }
}
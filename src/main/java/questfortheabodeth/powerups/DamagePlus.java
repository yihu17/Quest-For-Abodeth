package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

/**
 * A pickup that increases the amount of damage the player
 * does for a set amount of time. Once this is picked up
 * a new thread is created with the sole function of sleeping
 * and calling the removeBuff function.
 *
 * @see Thread
 * @see Runnable
 */
public class DamagePlus extends Pickup implements Runnable
{
    private int damageBoost = 10;
    private int timeout;
    private Player p;

    /**
     * Creates a new damage plus pickup
     * @param x (int) X position of the pickup
     * @param y (int) Y position of the pickup
     * @param timeout (int) Amount of time the buff lasts
     */
    public DamagePlus(int x, int y, int timeout)
    {
        super(x, y, "res/assets/pickups/damagePickup.png");
        this.timeout = timeout;
    }

    /**
     * Applies the buff to the player
     * @param p (Player) The player object
     */
    @Override
    public void applyBuff(Player p)
    {
        this.p = p;
        p.addDamage(damageBoost);
        new Thread(this).start();
    }

    /**
     * Remove the damage increase from the player
     * @param p (Player) The player object
     */
    @Override
    public void removeBuff(Player p)
    {
        p.addDamage(damageBoost * -1);
        System.out.println("Damage is now " + p.getAdditionalDamage());
    }

    /**
     * ONce the player has pickup up this powerup this runnable
     * runs in a newly created thread. It sleeps for a set amount
     * of time and then removes the increased damage
     */
    @Override
    public void run()
    {
        p.addCurrentPowerup("damagePickup");
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            p.removeCurrentPowerup("damagePickup");
            removeBuff(p);
        }
    }
}

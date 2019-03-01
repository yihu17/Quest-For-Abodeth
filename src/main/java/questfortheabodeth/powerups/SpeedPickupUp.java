package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;

/**
 * Increases the players speed for a set amount of time
 * and automatically changes it back after the time period
 * has passed
 */
public class SpeedPickupUp extends Pickup implements Runnable
{
    private int timeout;
    private Player p;

    /**
     * Creates a new speed boost pickup
     *
     * @param x       (int) X position of this pickup
     * @param y       (int) Y position of this pickup
     * @param timeout (int) Number of milliseconds this boost is applied for
     */
    public SpeedPickupUp(int x, int y, int timeout)
    {
        super(x, y, "res/assets/pickups/speedPickup.png");
        this.timeout = timeout;
    }

    /**
     * Applies the speed boost to the player and starts the thread
     * that will eventually remove the buff
     * @param p (Player) The player object
     */
    @Override
    public void applyBuff(Player p)
    {
        this.p = p;
        System.out.println("Getting buffed by speed boost");
        p.setMovementSpeed(7);
        System.out.println("Speed is now " + p.getMovementSpeed());
        new Thread(this).start();
    }

    /**
     * Sets the player speed back to its original value
     * @param p (Player) The player object
     */
    @Override
    public void removeBuff(Player p)
    {
        p.setMovementSpeed(Settings.PLAYER_SPEED);
    }

    /**
     * Waits for a set amount of time when the thread is
     * run and then removes the speed boost
     */
    @Override
    public void run()
    {
        p.addCurrentPowerup("speedPickup");
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            p.removeCurrentPowerup("speedPickup");
            removeBuff(p);
        }
    }
}
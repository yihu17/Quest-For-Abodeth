package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class SpeedPickupUp extends Pickup
{
    private int timeout;
    private Player p;

    public SpeedPickupUp(int x, int y, int timeout)
    {
        super(x, y, "res/assets/pickups/speedPickup.png");
        this.timeout = timeout;
    }

    //function for when picked up/ used:
    @Override
    public void applyBuff(Player p)
    {
        this.p = p;
        System.out.println("Getting buffed by speed boost");
        p.setMovementSpeed(10);
        System.out.println("Speed is now " + p.getMovementSpeed());
        new Thread(this).start();
    }

    @Override
    public void removeBuff(Player p)
    {
        p.setMovementSpeed(6);
    }

    public void run()
    {
        p.addCurrentPowerup("speedPickup");
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        }
        p.removeCurrentPowerup("speedPickup");
        removeBuff(p);
    }
}